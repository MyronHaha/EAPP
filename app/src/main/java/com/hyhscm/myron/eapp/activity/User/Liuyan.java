package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseBeanActivity;
import com.hyhscm.myron.eapp.adapter.MyneedAdapter;
import com.hyhscm.myron.eapp.data.Comment;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.JsonBean;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;



/**
 * Created by Jason on 2017/12/1.
 */

public class Liuyan extends BaseBeanActivity<Comment> {
    @BindView(R.id.et_liuyan)
    EditText et;
    @BindView(R.id.tv_cancel)
    TextView tv_cancle;
    @BindView(R.id.tv_sure)
    TextView tv_submit;
    private String from = "";
    public Liuyan() {
        super(R.layout.layout_liuyan);
    }
    protected void initView(){
        if(getIntent().getStringExtra("from").equals("product")){
            from = "product";
        }else{
            from = "need";
        }
    }
    @OnClick(R.id.tv_cancel)
    public void cancel() {
        this.finish();
    }

    @OnClick(R.id.tv_sure)
    public void submit() {
           if(et.getText().toString().equals("")){
               MyToast.makeText(Liuyan.this,"留言不能为空！", Toast.LENGTH_SHORT).show();
               return;
           }

           if(from.equals("need")){
               HttpCore.commentAdd(getIntent().getStringExtra("demand_id") + "", et.getText().toString(), new IResultHandler<Comment>() {
                   @Override
                   public void onSuccess(Result<Comment> rs) {
                       try{
                           if(rs.getSuccess()){
                               new  PromptDialog(Liuyan.this).showSuccess("留言成功！");
                               Intent intent = new Intent();
                               Liuyan.this.setResult(100, intent);
                               Liuyan.this.finish();
                           }
                       }catch (Exception e){
                           new  PromptDialog(Liuyan.this).showError("留言失败！");
                       }
                   }
               });
           }else if(from.equals("product")){
               HttpCore.pcommentAdd(getIntent().getStringExtra("product_id") + "", et.getText().toString(), new IResultHandler<Comment>() {
                   @Override
                   public void onSuccess(Result<Comment> rs) {
                       try{
                           if(rs.getSuccess()){
                               new  PromptDialog(Liuyan.this).showSuccess("留言成功！");
                               Intent intent = new Intent();
                               Liuyan.this.setResult(101, intent);
                               Liuyan.this.finish();
                           }
                       }catch (Exception e){
                           new  PromptDialog(Liuyan.this).showError("留言失败！");
                       }
                   }
               });
           }


    }

}