package com.hyhscm.myron.eapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.leefeng.promptlibrary.OnAdClickListener;
import me.leefeng.promptlibrary.PromptDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jason on 2017/10/19.
 */

public class common {
    static View view;
    static TextView tv;
    static Toolbar tootlbar;
    static ImageView iv;
    static View object;
    public static PromptDialog promptDialog;

    /*状态栏相关*/
    public static void goBack(final Context context) {
        View v = ((Activity) context).findViewById(R.id.ll_back);
        promptDialog = new PromptDialog((Activity) context);
        if (v != null) {
//            iv = (ImageView) ((Activity) context).findViewById(R.id.iv_back);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) context).finish();
//                ((Activity) context).overridePendingTransition(R.anim.out, R.anim.in);
                }
            });
        }
    }

    public static void changeTitle(Context context, String title) {
        tv = (TextView) ((Activity) context).findViewById(R.id.tv_title);
        tv.setText(title);
    }

    public static void showObj(Context context, int resourceId, Boolean visible) {
        tootlbar = (Toolbar) ((Activity) context).findViewById(resourceId);
        if (visible) {
            tootlbar.setVisibility(View.VISIBLE);
        } else {
            tootlbar.setVisibility(View.GONE);
        }
    }

    public static void hideObj(Context context, int resourceId) {
        object = (ImageView) ((Activity) context).findViewById(resourceId);
        object.setVisibility(View.GONE);
    }

    public static void hideObjs(Context context, int[] resourceId) {
        for (int i = 0; i < resourceId.length; i++) {
            View ob = ((Activity) context).findViewById(resourceId[i]);
            ob.setVisibility(View.GONE);
        }
    }

    public static View getToolView(Context context, int id) {
        object = ((Activity) context).findViewById(id);
        return object;
    }

    /*状态栏相关*/
    /*
    * 解决scrollView 嵌套listview 只显示一条；
    */
//    public static void setListViewHeightBasedOnChildren(ListView mlistView, BaseAdapter mainAdapter) {
//        int totalHeight = 0;                                    // 定义、初始化listview总高度值
//        for (int i = 0; i < mainAdapter.getCount(); i++) {
//            View listItem = mainAdapter.getView(i, null, mlistView);          // 获取单个item
//            listItem.setLayoutParams(new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));// 设置item高度为适应内容
//            listItem.measure(0, 0);                                        // 测量现在item的高度
//            totalHeight += listItem.getMeasuredHeight();                   // 总高度增加一个listitem的高度
//        }
//        ViewGroup.LayoutParams params = mlistView.getLayoutParams();
//        params.height = totalHeight + ((mlistView.getDividerHeight() )* (mainAdapter.getCount() - 1)); // 将分割线高度加上总高度作为最后listview的高度
//        mlistView.setLayoutParams(params);
//        mlistView.invalidate();
//    }
    public static void launchActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void launchActivityWithIntent(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static void launchActivityWithBean(Context context, Class c, Object data) {
        Intent intent = new Intent(context, c);
        intent.putExtra("data", (Serializable) data);
        context.startActivity(intent);
    }


    public static void createData(List list) {
        if (list.size() == 0) {
            //没数据刷新；
            for (int i = 0; i < 10; i++) {
                list.add(i + "");
            }
        }
    }

    public static void createData(List list, int type) {

        Random rd = new Random();

        if (list.size() == 0) {
            //没数据刷新；
            for (int i = 0; i < 10; i++) {
                int r = rd.nextInt(300);
                list.add(i + r);
            }
        }
    }

    public static List createURlsData(int type) {
        List list = new ArrayList();
//        img:http://m.hyhscm.com/uploadFile/image/efe0d1bb-78a7-4dc2-8c48-ccd354a56145.jpg
//     img:http://m.hyhscm.com/uploadFile/image/7f44e494-d742-4067-9bb9-9342d30d093c.jpg
//        img:http://m.hyhscm.com/uploadFile/image/c8fc82ce-7fa8-4944-a3b9-9f64eedc2547.jpg
//      img:http://m.hyhscm.com/uploadFile/image/eb45cab0-7463-42cb-98b0-6290be66e367.jpg
        list.add("http://m.hyhscm.com/uploadFile/image/efe0d1bb-78a7-4dc2-8c48-ccd354a56145.jpg");
//        list.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2476422624,920329811&fm=27&gp=0.jpg");
//        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1310959670,527650836&fm=27&gp=0.jpg");
//        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3627954361,2661374011&fm=27&gp=0.jpg");
//        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=29133.31296,1089761209&fm=27&gp=0.jpg");
//        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=454262349,220003234&fm=27&gp=0.jpg");
//        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1100621677,2276783790&fm=27&gp=0.jpg");
        return list;
    }


    //url 拼凑；
    public static String getUrl(String url, HashMap<String, String> params) {
        // 添加url参数
        if (params.size() > 0) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    //请求结果输出
    public static void logResult(String str) throws Exception {
        Log.e("result", str);
    }

    //
    public static void SP_Write(Context context, HashMap map) {
        //创建sharedPreference对象，UserInfo表示文件名，MODE_PRIVATE表示访问权限为私有的
        SharedPreferences sp = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
        //获得sp的编辑器
        SharedPreferences.Editor ed = sp.edit();
        if (map != null) {
            Iterator<String> it = map.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = (String) map.get(key);
                //以键值对的显示将用户名和密码保存到sp中
                ed.putString(key, value);
            }
        }
        ed.commit();
    }

    //读取保存在本地数据
    public static String SP_Read(Context context, String key) {
        //创建SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码
        String value = sp.getString(key, "");
        return value;
    }

    public static boolean SP_Clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
        if (sp != null) {
            sp.edit().clear().commit();
            return true;
        }
        return false;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public static PromptDialog createDialog(Activity activity) {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(activity);
        }
        return promptDialog;
    }

    public static PromptDialog setNormalDialog(Activity activity, String info) {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(activity);
        }
        promptDialog.showLoading(info);
        return promptDialog;
    }

    //
//    public static String dataToString(Date date){
//        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String da = dateTimeformat.format(date);
//        return da;
//    }
    /*年月日*/
    public static String dataToStringSimple(Date date) {
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd");
        String da = dateTimeformat.format(date);
        return da;
    }

    /*完整时间*/
    public static String dataToStringDetail(Date date) {
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String da = dateTimeformat.format(date);
        return da;
    }
    //广告dialog
//        new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {
//            promptDialog.getDefaultBuilder().backAlpha(150);
//            Glide.with(LoginActivity.this).load("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2476422624,920329811&fm=27&gp=0.jpg")
//                    .into(promptDialog.showAd(true, new OnAdClickListener() {
//                        @Override
//                        public void onAdClick() {
//                            Toast.makeText(LoginActivity.this,"点击了广告",Toast.LENGTH_SHORT).show();
//                        }
//                    }));
//        }
//    },2000);

    /**
     * /********************消息提示框
     *************/
//        promptDialog.showSuccess("登陆成功");
//        promptDialog.showError("登录失败");
//        promptDialog.showWarn("注意");
//        promptDialog.showInfo("成功了");
//        promptDialog.showCustom(R.mipmap.ic_launcher, "自定义图标的");
//        promptDialog.dismiss();
//        promptDialog.dismissImmediately();

//1 minute = 60 seconds
//1 hour = 60 x 60 = 3600
//1 day = 3600 x 24 = 86400
    public static String TimeDifference(Date startDate) {
        long different = new Date().getTime() - startDate.getTime();
        long secondsInMilli = 1000;

        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weekInMilli = daysInMilli * 7;
        long monthsMilli = daysInMilli * 30;
        long yearsMilli = monthsMilli * 12;

        long elapsedDays = different / daysInMilli;
        long elapsedHours = different / hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        long elapsedMonths = different / monthsMilli;
        long elapsedWeeks = different / weekInMilli;
        long elapsedYears = different / yearsMilli;
//    different = different%monthsMilli;
        if (elapsedYears > 0) {
            return String.format("%d年前", elapsedYears);
        } else if (elapsedMonths > 0) {
            return String.format("%d个月前", elapsedMonths);
        } else if (elapsedWeeks > 0) {
            return String.format("%d周前", elapsedWeeks);
        } else if (elapsedDays > 0) {
            return String.format("%d天前", elapsedDays);
        } else if (elapsedHours > 0) {
            return String.format("%d小时前", elapsedHours);

        }
        if (elapsedMinutes > 0) {
            return String.format("%d分钟前", elapsedMinutes);
        } else {
            return "刚刚";
        }


    }

    // 一天内时间用时分秒显示，一天后用年月日显示
    public static String dataToString(Date startDate) {
        long different = new Date().getTime() - startDate.getTime();
        long secondsInMilli = 1000;

        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;


        long elapsedDays = different / daysInMilli;
        long elapsedHours = different / hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;

//    different = different%monthsMilli;

        if (elapsedDays < 0) {
            if (elapsedHours > 0) {
                return String.format("%d 小时前", elapsedHours);
            } else if (elapsedMinutes > 0) {
                return String.format("%d 分钟前", elapsedMinutes);
            } else {
                return "刚刚";
            }
        } else {
            return dataToStringSimple(startDate);
        }


    }

    /**
     * 自适应高度 10dp 圆角
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        //                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        if (imageView == null) {
//                            return false;
//                        }
////                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
////                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
////                        }
//                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
//                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
//                        imageView.setLayoutParams(params);
//                        return false;
//                    }
//                })
        Glide.with(context)
                .load(imageUrl)
                .placeholder(errorImageId)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0, RoundedCornersTransformation.CornerType.ALL))
                .error(errorImageId)
                .into(imageView);


    }

    // 八位以上 数字字母组合
    public static boolean matchPwd(String password) {
        String regex = "^(?!\\d+$|[a-zA-Z]+$)\\w{8,}$";
        return password.matches(regex);
    }

    public static boolean matchPhone(String phone) {
        String regex = "^[1][3,4,5,7,8][0-9]{9}$";
        return phone.matches(regex);
    }


    /**
     * 获取应用当前版本代码
     *
     * @return
     */
    public static int getCurrentVerCode() {
        String packageName = EAPPApplication.getInstance().getPackageName();
        int currentVer = -1;
        try {
            currentVer = EAPPApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVer;
    }

    /**
     * 获取应用当前版本名称
     *
     * @return
     */
    public static String getCurrentVerName() {
        String packageName = EAPPApplication.getInstance().getPackageName();
        String currentVerName = "";
        try {
            currentVerName = EAPPApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVerName;
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getAppName() {
        return EAPPApplication.getInstance().getResources().getText(R.string.app_name).toString();
    }

    //
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}


