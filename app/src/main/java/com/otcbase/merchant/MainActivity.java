package com.otcbase.merchant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.otcbase.merchant.utils.FileUtils;
import com.otcbase.merchant.utils.ImagePickerAgent;
import com.otcbase.merchant.utils.LogUtils;
import com.otcbase.merchant.utils.PermissionUtils;
import com.otcbase.merchant.utils.TakeCaptureAgent;
import com.otcbase.merchant.view.MyWebView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.iv_go_forward)
    ImageView ivGoForward;
    @BindView(R.id.iv_reload)
    ImageView ivReload;


    @BindView(R.id.otc_webView)
    @Nullable
    MyWebView wvOTC;


    private ImagePickerAgent imagePickerAgent;
    private TakeCaptureAgent takeCaptureAgent;

    private AnimationSet animationSet = new AnimationSet(true);
    @OnClick({R.id.iv_go_back, R.id.iv_go_forward, R.id.iv_reload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
//                if (wvOTC != null && wvOTC.canGoBack()){
                    wvOTC.goBack();
//                }
                break;
            case R.id.iv_go_forward:
                if (wvOTC != null && wvOTC.canGoForward()){
                    wvOTC.goForward();
                }
                break;
            case R.id.iv_reload:
                wvOTC.reload();
                ivReload.startAnimation(animationSet);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(500);
        animationSet.addAnimation(rotateAnimation);

        ivGoBack.setEnabled(false);
        ivGoForward.setEnabled(false);
        wvOTC.setOpenFileChooser(new MyWebView.IOpenFileChooser() {
            private boolean checkAcceptTypeImage(String[] acceptTypes) {
                if (acceptTypes != null && acceptTypes.length > 0) {
                    String arrayStr = Arrays.toString(acceptTypes);
                    if (arrayStr.contains("image/")) {
                        return true;
                    }
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


                    filePathCallback.onReceiveValue(null);

                } else {
                    if (!PermissionUtils.checkWriteExternalStorage(baseActivity)) {
                        filePathCallback.onReceiveValue(null);
                        return;
                    }
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);

                    // do not use accept type from h5 , may occur issue
                    i.setType("*/*");

//                        if (acceptTypes.length > 0) {
//                            i.setType(acceptTypes[0]);
//                        } else {
//                            i.setType("*/*");
//                        }

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

        wvOTC.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                LogUtils.d(" onPageCommitVisible : " + url);
                if (isFinishing()) {
                    return;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isFinishing()) {
                    String originalUrl = wvOTC.copyBackForwardList().getCurrentItem().getOriginalUrl();
                    boolean isCanGoBack = wvOTC.canGoBack() && !(originalUrl.equals(url) || originalUrl.equals(url + "/index"));
                    ivGoBack.setEnabled(isCanGoBack);
                    ivGoForward.setEnabled(wvOTC.canGoForward());
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        });
        wvOTC.getSettings().setLoadWithOverviewMode(true);
        wvOTC.getSettings().setUseWideViewPort(true);
        wvOTC.getSettings().setJavaScriptEnabled(true);
        wvOTC.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        wvOTC.getSettings().setDomStorageEnabled(true);
        wvOTC.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        wvOTC.getSettings().setAppCachePath(appCachePath);
        wvOTC.getSettings().setAllowFileAccess(true);
        wvOTC.getSettings().setAppCacheEnabled(true);
        wvOTC.setProgressBarColor(getResources().getDrawable(R.drawable.bg_webview_progress_color));
        // 载入内容
        wvOTC.loadUrl("https://www.otcbase.com/zh-CN/mobile/account/sign-in");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvOTC.canGoBack()) {

            wvOTC.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
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
}
