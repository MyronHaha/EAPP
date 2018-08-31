package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyhscm.myron.eapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2017/11/16.
 */

public class MyCornAdapter extends RecyclerView.Adapter<MyCornAdapter.MyViewHolder> {

    private Context mContext;
    private List data = new ArrayList<>();//数据


    public MyCornAdapter(Context context, List datas) {
        mContext = context;
        this.data = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据item类别加载不同ViewHolder
        Log.e("onCreateViewHolder", "");
        View view = LayoutInflater.from(mContext
        ).inflate(R.layout.item_hebi, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewholder, int i) {

    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }


    //自定义ViewHolder，用于加载图片
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
