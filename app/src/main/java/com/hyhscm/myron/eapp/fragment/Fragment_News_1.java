package com.hyhscm.myron.eapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Home.ENewsDetail;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/20.
 */

//public class Fragment_News_1 extends Fragment {
//    @BindView(R.id.rv)
//    LRecyclerView rv;
//    private LRecyclerViewAdapter lRecyclerViewAdapter;
//    private int pageCount = 1;
//    private List data = new ArrayList();
//
//    public static Fragment_News_1 instance() {
//        Fragment_News_1 view = new Fragment_News_1();
//        return view;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.first_fragment, null);
//        ButterKnife.bind(this, view);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initRv();
//    }
//
//    private void initRv() {
//        common.createData(data);
//        //必须制定数据类型，否则不显示；
//        BaseQuickAdapter<String> adapter = new BaseQuickAdapter<String>(getActivity(), "news", R.layout.item_toutiao2, data) {
//            @Override
//            protected void convert(BaseViewHolder holder, String item) {
//                Log.e("item", item.toString());
//
//                holder.setText(R.id.tv_title, String.valueOf(item) + "新闻标题，新闻标题");
//            }
//
//            @Override
//            public void onBindViewHolder(BaseViewHolder holder, int position) {
//                super.onBindViewHolder(holder, position);
//            }
//        };
////        MyCornAdapter adapter = new MyCornAdapter(getActivity(),data);
////        new MyAdapter(getActivity(),"news",R.layout.item_toutiao2,data)
//        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
//        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int i) {
//                common.launchActivity(getContext(), ENewsDetail.class);
//            }
//        });
//        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv.setAdapter(lRecyclerViewAdapter);
//        rv.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        rv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        rv.setLoadMoreEnabled(true);
//        rv.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });
//        rv.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        data.addAll(data);
////                        rv.setNoMore(true);
//                        lRecyclerViewAdapter.notifyDataSetChanged();
//                        rv.refreshComplete(10);
//                    }
//                }, 100);
//            }
//        });
//    }
//
//    private void refresh() {
//        pageCount = 1;
//        data.clear();
//        common.createData(data);
//        lRecyclerViewAdapter.notifyDataSetChanged();
//        rv.refreshComplete(10);
//    }
//
////    class MyAdapter extends  BaseQuickAdapter<String>{
////
////        public MyAdapter(Context context, String type, int layoutResID, List<String> data) {
////            super(context, type, layoutResID, data);
////        }
////
////        @Override
////        protected void convert(BaseViewHolder holder, String item) {
////            TextView tv = (TextView) holder.getView(R.id.tv_title);
////            Log.e("item",item);
////            tv.setText(item+"");
////            holder.setText(R.id.tv_title,item);
////        }
////    }
//}

public class Fragment_News_1 extends BaseFragment<News> {

    public static Fragment_News_1 instance() {
        Fragment_News_1 view = new Fragment_News_1();
        return view;
    }

    public Fragment_News_1() {
        super(R.layout.first_fragment, R.layout.item_toutiao2);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.multiType ="news";
        initListView(R.layout.item_toutiao2);
        initData();
    }

    protected void initData() {
//        super.url = Url.NEWS;
//        super.pageSize = 6;

        params.put("t","1");
        initData(Url.NEWS, "0", 15);

    }

    @Override
    protected void bindView(BaseViewHolder holder, News item) {
        if (holder instanceof BaseQuickAdapter.NewsAdv) {

        } else {
            holder.setText(R.id.tv_see, item.getHit() + "");
            holder.setText(R.id.tv_title, item.getTitle());
            holder.setText(R.id.tv_ago, common.dataToStringSimple(item.getDisplayTime()));
//        Glide.with(EAPPApplication.getInstance())
//                .load(Url.WEBPATH + item.getImg())
//                .placeholder(R.mipmap.img_nothingbg2)
//                .error(R.mipmap.img_nothingbg2)
//                .into((ImageView) holder.getView(R.id.iv));
            common.loadIntoUseFitWidth(getActivity(), Url.WEBPATH + item.getImg(), R.mipmap.img_nothingbg2, (ImageView) holder.getView(R.id.iv));

        }
    }

    @Override
    protected void itemClick(View v, int i, News item) {
        Intent intent  = new Intent(getActivity(), ENewsDetail.class);
        intent.putExtra("item",item);
        launchActivityWithIntent(intent);
//        String url = Url.NEWS_DETAIL+"?id="+item.getId();
//        url = " http://m.hyhscm.com/newsInfo.html?id=2943";
//        L.e("new--"+url);
//        new FinestWebView.Builder(getActivity()).show(url);
    }



}