package com.hyhscm.myron.eapp.activity.Im;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.data.Contract;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyphenate.easeui.widget.EaseTitleBar;

import butterknife.BindView;

/**
 * Created by Myron on 2018/1/16.
 */
public class ContractList extends ListBaseBeanActivity<Contract> {
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    public static int UndoFriendRequests = 0; //未处理好友请求数

    public ContractList() {
        super(R.layout.layout_contact);
    }

    RelativeLayout rl_new;
    public static TextView tv_undoFriend;
    @Override
    protected void initView() {
        super.initView();
        initListView(R.layout.item_contact);
        View header = LayoutInflater.from(this).inflate(R.layout.head_contract, (ViewGroup) this.findViewById(android.R.id.content), false);
        rl_new = (RelativeLayout) header.findViewById(R.id.rl_new);
        tv_undoFriend = (TextView) header.findViewById(R.id.undofriends);


        rl_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(ContractManager.class);
            }
        });
        getmRecyclerViewAdapter().addHeaderView(header);

        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContractList.this.finish();
            }
        });
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContractList.this, UserSearch.class);
                startActivity(intent);
            }
        });
        titleBar.setTitle("联系人");
    }

    @Override
    protected void initData() {
        super.initData(Url.CONTACT, 50);
    }

    @Override
    protected void bindView(BaseViewHolder holder, Contract item) {
//        super.bindView(holder, item);
        holder.setText(R.id.name, item.getNname());
        holder.setRoundImageView(R.id.iv, Url.IMAGEPATH + item.getPic());
    }

    @Override
    protected void itemClick(View v, int i, Contract item) {
//        super.itemClick(v, i, item);

        if (HttpCore.uid.equals(item.getUiid())) {
            MyToast.makeText(ContractList.this, "自言自语不太好吧~-~", 1500).show();
            return;
        }
        Intent intent = new Intent(this, FriendDetail.class);
        intent.putExtra("item",item);
        startActivity(intent);
//
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra(Constant.EXTRA_USER_ID, item.getUiid());
//        startActivity(intent);
    }
// refresh  contractList
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void loadData() {
        super.loadData();
        updateFriendRequest();
    }

    /**
     * update the friends request num
     */
    public static void updateFriendRequest(){
        HttpCore.getFriendRequests(new IListResultHandler() {
            @Override
            public void onSuccess(ListResult rs) {
                if(rs.getSuccess()){
                    UndoFriendRequests = rs.getBiz().size();
                    if(UndoFriendRequests!=0){
                        if (tv_undoFriend!=null) {
                            tv_undoFriend.setVisibility(View.VISIBLE);
                            tv_undoFriend.setText(UndoFriendRequests+"");
                        }
                    }else{
                        if(tv_undoFriend!=null){
                            tv_undoFriend.setVisibility(View.INVISIBLE);
                        }
                    }
                    Log.e("UndoFriendRequests",UndoFriendRequests+"");
                }
            }
        });
    }
//    EaseContactListFragment contactListFragment;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content);
//
//
////直接启动联系人列表
//        contactListFragment = new EaseContactListFragment();
////需要设置联系人列表才能启动fragment
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                contactListFragment.setContactsMap(getContacts());
//                contactListFragment.refresh();
//            }
//        }).start();
////设置item点击事件
//        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
//
//            @Override
//            public void onListItemClicked(EaseUser user) {
//                startActivity(new Intent(ContractList.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
//            }
//        });
//        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
//            @Override
//            public void onContactAdded(String s) {
//                Log.e("TalkActivity", s);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        contactListFragment.setContactsMap(getContacts());
//                        contactListFragment.refresh();
//                    }
//                }).start();
//            }
//
//            @Override
//            public void onContactDeleted(String s) {
//
//            }
//
//            @Override
//            public void onContactInvited(String s, String s1) {
//
//            }
//
//            @Override
//            public void onFriendRequestAccepted(String s) {
//
//            }
//
//            @Override
//            public void onFriendRequestDeclined(String s) {
//
//            }
//        });
//
//        getSupportFragmentManager().beginTransaction().add(R.id.content, contactListFragment).commit();
//
//    }


    /**
     * 获取联系人
     *
     * @return
     */
//    private Map<String, EaseUser> getContacts() {
//        Map<String, EaseUser> map = new HashMap<>();
//        try {
//            List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//            L.e("......有几个好友:" + userNames.size());
//            for (String userId : userNames) {
//                L.e("好友列表中有 : " + userId);
//                map.put(userId, new EaseUser(userId));
//            }
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }
//        return map;
//    }

}