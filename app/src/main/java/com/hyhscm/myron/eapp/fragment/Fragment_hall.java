package com.hyhscm.myron.eapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.adapter.RvSmallHallAdapter;
import com.hyhscm.myron.eapp.adapter.SwitchAdapter;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/20.
 */

public class Fragment_hall extends Fragment {
   @BindView(R.id.hall_list)
    RecyclerView hall;
    int startCount;
    static RvSmallHallAdapter adapter;
    static List<Merchants> tdata = new ArrayList();
    public static Fragment_hall newInstance(List<Merchants> data) {
        Fragment_hall fragment = new Fragment_hall();
        tdata = data;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fg_hall, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        startCount = getArguments().getInt("startCount");
         adapter   = new RvSmallHallAdapter(getActivity(),tdata);
        hall.setLayoutManager(new GridLayoutManager(getActivity(),3));

        L.e("shDataSize:"+tdata.size());
        hall.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//       hall.setAdapter(new BaseQuickAdapter(getActivity(),"",R.layout.item_small_hall3,tdata) {
//           @Override
//           protected void convert(BaseViewHolder holder, Object item) {
//               Merchants merchants = (Merchants) item;
//
//               Glide.with(getActivity())
//                       .load(Url.IMAGEPATH + merchants.getImg())
//                       .error(R.mipmap.img_nothingbg3)
//                       .dontAnimate()
//                       .into((ImageView) holder.getView(R.id.iv_hall));
//           }
//
//           @Override
//           public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//           }
//       });
//        initView();
    }


}
