package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.JsonBean;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/12/5.
 * city pic single  result code 102
 * city pic mutil                 200
 */

public class ChooseSecond extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    private BaseQuickAdapter<Areas> adapter;
    public static String str_city = "广州";
    private List<Integer> seletedArr = new ArrayList();
    private List<Areas> data = new ArrayList();
    private List<Areas> mulsel_citys = new ArrayList();
    public ChooseSecond(){
        super(R.layout.layout_choose);
    }


    protected void initView() {
        super.initView();
        goBack();
        changeTitle( ChooseFirst.str_province);
        //代理的城市
        if (getIntent().getBooleanExtra("multipleChoose", false)) {
            ((TextView) getToolView(R.id.tv_right)).setText("保存");
            ((TextView) getToolView(R.id.tv_right)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent result = new Intent();
                    result.putExtra("result", (Serializable) mulsel_citys);
                    ChooseSecond.this.setResult(200, result);
                    ChooseSecond.this.finish();
                }
            });
        }
//         else   if (getIntent().getBooleanExtra("multipleChoose_hos", false)){
//
//        }
        else{
            hideObjs(new int[]{R.id.tv_right});
        }
        initRv();
    }

    private void initRv() {
        //多选  我代理的城市
        if (getIntent().getBooleanExtra("multipleChoose", false)) {
            data.clear();
            data.addAll(ChooseFirst.childList);
            adapter = new BaseQuickAdapter<Areas>(ChooseSecond.this, "", R.layout.item_single, data) {

                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    TextView tv = (TextView) holder.getView(R.id.tv);
                    tv.setText(item.getName());

                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                    if (isSelected(position)) {
                        CheckBox cb = (CheckBox) base.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_uncheck_bg);
                        mulsel_citys.remove(data.get(position));
                        seletedArr.remove((Object) position);
                    } else {
                        CheckBox cb = (CheckBox) base.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_check_bg);
                        mulsel_citys.add(data.get(position));
                        seletedArr.add(position);
                    }
                }
            });
        } else if(getIntent().getBooleanExtra("multipleChoose_hos", false)){
            //医院
            adapter = new BaseQuickAdapter<Areas>(ChooseSecond.this, "", R.layout.item_province, ChooseFirst.childList) {

                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    TextView tv = (TextView) holder.getView(R.id.tv);
                    tv.setText(item.getName());
                    holder.getView(R.id.iv_right).setVisibility(View.GONE);
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                 int pid    = ChooseFirst.childList.get(position).getId();
                    Intent intent = new Intent(ChooseSecond.this,ChooseThird.class);
                    intent.putExtra("cityid", pid);
                    intent.putExtra("type","选择代理的医院");
                    intent.putExtra("multipleChoose",false);
                    launchActivityWithIntent(intent,120);

                }
            });
        }
        else {
            //省市
            adapter = new BaseQuickAdapter<Areas>(ChooseSecond.this, "", R.layout.item_province, ChooseFirst.childList) {

                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    TextView tv = (TextView) holder.getView(R.id.tv);
                    tv.setText(item.getName());
                    holder.getView(R.id.iv_right).setVisibility(View.GONE);
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                    str_city = ChooseFirst.childList.get(position).getName();
                    Intent intent = new Intent();
                    intent.putExtra("province", ChooseFirst.str_province);
                    intent.putExtra("city", str_city);
                    intent.putExtra("city_id",ChooseFirst.childList.get(position).getId());
                    ChooseSecond.this.setResult(102, intent);
                    ChooseSecond.this.finish();
                }
            });
        }


        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }
    // 判断多选；

    public boolean isSelected(int find) {
        if (seletedArr.contains(find)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(requestCode==120&&resultCode==120){
             this.setResult(120,data);
             this.finish();
         }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
