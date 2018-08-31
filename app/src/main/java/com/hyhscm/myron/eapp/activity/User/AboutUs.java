package com.hyhscm.myron.eapp.activity.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.common.SupportErrorDialogFragment;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.utils.X5WebView;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUs extends BaseActivity {
@BindView(R.id.myweb)
X5WebView mWebView;

public  AboutUs(){
    super(R.layout.activity_about_us);
}
    @Override
    protected void initView() {
        super.initView();
        changeTitle("关于我们");
        hideObjs(new int[]{R.id.tv_right});
//        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
//        mWebView.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
//        mWebView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebView.loadUrl("http://m.hyhscm.com/about.html");
    }
}
