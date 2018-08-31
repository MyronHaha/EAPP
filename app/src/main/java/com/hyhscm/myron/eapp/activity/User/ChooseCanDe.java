
package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.DeleteRecycleView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/12/1.
 */

public class ChooseCanDe extends BaseActivity {
    @BindView(R.id.rv)
    DeleteRecycleView rv;
    @BindView(R.id.tv_type)
    TextView tv_type;

    List<Areas> resultList = new ArrayList();
    List<Areas> proList = new ArrayList<>();
    List<Areas> hosList = new ArrayList<>();

    BaseQuickAdapter<Areas> adapter;
    String type = "";
    List hasSelectedCities = new ArrayList();
    Set set = new HashSet<Areas>();
    public ChooseCanDe() {
        super(R.layout.rv_candelete);
    }

    protected void initView() {
        super.initView();
        try {
            hasSelectedCities = (List) getIntent().getSerializableExtra("selectedCities");
            if (hasSelectedCities.size() > 0) {
                resultList = hasSelectedCities;
                proList = hasSelectedCities;
            }

        } catch (Exception e) {

        }
        type = getIntent().getStringExtra("type");
        if (type.equals("城市")) {
            resultList = AgentSetting.citysList;
            common.changeTitle(this, "我代理的城市");
            tv_type.setText("我代理的城市");
            ((TextView) (common.getToolView(this, R.id.tv_right))).setText("保存");
            common.getToolView(this, R.id.tv_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //保存数据接口；
                    Intent citysCount = new Intent();
                    citysCount.putExtra("result", (Serializable) resultList);
                    ChooseCanDe.this.setResult(200, citysCount);
                    ChooseCanDe.this.finish();
                }
            });
            adapter = new BaseQuickAdapter<Areas>(ChooseCanDe.this, "", R.layout.item_delte, resultList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setOnItemClickListener(new DeleteRecycleView.OnItemClickListener() {


                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onDeleteClick(int position) {
                    try {
                        resultList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, resultList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            adapter.notifyDataSetChanged();
        } else if (type.equals("省份")) {
            proList = AgentSetting.proList;
            common.changeTitle(this, "我代理的省份");
            tv_type.setText("我代理的省份");
            ((TextView) (common.getToolView(this, R.id.tv_right))).setText("保存");
            common.getToolView(this, R.id.tv_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //保存数据接口；
                    Intent citysCount = new Intent();
                    citysCount.putExtra("prosList", (Serializable) proList);
                    ChooseCanDe.this.setResult(113, citysCount);
                    ChooseCanDe.this.finish();
                }
            });
            adapter = new BaseQuickAdapter<Areas>(ChooseCanDe.this, "", R.layout.item_delte, proList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setOnItemClickListener(new DeleteRecycleView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onDeleteClick(int position) {
                    try {
                        proList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, proList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            adapter.notifyDataSetChanged();
        } else if (type.equals("医院")) {
            hosList = AgentSetting.hosList1;
            common.changeTitle(this, "我代理的医院");
            tv_type.setText("我代理的医院");
            ((TextView) (common.getToolView(this, R.id.tv_right))).setText("保存");
            common.getToolView(this, R.id.tv_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //保存数据接口；
                    Intent citysCount = new Intent();
                    citysCount.putExtra("hosList", (Serializable) hosList);
                    ChooseCanDe.this.setResult(106, citysCount);
                    ChooseCanDe.this.finish();
                }
            });
            adapter = new BaseQuickAdapter<Areas>(ChooseCanDe.this, "", R.layout.item_delte, hosList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setOnItemClickListener(new DeleteRecycleView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onDeleteClick(int position) {
                    try {
                        hosList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, hosList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            adapter.notifyDataSetChanged();
        } else if (type.equals("医院上量")) {
            hosList = AgentSetting.hosList2;
            common.changeTitle(this, "我代理的医院");
            tv_type.setText("我代理的医院");
            ((TextView) (common.getToolView(this, R.id.tv_right))).setText("保存");
            common.getToolView(this, R.id.tv_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //保存数据接口；
                    Intent citysCount = new Intent();
                    citysCount.putExtra("hosList", (Serializable) hosList);
                    ChooseCanDe.this.setResult(107, citysCount);
                    ChooseCanDe.this.finish();
                }
            });
            adapter = new BaseQuickAdapter<Areas>(ChooseCanDe.this, "", R.layout.item_delte, hosList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setOnItemClickListener(new DeleteRecycleView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onDeleteClick(int position) {
                    try {
                        hosList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, hosList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            adapter.notifyDataSetChanged();
        } else if (type.equals("区域")) {
            common.changeTitle(this, "区域");
            tv_type.setText("我选择的城市");
            ((TextView) (common.getToolView(this, R.id.tv_right))).setText("保存");
            common.getToolView(this, R.id.tv_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //保存数据接口；
                    Intent citysCount = new Intent();
                    citysCount.putExtra("result", (Serializable) resultList);
                    ChooseCanDe.this.setResult(200, citysCount);
                    ChooseCanDe.this.finish();
                }
            });
            adapter = new BaseQuickAdapter<Areas>(ChooseCanDe.this, "", R.layout.item_delte, resultList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setOnItemClickListener(new DeleteRecycleView.OnItemClickListener() {


                @Override
                public void onItemClick(View view, int position) {

                }

                @Override
                public void onDeleteClick(int position) {
                    try {
                        resultList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(0, resultList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
//        initRv();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200 && data != null) {
//            resultList = ;
            List list = (List) data.getSerializableExtra("result");
//            resultList.clear();
            resultList.addAll(list);
            List tempList  = new ArrayList();
            if(resultList.size()>0){
                for(Areas areas:resultList){
                    if(!set.contains(areas.getId())){
                        set.add(areas.getId());
                        tempList.add(areas);
                    }
                }
            }
            resultList.clear();
            resultList.addAll(tempList);
            L.e("resultList", "" + resultList.size()+"set-size"+set.size());
            adapter.notifyDataSetChanged();
        } else if (requestCode == 113 && resultCode == 113 && data != null) {
            List list = (List) data.getSerializableExtra("prosel");
            proList.clear();
            proList.addAll(list);
            adapter.notifyDataSetChanged();
        } else if (requestCode == 120 && resultCode == 120 && data != null) {
            List list = (List) data.getSerializableExtra("result");
            hosList.clear();
            hosList.addAll(list);
            L.e("sieze" + hosList.size());
            adapter.notifyDataSetChanged();
        }
    }

    /* request */
    @OnClick(R.id.set)
    public void tochoose() {
        if (type.equals("区域")||type.equals("城市")) {
            Intent intent = new Intent(ChooseCanDe.this, ChooseFirst.class);
            intent.putExtra("type", "城市");
            startActivityForResult(intent, 200);
//        }
//        else if (type.equals("城市")) {
//            Intent intent = new Intent(ChooseCanDe.this, ChooseFirst.class);
//            intent.putExtra("type", "城市");
//            startActivityForResult(intent, 200);
        } else if (type.equals("省份")) {
            Intent intent = new Intent(ChooseCanDe.this, ChooseFirst.class);
            intent.putExtra("type", "省份");
            startActivityForResult(intent, 113);
        } else if (type.equals("医院") || type.equals("医院上量")) {
            Intent intent = new Intent(ChooseCanDe.this, ChooseFirst.class);
            intent.putExtra("type", "医院");
            startActivityForResult(intent, 120);
        }

    }
}
