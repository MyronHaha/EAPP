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
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.data.Comment;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/22.
 * 切换页面的fg 的 rv 的adapter
 */

public class SwitchAdapter extends RecyclerView.Adapter<SwitchAdapter.ViewHolder> {
    private List<Demand> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    TagAdapter adapter;
    List<String> tagList = new ArrayList<>();
    public SwitchAdapter(Context context, List<Demand> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        adapter  = new TagAdapter(context, tagList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = inflater.inflate(R.layout.item_switch2, null);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("view bind----","--demand view bind------");
        Demand de = data.get(position);

//        if(!de.getUimg().equals("")){
        //userimg
//        Glide.with(context).load(Url.IMAGEPATH + de.getUimg())
//                .transform(new GlideCircleTransform(context))
//                .placeholder(R.mipmap.img_head)
//                .error(R.mipmap.img_head)
//                .dontAnimate()
//                .into(holder.user);
//        }else{
//            Glide.with(context).load(R.mipmap.img_head)
//                    .transform(new GlideCircleTransform(context))
//                    .into(holder.user);
//        }
        if (data.size() > 0 && position < data.size()) {
//            holder.tv_see.setText(de.getHit() + "");
            holder.tv_content.setText(de.getTitle());
//            holder.tv_user.setText(de.getCname());
            holder.tv_ago.setText(common.dataToString(de.getCreationTime()));

//            if(de.getTops()>0){
//                holder.iv_ding.setVisibility(View.VISIBLE);
//            }
//            if(de.getIsCommend()>0){
//                holder.iv_tuijian.setVisibility(View.VISIBLE);
//            }
            if(de.getHit()>50){
                holder.iv_huore.setVisibility(View.VISIBLE);
            }
            tagList.clear();
            if (de.getAreas().equals("")) {
                tagList.add("暂无地区");
            } else {
                tagList.addAll(((MainActivity) context).splitString(de.getAreas(), "\\s+"));
            }

            FullyGridLayoutManager manager = new FullyGridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false);
            holder.rv.setFocusable(false);
            holder.rv.setFocusableInTouchMode(false);
            holder.rv.setLayoutManager(manager);
            holder.rv.setAdapter(adapter);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.iv_user)
//        ImageView user;
        @BindView(R.id.tv_content)
        TextView tv_content;
//        @BindView(R.id.tv_user)
//        TextView tv_user;
        @BindView(R.id.tv_ago)
        TextView tv_ago;
//        @BindView(R.id.tv_see)
//        TextView tv_see;
        @BindView(R.id.rv_areas)
        RecyclerView rv;
//        @BindView(R.id.iv_ding)
//        TextView iv_ding;
//        @BindView(R.id.iv_tuijian)
//        TextView iv_tuijian;
        @BindView(R.id.iv_huore)
        TextView iv_huore;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public void setData(List list) {
        this.data = list;
    }
}
