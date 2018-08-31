package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/11/22.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private static final int ITEM_TYPE_IMAGE = 0;
    private static final int ITEM_TYPE_TEXT = 1;
    static onItemClickListener listener = null;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    List list = new ArrayList();

    public HomeAdapter(Context context,List data) {
        this.list  = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
   public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
   }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType ==ITEM_TYPE_IMAGE) {
            return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_img, parent, false));
        } else {
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.item_news, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          //
        News  item = (News) list.get(position);
        if(holder.getItemViewType()==ITEM_TYPE_IMAGE){
            ( (ImageViewHolder)holder).mTextView.setText(item.getTitle());
            Glide.with(EAPPApplication.getInstance()).load(Url.WEBPATH+item.getImg())
                    .error(R.mipmap.img_nothingbg3)
                    .placeholder(R.mipmap.img_nothingbg3)
                    .dontAnimate()  // 防止变形；
                    .into(( (ImageViewHolder)holder).mImageView);

        }else{
            ( (TextViewHolder)holder).mTextView.setText(item.getTitle());
            Glide.with(EAPPApplication.getInstance()).load(Url.WEBPATH+item.getImg())
                    .error(R.mipmap.img_nothingbg3)
                    .placeholder(R.mipmap.img_nothingbg3)
                    .dontAnimate()  // 防止变形；
                    .into(( (TextViewHolder)holder).imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_IMAGE: ITEM_TYPE_TEXT;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.imageView)
        ImageView imageView;
       @BindView(R.id.tv_title)
        TextView mTextView;
        TextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onItemClick(getPosition(),getItemViewType());
                    }
                }
            });
        }
    }

 public static  class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTextView;
        @BindView(R.id.imageView)
        ImageView mImageView;
        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onItemClick(getPosition(),getItemViewType());
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(int position,int viewType);
    }
    public void setData(List li){
        this.list = li;
    }
}
