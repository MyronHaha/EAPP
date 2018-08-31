package com.hyhscm.myron.eapp.activity.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.User.MyProductDtail;
import com.hyhscm.myron.eapp.circlePic.BigPic;
import com.hyhscm.myron.eapp.data.Comment;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.Goods;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.DensityUtil;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Jason on 2017/12/06.
 * 产品详情  旧版旧版旧版旧版旧版 用MYproductDetail
 * tv_name
 * tv_publish
 * tv_price
 * tv_detail
 * tv_pzwh
 * tv_gg
 * tv_xh
 * tv_pp
 * tv_dwgg
 * tv_cl
 * title_detail
 * tv_sm
 */

public class ProductDetail extends BaseActivity {
    @BindView(R.id.banner)
    BGABanner mContentBanner;
    private Goods mproduct;
    String index = "";
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_publish)
    TextView tv_publish;

    @BindView(R.id.tv_pzwh)
    TextView tv_pzwh;
    @BindView(R.id.tv_gg)
    TextView tv_gg;
    @BindView(R.id.tv_xh)
    TextView tv_xh;
    @BindView(R.id.tv_pp)
    TextView tv_pp;
    @BindView(R.id.tv_ftype)
    TextView tv_ftype;
    @BindView(R.id.tv_ctype)
    TextView tv_ctype;
    @BindView(R.id.tv_room)
    TextView tv_room;

    @BindView(R.id.tv_sm)
    TextView tv_sm;
    List<String> imgs = new ArrayList<>();

    public ProductDetail() {
        super(R.layout.layout_product__detail);
    }

    protected void initView() {
        super.initView();
        changeTitle("产品详情");
        hideObjs(new int[]{R.id.tv_right});
        mproduct = (Goods) getIntent().getSerializableExtra("item");
        if (!mproduct.getImg().equals("")) {
            imgs.add(mproduct.getImg());
        }
        if (!mproduct.getImg1().equals("")) {
            imgs.add(mproduct.getImg1());
        }
        if (!mproduct.getImg2().equals("")) {
            imgs.add(mproduct.getImg2());
        }
        if (!mproduct.getImg3().equals("")) {
            imgs.add( mproduct.getImg3());
        }
        if (!mproduct.getImg4().equals("")) {
            imgs.add( mproduct.getImg4());
        }
        initBanner();
        initV();
    }

    private void initV() {
        tv_name.setText(mproduct.getName());
        tv_publish.setText(mproduct.getManufacturer());
        tv_pzwh.setText(mproduct.getWarrant());
        tv_gg.setText(mproduct.getSpec());
        tv_xh.setText(mproduct.getCode());
        tv_pp.setText(mproduct.getBrand());
        tv_ftype.setText(mproduct.getFclass());
        tv_ctype.setText(mproduct.getCclass());
        tv_room.setText(mproduct.getDepts());
        tv_sm.setText(mproduct.getContent());

    }


    private void initBanner() {
//        if (mproduct.getImg().equals("")) {
//            mContentBanner.setData(R.mipmap.img_nothingbg2);
//        } else {

//        }
        mContentBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                if(model!=null){
                    itemView.setAdjustViewBounds(true);
                    itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                    itemView.setMaxWidth(DensityUtil.getDisplay(ProductDetail.this).getWidth());
                    itemView.setMaxHeight(DensityUtil.dip2px(ProductDetail.this,200));
                }

                Glide.with(ProductDetail.this.getApplicationContext())
                        .load(Url.IMAGEPATH+model)
                        .placeholder(R.mipmap.img_nothingbg3)
                        .error(R.mipmap.img_nothingbg3)
                        .dontAnimate()
                        .into(itemView);
            }


        });
        mContentBanner.setAutoPlayAble(true);
        mContentBanner.setData(imgs, null);
        L.e("imgs:"+imgs.size());
        mContentBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
//                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
//                        Log.e("gogogoo","gogooogogogogo");
                mContentBanner.stopAutoPlay();
                launchActivityWithIntent(new Intent(ProductDetail.this, BigPic.class).putExtra("url", model));
            }
        });
//        mContentBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
//            @Override
//            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
////                Glide.with(ProductDetail.this)
////                        .load(model)
////                        .placeholder(R.mipmap.img_nothingbg2)
////                        .error(R.mipmap.img_nothingbg2)
////                        .fitCenter()
////                        .into(itemView);
//
//                //设置图片宽高比
//                float scale = (float) 750 / (float) 320;
////获取屏幕的宽度
//                WindowManager wm = (WindowManager) ProductDetail.this.getSystemService(Context.WINDOW_SERVICE);
//                Point size = new Point();
//                wm.getDefaultDisplay().getSize(size);
//                int screenWidth = size.x;
////计算BGABanner的应有高度
//                int viewHeight = Math.round(screenWidth / scale);
////设置BGABanner的宽高属性
//                ViewGroup.LayoutParams banner_params = banner.getLayoutParams();
//                banner_params.width = screenWidth;
//                banner_params.height = viewHeight;
//                banner.setLayoutParams(banner_params);
////此处使用的是glide的override函数直接设置图片尺寸
//                Glide.with(ProductDetail.this)
//                        .load(index + model)
//                        .placeholder(R.mipmap.img_nothingbg2)
//                        .override(banner_params.width, banner_params.height)
//                        .into(itemView);
//            }
//        });
//        mContentBanner.setData(imgs, null);
    }

}
