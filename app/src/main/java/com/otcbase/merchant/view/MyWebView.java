package com.otcbase.merchant.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.otcbase.merchant.R;
import com.otcbase.merchant.utils.LogUtils;


public class MyWebView extends WebView {
    private IOpenFileChooser openFileChooser;
    private ProgressBar progressBar;
    private Context mContext;

    public MyWebView(Context context) {
        super(context);
        init(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOpenFileChooser(IOpenFileChooser openFileChooser) {
        this.openFileChooser = openFileChooser;
    }

    public void setProgressBarColor(Drawable drawable){
        progressBar.setProgressDrawable(drawable);
    }

    private void init(Context context) {
        WebSettings settings = getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context, 2)));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.bg_webview_progress_color));
        progressBar.setMax(100);
        addView(progressBar);

        setWebChromeClient(new WebChromeClient() {

            @Keep
            public void openFileChooser(final ValueCallback<Uri> filePathCallback) {
                if (openFileChooser != null) {
                    openFileChooser.onOpenFileChooser(filePathCallback, new String[]{"*/*"});
                }

            }

            @Keep
            public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType) {
                if (openFileChooser != null) {
                    openFileChooser.onOpenFileChooser(filePathCallback, new String[]{acceptType});
                }
            }

            // Android version lower than 5.0
            @Keep
            public void openFileChooser(final ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
                if (openFileChooser != null) {
                    openFileChooser.onOpenFileChooser(filePathCallback, new String[]{acceptType});
                }

            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }


            @Override
            public boolean onShowFileChooser(WebView webView, final ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                final Activity baseActivity = getActivity(MyWebView.this.getContext());
                if (baseActivity != null && openFileChooser != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        openFileChooser.onOpenFileChooser(new ValueCallback<Uri>() {
                            @Override
                            public void onReceiveValue(Uri value) {
                                if (value != null) {
                                    filePathCallback.onReceiveValue(new Uri[]{value});
                                } else {
                                    filePathCallback.onReceiveValue(null);
                                }
                            }
                        }, fileChooserParams.getAcceptTypes());
                    }
                    return true;
                }
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                LogUtils.d("onJsAlert ===> " + message);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyWebView.this.getContext());
                builder.setTitle(getResources().getString(R.string.tip));
                builder.setMessage(message);
                builder.setPositiveButton(getResources().getString(R.string.dialog_confirm), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }

                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyWebView.this.getContext());
                builder.setTitle(getResources().getString(R.string.tip));
                builder.setMessage(message);
                builder.setPositiveButton(getResources().getString(R.string.dialog_confirm), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }

                });
                builder.setNeutralButton(getResources().getString(R.string.dialog_cancel), new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();

                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (newProgress < 10) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(10);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    }
                }
            }
        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                skipPayment(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                skipPayment(url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyWebView.this.getContext());
                builder.setTitle(getResources().getString(R.string.tip));
                builder.setMessage(getResources().getString(R.string.notification_error_ssl_cert_invalid));
                builder.setPositiveButton(getResources().getString(R.string.dialog_confirm), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }

                });
                builder.setNeutralButton(getResources().getString(R.string.dialog_cancel), new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    private void skipPayment(String url) {
        if (url.contains("platformapi/startapp")) {
            Intent intent;
            try {
                intent = Intent.parseUri(url,
                        Intent.URI_INTENT_SCHEME);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setComponent(null);
                mContext.startActivity(intent);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                && (url.contains("platformapi") && url.contains("startApp"))) {
            Intent intent;
            try {
                intent = Intent.parseUri(url,
                        Intent.URI_INTENT_SCHEME);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setComponent(null);
                mContext.startActivity(intent);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else if (url.startsWith("weixin://wap/pay?")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            mContext.startActivity(intent);
        }
    }

    public interface IOpenFileChooser {
        void onOpenFileChooser(ValueCallback<Uri> filePathCallback, String[] acceptTypes);
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            Context base = ((ContextWrapper) context).getBaseContext();
            if (base instanceof Activity) {
                return (Activity) base;
            }
        }
        return null;
    }
}
