package com.hyhscm.myron.eapp.activity.Home;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.adapter.MyCornAdapter;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.DensityUtil;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.linchaolong.android.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/12/06.
 * 展厅 列表
 */

public class HallList extends ListBaseBeanActivity<Merchants> {
    @BindView(R.id.search)
    EditText et_search;
    @BindView(R.id.rv)
    LRecyclerView rv;

    public HallList() {
        super(R.layout.layout_hall_list);
    }

    protected void initView() {
//        common.changeTitle(this, "展厅");

        initListView(R.layout.item_hall);
        et_search.clearFocus();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                params.put("k", editable.toString());
                rv.forceToRefresh();
            }
        });
    }

    @Override
    protected void initData() {
        initData(Url.COMPANY, 8);
    }

    @Override
    protected void bindView(BaseViewHolder holder, final Merchants item) {
        holder.setText(R.id.tv_name, item.getName());
        holder.setText(R.id.content, item.getContent());
//        ImageView imageView  =   holder.retrieveView(R.id.iv);
//        Log.e("iv_id",""+imageView.toString());

//      holder.setImage(R.id.iv,Url.WEBPATH+item.getImg());
//          common.loadIntoUseFitWidth(EAPPApplication.getInstance(),Url.WEBPATH+item.getImg(),R.mipmap.img_nothingbg2,imageView);
//        Glide.with(this)
//                .load(Url.WEBPATH+item.getImg())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
//                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
//                        imageView.setLayoutParams(params);
//                        return false;
//                    }
//                })
//                .placeholder(R.mipmap.img_nothingbg2)
//                .error(R.mipmap.img_nothingbg2)
//                .into(imageView);
        Glide.with(this).load(Url.WEBPATH + item.getImg())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.img_nothingbg2)
                .error(R.mipmap.img_nothingbg2)
                .into((ImageView) holder.getView(R.id.iv));
//         final ImageView iv  = (ImageView) holder.getView(R.id.iv);
//         new Handler().post(new Runnable() {
//             @Override
//             public void run() {
//                 holder.setImage(R.id.iv,Url.WEBPATH+item.getImg());
//             }
//         });

    }

    @Override
    protected void itemClick(View v, int i, Merchants item) {
        Intent intent = new Intent(this, HallDetail.class);
        intent.putExtra("item", item);
        common.launchActivityWithIntent(this, intent);
    }

    @OnClick(R.id.ll_back)
    public void back() {
        this.finish();
    }
}
