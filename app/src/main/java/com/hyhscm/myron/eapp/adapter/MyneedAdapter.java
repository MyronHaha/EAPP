package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/16.
 */

public class MyneedAdapter extends RecyclerView.Adapter<MyneedAdapter.MyViewHolder> {

    private Context mContext;
    private static List data = new ArrayList<>();//数据


    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    private static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public MyneedAdapter(Context context, List datas) {
        mContext = context;
        this.data = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据item类别加载不同ViewHolder
        Log.e("onCreateViewHolder", "");
        View view = LayoutInflater.from(mContext
        ).inflate(R.layout.item_myneed, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;


    }

    //bindData
    @Override
    public void onBindViewHolder(final MyViewHolder viewholder, final int i) {
        viewholder.tv_title.setText(data.get(i) + "hahahhaha");


    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_delete)
        ImageView delete;
        @BindView(R.id.tv_title)
        TextView tv_title;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.remove(getAdapterPosition() - 1);
                    notifyItemRemoved(getAdapterPosition() - 1);
                    notifyItemRangeChanged(0, data.size());
                    Log.e("index", getPosition() + "");
                    // 服务器删除接口
                    MyToast.makeText(mContext, "删除成功", 2000).show();
                }
            });
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
