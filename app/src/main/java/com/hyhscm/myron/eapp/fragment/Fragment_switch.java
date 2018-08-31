package com.hyhscm.myron.eapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.activity.User.MyNeedDetail;
import com.hyhscm.myron.eapp.adapter.GridViewAdapter;
import com.hyhscm.myron.eapp.adapter.HomeAdapter;
import com.hyhscm.myron.eapp.adapter.HomeAdapter2;
import com.hyhscm.myron.eapp.adapter.SwitchAdapter;
import com.hyhscm.myron.eapp.data.BaseResult;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.FullyLinearLayoutManager;
import com.mph.okdroid.response.GsonResHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Jason on 2017/11/20.
 */

public class Fragment_switch extends Fragment {
//    public static List list1 = new ArrayList();
//    public static List list2 = new ArrayList();
    List list = new ArrayList();
    @BindView(R.id.rv_switch)
    LRecyclerView rv1;
    FullyLinearLayoutManager linearLayoutManager;
    public static SwitchAdapter mAdapter1, mAdapter2;
    public static LRecyclerViewAdapter mLRecyclerViewAdapter, mLRecyclerViewAdapter1, mLRecyclerViewAdapter2;
    List data = new ArrayList();
    int type;
    public static Fragment_switch newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("switchType", type);
        Fragment_switch fragment = new Fragment_switch();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yfx_viewpager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         type = getArguments().getInt("switchType");
        if (type == 0) {
            mAdapter1 = new SwitchAdapter(getActivity(), list);
            mLRecyclerViewAdapter1 = new LRecyclerViewAdapter(mAdapter1);
            mLRecyclerViewAdapter = mLRecyclerViewAdapter1;
            Log.e("hahaahhaha", type + "");
        } else {
            Log.e("hahaahhaha", type + "");
            mAdapter2 = new SwitchAdapter(getActivity(), list);
            mLRecyclerViewAdapter2 = new LRecyclerViewAdapter(mAdapter2);
            mLRecyclerViewAdapter = mLRecyclerViewAdapter2;

        }
        initxRefresh();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initList1(type);
            }
        }).start();

//        initView();
    }

    private void initView() {
        initxRefresh();
    }

    private void initxRefresh() {
        linearLayoutManager = new FullyLinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rv1.setHasFixedSize(true);
//
//        rv1.setNestedScrollingEnabled(false);
        rv1.setFocusableInTouchMode(false);
        rv1.setLayoutManager(linearLayoutManager);
        rv1.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        rv1.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
                .setHeight(1.0f)
                .setColorResource(R.color.devider1)
                .setLeftPadding(getActivity().getResources().getDimension(R.dimen.x26))
                .setRightPadding(getActivity().getResources().getDimension(R.dimen.x26))
                .build();
        rv1.addItemDecoration(divider);
        rv1.setAdapter(mLRecyclerViewAdapter);
        rv1.setLoadMoreEnabled(false);
        rv1.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if(HttpCore.isLogin){
                    ((MainActivity)getActivity()).getDemandById(((Demand)list.get(i)).getId()+"");
                }else{
                    common.launchActivity(getActivity(),LoginActivity.class);
                }

            }
        });
    }

    //根据type 获取数据；
    public void initList1(int type) {
        HttpCore.getDemandList(type+"",0,5, new IListResultHandler<Demand>() {
            @Override
            public void onSuccess(ListResult<Demand> rs) {
                list.clear();
                list.addAll(rs.getBiz());
               mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        Log.e("data1refresh", "call");
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        Log.e("fragmentchange",""+hidden);
//            initList1(type);
//    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        initList1(type);
//    }
}
