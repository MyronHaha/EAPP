package com.hyhscm.myron.eapp.activity.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.design.MaterialDialog;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.Bizinfo;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Labelinfo;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 */

public class AgentSetting extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView tag_adg;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.gv)
    RecyclerView rv;
    @BindView(R.id.gv2)
    RecyclerView rv2;
    @BindView(R.id.tv_base)
    TextView tv_base;
    @BindView(R.id.tv_room)
    TextView tv_room;
    @BindView(R.id.province)
    TextView tv_provice;
    @BindView(R.id.city)
    TextView tv_city;
    @BindView(R.id.tv_hos)
    TextView tv_hos;
    @BindView(R.id.tv_hos2)
    TextView tv_hos2;
    List<String> cusTagList = new ArrayList();
    BaseQuickAdapter<String> adapter;

    List<String> tempList = new ArrayList<>();
    String result = "";
    List<Integer> seletedArr = new ArrayList<>();//mutiple
    List<Integer> seletedArr4_1 = new ArrayList<>();//mutiple
    List<Integer> seletedArr4_2 = new ArrayList<>();//mutiple
    BaseQuickAdapter<Labelinfo> typeAdapter;
    BaseQuickAdapter<Labelinfo> adapter2;
    List<String> typeList4_1 = new ArrayList<>();//mutiple
    List<String> typeList4_2 = new ArrayList<>();//mutiple
    //常驻
    String czProvince = "";
    String czCity = "";
    String czCityId = "";
    //科室
    List roomList = new ArrayList();


    //
    private Bizinfo info = null; //修改 提交的
    private Bizinfo getInfo = null;//获取的；

    protected List<Areas> allAreas = new ArrayList<>();

    public AgentSetting() {
        super(R.layout.layout_agent_setting);
    }

    //pro
    public static List<Areas> proList = new ArrayList();
    //city
    public static List<Areas> citysList = new ArrayList();
    //hos1
    public static List<Areas> hosList1 = new ArrayList();
    //hos2
    public static List<Areas> hosList2 = new ArrayList();
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_agent_setting);
//        ButterKnife.bind(this);
//        initView();
//    }
    String deptsid;
    String areasid1;
    String areasid2;
    String hospid1;
    String hospid2;
    String lablels;  //???

    //   info.setAgentsid(agentid);
//            info.setChannelid(channelid);
//            info.setResidentsid(czCityId);
//            info.setDeptsid(appendListLable(roomList, ","));
//            info.setAreasid1(appendListArea(proList, ","));
//            info.setAreasid2(appendListArea(citysList, ","));
//            info.setHospid1(appendListArea(hosList1, ","));
//            info.setHospid2(appendListArea(hosList2, ","));
//            info.setLabels(appendList(cusTagList, ","));
    @Override
    protected void initData() {
        HttpCore.getAgentInfo(new IResultHandler<Bizinfo>() {
            @Override
            public void onSuccess(Result<Bizinfo> rs) {
                if (rs.getSuccess()) {
                    allAreas = areas();
                    getInfo = rs.getBiz();
                    deptsid = getInfo.getDeptsid();
                    areasid1 = getInfo.getAreasid1();
                    areasid2 = getInfo.getAreasid2();
                    hospid1 = getInfo.getHospid1();
                    hospid2 = getInfo.getHospid2();
                    lablels = getInfo.getLabels();
                    cusTagList.addAll(splitString(lablels, ","));
                    adapter.notifyDataSetChanged();
                    info.setDeptsid(deptsid);
                    info.setAreasid1(areasid1);
                    info.setAreasid2(areasid2);
                    info.setHospid1(hospid1);
                    info.setHospid2(hospid2);
                    String agent = getInfo.getAgentsid();
                    String channel = getInfo.getChannelid();
                    List list = splitString(agent, ",");
                    List list2 = splitString(channel, ",");
                    //  all lables --match id with- splitList
                    refreshLabelInfoSeleted(labels(2), list, seletedArr4_1, typeList4_1);
                    refreshLabelInfoSeleted(labels(3), list2, seletedArr4_2, typeList4_2);
                    //
                    tv_room.setText(splitString(getInfo.getDepts(), "\\s+").size() + "");
                    tv_base.setText(getInfo.getResidents());
                    tv_city.setText(splitString(getInfo.getAreas2(), "\\s").size() + "");


                    try {
                        tv_provice.setText(splitString(getInfo.getAreas1(), "\\s").size() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tv_hos.setText(splitString(getInfo.getHosp1(), "\\s").size() + "");
                    tv_hos2.setText(splitString(getInfo.getHosp2(), "\\s").size() + "");
                }
                typeAdapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();

                //这里初始化多选list
                refreshAreaSeletedById(1, allAreas, splitString(getInfo.getAreasid1(), ","), proList);
                refreshAreaSeletedById(2, allAreas, splitString(getInfo.getAreasid2(), ","), citysList);
                refreshAreaSeletedById(3, allAreas, splitString(getInfo.getHospid1(), ","), hosList1);
                refreshAreaSeletedById(3, allAreas, splitString(getInfo.getHospid2(), ","), hosList2);
            }


        });
    }

    protected void initView() {
        super.initView();
        info = new Bizinfo();
        common.changeTitle(this, "代理设置");
        common.hideObjs(this, new int[]{R.id.tv_right});
        initRV_agent();
        initChooseList();
        setTextMax(tv_provice);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int input, int i1, int back) {
            }


            @Override
            public void afterTextChanged(Editable editable) {
                result = editable.toString();
            }
        });
    }

    private void initRV_agent() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);
        tag_adg.setLayoutManager(manager);
//        cusTagList.add("供应商");
//        cusTagList.add("经销商");
//        cusTagList.add("代理商");
        adapter = new BaseQuickAdapter<String>(AgentSetting.this, "", R.layout.item_ptag_delete, cusTagList) {
            @Override
            protected void convert(BaseViewHolder holder, final String item) {
                ((TextView) holder.getView(R.id.tv_tag)).setText(item);
                ((TextView) holder.getView(R.id.tv_tag)).setBackgroundResource(R.drawable.shape_tag_blue);
                holder.getView(R.id.iv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cusTagList.remove(item);
                        int index = cusTagList.indexOf(item);
                        adapter.notifyItemRemoved(index);
                        notifyItemRangeChanged(0, cusTagList.size());
                    }
                });
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);
            }
        };
        tag_adg.setAdapter(adapter);
    }

    @OnClick(R.id.tv_add)
    public void addCustomedTag() {
        String[] arr = result.split("\\s+");
        List<String> error = new ArrayList<>();
        tempList.clear();
        for (String tag : arr) {
            if (tag.length() > 4) {
                error.add(tag);
            } else {
                tempList.add(tag);
            }
        }
        for (String err : error) {
            MyToast.makeText(AgentSetting.this, "标签：" + err + "长度大于4", Toast.LENGTH_SHORT).show();
        }
        cusTagList.addAll(tempList);
        adapter.notifyDataSetChanged();
        et.setText("");
        et.clearFocus();
    }

    @OnClick(R.id.r1)
    public void chooseProandCity() {
        Intent intent = new Intent(AgentSetting.this, ChooseFirst.class);
        intent.putExtra("type", "常驻");
        startActivityForResult(intent, 101);
    }

    @OnClick(R.id.r2)
    public void chooseRoom() {
        Intent intent = new Intent(AgentSetting.this, ChooseFirst.class);
        intent.putExtra("type", "科室");
        startActivityForResult(intent, 103);
    }

    @OnClick(R.id.r4)
    public void choosePro() {
        Intent intent = new Intent(AgentSetting.this, ChooseCanDe.class);
        intent.putExtra("type", "省份");
        startActivityForResult(intent, 133);
    }

    @OnClick(R.id.r5)
    public void chooseCity() {
        Intent intent = new Intent(AgentSetting.this, ChooseCanDe.class);
        intent.putExtra("type", "城市");
        startActivityForResult(intent, 105);
    }

    @OnClick(R.id.r6)
    public void chooseHos() {
        Intent intent = new Intent(AgentSetting.this, ChooseCanDe.class);
        intent.putExtra("type", "医院");
        startActivityForResult(intent, 106);
    }

    @OnClick(R.id.r7)
    public void chooseHos2() {
        Intent intent = new Intent(AgentSetting.this, ChooseCanDe.class);
        intent.putExtra("type", "医院上量");
        startActivityForResult(intent, 107);
    }

    @OnClick(R.id.rv)
    public void outSide() {
        et.setFocusable(false);
    }

    @OnClick(R.id.et)
    public void et_click() {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        et.findFocus();

    }

    public void initChooseList() {
        final List<Labelinfo> data1 = labels(2);
        final List<Labelinfo> data2 = labels(3);
//        data1.add("医院临床");
//        data1.add("药店");
//        data1.add("第三终端");
//        data1.add("民营医院");
//        data1.add("电商");
//        data1.add("其他");
//        data2.add("医保");
//        data2.add("中标");
//        data2.add("不需中标");
        rv.setItemViewCacheSize(50); // 复用大小
        rv2.setItemViewCacheSize(20); // 复用大小

        final FullyGridLayoutManager manager1 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager2 = new FullyGridLayoutManager(this, 4);
        rv.setLayoutManager(manager1);
        rv2.setLayoutManager(manager2);
        //类型
        typeAdapter = new BaseQuickAdapter<Labelinfo>(AgentSetting.this, "myneed", R.layout.item_ptag, data1) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                holder.setText(R.id.tv_tag, item.getText());
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                if (isSelected(holder.getPosition(), 41)) {
                    changeTextBg(true, tv);
                } else {
                    changeTextBg(false, tv);
                }
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }
        };
        typeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                if (isSelected(position, 41)) {
                    changeTextBg(false, tv);
                    typeList4_1.remove(data1.get(position).getId() + "");
                    seletedArr4_1.remove((Object) position);
                } else {
                    changeTextBg(true, tv);
                    seletedArr4_1.add(position);
                    typeList4_1.add(data1.get(position).getId() + "");
                }


            }
        });
        rv.setAdapter(typeAdapter);
        //渠道
        adapter2 = new BaseQuickAdapter<Labelinfo>(AgentSetting.this, "myneed", R.layout.item_ptag, data2) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                holder.setText(R.id.tv_tag, item.getText());
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                if (isSelected(holder.getPosition(), 42)) {
                    changeTextBg(true, tv);

                } else {
                    changeTextBg(false, tv);

                }
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }
        };
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                if (isSelected(position, 42)) {
                    changeTextBg(false, tv);
                    typeList4_2.remove(data2.get(position).getId() + "");
                    seletedArr4_2.remove((Object) position);
                } else {
                    changeTextBg(true, tv);
                    seletedArr4_2.add(position);
                    typeList4_2.add(data2.get(position).getId() + "");
                }
            }
        });
        rv2.setAdapter(adapter2);


    }


    // 判断多选；
    public boolean isSelected(int find, int type) {
        if (type == 41) {
            seletedArr = seletedArr4_1;
        } else if (type == 42) {
            seletedArr = seletedArr4_2;
        }

        if (seletedArr.contains(find)) {
            return true;
        } else {
            return false;
        }
    }

    //changeBg
    public void changeTextBg(boolean isSelected, TextView tv) {
        if (!isSelected) {
            tv.setTextColor(Color.parseColor("#808080"));
            tv.setBackgroundResource(R.drawable.shape_tag_grey);
        } else {
            tv.setTextColor(AgentSetting.this.getResources().getColor(R.color.white));
            tv.setBackgroundResource(R.drawable.shape_tag_blue);
        }

    }

    //常驻地选择回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 111) {
                czProvince = data.getStringExtra("province");
                czCity = data.getStringExtra("city");
                czCityId = data.getIntExtra("city_id", -1) + "";
                info.setResidentsid(czCityId);
            } else if (resultCode == 112) {
                roomList = (List) data.getSerializableExtra("roomList");
                L.e(roomList.size() + "");
                tv_room.setText("" + roomList.size());
                info.setDeptsid(appendListLable(roomList, ","));
            } else if (resultCode == 113) { //代理省份 多选
                proList = (List) data.getSerializableExtra("prosList");
                tv_provice.setText("" + proList.size());
                info.setAreasid1(appendListArea(proList, ","));
            } else if (resultCode == 200) {  //代理城市 多选
                citysList = (List) data.getSerializableExtra("result");
//                L.e(list.size() + "");
                tv_city.setText("" + citysList.size() + "");
                info.setAreasid2(appendListArea(citysList, ","));
            } else if (requestCode == 106 && resultCode == 106) {
                hosList1 = (List) data.getSerializableExtra("hosList");
//                L.e(list.size() + "");
                tv_hos.setText("" + hosList1.size() + "");
                info.setHospid1(appendListArea(hosList1, ","));
            } else if (requestCode == 107 && resultCode == 107) {
                hosList2 = (List) data.getSerializableExtra("hosList");
//                L.e(list.size() + "");
                tv_hos2.setText("" + hosList2.size() + "");
                info.setHospid2(appendListArea(hosList2, ","));

            }
        } else {
            czProvince = "广东省";
            czCity = "广州";
        }
        L.e("fdsfd", "p" + czCity + "city" + czProvince);
        tv_base.setText(czProvince + " " + czCity);
    }

    @OnClick(R.id.bt_change)
    public void submit() {
        String agentid = appendList(typeList4_1, ",");
        String channelid = appendList(typeList4_2, ",");
        try {
            info.setAgentsid(agentid);
            info.setChannelid(channelid);
            info.setLabels(appendList(cusTagList, ","));

            HttpCore.agentSetting(info, new IResultHandler<Bizinfo>() {
                @Override
                public void onSuccess(Result<Bizinfo> rs) {
                    if (rs.getSuccess()) {
                        new PromptDialog(AgentSetting.this).showSuccess("保存成功！");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AgentSetting.this.finish();
                            }
                        }, 500);
                    } else {
                        new PromptDialog(AgentSetting.this).showError(rs.getMsg());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTextMax(TextView tv) {
        tv.setMaxEms(10);
        tv.setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public void onBackPressed() {
////        super.onBackPressed();
//        PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
//            @Override
//            public void onClick(PromptButton button) {
//
//                AgentSetting.this.finish();
//            }
//        });
//        confirm.setTextColor(getResources().getColor(R.color.textnormal));
//      PromptButton cancel =  new PromptButton("取消", new PromptButtonListener() {
//            @Override
//            public void onClick(PromptButton button) {
//            }
//
//        });
//
//        cancel.setTextColor(getResources().getColor(R.color.Color_Red));
        showNormalDialog("确认" + "\n\r" + "退出吗？", new promptListener() {
            @Override
            public void cancel(MaterialDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void prompt(MaterialDialog dialog) {
                AgentSetting.this.finish();
                dialog.dismiss();
            }
        });


    }

    @OnClick(R.id.iv_back)
    public void goback() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        proList.clear();
        citysList.clear();
        hosList1.clear();
        hosList2.clear();
        super.onDestroy();

    }
}
