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
import android.widget.Toast;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.linchaolong.android.imagepicker.ImagePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 * 修改密码
 */

public class FeedBack extends BaseActivity {
   @BindView(R.id.et_jy)
   EditText et_jy;
   @BindView(R.id.bt_change)
   Button bt_submit;


  public FeedBack(){
      super(R.layout.layout_feedback);
  }

    protected void initView() {
        super.initView();
        changeTitle("意见反馈");
     hideObjs(new int[]{R.id.tv_right});
    }

   @OnClick(R.id.bt_change)
    public void submit(){
        if(et_jy.getText().toString().equals("")){
            MyToast.makeText(this,"请输入内容！",Toast.LENGTH_SHORT).show();
            return;
        }
       HttpCore.feedBack(et_jy.getText().toString(), new IResultHandler() {
           @Override
           public void onSuccess(Result rs) {
               if(rs.getSuccess()){

                   new PromptDialog(FeedBack.this).showSuccess("意见反馈成功！");
               }
           }
       });
   }


}
