package com.hyhscm.myron.eapp.activity.Im;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;

import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContactManager;
import com.hyphenate.easeui.model.Contract;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;


import butterknife.BindView;

/**
 * Created by Myron on 2018/1/16.
 */
public class ContractManager extends ListBaseBeanActivity<Contract> {

    public static List list = new ArrayList();
    public static BaseQuickAdapter<Contract> adapter;
    public ContractManager() {
        super(R.layout.layout_contract_manager);
    }

    @Override
    protected void initView() {
        super.initView();
        initListView(R.layout.item_contract_manager);
        changeTitle("新的好友");
//        initRv();
    }

    protected void initData() {
        super.initData();
        initData(Url.FRIEND_REQUEST, 10);
    }
//   protected void initView() {
//        super.initView();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        adapter = new BaseQuickAdapter<Contract>(ContractManager.this, "", R.layout.item_contract_manager, list) {
//            @Override
//            protected void convert(BaseViewHolder holder, final Contract item) {
//                ((TextView)holder.getView(R.id.name)).setText(item.getNname());
//                ((TextView)holder.getView(R.id.remarks)).setText(item.getCname());
//                if (item.getAccepted()) {
//                    ((TextView) holder.getView(R.id.tv_state)).setText("已接受");
//                    ((TextView) holder.getView(R.id.tv_state)).setBackgroundResource(R.drawable.shape_tag_grey);
//                } else {
//                    ((TextView) holder.getView(R.id.tv_state)).setText("接受");
//                    ((TextView) holder.getView(R.id.tv_state)).setBackgroundResource(R.drawable.shape_tag_blue);
//                }
//
//                if (!item.getAccepted()) {
//                    holder.getView(R.id.tv_state).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //同意username的好友请求
//
//
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        EMClient.getInstance().contactManager().acceptInvitation(item.getUiid()); //异步
//                                        item.setAccepted(true);
//                                        handler.sendEmptyMessage(100);
//                                    } catch (HyphenateException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }).start();
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onBindViewHolder(BaseViewHolder holder, int position) {
//                super.onBindViewHolder(holder, position);
//            }
//        };
//        recyclerView.setAdapter(adapter);
//    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {

                adapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void bindView(BaseViewHolder holder, final Contract item) {
        super.bindView(holder, item);
        if (item!=null) {
            ((TextView)holder.getView(R.id.name)).setText(item.getNname());
            ((TextView)holder.getView(R.id.remarks)).setText(item.getSourcename());
            holder.setRoundImageView(R.id.iv,Url.IMAGEPATH+item.getPic());
            L.e(item.getId()+"idididididididid");
          //接受
            ((TextView)holder.getView(R.id.tv_state)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpCore.doRequest(item.getId()+"", "1", new IResultHandler<Contract>() {
                        @Override
                        public void onSuccess(Result<Contract> rs) {
                            if(rs.getSuccess())
                            {
                                //参数为要添加的好友的username和添加理由
                                //同意username的好友请求
                                try {
                                    EMClient.getInstance().contactManager().acceptInvitation(item.getUiid());//需异步处理
                                    common.promptDialog.showSuccess("添加成功");
                                    loadData();
                                    ContractList.updateFriendRequest();
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });
            //拒绝
            ((TextView)holder.getView(R.id.tv_state2)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpCore.doRequest(item.getId()+"", "0", new IResultHandler<Contract>() {
                        @Override
                        public void onSuccess(Result<Contract> rs) {
                            if(rs.getSuccess())
                            {
                                try {
                                    EMClient.getInstance().contactManager().declineInvitation(item.getUiid());
                                    common.promptDialog.showSuccess("已拒绝");
                                    loadData();
                                    ContractList.updateFriendRequest();
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void itemClick(View holder, int i, Contract item) {

    }
}