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
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.data.Comment;
import com.hyhscm.myron.eapp.data.FocusInfo;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/22.
 */

public class HomeAdapter2 extends RecyclerView.Adapter<HomeAdapter2.ViewHolder> {
    private static final int ITEM_TYPE_IMAGE = 0;
    private static final int ITEM_TYPE_TEXT = 1;
    private List<FocusInfo> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
//    String[] urls = {
//            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3807720874,2433149685&fm=27&gp=0.jpg"
//            ,"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2426407509,4199842435&fm=27&gp=0.jpg",
//            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=61561371,604619965&fm=27&gp=0.jpg",
//            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2639088341,2223755776&fm=27&gp=0.jpg",
//            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=397396640,3939866099&fm=27&gp=0.jpg",
//            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1602552054,373587514&fm=27&gp=0.jpg",
//            "https://ss0.baidu.com/73x1bjeh1BF3odCf/it/u=3039422686,2423978616&fm=85&s=391E1FD06801C24316993F0C0300F0D5"
//    };

    public HomeAdapter2(Context context, List<FocusInfo> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_IMAGE;
        } else {
            return ITEM_TYPE_TEXT;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view1 = inflater.inflate(R.layout.item_dt, null);
        View view2 = inflater.inflate(R.layout.item_dt_title, null);

        if (viewType == ITEM_TYPE_IMAGE) {
            viewHolder = new ViewHolder(view2);
        } else {
            viewHolder = new ViewHolder(view1);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FocusInfo item = (FocusInfo) data.get(position);
        holder.tv_ago.setText(item.getCreationTime());
        holder.tv_liulan.setText(item.getHit()+"");
        holder.tv_user.setText(item.getNname());
        holder.tv_dianzan.setText("点赞数 "+item.getTuc()+"");
        holder.tv_content.setText(item.getContent());
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        List<String> images = splitString(item.getPic(),";");
        if (images.size() < 0) {
            holder.devider.setVisibility(View.GONE);
        } else {
            holder.devider.setVisibility(View.VISIBLE);
        }


        if(images.size()<0){

        }else{
            if (images != null) {
                for (String image : images) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(Url.IMAGEPATH+image);
                    info.setBigImageUrl(Url.IMAGEPATH+image);
                    imageInfo.add(info);
                }
            }
            holder.nineGrid.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
        }

        Glide.with(context).load(Url.HOSTNAME+item.getImg())
                .error(R.mipmap.img_head)
                .dontAnimate()
                .transform(new GlideCircleTransform(context))
                .into(holder.user);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nineGrid)
        NineGridView nineGrid;
        @BindView(R.id.devider2)
        View devider;
        @BindView(R.id.iv_user)
        ImageView user;
        @BindView(R.id.tv_user)
        TextView tv_user;
        @BindView(R.id.tv_ago)
        TextView tv_ago;
        @BindView(R.id.tv_liulan)
        TextView tv_liulan;
        @BindView(R.id.tv_dianzan)
        TextView tv_dianzan;
        @BindView(R.id.tv_content)
        TextView tv_content;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }
    protected List splitString(String str,String regex){
        String[] arr = str.split(regex);
        List list = new ArrayList();
        for(String s:arr){
            if(!s.equals("")){
                list.add(s);
            }
        }
        return list;
    }
}
