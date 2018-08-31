package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/16.
 * 快速创建adpater
 */

public class RvSmallHallAdapter extends RecyclerView.Adapter<RvSmallHallAdapter.MyViewHolder> {
    private static Context context;
    List<Merchants> data = new ArrayList();

    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    private static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public RvSmallHallAdapter(Context context, List datas) {
        this.context = context;
        this.data = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder", "");
        View view = null;
//        view = LayoutInflater.from(context
//        ).inflate(R.layout.item_small_hall2, parent,
//                false);
//        if (viewType == 1) {
//            ViewGroup.LayoutParams params = view.getLayoutParams();
//
//        } else {
            view = LayoutInflater.from(context
            ).inflate(R.layout.item_small_hall3, parent,
                    false);
//        }

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    //bindData
    @Override
    public void onBindViewHolder(MyViewHolder viewholder, int i) {
        Merchants merchants = (Merchants) data.get(i);

//        int width = (int) context.getResources().getDimension(R.dimen.x500);
//        viewholder.iv.setAdjustViewBounds(true);
//        ViewGroup.LayoutParams layoutParams = viewholder.iv.getLayoutParams();
//        layoutParams.width = width;
//        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        viewholder.iv.setLayoutParams(layoutParams);
//        viewholder.iv.setMaxWidth(width);
//        viewholder.iv.setMaxHeight(width*2);
        Glide.with(context)
                .load(Url.IMAGEPATH + merchants.getImg())
                .error(R.mipmap.img_nothingbg3)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewholder.iv);

//        if(i<3){
//                viewholder.right.setVisibility(View.VISIBLE);
//                viewholder.bottom.setVisibility(View.VISIBLE);
//        }else{
//            viewholder.right.setVisibility(View.VISIBLE);
//        }
//        if(i==2||i==5){
//            viewholder.right.setVisibility(View.INVISIBLE);
//        }
        viewholder.tv.setText(merchants.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_hall)
        ImageView iv;
//        @BindView(R.id.l)
//        View left;
//        @BindView(R.id.t)
//        View top;
//        @BindView(R.id.r)
//        View right;
//        @BindView(R.id.b)
//        View bottom;
        @BindView(R.id.tv_name)
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
        if (position == getItemCount() - 1) {
            return 2;
        }else if(position == 0){
            return 1;
        } else {
            return super.getItemViewType(position);
        }
    }
}
