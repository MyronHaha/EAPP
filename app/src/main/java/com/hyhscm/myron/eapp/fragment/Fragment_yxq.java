package com.hyhscm.myron.eapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Im.ChatActivity;
import com.hyhscm.myron.eapp.activity.Im.ContractList;
import com.hyhscm.myron.eapp.activity.Im.ConversationList;
import com.hyhscm.myron.eapp.activity.Im.DemoHelper;
import com.hyhscm.myron.eapp.activity.Im.SysMsg;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.adapter.SingleItemAdapter;
import com.hyhscm.myron.eapp.data.Contract;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/11/20.
 */

public class Fragment_yxq extends Fragment {
    public static ConversationList conversationListFragment;
    private ProgressDialog dialog;
    private ImageView iv_right;
    public static List<Contract> contractList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initView();
//        initContractsList();

//        if (!DemoHelper.getInstance().isLoggedIn()) {
//            //环信登录
//            EMClient.getInstance().login(HttpCore.uid, HttpCore.pushKey, new EMCallBack() {
//
//                @Override
//                public void onSuccess() {
//                    L.e("环信登录成功！");
//                    EMClient.getInstance().groupManager().loadAllGroups();
//                    EMClient.getInstance().chatManager().loadAllConversations();
//                }
//
//                @Override
//                public void onProgress(int progress, String status) {
//                }
//
//                @Override
//                public void onError(int code, final String error) {
//                    L.e("环信登录失败！" + error);
//                }
//            });
//        }
        dialog = new ProgressDialog(getActivity());
        iv_right = (ImageView) getActivity().findViewById(R.id.iv_right);
        iv_right.setImageResource(R.mipmap.icon_txl);
        getActivity().findViewById(R.id.fl_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ContractList.class));
            }
        });
        conversationListFragment = new ConversationList();//cid conversationid  btype  mid,img,name,
        //加载会话
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content, conversationListFragment).commit();
    }







//    public static List mapTransitionList(Map map) {
//        List list = new ArrayList();
//        Iterator iter = map.entrySet().iterator(); // 获得map的Iterator
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            list.add(entry.getKey());
//            list.add(entry.getValue());
//        }
//        return list;
//    }
}
