package com.otcbase.merchant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.otcbase.merchant.quickadapter.BaseAdapterHelper;
import com.otcbase.merchant.quickadapter.QuickAdapter;
import com.otcbase.merchant.utils.FileUtils;
import com.otcbase.merchant.utils.ImagePickerAgent;
import com.otcbase.merchant.utils.PermissionUtils;
import com.otcbase.merchant.utils.TakeCaptureAgent;
import com.otcbase.merchant.view.MyWebView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Environment.DIRECTORY_DCIM;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_reload)
    ImageView ivReload;
    @BindView(R.id.iv_customer_service)
    ImageView ivCustomerService;
    @BindView(R.id.otc_webView)
    MyWebView wvOTC;


    private ImagePickerAgent imagePickerAgent;
    private TakeCaptureAgent takeCaptureAgent;

    private AnimationSet animationSet = new AnimationSet(true);

    private boolean isClick = true;
    @OnClick({R.id.iv_reload, R.id.iv_customer_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_customer_service:
                if (wvOTC.getProgressBar().getVisibility() == View.GONE){
                    if (isClick){
                        ivCustomerService.setImageDrawable(getResources().getDrawable(R.drawable.icon_customer_service_close));
                    } else {
                        ivCustomerService.setImageDrawable(getResources().getDrawable(R.drawable.icon_customer_service_open));
                    }
                    isClick = !isClick;
                    wvOTC.loadUrl("javascript:window.toggleChatWidget()");
                }
                break;
            case R.id.iv_reload:
                if (wvOTC != null) {
                    isClick = true;
                    ivCustomerService.setImageDrawable(getResources().getDrawable(R.drawable.icon_customer_service_open));
                    wvOTC.reload();
                }
                ivReload.startAnimation(animationSet);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorCharcoalGrey));//将状态栏设置成透明色
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorCharcoalGrey));//将导航栏设置为透明色
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);
        animationSet.addAnimation(rotateAnimation);

        wvOTC.setOpenFileChooser(new MyWebView.IOpenFileChooser() {
            private boolean checkAcceptTypeImage(String[] acceptTypes) {
                if (acceptTypes != null && acceptTypes.length > 0) {
                    String arrayStr = Arrays.toString(acceptTypes);
                    if (arrayStr.contains("image/")) {

                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onOpenFileChooser(final ValueCallback<Uri> filePathCallback, String[] acceptTypes) {
                final Activity baseActivity = MainActivity.this;
                if (checkAcceptTypeImage(acceptTypes)) {
                    List<String> actions = new ArrayList<>();
                    actions.add(getResources().getString(R.string.user_info_action_album));
                    actions.add(getResources().getString(R.string.user_info_action_camera));
                    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                ImagePickerAgent imagePickerAgent = new ImagePickerAgent(baseActivity, new ImagePickerAgent.OnImagePickerListener() {
                                    @Override
                                    public void onPickImage(Uri imageUri, String imagePath) {
                                        filePathCallback.onReceiveValue(imageUri);
                                    }

                                    @Override
                                    public void onError() {
                                        filePathCallback.onReceiveValue(null);
                                    }
                                });
                                boolean open = showImagePicker(imagePickerAgent);
                                if (!open) {
                                    filePathCallback.onReceiveValue(null);
                                }
                            } else if (position == 1) {
                                final Uri photoUri = FileUtils.getCaptureUri(MainActivity.this);
                                if (photoUri != null) {
                                    boolean result = showCapture(photoUri, new TakeCaptureAgent(baseActivity, new TakeCaptureAgent.OnTakeCaptureResultListener() {
                                        @Override
                                        public void onTakeCaptureResult(int resultCode, @NonNull Intent data) {
                                            filePathCallback.onReceiveValue(photoUri);
                                        }

                                        @Override
                                        public void onTakeCaptureError(int resultCode) {
                                            filePathCallback.onReceiveValue(null);
                                        }
                                    }));
                                    if (!result) {
                                        filePathCallback.onReceiveValue(null);
                                    }
                                } else {
                                    filePathCallback.onReceiveValue(null);
                                }
                            }
                        }
                    };


                    DialogPlus dialogPlus = createActionDialog(baseActivity, actions, onItemClickListener, null, new OnCancelListener() {
                        @Override
                        public void onCancel(@NonNull DialogPlus dialog) {
                            filePathCallback.onReceiveValue(null);
                        }
                    });
                    dialogPlus.show();

                } else {
                    if (!PermissionUtils.checkWriteExternalStorage(baseActivity)) {
                        filePathCallback.onReceiveValue(null);
                        return;
                    }
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    Intent intent = Intent.createChooser(i, getResources().getString(R.string.select));
                    startActivityForResult(intent, new OnActivityResultListener() {
                        @Override
                        public void result(int resultCode, @Nullable Intent data) {
                            if (data != null) {
                                if (resultCode == Activity.RESULT_OK) {
                                    Uri uri = data.getData();
                                    if (uri != null) {
                                        filePathCallback.onReceiveValue(uri);
                                        return;
                                    }
                                }
                            }

                            filePathCallback.onReceiveValue(null);
                        }
                    });
                }
            }
        });

        wvOTC.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult result = wvOTC.getHitTestResult();
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("保存图片到本地");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            saveImage(result.getExtra());
                                        }
                                    }).start();
                                }
                            }).start();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // 自动dismiss
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                return false;
            }
        });

        String strAgent = "otcbase/" + BuildConfig.VERSION_CODE + "/" + BuildConfig.APPLICATION_ID;
        wvOTC.getSettings().setUserAgentString(wvOTC.getSettings().getUserAgentString() + strAgent);
        wvOTC.getSettings().setJavaScriptEnabled(true);
        wvOTC.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // 载入内容
        wvOTC.loadUrl("https://www.otcbase.com/zh-CN/mobile/account/sign-in");
    }

    public void saveImage(String data) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(data);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            if (bm != null) {
                save2Album(bm,new SimpleDateFormat("SXS_yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".jpg");
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }

    private void save2Album(Bitmap bitmap, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (wvOTC != null) {
            wvOTC.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wvOTC.clearHistory();

            ((ViewGroup) wvOTC.getParent()).removeView(wvOTC);
            wvOTC.destroy();
            wvOTC = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvOTC.canGoBack()) {

            wvOTC.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    final public static int REQUEST_CODE_START_FOR_RESULT = 201;


    private OnActivityResultListener mActivityResultListener = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imagePickerAgent != null && imagePickerAgent.handleActivityResult(this, requestCode, resultCode, data)) {
            imagePickerAgent = null;
        }

        if (takeCaptureAgent != null && takeCaptureAgent.handleActivityResult(requestCode, resultCode, data)) {
            takeCaptureAgent = null;
        }

        if (requestCode == REQUEST_CODE_START_FOR_RESULT) {
            if (mActivityResultListener != null) {
                mActivityResultListener.result(resultCode, data);
            }
            mActivityResultListener = null;
        }

    }

    public boolean showImagePicker(ImagePickerAgent imagePickerAgent) {
        this.imagePickerAgent = imagePickerAgent;
        return imagePickerAgent.show();
    }


    public boolean showCapture(Uri uri, TakeCaptureAgent takeCaptureAgent) {
        this.takeCaptureAgent = takeCaptureAgent;
        return takeCaptureAgent.show(uri);
    }

    public interface OnActivityResultListener {
        void result(int resultCode, @Nullable Intent data);
    }

    public void startActivityForResult(Intent intent, OnActivityResultListener listener) {
        startActivityForResult(intent, REQUEST_CODE_START_FOR_RESULT);
        this.mActivityResultListener = listener;
    }

    public DialogPlus createActionDialog(Context context, List<String> actions, final AdapterView.OnItemClickListener itemClickListener,
                                         OnDismissListener dismissListener, final OnCancelListener cancelListener) {
        QuickAdapter adapter = new QuickAdapter<String>(context, R.layout.layout_dialog_action_item, actions) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.lib_tv_dialog_action_title, item);
                helper.setTextColor(R.id.lib_tv_dialog_action_title, getResources().getColor(R.color.colorFont));
            }
        };
        final DialogPlus dialogPlus;
        int margin = wvOTC.dp2px(context, 10);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_dialog_action, null);
        view.setBackgroundColor(Color.TRANSPARENT);

        DialogPlusBuilder builder = DialogPlus.newDialog(context)
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setContentHolder(new ViewHolder(view))
                .setMargin(margin, 0, margin, margin)
                .setCancelable(true)
                .setContentBackgroundResource(android.R.color.transparent)
                .setGravity(Gravity.BOTTOM);
        dialogPlus = builder.create();
        ListView listView = view.findViewById(R.id.lib_lv_dialog_action);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogPlus.dismiss();
                itemClickListener.onItemClick(parent, view, position, id);
            }
        });
        listView.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        TextView viewCancel = view.findViewById(R.id.lib_btn_dialog_action_cancel);
        viewCancel.setTextColor(getResources().getColor(R.color.colorFont));
        viewCancel.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        viewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
                if (cancelListener != null) {
                    cancelListener.onCancel(dialogPlus);
                }
            }
        });
        return dialogPlus;

    }
}
