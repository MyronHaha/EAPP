package com.hyhscm.myron.eapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.media.JetPlayer;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.ref.WeakReference;
import java.util.Map;

import butterknife.BindView;

public class MyWeb extends BaseActivity {
    @BindView(R.id.myweb)
    X5WebView mWebView;
    String url;

    public MyWeb() {
        super(R.layout.activity_about_us);
    }

    @Override
    protected void initView() {
        super.initView();

        hideObjs(new int[]{R.id.tv_right});
        url = getIntent().getStringExtra("url");
        L.e("url="+url);
        changeTitle(getTitle(url));
//        mWebView.addJavascriptInterface(new JSInterface(), "App");
//        mWebView.clearCache(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                System.out.print("onPageFinished:"+s);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                System.out.print("onPageStarted"+s);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("UrlLoading：",url+"");
                view.loadUrl(url);
                return true;
            }
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();  // 接受信任所有网站的证书
//            }
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, final WebResourceRequest request) {
//                if (request != null && request.getUrl() != null) {
////                    Log.e("拦截url：",request.getUrl().toString()+"");
//                    String scheme = request.getUrl().getScheme().trim();
//                    if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
//                        return super.shouldInterceptRequest(view, new WebResourceRequest() {
//                            @Override
//                            public Uri getUrl() {
//                                return Uri.parse(url);
//                            }
//
//                            @SuppressLint("NewApi")
//                            @Override
//                            public boolean isForMainFrame() {
//                                return request.isForMainFrame();
//                            }
//
//                            @SuppressLint("NewApi")
//                            @Override
//                            public boolean hasGesture() {
//                                return request.hasGesture();
//                            }
//
//                            @SuppressLint("NewApi")
//                            @Override
//                            public String getMethod() {
//                                return request.getMethod();
//                            }
//
//                            @SuppressLint("NewApi")
//                            @Override
//                            public Map<String, String> getRequestHeaders() {
//                                return request.getRequestHeaders();
//                            }
//                        });
//                    }
//                }
//                return super.shouldInterceptRequest(view, request);
//            }
//
//            @Override
//            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
//                Log.e("打印日志","网页加载失败");
//            }
        });
//        //进度条
//        mWebView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public void onProgressChanged(WebView webView, int i) {
//                if (i == 100) {
//                    Log.i("打印日志","加载完成");
//
//                }
//            }
//        });
        mWebView.loadUrl(url);


    }

//    public class JSInterface{
//        @JavascriptInterface
//        public void closeActivity() {
//           MyWeb.this.finish();
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
    }

    public String getTitle(String url){
        String title = "";
        if(url.equals(Url.REGIST_AGREEMENT)){
            title = "服务条款";
        }
        return title;
    }
}
