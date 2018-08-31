package com.hyhscm.myron.eapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Home.ENewsDetail;
import com.hyhscm.myron.eapp.activity.Im.ChatActivity;
import com.hyhscm.myron.eapp.activity.Im.DemoHelper;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.activity.User.MyNeedDetail;
import com.hyhscm.myron.eapp.activity.User.MyProductDtail;
import com.hyhscm.myron.eapp.activity.User.NeedPublish;
import com.hyhscm.myron.eapp.activity.User.ProductPublish;
import com.hyhscm.myron.eapp.data.Advert;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UserInfo;
import com.hyhscm.myron.eapp.fragment.Fragment_yfx;
import com.hyhscm.myron.eapp.fragment.Fragment_my;
import com.hyhscm.myron.eapp.fragment.Fragment_home;
import com.hyhscm.myron.eapp.fragment.Fragment_yxq;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.updateApp.UpdateApp;
import com.hyhscm.myron.eapp.utils.DensityUtil;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.morewindow.MoreWindow;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.utils.Global;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hyhscm.myron.eapp.fragment.Fragment_yxq.conversationListFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ASK_WRITE_SETTINGS = 0x100 ;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.tab_post_icon)
    ImageView iv3;
    @BindView(R.id.iv4)
    ImageView iv4;
    @BindView(R.id.iv5)
    ImageView iv5;

    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.rl4)
    RelativeLayout rl4;
    @BindView(R.id.rl5)
    RelativeLayout rl5;
    @BindView(R.id.unread_msg_number)
    TextView unreadLabel;
    MoreWindow mMoreWindow;
//    @BindView(R.id.unread_msg_number)
//    TextView unreadLabel;

    //    private String[] titles = {"e链", "医械圈", "e发现", "我的"};
    private int[] imgsNormal = {R.mipmap.tab_home, R.mipmap.tab_yxq, R.mipmap.tab_yfx, R.mipmap.tab_my};
    private int[] imgsTab = {R.mipmap.tab_home_click, R.mipmap.tab_yxq_click, R.mipmap.tab_yfx_click, R.mipmap.tab_my_click};
    private ImageView[] iv;
    private TextView[] tv;
    //    private int[] imgs = { R.drawable.selector_tab3, R.drawable.selector_tab2,R.drawable.selector_tab1, R.drawable.selector_tab4};
    Fragment currentFragment = null;
    Fragment_yfx home;
    Fragment_my my;
    Fragment_home yfx; //医发现
    Fragment_yxq yxq; //医械圈
    //exit
    private boolean isExit = false;
    private long lastTime = 0;

    public static List bannerList = new ArrayList();
    // im
    EMMessageListener messageListener;   //信息监听
    EMConversationListener conversationListener;//
    EMContactListener emContactListener;//好友管理监听
    public static Context mcontext;
    public static View lastView;

    public static int allneedCount;
    public static int allproductCount;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("onCreate:bundle"+bundle==null?"null":"not null");
        Log.e("width", "" + DensityUtil.getDisplay(this).getWidth());
        Log.e("height", "" + DensityUtil.getDisplay(this).getHeight());
        mcontext = this;
        _mActivity = this;
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_CODE_ASK_WRITE_SETTINGS);
            } else {
                //有了权限，你要做什么呢？具体的动作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateApp.getInstance(MainActivity.this).checkPermission(MainActivity.this, false);
                    }
                }).start();
            }
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UpdateApp.getInstance(MainActivity.this).checkPermission(MainActivity.this, false);
                }
            }).start();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                UpdateApp.getInstance(MainActivity.this).checkPermission(MainActivity.this, false);
//            }
//        }).start();
        addHuanXinListeners();
    }
//  6.0 M 版本 不能弹框询问 读写权限 bug
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ASK_WRITE_SETTINGS) {
            if (Settings.System.canWrite(this)) {
                L.e( "onActivityResult write settings granted" );
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateApp.getInstance(MainActivity.this).checkPermission(MainActivity.this, false);
                    }
                }).start();
            }
        }
    }

    //环信监听接口
    public void addHuanXinListeners() {
        // 会话监听
        if (messageListener == null) {
            messageListener = new EMMessageListener() {
                @Override
                public void onMessageReceived(List<EMMessage> messages) {

                    // notify new message
                    for (EMMessage message : messages) {
                        if (!DemoHelper.getInstance().getEaseUi().hasForegroundActivies()) {
                            DemoHelper.getInstance().getNotifier().onNewMsg(message);
                        }
                    }
                    refreshUIWithMessage();
//                    if (Fragment_yxq.conversationListFragment != null) {
//                        Fragment_yxq.conversationListFragment.refresh();
//                    }

                }

                @Override
                public void onCmdMessageReceived(List<EMMessage> messages) {
                    //red packet code : 处理红包回执透传消息
//                    for (EMMessage message : messages) {
//                        EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                        final String action = cmdMsgBody.action();//获取自定义action
//                        if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                            RedPacketUtil.receiveRedPacketAckMessage(message);
//                        }
//                    }
                    //end of red packet code
                    if (Fragment_yxq.conversationListFragment != null) {
                        Fragment_yxq.conversationListFragment.refresh();
                    }

                }

                @Override
                public void onMessageRead(List<EMMessage> messages) {
                    L.e("onMessageRead");
                }

                @Override
                public void onMessageDelivered(List<EMMessage> message) {

                    L.e("onMessageDelivered");
                }

                @Override
                public void onMessageRecalled(List<EMMessage> messages) {
                    L.e("onMessageRecalled");
                    if (Fragment_yxq.conversationListFragment != null) {
                        Fragment_yxq.conversationListFragment.refresh();
                    }
                }

                @Override
                public void onMessageChanged(EMMessage message, Object change) {

                    L.e("onMessageChanged");

                }
            };
        }
        //好友
        if (emContactListener == null) {
            emContactListener = new EMContactListener() {
                @Override
                public void onContactAdded(String s) {
                    L.e("好友添加了" + s);
                }

                @Override
                public void onContactDeleted(String s) {
                    L.e("好友删除" + s);
                }

                @Override
                public void onContactInvited(String s, String s1) {

                    L.e("收到好友邀请" + s + s1);
                    String from = Global.getNickByAll(s).getNname();
                    EMMessage msgNotification = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                    EMTextMessageBody txtBody = new EMTextMessageBody("可以加一下好友吗？");
                    msgNotification.addBody(txtBody);
                    msgNotification.setUnread(true);
                    msgNotification.setAttribute("contract", "add");
                    msgNotification.ext().put("contract", "add");
                    msgNotification.setFrom(s.trim());
                    L.e("sssssssss" + Global.getNickByAll(s).getNname());
                    msgNotification.setMsgTime(System.currentTimeMillis());
                    msgNotification.setLocalTime(System.currentTimeMillis());
                    msgNotification.setChatType(EMMessage.ChatType.Chat);
//                    msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                    msgNotification.setStatus(EMMessage.Status.SUCCESS);
                    DemoHelper.getInstance().getNotifier().sendNotification(msgNotification, false);
                }

                @Override
                public void onFriendRequestAccepted(String s) {
                    EMMessage msgNotification = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                    EMTextMessageBody txtBody = new EMTextMessageBody("我已接受你的请求");
                    msgNotification.addBody(txtBody);
                    msgNotification.setUnread(true);
                    msgNotification.setAttribute("contract", "accepted");
//                    msgNotification.ext().put("contract","accepted");
                    msgNotification.setFrom(s);
                    msgNotification.setMsgTime(System.currentTimeMillis());
                    msgNotification.setLocalTime(System.currentTimeMillis());
                    msgNotification.setChatType(EMMessage.ChatType.Chat);
//                    msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                    msgNotification.setStatus(EMMessage.Status.SUCCESS);
                    DemoHelper.getInstance().getNotifier().sendNotification(msgNotification, false);
//                    Contract contract = new Contract();
//                    contract.setAccepted(true);
//                    contract.setNname(Global.getNickByAll(s).getNname());
//                    ContractManager.list.add(contract);
//                    ContractManager.adapter.notifyDataSetChanged();
                    L.e("好友接受" + s);
                }

                @Override
                public void onFriendRequestDeclined(String s) {
                    L.e("好友拒绝" + s);

                }
            };
        }
//        if (conversationListener == null) {
//            conversationListener = new EMConversationListener() {
//                @Override
//                public void onCoversationUpdate() {
//                    L.e("onCoversationUpdate__onCoversationUpdate");
//                }
//            };
//        }
//        EaseUserUtils.setUserNick();
        //添加监听
        try{
            EMClient.getInstance().chatManager().addMessageListener(messageListener);
            EMClient.getInstance().contactManager().setContactListener(emContactListener);
        }catch(Exception e){
            e.printStackTrace();
        }

        //        EMClient.getInstance().chatManager().addConversationListener(conversationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastView.getId() != R.id.tab_post_icon && HttpCore.isLogin) {
            onClick(lastView);
        } else if (lastView.getId() != R.id.tab_post_icon && !HttpCore.isLogin) {
//            switchFragment(yfx);
        }

        if (currentFragment instanceof Fragment_home && yfx != null) {
            Log.e("onResume", "resume");
            yfx.refreshSwitch();
        }
        if (HttpCore.isLogin) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshUIWithMessage(); //更新会话 和未读。
                    sendFriendTransactionNotify();// 是否有好友请求
                }
            }).start();
        }

//        updateUnreadLabel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MyToast.mToast != null) {
            MyToast.mToast.cancel();
            MyToast.mToast = null;
        }
//        EMClient.getInstance().chatManager().removeConversationListener(conversationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除会话监听
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().contactManager().removeContactListener(emContactListener);

    }

    public void sendFriendTransactionNotify() {
        HttpCore.getFriendRequests(new IListResultHandler() {
            @Override
            public void onSuccess(ListResult rs) {
                if (rs.getSuccess()) {
                    if (rs.getBiz().size() > 0) {
                        EMMessage msgNotification = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                        EMTextMessageBody txtBody = new EMTextMessageBody("你有新的好友请求待处理");
                        msgNotification.addBody(txtBody);
                        msgNotification.setUnread(true);
                        msgNotification.setAttribute("contract", "notadd");
                        msgNotification.setAttribute("num", rs.getBiz().size() + "");
                        msgNotification.setMsgTime(System.currentTimeMillis());
                        msgNotification.setLocalTime(System.currentTimeMillis());
                        msgNotification.setChatType(EMMessage.ChatType.Chat);
//                    msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                        msgNotification.setStatus(EMMessage.Status.SUCCESS);
                        DemoHelper.getInstance().getNotifier().sendNotification(msgNotification, false);
                    }
                }
            }
        });
    }
    /**
     * get unread message count
     *
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    protected void initView() {
        super.initView();
        getBanner();
        common.changeTitle(MainActivity.this, "e链网");
        iv = new ImageView[]{iv1, iv2, iv4, iv5};
        tv = new TextView[]{tv1, tv2, tv4, tv5};
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);

        initHomeFragment();
        initFragment();
        lastView = rl1; // 默认上次点击首页
    }


    private void initHomeFragment() {
        yfx = new Fragment_home();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_content, yfx);
        transaction.commit();
    }

    private void initFragment() {
        home = new Fragment_yfx();
        yxq = new Fragment_yxq();
        my = new Fragment_my();
        currentFragment = yfx;
    }

    public void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        Log.e("switchFragment", "");
        if (!targetFragment.isAdded()) {
            Log.e("now_addfragment", "");
            transaction.hide(currentFragment)
                    .add(R.id.fl_content, targetFragment)
                    .commit();
        } else {
            if (currentFragment == targetFragment) {
                return;
            }
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();

        }
        currentFragment = targetFragment;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 0:
                    common.getToolView(MainActivity.this, R.id.tv_right).setVisibility(View.GONE);
                    common.getToolView(MainActivity.this, R.id.iv_right).setVisibility(View.GONE);
                    common.changeTitle(MainActivity.this, "e链");
                    break;

                case 1:
                    common.changeTitle(MainActivity.this, "医械圈");
                    common.getToolView(MainActivity.this, R.id.tv_right).setVisibility(View.GONE);
                    common.getToolView(MainActivity.this, R.id.iv_right).setVisibility(View.VISIBLE);
                    break;

                case 2:
                    common.getToolView(MainActivity.this, R.id.tv_right).setVisibility(View.GONE);
                    common.getToolView(MainActivity.this, R.id.iv_right).setVisibility(View.GONE);
                    common.changeTitle(MainActivity.this, "e发现");
                    break;

                case 3:
                    common.getToolView(MainActivity.this, R.id.tv_right).setVisibility(View.VISIBLE);
                    common.getToolView(MainActivity.this, R.id.iv_right).setVisibility(View.GONE);
                    ((TextView) common.getToolView(MainActivity.this, R.id.tv_right)).setText("退 出");
                    common.getToolView(MainActivity.this, R.id.tv_right).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            logout();
                        }
                    });
                    common.changeTitle(MainActivity.this, "我的");
                    break;
                case 5:

                    showMoreWindow(iv3);
                    break;
                case 6:
                    // refresh unread count 未读
                    updateUnreadLabel();

                    // refresh conversation list 会话
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                    //聊天
                    if (ChatActivity.chatFragment != null) {
                        ChatActivity.getMessageList().refresh();
                    }
                    break;
            }

            int index = msg.what;
            if (index < 4) {
                for (int i = 0; i < iv.length; i++) {
                    if (i == index) {
                        iv[index].setImageResource(imgsTab[index]);
                        tv[index].setTextColor(Color.parseColor("#4ea4dc"));
                    } else {
                        iv[i].setImageResource(imgsNormal[i]);
                        tv[i].setTextColor(Color.parseColor("#4d4d4d"));
                    }
                }
            }

        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                EAPPApplication.getInstance().exit();
            } else {
                MyToast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
//        Log.e("position", index + "'");
        lastView = view;
        Message msg = handler.obtainMessage();
        switch (view.getId()) {
            case R.id.rl1:
                msg.what = 0;
                switchFragment(yfx);
                break;
            case R.id.rl2:
                msg.what = 1;
//                switchFragment(yxq);
                checkIsLogin(2);
                break;
            case R.id.tab_post_icon:
                msg.what = 5;
//                switchFragment(home);
                break;
            case R.id.rl4:
                msg.what = 2;
                checkIsLogin(4);
                break;
            case R.id.rl5:
                msg.what = 3;
                checkIsLogin(5);
                break;
            default:
                break;
        }
        L.e(msg.what + "");
        handler.sendMessage(msg);
    }

    private void showMoreWindow(View view) {

        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init();
        }

        mMoreWindow.showMoreWindow(view, 100);
    }

    public void show1(View view) {
        checkIsLogin(11);
    }

    public void show2(View view) {
        checkIsLogin(12);
    }

    public void show3(View view) {
        checkIsLogin(13);

    }

    private void getBanner() {
        //获取banner 数据
        HttpCore.index(new IListResultHandler<Advert>() {
            @Override
            public void onSuccess(ListResult<Advert> rs) {
                if (rs.getSuccess()) {
                    for (Advert ad : rs.getBiz()) {
                        bannerList.add(Url.WEBPATH + ad.getImg());
                        L.e("img:" + Url.WEBPATH + ad.getImg());
                    }
                }
            }
        });
    }

    public void checkIsLogin(final int i) {

        final String phone = common.SP_Read(this, "phone");
        final String p = common.SP_Read(this, "p");
        if (HttpCore.isLogin) {
//            dialog.showInfo("加载中...");
            skipTo(i);
        } else {

            if (NetUtil.getNetWorkState(this) == NetUtil.NETWORK_NONE) {
                MyToast.makeText(this, "请检查网络连接", 1000).show();
                launchActivity(LoginActivity.class);
                return;
            }

            if (!"".equals(p) && !"".equals(phone)) {
                HttpCore.Login(phone, "", p, new IResultHandler<UserInfo>() {
                    @Override
                    public void onSuccess(Result<UserInfo> rs) {
                        if (!rs.getSuccess()) {
                            launchActivity(LoginActivity.class);
                        } else {
                            skipTo(i);
                        }
                    }
                });
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launchActivity(LoginActivity.class);
                    }
                }, 500);
            }
        }
    }

    private void skipTo(int i) {
        if (i == 2) {
            switchFragment(yxq);
        } else if (i == 4) {
            switchFragment(home);
        } else if (i == 5) {
            switchFragment(my);
        } else if (i == 11) {
            Intent intent = new Intent(MainActivity.this, NeedPublish.class);
            intent.putExtra("type", 0);
            launchActivityWithIntent(intent);
            mMoreWindow.dismiss();
        } else if (i == 12) {
            Intent intent = new Intent(MainActivity.this, NeedPublish.class);
            intent.putExtra("type", 1);
            launchActivityWithIntent(intent);
            mMoreWindow.dismiss();
        } else if (i == 13) {
            launchActivity(ProductPublish.class);
            mMoreWindow.dismiss();
        }
        dialog.dismiss();
    }

    private void refreshUIWithMessage() {
        handler.sendEmptyMessage(6);
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    // 商机
    public void getDemandById(final String param) {
        dialog.showInfo("加载中...");
        HttpCore.getDemandById(param, new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
                dialog.dismiss();
                if (rs.getSuccess()) {
                    launchActivityWithIntent(new Intent(MainActivity.this, MyNeedDetail.class).putExtra("item", (Demand) rs.getBiz()));
                } else {  //超时
                    L.e("failedto_getDemandById" + rs.getMsg());
                    AutoLogin(new IResultHandler() {
                        @Override
                        public void onSuccess(Result rs) {
                            if (rs.getSuccess()) {
                                HttpCore.getDemandById(param, new IResultHandler() {
                                    @Override
                                    public void onSuccess(Result rs) {
                                        dialog.dismiss();
                                        if (rs.getSuccess()) {
                                            launchActivityWithIntent(new Intent(MainActivity.this, MyNeedDetail.class).putExtra("item", (Demand) rs.getBiz()));
                                        } else {
                                            dialog.showWarn("登录超时，请重新登录！");
                                            launchActivity(LoginActivity.class);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    //招商
    public void getMerchantById(String param) {
        dialog.showInfo("加载中...");
        HttpCore.getProductDetail(param, new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
                dialog.dismiss();
                launchActivityWithIntent(new Intent(MainActivity.this, MyProductDtail.class).putExtra("item", (Product) rs.getBiz()));
            }
        });
    }

    //news
    public void getNewsById(String param) {
        dialog.showInfo("加载中...");
        launchActivityWithIntent(new Intent(MainActivity.this, ENewsDetail.class).putExtra("param", param));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putSerializable("la", (Serializable) lastView);
    }

    public void getPermissions() {
        // 缺少权限时, 进入权限配置页面
        if (Build.VERSION.SDK_INT >= 23) {
            int checkLocalPhonePermission = ActivityCompat.checkSelfPermission(this, "android.permission.CHANGE_WIFI_STATE");
            if (checkLocalPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.CHANGE_WIFI_STATE", "android.permission.WRITE_SETTINGS"}, 101);
            }
            return;
        }
        // 缺少权限时, 进入权限配置页面
        if (Build.VERSION.SDK_INT >= 23) {
            int checkLocalPhonePermission = ActivityCompat.checkSelfPermission(this, "android.permission.CHANGE_WIFI_STATE");
            if (checkLocalPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.CHANGE_WIFI_STATE", "android.permission.WRITE_SETTINGS"}, 101);
                return;
            }
            //适配小米机型
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, Process.myUid(), getPackageName());
            if (checkOp == AppOpsManager.MODE_IGNORED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.CHANGE_WIFI_STATE", "android.permission.WRITE_SETTINGS"}, 101);
                return;
            }
        }
    }


    protected void checkPermission() {
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            int checkPermissionResult = this.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION");
            if (checkPermissionResult != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 101);
                return;
            } else {
                //获取到权限

            }
        } else {

            //获取到权限
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CHANGE_NETWORK_STATE) || !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_SETTINGS)) {
                    AskForPermission();
                }
            }
        }
    }

    private void AskForPermission() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
        startActivity(intent);

    }

}
