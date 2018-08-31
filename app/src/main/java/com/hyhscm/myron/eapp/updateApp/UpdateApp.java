package com.hyhscm.myron.eapp.updateApp;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;


import com.common.design.MaterialDialog;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.data.AppInfo;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.CommonProgressDialog;
import com.mph.okdroid.response.GsonResHandler;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jason on 2018/3/9.
 */

public class UpdateApp {
    private static final String APPID = "5a56bd60548b7a544d00005b";
    private static final String API_TOKEN = "4f736d2bd6256a1bd5dd2ffde6782f20";
    static UpdateApp obj = null;
    static Context mContext;
    private static final int REQUEST_CODE_PERMISSION_SD = 101;
    private static String packageName;
    private static String appName;
    private static String downLoadPath;
    private static String apkName;
    private static CommonProgressDialog pBar;
    private static boolean isclick = false;
    public UpdateApp() {
        if (obj == null) {
            obj = this;
        }

    }

    public static UpdateApp getInstance(Context context) {
        mContext = context;
        packageName = EAPPApplication.getInstance().getPackageName();
        appName = common.getAppName();
        downLoadPath = Environment.getExternalStorageDirectory() + "/" + packageName;
        apkName = appName + ".apk";
        return obj == null ? new UpdateApp() : obj;
    }


//  //权限监听
//    private RationaleListener rationaleListener = new RationaleListener() {
//        @Override
//        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
//            // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
//            AndPermission.rationaleDialog(Main2Activity.this, rationale).show();
//        }
//    };


    public void checkPermission(Context context,boolean autoShow) {
        this.isclick = autoShow;
        AndPermission.with(mContext)
                .requestCode(200)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .callback(listener)
                .start();

    }

    // 获取更新版本号
    public void getVersion() {
        final int vision = common.getCurrentVerCode();
        if(NetUtil.getNetWorkState(mContext)==NetUtil.NETWORK_NONE){

        }else{
        HttpCore.get("http://api.fir.im/apps/latest/" + APPID + "?api_token=" + API_TOKEN, new GsonResHandler<AppInfo>() {
            @Override
            public void onFailed(int i, String s) {
                Log.e("获取信息失败", s);
            }

            @Override
            public void onSuccess(int i, AppInfo appInfo) {

                String newversion = appInfo.getVersion();

                String content = appInfo.getChangelog() == null ? "" : appInfo.getChangelog();
                String url = appInfo.getInstallUrl();

                double newversioncode = Double
                        .parseDouble(newversion);
//        int cc = (int) (newversioncode);
                double cc = newversioncode;
                System.out.println(newversion + "v" + vision + ",,"
                        + cc);
                // 版本号不同
                if (cc != vision) {
                    // 有新版本 记录新版本数据
                    if (vision < cc) {
                        HttpCore.newVersionCode = newversion;
                        HttpCore.newVersionName=appInfo.getVersionShort();
                        System.out.println(newversion + "v"
                                + vision);
                    //跨越两个版本 强制显示；
//                        if((cc-vision)>=2){
//                            ShowDialog(vision, newversion, content, url);
//                        }else{
//                            if(isclick){ // 主动更新
//                                ShowDialog(vision, newversion, content, url);
//                            }            //用户操作记录
//                            else if (!common.SP_Read(mContext, "updateClose").equals("true")) {
//                                ShowDialog(vision, newversion, content, url);
//                            }
//                        }
                        ShowDialog(vision, newversion, content, url);
                    }
                }else {
                       if(isclick){
                           new MaterialDialog.Builder(mContext)
                                   .setCanceledOnTouchOutside(true)
                                   .setTitle("提示")
                                   .setMessage("当前已是最新版本")
                                   .setPositiveButton(new MaterialDialog.OnClickListener() {
                                       @Override
                                       public boolean onClick(DialogInterface dialogInterface, int i) {
                                           dialogInterface.dismiss();
                                           return false;
                                       }
                                   })
                                   .show();
                       }
                }
            }
        });}
//        String data = "";

//        = "更新内容"+"\n" +
//                "1.xxxxxxxxxx\n" +
//                "2.xxxxxxxxxxxx-\n";//更新内容
//        String url = "https://download.fir.im/apps/5a56bd60548b7a544d00005b/install?download_token=1e844cfc226568f2b3916f6a1bf91c89&release_id=5a9e02a4548b7a649947a6c8";//安装包下载地址

    }

    /**
     * 提示升级
     */
    private void ShowDialog(int vision, String newversion, String content,
                            final String url) {
//        new android.app.AlertDialog.Builder(this)
//                .setTitle("版本更新")
//                .setMessage(content)
//                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        pBar = new CommonProgressDialog(Main2Activity.this);
//                        pBar.setCanceledOnTouchOutside(false);
//                        pBar.setTitle("正在下载");
//                        pBar.setCustomTitle(LayoutInflater.from(
//                                Main2Activity.this).inflate(
//                                R.layout.title_dialog, null));
//                        pBar.setMessage("正在下载");
//                        pBar.setIndeterminate(true);
//                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        pBar.setCancelable(true);
//                        // downFile(URLData.DOWNLOAD_URL);
//                        final DownloadTask downloadTask = new DownloadTask(
//                                Main2Activity.this);
//                        downloadTask.execute(url);
//                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                downloadTask.cancel(true);
//                            }
//                        });
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();

        //        final MaterialDialog dialog = new MaterialDialog(this);//自定义的对话框，可以呀alertdialog
//        dialog.content(content).btnText("取消", "更新").title("版本更新 ")
//                .titleTextSize(15f).show();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
//            @Override
//            public void onBtnClick() {
//                dialog.dismiss();
//            }
//        }, new OnBtnClickL() {// right btn click listener
//
//            @Override
//            public void onBtnClick() {
//                dialog.dismiss();
//                // pBar = new ProgressDialog(MainActivity.this,
//                // R.style.dialog);
//                pBar = new CommonProgressDialog(MainActivity.this);
//                pBar.setCanceledOnTouchOutside(false);
//                pBar.setTitle("正在下载");
//                pBar.setCustomTitle(LayoutInflater.from(
//                        MainActivity.this).inflate(
//                        R.layout.title_dialog, null));
//                pBar.setMessage("正在下载");
//                pBar.setIndeterminate(true);
//                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pBar.setCancelable(true);
//                // downFile(URLData.DOWNLOAD_URL);
//                final DownloadTask downloadTask = new DownloadTask(
//                        MainActivity.this);
//                downloadTask.execute(url);
//                pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        downloadTask.cancel(true);
//                    }
//                });
//            }
//        });
        new MaterialDialog.Builder(mContext)
                .setCanceledOnTouchOutside(false)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        pBar = new CommonProgressDialog(mContext);
                        pBar.setCanceledOnTouchOutside(false);
                        pBar.setTitle("正在下载");
                        pBar.setCustomTitle(LayoutInflater.from(
                                mContext).inflate(
                                R.layout.title_dialog, null));
                        pBar.setMessage("正在下载");
                        pBar.setIndeterminate(true);
                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pBar.setCancelable(true);
                        // downFile(URLData.DOWNLOAD_URL);
                        final DownloadTask downloadTask = new DownloadTask(
                                mContext);
                        downloadTask.execute(url);
                        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true);
                            }
                        });
                        return false;
                    }
                })
                .setNegativeButton("稍后再试", new MaterialDialog.OnClickListener() {
                    @Override
                    public boolean onClick(DialogInterface dialog, int i) {
                        HashMap map = new HashMap();
                        map.put("updateClose","true");
                        common.SP_Write(mContext,map);
                        dialog.dismiss();
                        return false;
                    }
                }).show();
    }


    /**
     * 下载应用
     */
    static class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(downLoadPath,
                            apkName);

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(context, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            pBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            pBar.setIndeterminate(false);
            pBar.setMax(100);
            pBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            pBar.dismiss();
            if (result != null) {

//                AndPermission.with(mContext)
//                        .requestCode(REQUEST_CODE_PERMISSION_SD)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(rationaleListener
//                        )
//                        .send();
//
//
//                Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(context, "File downloaded",
                // Toast.LENGTH_SHORT)
                // .show();
                update();
            }

        }
    }

    // 执行apk
    private static void update() {
        //安装应用
        File apkFile = new File(downLoadPath + "/" + apkName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(mContext, packageName + ".provider", apkFile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }


    //权限监听
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            if (requestCode == 200) {
                getVersion();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                //适配小米机型
                AppOpsManager appOpsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, Process.myUid(), mContext.getPackageName());
                if (checkOp == AppOpsManager.MODE_IGNORED) {
//                    ActivityCompat.requestPermissions(UpdateApp.this,
//                            new String[]{"android.permission.CHANGE_WIFI_STATE", "android.permission.WRITE_SETTINGS"}, 101);
//                    return;
                    AndPermission.with(mContext)
                            .requestCode(100)
                            .permission(Manifest.permission.WRITE_SETTINGS,Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                            .callback(listener)
                            .start();
                }
                MyToast.makeText(mContext, "用户拒绝授权", 2000).show();
            }
        }
    };



}
