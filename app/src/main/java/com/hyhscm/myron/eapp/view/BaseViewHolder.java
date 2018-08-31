package com.hyhscm.myron.eapp.view;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;

import java.net.URI;
import java.util.logging.Handler;

/**
 * Created by Jason on 2017/12/4.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;
    private View convertView;


    public BaseViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
        convertView = itemView;
    }

    //根据id检索获得该View对象
    public <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, CharSequence charSequence) {
        TextView textView = retrieveView(viewId);
        textView.setText(charSequence);
        return this;
    }

    public View getView(int viewId) {
        return retrieveView(viewId);
    }

    public BaseViewHolder setImageUrl(int viewId, String url) {
        ImageView iv = retrieveView(viewId);
//        L.e("url", url);
        iv.setImageURI(Uri.parse(url));
        return this;
    }

    public BaseViewHolder setRoundImageView(int viewId, String url) {
        ImageView iv = retrieveView(viewId);
        L.e("setRoundImageView--url", url);
//        if(url.trim().equals(Url.IMAGEPATH)){
//            Glide.with(EAPPApplication.getInstance()).load(R.mipmap.img_nothingbg2)
//                    .transform(new GlideCircleTransform(EAPPApplication.getInstance()))
//                    .into(iv);
//        }else {
        Glide.with(EAPPApplication.getInstance()).load(url)
                .transform(new GlideCircleTransform(EAPPApplication.getInstance()))
                .error(R.mipmap.img_head)
                .placeholder(R.mipmap.img_head)
                .into(iv);
//        }
        return this;
    }

    public BaseViewHolder setImage(final int viewId, final String url) {
        ImageView iv = retrieveView(viewId);
        L.e("setImageView--url", url);
        Glide.with(EAPPApplication.getInstance()).load(url)
                .error(R.mipmap.img_nothingbg2)
                .placeholder(R.mipmap.img_nothingbg2)
                .dontAnimate()  // 防止变形；
                .into(iv);
//        }
        return this;
    }
}
