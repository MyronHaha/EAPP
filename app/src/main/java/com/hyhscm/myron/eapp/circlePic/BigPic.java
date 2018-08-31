package com.hyhscm.myron.eapp.circlePic;

import android.app.Activity;
import android.media.Image;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.net.Url;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Jason on 2018/2/7.
 */

public class BigPic extends BaseActivity{
    @BindView(R.id.photoView)
    PhotoView view ;
    String imgUrl = "";
    public BigPic(){
        super(R.layout.layout_bigpic);
    }
    protected void initView(){
        super.initView();

    }
    protected void initData(){
        super.initData();
        imgUrl = getIntent().getStringExtra("url");
        if(!"".equals(imgUrl)){
            Glide.with(this).load(Url.IMAGEPATH+imgUrl)
                    .error(R.mipmap.img_nothingbg3).into(view);

        }
    }
    @OnClick(R.id.iv_back)
    public void back(){
        this.finish();
    }
}
