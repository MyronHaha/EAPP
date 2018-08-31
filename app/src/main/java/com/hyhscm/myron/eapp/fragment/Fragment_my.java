package com.hyhscm.myron.eapp.fragment;


import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.MyWeb;
import com.hyhscm.myron.eapp.activity.User.AboutUs;
import com.hyhscm.myron.eapp.activity.User.AgentSetting;
import com.hyhscm.myron.eapp.activity.User.MyCorn;
import com.hyhscm.myron.eapp.activity.User.MyHall;
import com.hyhscm.myron.eapp.activity.User.MyNeed;
import com.hyhscm.myron.eapp.activity.User.MyProduct;
import com.hyhscm.myron.eapp.activity.User.NeedPublish;
import com.hyhscm.myron.eapp.activity.User.UserSetting;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.data.AccredInfo;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.SignResult;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.updateApp.UpdateApp;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.mph.okdroid.response.RawResHandler;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/20.
 */

public class Fragment_my extends Fragment {
    @BindView(R.id.iv_user)
    ImageView userIcon;
    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;
    @BindView(R.id.rl_agent)
    RelativeLayout rl_agent;
    @BindView(R.id.rl_need)
    RelativeLayout rl_xuqiu;
    @BindView(R.id.rl_product)
    RelativeLayout rl_product;
    @BindView(R.id.tv_user)
    TextView tv_name;
    // 刷新头像姓名
    protected String ImgURL;
    protected String name;

    //更新
    @BindView(R.id.tcurrent)
    TextView tv_current;
    @BindView(R.id.tips)
    TextView tips;

    //签到
    @BindView(R.id.tv_sign)
    TextView sign;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        if(NetUtil.getNetWorkState(getActivity())==NetUtil.NETWORK_NONE){
            MyToast.makeText(getActivity(),"请检查网络连接",1000).show();
            return;
        }else {
//            initData();
        }
    }
    private  void initData(){

        // 查询签到
        HttpCore.getSignInfo(new RawResHandler() {
            @Override
            public void onFailed(int i, String s) {

            }

            @Override
            public void onSuccess(int i, String s) {
                try{
                    JSONObject object = new JSONObject(s) ;
                    JSONObject item = new JSONObject(object.getString("biz"));
                    if(object.getBoolean("success")){
                        if( item.getString("hasSign") .equals("1")){
                            sign.setText("已签到");
                            sign.setBackgroundResource(R.drawable.unsolid_round_primary);
                            sign.setTextColor(Color.parseColor("#3db0ff"));
                        }else{
                            sign.setText("签到");
                            sign.setBackgroundResource(R.drawable.unsolid_round_pink);
                            sign.setTextColor(Color.parseColor("#ff6aad"));
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void initView() {
//        common.hideObjs(getActivity(), new int[]{R.id.iv_back, R.id.tv_right});
//        common.changeTitle(getActivity(), "我的");
        initUserIcon();
        tv_current.setText("当前版本："+common.getCurrentVerName());
        L.e("newversion:"+HttpCore.newVersionName);
        if(!TextUtils.isEmpty(HttpCore.newVersionName)){
            tips.setText("更新版本至-"+HttpCore.newVersionName);
        }
    }

    private void initUserIcon() {
        ImgURL = HttpCore.img;
        name = HttpCore.name;
        tv_name.setText(name);
        // 替换 url
        Glide.with(this)
                .load(Url.IMAGEPATH+ImgURL)
                .transform(new GlideCircleTransform(getActivity()))
                .error(R.mipmap.img_head)
                .placeholder(R.mipmap.img_head)
                .into(userIcon);
        L.e(Url.IMAGEPATH+HttpCore.img);
    }

    @OnClick(R.id.rl_setting)
    public void UserSetting(){
        common.launchActivity(getActivity(), UserSetting.class);
    }
    @OnClick(R.id.rl_agent)
    public void agentSetting(){
        common.launchActivity(getActivity(), AgentSetting.class);
    }
    @OnClick(R.id.rl_need)
    public void myNeed(){
        common.launchActivity(getActivity(), MyNeed.class);
    }
    @OnClick(R.id.rl_product)
    public void myProduct(){
        common.launchActivity(getActivity(), MyProduct.class);
    }
    @OnClick(R.id.rl5)
    public void myHall(){
        HttpCore.getAccredInfo(new IResultHandler<AccredInfo>() {
            @Override
            public void onSuccess(Result<AccredInfo> rs) {

                int state = -100;

                if(rs.getSuccess()){
                    state = rs.getBiz().getState();
                    if(state==2){
                        common.launchActivity(getActivity(), MyHall.class);
                    }else{
                        new PromptDialog(getActivity()).showWarn("认证审核暂未通过，无法查看和设置！");
                    }
                }
            }
        });

    }
    @OnClick(R.id.rl6)
    public void myCorn(){
//        MyToast.makeText(getActivity(),"暂未开放，敬请期待！", Toast.LENGTH_SHORT).show();
        common.launchActivity(getActivity(), MyCorn.class);
    }
    @OnClick(R.id.rl7)
    public void aboutUs(){
        common.launchActivity(getActivity(), AboutUs.class);
    }
    //更新；
    @OnClick(R.id.rl8)
    public void updateApp(){
        UpdateApp.getInstance(getActivity()).checkPermission(getActivity(),true);
    }

    @OnClick(R.id.rl9)
    public void chatLine(){
        Intent intent  = new Intent(getActivity(), MyWeb.class);
        intent.putExtra("url","http://10.10.10.159/m_echain/service.html");
        startActivity(intent);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            //更新 昵称和头像；
            if(!name.trim().equals(HttpCore.name.trim()) ){
                tv_name.setText(HttpCore.name);
                name = HttpCore.name;
            }
            if(ImgURL!=null&&HttpCore.img!=null){
                if(!ImgURL.trim().equals(HttpCore.img.trim())){
                    Glide.with(this)
                            .load(Url.IMAGEPATH+HttpCore.img)
                            .transform(new GlideCircleTransform(getActivity()))
                            .error(R.mipmap.img_head)
                            .placeholder(R.mipmap.img_head)
                            .into(userIcon);
                    ImgURL = HttpCore.img;
                }
            }


            // 查询签到
            HttpCore.getSignInfo(new RawResHandler() {
                @Override
                public void onFailed(int i, String s) {

                }

                @Override
                public void onSuccess(int i, String s) {
                    try{
                        JSONObject object = new JSONObject(s) ;
                        JSONObject item = new JSONObject(object.getString("biz"));
                        if(object.getBoolean("success")){
                            if( item.getString("hasSign") .equals("1")){
                                sign.setText("已签到");
                                sign.setBackgroundResource(R.drawable.unsolid_round_primary);
                                sign.setTextColor(Color.parseColor("#3db0ff"));
                            }else{
                                sign.setText("签到");
                                sign.setBackgroundResource(R.drawable.unsolid_round_pink);
                                sign.setTextColor(Color.parseColor("#ff6aad"));
                            }
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @OnClick(R.id.tv_sign)
    public void sign(){
        if (!sign.getText().equals("已签到")) {
            HttpCore.Sign(new IResultHandler<SignResult>() {
                @Override
                public void onSuccess(Result<SignResult> rs) {
                    if (rs.getSuccess()) {
                        String getCorn =   rs.getBiz().getQty(); // 获得金币
                        sign.setText("已签到");
                        sign.setTextColor(Color.parseColor("#3db0ff"));
                        sign.setBackgroundResource(R.drawable.unsolid_round_primary);
                        ((BaseActivity)getActivity()).showSignResult(getActivity(),getActivity().getLayoutInflater().inflate(R.layout.tips_signforbean,null),R.id.tv_signresult,"获得"+getCorn+"颗e金豆");
                    }else{
                        sign.setText("签到");
                        sign.setTextColor(Color.parseColor("#ff6aad"));
                        sign.setBackgroundResource(R.drawable.unsolid_round_pink);
                    }
                }
            });
        }else{
            MyToast.makeText(getActivity(),"今天已签到过了，明天继续吧！",1500).show();
        }
    }

    @OnClick(R.id.test)
    public void showView(){
        Demand demand = new Demand();
        View view  = getActivity().getLayoutInflater().inflate(R.layout.wx_share_dialog_success,null);
        ((BaseActivity)getActivity()).showCustomDialog(view,demand,1,false);
    }
}
