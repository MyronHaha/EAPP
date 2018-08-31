package com.hyhscm.myron.eapp.view;

/**
 * Created by Jason on 2017/11/23.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyhscm.myron.eapp.R;
import com.lzy.ninegrid.NineGridView;

import java.util.List;

import javax.microedition.khronos.opengles.GL;


public class GlideLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {

        Glide.with(context).load(url)//
                .error(R.drawable.ic_default_image)//
                .placeholder(R.drawable.ic_default_image)
                .into(imageView);

    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }

}