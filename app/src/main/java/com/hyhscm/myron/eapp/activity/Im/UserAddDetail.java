package com.hyhscm.myron.eapp.activity.Im;

import android.icu.text.IDNA;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.Contract;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import javax.microedition.khronos.opengles.GL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jason on 2018/1/30.
 * <p>
 * "areas": "",
 * "b": null,
 * "cid": 1,
 * "cname": "合壹汇",
 * "creationTime": null,
 * "creatorId": 0,
 * "fc": 0,
 * "fsc": 0,
 * "hasf": 0,
 * "id": 41,
 * "nname": "合壹汇",
 * "oid": 1,
 * "pc": 0,
 * "pic": "",
 * "sign": "",
 * "sourceId": 0,
 * "sourceName": "",
 * "sourceParam": "",
 * "sourceType": 0,
 * "state": 0,
 * "uiid": "HyhYEWHyhpS4lOZwb8P5AaEgsfhYTgzh",
 * "updateId": 0,
 * "updateTime": null
 */

public class UserAddDetail extends BaseActivity {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_level)
    TextView level;
    @BindView(R.id.tv_vip)
    TextView vip;
    @BindView(R.id.tv_signature)
    TextView signature;
    @BindView(R.id.tv_area)
    TextView area;

    @BindView(R.id.iv)
    ImageView userIv;
    @BindView(R.id.iv_vip)
    ImageView vipIv;
    @BindView(R.id.et_remarks)
    EditText remarks;
    @BindView(R.id.et_info)
    EditText info;

    private Contract contract;

    public UserAddDetail() {
        super(R.layout.layout_friend_add);
    }

    protected void initView() {
        super.initView();
        titleBar.setTitle("详细资料");
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAddDetail.this.finish();
            }
        });
    }

    protected void initData() {
        super.initData();
        try {
            contract = (Contract) getIntent().getSerializableExtra("item");
            name.setText(contract.getNname());
            Glide.with(this).load(Url.IMAGEPATH + contract.getPic()).error(R.mipmap.img_head).into(userIv);
            signature.setText(contract.getSign());
            area.setText(contract.getAreas());

            //            level.setText("等级");
            if(contract.getState()!=0){
               vipIv.setImageResource(R.mipmap.icon_vip_y);
               vip.setBackgroundResource(R.drawable.shape_vip);
            }else{
                vipIv.setImageResource(R.mipmap.icon_vip_b);
                vip.setBackgroundResource(R.drawable.shape_unvip);
            }

        } catch (Exception e) {

        }

    }
    @OnClick(R.id.bt_add)
    public void addFriend(){
        String cName = remarks.getText().toString();
        final String infoV = info.getText().toString();
        //参数为要添加的好友的username和添加理由
        try {
//            EMClient.getInstance().contactManager().addContact(contract.getUiid(), infoV);
            HttpCore.addFriend(contract.getCid() + "", cName, infoV, new IResultHandler<com.hyphenate.easeui.model.Contract>() {
                @Override
                public void onSuccess(Result<com.hyphenate.easeui.model.Contract> rs) {
                    if(rs.getSuccess()){
                        try {
                            EMClient.getInstance().contactManager().addContact(contract.getUiid(),infoV);
                            common.promptDialog.showSuccess("请求发送成功！");
                            SystemClock.sleep(500);
                            UserAddDetail.this.finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
