package com.hyhscm.myron.eapp.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;

/**
 * Created by Jason on 2017/12/19.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
//              if(netWorkState== NetUtil.NETWORK_NONE){
//                  L.e("hahha","dsfsf");
//                  MyToast.makeText(context,"fsdf",2000).show();
//              }
        }
    }


}
