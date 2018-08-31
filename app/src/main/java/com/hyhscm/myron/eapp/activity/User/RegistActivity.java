package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.MyWeb;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/12/14.
 */

public class RegistActivity extends BaseActivity {
    @BindView(R.id.tv_right)
    TextView tv_login;
    @BindView(R.id.et_phone)
    EditText phone;
    @BindView(R.id.et_pass)
    EditText pass;
    @BindView(R.id.getcode)
    Button btn_getCode;     //获取验证码
    @BindView(R.id.check_group)
    RadioGroup group;
    @BindView(R.id.ck1)
    RadioButton ck1;
    @BindView(R.id.ck2)
    RadioButton ck2;
    @BindView(R.id.ck3)
    RadioButton ck3;
    //用户协议
    @BindView(R.id.cb_agree)
    CheckBox cb_agree;
   //注册按钮
    @BindView(R.id.bt_login)
    Button bt_regist;
    private int recLen = 60;
   int type =1;
   private PromptDialog dialog;
    public RegistActivity() {
        super(R.layout.layout_regist2);
    }


    //    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_regist);
//        ButterKnife.bind(this);
//        EAPPApplication.getInstance().addActivity(this);
//        initView();
//    }
    //获取验证码
    @OnClick(R.id.getcode)
    public void getCode() {
        if (!common.matchPhone(phone.getText().toString())) {
            dialog.showWarn("请输入正确的手机号码！");
            phone.requestFocus();
        } else {
            HttpCore.getVerifyCode(Url.GETVFCODE_REGIST, phone.getText().toString(), new IResultHandler() {
                @Override
                public void onSuccess(Result rs) {
                    if (rs.getSuccess()) {
                        dialog.showSuccess("验证码发送成功！");
                        final Timer timer = new Timer();
                        // 倒计时
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {  // UI thread
                                    @Override
                                    public void run() {
                                        recLen--;
                                        btn_getCode.setEnabled(false);
                                        btn_getCode.setBackgroundResource(R.drawable.shape_tag_grey_dark);
                                        if (recLen <= 0) {
                                            timer.cancel();
                                            btn_getCode.setEnabled(true);
                                            btn_getCode.setBackgroundResource(R.drawable.shape_tag_blue);
                                            recLen = 60;
                                            btn_getCode.setText("重新获取");
                                        }else {
                                            btn_getCode.setText("重新获取/"+recLen + "s");
                                        }
                                    }

                                });

                            }

                        };
                        timer.schedule(task, 1000, 1000);
                    } else {
                        if (rs.getErrorCode().equals("-2")) {
                            dialog.showError("验证码请求过于频繁，请1小时后再试！");
                        }
                    }
                }
            });
        }
    }

    protected void initView() {
        super.initView();
        dialog = new PromptDialog(this);
        common.changeTitle(this, "注册");
        tv_login.setText("登录");
        phone.clearFocus();
        pass.clearFocus();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.ck1) {
                    ck1.setBackgroundResource(R.drawable.shape_tag_blue);
                    ck1.setTextColor(Color.parseColor("#ffffff"));
                    type = 1;
                } else {
                    ck1.setBackgroundColor(Color.parseColor("#ffffff"));
                    ck1.setTextColor(Color.parseColor("#808080"));

                }
                if (id == R.id.ck2) {
                    ck2.setBackgroundResource(R.drawable.shape_tag_blue);
                    ck2.setTextColor(Color.parseColor("#ffffff"));
                    type =2;
                } else {
                    ck2.setBackgroundColor(Color.parseColor("#ffffff"));
                    ck2.setTextColor(Color.parseColor("#808080"));
                }
                if (id == R.id.ck3) {
                    ck3.setBackgroundResource(R.drawable.shape_tag_blue);
                    ck3.setTextColor(Color.parseColor("#ffffff"));
                    type =3;
                } else {
                    ck3.setBackgroundColor(Color.parseColor("#ffffff"));
                    ck3.setTextColor(Color.parseColor("#808080"));
                }
            }

        });
      cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(b){
                  L.e("check",b+"");
                  bt_regist.setBackgroundResource(R.drawable.shape_tag_blue);
                  bt_regist.setTextColor(Color.parseColor("#ffffff"));
              }else{
                  L.e("check",b+"");
                  bt_regist.setBackgroundResource(R.drawable.shape_tag_white);
                  bt_regist.setTextColor(Color.parseColor("#c7c7c7"));
              }
          }
      });
    }

    @OnClick(R.id.tv_right)
    public void login() {
        launchActivity(LoginActivity.class);
    }

@OnClick(R.id.bt_login)
    public void regist(){
    if(pass.getText().toString().equals("")){
        new PromptDialog(RegistActivity.this).showWarn("验证码不能为空！");
        pass.requestFocus();
        return;
    }
    if(!cb_agree.isChecked()){
        dialog.showWarn("请阅读并确认服务条款！");
        return;
    }
        HttpCore.Regist(phone.getText().toString(),pass.getText().toString(),type+"", new IResultHandler<String>() {
            @Override
            public void onSuccess(Result<String> rs) {
                if(rs.getSuccess()){
                    dialog.showSuccess("注册成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            launchActivity(LoginActivity.class);
                        }
                    },500);
                }else{
                    dialog.showError(rs.getMsg());
                }
            }
        });
}
    @OnClick(R.id.tv_agree)
    public void readRegistAgreement(){
        Intent intent = new Intent(RegistActivity.this ,MyWeb.class);
        intent.putExtra("url",Url.REGIST_AGREEMENT);
        launchActivityWithIntent(intent);
    }
    @OnClick(R.id.iv_wechat)
    public void WxLogin(){
        setWXLogin();
    }
    /**
     * 微信登录
     */
    private void setWXLogin() {
        IWXAPI api = (EAPPApplication.getInstance()).getWxApi();
        if (api != null && api.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "123";
            boolean  result  =  api.sendReq(req);
            Log.e("callWxClient",result+"");
        } else {
            MyToast.makeText(this, "您尚未安装微信", Toast.LENGTH_SHORT).show();
        }
    }

}
