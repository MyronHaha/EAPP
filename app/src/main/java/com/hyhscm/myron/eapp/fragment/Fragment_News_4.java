package com.hyhscm.myron.eapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Home.ENewsDetail;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;


/**
 * Created by Jason on 2017/11/20.
 */

public class Fragment_News_4 extends BaseFragment<News> {

    public static Fragment_News_4 instance() {
        Fragment_News_4 view = new Fragment_News_4();
        return view;
    }

    public Fragment_News_4() {
        super(R.layout.first_fragment, R.layout.item_toutiao2);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.multiType = "news";
        initListView(R.layout.item_toutiao2);
        initData();
    }

    protected void initData() {
//        super.url = Url.NEWS;
//        super.pageSize = 6;
        params.put("t", "5");
        initData(Url.NEWS, "0", 15);

    }

    @Override
    protected void bindView(BaseViewHolder holder, News item) {
        if (holder instanceof BaseQuickAdapter.NewsAdv) {

        }else {
            holder.setText(R.id.tv_see, item.getHit() + "");
            holder.setText(R.id.tv_title, item.getTitle());
            holder.setText(R.id.tv_ago, common.dataToStringSimple(item.getDisplayTime()));
//            Glide.with(EAPPApplication.getInstance())
//                    .load(Url.WEBPATH + item.getImg())
//                    .placeholder(R.mipmap.img_nothingbg2)
//                    .error(R.mipmap.img_nothingbg2)
//                    .into((ImageView) holder.getView(R.id.iv));
            common.loadIntoUseFitWidth(getActivity(), Url.WEBPATH + item.getImg(), R.mipmap.img_nothingbg2, (ImageView) holder.getView(R.id.iv));
        }
    }

    @Override
    protected void itemClick(View v, int i, News item) {
        Intent intent = new Intent(getActivity(), ENewsDetail.class);
        intent.putExtra("item", item);
        launchActivityWithIntent(intent);
//        String url = Url.NEWS_DETAIL+"?id="+item.getId();
//        url = " http://m.hyhscm.com/newsInfo.html?id=2943";
//        L.e("new--"+url);
//        new FinestWebView.Builder(getActivity()).show(url);
    }
}
