package com.hyhscm.myron.eapp.activity.User;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.data.AccredInfo;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UploadRe;
import com.hyhscm.myron.eapp.data.User;
import com.hyhscm.myron.eapp.data.UserInfo;
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
import com.mph.okdroid.response.GsonResHandler;


import java.util.HashMap;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 * 账号设置
 */

public class AccountInfo extends BaseActivity {
    @BindView(R.id.submit_photo)
    TextView submit_photo;
    @BindView(R.id.iv_user)
    ImageView icon;
    @BindView(R.id.bt_change)
    Button change;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et2)
    EditText et_name;
    @BindView(R.id.et3)
    EditText et_mail;
    @BindView(R.id.et4)
    EditText et_add;
    User user;
    ImagePicker imagePicker = new ImagePicker();
    String imgUrl = "";
     PromptDialog dialog ;
     int userType = -1;
     int subType =-1;
    @BindView(R.id.check_group)
    RadioGroup group;
    @BindView(R.id.ck1)
    RadioButton ck1;
    @BindView(R.id.ck2)
    RadioButton ck2;
    @BindView(R.id.ck3)
    RadioButton ck3;
    public AccountInfo() {
        super(R.layout.layout_account_info);
    }

    protected void initView() {
        super.initView();
        user = new User();
        dialog = new PromptDialog(AccountInfo.this);
     changeTitle( "账号信息");
     hideObjs(new int[]{R.id.tv_right});

    }

    @Override
    protected void initData() {
        HttpCore.getUserInfo(new IResultHandler<User>() {
            @Override
            public void onSuccess(Result<User> rs) {
                if (rs.getSuccess()) {
                   User info = rs.getBiz();
                   et_phone.setText(info.getPhone());
                   et_name.setText(info.getName());
                   et_mail.setText(info.getMail());
                   et_add.setText(info.getAddress());
                    Glide.with(EAPPApplication.getInstance()).load(Url.IMAGEPATH+info.getImg())
                            .error(R.mipmap.img_head)
                            .placeholder(R.mipmap.img_head)
                            .fitCenter()
                            .into(icon);
                   userType = info.getType();
                   if(userType!=0){
                       group.clearCheck();
                       if(userType==1){
                           ck1.setBackgroundResource(R.drawable.shape_tag_blue);
                           ck1.setTextColor(Color.parseColor("#ffffff"));
                       }else if(userType==2){
                           ck2.setBackgroundResource(R.drawable.shape_tag_blue);
                           ck2.setTextColor(Color.parseColor("#ffffff"));
                       } else if (userType==3) {
                           ck3.setBackgroundResource(R.drawable.shape_tag_blue);
                           ck3.setTextColor(Color.parseColor("#ffffff"));
                       }

                   }else{
                       group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                           @Override
                           public void onCheckedChanged(RadioGroup radioGroup, int i) {
                               int id = radioGroup.getCheckedRadioButtonId();
                               if (id == R.id.ck1) {
                                   ck1.setBackgroundResource(R.drawable.shape_tag_blue);
                                   ck1.setTextColor(Color.parseColor("#ffffff"));
                                   subType = 1;
                               } else {
                                   ck1.setBackgroundColor(Color.parseColor("#ffffff"));
                                   ck1.setTextColor(Color.parseColor("#808080"));

                               }
                               if (id == R.id.ck2) {
                                   ck2.setBackgroundResource(R.drawable.shape_tag_blue);
                                   ck2.setTextColor(Color.parseColor("#ffffff"));
                                   subType =2;
                               } else {
                                   ck2.setBackgroundColor(Color.parseColor("#ffffff"));
                                   ck2.setTextColor(Color.parseColor("#808080"));
                               }
                               if (id == R.id.ck3) {
                                   ck3.setBackgroundResource(R.drawable.shape_tag_blue);
                                   ck3.setTextColor(Color.parseColor("#ffffff"));
                                   subType =3;
                               } else {
                                   ck3.setBackgroundColor(Color.parseColor("#ffffff"));
                                   ck3.setTextColor(Color.parseColor("#808080"));
                               }
                           }

                       });
                   }
                   Glide.with(EAPPApplication.getInstance())
                           .load(Url.IMAGEPATH+info.getImg())
                           .error(R.mipmap.img_head);
                }
            }
        });
    }

    /*调用相册*/
    private void initPhotoPicker() {
        // 设置标题
        imagePicker.setTitle("设置头像");
// 设置是否裁剪图片
        imagePicker.setCropImage(true);
// 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override
            public void onCropImage(final Uri imageUri) {
                icon.setImageURI(imageUri);
                Glide.with(AccountInfo.this).load(imageUri)
//                        .transform(new GlideCircleTransform(AccountInfo.this))
                        .into(icon);
                // 图片上传代码
//                MyToast.makeText(AccountInfo.this, "这里写上传代码！！", Toast.LENGTH_SHORT).show();
                HttpCore.UploadImage(UriToPathUtil.getImageAbsolutePath(AccountInfo.this, imageUri), new IResultHandler<UploadRe>() {
                    @Override
                    public void onSuccess(Result<UploadRe> rs) {
                        if (rs.getSuccess()) {
                            imgUrl = rs.getBiz().getPath();
                            user.setImg(imgUrl + "");
                            L.e("imgurl re" + imgUrl);
                        }
                    }
                });
            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.OVAL)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(90, 90)
                        // 宽高比
                        .setAspectRatio(1, 1);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
                MyToast.makeText(AccountInfo.this, "用户拒绝了授权！", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // 监听
    @OnClick(R.id.submit_photo)
    public void setSubmit_photo() {
//        MyToast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
        initPhotoPicker();
    }

    @OnClick(R.id.bt_change)
    public void changeInfo() {
 
//        MyToast.makeText(this, "修改资料。。上传图片 ", Toast.LENGTH_SHORT).show();
        if (checkNull()) {
            dialog.showSuccess("修改中...");
            user.setName(et_name.getText().toString());
            user.setPhone(et_phone.getText().toString());
            //邮箱？地址？
            user.setMail(et_mail.getText().toString());
            user.setAddress(et_add.getText().toString());
            //没设置过类型 且有值
            if(userType==0&&subType!=-1){
                user.setType(subType);
            }
            HttpCore.userm(user, new IResultHandler<UserInfo>() {
                @Override
                public void onSuccess(Result<UserInfo> rs) {
                    if (rs.getSuccess()) {
                        //修改姓名头像 刷新；
                        HttpCore.name =user.getName();
                        HttpCore.img = user.getImg();

               dialog.showSuccess("修改成功!");
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       AccountInfo.this.finish();
                   }
               },500);
                     }else{
                        dialog.showError(rs.getMsg());
                    }
                }
            });
        }


    }

    public boolean checkNull() {
        if (et_phone.getText().toString().equals("")) {
     dialog.showWarn("手机号码不能为空！");
            return false;
        } else if (et_name.getText().toString().equals("")) {
     dialog.showWarn("用户名不能为空！");
            return false;
        } else if (et_mail.getText().toString().equals("")) {
     dialog.showWarn("邮箱不能为空！");
            return false;
        } else if (et_add.getText().toString().equals("")) {
     dialog.showWarn("地址不能为空！");
            return false;
        }
        return true;
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
