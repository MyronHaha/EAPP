package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/16.
 */
// item tag
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder>  {

    private Context mContext;
    private List data = new ArrayList<>();//数据


    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    private static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public TagAdapter(Context context, List datas) {
        mContext = context;
        this.data = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据item类别加载不同ViewHolder
        Log.e("onCreateViewHolder","");
        View view = LayoutInflater.from(mContext
        ).inflate(R.layout.item_ptag_notfill, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    //bindData
    @Override
    public void onBindViewHolder(MyViewHolder viewholder, int i) {
         viewholder.tag.setText((CharSequence) data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size()>3?3:data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_tag)
       TextView tag;
        public MyViewHolder(View view) {
            super(view);
         ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(view,getPosition());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
