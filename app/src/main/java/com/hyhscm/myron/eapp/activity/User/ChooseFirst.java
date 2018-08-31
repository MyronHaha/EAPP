package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.JsonBean;
import com.hyhscm.myron.eapp.data.Labelinfo;
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
 * city pic
 */
/*
* resultcode 111 常驻
* 112 科室*/
public class ChooseFirst extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    private ArrayList<Areas> data_pro = new ArrayList<>();
    private ArrayList<Areas> data_city = new ArrayList<>();
    private BaseQuickAdapter adapter;
    public static String str_province = "广东";
    public static List<Areas> childList = new ArrayList<>();

    public ChooseFirst() {
        super(R.layout.layout_choose);
    }

    List<Integer> seletedArr = new ArrayList();
    String type = "";
    String title = "";
    //keshi
    List<String> roomSel = new ArrayList<String>();
    List<Labelinfo> room = new ArrayList<Labelinfo>();

    //代理省份
    List<Areas> proData = new ArrayList<>();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_choose);
//        ButterKnife.bind(this);
//
//        initView();
//
//    }

    protected void initView() {
        super.initView();
        type = getIntent().getStringExtra("type");
        //标题设定
        if (type.equals("常驻")) {                            //省和城市  单选  102 requst to second    111  resultcode
            data_pro = (ArrayList<Areas>) province();
            title = "全国";
            hideObjs(new int[]{R.id.tv_right});
        } else if (type.equals("科室")) {                    //科室       多选   112 res
            title = "科室";
            ((TextView) getToolView(R.id.tv_right)).setText("保存");
            ((TextView) getToolView(R.id.tv_right)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String roomIntArr = appendRoom();
                    Intent intent = new Intent();
                    intent.putExtra("roomarr", roomIntArr);
                    intent.putExtra("roomList", (Serializable) room);
                    intent.putStringArrayListExtra("room", (ArrayList<String>) roomSel);
                    ChooseFirst.this.setResult(112, intent);
                    ChooseFirst.this.finish();
                }
            });
        } else if (type.equals("省份")) {                    //省份       多选   113 res
            data_pro = (ArrayList<Areas>) province();
            title = "我要代理的省份";
            ((TextView) getToolView(R.id.tv_right)).setText("保存");
            ((TextView) getToolView(R.id.tv_right)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("prosel", (Serializable) proData);
                    ChooseFirst.this.setResult(113, intent);
                    ChooseFirst.this.finish();
                }
            });
        } else if (type.equals("城市")) {                    //城市        多选  200 res
            data_pro = (ArrayList<Areas>) province();
            title = "我要代理的城市";
        } else if (type.equals("医院")) {                    //医院        多选  106 res
            data_pro = (ArrayList<Areas>) province();
            title = "我要代理的医院";
        }
        changeTitle(title);
//        if (type.equals("常驻")) {
//
//        } else if (type.equals("科室")) {
//
//        } else if (type.equals("省份")) {
//
//            //读取本地存储数据，如果空，在请求
//
//
//        } else if (type.equals("城市")) {
//        }

        initRv();
    }

    private void initRv() {
        if (type.equals("常驻")) {
            adapter = new BaseQuickAdapter<Areas>(ChooseFirst.this, "", R.layout.item_province, data_pro) {

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
                    str_province = data_pro.get(position).getName();
                    childList = city(data_pro.get(position).getId());
                    Intent intent = new Intent(ChooseFirst.this, ChooseSecond.class);
                    startActivityForResult(intent, 102);
                }
            });
        } else if (type.equals("科室")) {
            final List<Labelinfo> data_room = labels(4);
            adapter = new BaseQuickAdapter<Labelinfo>(ChooseFirst.this, "", R.layout.item_single, data_room) {

                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    TextView tv = (TextView) holder.getView(R.id.tv);
                    tv.setText(item.getText());
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
                        roomSel.remove(data_room.get(position));
                        room.remove(data_room.get(position));
                        seletedArr.remove((Object) position);
                    } else {
                        CheckBox cb = (CheckBox) base.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_check_bg);
                        roomSel.add(data_room.get(position).getText());
                        room.add(data_room.get(position));
                        seletedArr.add(position);
                    }
                }
            });

        } else if (type.equals("省份")) {
            adapter = new BaseQuickAdapter<Areas>(ChooseFirst.this, "", R.layout.item_single, data_pro) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
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
                        proData.remove(data_pro.get(position));
                        seletedArr.remove((Object) position);
                    } else {
                        CheckBox cb = (CheckBox) base.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_check_bg);
                        proData.add(data_pro.get(position));
                        seletedArr.add(position);
                    }
                }
            });
        } else if (type.equals("城市")) {
            adapter = new BaseQuickAdapter<Areas>(ChooseFirst.this, "", R.layout.item_province, data_pro) {

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
                    str_province = data_pro.get(position).getName();
                    childList = city(data_pro.get(position).getId());
                    Intent intent = new Intent(ChooseFirst.this, ChooseSecond.class);
                    intent.putExtra("multipleChoose", true);
                    startActivityForResult(intent, 200);
                }
            });
        }else if (type.equals("医院")) {
            adapter = new BaseQuickAdapter<Areas>(ChooseFirst.this, "", R.layout.item_province, data_pro) {

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
                    str_province = data_pro.get(position).getName();
                    childList = city(data_pro.get(position).getId());
                    Intent intent = new Intent(ChooseFirst.this, ChooseSecond.class);
                    intent.putExtra("multipleChoose_hos", true);
                    startActivityForResult(intent, 120);
                }
            });
        }
        rv.setItemViewCacheSize(100);
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

    //接受返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 102) {
            this.setResult(111, data);
            this.finish();
        } else if (requestCode == 200) {  //代理的城市 多选
            this.setResult(200, data);

//            List<Areas> list = (List<Areas>) data.getSerializableExtra("result");
            this.finish();
        }else if(requestCode==120&&resultCode==120){
            this.setResult(120, data);
            this.finish();
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (type.equals("全国")) {
            Intent cancel = new Intent();
            cancel.putExtra("province", "cancel");
            cancel.putExtra("city", "cancel");
            this.setResult(101, cancel);
        } else {
            super.onBackPressed();
        }

    }

    public String appendRoom() {
        String roomStr = "";
        for (Labelinfo a : room) {
            roomStr += a.getId() + ",";
        }
        return roomStr;
    }
}
