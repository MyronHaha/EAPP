package com.hyhscm.myron.eapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.activity.Im.DemoHelper;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetBroadcastReceiver;
import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.service.InitializeService;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.view.GlideLoader;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.lzy.ninegrid.NineGridView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2017/11/2.
 */

public class EAPPApplication extends Application {
    private List<Activity> list = new ArrayList<Activity>();
    private static EAPPApplication ea;
    private boolean isInit = false;

    //APP_ID为从官网申请到的appId
    //微信appid
    private String APP_ID = "wx000dc3a14fe565d9";
    //IWXAPI是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public void regToWx() {
        //通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        //将应用的appId注册到微信
        api.registerApp(APP_ID);
    }

    public IWXAPI getWxApi() {
        if (api != null) {
            return api;
        } else {
            api = WXAPIFactory.createWXAPI(this, APP_ID, true);
            return api;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
        ea = this;
        HttpCore.init();
        InitializeService.start(this);//启动服务执行耗时操作
//        if (!isInit) {
////
////            EMOptions options = new EMOptions();
////// 默认添加好友时，是不需要验证的，改成需要验证
////            options.setAcceptInvitationAlways(false);
////// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
////            options.setAutoTransferMessageAttachments(true);
////// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
////            options.setAutoDownloadThumbnail(true);
//////初始化
////            EMClient.getInstance().init(this, options);
//////在做打包混淆时，关闭debug模式，避免消耗不必要的资源
////            EMClient.getInstance().setDebugMode(true);
////            EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(true);
////            EaseUI.getInstance().init(this, initOptions());
////            EMClient.getInstance().setDebugMode(true);
//            DemoHelper.getInstance().init(ea);
//            initX5();
//            isInit = true;
//            if (api == null) {
//                regToWx();
//            }
//        }
    }

    public static EAPPApplication getInstance() {
        return ea;
    }

    public void addActivity(Activity activity) {
        list.add(activity);
    }

    //退出销毁act
    public void exit() {

        for (Activity activity : list) {
            Log.e("activitySize==",activity.getLocalClassName());
            activity.finish();
        }
        list.clear();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    // 只销毁之前的ac
    public void clearActivities() {
        for (Activity activity : list) {
            activity.finish();
        }
    }


//    private EMOptions initOptions() {
//
//        EMOptions options = new EMOptions();
//        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
//        // options.setAppKey("lzan13#hxsdkdemo");
//        // 设置自动登录
//        options.setAutoLogin(true);
//        // 设置是否需要发送已读回执
//        options.setRequireAck(true);
//        // 设置是否需要发送回执，
//        options.setRequireDeliveryAck(true);
//        // 设置是否需要服务器收到消息确认
////        options.setRequireServerAck(true);
//        // 设置是否根据服务器时间排序，默认是true
//        options.setSortMessageByServerTime(false);
//        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
//        options.setAcceptInvitationAlways(false);
//        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
//        options.setAutoAcceptGroupInvitation(false);
//        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
//        options.setDeleteMessagesAsExitGroup(false);
//        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
//        options.allowChatroomOwnerLeave(true);
//        // 设置google GCM推送id，国内可以不用设置
//        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
//        // 设置集成小米推送的appid和appkey
//        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);
//
//        return options;
//    }
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }


//分包

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void initX5() {

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.e("app", " onCoreInitFinished ");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
    }
}
