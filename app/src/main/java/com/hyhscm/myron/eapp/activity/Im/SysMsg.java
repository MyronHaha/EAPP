package com.hyhscm.myron.eapp.activity.Im;

import android.content.Intent;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.Home.ENewsDetail;
import com.hyhscm.myron.eapp.activity.Home.ProductDetail;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.User.MyNeedDetail;
import com.hyhscm.myron.eapp.activity.User.MyProductDtail;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.Goods;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.SMsg;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;

import butterknife.BindView;



public class SysMsg extends ListBaseBeanActivity<SMsg>{
  @BindView(R.id.rv)
  LRecyclerView rv;
 private String cid;
   public SysMsg(){
        super(R.layout.layout_sysmsg);
    }
   protected void initView() {
       super.initView();
        common.getToolView(this,R.id.tv_right).setVisibility(View.GONE);
         changeTitle("系统消息");
       initListView(R.layout.item_sysmsg_small);
    }

    protected void initData() {
        super.initData();
        cid = getIntent().getStringExtra("cid");
        params.put("cid",cid);
        initData(Url.SYSMSGLIST, 10);
    }

    @Override
    protected void bindView(BaseViewHolder holder, SMsg item) {

//        if (item.getBtype()==3||item.getBtype()==4) {
//
//            holder.getView(R.id.rl_small).setVisibility(View.GONE);
//            holder.getView(R.id.rl_big).setVisibility(View.VISIBLE);
//            holder.setText(R.id.content,item.getContent());    //content
//        }
      if(holder instanceof BaseQuickAdapter.MsgView1) {
          BaseQuickAdapter.MsgView1 hold = (BaseQuickAdapter.MsgView1) holder;
          hold.title.setText(item.getName());
          hold.content.setText( item.getContent());
          hold.date.setText(common.dataToString(item.getCreationTime()));
          if(item.getImg().equals("")){
              holder.getView(R.id.iv).setVisibility(View.GONE);
          }else{
              holder.setImage(R.id.iv,Url.IMAGEPATH+item.getImg()); //img
          }
      }else{
          BaseQuickAdapter.MsgView2 hold2 = (BaseQuickAdapter.MsgView2) holder;
          hold2.title.setText(item.getName());
          hold2.date.setText(common.dataToString(item.getCreationTime()));
          if(item.getImg().equals("")){
              holder.getView(R.id.iv).setVisibility(View.GONE);
          }else{
              holder.setImage(R.id.iv,Url.IMAGEPATH+item.getImg()); //img
          }
      }

    }

    @Override
    protected void itemClick(View v, int i, SMsg item) {
        switch(item.getBtype()){
            case 0:
                break;
            case 1:
                break;
            case 2://资讯
                getNewsById(item.getParam());
                break;
            case 3://需求
                getDemandById(item.getParam());
                break;
            case 4://招商
                getMerchantById(item.getParam());
                break;
            case 5:
                break;
            case 6:
                break;
            case 7://产品
                getProductById(item.getParam());
                break;
            case 8:
                getNewsById(item.getParam());
                break;
        }
    }

    public void getDemandById(String param){
        dialog.showInfo("加载中...");
        HttpCore.getDemandById(param, new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
            dialog.dismiss();
                 launchActivityWithIntent(new Intent(SysMsg.this, MyNeedDetail.class).putExtra("item",(Demand)rs.getBiz()));
            }
        });
    }
//产品
    public void getProductById(String param){
        dialog.showInfo("加载中...");
        HttpCore.getProductById(param, new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
                dialog.dismiss();
                launchActivityWithIntent(new Intent(SysMsg.this, ProductDetail.class).putExtra("item",(Goods)rs.getBiz()));
            }
        });
    }
//招商
    public void getMerchantById(String param){
        dialog.showInfo("加载中...");
        HttpCore.getProductDetail(param, new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
                dialog.dismiss();
                launchActivityWithIntent(new Intent(SysMsg.this, MyProductDtail.class).putExtra("item",(Product)rs.getBiz()));
            }
        });
    }
    //news
    public void getNewsById(String param){
        dialog.showInfo("加载中...");
        launchActivityWithIntent(new Intent(SysMsg.this, ENewsDetail.class).putExtra("param",param));
    }

}
