package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.AccredInfo;
import com.hyhscm.myron.eapp.data.Accreditation;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UploadRe;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.UriToPathUtil;
import com.hyhscm.myron.eapp.utils.common;
import com.linchaolong.android.imagepicker.ImagePicker;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 * 深度认证 未认证 选择方式
 * 已认证  显示状态
 */

public class UserVerify extends BaseActivity {
    private Boolean reVfy = false;

    public UserVerify() {
        super(R.layout.layout_verify);
    }

    private AccredInfo info = null;//认证信息；
    private Intent intent = null; // 保存intent
    protected void initView() {
        super.initView();
        common.changeTitle(this, "深度认证");
        common.hideObjs(this, new int[]{R.id.tv_right});
        intent = getIntent();
    }

    @Override
    protected void onResume() {
L.e("onresume");
        if(intent.getBooleanExtra("revfy",false)){
            super.onResume();
        }else {
            super.onResume();
            HttpCore.getAccredInfo(new IResultHandler<AccredInfo>() {
                @Override
                public void onSuccess(Result<AccredInfo> rs) {
                    if (rs.getSuccess()) {
                        info = rs.getBiz();
                        if (info.getCtype() == 1) { // 企业
                            Intent intent = new Intent(UserVerify.this, UserVerify_Su_Company.class);
                            intent.putExtra("item", info);
                            launchActivityWithIntent(intent);
                            UserVerify.this.finish();
                        } else if (info.getCtype() == 2) {
                            Intent intent = new Intent(UserVerify.this, UserVerify_Su_Person.class);
                            intent.putExtra("item", info);
                            launchActivityWithIntent(intent);
                            UserVerify.this.finish();
                        }else{
                            return;
//                            UserVerify.this.onResume();
                        }

                    }
                }
            });
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.ll_person)
    public void personal_verify() {
        launchActivity(VfyPersonal.class);
        UserVerify.this.finish();
    }

    @OnClick(R.id.ll_company)
    public void company_verify() {
        launchActivity(VfyCompany.class);
        UserVerify.this.finish();
    }
}
