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

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 * 深度认证
 * 图片？ img 1234
 */

public class VfyCompany extends BaseActivity {
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
    @BindView(R.id.et_reason)
    EditText et_reason;
    protected PromptDialog dialog;
    protected AccredInfo object;

    public VfyCompany() {
        super(R.layout.layout_authorize);
    }

    protected void initView() {
        super.initView();
        common.changeTitle(this, "深度认证");
        common.hideObjs(this, new int[]{R.id.tv_right});
        dialog = new PromptDialog(VfyCompany.this);
        object = new AccredInfo();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
    }

    /*调用相册*/
    private void callPhotoPicker(final ImageView icon, final String title, final Boolean cropImage, final Boolean roundImage) {
        // 设置标题
        imagePicker.setTitle(title);
// 设置是否裁剪图片
        imagePicker.setCropImage(cropImage);
// 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
//                MyToast.makeText(VfyCompany.this, "uri:"+imageUri, Toast.LENGTH_SHORT).show();
                if (!cropImage) {
                    icon.setImageURI(imageUri);
                }
                HttpCore.UploadImage(UriToPathUtil.getImageAbsolutePath(VfyCompany.this, imageUri), new IResultHandler<UploadRe>() {
                    @Override
                    public void onSuccess(Result<UploadRe> rs) {
                        if (rs.getSuccess()) {
                          if(title.equals("设置营业执照")){
                              object.setImg(rs.getBiz().getPath());
                          }else if(title.equals("设置经营许可证")){
                              object.setImg2(rs.getBiz().getPath());
                          } else if (title.equals("设置联系人身份证")) {
                              object.setImg3(rs.getBiz().getPath());
                          }else if(title.equals("设置法人身份证")){
                              object.setImg1(rs.getBiz().getPath());
                          }
                        }
                        L.e("UploadImage" + rs.getMsg()+"rs path"+rs.getBiz().getPath());
                    }
                });
            }


            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
                MyToast.makeText(VfyCompany.this, "用户拒绝了授权！", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // 监听
    //营业执照
    @OnClick(R.id.iv_business_id)
    public void chooseBusinId() {
//        MyToast.makeText(this, "营业", Toast.LENGTH_SHORT).show();
        callPhotoPicker(iv_business_id, "设置营业执照", false, false);
    }

    //经营许可证；
    @OnClick(R.id.iv_business)
    public void chooseBussin() {
//        MyToast.makeText(this, " 经营", Toast.LENGTH_SHORT).show();
        callPhotoPicker(iv_business, "设置经营许可证", false, false);
    }

    //联系人id；
    @OnClick(R.id.iv_contract_iden)
    public void chooseContractId() {
//        MyToast.makeText(this, " 联系人id", Toast.LENGTH_SHORT).show();
        callPhotoPicker(iv_contract_id, "设置联系人身份证", false, false);
    }

    //法人id；
    @OnClick(R.id.iv_identify)
    public void chooseRepresentId() {
//        MyToast.makeText(this, " 法人身份证", Toast.LENGTH_SHORT).show();
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
                object.setImg1("");
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
                object.setImg2("");
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
                object.setImg3("");
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
                object.setImg4("");
            }
        });
    }

    //提交
    @OnClick(R.id.bt_change)
    public void submit() {
        if (checkNull()) {
            dialog.showInfo("认证中...");
            object.setCtype(1);
            object.setAccountno(et_ac_company.getText().toString());
            object.setAddress(et_dizhi.getText().toString());
            object.setName(et_qiye.getText().toString());
            object.setLinkman(et_contract.getText().toString());
            object.setLinkmanTel(et_phone.getText().toString());
            object.setLegalperson(et_faren.getText().toString());
            object.setContent(et_reason.getText().toString());  //认证理由；
//            object.setImg("uploadFile/image/1928ce49-f4be-415c-b8e5-26dc8092c08e.png");
            HttpCore.accredApply(object, new IResultHandler<AccredInfo>() {
                @Override
                public void onSuccess(Result<AccredInfo> rs) {
                    if (rs.getSuccess()) {
                        dialog.showSuccess("认证成功");
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           VfyCompany.this.finish();
                       }
                   },800);

                    } else {
                        dialog.showError(rs.getMsg());
                    }
                }
            });
        }

//        MyToast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
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

    public boolean checkNull() {
        if (et_qiye.getText().toString().equals("")) {
            dialog.showWarn("企业名称不能为空！");
            return false;
        } else if (et_faren.getText().toString().equals("")) {
            dialog.showWarn("企业法人不能为空！");
            return false;
        } else if (et_dizhi.getText().toString().equals("")) {
            dialog.showWarn("公司地址不能为空！");
            return false;
        } else if (et_contract.getText().toString().equals("")) {
            dialog.showWarn("联系人不能为空！");
            return false;
        } else if (et_phone.getText().toString().equals("")) {
            dialog.showWarn("联系人电话不能为空！");
            return false;
        } else if (et_ac_company.getText().toString().equals("")) {
            dialog.showWarn("公司账户不能为空！");
            return false;
        }else if(et_reason.getText().toString().equals("")){
            dialog.showWarn("认证理由不能为空！");
            return false;
        }
        return true;
    }
}
