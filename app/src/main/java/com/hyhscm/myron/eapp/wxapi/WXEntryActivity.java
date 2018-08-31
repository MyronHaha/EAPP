package com.hyhscm.myron.eapp.wxapi;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.RequestQueue;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.platform.comapi.map.E;
import com.google.android.gms.common.api.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.activity.Home.HomeNeed;
import com.hyhscm.myron.eapp.activity.Home.HomeProduct;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.data.CommitResult;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.ShareCallBack;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.mph.okdroid.response.GsonResHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2018/2/26.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static Context mcontext;
    private static Activity mActivity;
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private IWXAPI api;
    private JsonObject jsonObject = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = this;
        mActivity = this;
        api = EAPPApplication.getInstance().getWxApi();
        api.handleIntent(this.getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * 微信发送的请求将回调此方法
     *
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("req", baseReq.toString());
        try {
            Intent intent = new Intent(EAPPApplication.getInstance(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            EAPPApplication.getInstance().startActivity(intent);
        } catch (Exception e) {

        }
        finish();
    }

    /**
     * 发送到微信请求的相应结果
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            //发送成功
            case BaseResp.ErrCode.ERR_OK:

                if (resp.getType() == RETURN_MSG_TYPE_SHARE) {
                    MyToast.makeText(mcontext,"分享成功！",2000).show();
                    HttpCore.isShared = true;
                     shareSuccess();

                } else {
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    if (sendResp != null) {
                        String code = sendResp.code;
//                    getAccess_token(code);
//                        HttpCore.setToken("");
                        LoginWithWxCode(code);
                    }
                }

                break;
            //发送取消
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (resp.getType() == RETURN_MSG_TYPE_SHARE) {
                    MyToast.makeText(mcontext, "用户取消分享！", 2000).show();
                } else {
                    MyToast.makeText(mcontext, "用户取消登录！", 2000).show();
                }

                break;
            //发送拒绝
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (resp.getType() == RETURN_MSG_TYPE_SHARE) {
                    Log.e("ERR_AUTH_DENIED", "true");
                } else {
                    MyToast.makeText(mcontext, "用户取消授权", 2000).show();
                }

                break;
            default:
                L.e("default");

                break;
        }
        finish();
    }


    public static void LoginWithWxCode(final String code) {
        HttpCore.loginWx(code, "", new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
                Log.e("msg", rs.getMsg());
                if (rs.getSuccess()) {
                    MyToast.makeText(mActivity, "登录成功！", 1000).show();
//                    new PromptDialog(mActivity).showSuccess("登录成功！");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mcontext, MainActivity.class);
                            common.launchActivityWithIntent(mcontext, intent);
                            mActivity.finish();
                        }
                    }, 500);

                    return;
                } else {
                    // 失败 1. biz null 2. biz 为1 或 2
                    if (rs.getBiz() != null) {
                        if (!rs.getSuccess() && rs.getBiz().equals("1")) {
                            MyToast.makeText(mActivity, "微信号未绑定！", 1000).show();
//                            new PromptDialog(mActivity).showWarn("微信号未绑定！");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mcontext, BindPhone.class);
                                    intent.putExtra("code", code);
                                    Log.e("未绑定:", code);
                                    common.launchActivityWithIntent(mcontext, intent);
                                    mActivity.finish();
                                }
                            }, 500);

                        } else if (!rs.getSuccess() && rs.getBiz().equals("2")) {
                            Log.e("已注册:", code);
                            MyToast.makeText(mActivity, "手机号已注册，请获取验证码登录！", 1000).show();

//                            new PromptDialog(mActivity).showWarn("手机号已注册，请获取验证码登录！");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mcontext, LoginActivity.class);
                                    common.launchActivityWithIntent(mcontext, intent);
                                }
                            }, 500);
                            mActivity.finish();
                        }
                    } else {
                        //msg = "微信登录失败"  biz ==null
                        //errorCode = "-1"
                        MyToast.makeText(mActivity, "微信登录失败", 1000).show();
//                        new PromptDialog(mActivity).showError("微信登录失败！");
                    }

                }

            }
        });
    }
// 分享回调
    /*shareType  0 1 2   0时需要再次判断 商机或代理  1 产品招商 2 资讯
    * */

    public void shareSuccess() {
        String id ="-1";
        String st = "-1";
        switch (HttpCore.shareType){
            case 0:
                Demand item = (Demand) HttpCore.ShareItem;
                id = item.getId()+"";
                if (item.getStatus()==0&&item.getType()==1) {
                   st = "1";
                }else if(item.getStatus()==0&&item.getType()!=1){
                    st = "2";
                }
                break;
            case 1:
                Product product = (Product) HttpCore.ShareItem;
                id = product.getId()+"";
                st = "3";
                break;
            case 2:
                News news = (News) HttpCore.ShareItem;
                id = news.getId()+"";
                st="4";
                break;
        }
        L.e("sharedCallback--"+"type:"+HttpCore.shareType+"\n"+"st:"+st+"\n"+"su:"+ HttpCore.MD5SU +"\n"+"id:"+id+"\n"+"isTopShared:"+HttpCore.isTopShared);
       HttpCore.sharedCallBack(HttpCore.MD5SU, id, st, HttpCore.isTopShared ? "1" : "", new IResultHandler<ShareCallBack>() {
           @Override
           public void onSuccess(Result<ShareCallBack> rs) {
                 if(rs.getSuccess()){
                     L.e("分享记录成功");
                 }else{
                     L.e("分享记录失败");
                 }
           }
       });
        //置顶
       if(HttpCore.isTopShared){
           HttpCore.isMakedTop = false;
           HttpCore.makeTop(id+ "", HttpCore.shareType+1+"",HttpCore.isTopShared?"1":"", new IResultHandler<CommitResult>() {
               @Override
               public void onSuccess(Result<CommitResult> rs) {
                   if (rs.getSuccess()) {
                       HttpCore.isMakedTop = true;
                       L.e("分享成功， 置顶成功");
                   } else {
                       HttpCore.isMakedTop = false;
                       L.e("分享成功， 置顶失败");
                   }
                   //回调刷新界面
                   SendMakeTopCallbackMessage();
               }
           });
       }

    }

/*0 需求 1 产品
* */
    public void SendMakeTopCallbackMessage(){
        if(HttpCore.shareType==0){
            if(HomeNeed.mInstance!=null){
                Message msg =   HomeNeed.mInstance.reFreshDatahandler.obtainMessage();
                msg.obj = HttpCore.isMakedTop;
                HomeNeed.mInstance.reFreshDatahandler.sendMessage(msg);
            }else{
                L.e("分享成功， 置顶成功,handler发送消息失败");
            }
        }else
        if(HttpCore.shareType==1){
            if(HomeNeed.mInstance!=null){
                Message msg =   HomeProduct.mInstance.reFreshDatahandler.obtainMessage();
                msg.obj = HttpCore.isMakedTop;
                HomeProduct.mInstance.reFreshDatahandler.sendMessage(msg);
            }else{
                L.e("分享成功， 置顶成功,handler发送消息失败");
            }
        }
    }
}