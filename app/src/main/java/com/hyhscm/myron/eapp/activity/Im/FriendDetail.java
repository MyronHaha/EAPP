package com.hyhscm.myron.eapp.activity.Im;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.Im.runtimepermissions.PermissionsManager;
import com.hyhscm.myron.eapp.activity.Im.runtimepermissions.PermissionsResultAction;
import com.hyhscm.myron.eapp.data.Contract;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyphenate.easeui.ui.EaseChatFragment;

import org.apache.http.protocol.HTTP;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Myron on 2018/1/16.
 * 用户类别？
 * 渠道？
 */
public class FriendDetail extends BaseActivity {
    @BindView(R.id.tv_user)
    TextView tv_name;
    @BindView(R.id.resident)
    TextView tv_resident;
    @BindView(R.id.iv_user)
    ImageView iv_user;
    @BindView(R.id.tv_signature)
            TextView tv_sign;
    @BindView(R.id.tv_remarks)
            TextView tv_remarks;
    Contract contract = null;

    public FriendDetail() {
        super(R.layout.layout_friend_detail);
    }

    protected void initView() {
        super.initView();
    }

    protected void initData() {
        super.initData();
        try {
            contract = (Contract) getIntent().getSerializableExtra("item");
            if (contract != null) {
                tv_name.setText(contract.getNname());
                Glide.with(this)
                        .load(Url.IMAGEPATH + contract.getPic())
                        .error(R.mipmap.img_head)
                        .into(iv_user);
                tv_sign.setText(contract.getSign());
                tv_remarks.setText(contract.getCname());
                tv_resident.setText(contract.getAreas());
            }
        } catch (Exception e) {

        }
    }
@OnClick(R.id.send)
    public void toChat(){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, contract.getUiid());
        startActivity(intent);
}
@OnClick(R.id.delete)
    public void deleteContract(){
    HttpCore.deleteFriend(contract.getId()+"", new IResultHandler<com.hyphenate.easeui.model.Contract>() {
        @Override
        public void onSuccess(Result<com.hyphenate.easeui.model.Contract> rs) {
            if(rs.getSuccess()){
                dialog.showSuccess("删除成功！");
                FriendDetail.this.finish();
            }else{
                dialog.showError(rs.getMsg());
            }
        }
    });
}
}