package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.ImageVideoWrapperEncoder;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/11/30.
 * 修改密码
 */

public class Authorize extends AppCompatActivity {
    @BindView(R.id.et_qiye)
    EditText et_qiye;
    @BindView(R.id.et_faren)
    EditText et_faren;
    @BindView(R.id.et_dizhi)
    EditText et_dizhi;
    @BindView(R.id.et_contract)
    EditText et_contract;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_ac_company)
    EditText et_ac_company;
    @BindView(R.id.iv_business_id)
    ImageView iv_business_id;  //y营业执照
    @BindView(R.id.iv_identify)
    ImageView iv_id; //法人身份证
    @BindView(R.id.iv_business) //经营许可证
            ImageView iv_business;
    @BindView(R.id.iv_contract_iden) //联系人身份证
            ImageView iv_contract_id;
    ImagePicker imagePicker = new ImagePicker();
    @BindView(R.id.iv_shanchu)
    ImageView iv_remove1;
    @BindView(R.id.iv_shanchu1)
    ImageView iv_remove2;
    @BindView(R.id.iv_shanchu3)
    ImageView iv_remove3;
    @BindView(R.id.iv_shanchu4)
    ImageView iv_remove4;
    @BindView(R.id.bt_change)
    Button bt_submit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_authorize);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        common.changeTitle(this, "深度认证");
        common.hideObjs(this, new int[]{R.id.tv_right});
        common.goBack(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
    }

    /*调用相册*/
    private void callPhotoPicker(final ImageView icon, String title, final Boolean cropImage, final Boolean roundImage) {
        // 设置标题
        imagePicker.setTitle(title);
// 设置是否裁剪图片
        imagePicker.setCropImage(cropImage);
// 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
//                MyToast.makeText(Authorize.this, "uri:"+imageUri, Toast.LENGTH_SHORT).show();
                if (!cropImage) {
                    icon.setImageURI(imageUri);
                }
            }

            // 裁剪图片回调
//            @Override
//            public void onCropImage(Uri imageUri) {
////                icon.setImageURI(imageUri);
////
//                if(cropImage){
//                    if(roundImage){
//                        Glide.with(Authorize.this).load(imageUri)
//                                .transform(new GlideCircleTransform(Authorize.this))
//                                .into(icon);
//                    }else{
//                        Glide.with(Authorize.this).load(imageUri)
////                            .transform(new GlideCircleTransform(Authorize.this))
//                                .into(icon);
//
//                    }
//                }
//
//
//                // 图片上传代码
////                MyToast.makeText(Authorize.this, "这里写上传代码！！", Toast.LENGTH_SHORT).show();
//
//            }

//            // 自定义裁剪配置
//            @Override
//            public void cropConfig(CropImage.ActivityBuilder builder) {
//                builder
//                        // 是否启动多点触摸
//                        .setMultiTouchEnabled(false)
//                        // 设置网格显示模式
//                        .setGuidelines(CropImageView.Guidelines.OFF)
//                        // 圆形/矩形
//                        .setCropShape(CropImageView.CropShape.OVAL)
//                        // 调整裁剪后的图片最终大小
//                        .setRequestedSize(560, 900)
//                        // 宽高比
//                        .setAspectRatio(9, 16);
//            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
                MyToast.makeText(Authorize.this, "用户拒绝了授权！", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // 监听
    //营业执照
    @OnClick(R.id.iv_business_id)
    public void chooseBusinId() {
        MyToast.makeText(this, "营业", Toast.LENGTH_SHORT).show();
        callPhotoPicker(iv_business_id, "设置营业执照", false, false);
    }

    //经营许可证；
    @OnClick(R.id.iv_business)
    public void chooseBussin() {
        MyToast.makeText(this, " 经营", Toast.LENGTH_SHORT).show();
        callPhotoPicker(iv_business, "设置经营许可证", false, false);
    }

    //联系人id；
    @OnClick(R.id.iv_contract_iden)
    public void chooseContractId() {
        MyToast.makeText(this, " 联系人id", Toast.LENGTH_SHORT).show();
        callPhotoPicker(iv_contract_id, "设置联系人身份证", false, false);
    }

    //法人id；
    @OnClick(R.id.iv_identify)
    public void chooseRepresentId() {
        MyToast.makeText(this, " 法人身份证", Toast.LENGTH_SHORT).show();
        callPhotoPicker(iv_id, "设置法人身份证", false, false);
    }

    @OnClick(R.id.iv_shanchu)
    public void remove1() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                iv_business_id.setImageURI(null);
                iv_business_id.setImageResource(R.mipmap.img_increase);
                ViewGroup.LayoutParams params = iv_business_id.getLayoutParams();
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                iv_business_id.setLayoutParams(params);
            }
        });
    }

    @OnClick(R.id.iv_shanchu1)
    public void remove2() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                iv_id.setImageResource(R.mipmap.img_increase);
                ViewGroup.LayoutParams params = iv_id.getLayoutParams();
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                iv_id.setLayoutParams(params);
            }
        });
    }

    @OnClick(R.id.iv_shanchu3)
    public void remove3() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                iv_business.setImageResource(R.mipmap.img_increase);
                ViewGroup.LayoutParams params = iv_business.getLayoutParams();
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                iv_business.setLayoutParams(params);
            }
        });
    }

    @OnClick(R.id.iv_shanchu4)
    public void remove4() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                iv_contract_id.setImageResource(R.mipmap.img_increase);
                ViewGroup.LayoutParams params = iv_contract_id.getLayoutParams();
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                iv_contract_id.setLayoutParams(params);
            }
        });
    }

    //提交
    @OnClick(R.id.bt_change)
    public void submit(){
        MyToast.makeText(this,"submit",Toast.LENGTH_SHORT).show();
    }
//    @OnClick(R.id.dislogin)
//    public void dislogin(){
//        MyToast.makeText(this,"dislogin",Toast.LENGTH_SHORT).show();
//
//    }
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
