package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/16.
 * 快速创建adpater
 */

public class RvSimpleAdapter extends RecyclerView.Adapter<RvSimpleAdapter.MyViewHolder> {


    private static Context context;
    List data = new ArrayList();
    static List taglist = new ArrayList();


    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    private static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public RvSimpleAdapter(Context context, List datas) {
        this.context = context;
        this.data = datas;
//        common.createData(taglist);
        for(int i = 0;i<6;i++){
            taglist.add("标签"+i);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据item类别加载不同ViewHolder
        Log.e("onCreateViewHolder", "");
        View view = LayoutInflater.from(context
        ).inflate(R.layout.item_zs, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
//
//        //给布局设置点击和长点击监听
//        view.setOnClickListener(this);
//        view.setOnLongClickListener(this);

        return holder;


    }

    //bindData
    @Override
    public void onBindViewHolder(MyViewHolder viewholder, int i) {

    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
         return super.getItemViewType(position);
    }


}
