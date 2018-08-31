package com.hyhscm.myron.eapp.activity.User;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 * 修改密码
 */

public class ForgetPassword extends BaseActivity {
    @BindView(R.id.et1)
    EditText phone;
    @BindView(R.id.et2)
    EditText et_new;
    @BindView(R.id.et3)
    EditText et_confirm;
    @BindView(R.id.bt_change)
    Button change;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.getcode)
    Button btn_getCode;
    @BindView(R.id.tv_check)
    TextView check1;
    @BindView(R.id.tv_check2)
    TextView check2;

    private int recLen = 60;

    public ForgetPassword() {
        super(R.layout.layout_forget_pd);
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_c_pd);
//        ButterKnife.bind(this);
//        initView();
//    }

    protected void initView() {
        super.initView();
        common.changeTitle(this, "忘记密码");
        common.hideObjs(this, new int[]{R.id.tv_right});
        et_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    check1.setVisibility(View.GONE);
                    return;
                }else{
                    if(!common.matchPwd(et_new.getText().toString())){
                        check1.setVisibility(View.VISIBLE);
                    }else{
                        check1.setVisibility(View.GONE);
                    }
                }

            }
        });
        et_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    check2.setVisibility(View.GONE);
                    return;
                }else{
                    if(!common.matchPwd(et_confirm.getText().toString())){
                        check2.setVisibility(View.VISIBLE);
                    }else{
                        check2.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    @OnClick(R.id.getcode)
    public void getCode() {
        if (!common.matchPhone(phone.getText().toString())) {
            dialog.dismiss();
            dialog.showWarn("请输入正确的手机号码！");
            phone.requestFocus();
            return;
        }
            HttpCore.getVerifyCode(Url.GETVFCODE_CHANGECODE, phone.getText().toString(), new IResultHandler() {
                @Override
                public void onSuccess(Result rs) {
                    if (rs.getSuccess()) {
                        dialog.dismiss();
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
                            dialog.dismiss();
                            dialog.showError("验证码请求过于频繁，请1小时后再试！");
                        }
                    }
                }
            });
        }

    @OnClick(R.id.bt_change)
    public void changePassword() {
        if(!et_new.getText().toString().trim().equals(et_confirm.getText().toString().trim())){
            dialog.dismiss();
            dialog.showWarn("密码不一致，请重新输入！");
            et_new.getText().clear();
            et_confirm.getText().clear();
            et_new.requestFocus();
            check1.setVisibility(View.GONE);
            check2.setVisibility(View.GONE);
            return;
        }
    HttpCore.changePwd(phone.getText().toString(),"", et_new.getText().toString(), et_code.getText().toString(),1, new IResultHandler() {
        @Override
        public void onSuccess(Result rs) {
            if(rs.getSuccess()){
                new PromptDialog(ForgetPassword.this).showSuccess("修改成功！");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ForgetPassword.this.finish();
                        launchActivity(LoginActivity.class);
                    }
                },500);


            }else{
                new PromptDialog(ForgetPassword.this).showError(rs.getMsg());
            }
        }
    });
    }

    @Override
    public void onBackPressed() {
    launchActivity(LoginActivity.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.dismiss();
        dialog.dismissImmediately();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        dialog.dismissImmediately();
    }
}
