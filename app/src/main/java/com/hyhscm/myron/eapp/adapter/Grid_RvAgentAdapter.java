package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/16.
 * 快速创建adpater
 */

public class Grid_RvAgentAdapter extends RecyclerView.Adapter<Grid_RvAgentAdapter.MyViewHolder> {
    private static Context context;
    List data = new ArrayList();
    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
    private static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public Grid_RvAgentAdapter(Context context, List datas) {
        this.context = context;
        this.data = datas;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder", "");
        View view = LayoutInflater.from(context
        ).inflate(R.layout.item_type, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
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
     @BindView(R.id.tv_tag)
     TextView tag;
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
