package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UploadRe;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.UriToPathUtil;
import com.hyhscm.myron.eapp.utils.common;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;


import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/12/06.
 * 我的展厅
 */

public class MyHall extends BaseActivity {
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.et4)
    EditText et4;
    @BindView(R.id.iv_add)
    ImageView iv_add;
    ImagePicker imagePicker = new ImagePicker();
    Merchants merchants;
    PromptDialog dialog;

    public MyHall() {
        super(R.layout.layout_myhall);
    }

    protected void initView() {
        super.initView();
        merchants = new Merchants();
        dialog = new PromptDialog(MyHall.this);
        common.changeTitle(this, "我的展厅");
        common.hideObjs(this, new int[]{R.id.tv_right});
    }

    @Override
    protected void initData() {
        HttpCore.getHallDetail(HttpCore.userId + "", new IResultHandler<Merchants>() {
            @Override
            public void onSuccess(Result<Merchants> rs) {
                if (rs.getSuccess()) {
                    Merchants merchants = rs.getBiz();
                    if (merchants.getCompanyName().equals(""))
                        et1.setHint("请输入公司名字");
                    else
                        et1.setText(rs.getBiz().getCompanyName());

                    if (merchants.getContent().equals(""))
                        et2.setText("请输入公司简介");
                    else
                        et2.setText(rs.getBiz().getContent());

                    if (merchants.getLinkman().equals(""))
                        et3.setHint("请输入联系人");
                    else
                        et3.setText(rs.getBiz().getLinkman());

                    if (merchants.getLinkmanTel().equals(""))
                        et4.setHint("请输入联系人电话");
                    else
                        et4.setText(rs.getBiz().getLinkmanTel());
                    Glide.with(MyHall.this)
                            .load(Url.IMAGEPATH + merchants.getImg())
                            .error(R.mipmap.img_head)
                            .into(iv_add);
                }

            }

    });
}

    /*调用相册*/
    private void initPhotoPicker() {
        // 设置标题
        imagePicker.setTitle("选择展厅相片");
// 设置是否裁剪图片
        imagePicker.setCropImage(false);
// 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
                Glide.with(MyHall.this).load(imageUri)
                        .into(iv_add);
                HttpCore.UploadImage(UriToPathUtil.getImageAbsolutePath(MyHall.this, imageUri), new IResultHandler<UploadRe>() {
                    @Override
                    public void onSuccess(Result<UploadRe> rs) {
                        if (rs.getSuccess()) {
                            merchants.setImg(rs.getBiz().getPath());
                        } else {
                            L.e("UploadImage" + rs.getMsg());
                        }
                    }
                });
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
                MyToast.makeText(MyHall.this, "用户拒绝了授权！", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // 监听
    @OnClick(R.id.bt_change)
    public void changeInfo() {
        if (checkNull()) {
            dialog.showInfo("修改中...");
            merchants.setCompanyName(et1.getText().toString());
            merchants.setContent(et2.getText().toString());
            merchants.setLinkman(et3.getText().toString());
            merchants.setLinkmanTel(et4.getText().toString());
            HttpCore.changeHallInfo(merchants, new IResultHandler<Merchants>() {
                @Override
                public void onSuccess(Result<Merchants> rs) {
                    if(rs.getSuccess()){
                        dialog.showSuccess("修改成功！");
                    }
                }
            });
//            Gson gson = new Gson();
//            Log.e("gg",   gson.toJson(merchants));
//            HttpCore.
        }

    }

    private boolean checkNull() {
        if (et1.getText().equals("")) {
            dialog.showInfo("公司名称不能为空");
            return false;
        }  if (et2.getText().equals("")) {
            dialog.showInfo("公司简介不能为空");
            return false;
        }  if (et3.getText().equals("")) {
            dialog.showInfo("联系人不能为空");
            return false;
        }  if (et4.getText().equals("")) {
            dialog.showInfo("联系电话不能为空");
            return false;
        }
        return true;
    }

    @OnClick(R.id.iv_add)
    public void showPicker() {
        initPhotoPicker();
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


}
