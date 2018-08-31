package com.hyhscm.myron.eapp.service;

/**
 * Created by Jason on 2018/4/12.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.activity.Im.DemoHelper;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.view.GlideLoader;
import com.lzy.ninegrid.NineGridView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * 启动初始化Service
 */
public class InitializeService extends IntentService {
    private static boolean isInit = false;
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    public InitializeService() {
        super("InitializeService");
    }

    /**
     * 启动调用
     * @param context
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
               if(!isInit) {
                   performInit();
               }
        }
    }

    /**
     * 启动初始化操作
     */
    private void performInit() {
        if (!isInit) {
//
//            EMOptions options = new EMOptions();
//// 默认添加好友时，是不需要验证的，改成需要验证
//            options.setAcceptInvitationAlways(false);
//// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//            options.setAutoTransferMessageAttachments(true);
//// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//            options.setAutoDownloadThumbnail(true);
////初始化
//            EMClient.getInstance().init(this, options);
////在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//            EMClient.getInstance().setDebugMode(true);
//            EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(true);
//            EaseUI.getInstance().init(this, initOptions());
//            EMClient.getInstance().setDebugMode(true);
            NineGridView.setImageLoader(new GlideLoader());
            DemoHelper.getInstance().init(EAPPApplication.getInstance());
            EAPPApplication.getInstance().initX5();
            UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, ""); //umeng
            UMConfigure.setEncryptEnabled(true);
            MobclickAgent.setDebugMode(true);
            //禁止默认的页面统计功能，这样将不会再自动统计Activity页面。（包含Activity、Fragment或View的应用）
            MobclickAgent.openActivityDurationTrack(false);
            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
            if (EAPPApplication.getInstance().getWxApi() == null) {
                EAPPApplication.getInstance().regToWx();
            }
            isInit = true;
        }
    }
}