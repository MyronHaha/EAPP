package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Jason on 2017/12/5.
 * city pic single  result code 102
 * city pic mutil                 200
 */

public class ChooseThird extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    private BaseQuickAdapter<Areas> adapter;
    String title ="";
    //代理的医院
    List<Areas> mulsel_hos = new ArrayList<>();
    List<Areas> hos_data  = new ArrayList<>();
    List<Integer> seletedArr = new ArrayList<>();
    int cityId = -1;
    public ChooseThird(){
        super(R.layout.layout_choose);
    }


    protected void initView() {
        super.initView();
        try{
            title = getIntent().getStringExtra("type");
            cityId = getIntent().getIntExtra("cityid",-1);
        }catch (Exception e){
            e.printStackTrace();
        }

        changeTitle( title);
        //代理医院
        if (title.equals("选择代理的医院")){
            ((TextView)getToolView(R.id.tv_right)).setText("保存");
            ((TextView)getToolView(R.id.tv_right)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent result = new Intent();
                    result.putExtra("result", (Serializable)mulsel_hos );
                    ChooseThird.this.setResult(120,result);
                    ChooseThird.this.finish();
                }
            });
        }
        else{
            hideObjs(new int[]{R.id.tv_right});
        }
        initRv();
    }

    private void initRv() {
        //多选  我代理的城市
        if (title.equals("选择代理的医院")) {
            hos_data.clear();
            hos_data.addAll(hosp(cityId));
            adapter = new BaseQuickAdapter<Areas>(ChooseThird.this, "", R.layout.item_single, hos_data) {

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
                        mulsel_hos.remove(hos_data.get(position));
                        seletedArr.remove((Object) position);
                    } else {
                        CheckBox cb = (CheckBox) base.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_check_bg);
                        mulsel_hos.add(hos_data.get(position));
                        seletedArr.add(position);
                    }
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
}
