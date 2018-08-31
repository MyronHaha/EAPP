package com.hyhscm.myron.eapp.net;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.Im.DemoHelper;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.data.AccredInfo;
import com.hyhscm.myron.eapp.data.Advert;
import com.hyhscm.myron.eapp.data.BaseResult;
import com.hyhscm.myron.eapp.data.Bizinfo;
import com.hyhscm.myron.eapp.data.Comment;
import com.hyhscm.myron.eapp.data.CommitResult;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.FocusInfo;
import com.hyhscm.myron.eapp.data.Goods;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.ProductCategoryBean;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.ShareCallBack;
import com.hyhscm.myron.eapp.data.SignResult;
import com.hyhscm.myron.eapp.data.TopNum;
import com.hyhscm.myron.eapp.data.UploadRe;
import com.hyhscm.myron.eapp.data.User;
import com.hyhscm.myron.eapp.data.UserGoldBean;
import com.hyhscm.myron.eapp.data.UserInfo;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.model.Contract;
import com.hyphenate.easeui.utils.Global;
import com.mph.okdroid.OkDroid;
import com.mph.okdroid.response.GsonResHandler;
import com.mph.okdroid.response.IResponseDownloadHandler;
import com.mph.okdroid.response.IResponseHandler;
import com.mph.okdroid.response.RawResHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Jason on 2017/11/3.
 */

public class HttpCore {
    private static OkHttpClient okHttpClient;
    public static boolean ISNOVCODE = false;//测试没有验证码；
    private static OkDroid okDroid;
    private static String token = "";
    public static int userId = -100;
    public static String name = "";
    public static String img = "";
    public static String uid = "";
    public static String pushKey = "";
    public static boolean isLogin = false;

    public static String newVersionName = "";
    public static String newVersionCode = "";
    public static List<Contract> contractList = new ArrayList<>();

    //
    public static Object ShareItem = null; //

    public static int shareType = -1; //分享的类型；
    public static String MD5SU = "";//加密的su
    public static boolean isTopShared;  // 这个在分享都会传
    public static boolean isShared = false;  // 只有置顶后 分享  回界面刷新才用到
    public static boolean isMakedTop = false; // 是否置顶成功 ，同上

    public void setShareResult(boolean result) {
        this.isShared = result;
    }

    public static void setToken(String t) {
        token = t;
    }

    public static void init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        okDroid = new OkDroid(okHttpClient);
        okDroid.setDebug(false);
    }

    public static void post(final String url, HashMap map, final IResponseHandler handler) {
        L.e("url", url + "userID:" + map.toString() + "token:" + token);
        okDroid.post().url(url)
                .tag(url)
                .addHeader("token", token)
                .params(map)
                .enqueue(new IResponseHandler() {
                    @Override
                    public void onSuccess(Response response) {
                        if (url.equals(Url.HOSTNAME + Url.LOGIN) || url.equals(Url.HOSTNAME + Url.WXLOGIN)) {
                            setToken(response.header("token"));
                            L.e("url:" + url + "token:" + response.header("token"));
                        }
                        handler.onSuccess(response);
                    }

                    @Override
                    public void onFailed(int i, String s) {
                        handler.onFailed(i, s);
                    }

                    @Override
                    public void onProgress(long l, long l1) {
                        handler.onProgress(l, l1);
                    }
                });
    }

    //with params
    public static void get(String url, HashMap map, IResponseHandler handler) {
        String newUrl;
        if (map != null) {
            newUrl = common.getUrl(url, map);
        } else {
            newUrl = url;
        }
        L.e("newURl", newUrl + "userID:" + HttpCore.userId + "\n" + "token" + token);
        okDroid.get().url(newUrl)
                .tag(newUrl)
                .addHeader("token", token)
                .enqueue(handler);
    }

    // no params
    public static void get(String url, IResponseHandler handler) {
        L.e("newURl", url + "userID:" + HttpCore.userId);
        okDroid.get().url(url)
                .addHeader("token", token)
                .tag(url)
                .enqueue(handler);

    }

    public static boolean checkResult(int status, final BaseResult object) {
        if (status == 200 && object.getSuccess()) {
            return true;
        } else {
            if (status != 200) {
                common.promptDialog.showWarn("请求失败");
            }
            if (!object.getSuccess()) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.makeText(BaseActivity._mActivity,object.getMsg(),1500).show();
                    }
                });

                //errorCode -1
                if (object.getMsg().equals("登录超时！")) {
                    final Intent intent = new Intent(EAPPApplication.getInstance(), LoginActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            common.launchActivityWithIntent(EAPPApplication.getInstance(), intent);
                        }
                    }, 1000);
                }
            }
        }
        return false;
    }

    /*
    * path foldername +/ apkname.apk*/
    public static void downLoadFile(Context context, String url, String path, IResponseDownloadHandler handler) {
        okDroid.download().url(url)
                .tag(context)
                .filePath(Environment.getExternalStorageDirectory() + "/" + path)
                .enqueue(handler);
    }

    public static void uploadFile(String url, HashMap map, String absolutelyPath, GsonResHandler handler) {
        File file = new File(absolutelyPath);
        okDroid.upload().url(url)
                .tag(absolutelyPath)
                .params(map)
                .addHeader("token", token)
                .addFile("file", file)
                .enqueue(handler);

    }

    //-------------------------------------------------------------------------------------------------------------------------------------//
    //上传图片
    public static void UploadImage(String absolutelyPath, final IResultHandler<UploadRe> handler) {
        uploadFile(Url.HOSTNAME + Url.UPLOAD, null, absolutelyPath, new GsonResHandler<Result<UploadRe>>() {
            @Override
            public void onFailed(int i, String s) {
                L.e("上传失败--" + s);
            }

            @Override
            public void onSuccess(int i, Result<UploadRe> uploadReResult) {
                L.e("上传成功");
                handler.onSuccess(uploadReResult);
            }
        });
    }

    //首页banner
    public static void index(final IListResultHandler<Advert> handler) {
        get(Url.HOSTNAME + Url.INDEX, new GsonResHandler<ListResult<Advert>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(i + s);
            }

            @Override
            public void onSuccess(int i, ListResult<Advert> result) {
                handler.onSuccess(result);
            }
        });
    }

    //login
    public static void Login(final String phone, String pwd, final String p, final IResultHandler<UserInfo> handler) {

        HashMap map = new HashMap();
        map.put("phone", phone);
        map.put("pwd", pwd);
        map.put("p", p);
        post(Url.HOSTNAME + Url.LOGIN, map, new GsonResHandler<Result<UserInfo>>() {
            @Override
            public void onFailed(int i, String s) {
                L.e(i + "," + s);
//                BaseActivity.dialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<UserInfo> result) {
                L.e(i + "");
                if (checkResult(i, result)) {
//                    if (p == "" ) {
//                        common.promptDialog.showSuccess("登录成功");
//                    }
//                    HttpCore.setToken(result.getBiz().getToken());  // 设置token
                    HttpCore.userId = result.getBiz().getId();      //用户id
                    HttpCore.name = result.getBiz().getName();
                    Log.e("HttpCorename=" + HttpCore.name, "resultName:" + result.getBiz().getName());
                    //本地头像 给环信
                    HttpCore.img = result.getBiz().getImg();
                    Global.userImg = HttpCore.img;

                    HttpCore.pushKey = result.getBiz().getPushkey();
                    HttpCore.uid = result.getBiz().getUid();
                    HashMap user = new HashMap();                   //保存用户信息
                    user.put("phone", phone.equals("") ? result.getBiz().getPhone() : phone);
                    user.put("name", result.getBiz().getName());
                    user.put("p", result.getBiz().getP());
                    common.SP_Write(EAPPApplication.getInstance().getApplicationContext(), user);
                    handler.onSuccess(result);
                    isLogin = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initContractsList(new IListResultHandler() {
                                @Override
                                public void onSuccess(ListResult rs) {
                                    L.e("initConractList_success" + common.dataToStringDetail(new Date()));
                                }
                            });
                        }
                    }).start();
                    if (!DemoHelper.getInstance().isLoggedIn()) {
                        //环信登录
                        EMClient.getInstance().login(HttpCore.uid, HttpCore.pushKey, new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                L.e("环信登录成功！");
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                            }

                            @Override
                            public void onProgress(int progress, String status) {
                            }

                            @Override
                            public void onError(int code, final String error) {
                                L.e("环信登录失败！" + error);
                            }
                        });
                    }

                } else {
                    handler.onSuccess(result);
                }
            }
        });
    }

    /*login with code*/
    public static void LoginWithCode(final String phone, String code, final IResultHandler<UserInfo> handler) {

        HashMap map = new HashMap();
        map.put("phone", phone);
        map.put("code", code);
        post(Url.HOSTNAME + Url.LOGIN, map, new GsonResHandler<Result<UserInfo>>() {
            @Override
            public void onFailed(int i, String s) {

            }

            @Override
            public void onSuccess(int i, Result<UserInfo> result) {
                L.e(i + "");
                if (checkResult(i, result)) {
                    common.promptDialog.showSuccess("登录成功");
//                    HttpCore.setToken(result.getBiz().getToken());  // 设置token
                    HttpCore.userId = result.getBiz().getId();      //用户id
                    HttpCore.name = result.getBiz().getName();
                    //本地头像 给环信
                    HttpCore.img = result.getBiz().getImg();
                    Global.userImg = HttpCore.img;
                    HttpCore.pushKey = result.getBiz().getPushkey();
                    HttpCore.uid = result.getBiz().getUid();
                    HashMap user = new HashMap();                   //保存用户信息
                    user.put("phone", phone.equals("") ? result.getBiz().getPhone() : phone);
                    user.put("name", result.getBiz().getName());
                    user.put("p", result.getBiz().getP());
                    common.SP_Write(EAPPApplication.getInstance().getApplicationContext(), user);
                    handler.onSuccess(result);
                    isLogin = true;
                    initContractsList(new IListResultHandler() {
                        @Override
                        public void onSuccess(ListResult rs) {

                        }
                    });
                    //环信登录
                    if (!DemoHelper.getInstance().isLoggedIn()) {
                        //环信登录
                        EMClient.getInstance().login(HttpCore.uid, HttpCore.pushKey, new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                L.e("环信登录成功！");
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
//                startActivity(new Intent(getActivity(), MainActivity.class));

                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, final String error) {
                                L.e("环信登录失败！" + error);

                            }
                        });
                    }
                }
            }
        });
    }

    // 获取 contractList allContractList;
    private static void initContractsList(final IListResultHandler handler) {
        HttpCore.getContracts(new IListResultHandler<Contract>() {
            @Override
            public void onSuccess(ListResult<Contract> rs) {
                if (rs.getSuccess()) {
                    L.e("getContracts;" + common.dataToStringDetail(new Date()));
                    contractList = rs.getBiz();
                    Global.contractList = contractList;
                    L.e("Globalsize" + Global.contractList.size() + "");
                    getAllContracts(new IListResultHandler<Contract>() {
                        @Override
                        public void onSuccess(ListResult<Contract> rs) {
                            if (rs.getSuccess()) {
                                Global.allContractList = rs.getBiz();
                            }
                        }
                    });
                }

                handler.onSuccess(rs);
            }
        });
    }

    /*注册*/
    public static void Regist(String phone, String code, String type, final IResultHandler<String> handler) {
        HashMap map = new HashMap();
        map.put("phone", phone);
        map.put("code", code);
        map.put("type", type);
        post(Url.HOSTNAME + Url.REGIST, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Result result = gson.fromJson(s, new TypeToken<Result>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {

            }
        });
    }

    /*获取验证码  */
    public static void getVerifyCode(String url, String phone, final IResultHandler<String> handler) {
        HashMap map = new HashMap();
        map.put("phone", phone);
        post(Url.HOSTNAME + url, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Result result = gson.fromJson(s, new TypeToken<Result<String>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
                L.e(i + s);
            }
        });
    }

    //-------需求----------
    // 获取需求表
    public static void getDemandList(String st, int count, int pageSize, final IListResultHandler<Demand> handler) {
        getDemandList("", st, "", "", "", "", 0, pageSize, handler);
    }

    public static void getDemandById(String id, final IResultHandler handler) {
        HashMap map = new HashMap();
        map.put("id", id);
        get(Url.HOSTNAME + Url.DEMANDBYID, map, new RawResHandler() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(i + s);
            }

            @Override
            public void onSuccess(int i, String result1) {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                Gson gson = builder.create();
                Result<Demand> result = gson.fromJson(result1, new TypeToken<Result<Demand>>() {
                }.getType());
                handler.onSuccess(result);
            }
        });
    }

    public static void getDemandList(String k, String st, String aid, String did, String uid, String s, int count, int pageSize, final IListResultHandler<Demand> handler) {
        HashMap map = new HashMap();
        map.put("count", count + "");
        map.put("limit", pageSize + "");
        map.put("k", k);
        map.put("st", st);
        map.put("aid", aid);
        map.put("did", did);
        map.put("uid", uid);
        map.put("s", s);
        get(Url.HOSTNAME + Url.DEMAND, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<Demand>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(i + s);
            }

        });
    }

    /*我的需求列表*/
    public static void getMyDemandList(String k, String st, String aid, String did, String uid, String s, int count, int pageSize, final IListResultHandler<Demand> handler) {
        HashMap map = new HashMap();
        map.put("count", count + "");
        map.put("limit", pageSize + "");
        map.put("k", k);
        map.put("st", st);
        map.put("aid", aid);
        map.put("did", did);
        map.put("uid", uid);
        map.put("s", s);
        get(Url.HOSTNAME + Url.DEMAND_MY, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<Demand>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    /*发布需求*/
    public static void demandAdd(Demand d, final IResultHandler<Demand> handler) {
        HashMap map = new HashMap();
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        GsonBuilder builder = new GsonBuilder();
//        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
//        d.setCreationTime(new Date());  // 少了报错
        Gson gson = builder.create();
        String dstr = gson.toJson(d);
        map.put("b", dstr);
//
//        post(Url.HOSTNAME + Url.DEMAND_CREATE, map, new GsonResHandler<Result<Demand>>() {
//            @Override
//            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
//            }
//
//            @Override
//            public void onSuccess(int i, Result<Demand> result) {
//                handler.onSuc cess(result);
//            }
//        });
        post(Url.HOSTNAME + Url.DEMAND_CREATE, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                Gson gson = builder.create();
                Result<Demand> result = gson.fromJson(s, new TypeToken<Result<Demand>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
                L.e("需求发布失败：" + s);
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }
        });
    }

    /*删除需求*/
    public static void demandDel(int did, final IResultHandler<String> handler) {
        HashMap map = new HashMap();
        map.put("id", did + "");
        post(Url.HOSTNAME + Url.DEMAND_REMOVE, map, new GsonResHandler<Result<String>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<String> result) {
                handler.onSuccess(result);
            }
        });
    }

    /*增加需求评论*/
    public static void commentAdd(String sid, String content, final IResultHandler<Comment> handler) {
        HashMap map = new HashMap();
        map.put("sid", sid);
        map.put("c", content);
        post(Url.HOSTNAME + Url.DEMANDCOMMENT_ADD, map, new GsonResHandler<Result<Comment>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<Comment> result) {
                handler.onSuccess(result);
            }
        });
    }

    /*删除需求评论*/
    public static void commentDel(int did, final IResultHandler<String> handler) {
        HashMap map = new HashMap();
        map.put("id", did + "");
        post(Url.HOSTNAME + Url.DEMANDCOMMENT_DEL, map, new GsonResHandler<Result<String>>() {
            @Override
            public void onFailed(int i, String s) {
                L.e(i + "--error--" + s);
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<String> result) {
                L.e(i + "--succ--");
                handler.onSuccess(result);
            }
        });
    }

    //----------招商---------------------
    /*招商列表*/
    public static void getProductList(int count, int pageSize, final IListResultHandler<Product> handler) {
        HashMap map = new HashMap();
        map.put("count", count + "");
        map.put("limit", pageSize + "");
        get(Url.HOSTNAME + Url.MERCHANTLIST, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<Product>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    /*产品详情*/
    public static void getProductDetail(String id, final IResultHandler<Merchants> handler) {
        HashMap map = new HashMap();
        map.put("id", id + "");
        get(Url.HOSTNAME + Url.MERCHANT_DETAIL, new GsonResHandler<Result<Merchants>>() {
            @Override
            public void onFailed(int i, String s) {
                L.e("getHallDetail" + s);
            }

            @Override
            public void onSuccess(int i, Result<Merchants> result) {
                handler.onSuccess(result);
            }
        });
    }

    //
    public static void getProductById(String id, final IResultHandler handler) {
        HashMap map = new HashMap();
        map.put("id", id);
        get(Url.HOSTNAME + Url.PRODUCTBYID, map, new RawResHandler() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, String result1) {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                Gson gson = builder.create();
                Result<Goods> result = gson.fromJson(result1, new TypeToken<Result<Goods>>() {
                }.getType());
                handler.onSuccess(result);
            }
        });
    }

    /*发布招商*/
    public static void productAdd(Product b, final IResultHandler<Product> handler) {
        HashMap map = new HashMap();
        Gson g = new Gson();
        String dstr = g.toJson(b);
        ;
        map.put("b", dstr);
        post(Url.HOSTNAME + Url.MERCHANT_CREATE, map, new RawResHandler() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, String result1) {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                // Register an adapter to manage the date types as long values
//                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                        return new Date(json.getAsJsonPrimitive().getAsLong());
//                    }
//                });
                Gson gson = builder.create();
                Result<Product> result = gson.fromJson(result1, new TypeToken<Result<Product>>() {
                }.getType());
                handler.onSuccess(result);
            }
        });
    }

    /*商品上下架
    * b 需要编辑的实体
    * id 需要下架的实体对象id
    * t 1 下架 2 上架
    * */
    public static void product_invisible(Product b, String id, final IResultHandler<Product> handler) {

        HashMap map = new HashMap();
        String t = "";
        if (b != null) {
            t = "2";
            Gson g = new Gson();
            String dstr = g.toJson(b);
            map.put("b", dstr);
        } else {
            t = "1";
            map.put("id", id);
        }
        map.put("t", t);
        post(Url.HOSTNAME + Url.PRODUCT_INVISIBLE, map, new RawResHandler() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, String result1) {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd HH:mm:ss");

                Gson gson = builder.create();
                Result<Product> result = gson.fromJson(result1, new TypeToken<Result<Product>>() {
                }.getType());
                handler.onSuccess(result);
            }
        });
    }

    /*删除招商*/
    public static void productDel(int did, final IResultHandler<Product> handler) {
        HashMap map = new HashMap();
        map.put("id", did + "");
        post(Url.HOSTNAME + Url.MERCHANT_REMOVE, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Result<Product> result = gson.fromJson(s, new TypeToken<Result<Product>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    /*增加产品评论*/
    public static void pcommentAdd(String sid, String content, final IResultHandler<Comment> handler) {
        HashMap map = new HashMap();
        map.put("sid", sid);
        map.put("c", content);
        post(Url.HOSTNAME + Url.MERCHANTCOMMENT_ADD, map, new GsonResHandler<Result<Comment>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<Comment> result) {
                handler.onSuccess(result);
            }
        });
    }

    /*删除产品评论*/
    public static void pcommentDel(int did, final IResultHandler<String> handler) {
        HashMap map = new HashMap();
        map.put("id", did + "");
        post(Url.HOSTNAME + Url.MERCHANTCOMMENT_DEL, map, new GsonResHandler<Result<String>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<String> result) {
                handler.onSuccess(result);
            }
        });
    }

    //----------展厅------------
    /*展厅列表*/
    public static void getHallList(int count, int pageSize, final IListResultHandler<Merchants> handler) {
        HashMap map = new HashMap();
        map.put("count", count + "");
        map.put("limit", pageSize + "");
        get(Url.HOSTNAME + Url.COMPANY, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<Merchants>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    /*展厅详情*/
    public static void getHallDetail(String id, final IResultHandler<Merchants> handler) {

        HashMap map = new HashMap();
        map.put("uid", id);
        get(Url.HOSTNAME + Url.COMPANY_DETAIL, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Result<Merchants> result = gson.fromJson(s, new TypeToken<Result<Merchants>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    public static void changeHallInfo(final Merchants merchant, final IResultHandler<Merchants> handler) {
        HashMap map = new HashMap();
        map.put("b", new Gson().toJson(merchant));
        post(Url.HOSTNAME + Url.UPDATEMINE, map, new GsonResHandler<Result<Merchants>>() {
            @Override
            public void onFailed(int i, String s) {
                L.e("changeHallInfo" + s);
            }

            @Override
            public void onSuccess(int i, Result<Merchants> rs) {
                handler.onSuccess(rs);
            }
        });
    }


    //----------用户------------
    /*修改密码
    * param ：type 0 修改密码，1忘记密码*/
    public static void changePwd(String phone, String oldPwd, String newPwd, String code, int type, final IResultHandler handler) {
        String url = "";
        HashMap map = new HashMap();

        map.put("newPwd", newPwd);
        if (type == 1) {
            map.put("code", code);
            map.put("phone", phone);
            url = Url.HOSTNAME + Url.CHANGECODEPWD;
        }
        if (type == 0) {
            map.put("oldPwd", oldPwd);
            url = Url.HOSTNAME + Url.CHANGEPWD;
        }
        post(url, map, new RawResHandler() {
            @Override
            public void onFailed(int i, String s) {
                L.e("changePwd" + s);
            }

            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Result result = gson.fromJson(s, new TypeToken<Result>() {
                }.getType());
                handler.onSuccess(result);
            }
        });
    }

    /*获取用户信息*/
    public static void getUserInfo(final IResultHandler<User> handler) {
        get(Url.HOSTNAME + Url.USER_INFO, new GsonResHandler<Result<User>>() {
            @Override
            public void onFailed(int i, String s) {

            }

            @Override
            public void onSuccess(int i, Result<User> rs) {
                handler.onSuccess(rs);
            }
        });
    }

    /*完善用户信息*/
    public static void bizAdd(Bizinfo b, final IResultHandler<Bizinfo> handler) {
        HashMap map = new HashMap();
        Gson g = new Gson();
        String dstr = g.toJson(b);
        ;
        map.put("b", dstr);
        post(Url.HOSTNAME + Url.INPUTINFO, map, new GsonResHandler<Result<Bizinfo>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<Bizinfo> result) {
                handler.onSuccess(result);
            }
        });
    }

    /*修改用户信息*/
    public static void userm(User b, final IResultHandler<UserInfo> handler) {
        HashMap map = new HashMap();
        Gson g = new Gson();
        String dstr = g.toJson(b);
        ;
        map.put("b", dstr);
        post(Url.HOSTNAME + Url.USER_CHANGE, map, new GsonResHandler<Result<UserInfo>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<UserInfo> result) {
                handler.onSuccess(result);
            }
        });
    }

    public static void getAccredInfo(final IResultHandler<AccredInfo> hanler) {
        get(Url.HOSTNAME + Url.USER_VERIFY, new GsonResHandler<Result<AccredInfo>>() {
            @Override
            public void onFailed(int i, String s) {
                L.e("error-getAccredInfo" + s);
            }

            @Override
            public void onSuccess(int i, Result<AccredInfo> rs) {
                hanler.onSuccess(rs);
            }
        });
    }

    /*修改深度认证信息*/
    public static void accredApply(AccredInfo b, final IResultHandler<AccredInfo> handler) {
        HashMap map = new HashMap();
        Gson g = new Gson();
        String dstr = g.toJson(b);
        ;
        map.put("b", dstr);
        post(Url.HOSTNAME + Url.USER_VERIFY_CHANGE, map, new GsonResHandler<Result<AccredInfo>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<AccredInfo> result) {
                handler.onSuccess(result);
            }
        });
    }

    // 代理设置
    public static void agentSetting(Bizinfo b, final IResultHandler<Bizinfo> handler) {
        HashMap map = new HashMap();
        Gson g = new Gson();
        String dstr = g.toJson(b);
        ;
        map.put("b", dstr);
        post(Url.HOSTNAME + Url.USER_AGENT_CHANGE, map, new GsonResHandler<Result<Bizinfo>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<Bizinfo> result) {
                handler.onSuccess(result);
            }
        });
    }

    //获取代理信息
    public static void getAgentInfo(final IResultHandler<Bizinfo> handler) {
        get(Url.HOSTNAME + Url.USER_AGENT, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Result result = gson.fromJson(s, new TypeToken<Result<Bizinfo>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
                L.e("getAgentInfo- error");
            }
        });
    }
    //-------------------------------------新闻

    /*新闻列表 t 类别*/
    public static void getNewsList(int count, int pageSize, final IListResultHandler<News> handler) {
        HashMap map = new HashMap();
        map.put("count", count + "");
        map.put("limit", pageSize + "");
        get(Url.HOSTNAME + Url.NEWS, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<News>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    /*news by id */
    public static void getNewsById(String id, final IResultHandler handler) {
        HashMap map = new HashMap();
        map.put("id", id);
        get(Url.HOSTNAME + Url.PRODUCTBYID, map, new RawResHandler() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, String result1) {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                Gson gson = builder.create();
                Result<News> result = gson.fromJson(result1, new TypeToken<Result<News>>() {
                }.getType());
                handler.onSuccess(result);
            }
        });
    }

    //-------------------------------------IM
   /*获取联系人列表*/
    public static void getContracts(final IListResultHandler handler) {
        get(Url.HOSTNAME + Url.CONTACT, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<Contract>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    /*最近好友请求*/
    public static void getFriendRequests(final IListResultHandler handler) {
        get(Url.HOSTNAME + Url.FRIEND_REQUEST, new GsonResHandler<ListResult<Contract>>() {
            @Override
            public void onSuccess(int i, ListResult<Contract> contractListResult) {
                handler.onSuccess(contractListResult);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }


        });
    }

    /*获取联系人列表*/
    public static void getAllContracts(final IListResultHandler<Contract> handler) {
        get(Url.HOSTNAME + Url.CONTACT_SEARCH, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<Contract>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

    /*请求添加好友*/
    public static void addFriend(String cid, String n, String m, final IResultHandler<Contract> handler) {
        HashMap map = new HashMap();
        map.put("cid", cid);
        map.put("n", n);
        map.put("m", m);
        post(Url.HOSTNAME + Url.FRIEND_ADD, map, new GsonResHandler<Result<Contract>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<Contract> rs) {
                handler.onSuccess(rs);
            }
        });
    }

    /*删除好友*/
    public static void deleteFriend(String id, final IResultHandler<Contract> handler) {
        HashMap map = new HashMap();
        map.put("id", id);
        post(Url.HOSTNAME + Url.FRIEND_DELETE, map, new GsonResHandler<Result<Contract>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<Contract> contractResult) {
                handler.onSuccess(contractResult);
            }
        });
    }

    /*处理好友请求*/
    public static void doRequest(String id, String type, final IResultHandler<Contract> handler) {
        HashMap map = new HashMap();
        map.put("id", id);  // value
        map.put("t", type); // 0 拒绝 1 接受
        post(Url.HOSTNAME + Url.FRIEDN_DOREQUEST, map, new GsonResHandler<Result<Contract>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<Contract> rs) {
                handler.onSuccess(rs);
            }
        });
    }

    // 意见反馈
    public static void feedBack(String content, final IResultHandler handler) {
        HashMap map = new HashMap();
        map.put("content", content);
        post(Url.HOSTNAME + Url.FEEDBACK, map, new GsonResHandler<Result>() {
            @Override
            public void onFailed(int i, String s) {

            }

            @Override
            public void onSuccess(int i, Result result) {
                handler.onSuccess(result);
            }
        });
    }

    //服务端微信登录
    public static void loginWx(final String code, final String phone, final IResultHandler handler) {
        HashMap map = new HashMap();
        map.put("code", code);
        map.put("phone", phone);

//        LoginWithCode(phone,code,handler);
        post(Url.HOSTNAME + Url.WXLOGIN, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                Gson gson = builder.create();

                try {
//                    JSONObject job = new JSONObject(s);
//                    String bizString = job.getString("biz");
                    JSONObject job = new JSONObject(s);
                    boolean isSuccess = job.getBoolean("success");
//                    if (bizString.length() > 2) {


                    if (isSuccess) {
                        Result<UserInfo> result = gson.fromJson(s, new TypeToken<Result<UserInfo>>() {
                        }.getType());
//                        HttpCore.setToken(result.getBiz().getToken());  // 设置token
                        HttpCore.userId = result.getBiz().getId();      //用户id
                        HttpCore.name = result.getBiz().getName();
                        Log.e("HttpCorename=" + HttpCore.name, "resultName:" + result.getBiz().getName());
                        //本地头像 给环信
                        HttpCore.img = result.getBiz().getImg();
                        Global.userImg = HttpCore.img;
                        HttpCore.pushKey = result.getBiz().getPushkey();
                        HttpCore.uid = result.getBiz().getUid();
                        HashMap user = new HashMap();                   //保存用户信息
                        user.put("phone", result.getBiz().getPhone());
                        user.put("name", result.getBiz().getName());
                        user.put("p", result.getBiz().getP());
                        common.SP_Write(EAPPApplication.getInstance().getApplicationContext(), user);
                        isLogin = true;

                        initContractsList(new IListResultHandler() {
                            @Override
                            public void onSuccess(ListResult rs) {

                            }
                        });
                        if (!DemoHelper.getInstance().isLoggedIn()) {
                            //环信登录
                            EMClient.getInstance().login(HttpCore.uid, HttpCore.pushKey, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    L.e("环信登录成功！");
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                }

                                @Override
                                public void onProgress(int progress, String status) {
                                }

                                @Override
                                public void onError(int code, final String error) {
                                    L.e("环信登录失败！" + error);
                                }
                            });
                        }
                        handler.onSuccess(result);
                    } else {
                        Result<String> result = gson.fromJson(s, new TypeToken<Result<String>>() {
                        }.getType());
                        handler.onSuccess(result);
                    }

//                    }
//                    else {
//                        Result<String> result = gson.fromJson(s, new TypeToken<Result<String>>() {
//                        }.getType());
//                        handler.onSuccess(result);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int i, String s) {

            }

//            @Override
//            public void onSuccess(int i, Result<String> result) {
//                if (result.getSuccess()&&result.getBiz()!=null) {
//
//                    HttpCore.setToken(result.getBiz().getToken());  // 设置token
//                    HttpCore.userId = result.getBiz().getId();      //用户id
//                    HttpCore.name = result.getBiz().getName();
//                    Log.e("HttpCorename=" + HttpCore.name, "resultName:" + result.getBiz().getName());
//                    HttpCore.img = result.getBiz().getImg();
//                    HttpCore.pushKey = result.getBiz().getPushkey();
//                    HttpCore.uid = result.getBiz().getUid();
//                    HashMap user = new HashMap();                   //保存用户信息
//                    user.put("phone", result.getBiz().getPhone());
//                    user.put("name", result.getBiz().getName());
//                    user.put("p", result.getBiz().getP());
//                    common.SP_Write(EAPPApplication.getInstance().getApplicationContext(), user);
//                    isLogin = true;
//                    initContractsList(new IListResultHandler() {
//                        @Override
//                        public void onSuccess(ListResult rs) {
//
//                        }
//                    });
//                    if (!DemoHelper.getInstance().isLoggedIn()) {
//                        //环信登录
//                        EMClient.getInstance().login(HttpCore.uid, HttpCore.pushKey, new EMCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                L.e("环信登录成功！");
//                                EMClient.getInstance().groupManager().loadAllGroups();
//                                EMClient.getInstance().chatManager().loadAllConversations();
//                            }
//
//                            @Override
//                            public void onProgress(int progress, String status) {
//                            }
//
//                            @Override
//                            public void onError(int code, final String error) {
//                                L.e("环信登录失败！" + error);
//                            }
//                        });
//                    }
//                }
//                handler.onSuccess(result);
//            }
        });

    }

    public static void getFocusList(int count, int pageSize, final IListResultHandler<FocusInfo> handler) {
        HashMap map = new HashMap();
        map.put("count", count + "");
        map.put("limit", pageSize + "");
        get(Url.HOSTNAME + Url.FOCUSLIST, map, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<FocusInfo>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }


    //置顶
    /*t:1 商机、代理 2、招商产品*
    / t1:分享置顶 1
     */
    public static void makeTop(String id, String t, String t1, final IResultHandler<CommitResult> handler) {
        HashMap map = new HashMap();
        map.put("id", id);
        map.put("t", t);
        map.put("t1", t1);
        post(Url.HOSTNAME + Url.MAKETOP, map, new GsonResHandler<Result<CommitResult>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<CommitResult> commitResultResult) {
                handler.onSuccess(commitResultResult);
            }
        });
    }

    //获取签到信息 no total
    public static void getSignInfo(final RawResHandler handler) {
        get(Url.HOSTNAME + Url.SIGNINFO, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                handler.onSuccess(i, s);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }


        });
    }

    //签到
    public static void Sign(final IResultHandler<SignResult> handler) {

        post(Url.HOSTNAME + Url.SIGN, new HashMap(), new GsonResHandler<Result<SignResult>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<SignResult> signResultResult) {
                handler.onSuccess(signResultResult);
            }
        });
    }

    //获取头部数字
    public static void getTopNum(final IListResultHandler<TopNum> handler) {
        get(Url.HOSTNAME + Url.TOPNUM, new GsonResHandler<ListResult<TopNum>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, ListResult<TopNum> signResultResult) {
                handler.onSuccess(signResultResult);
            }
        });
    }

    /*分享回调
    *  su ,md5 加密字段
    *  itemID item 的id
    *  st  1. 商机
    *      2.代理
    *      3.产品
    *  t1  仅置顶的分享需要 t1 = 1
    * */
    public static void sharedCallBack(String su, String itemId, String st, String t1, final IResultHandler<ShareCallBack> handler) {
        HashMap map = new HashMap();
        map.put("su", su);
        map.put("sid", itemId);
        map.put("st", st);
        map.put("t1", t1);

        post(Url.HOSTNAME + Url.SHARE_CALLBACK, new HashMap(), new GsonResHandler<Result<ShareCallBack>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<ShareCallBack> shareCallBackResult) {
                handler.onSuccess(shareCallBackResult);
            }
        });
    }

    /*金豆查询接口*/
    public static void getUserGBean(final IResultHandler<UserGoldBean> handler) {
        get(Url.HOSTNAME + Url.HEBIDETAIL, new GsonResHandler<Result<UserGoldBean>>() {
            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

            @Override
            public void onSuccess(int i, Result<UserGoldBean> userGoldBeanResult) {
//                handler.onSuccess(signResultResult);
                handler.onSuccess(userGoldBeanResult);
            }
        });
    }

    public static void getProductCategory(final IListResultHandler<ProductCategoryBean> handler) {
        get(Url.HOSTNAME + Url.PRODUCT_CATEGORY, new RawResHandler() {
            @Override
            public void onSuccess(int i, String s) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ListResult result = gson.fromJson(s, new TypeToken<ListResult<ProductCategoryBean>>() {
                }.getType());
                handler.onSuccess(result);
            }

            @Override
            public void onFailed(int i, String s) {
//                common.promptDialog.showError(NetUtil.getErrorString(s));
            }

        });
    }

}
