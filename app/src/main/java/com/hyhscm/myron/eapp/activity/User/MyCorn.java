package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.design.MaterialDialog;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.MyWeb;
import com.hyhscm.myron.eapp.adapter.MyCornAdapter;
import com.hyhscm.myron.eapp.adapter.MyneedAdapter;
import com.hyhscm.myron.eapp.data.BaseResult;
import com.hyhscm.myron.eapp.data.GoldBean;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.SignResult;
import com.hyhscm.myron.eapp.data.UserGoldBean;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.mph.okdroid.response.RawResHandler;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/12/6.
 */

public class MyCorn extends ListBaseBeanActivity<GoldBean> {
    @BindView(R.id.rv)
    LRecyclerView rv;
//
//    @BindView(R.id.bt_sign)
//    Button bt_sign;
    @BindView(R.id.tv_total)
    TextView total;
    public MyCorn(){
        super(R.layout.layout_corn);
    }
    protected void initView() {
        changeTitle("我的金豆");
        ((TextView)getToolView(R.id.tv_right)).setText("关于e金豆");
        initListView(R.layout.item_hebi2);
    }

    protected void initData() {
        super.initData(Url.HEBILIST,10);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        // 查询签到
//        HttpCore.getSignInfo(new RawResHandler() {
//            @Override
//            public void onFailed(int i, String s) {
//
//            }
//            @Override
//            public void onSuccess(int i, String s) {
//                try{
//                    JSONObject object = new JSONObject(s) ;
//                    JSONObject item = new JSONObject(object.getString("biz"));
//                    if(object.getBoolean("success")){
//                        if( item.getString("hasSign") .equals("1")){
//                            bt_sign.setText("已签到");
//                            bt_sign.setBackgroundResource(R.drawable.unsolid_round_primary);
//                            bt_sign.setTextColor(Color.parseColor("#3db0ff"));
//                        }else{
//                            bt_sign.setText("签到");
//                            bt_sign.setBackgroundResource(R.drawable.unsolid_round_pink);
//                            bt_sign.setTextColor(Color.parseColor("#ff6aad"));
//                        }
//                    }
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
        // 查询用户金豆
        HttpCore.getUserGBean(new IResultHandler<UserGoldBean>() {
            @Override
            public void onSuccess(Result<UserGoldBean> rs) {
                if (rs.getSuccess()) {
                    total.setText(rs.getBiz().getCredit()+"");
                }
            }
        });
    }

    //    private void initRv() {
//        common.createData(data);
//        MyCornAdapter adapter = new MyCornAdapter(this,data);
//         lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
//        rv.setLayoutManager(new LinearLayoutManager(this));
//        rv.setAdapter(lRecyclerViewAdapter);
//        rv.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        rv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        rv.setLoadMoreEnabled(true);
//        rv.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });
//        rv.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        data.addAll(data);
////                        rv.setNoMore(true);
//                        lRecyclerViewAdapter.notifyDataSetChanged();
//                        rv.refreshComplete(10);
//                    }
//                },100);
//            }
//        });
//    }
//
//    private void refresh() {
//        pageCount = 1;
//        data.clear();
//        common.createData(data);
//        lRecyclerViewAdapter.notifyDataSetChanged();
//        rv.refreshComplete(10);
//    }


//    private void changeTitleUi(TextView view,boolean click){
//        Drawable drawable = null;
//        int color ;
//        if(click){
//             drawable= getResources().getDrawable(R.drawable.shape_indicator);
//             color = getResources().getColor(R.color.colorPrimary);
//        }else{
//            drawable= getResources().getDrawable(R.drawable.shape_indicator_null);
//            color = getResources().getColor(R.color.tx_primary);
//        }
///// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        view.setCompoundDrawables(null,null,null,drawable);
//        view.setTextColor(color);
//    }
//
//    @OnClick(R.id.vp1)
//    public void changeTitle1(){
//        changeTitleUi(view1,true);
//        changeTitleUi(view2,false);
//        if(rv.getVisibility()==View.GONE){
//            rv.setVisibility(View.VISIBLE);
//            rv.forceToRefresh();
//        }
//        if(view.getVisibility() == View.VISIBLE){
//            view.setVisibility(View.GONE);
//        }
//    }
//    @OnClick(R.id.vp2)
//    public void changeTitle2(){
//        changeTitleUi(view2,true);
//        changeTitleUi(view1,false);
//        if(rv.getVisibility()==View.VISIBLE){
//            rv.setVisibility(View.GONE);
//        }
//        if(view.getVisibility() == View.GONE){
//            view.setVisibility(View.VISIBLE);
//        }
//
//    }

    @OnClick(R.id.tv_right)
    public void aboutGoldBean(){
        Intent intent = new Intent(this, MyWeb.class);
        intent.putExtra("url",Url.ABOUT_GOLDBEAN);
        launchActivityWithIntent(intent);
    }
//    @OnClick(R.id.bt_sign)
//    public void sign(){
//        if (!bt_sign.getText().equals("已签到")) {
//            HttpCore.Sign(new IResultHandler<SignResult>() {
//                @Override
//                public void onSuccess(Result<SignResult> rs) {
//                    if (rs.getSuccess()) {
//                        String getCorn =   rs.getBiz().getQty(); // 获得金币
//                        bt_sign.setText("已签到");
//                        bt_sign.setBackgroundResource(R.drawable.selector_round_grey27);
//                        showSignResult(getLayoutInflater().inflate(R.layout.tips_signforbean,null),R.id.tv_signresult,"获得"+getCorn+"颗e金豆");
//                    }
//                }
//            });
//        }else{
//            MyToast.makeText(_mActivity,"今天已签到过了，明天继续吧！",1500).show();
//        }
//        }
//  @OnClick(R.id.bt_vip)
//    public void getvip(){
//      MyToast.makeText(_mActivity,"敬请期待！",1500).show();
//
//}

    @Override
    protected void bindView(BaseViewHolder holder, GoldBean item) {
        super.bindView(holder, item);
        if(item!=null){
//            holder.setText(R.id.time,item.getRealtime());
            ((TextView)holder.getView(R.id.time)).setText(common.dataToStringDetail(item.getRealTime()));
            ((TextView)holder.getView(R.id.title)).setText(item.getSourceName());

//            holder.setText(R.id.title,item.getSourcename());
            TextView count = (TextView) holder.getView(R.id.count);
            if(item.getCredit()>0){
                  count.setTextColor(Color.parseColor("#00FF00"));
                  count.setText(String.format("+ %d",item.getCredit()));
            }else{
                 count.setTextColor(Color.parseColor("#EE4000"));
                 count.setText(String.format("- %d",-item.getCredit()));
            }

        }
    }
}
