package com.ubfx.library.browser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.ubfx.library.R;
import com.ubfx.library.dialog.OnConfirmListener;
import com.ubfx.library.dialog.UBFXDialogUtils;
import com.ubfx.library.dialog.builder.ConfirmDialogBuilder;
import com.ubfx.log.LogUtils;;
import com.ubfx.library.utils.ViewUtils;

/**
 * Created by yangchuanzhe on 2019/3/6.
 */
public abstract class BaseWebView extends WebView {

    private WebViewClient mClient;

    private IOpenFileChooser openFileChooser;

    private ProgressBar progressBar;

    public BaseWebView(Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    public void setWebViewClient(WebViewClient client) {
        this.mClient = client;
    }

    public void setOpenFileChooser(IOpenFileChooser openFileChooser) {
        this.openFileChooser = openFileChooser;
    }

    public void setUserAgentString(String customUA) {
        WebSettings settings = getSettings();
        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + customUA);
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

        progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context,2)));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.lib_progressbar_color));
        progressBar.setMax(100);
        addView(progressBar);

        super.setWebChromeClient(new WebChromeClient() {

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
                LogUtils.d("console ===> " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }


            @Override
            public boolean onShowFileChooser(WebView webView, final ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                final Activity baseActivity = ViewUtils.getActivity(BaseWebView.this);
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

                ConfirmDialogBuilder builder = new ConfirmDialogBuilder(view.getContext());
                builder.setTitle(view.getContext().getString(R.string.lib_tip))
                        .setContent(message).setConfirmListener(new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        result.confirm();
                    }
                }).setCancelable(false).setCancelDesc("");

                UBFXDialogUtils.createConfirmDialog(view.getContext(), builder).show();

                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                UBFXDialogUtils.createConfirmDialog(view.getContext(), view.getContext().getString(R.string.lib_tip), message, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        result.confirm();
                    }
                }, new OnCancelListener() {
                    @Override
                    public void onCancel(@NonNull DialogPlus dialog) {
                        result.cancel();
                    }
                }).show();

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


        super.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mClient != null) {
                    mClient.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mClient != null) {
                    mClient.onPageFinished(view, url);
                }

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (mClient != null) {
                    mClient.onLoadResource(view, url);
                }
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (mClient != null) {
                    mClient.onPageCommitVisible(view, url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mClient != null) {
                    return mClient.shouldOverrideUrlLoading(view, url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (mClient != null) {
                    return mClient.shouldInterceptRequest(view, url);
                }
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                String title = view.getResources().getString(R.string.lib_tip);
                String tip = view.getResources().getString(R.string.lib_notification_error_ssl_cert_invalid);

                ConfirmDialogBuilder builder = new ConfirmDialogBuilder(getContext());
                builder.setTitle(title).setContent(tip);
                builder.setConfirmDesc(view.getResources().getString(R.string.lib_continue));
                builder.setConfirmListener(new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        if (handler != null) {
                            handler.proceed();
                        }
                    }
                });
                builder.setCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(@NonNull DialogPlus dialog) {
                        if (handler != null) {
                            handler.cancel();
                        }
                    }
                });

                UBFXDialogUtils.createConfirmDialog(getContext(), builder).show();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                if (mClient != null) {
                    return mClient.shouldOverrideKeyEvent(view, event);
                }
                return super.shouldOverrideKeyEvent(view, event);
            }
        });

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }


    public interface IOpenFileChooser {
        void onOpenFileChooser(ValueCallback<Uri> filePathCallback, String[] acceptTypes);
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
