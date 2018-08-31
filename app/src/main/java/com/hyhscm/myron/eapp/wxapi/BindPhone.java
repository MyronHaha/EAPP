package com.hyhscm.myron.eapp.wxapi;

import android.content.Intent;
import android.os.Handler;
import android.widget.EditText;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;

import org.apache.http.protocol.HTTP;


import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2018/2/27.
 */

public class BindPhone extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText phone;
    private String code = "";

    public BindPhone() {
        super(R.layout.layout_bind);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        changeTitle("绑定手机号");
        code = getIntent().getStringExtra("code");

    }

    @OnClick(R.id.bt_bind)
    public void bindPhone() {
        if (phone.getText().toString().equals("")) {
            dialog.showWarn("请输入手机号！");
            return;
        } else if (!common.matchPhone(phone.getText().toString())) {
            dialog.showWarn("请输入正确的手机号码！");
        }
        //HttpCore.setToken("");
        HttpCore.loginWx(code, phone.getText().toString(), new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
                if (!rs.getSuccess() && String.valueOf(rs.getBiz()).equals("1")) {

                    MyToast.makeText(BindPhone.this, "微信号未绑定！", 1000).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(BindPhone.this, BindPhone.class);
                            intent.putExtra("code", code);
                            launchActivityWithIntent(intent);
                        }
                    }, 500);

                } else if (!rs.getSuccess() && String.valueOf(rs.getBiz()).equals("2")) {
                    MyToast.makeText(BindPhone.this, "手机号已注册，请获取验证码登录！", 1000).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(BindPhone.this, LoginActivity.class);
                            launchActivityWithIntent(intent);
                        }
                    }, 500);

                }else if(!rs.getSuccess()){
                    L.e("微信登录"+rs.getMsg()+"_errorCode:"+rs.getErrorCode());
                    MyToast.makeText(BindPhone.this, rs.getMsg(), 1000).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(BindPhone.this, LoginActivity.class);
                            launchActivityWithIntent(intent);
                        }
                    }, 500);
                }
            }
        });
    }
}
