package com.hyhscm.myron.eapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.B;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.common.design.MaterialDialog;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.activity.Im.runtimepermissions.PermissionsManager;
import com.hyhscm.myron.eapp.activity.Im.runtimepermissions.PermissionsResultAction;
import com.hyhscm.myron.eapp.activity.User.ChangePassword;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.activity.User.MyProduct;
import com.hyhscm.myron.eapp.activity.User.NeedPublish;
import com.hyhscm.myron.eapp.activity.User.ProductPublish;
import com.hyhscm.myron.eapp.activity.User.UserSetting;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Labelinfo;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UserInfo;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.ACache;
import com.hyhscm.myron.eapp.utils.AndroidWorkaround;
import com.hyhscm.myron.eapp.utils.DensityUtil;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MD5Util;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.mph.okdroid.response.GsonResHandler;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;


import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by jack chen on 2017-12-19.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    int layoutResID = 0;
    protected EAPPApplication appApplication;
    protected Bundle bundle;
    protected LayoutInflater inflater;
    public static  BaseActivity _mActivity;
    protected HashMap<String, Object> params = new HashMap();
    ACache cache = null;
    public static PromptDialog dialog;
    private static final int REQUEST_CODE_ASK_WRITE_SETTINGS = 0x100 ;
    private static  int statusBarHeight = 0;
   static Handler commonHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
             if(msg.what == 0x10000){
                 MyToast.makeText(_mActivity,(String)msg.obj,15000).show();
             }
        }
    };
    public BaseActivity() {
    }



    public BaseActivity(int layoutResID) {
        this.layoutResID = layoutResID;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        cache = ACache.get(this, "Labelinfo");
        _mActivity = this;
        bundle = getIntent().getExtras();
        appApplication = (EAPPApplication) getApplication();
        setTranslucentStatus();
        if (layoutResID != 0) {
            setContentView(this.layoutResID);
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
            inflater = LayoutInflater.from(this);
            ButterKnife.bind(this);
        }
        appApplication.addActivity(this);
        common.goBack(this);
        initView();
        goBack();
        params.clear();
//        initData();
    }
    public int getStatusBarHeight(Context context) {
        if (statusBarHeight != 0)
            return statusBarHeight;

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
    protected void setTranslucentStatus() {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
         changeStatusStyle();
    }
    public void changeStatusStyle(){
            setWindowStatusBarColor(R.color.colorPrimary);
    }
    //0428 gai
    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(this)) {
                    initData();
                } else {
                    MyToast.makeText(this, "当前安卓系统为6.0，必须手动授予应用读写设置权限后手动重启应用即可",5000).show();
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + this.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivityForResult(intent,REQUEST_CODE_ASK_WRITE_SETTINGS);
                    SystemClock.sleep(12000);
                }
            } else {
                initData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * view 初始化
     */
    protected void initView() {

        common.createDialog(this);
        dialog = common.promptDialog;
//        dialog = common.createDialog(this);
    }

    /**
     * Data 初始化
     */
    protected void initData() {

    }

    public void setWindowStatusBarColor(int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(this.getResources().getColor(colorResId));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack() {
        View v = findViewById(R.id.ll_back);
        if (v != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _mActivity.finish();
                }
            });
        }
    }

    protected void changeTitle(String title) {
        TextView tv = (TextView) findViewById(R.id.tv_title);
        tv.setText(title);
    }

    protected void hideObjs(int[] resourceId) {
        for (int i = 0; i < resourceId.length; i++) {
            View ob = this.findViewById(resourceId[i]);
            ob.setVisibility(View.GONE);
        }
    }

    protected View getToolView(int id) {
        return common.getToolView(this, id);
    }

    public void launchActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    protected void launchActivityWithIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    protected void launchActivityWithIntent(Intent intent) {
        startActivity(intent);
    }

    protected void launchActivity(Class c, int requestCode) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ASK_WRITE_SETTINGS) {
            if (Settings.System.canWrite(this)) {
//                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
//                PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, restartIntent);
////                android.os.Process.killProcess(android.os.Process.myPid());
//                EAPPApplication.getInstance().exit();
                 Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                L.e( "onActivityResult write settings granted" );
            }else{
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    /*常用分类 2 渠道 3 科室 4 管理类别 5 业务类型 6 医保
          7 产地 8 优势 9 功能类别10  11 类别*/
    public List<Labelinfo> labels(int type) {
        List<Labelinfo> ls = labels();
        List<Labelinfo> ls1 = new ArrayList<>();
        for (Labelinfo l : ls) {
            if (l.getType() == type)
                ls1.add(l);
        }
        return ls1;
    }

    protected List<Labelinfo> labels() {

        final ArrayList<Labelinfo> lbs = new ArrayList<>();
        Object obj = cache.getAsObject("Labelinfo");
        if (obj != null) {
            lbs.addAll((ArrayList<Labelinfo>) obj);
        }
        if (lbs.size() == 0) {
            if (NetUtil.getNetWorkState(this) == NetUtil.NETWORK_NONE) {

            } else {
                HttpCore.get(Url.HOSTNAME + Url.TAGCATOGORY, new HashMap(), new GsonResHandler<ListResult<Labelinfo>>() {
                    @Override
                    public void onFailed(int i, String s) {
                        common.promptDialog.showError(NetUtil.getErrorString(s));
                    }

                    @Override
                    public void onSuccess(int i, ListResult<Labelinfo> result) {
                        cache.put("Labelinfo", (ArrayList<Labelinfo>) result.getBiz());
                        lbs.addAll(result.getBiz());

                    }
                });
            }
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        return lbs;
    }

    protected List<Areas> province() {
        return areas(-1, 1);
    }

    protected List<Areas> city(int pid) {
        return areas(pid, -1);
    }

    protected List<Areas> hosp(int pid) {
        return areas(pid, -1);
    }

    //分类取
    List<Areas> areas(int pid, int l) {
        List<Areas> ls = areas();
        List<Areas> ls1 = new ArrayList<>();
        for (Areas a : ls) {
            if (a.getLevels() == l)
                ls1.add(a);
            if (a.getPid() == pid)
                ls1.add(a);
        }
        return ls1;
    }

    //地区所有数据
    protected List<Areas> areas() {

        final ArrayList<Areas> lbs = new ArrayList<>();
        Object obj = cache.getAsObject("Areas");
        if (obj != null) {
            lbs.addAll((ArrayList<Areas>) obj);
        }
        if (lbs.size() == 0) {
            if (NetUtil.getNetWorkState(this) == NetUtil.NETWORK_NONE) {
//                MyToast.makeText(this, "请检查网络连接", 1000).show();
            } else {
                HttpCore.get(Url.TAGHOSPITAL, new HashMap(), new GsonResHandler<ListResult<Areas>>() {
                    @Override
                    public void onFailed(int i, String s) {
                    }

                    @Override
                    public void onSuccess(int i, ListResult<Areas> result) {
                        lbs.addAll(result.getBiz());
                        cache.put("Areas", (ArrayList<Areas>) result.getBiz());
//                    ArrayList<Areas> obj1=(ArrayList<Areas>)cache.getAsObject("Areas");
                    }
                });
            }
        }

        return lbs;
    }

    public String appendList(List list, String devider) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                result += list.get(i) + devider;
            } else {
                result += list.get(i);
            }

        }
        return result;
    }

    public List splitString(String str, String regex) {
        String[] arr = str.split(regex);
        List list = new ArrayList();
        for (String s : arr) {
            if (!s.equals("")) {
                list.add(s);
            }
        }
        return list;
    }

    protected String appendListArea(List<Areas> list, String devider) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                result += list.get(i).getId() + devider;
            } else {
                result += list.get(i).getId();
            }

        }
        return result;
    }

    //
    protected String appendListLable(List<Labelinfo> list, String devider) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                result += list.get(i).getId() + devider;
            } else {
                result += list.get(i).getId();
            }

        }
        return result;
    }
/*data labels(*)
* resultList 需要比对的list id string
* changeList 序号list
* subList 匹配结果list string*/
    public void refreshLabelInfoSeleted(List<Labelinfo> data, List resultList, List changeList, List<String> subList) {
        for (int i = 0; i < resultList.size(); i++) {

            for (int j = 0; j < data.size(); j++) {
                if (String.valueOf(data.get(j).getId()).equals(resultList.get(i))) {
                    changeList.add(j);
                    subList.add(data.get(j).getId() + "");
                }
            }
        }
    }

    //data 本地全部area ，result 需要匹配的list<String>，sublist 赋值的list<Areas>
    /*
    * city api返回带省份前缀，用id遍历*/
    public void refreshAreaSeletedById(int level, List<Areas> data, List<String> resultList, List<Areas> subList) {
        for (int i = 0; i < resultList.size(); i++) {

            for (int j = 0; j < data.size(); j++) {
                String a = String.valueOf(data.get(j).getId());
                String b = resultList.get(i).trim();
                if (data.get(j).getLevels() == level) {
                    if (a.equals(b) && !subList.contains(data.get(j))) {
                        subList.add(data.get(j));
                    }
                }
            }
        }
    }

    public String changeLine(List list) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                result += list.get(i) + "\n";
            } else {
                result += list.get(i);
            }
        }
        L.e(result);
        return result;
    }


    public void AutoLogin(final IResultHandler handler) {
        final String phone = common.SP_Read(this, "phone");
        final String p = common.SP_Read(this, "p");
        if (p.equals("")) {
            dialog.dismiss();
            dialog.showError("请先登录");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    launchActivity(LoginActivity.class);
                }
            }, 500);

        } else {
            if (NetUtil.getNetWorkState(this) == NetUtil.NETWORK_NONE) {
                MyToast.makeText(this, "请检查网络连接", 1000).show();
            } else {
                HttpCore.Login(phone, "", p, new IResultHandler<UserInfo>() {
                    @Override
                    public void onSuccess(Result<UserInfo> rs) {
                        if (rs.getSuccess()) {
                            handler.onSuccess(rs);
                        } else {
                            dialog.dismiss();
                            dialog.showError("登录异常，请重新登录");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    launchActivity(LoginActivity.class);
                                }
                            }, 500);

                        }
                    }
                });
            }
        }

    }


    public void dial(final String phone) {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                // 弹窗需要解释为何需要该权限，再次请求授权
                MyToast.makeText(this, "请先设置应用权限", Toast.LENGTH_LONG).show();

                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        1);
            }
        } else {
            // 已经获得授权，可以打电话
//            PromptButton sure = new PromptButton("确定", new PromptButtonListener() {
//                @Override
//                public void onClick(PromptButton promptButton) {
//                    CallPhone(phone);
//                }
//            });
//            sure.setFocusBacColor(Color.parseColor("#3db0ff"));
//            sure.setTextColor(Color.parseColor("#ff5959"));
//            showUiDialog("确认拨号给" + "\n\r" + phone + "?",
//                    sure);
            showNormalDialog(getResources().getString(R.string.prompt_str, "拨号给 " + phone), new promptListener() {
                @Override
                public void cancel(MaterialDialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void prompt(MaterialDialog dialog) {
                    CallPhone(phone);
                    dialog.dismiss();
                }
            });
        }

    }

    private void CallPhone(String phone) {
        String number = phone;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        startActivity(intent);
    }

    public void showUiDialog(String msg, PromptButton sure) {
        PromptButton button = new PromptButton("取消", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton promptButton) {

            }
        });
        new PromptDialog(this).showWarnAlert(msg, sure, button);
    }

    //分享到微信
    /*
    sharetype ： 0,1,2  需求 产品  资讯
    id：id
    shareWhere:0 friends 1 zoom
    isTopShared 分享去置顶
    * */
    public void shareToWx(int shareType, String id, String content, int shareWhere, boolean isTopShared) {
        HttpCore.isTopShared = isTopShared;
        IWXAPI api = EAPPApplication.getInstance().getWxApi();
        String su = "yz" + id + System.currentTimeMillis();
        String suMD5 = MD5Util.getMD5(su);
        HttpCore.MD5SU = suMD5;
        L.e("beforeMD5:" + su);
        L.e("afterMD5:" + suMD5);
        L.e("itemId:" + id);
        String t = "";
        if (api != null && api.isWXAppInstalled()) {
            WXWebpageObject webpage = new WXWebpageObject();
            switch (shareType) {
                case 0:
                    t = "e链网-最新需求";
                    webpage.webpageUrl = Url.WX_DEMAND + suMD5 + "&id=" + id;
                    break;
                case 1:
                    t = "e链网-招商产品";
                    webpage.webpageUrl = Url.WX_PRODUCTMERCHANT + suMD5 + "&id=" + id;
                    break;
                case 2:
                    t = "e链网-资讯";
                    webpage.webpageUrl = Url.WX_NEWS + suMD5 + "&id=" + id;
                    break;
                default:
                    t = "e链网";
                    webpage.webpageUrl = Url.WEBPATH;
                    break;
            }
            /*wx*/
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = t;
            msg.description = content;
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.wx_logo);
//             Bitmap thumb = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            msg.setThumbImage(thumb);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = shareWhere == 1 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
        } else {
            MyToast.makeText(this, "您尚未安装微信", Toast.LENGTH_SHORT).show();
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //    public static byte[] bitmap2Bytes(Bitmap bitmap,int maxkb){
//         ByteArrayOutputStream output = new ByteArrayOutputStream();
//         bitmap.compress(Bitmap.CompressFormat.PNG,100,output);
//         int options = 100;
//         while(output.toByteArray().length>maxkb&&options!=10){
//             output.reset();
//             bitmap.compress(Bitmap.CompressFormat.PNG, options, output);
//             options-=10;
//         }
//         return output.toByteArray();
//    }
/*
*
* position  bottom 0*/
//分享窗口
// 需求
    public void showCustomDialog(View view, final Demand demand, int position, final boolean isTopShared) {
        L.e("param:type_" + HttpCore.shareType + "isTopShared:" + isTopShared);
        HttpCore.ShareItem = demand;
        HttpCore.shareType = 0;

        final MaterialDialog dialog = new MaterialDialog.Builder(this).create();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (position == 0) {
            final Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.dialog_theme);
            final WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }


        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (NeedPublish.mActivty != null) {
                    NeedPublish.mActivty.needHandler.obtainMessage(0).sendToTarget();
                }
            }
        });
        view.findViewById(R.id.fri_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shareToWx(0, demand.getId() + "", demand.getTitle(), 0, isTopShared);
                if (NeedPublish.mActivty != null) {
                    NeedPublish.mActivty.needHandler.obtainMessage(0).sendToTarget();
                }
            }
        });
        view.findViewById(R.id.zoom_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shareToWx(0, demand.getId() + "", demand.getTitle(), 1, isTopShared);
                if (NeedPublish.mActivty != null) {
                    NeedPublish.mActivty.needHandler.obtainMessage(0).sendToTarget();
                }
            }
        });

    }

    //产品
    public void showCustomDialog(View view, final Product product, int position, final boolean isTopShared) {
        L.e("param:type_" + HttpCore.shareType + "isTopShared:" + isTopShared);

        HttpCore.ShareItem = product;
        HttpCore.shareType = 1;

        final MaterialDialog dialog = new MaterialDialog.Builder(this).create();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (position == 0) {
            final Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.dialog_theme);
            final WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }


        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (ProductPublish.mActivty != null) {
                    ProductPublish.mActivty.productHandler.obtainMessage(0).sendToTarget();
                }
            }
        });
        view.findViewById(R.id.fri_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shareToWx(1, product.getId() + "", product.getName(), 0, isTopShared);
                if (ProductPublish.mActivty != null) {
                    ProductPublish.mActivty.productHandler.obtainMessage(0).sendToTarget();
                }
            }
        });
        view.findViewById(R.id.zoom_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shareToWx(1, product.getId() + "", product.getName(), 1, isTopShared);
                if (ProductPublish.mActivty != null) {
                    ProductPublish.mActivty.productHandler.obtainMessage(0).sendToTarget();
                }
            }
        });

    }

    //新闻
    public void showCustomDialog(View view, final News news, int position, final boolean isTopShared) {
        L.e("param:type_" + HttpCore.shareType + "isTopShared:" + isTopShared);

        HttpCore.ShareItem = news;
        HttpCore.shareType = 2;

        final MaterialDialog dialog = new MaterialDialog.Builder(this).create();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (position == 0) {
            final Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.dialog_theme);
            final WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.fri_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shareToWx(2, news.getId() + "", news.getTitle(), 0, isTopShared);
            }
        });
        view.findViewById(R.id.zoom_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shareToWx(2, news.getId() + "", news.getTitle(), 1, isTopShared);
            }
        });

    }

    // 文字list
    public void showStringListDialog(String[] items, MaterialDialog.OnClickListener listener) {
        new MaterialDialog.Builder(this)
                .setCanceledOnTouchOutside(false)
                .setItems(items, listener).create().show();
    }

    public void showCustomAlert(MaterialDialog alert, View view, int[] viewid, View.OnClickListener listener) {

        final MaterialDialog dialog = alert;
        dialog.setContentView(view);
        dialog.show();
        if (viewid != null) {
            for (int id : viewid) {
                view.findViewById(id).setOnClickListener(listener);
            }
        }
        if (view.findViewById(R.id.iv_close) != null) {
            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    // 是否...
    public void showNormalDialog(String alertMsg, final promptListener listener) {
        View view = getLayoutInflater().inflate(R.layout.dialog_normal, null);
        final MaterialDialog dialog = new MaterialDialog.Builder(this).create();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
//        ((TextView)view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_alert)).setText(alertMsg);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cancel(dialog);
            }
        });
        view.findViewById(R.id.prompt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.prompt(dialog);
            }
        });
        dialog.show();
    }

    public interface promptListener {
        public void cancel(MaterialDialog dialog);

        public void prompt(MaterialDialog dialog);
    }

    //签到
    public void showSignResult(Context context,View view, int viewid, String info) {

        final MaterialDialog dialog = new MaterialDialog.Builder(context).create();
        dialog.setContentView(view);
        dialog.show();
        ((TextView) view.findViewById(viewid)).setText(info);
        if (view.findViewById(R.id.iv_close) != null) {
            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    //登出
    public void logout() {
        showNormalDialog("确认" + "\n\r" + "退出登录？", new promptListener() {
            @Override
            public void cancel(MaterialDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void prompt(MaterialDialog dialog) {
                try {
                    if (common.SP_Clear(_mActivity.getApplicationContext())) {
//                    EAPPApplication.getInstance().clearActivities();
                        HttpCore.isLogin = false;
                        HttpCore.setToken("");
                        EMClient.getInstance().logout(true, new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onProgress(int progress, String status) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onError(int code, String message) {
                                // TODO Auto-generated method stub

                            }
                        });//退出环信;
//                    SystemClock.sleep(1000);
                        launchActivity(LoginActivity.class);
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    // 匹配sourcetype
    public String matchSourceType(int sourceType) {
        String str = "";
        switch (sourceType) {
            case 1:
                str = "活动";
                break;
            case 2:
                str = "新闻";
                break;
            case 3:
                str = "产品";
                break;
            case 4:
                str = "商机";
                break;
            case 5:
                str = "代理";
                break;
            case 6:
                str = "其他";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }
// 只用于产品编辑 优势标签 匹配已选标签，自定义标签；
    public List[] getSelectedAdvantages(List<Labelinfo> data,List<String> find){
        List[] listArray = null;
        int dataSize = data.size();
        List<String>  clicklist = new ArrayList<>();
        List<String>   customList = new ArrayList<>();
        boolean isfound = false;
        for(String a:find){
            isfound = false;
            for(int i = 0;i<data.size();i++){
                if(a.equals(data.get(i).getText())){
                    isfound = true;
                    clicklist.add(data.get(i).getText());
                    data.get(i).setSelected(true);
                }
            }
            if(!isfound){
                customList.add(a);
            }
        }
        listArray = new List[]{clicklist,customList};
        return listArray;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("cautious------baseActivity destroy!");
    }
}
