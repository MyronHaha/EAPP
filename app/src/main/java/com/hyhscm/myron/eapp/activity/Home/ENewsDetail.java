package com.hyhscm.myron.eapp.activity.Home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.E;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.fragment.Fragment_News_1;
import com.hyhscm.myron.eapp.fragment.Fragment_News_2;
import com.hyhscm.myron.eapp.fragment.Fragment_News_3;
import com.hyhscm.myron.eapp.fragment.Fragment_News_4;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.X5WebView;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.ViewFindUtils;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/12/06.
 * e械新闻
 */

public class ENewsDetail extends BaseActivity {
    @BindView(R.id.wv)
    X5WebView mWebView;
    @BindView(R.id.myProgressBar)
    ProgressBar bar;
    String htmlStr = "";
    private ConstraintLayout mRootLayout;
    private String id = "";
    News news = null;

    public ENewsDetail() {
        super(R.layout.layout_news_detail);
    }

    @Override
    protected void initView() {
        super.initView();
        changeTitle("资讯内容");

//        addWebViewToLayout();
        try {
            if (getIntent().getSerializableExtra("item") == null) {
                id = getIntent().getStringExtra("param");
//               hideObjs(new int[]{R.id.iv_right});
            } else {
                news = (News) getIntent().getSerializableExtra("item");
                id = news.getId() + "";

            }
        } catch (Exception e) {

        }
        mWebView.loadUrl(Url.NEWS_DETAIL + "?id=" + id);//
        Log.e("urlurlurl", Url.NEWS_DETAIL + "?id=" + id);
//        mWebView.loadUrl(" http://m.hyhscm.com/newsInfo.html?id=2943");
        //设置自适应屏幕，两者合用
        // 解决对某些标签的不支持出现白屏
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                view.loadUrl(Url.NEWS_DETAIL + "?id=" + id);
//                L.e("error:" + error);
//            }


//        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    handler.obtainMessage(0).sendToTarget();
//                    bar.setVisibility(View.INVISIBLE);
                } else {
                    handler.obtainMessage(newProgress).sendToTarget();
//                    if (View.INVISIBLE == bar.getVisibility()) {
//                        bar.setVisibility(View.VISIBLE);
//                    }
//                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });


//        tv.setText(Html.fromHtml(news.getContent()));
    }

//        Spanned sp = Html.fromHtml(news.getContent(), new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {
//                InputStream is = null;
//                try {
//                    is = (InputStream) new URL(source).getContent();
//                    Drawable d = Drawable.createFromStream(is, "src");
//                    d.setBounds(0, 0, d.getIntrinsicWidth(),
//                            d.getIntrinsicHeight());
//                    is.close();
//                    return d;
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        }, null);
//        tv.setText(sp);
//    }

    @OnClick(R.id.ll_right)
    public void share() {
        //bottom
        View view = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
        showCustomDialog(view, news, 0, false);

//        shareToWx(0,demand.getId()+"",demand.getTitle(),demand.getContent(),0);
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                bar.setVisibility(View.INVISIBLE);
            } else {
                if (View.INVISIBLE == bar.getVisibility()) {
                    bar.setVisibility(View.VISIBLE);
                }
                bar.setProgress(msg.what);
            }
        }
    };
}
