package com.hyhscm.myron.eapp.activity.User;

import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.data.AccredInfo;
import com.hyhscm.myron.eapp.data.Bizinfo;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 */

public class UserSetting extends BaseActivity {
    @BindView(R.id.r2)
    RelativeLayout r2; //修改密码
    @BindView(R.id.r3)
    RelativeLayout r3;//账号信息
    @BindView(R.id.r4)
    RelativeLayout r4; //深度认证
    @BindView(R.id.r5)   //关于我们
            RelativeLayout r5;
    @BindView(R.id.r6)  //反馈
            RelativeLayout r6;
    @BindView(R.id.r7)  //反馈
            RelativeLayout r7;
@BindView(R.id.tv_vstate)
TextView tv_state;
    public UserSetting(){
        super(R.layout.layout_setting);
    }
    protected void initView() {
        super.initView();
        common.changeTitle(this, "设置");
        common.hideObjs(this, new int[]{R.id.tv_right});
    }
    protected void initData(){
        super.initData();
        HttpCore.getAccredInfo(new IResultHandler<AccredInfo>() {
            @Override
            public void onSuccess(Result<AccredInfo> rs) {
                int type = -100;
                int state = -100;
                String t ="";
                String s = "";
                if(rs.getSuccess()){
                    state = rs.getBiz().getState();
                    type = rs.getBiz().getCtype();
                    switch (state){
                        case -1:
                            s = "认证失败";
                            break;
                        case 1:
                            s = "审核中";
                            break;
                        case 2:
                            s = "认证成功";
                            break;
                    }
                    switch (type){
                        case 1:
                            t = "企业认证";
                            break;
                        case 2:
                            t = "个人认证";
                            break;
                        default:
                            t="未认证";
                            break;

                    }
                    L.e("s"+state);
                    tv_state.setText(t+" "+s);
                }
            }
        });
    }
    @OnClick(R.id.r2)
    public void changePassword() {
        common.launchActivity(this, ChangePassword.class);
    }

    @OnClick(R.id.r3)
    public void accountInfo() {
        common.launchActivity(this, AccountInfo.class);
    }

    @OnClick(R.id.r4)
    public void authorize() {
        common.launchActivity(this, UserVerify.class);
    }



    @OnClick(R.id.r6)
    public void feedBack() {
        common.launchActivity(this, FeedBack.class);
    }
    @OnClick(R.id.r7)
    public void aboutUs() {
        MyToast.makeText(UserSetting.this,"关于我们", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.r8)
    public void dislogin() {
        final PromptDialog dialog  = new PromptDialog(this);
        PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {

        if(  common.SP_Clear(UserSetting.this.getApplicationContext())){
            EAPPApplication.getInstance().clearActivities();
            HttpCore.isLogin = false;
            HttpCore.setToken("");
            dialog.showInfo("请稍后...");
            EMClient.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgress(int progress, String status) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int code, String message) {
                    // TODO Auto-generated method stub

                }
            });//退出环信;
            SystemClock.sleep(500);
            launchActivity(LoginActivity.class);
        }
            }
        });
        confirm.setFocusBacColor(Color.parseColor("#ff5959"));
        dialog.showWarnAlert("你确定要退出登录？", new PromptButton("取消", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {
            }
        }), confirm);
//        dialog.showInfo("请稍后...");
//        if(  common.SP_Clear(this)){
//            EAPPApplication.getInstance().clearActivities();
//            SystemClock.sleep(1000);
//            launchActivity(LoginActivity.class);
//        }
    }

}
