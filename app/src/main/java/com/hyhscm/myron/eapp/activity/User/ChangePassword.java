package com.hyhscm.myron.eapp.activity.User;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.Script;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/11/30.
 * 修改密码
 */

public class ChangePassword extends BaseActivity {
    @BindView(R.id.et1)
    EditText et_origin;
    @BindView(R.id.et2)
    EditText et_new;
    @BindView(R.id.et3)
    EditText et_confirm;
    @BindView(R.id.bt_change)
    Button change;
    @BindView(R.id.tv_check)
    TextView check1;
    @BindView(R.id.tv_check2)
    TextView check2;
    public ChangePassword() {
        super(R.layout.layout_c_pd);
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
        common.changeTitle(this, "修改密码");
        common.hideObjs(this, new int[]{R.id.tv_right});
        common.goBack(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
        et_origin.clearFocus();
        et_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                  if(TextUtils.isEmpty(et_new.getText().toString())){
                           check1.setVisibility(View.GONE);
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
                if(TextUtils.isEmpty(et_confirm .getText().toString())){
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

    @OnClick(R.id.bt_change)
    public void changePassword() {
        if(!et_new.getText().toString().trim().equals(et_confirm.getText().toString().trim())){
            dialog.showWarn("密码不一致，请重新输入！");
            et_new.getText().clear();
            et_confirm.getText().clear();
            et_new.requestFocus();
            check1.setVisibility(View.GONE);
            check2.setVisibility(View.GONE);
            return;
        }
        HttpCore.changePwd("",et_origin.getText().toString(),et_new.getText().toString(),"",0, new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
          if(rs.getSuccess()){
              dialog.showSuccess("修改成功！");
              common.SP_Clear(ChangePassword.this);
              ChangePassword.this.finish();
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
    @OnClick(R.id.tv_forget)
    public void forgetPassword(){
        launchActivity(ForgetPassword.class);
    }
}
