package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.AccredInfo;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.Builder;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 * 深度认证 success
 */

public class UserVerify_Su_Person extends BaseActivity {

    @BindView(R.id.tv_right)
    TextView right;
    @BindView(R.id.iv_ssmall)
    ImageView iv_ssmall; //xiao
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.iv_type)
    ImageView iv_type;
    @BindView(R.id.iv_sb)
    ImageView iv_sb;//da
    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_add)
    TextView address;
    @BindView(R.id.tv_phone)
    TextView phone;
   @BindView(R.id.iv1)
           ImageView iv1;
   @BindView(R.id.iv2)
           ImageView iv2;

   @BindView(R.id.tv_result)
           TextView tv_result;
   @BindView(R.id.ll_error)
    LinearLayout error;
    AccredInfo info = null;
    private int state = -100;

    public UserVerify_Su_Person() {
        super(R.layout.layout_vfy_state);
    }

    protected void initView() {
        super.initView();
        right.setText("重新认证");
        info = (AccredInfo) getIntent().getSerializableExtra("item");
        iv_type.setImageResource(R.mipmap.img_grrz);
        state = info.getState(); //认证状态
        switch (state) {
            case -1:
                changeUi(R.mipmap.icon_wtg1, R.mipmap.icon_wtg2, "认证失败");
                error.setVisibility(View.VISIBLE);
                tv_result.setText(info.getResult());
                break;
            case 1:
                changeUi(R.mipmap.icon_shz1, R.mipmap.icon_shz2, "审核中");
                break;
            case 2:
                changeUi(R.mipmap.icon_yrz1, R.mipmap.icon_yrz2, "认证成功");
                break;
        }
        name.setText(info.getName());
        address.setText(info.getAddress());
        phone.setText(info.getLinkmanTel());
        Glide.with(this)
                .load(Url.IMAGEPATH+info.getImg1())
                .error(R.drawable.ic_default_image)
                .into(iv1);

    }

    //重新认证
    @OnClick(R.id.tv_right)
    public void reVertify() {
        final PromptDialog dialog  = new PromptDialog(this);
        dialog.getAlertDefaultBuilder().icon(R.mipmap.img_head);
        PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {
                Intent intent = new Intent(UserVerify_Su_Person.this,UserVerify.class);
                intent.putExtra("revfy",true);
                launchActivityWithIntent(intent);
                UserVerify_Su_Person.this.finish();

            }
        });
        confirm.setFocusBacColor(Color.parseColor("#3db0ff"));
        dialog.showWarnAlert("是否确认重新认证？", new PromptButton("取消", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {
            }
        }), confirm);
    }


    public void changeUi(int ssmal, int sb, String state) {
        iv_ssmall.setImageResource(ssmal);
        iv_sb.setImageResource(sb);
        tv_state.setText(state);
        changeTitle(state);
    }
}
