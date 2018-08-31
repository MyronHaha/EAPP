package com.hyhscm.myron.eapp.activity.Home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.design.MaterialDialog;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.User.MyCorn;
import com.hyhscm.myron.eapp.activity.User.MyProductDtail;
import com.hyhscm.myron.eapp.activity.User.ProductPublish;
import com.hyhscm.myron.eapp.adapter.TagAdapter;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.CommitResult;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Labelinfo;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.CustomedPopu;
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Jason on 2017/12/1.
 */

public class HomeProduct extends ListBaseBeanActivity<Product> {
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
    private List<Labelinfo> info2;
    private List<Integer> seletedArr = new ArrayList<>();//mutiple
    private List<String> typeList = new ArrayList<>();//mutiple
    private List<Integer> seletedArr1 = new ArrayList<>();//mutiple
    //筛选
    //4-1   渠道；
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

    public HomeProduct() {
        super(R.layout.layout_homeproduct);
        mInstance = this;
    }

    @OnClick(R.id.iv_right)
    public void publishNeed() {
        launchActivity(ProductPublish.class);
    }

    protected void initData() {
        super.initData();
        if (getIntent().getStringExtra("from") != null) {
            String from = getIntent().getStringExtra("from");
/*产品分类页面：1.item点击 type ，sourceid ，最新s = 0
*               2.右方箭头 type， 最新s=0
* */
            if (getIntent().getExtras() != null && from.equals("ProductCategory")) {
                Bundle bundle = getIntent().getExtras();
                params.clear();
                if (bundle.getString("type") != null) {
                    params.put("cid", bundle.getString("type"));
                }
//                if(bundle.getString("sourceId")!=null){
//                    params.put("cid",bundle.getString("sourceId"));
//                }

            }
        } else {
            params.put("s", selectDefault1);
        }
        initData(Url.MERCHANTLIST, 15);
        options1Items = (ArrayList<Areas>) province();
        info2 = labels(2);
        data1.addAll(labels(3));

        data2 = labels(7);
        data3 = labels(6);
        data4 = labels(4);
    }

    public static HomeProduct mInstance = null;
    public Handler reFreshDatahandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean isMakedtop = (boolean) msg.obj;
            if (isMakedtop) {
                MyToast.makeText(_mActivity, "置顶成功！", 1000).show();
                loadData();
            } else {
                MyToast.makeText(_mActivity, "置顶失败！", 1000).show();
                loadData();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        if(HttpCore.isShared&&HttpCore.isTopShared){
//            L.e("分享成功 ，置顶，刷新界面");
//            loadData();
//            MyToast.makeText(_mActivity, "置顶成功！", 1000).show();
//            HttpCore.isShared = false;
//            HttpCore.isMakedTop = false;
//        }
//        else {
//            if(!HttpCore.isMakedTop){
//                MyToast.makeText(_mActivity, "置顶失败！", 1000).show();
//
//            }
//            HttpCore.isShared = false;
//            HttpCore.isMakedTop = false ;
//        }
//        rv.forceToRefresh();
    }


    protected void initView() {
        super.initView();
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
        findViewById(R.id.rl1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(0);
            }
        });
        findViewById(R.id.rl3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(1);
            }
        });
        findViewById(R.id.rl2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(2);
            }
        });
        findViewById(R.id.rl4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(3);
            }
        });
        initListView(R.layout.item_homeproduct2);
        d = new ProgressDialog(this);
    }

    public void popup(final int i) {
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
            BaseQuickAdapter<Areas> adapter1 = new BaseQuickAdapter<Areas>(HomeProduct.this, "myneed", R.layout.item_pop, options1Items) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (holder.getPosition() == pos1) {
                        item_last = holder;
                        holder.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.page_bg));
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
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
//                    MyToast.makeText(HomeProduct.this, options1Items.get(position).getName() + "--" + position, 1000).show();
                    //not first
                    if (item_last != null) {
                        //上次ui
                        item_last.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    if (item_last2 != null) {
                        //上次ui2
//                        item_last2.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    v.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.page_bg));
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
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
            adapter2 = new BaseQuickAdapter<Areas>(HomeProduct.this, "myneed", R.layout.item_pop, cityList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (pos2 == holder.getPosition()) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
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
//                        item_last2.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
//                    v.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.page_bg));
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
            if (pos1 != -1) {
                rv1.smoothScrollToPosition(pos1);
            }
            if (pos2 != 0) {
                rv2.smoothScrollToPosition(pos2);
            }
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
            BaseQuickAdapter adapter = new BaseQuickAdapter(HomeProduct.this, "myneed", R.layout.item_single, data) {
                @Override
                protected void convert(BaseViewHolder holder, Object item) {
                    holder.setText(R.id.tv, item.toString());
                    if (holder.getPosition() == Integer.parseInt(selectDefault1.trim())) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
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
                    tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
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
                        L.e("cid", appendList(typeList, ","));

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
            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data) {
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
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
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
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
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
//                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
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
            final BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data1) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    L.e(this.toString());
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);


                    } else {

                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
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
            BaseQuickAdapter<Labelinfo> adapter2 = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data2) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
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
            BaseQuickAdapter<Labelinfo> adapter4 = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data4) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
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
            BaseQuickAdapter<Labelinfo> adapter3 = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data3) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
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
//                    MyToast.makeText(HomeProduct.this, province + "--" + city, 1000).show();
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
            tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
            tv.setBackgroundResource(R.drawable.shape_tag_blue);
        }

    }

    @Override
    protected void bindView(final BaseViewHolder holder, final Product item) {
        try {

//            // advertisment
//            if(holder.getView(R.id.tv_content)!=null){
//                holder.setText(R.id.tv_content,item.getContent());
//            }
//            if (holder.getView(R.id.tv_user)!=null) {
//                holder.setText(R.id.tv_user,item.getName());
//            }
//            if(holder.getView(R.id.iv_adv)!=null){
//                holder.setImage(R.id.adv,Url.IMAGEPATH+splitString(item.getImg(),";").get(0));
//            }
            holder.setIsRecyclable(false);    //0518
            if (holder.getView(R.id.tv_user) != null) {
                holder.setText(R.id.tv_user, item.getName());
            }

            if (holder.getView(R.id.tv_content) != null) {
                holder.setText(R.id.tv_content, item.getName());
            }

//            holder.setText(R.id.tv_info, item.getName());
            if (holder.getView(R.id.tv_ago) != null) {
                holder.setText(R.id.tv_ago, common.dataToString(item.getDisplayTime()));
            }

//            holder.setText(R.id.tv_gg, item.getSpec());
            if (holder.getView(R.id.tv_adv) != null) {
                String adv = item.getAdvantage();
                if (adv.trim().equals("")) {
                    holder.setText(R.id.tv_adv, String.format(getResources().getString(R.string.product_adv), "暂无"));
                } else {
                    holder.setText(R.id.tv_adv, String.format(getResources().getString(R.string.product_adv), adv));
                }
            }

//            holder.setText(R.id.tv_adv, String.format(getResources().getString(R.string.product_adv),item.getAdvantage()));
            if (holder.getView(R.id.iv_product) != null && !item.getImg().equals("")) {

                String url = "";
                if (splitString(item.getImg(), ";").size() == 0) {
                    try {
                        url = item.getImg();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    url = (String) splitString(item.getImg(), ";").get(0);
                }
                Glide.with(this).load(Url.IMAGEPATH + url)
                        .error(R.mipmap.img_nothingbg2)
                        .placeholder(R.mipmap.img_nothingbg2)
                        .dontAnimate()
                        .into((ImageView) holder.getView(R.id.iv_product));
            }

            if (holder.getView(R.id.gridView) != null) {
                List<String> tagList = new ArrayList<>();
                tagList.clear();
                if (item.getChannelName().equals("")) {
                    tagList.add("暂无标签");
                } else {
                    tagList.addAll(splitString(item.getChannelName(), "\\s+"));
                }
                RecyclerView rv = (RecyclerView) holder.getView(R.id.gridView);
                TagAdapter adapter = new TagAdapter(HomeProduct.this, tagList);
                GridLayoutManager manager = new GridLayoutManager(HomeProduct.this, 1, GridLayoutManager.HORIZONTAL, false);
                rv.setFocusable(false);
                rv.setFocusableInTouchMode(false);
                rv.setLayoutManager(manager);
                rv.setAdapter(adapter);
            }
//            holder.setImage(R.id.iv_product, Url.IMAGEPATH + splitString(item.getImg(), ";").get(0));

//            adapter.notifyDataSetChanged();

            //下拉更多
            if (holder.getView(R.id.moreclick) != null) {
                holder.getView(R.id.moreclick).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showStringListDialog(new String[]{"置顶", "分享"}, new MaterialDialog.OnClickListener() {
                            @Override
                            public boolean onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    final MaterialDialog dialog = new MaterialDialog.Builder(HomeProduct.this).create();
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
                                } else {
                                    View v = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
                                    showCustomDialog(v, item, 1, false);
                                }
                                return false;
                            }
                        });
                    }
                });
            }


            // 展开
            TextView tv_expan = null;
            String content = item.getContent().trim();
            if (holder.getView(R.id.tv_expan) != null) {
                tv_expan = (TextView) holder.getView(R.id.tv_expan);
                if (content.length() > 50) {
                    tv_expan.setVisibility(View.VISIBLE);
                    String con = (item.getContent().substring(0, 20)).concat("...");
                    holder.setText(R.id.tv_content, con);
                } else {
                    tv_expan.setVisibility(View.GONE);
                }
                final TextView finalTv_expan = tv_expan;
                tv_expan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finalTv_expan.setVisibility(View.GONE);
                        holder.setText(R.id.tv_content, item.getContent().trim());
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    protected void itemClick(View v, int i, Product item) {
        Intent intent = new Intent(HomeProduct.this, MyProductDtail.class);
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


}


