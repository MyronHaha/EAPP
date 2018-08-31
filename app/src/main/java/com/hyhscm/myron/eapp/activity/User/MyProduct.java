package com.hyhscm.myron.eapp.activity.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.common.design.MaterialDialog;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.adapter.MyProductAdapter;
import com.hyhscm.myron.eapp.adapter.MyneedAdapter;
import com.hyhscm.myron.eapp.adapter.TagAdapter;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.CommitResult;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.JsonBean;
import com.hyhscm.myron.eapp.data.Labelinfo;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.CustomedPopu;
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;


/**
 * Created by Jason on 2017/12/1.
 * http://8.8.8.8:8080/web.server/yjhm/pq?uid=1
 */

public class MyProduct extends ListBaseBeanActivity<Product> {
    @BindView(R.id.search)
    EditText et_search;
    @BindView(R.id.rv)
    LRecyclerView rv;
    @BindView(R.id.iv1)
    ImageView iv2;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.iv2)
    ImageView iv3;
    @BindView(R.id.iv4)
    ImageView iv4;


    @BindView(R.id.area)
    TextView tv_area;
    @BindView(R.id.lastest)
    TextView tv_lastest;


    private PopupWindow popup;
    //list1 position  地区
    private ArrayList<Areas> options1Items = new ArrayList<>();
    private ArrayList<Areas> options2Items = new ArrayList<>();
    private String province = "广东省";
    private String city = "广州市";
    private BaseViewHolder item_last, item_last2;
    private List<Areas> cityList = new ArrayList<>();
    private BaseQuickAdapter<Areas> adapter2;
    private Areas firareas;
    //最新
    private String selectDefault1 = "0";
    private BaseViewHolder item_last3;
    //分类
    private String selectDefault3 = "耗材";
    private BaseViewHolder item_last4;
    private List<Integer> seletedArr = new ArrayList<>();//mutiple
    private List<String> typeList = new ArrayList<>();//mutiple
    private String append = "";//多选追加的字符串；
    private List<Integer> seletedArr1 = new ArrayList<>();//mutiple
    //筛选
    //4-1   渠道；
    String selectDefault4_1 = "医院临床";
    String selectDefault4_2 = "医保";
    String selectDefault4_3 = "省级代理";
    List<Integer> seletedArr4_1 = new ArrayList<>();//mutiple
    List<Integer> seletedArr4_2 = new ArrayList<>();//mutiple
    List<Integer> seletedArr4_3 = new ArrayList<>();//mutiple
    List<Integer> seletedArr4_4 = new ArrayList<>();//mutiple
    List<String> typeList4_1 = new ArrayList<>();//mutiple
    List<String> typeList4_2 = new ArrayList<>();//mutiple
    List<String> typeList4_3 = new ArrayList<>();//mutiple
    List<String> typeList4_4 = new ArrayList<>();//mutiple

    List<Labelinfo> data1 = new ArrayList<>();
    List<Labelinfo> data2;
    List<Labelinfo> data3;
    List<Labelinfo> data4;
    int pos1 = -1;
    int pos2 = 0;
    ProgressDialog d;
    private List<Labelinfo> info2;
    String[] menu = null;

    public MyProduct() {
        super(R.layout.layout_homeproduct);
    }

    @OnClick(R.id.iv_right)
    public void publishNeed() {
        launchActivity(ProductPublish.class);
    }

    private Product productTemp;

    protected void initData() {
        params.put("uid", HttpCore.userId + "");
        initData(Url.MERCHANTLIST, 8);
        options1Items = (ArrayList<Areas>) province();
        info2 = labels(2);
        data1.addAll(labels(3));

        data2 = labels(7);
        data3 = labels(6);
        data4 = labels(4);
    }

    protected void initView() {
        super.initView();
        d = new ProgressDialog(this);
        et_search.clearFocus();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                params.put("k", editable.toString());
                loadData();
            }
        });
        findViewById(R.id.area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(0);
            }
        });
        findViewById(R.id.lastest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(1);
            }
        });
        findViewById(R.id.type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(2);
            }
        });
        findViewById(R.id.sx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(3);
            }
        });
        initListView(R.layout.item_homeproduct2);
    }

    public void popup(final int i) {
        append = "";
        View root = null;
        if (i == 0) {

            root = this.getLayoutInflater().inflate(R.layout.layout_popup, null);

            final RecyclerView rv1, rv2;
            rv1 = (RecyclerView) root.findViewById(R.id.lv1);
            rv1.setItemViewCacheSize(100); // 复用大小
            rv2 = (RecyclerView) root.findViewById(R.id.lv2);
            rv2.setItemViewCacheSize(100);// 复用大小
            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
            rv1.setLayoutManager(manager1);
            rv2.setLayoutManager(new LinearLayoutManager(this));
            BaseQuickAdapter<Areas> adapter1 = new BaseQuickAdapter<Areas>(MyProduct.this, "myneed", R.layout.item_pop, options1Items) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (holder.getPosition() == pos1) {
                        item_last = holder;
                        holder.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.page_bg));
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder v, final int position) {
                    pos2 = 0;
//                    MyToast.makeText(MyProduct.this, options1Items.get(position).getName() + "--" + position, 1000).show();
                    //not first
                    if (item_last != null) {
                        //上次ui
                        item_last.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last.getView(R.id.tv);
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    if (item_last2 != null) {
                        //上次ui2
                        item_last2.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    v.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.page_bg));
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
                    item_last = v;
                    pos1 = position;
                    cityList.clear();
                    d.show();
                    options2Items = (ArrayList<Areas>) city(options1Items.get(position).getId());


                    //第一个 “全部”
                    firareas = new Areas();
                    firareas.setId(options1Items.get(position).getId());
                    firareas.setLevels(2);
                    firareas.setName("全部");
                    firareas.setPid(options1Items.get(position).getId());

                    cityList.add(firareas);
                    cityList.addAll(options2Items);
                    d.dismiss();
                    adapter2.notifyDataSetChanged();
                }
            });
            adapter2 = new BaseQuickAdapter<Areas>(MyProduct.this, "myneed", R.layout.item_pop, cityList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (pos2 == holder.getPosition()) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
                        item_last2 = holder;


                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder v, int position) {
                    //not first
                    if (item_last2 != null) {
                        //上次ui
                        item_last2.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
                    v.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.page_bg));
                    item_last2 = v;
                    pos2 = position;
                    city = cityList.get(position).getName();
                    params.put("aid", cityList.get(position).getId() + "");
                    tv_area.setText(cityList.get(pos2).getName());
                    loadData();
                    popup.dismiss();

                }
            });
            rv1.setAdapter(adapter1);
            rv2.setAdapter(adapter2);
        } else if (i == 1) {
            final List<String> data = new ArrayList();
            data.add("最新发布");
            data.add("综合");
            data.add("推荐");
            root = this.getLayoutInflater().inflate(R.layout.item_rv, null);
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv);
            rv.setItemViewCacheSize(20); // 复用大小
            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter adapter = new BaseQuickAdapter(MyProduct.this, "myneed", R.layout.item_single, data) {
                @Override
                protected void convert(BaseViewHolder holder, Object item) {
                    holder.setText(R.id.tv, item.toString());
                    if (holder.getPosition() == Integer.parseInt(selectDefault1.trim())) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
                        CheckBox cb = (CheckBox) holder.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_check_bg);
                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                    if (item_last3 != null) {
                        TextView tv = (TextView) item_last3.getView(R.id.tv);
                        tv.setTextColor(Color.parseColor("#4d4d4d"));
                        CheckBox cb = (CheckBox) item_last3.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_uncheck_bg);
                    }
                    TextView tv = (TextView) base.getView(R.id.tv);
                    tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
                    CheckBox cb = (CheckBox) base.getView(R.id.cb);
                    cb.setBackgroundResource(R.drawable.shape_check_bg);
                    item_last3 = base;
                    selectDefault1 = position + "";
                    params.put("s", selectDefault1);
                    tv_lastest.setText(data.get(position).substring(0, 2));
                    loadData();
                    popup.dismiss();
                }
            });
        } else if (i == 2) {

//            fid分类
            root = this.getLayoutInflater().inflate(R.layout.layout_popup_gv, null);
            ((TextView) root.findViewById(R.id.title)).setText("分类");
            ((TextView) root.findViewById(R.id.seltype)).setText("可多选");
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    typeList.clear();
//                    seletedArr.clear();
                    popup.dismiss();
                }
            });
            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        params.put("cid", appendList(typeList, ","));
                        loadData();
//                        for (String i : typeList) {
//                            append += i;
//                        }
//                        L.e("iiiii", append);
                    } catch (Exception e) {
                        L.e("iiiii", "null");
                    }
                    popup.dismiss();
                    typeList.clear();
//                    seletedArr.clear();
                }
            });
            final List<Labelinfo> data = info2;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.gv);
            rv.setItemViewCacheSize(50); // 复用大小
            final GridLayoutManager manager1 = new GridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                    if (!item.isSelected()) {
//1
                        TextView tv = (TextView) holder.getView(R.id.tv_tag);
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
//1
                        TextView tv = (TextView) holder.getView(R.id.tv_tag);
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
//2
                        typeList.add(data.get(holder.getPosition()).getId() + "");

                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                    TextView tv = (TextView) base.getView(R.id.tv_tag);
                    if (!data.get(position).isSelected()) {
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);

                        typeList.add(data.get(position).getId() + "");

                        data.get(position).setSelected(true);

                    } else {

                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);

                        typeList.remove(data.get(position).getId() + "");
                        data.get(position).setSelected(false);
                    }


//                    if (isSelected(position, 1)) {
//                        tv.setTextColor(Color.parseColor("#808080"));
//                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
//                        typeList.remove(data.get(position));
//                        seletedArr.remove((Object) position);
//                    } else {
//                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
//                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
//                        seletedArr.add(position);
//                        typeList.add(data.get(position).getId()+"");
//                    }
                }
            });
        } else if (i == 3) {
            //筛选

            root = this.getLayoutInflater().inflate(R.layout.layout_popup_mutiple_gv, null);
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
//                    typeList4_1.clear();
//                    typeList4_2.clear();
//                    typeList4_3.clear();
//                    typeList4_4.clear();
                }
            });
            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    append = "";
//                    for (String a : typeList4_1) {
//                        append += a;
//                    }
//                    append += "\n" + "22222";
//                    for (String b : typeList4_2) {
//                        append += b;
//                    }
//                    append += "\n" + "3333333";
//                    for (String c : typeList4_3) {
//                        append += c;
//                    }
//                    Log.e("result", "" + append);
                    params.put("chid", appendList(typeList4_1, ","));
                    params.put("mid", appendList(typeList4_2, ","));
                    params.put("bid", appendList(typeList4_3, ","));
                    params.put("did", appendList(typeList4_4, ","));
                    loadData();
                    popup.dismiss();
                    typeList4_1.clear();
                    typeList4_2.clear();
                    typeList4_3.clear();
                    typeList4_4.clear();
                }
            });
            RecyclerView rv, rv2, rv3, rv4;
            rv = (RecyclerView) root.findViewById(R.id.gv);
            rv2 = (RecyclerView) root.findViewById(R.id.gv2);
            rv3 = (RecyclerView) root.findViewById(R.id.gv3);
            rv4 = (RecyclerView) root.findViewById(R.id.gv4);
            rv.setItemViewCacheSize(20); // 复用大小
            rv2.setItemViewCacheSize(20); // 复用大小
            rv3.setItemViewCacheSize(20); // 复用大小
            rv4.setItemViewCacheSize(50); // 复用大小
            final FullyGridLayoutManager manager1 = new FullyGridLayoutManager(this, 4);
            final FullyGridLayoutManager manager2 = new FullyGridLayoutManager(this, 4);
            final FullyGridLayoutManager manager3 = new FullyGridLayoutManager(this, 4);
            final FullyGridLayoutManager manager4 = new FullyGridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            rv2.setLayoutManager(manager2);
            rv3.setLayoutManager(manager3);
            rv4.setLayoutManager(manager4);

            //渠道
            final BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data1) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    L.e(this.toString());
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);


                    } else {

                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        if (!typeList4_1.contains(item.getId() + ""))
                            typeList4_1.add(item.getId() + "");
                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                    TextView tv = (TextView) base.getView(R.id.tv_tag);
                    if (data1.get(position).isSelected()) {
                        changeTextBg(false, tv);
                        typeList4_1.remove(data1.get(position).getId() + "");
                        data1.get(position).setSelected(false);
                    } else {
                        changeTextBg(true, tv);
                        typeList4_1.add(data1.get(position).getId() + "");
                        data1.get(position).setSelected(true);
                    }

                }
            });

            //医保
            BaseQuickAdapter<Labelinfo> adapter2 = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data2) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        if (!typeList4_2.contains(item.getId() + ""))
                            typeList4_2.add(item.getId() + "");
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
                    if (data2.get(position).isSelected()) {
                        changeTextBg(false, tv);
                        typeList4_2.remove(data2.get(position).getId() + "");
                        data2.get(position).setSelected(false);
                    } else {
                        changeTextBg(true, tv);
                        typeList4_2.add(data2.get(position).getId() + "");
                        data2.get(position).setSelected(true);
                    }
                }
            });
            rv2.setAdapter(adapter2);

            //科室
            BaseQuickAdapter<Labelinfo> adapter4 = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data4) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        if (!typeList4_4.contains(item.getId() + ""))
                            typeList4_4.add(item.getId() + "");
                    }
                }


                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter4.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {

                    TextView tv = (TextView) base.getView(R.id.tv_tag);
                    if (data4.get(position).isSelected()) {
                        changeTextBg(false, tv);
                        typeList4_4.remove(data4.get(position).getId() + "");
                        data4.get(position).setSelected(false);
                    } else {
                        changeTextBg(true, tv);
                        typeList4_4.add(data4.get(position).getId() + "");
                        data4.get(position).setSelected(true);
                    }

                }
            });
            rv4.setAdapter(adapter4);

            //业务
            BaseQuickAdapter<Labelinfo> adapter3 = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data3) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        if (!typeList4_3.contains(item.getId() + ""))
                            typeList4_3.add(item.getId() + "");
                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                    TextView tv = (TextView) base.getView(R.id.tv_tag);
                    if (data3.get(position).isSelected()) {
                        changeTextBg(false, tv);
                        typeList4_3.remove(data3.get(position).getId() + "");
                        data3.get(position).setSelected(false);
                    } else {
                        changeTextBg(true, tv);
                        typeList4_3.add(data3.get(position).getId() + "");
                        data3.get(position).setSelected(true);
                    }
                }
            });
            rv3.setAdapter(adapter3);
        }
        popup = new CustomedPopu(this).getPopupView(root);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                setBackgroundAlpha(Detail_xq.this, 1.0f);
//窗口关闭处理；
                if (i == 0) {
                    //清除二级数据，防止下次与一级对应不上；
//                    cityList.clear();
//                    options1Items.clear();
//                    options2Items.clear();
//                    MyToast.makeText(MyProduct.this, province + "--" + city, 1000).show();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            iv2.setRotation(360);
                        }
                    });

                } else if (i == 1) {

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            iv.setRotation(360);
                        }
                    });

                } else if (i == 2) {
                    typeList.clear();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            iv3.setRotation(360);
                        }
                    });
                } else if (i == 3) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            iv4.setRotation(360);
                        }
                    });
                }

            }
        });
        //
        //show popu
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            int[] a = new int[2];
//            findViewById(R.id.d2).getLocationInWindow(a);
//            popup.showAtLocation(findViewById(R.id.d2), Gravity.NO_GRAVITY, 0, a[1] +    findViewById(R.id.d2).getHeight() + 0);
//        } else {
//            popup.showAsDropDown(findViewById(R.id.d2));
//        }
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            findViewById(R.id.d2).getGlobalVisibleRect(rect);
            int h = (findViewById(R.id.d2)).getResources().getDisplayMetrics().heightPixels - rect.bottom;
            popup.setHeight(h);
            popup.showAsDropDown(findViewById(R.id.d2));
        } else {
            popup.showAsDropDown(findViewById(R.id.d2));
        }
//  -----------
        if (i == 0) {
            iv2.setRotation(180);
        } else if (i == 1) {
            iv.setRotation(180);
        } else if (i == 2) {
            iv3.setRotation(180);

        } else if (i == 3) {
            iv4.setRotation(180);

        }

    }
//    public void popup(final int i) {
//        append = "";
//        View root = null;
//        if (i == 0) {
//            options1Items = (ArrayList<Areas>) province();
//            root = this.getLayoutInflater().inflate(R.layout.layout_popup, null);
//            final RecyclerView rv1, rv2;
//            rv1 = (RecyclerView) root.findViewById(R.id.lv1);
//            rv1.setItemViewCacheSize(100); // 复用大小
//            rv2 = (RecyclerView) root.findViewById(R.id.lv2);
//            rv2.setItemViewCacheSize(100);// 复用大小
//            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
//            rv1.setLayoutManager(manager1);
//            rv2.setLayoutManager(new LinearLayoutManager(this));
//            BaseQuickAdapter<Areas> adapter1 = new BaseQuickAdapter<Areas>(MyProduct.this, "myneed", R.layout.item_pop, options1Items) {
//                @Override
//                protected void convert(BaseViewHolder holder, Areas item) {
//                    holder.setText(R.id.tv, item.getName());
//                }
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder v, int position) {
//
//                    MyToast.makeText(MyProduct.this, options1Items.get(position).getName() + "--" + position, 1000).show();
//                    //not first
//                    if (item_last != null) {
//                        //上次ui
//                        item_last.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.white));
//                        TextView tv = (TextView) item_last.getView(R.id.tv);
//                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.tx_primary));
//                    }
//                    if (item_last2 != null) {
//                        //上次ui2
//                        item_last2.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.white));
//                        TextView tv = (TextView) item_last2.getView(R.id.tv);
//                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.tx_primary));
//                    }
//                    v.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.page_bg));
//                    TextView tv = (TextView) v.getView(R.id.tv);
//                    tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
//                    item_last = v;
//                    cityList.clear();
//                    options2Items = (ArrayList<Areas>) city(options1Items.get(position).getId());
//                    //第一个 “全部”
//                    firareas = new Areas();
//                    firareas.setId(options1Items.get(position).getId());
//                    firareas.setLevels(2);
//                    firareas.setName("全部");
//                    firareas.setPid(options1Items.get(position).getId());
//
//                    cityList.add(firareas);
//                    cityList.addAll(options2Items);
//                    adapter2.notifyDataSetChanged();
//                }
//            });
//            adapter2 = new BaseQuickAdapter<Areas>(MyProduct.this, "myneed", R.layout.item_pop, cityList) {
//                @Override
//                protected void convert(BaseViewHolder holder, Areas item) {
//                    holder.setText(R.id.tv, item.getName());
//                }
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder v, int position) {
//                    //not first
//                    if (item_last2 != null) {
//                        //上次ui
//                        item_last2.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.white));
//                        TextView tv = (TextView) item_last2.getView(R.id.tv);
//                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.tx_primary));
//                    }
//                    TextView tv = (TextView) v.getView(R.id.tv);
//                    tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
//                    v.getView(R.id.ll_item).setBackgroundColor(MyProduct.this.getResources().getColor(R.color.page_bg));
//                    item_last2 = v;
//
//                    city = cityList.get(position).getName();
//                    loadData();
//                    params.put("aid",cityList.get(position).getId()+"");
//                    popup.dismiss();
//
//                }
//            });
//            rv1.setAdapter(adapter1);
//            rv2.setAdapter(adapter2);
//        } else if (i == 1) {
//            final List<String> data = new ArrayList();
//            data.add("最新发布");
//            data.add("综合");
//            data.add("推荐");
//            root = this.getLayoutInflater().inflate(R.layout.item_rv, null);
//            RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv);
//            rv.setItemViewCacheSize(20); // 复用大小
//            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
//            rv.setLayoutManager(manager1);
//            BaseQuickAdapter adapter = new BaseQuickAdapter(MyProduct.this, "myneed", R.layout.item_single, data) {
//                @Override
//                protected void convert(BaseViewHolder holder, Object item) {
//                    holder.setText(R.id.tv, item.toString());
//                }
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            rv.setAdapter(adapter);
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder base, int position) {
//                    if (item_last3 != null) {
//                        TextView tv = (TextView) item_last3.getView(R.id.tv);
//                        tv.setTextColor(Color.parseColor("#4d4d4d"));
//                        CheckBox cb = (CheckBox) item_last3.getView(R.id.cb);
//                        cb.setBackgroundResource(R.drawable.shape_uncheck_bg);
//                    }
//                    TextView tv = (TextView) base.getView(R.id.tv);
//                    tv.setTextColor(MyProduct.this.getResources().getColor(R.color.colorPrimary));
//                    CheckBox cb = (CheckBox) base.getView(R.id.cb);
//                    cb.setBackgroundResource(R.drawable.shape_check_bg);
//                    item_last3 = base;
//                    selectDefault1 = position + "";
//                    params.put("s", selectDefault1);
//                    loadData();
//                    popup.dismiss();
//                }
//            });
//        } else if (i == 2) {
//
////            fid分类
//            root = this.getLayoutInflater().inflate(R.layout.layout_popup_gv, null);
//            ((TextView) root.findViewById(R.id.title)).setText("分类");
//            ((TextView) root.findViewById(R.id.seltype)).setText("可多选");
//            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    typeList.clear();
//                    seletedArr.clear();
//                    popup.dismiss();
//                }
//            });
//            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//                        params.put("cid",appendList(typeList,","));
//                        loadData();
////                        for (String i : typeList) {
////                            append += i;
////                        }
////                        L.e("iiiii", append);
//                    } catch (Exception e) {
//                        L.e("iiiii", "null");
//                    }
//                    popup.dismiss();
//                    typeList.clear();
//                    seletedArr.clear();
//                }
//            });
//            final List<Labelinfo> data = labels(2);
//            final RecyclerView rv1;
//            RecyclerView rv = (RecyclerView) root.findViewById(R.id.gv);
//            rv.setItemViewCacheSize(50); // 复用大小
//            final GridLayoutManager manager1 = new GridLayoutManager(this, 4);
//            rv.setLayoutManager(manager1);
//            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data) {
//                @Override
//                protected void convert(BaseViewHolder holder, Labelinfo item) {
//                    holder.setText(R.id.tv_tag, item.getText());
//                }
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            rv.setAdapter(adapter);
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder base, int position) {
//                    TextView tv = (TextView) base.getView(R.id.tv_tag);
//                    if (isSelected(position, 1)) {
//                        tv.setTextColor(Color.parseColor("#808080"));
//                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
//                        typeList.remove(data.get(position));
//                        seletedArr.remove((Object) position);
//                    } else {
//                        tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
//                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
//                        seletedArr.add(position);
//                        typeList.add(data.get(position).getId()+"");
//                    }
//                }
//            });
//        } else if (i == 3) {
//            //筛选
//            final List<Labelinfo> data1 = labels(3);
//            final List<Labelinfo> data2 = labels(7);
//            final List<Labelinfo> data3 = labels(6);
//            final List<Labelinfo> data4 = labels(4);
////            data1.add("医院临床");
////            data1.add("药店");
////            data1.add("第三终端");
////            data1.add("民营医院");
////            data1.add("电商");
////            data1.add("其他");
////            data2.add("医保");
////            data2.add("中标");
////            data2.add("不需中标");
////            data3.add("省级代理");
////            data3.add("市级代理");
////            data3.add("医院代理");
////            data3.add("医院上量");
//            root = this.getLayoutInflater().inflate(R.layout.layout_popup_mutiple_gv, null);
//            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    popup.dismiss();
//                    typeList4_1.clear();
//                    typeList4_2.clear();
//                    typeList4_3.clear();
//                    typeList4_4.clear();
//                }
//            });
//            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    append = "";
//                    for (String a : typeList4_1) {
//                        append += a;
//                    }
//                    append += "\n" + "22222";
//                    for (String b : typeList4_2) {
//                        append += b;
//                    }
//                    append += "\n" + "3333333";
//                    for (String c : typeList4_3) {
//                        append += c;
//                    }
//                    Log.e("result", "" + append);
//                    params.put("chid",appendList(typeList4_1,","));
//                    params.put("mid",appendList(typeList4_2,","));
//                    params.put("bid",appendList(typeList4_3,","));
//                    params.put("did",appendList(typeList4_4,","));
//                    loadData();
//                    ;                    popup.dismiss();
//                    typeList4_1.clear();
//                    typeList4_2.clear();
//                    typeList4_3.clear();
//                    typeList4_4.clear();
//                }
//            });
//            final RecyclerView rv, rv2, rv3,rv4;
//            rv = (RecyclerView) root.findViewById(R.id.gv);
//            rv2 = (RecyclerView) root.findViewById(R.id.gv2);
//            rv3 = (RecyclerView) root.findViewById(R.id.gv3);
//            rv4 =(RecyclerView) root.findViewById(R.id.gv4);
//            rv.setItemViewCacheSize(20); // 复用大小
//            rv2.setItemViewCacheSize(20); // 复用大小
//            rv3.setItemViewCacheSize(20); // 复用大小
//            rv4.setItemViewCacheSize(50); // 复用大小
//            final FullyGridLayoutManager manager1 = new FullyGridLayoutManager(this, 4);
//            final FullyGridLayoutManager manager2 = new FullyGridLayoutManager(this, 4);
//            final FullyGridLayoutManager manager3 = new FullyGridLayoutManager(this, 4);
//            final FullyGridLayoutManager manager4 = new FullyGridLayoutManager(this, 4);
//            rv.setLayoutManager(manager1);
//            rv2.setLayoutManager(manager2);
//            rv3.setLayoutManager(manager3);
//            rv4.setLayoutManager(manager4);
//
//            //渠道
//            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data1) {
//                @Override
//                protected void convert(BaseViewHolder holder, Labelinfo item) {
//                    holder.setText(R.id.tv_tag, item.getText());
//                }
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder base, int position) {
//                    TextView tv = (TextView) base.getView(R.id.tv_tag);
//                    if (isSelected(position, 41)) {
//                        changeTextBg(false, tv);
//                        typeList4_1.remove(data1.get(position).getId()+"");
//                        seletedArr4_1.remove((Object) position);
//                    } else {
//                        changeTextBg(true, tv);
//                        seletedArr4_1.add(position);
//                        typeList4_1.add(data1.get(position).getId()+"");
//                    }
//
//                }
//            });
//            rv.setAdapter(adapter);
//            //医保
//            BaseQuickAdapter<Labelinfo> adapter2 = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data2) {
//                @Override
//                protected void convert(BaseViewHolder holder, Labelinfo item) {
//                    holder.setText(R.id.tv_tag, item.getText());
//                }
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder base, int position) {
//                    TextView tv = (TextView) base.getView(R.id.tv_tag);
//                    if (isSelected(position, 42)) {
//                        changeTextBg(false, tv);
//                        typeList4_2.remove(data2.get(position).getId()+"");
//                        seletedArr4_2.remove((Object) position);
//                    } else {
//                        changeTextBg(true, tv);
//                        seletedArr4_2.add(position);
//                        typeList4_2.add(data2.get(position).getId()+"");
//                    }
//                }
//            });
//            rv2.setAdapter(adapter2);
//
//            //科室
//            BaseQuickAdapter<Labelinfo> adapter4 = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data4) {
//                @Override
//                protected void convert(BaseViewHolder holder, Labelinfo item) {
//                    holder.setText(R.id.tv_tag, item.getText());
//                }
//
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            adapter4.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder base, int position) {
//                    TextView tv = (TextView) base.getView(R.id.tv_tag);
//                    if (isSelected(position, 44)) {
//                        changeTextBg(false, tv);
//                        typeList4_4.remove(data4.get(position).getId()+"");
//                        seletedArr4_4.remove((Object) position);
//                    } else {
//                        changeTextBg(true, tv);
//                        typeList4_4.add(data4.get(position).getId()+"");
//                        seletedArr4_4.add(position);
//                    }
//                }
//            });
//            rv4.setAdapter(adapter4);
//
//            //业务
//            BaseQuickAdapter<Labelinfo> adapter3 = new BaseQuickAdapter<Labelinfo>(MyProduct.this, "myneed", R.layout.item_ptag, data3) {
//                @Override
//                protected void convert(BaseViewHolder holder, Labelinfo item) {
//                    holder.setText(R.id.tv_tag, item.getText());
//                }
//
//                @Override
//                public void onBindViewHolder(BaseViewHolder holder, int position) {
//                    super.onBindViewHolder(holder, position);
//                }
//            };
//            adapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseViewHolder base, int position) {
//                    TextView tv = (TextView) base.getView(R.id.tv_tag);
//                    if (isSelected(position, 43)) {
//                        changeTextBg(false, tv);
//                        typeList4_3.remove(data3.get(position).getId()+"");
//                        seletedArr4_3.remove((Object) position);
//                    } else {
//                        changeTextBg(true, tv);
//                        typeList4_3.add(data3.get(position).getId()+"");
//                        seletedArr4_3.add(position);
//                    }
//                }
//            });
//            rv3.setAdapter(adapter3);
//        }
////        popup = new CustomedPopu(this).getPopupView(root);
//        popup = new CustomedPopu(this).getPopupView(root);
//
//        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
////                setBackgroundAlpha(Detail_xq.this, 1.0f);
////窗口关闭处理；
//                if (i == 0) {
//                    //清除二级数据，防止下次与一级对应不上；
//                    cityList.clear();
//                    options1Items.clear();
//                    options2Items.clear();
//                    MyToast.makeText(MyProduct.this, province + "--" + city, 1000).show();
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv2.setRotation(360);
//                        }
//                    });
//
//                } else if (i == 1) {
//
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv.setRotation(360);
//                        }
//                    });
//
//                } else if (i == 2) {
//                    typeList.clear();
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv3.setRotation(360);
//                        }
//                    });
//                } else if (i == 3) {
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv4.setRotation(360);
//                        }
//                    });
//                }
//
//            }
//        });
//        popup.showAsDropDown(findViewById(R.id.ll_filter));
//        if (i == 0) {
//            iv2.setRotation(180);
//        } else if (i == 1) {
//            iv.setRotation(180);
//        } else if (i == 2) {
//            iv3.setRotation(180);
//
//        } else if (i == 3) {
//            iv4.setRotation(180);
//
//        }
//
//    }

    // 判断多选；
    public boolean isSelected(int find, int type) {
        if (type == 1) {
            seletedArr = seletedArr1;
        } else if (type == 41) {
            seletedArr = seletedArr4_1;
        } else if (type == 42) {
            seletedArr = seletedArr4_2;
        } else if (type == 43) {
            seletedArr = seletedArr4_3;
        } else if (type == 44) {
            seletedArr = seletedArr4_4;
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
            tv.setTextColor(MyProduct.this.getResources().getColor(R.color.white));
            tv.setBackgroundResource(R.drawable.shape_tag_blue);
        }

    }

    @Override
    protected void bindView(BaseViewHolder holder, final Product item) {
        try {
            //上下架编辑 state 1 下架 2 上架

            if (item.getState() == 1) {
                menu = new String[]{"置顶", "分享", "编辑", "删除"};
            } else if (item.getState() == 2) {   // 上架状态  不可编辑 可下架
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                menu = new String[]{"置顶", "分享", "下架", "删除"};
            }
//                holder.getView(R.id.tv_invisible).setVisibility(View.VISIBLE);
//                holder.getView(R.id.tv_invisible).setOnClickListener(
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                               HttpCore.product_invisible(null, item.getId() + "", new IResultHandler<Product>() {
//                                   @Override
//                                   public void onSuccess(Result<Product> rs) {
//                                       if(rs.getSuccess()){
//                                           MyToast.makeText(MyProduct.this,"下架成功",1500).show();
//                                       }else{
//                                           MyToast.makeText(MyProduct.this,"操作失败("+rs.getMsg()+")，请稍后再试",1500).show();
//                                       }
//                                   }
//                               });
//                            }
//                        }
//                );
//
//            }

                holder.setText(R.id.tv_content, item.getName());
                holder.setText(R.id.tv_ago, common.TimeDifference(item.getCreationTime()));
//            holder.setText(R.id.tv_gg, item.getSpec());
                holder.setText(R.id.tv_adv, item.getAdvantage());
                holder.setImage(R.id.iv_product, Url.IMAGEPATH + item.getImg());
                List<String> tagList = new ArrayList<>();
                tagList.clear();
                if (item.getChannelName().equals("")) {
                    tagList.add("暂无标签");
                } else {
                    tagList.addAll(splitString(item.getChannelName(), "\\s+"));
                }
                RecyclerView rv = (RecyclerView) holder.getView(R.id.gridView);
                TagAdapter adapter = new TagAdapter(MyProduct.this, tagList);
                GridLayoutManager manager = new GridLayoutManager(MyProduct.this, 1, GridLayoutManager.HORIZONTAL, false);
                rv.setFocusable(false);
                rv.setFocusableInTouchMode(false);
                rv.setLayoutManager(manager);
                rv.setAdapter(adapter);

                //下拉更多
                if (holder.getView(R.id.moreclick) != null) {
                    holder.getView(R.id.moreclick).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showStringListDialog(menu, new MaterialDialog.OnClickListener() {
                                @Override
                                public boolean onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            final MaterialDialog dialog = new MaterialDialog.Builder(MyProduct.this).create();
                                            View.OnClickListener listener = new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    switch (view.getId()) {
                                                        case R.id.bt_share:
//                                                MyToast.makeText(_mActivity, "分享一次", 1000).show();
                                                            View v = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
                                                            showCustomDialog(v, item, 1, true);
                                                            break;
                                                        case R.id.bt_maketop:
                                                            makeTop(item);
                                                            break;
                                                        case R.id.fl_getbean:
                                                            launchActivity(MyCorn.class);
                                                    }
                                                    dialog.dismiss();
                                                }
                                            };
                                            showCustomAlert(dialog, getLayoutInflater().inflate(R.layout.tips_goldbean, null), new int[]{R.id.fl_getbean, R.id.bt_share, R.id.bt_maketop}, listener);
                                            break;
                                        case 1:
                                            View v = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
                                            showCustomDialog(v, item, 1, false);
                                            break;
                                        case 2:
                                            if (item.getState() == 2) { // 下架操作
                                                HttpCore.product_invisible(null, item.getId() + "", new IResultHandler<Product>() {
                                                    @Override
                                                    public void onSuccess(Result<Product> rs) {
                                                        if (rs.getSuccess()) {
                                                            MyToast.makeText(MyProduct.this, "下架成功", 1500).show();
                                                        } else {
                                                            MyToast.makeText(MyProduct.this, "操作失败(" + rs.getMsg() + ")，请稍后再试", 1500).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                //产品编辑
                                                Intent intent = new Intent(MyProduct.this, ProductPublish.class);
                                                intent.putExtra("item", item);
                                                launchActivityWithIntent(intent);
                                            }
                                            break;
                                        case 3:
                                            showNormalDialog("确认" + "\n\r" + "删除该产品?", new promptListener() {
                                                @Override
                                                public void cancel(MaterialDialog dialog) {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void prompt(MaterialDialog dialog) {
                                                    HttpCore.productDel(item.getId(), new IResultHandler<Product>() {
                                                        @Override
                                                        public void onSuccess(Result<Product> rs) {
                                                            if (rs.getSuccess()) {
                                                                new PromptDialog(MyProduct.this).showSuccess("删除成功！");
                                                                loadData();
                                                            }else{
                                                                MyToast.makeText(MyProduct.this,"操作失败("+rs.getMsg()+")请稍后再试！",1500).show();
                                                            }
                                                        }
                                                    });
                                                    dialog.dismiss();
                                                }
                                            });
                                            break;

                                    }
                                    return false;
                                }
                            });
                        }
                    });
                }
//            ImageView delete = (ImageView) holder.getView(R.id.iv_delete);
//            delete.setVisibility(View.VISIBLE);
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showNormalDialog("确认" + "\n\r" + "删除该产品?", new promptListener() {
//                        @Override
//                        public void cancel(MaterialDialog dialog) {
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void prompt(MaterialDialog dialog) {
//                            HttpCore.productDel(item.getId(), new IResultHandler<Product>() {
//                                @Override
//                                public void onSuccess(Result<Product> rs) {
//                                    if (rs.getSuccess()) {
//                                        new PromptDialog(MyProduct.this).showSuccess("删除成功！");
//                                        loadData();
//                                    }
//                                }
//                            });
//                            dialog.dismiss();
//                        }
//                    });
//
//                }
//            });
//            adapter.notifyDataSetChanged();
        } catch (
                Exception e)

        {
            L.e(e.toString());
        }

    }

    protected void itemClick(View v, int i, Product item) {
        Intent intent = new Intent(MyProduct.this, MyProductDtail.class);
        intent.putExtra("item", item);
        launchActivityWithIntent(intent);
    }

// t 2 普通金豆置顶

    public void makeTop(Product item) {
        HttpCore.makeTop(item.getId() + "", "2", "", new IResultHandler<CommitResult>() {
            @Override
            public void onSuccess(Result<CommitResult> rs) {
                if (rs.getSuccess()) {
                    MyToast.makeText(_mActivity, "置顶成功！", 1000).show();
                    selectDefault1 = "0";
                    params.put("s", selectDefault1);
                    loadData();
                } else {
                    MyToast.makeText(_mActivity, rs.getMsg(), 1000).show();
                }
            }
        });
    }

//    @BindView(R.id.tv_area)
//    TextView tv_area;
//    @BindView(R.id.tv_leibie)
//    TextView tv_type;
//    @BindView(R.id.rv)
//    LRecyclerView rv;
//    private List data = new ArrayList();
//    private List more = new ArrayList();
//    private ArrayList<JsonBean> options1Items = new ArrayList<>();
//    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
//    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
//    private MyProductAdapter mAdapter;
//    private LRecyclerViewAdapter mRecyclerViewAdapter;
//    private LinearLayoutManager linearLayoutManager;
//    private int pageCount = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_myproduct);
//        ButterKnife.bind(this);
//        common.createData(data);
//        initView();
//        initJsonData();
//    }
//
//    @OnClick(R.id.area)
//    public void showAreaPicker() {
//        ShowPickerView();
//    }
//
//    @OnClick(R.id.type)
//    public void showTypePicker() {
//        showLBPicker();
//    }
//
//    private void initView() {
////        common.hideObjs(this, new int[]{R.id.tv_right});
//        common.goBack(this);
////        common.hideObjs(this, new int[]{ R.id.tv_right});
//        ((ImageView)common.getToolView(this,R.id.tv_right)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        common.changeTitle(this,"我的产品");
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
////        initHeader();
//        initxRefresh();
//    }
//
//    /*地区picker*/
//    private void initJsonData() {//解析数据
//
//        /**
//         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
//         * 关键逻辑在于循环体
//         *
//         * */
//        String JsonData = getJson(this, "province.json");//获取assets目录下的json文件数据
//
//        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
//
//        /**
//         * 添加省份数据
//         *
//         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
//         * PickerView会通过getPickerViewText方法获取字符串显示出来。
//         */
//        options1Items = jsonBean;
//
//        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
//            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
//            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
//
//            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
//                String CityName = jsonBean.get(i).getCityList().get(c).getName();
//                CityList.add(CityName);//添加城市
//
//                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
//
//                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
//                if (jsonBean.get(i).getCityList().get(c).getArea() == null
//                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
//                    City_AreaList.add("");
//                } else {
//
//                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
//                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
//
//                        City_AreaList.add(AreaName);//添加该城市所有地区数据
//                    }
//                }
//                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
//            }
//
//            /**
//             * 添加城市数据
//             */
//            options2Items.add(CityList);
//
//            /**
//             * 添加地区数据
//             */
//            options3Items.add(Province_AreaList);
//        }
//
////        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
//
//    }
//
//    //从assert 读取json
//    public String getJson(Context context, String fileName) {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            AssetManager assetManager = context.getAssets();
//            BufferedReader bf = new BufferedReader(new InputStreamReader(
//                    assetManager.open(fileName)));
//            String line;
//            while ((line = bf.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringBuilder.toString();
//    }
//
//    //解析json
//    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
//        ArrayList<JsonBean> detail = new ArrayList<>();
//        try {
//            JSONArray data = new JSONArray(result);
//            Gson gson = new Gson();
//            for (int i = 0; i < data.length(); i++) {
//                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
//                detail.add(entity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
////            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
//        }
//        return detail;
//    }
//
//    private void ShowPickerView() {// 弹出选择器
//
//        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPickerViewText() +
//                        options2Items.get(options1).get(options2) +
//                        options3Items.get(options1).get(options2).get(options3);
//
////                Toast.makeText(MyNeed.this,tx,Toast.LENGTH_SHORT).show();
//                tv_area.setText(tx);
//            }
//        })
//                .setTitleText("城市选择")
//                .setSelectOptions(18, 0, 0)
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setContentTextSize(20)
//                .build();
//
//        /*pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
//        pvOptions.show();
//    }
//    /*地区picker*/
//
//    /*类别picker*/
//    public void showLBPicker() {
//        final ArrayList<String> options1 = new ArrayList<>();
//        //条件选择器
//        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int op, int option2, int options3, View v) {
//                //返回的分别是三个级别的选中位置
//                String tx = options1.get(op);
//                tv_type.setText(tx);
//            }
//        })
//                .build();
//
//        options1.add("全部");
//        options1.add("求购");
//        options1.add("招商");
//        pvOptions.setPicker(options1);
//        pvOptions.show();
//    }
//
//    /*类别picker*/
//    private void initxRefresh() {
//        mAdapter = new MyProductAdapter(this, data);
//        mRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
//        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rv.setLayoutManager(linearLayoutManager);
//        rv.setAdapter(mRecyclerViewAdapter);
//        mRecyclerViewAdapter.notifyDataSetChanged();
//        //加载样式
//        rv.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
//        rv.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
//        rv.setHeaderViewColor(R.color.colorAccent, R.color.white, R.color.colorPrimary);
////设置底部加载颜色
//        rv.setFooterViewColor(R.color.colorAccent, R.color.colorPrimary, R.color.white);
////设置底部加载文字提示
//        rv.setFooterViewHint("拼命加载中...", "--------我是有底线的--------", "点击重新加载");
//        rv.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageCount = 1;
//                refreshList(0);
//            }
//        });
//        rv.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                Log.e("loadmore", "" + list.size());
//                pageCount++;
//                refreshList(1);
//            }
//        });
//        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, final int i) {
//                common.launchActivity(MyProduct.this,MyProductDtail.class);
//            }
//        });
//    }
//
//    public void refreshList(int type) {
//        if (type == 0) {
//            List newData = new ArrayList();
//            common.createData(newData); //重新请求数据'
//
//            data.clear(); //又忘了
//            data.addAll(newData);
//            Log.e("refresh", data.size() + "");
//            mRecyclerViewAdapter.notifyDataSetChanged();
//
//        } else if (type == 1) {
//            List moreData = new ArrayList();//请求分页数据
//            common.createData(moreData);
//
//            more.clear();  //别忘了
//
//            more.addAll(moreData);
//            data.addAll(more);
//            Log.e("loadmore", data.size() + "");
//        }
//
//        rv.refreshComplete(pageCount);
//        mRecyclerViewAdapter.notifyDataSetChanged();
//    }
////    private void initHeader() {
////        header = LayoutInflater.from(this).inflate(R.layout.layout_head_myneed, (ViewGroup)findViewById(android.R.id.content), false);
//////        footer = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.home_footer, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
//////        et_search = (EditText) header.findViewById(R.id.search);
//////        et_search.clearFocus();
////        tv_area = (TextView) header.findViewById(R.id.tv_area);
////        tv_type = (TextView) header.findViewById(R.id.type);
////    }


}