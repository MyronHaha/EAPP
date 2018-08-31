package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/16.
 * 招商
 */

public class RvZSAdapter extends RecyclerView.Adapter<RvZSAdapter.MyViewHolder> {


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
    public RvZSAdapter(Context context, List datas) {
        this.context = context;
        this.data = datas;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据item类别加载不同ViewHolder
        Log.e("onCreateViewHolder", "");
        View view = LayoutInflater.from(context
        ).inflate(R.layout.item_zs, parent,
                false);
//        if (viewType == 0) {
//            view.findViewById(R.id.guide1).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.view).setVisibility(View.VISIBLE);
//        }
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
//        GridViewAdapter_Tag tag = new GridViewAdapter_Tag(context, taglist);
        if (data.size() > 0) {
            Product merchants = (Product) data.get(i);
            viewholder.content.setText(merchants.getName());
            viewholder.adv.setText(String.format(context.getResources().getString(R.string.product_adv),merchants.getAdvantage()));
//            viewholder.content.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            viewholder.content.setSingleLine(true);
//            viewholder.content.setSelected(true);
//            viewholder.content.setFocusable(true);
//            viewholder.content.setMaxEms(7);
//            viewholder.content.setFocusableInTouchMode(true);

            viewholder.ago.setText(common.dataToString(merchants.getCreationTime()));
            if (!merchants.getImg().equals("")) {
                Glide.with(context).load(Url.IMAGEPATH + splitImages(merchants.getImg(), ";").get(0))
                        .error(R.mipmap.img_nothingbg2)
                        .dontAnimate()
                        .into(viewholder.iv_icon);
            }else{
                Glide.with(context).load(R.mipmap.img_nothingbg2)
                        .error(R.mipmap.img_nothingbg2)
                        .dontAnimate()
                        .into(viewholder.iv_icon);
            }


            List<String> tagList = new ArrayList<>();
            tagList.clear();
            if (merchants.getChannelName().equals("")) {
                tagList.add("暂无标签");
            } else {
                tagList.addAll(splitString(merchants.getChannelName(), "\\s+"));
            }
            TagAdapter adapter = new TagAdapter(context, tagList);
            //spanCount 横行 数列
            GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false);
            viewholder.gv.setFocusable(false);
            viewholder.gv.setFocusableInTouchMode(false);
            viewholder.gv.setLayoutManager(manager);
            viewholder.gv.setAdapter(adapter);


            if(i==data.size()-1){
                viewholder.devider.setVisibility(View.GONE);
            }
        }

//        setListViewHeightBasedOnChildren(viewholder.gv);
    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }


    //自定义ViewHolder，用于加载图片
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_product)
        ImageView iv_icon;
        @BindView(R.id.tv_info)
        TextView content;
        @BindView(R.id.tv_ago)
        TextView ago;
        @BindView(R.id.gridView)
        RecyclerView gv;
        @BindView(R.id.devider)
        View devider;
        @BindView(R.id.tv_adv)
        TextView adv;
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
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    protected List splitString(String str, String regex) {
        String[] arr = str.split(regex);
        return Arrays.asList(arr);
    }

    protected List splitImages(String str, String regex) {
        String[] arr = str.split(regex);
        List list = new ArrayList();
        for (String s : arr) {
            if (!s.equals("")) {
                list.add(s);
            }
        }
        return list;
    }
}
