package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.DensityUtil;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/16.
 * 头条
 */

public class RvToutiaoAdapter extends RecyclerView.Adapter<RvToutiaoAdapter.MyViewHolder> {


    private static Context context;
    List<News> data = new ArrayList();

    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    private static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public RvToutiaoAdapter(Context context, List datas) {
        this.context = context;
        this.data = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据item类别加载不同ViewHolder
        View view = null;
        view = LayoutInflater.from(context
        ).inflate(R.layout.item_toutiao2, parent,
                false);
        if (viewType == 2) {
            view.findViewById(R.id.devider1).setVisibility(View.GONE);
        }else if(viewType == 1){
            view = LayoutInflater.from(context
            ).inflate(R.layout.item_toutiao, parent,
                    false);
        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;


    }

    //bindData
    @Override
    public void onBindViewHolder(MyViewHolder viewholder, int i) {
        if (data.size() > 0) {

            News news = data.get(i);
            viewholder.title.setText(news.getTitle());
            viewholder.ago.setText(common.dataToString(news.getCreationTime()));
            viewholder.liulan.setText(news.getHit() + "");
            viewholder.tv_content.setText(news.getContent());
//            int width = DensityUtil.getDisplay(context).getWidth();
//            L.e("llllll" + width);
//            viewholder.iv.setAdjustViewBounds(true);
//            viewholder.iv.setMaxWidth(width);
//            viewholder.iv.setMaxHeight(width);
//
//            ViewGroup.LayoutParams layoutParams = viewholder.iv.getLayoutParams();
//            layoutParams.width = width;
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            viewholder.iv.setLayoutParams(layoutParams);
//            Glide.with(EAPPApplication.getInstance())
//                    .load(Url.WEBPATH + news.getImg())
//                    .placeholder(R.mipmap.img_nothingbg3)
//                    .error(R.mipmap.img_nothingbg3)
//                    .into(viewholder.iv);
            common.loadIntoUseFitWidth(context,Url.WEBPATH + news.getImg(),R.mipmap.img_nothingbg3,viewholder.iv);
//            if (news.getImg().equals("")) {
//          int width = DensityUtil.dip2px(context,DensityUtil.getDisplay(context).getWidth());
//          L.e(width+"");
//                viewholder.iv.setAdjustViewBounds(true);
//                viewholder.iv.setMaxWidth(width);
//                viewholder.iv.setMaxHeight((int) (width * 0.3));
//
//                ViewGroup.LayoutParams layoutParams = viewholder.iv.getLayoutParams();
//                layoutParams.width = width;
//                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                viewholder.iv.setLayoutParams(layoutParams);
//
//                Glide.with(context)
//                        .load(Url.WEBPATH+news.getImg())
//                        .placeholder(R.mipmap.img_nothingbg3)
//                        .error(R.mipmap.img_nothingbg3)
//                        .into(viewholder.iv);


//                ViewGroup.LayoutParams params = viewholder.iv.getLayoutParams();
//                params.width = DensityUtil.getDisplay(context).getWidth();
//                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                viewholder.iv.setLayoutParams(params);
//                viewholder.iv.setAdjustViewBounds(true);
//                viewholder.iv.setMaxWidth(DensityUtil.getDisplay(context).getWidth());
//                viewholder.iv.setMaxHeight((int) (0.50*DensityUtil.getDisplay(context).getWidth()));
//
//                viewholder.iv.setAdjustViewBounds(true);
//                viewholder.iv.setMaxWidth(DensityUtil.px2dip(context,DensityUtil.getDisplay(context).getWidth()));
//                viewholder.iv.setMaxWidth(200);


//
//            }
//            else{
//                Glide.with(context)
//                        .load(Url.IMAGEPATH+news.getImg())
//                        .error(R.mipmap.img_nothingbg2)
//                        .into(viewholder.iv);
//
//            }

//
//            viewholder.iv.setMaxWidth(DensityUtil.px2dip(context,DensityUtil.getDisplay(context).getWidth()));
//
//            viewholder.iv.setAdjustViewBounds(true);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_ago)
        TextView ago;
        @BindView(R.id.tv_see)
        TextView liulan;
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.iv_see)
        ImageView iv_see;
        @BindView(R.id.tv_content)
        TextView tv_content;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
//            iv_see.setVisibility(View.GONE);
//            liulan.setVisibility(View.GONE);
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
        if (position == data.size()-1) {
            return 2;
        }else if(position == 0){
            return 1;
        }
        else {
            return 0;
        }
    }

//    new RoundCornersTransformation(MainActivity.this,
//            dip2px(25),
//            mList.get(position).type);
}
