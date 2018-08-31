package com.hyhscm.myron.eapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UserInfo;
import com.hyhscm.myron.eapp.net.HttpCore;

import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.updateApp.UpdateApp;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by jack chen on 2017-12-19.
 */

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//
//        try {
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
//                if (Settings.System.canWrite(this)) {
////                    initData();
//                } else {
//                    MyToast.makeText(this, "当前安卓系统为6.0，必须手动授予应用读写设置权限后手动重启应用即可", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                    intent.setData(Uri.parse("package:" + this.getPackageName()));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    this.startActivityForResult(intent,100);
//
//                }
//            } else {
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchActivity(MainActivity.class);

                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
            }
        }, 300);
    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cache.put("Labelinfo", "");
                cache.put("Areas", "");
                areas();
                labels();
                checkLogin();
            }
        }).start();

//        launchActivity(MainActivity.class);
//        this.finish();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//              launchActivity(MainActivity.class);
//                finish();
//                overridePendingTransition(R.anim.enter_anim,R.anim.exit_anim);
//            }
//        },300);
    }

    public void checkLogin() {
        if (NetUtil.getNetWorkState(this) == NetUtil.NETWORK_NONE) {
            commonHandler.obtainMessage(0x10000,"请检查网络连接！").sendToTarget();//  调用的也是Message 内部obtain方法；
            return;
        }
        final String phone = common.SP_Read(this, "phone");
        final String p = common.SP_Read(this, "p");
        L.e("phone" + phone + "---" + "p" + p);
        if (!phone.equals("") && !p.equals("")) {
            HttpCore.Login(phone, "", p, new IResultHandler<UserInfo>() {
                @Override
                public void onSuccess(Result<UserInfo> rs) {
                }
            });

        }

    }


}
