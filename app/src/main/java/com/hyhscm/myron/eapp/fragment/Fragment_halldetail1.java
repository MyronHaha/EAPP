package com.hyhscm.myron.eapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Home.ENewsDetail;
import com.hyhscm.myron.eapp.activity.Home.HallDetail;
import com.hyhscm.myron.eapp.activity.Home.HomeProduct;
import com.hyhscm.myron.eapp.activity.Home.ProductDetail;
import com.hyhscm.myron.eapp.activity.User.MyProduct;
import com.hyhscm.myron.eapp.activity.User.MyProductDtail;
import com.hyhscm.myron.eapp.adapter.MyProductAdapter;
import com.hyhscm.myron.eapp.adapter.TagAdapter;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 和first_fragment 新闻首页公用布局 ，就一个list
 * Created by Jason on 2017/12/13.
 */

public class Fragment_halldetail1 extends BaseFragment<Product> {
    @BindView(R.id.rv)
    LRecyclerView rv;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private int pageCount = 1;
    private List data = new ArrayList();

    public static Fragment_halldetail1 instance() {
        Fragment_halldetail1 view = new Fragment_halldetail1();
        return view;
    }

    public Fragment_halldetail1() {
        super(R.layout.rv, R.layout.item_product);
    }


    protected void initData() {
        super.url = Url.MERCHANTLIST;
        super.pageSize = 6;
        params.put("uid", HallDetail.cid+"");
        initData(Url.MERCHANTLIST,"0",6);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.multiType = "hall";
        initListView(R.layout.item_product);
        initData();
    }
    protected void bindView(BaseViewHolder holder, final Product item) {
        try {
            holder.setText(R.id.tv_info, item.getName());
//            holder.setText(R.id.tv_ago, common.dataToString(item.getCreationTime()) + "");
            holder.setText(R.id.tv_ago, common.TimeDifference(item.getCreationTime()));

            holder.setText(R.id.tv_gg, item.getSpec());
            holder.setText(R.id.tv_adv, item.getAdvantage());
            holder.setImage(R.id.iv_product,Url.IMAGEPATH+splitString(item.getImg(),";").get(0));
            List<String> tagList = new ArrayList<>();
            tagList.clear();
            if (item.getChannelName().equals("")) {
                tagList.add("暂无标签");
            } else {
                tagList .addAll(splitString(item.getChannelName(), "\\s+")) ;
            }
            RecyclerView rv = (RecyclerView) holder.getView(R.id.gridView);
            TagAdapter adapter = new TagAdapter(getActivity(), tagList);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
            rv.setFocusable(false);
            rv.setFocusableInTouchMode(false);
            rv.setLayoutManager(manager);
            rv.setAdapter(adapter);
            if (HallDetail.cid== HttpCore.userId) {
                holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
            }
            holder.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        HttpCore.productDel(item.getId() , new IResultHandler<Product>() {
                            @Override
                            public void onSuccess(Result<Product> rs) {
                                if(rs.getSuccess())
                                {
                                    new PromptDialog(getActivity()).showSuccess("删除成功！");
                                    loadData();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
//            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            L.e("binderror"+e.getMessage());
        }

    }
    protected void itemClick(View v, int i, Product item) {
        Intent intent = new Intent(getActivity(), MyProductDtail.class);
        intent.putExtra("item",item);
        launchActivityWithIntent(intent);
    }

    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.rv, null);
//        ButterKnife.bind(this, view);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initRv();
//    }

//    private void initRv() {
//
//        MyProductAdapter adapter = new MyProductAdapter(getActivity(),data);
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

//    private void refresh() {
//        pageCount = 1;
//        data.clear();
//        common.createData(data);
//        lRecyclerViewAdapter.notifyDataSetChanged();
//        rv.refreshComplete(10);
//    }
}