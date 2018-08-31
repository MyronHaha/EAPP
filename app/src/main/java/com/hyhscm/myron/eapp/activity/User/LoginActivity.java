package com.hyhscm.myron.eapp.activity.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.data.BaseResult;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UserInfo;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetBroadcastReceiver;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;

import com.hyhscm.myron.eapp.wxapi.BindPhone;
import com.hyhscm.myron.eapp.wxapi.WXEntryActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.mph.okdroid.response.GsonResHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.leefeng.promptlibrary.OnAdClickListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/12/14.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_right)
    TextView tv_regist;
    @BindView(R.id.et_phone)
    EditText phone;
    @BindView(R.id.et_pass)
    EditText pass;
    @BindView(R.id.getcode)
    Button btn_getCode;     //获取验证码
    @BindView(R.id.tv_verify)  //登录模式
            TextView tv_mode;
    @BindView(R.id.iv_pwstate)
    ImageView iv_pwstate;

    @BindView(R.id.viewpass)
    LinearLayout ll_viewpass;
    boolean isclick = false;
    boolean seClick = false;
    private int recLen = 60;
    InputMethodManager imm;
    private boolean isPassShown = false; //眼睛
    private PromptDialog dialog ;
    public LoginActivity() {
        super(R.layout.layout_login2);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initView() {
        super.initView();
        changeTitle( "登录");
        hideObjs(new int[]{R.id.ll_back});
        tv_regist.setText("注册");
        dialog = new PromptDialog(this);
//        phone.clearFocus();
//        phone.setFocusable(false);

    }

//    @OnClick(R.id.tv_title)
//    public void changeAPPMode(){
//        if(!isclick){
//            isclick = true;
//        }else if(seClick) {
//            if (Url.HOSTNAME.equals("http://hyh.hyhscm.com")) {
//                Url.setIHostName("http://http://10.10.10.88:8080/web.server");
//                Url.setImagePath("http://10.10.10.88:8080//web.server/");
//                Url.setWebPath("http://10.10.10.88:8080//web.server/");
//               dialog.showInfo("已切换到内网");
//            } else {
//                Url.setIHostName("http://hyh.hyhscm.com");
//                Url.setImagePath("http://hyh.hyhscm.com/");
//                Url.setWebPath("http://m.hyhscm.com/");
//                dialog.showInfo("已切换到外网");
//            }
//            isclick =false;
//            seClick = false;
//        }   else if(isclick){
//            seClick =true;
//        }
//
//
//    }
    @OnClick(R.id.tv_right)
    public void regist() {
        common.launchActivity(LoginActivity.this, RegistActivity.class);
    }

    @OnClick(R.id.iv_back)
    public void quit() {
        EAPPApplication.getInstance().exit();
    }

    @OnClick(R.id.tv_verify)
    public void loginMode() {
        if (tv_mode.getText().equals("验证码登录")) {
            tv_mode.setText("密码登录");
            pass.setHint("验证码");
            btn_getCode.setVisibility(View.VISIBLE);
            ll_viewpass.setVisibility(View.GONE);
            btn_getCode.setVisibility(View.VISIBLE);
        } else {
            tv_mode.setText("验证码登录");
            pass.setHint("密码");
            btn_getCode.setVisibility(View.GONE);
            ll_viewpass.setVisibility(View.VISIBLE);
            btn_getCode.setVisibility(View.GONE);
        }
    }
    @OnClick(R.id.tv_forget)
    public void forgetPwd(){
//        LoginActivity.this.finish();
        launchActivity(ForgetPassword.class);
    }
    @OnClick(R.id.bt_login)
    public void login() {
        new PromptDialog(this).showInfo("登录中...");

// 验证码登录
        if(tv_mode.getText().toString().equals("密码登录")){
            if(checkNull(new EditText[]{phone,pass })){
                HttpCore.LoginWithCode(phone.getText().toString(), pass.getText().toString(), new IResultHandler<UserInfo>() {
                    @Override
                    public void onSuccess(final Result<UserInfo> rs) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                launchActivity(MainActivity.class);
                                LoginActivity.this.finish();
                                L.e(rs.getBiz().getName() + "");
                            }
                        }, 500);
                    }
                });
            }
        }
        else{
            if (checkNull(new EditText[]{phone, pass})) {
//            HashMap map = new HashMap();
//            map.put("phone", phone.getText().toString());
//            map.put("pwd", pass.getText().toString());
            HttpCore.Login(phone.getText().toString(), pass.getText().toString(), "", new IResultHandler<UserInfo>() {
                @Override
                public void onSuccess(final Result<UserInfo> var1) {
//                    dialog.dismiss();
                    if(!var1.getSuccess()){
                        return;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            launchActivity(MainActivity.class);
                            LoginActivity.this.finish();
//                            L.e(var1.getBiz().getName() + "");
                        }
                    }, 500);
                }

            });
//            HttpCore.Login(Url.LOGIN, map, new GsonResHandler<Result<UserInfo>>() {
//                @Override
//                public void onFailed(int i, String s) {
//                    common.promptDialog.showError(i + s);
//                }
//
//                @Override
//                public void onSuccess(int i, Result<UserInfo> result) {
//                    L.e(i + "");
//                    if (checkResult(i, result)) {
//                        common.promptDialog.showSuccess("登录成功");
//                        HttpCore.setToken(result.getBiz().getToken());  // 设置token
//                        HttpCore.userId = result.getBiz().getId();      //用户id
//                        HashMap user = new HashMap();                   //保存用户信息
//                        user.put("phone", phone.getText().toString());
//                        user.put("name", result.getBiz().getName());
//                        user.put("p", result.getBiz().getP());
//                        common.SP_Write(LoginActivity.this, user);
//
//                    }
//                }
//            });
        }}
/**
 /********************消息提示框*************/
//        promptDialog.showSuccess("登陆成功");
//        promptDialog.showError("登录失败");
//        promptDialog.showWarn("注意");
//        promptDialog.showInfo("成功了");
//        promptDialog.showCustom(R.mipmap.ic_launcher, "自定义图标的");
//        promptDialog.dismiss();
//        promptDialog.dismissImmediately();
    }
    //获取验证码
    @OnClick(R.id.getcode)
    public void getCode() {
        if (phone.getText().toString().equals("")) {
            new PromptDialog(BaseActivity._mActivity).showWarn("手机号不能为空！");
            phone.requestFocus();
        } else {
            HttpCore.getVerifyCode(Url.GETVFCODE_LOGIN, phone.getText().toString(), new IResultHandler() {
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
                                       btn_getCode.setBackgroundResource(R.drawable.shape_tag_grey);
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
                    }else{
                      if(rs.getErrorCode().equals("-2")){
                          dialog.showError("验证码发送失败！");
                      }
                    }
                }
            });
        }
    }
    @OnClick(R.id.rl_phone)
    public void phoneArea(){
        phone.requestFocus();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(phone.getWindowToken(),0);
    }
    @OnClick(R.id.rl_pass)
    public void passArea(){
        pass.requestFocus();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pass.getWindowToken(),0);
    }
    @OnClick(R.id.viewpass)
    public void changePassView(){

        if(isPassShown){
            L.e("ispassshow");
            iv_pwstate.setImageResource(R.mipmap.icon_xianshi);
            pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT );
            isPassShown = false;
        }else{
            L.e("notpassshow");
            iv_pwstate.setImageResource(R.mipmap.icon_yincang);
            pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPassShown = true;
        }
    }
    // 检测 输入
    public boolean checkNull(EditText[] inputArr) {
        for (EditText et : inputArr) {
            if (TextUtils.isEmpty(et.getText().toString())) {
                 try{
                     dialog.dismissImmediately();
                     dialog.showWarn(et.getHint().toString() + "不能为空");
                     et.requestFocus();
                 }catch(Exception e){
                     L.e(e.toString());
                 }
                return false;
            }
        }
        return true;
    }

//    public boolean checkResult(int status, BaseResult object) {
//        if (status == 200 && object.getSuccess()) {
//            return true;
//        } else {
//            if (status != 200) {
//                common.promptDialog.showWarn("请求失败");
//            }
//            if (!object.getSuccess()) {
//                common.promptDialog.showError(object.getMsg());
//            }
//        }
//        return false;
//    }
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


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        dialog.dismissImmediately();
    }

    @Override
    protected void onResume() {
        super.onResume();
        phone.clearFocus();
        pass.clearFocus();
        dialog.dismiss();
        dialog.dismissImmediately();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public void onBackPressed() {

    }


}
