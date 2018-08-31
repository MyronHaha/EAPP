package com.hyhscm.myron.eapp.activity.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.User.MyNeed;
import com.hyhscm.myron.eapp.activity.User.MyProduct;
import com.hyhscm.myron.eapp.activity.User.MyProductDtail;
import com.hyhscm.myron.eapp.activity.User.NeedPublish;
import com.hyhscm.myron.eapp.activity.User.ProductPublish;
import com.hyhscm.myron.eapp.adapter.TagAdapter;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.JsonBean;
import com.hyhscm.myron.eapp.data.Labelinfo;
import com.hyhscm.myron.eapp.data.Product;
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
import java.util.AbstractList;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
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


    public HomeProduct() {
        super(R.layout.layout_homeproduct);
    }

    @OnClick(R.id.iv_right)
    public void publishNeed() {
        launchActivity(ProductPublish.class);
    }

    protected void initData() {
        initData(Url.MERCHANTLIST, 8);

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
                      params.put("k",editable.toString());
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
        initListView(R.layout.item_homeproduct);
    }

    public void popup(final int i) {
        append = "";
        View root = null;
        if (i == 0) {
            options1Items = (ArrayList<Areas>) province();
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
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder v, int position) {

                    MyToast.makeText(HomeProduct.this, options1Items.get(position).getName() + "--" + position, 1000).show();
                    //not first
                    if (item_last != null) {
                        //上次ui
                        item_last.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    if (item_last2 != null) {
                        //上次ui2
                        item_last2.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    v.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.page_bg));
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
                    item_last = v;
                    cityList.clear();
                    options2Items = (ArrayList<Areas>) city(options1Items.get(position).getId());
                    //第一个 “全部”
                    firareas = new Areas();
                    firareas.setId(options1Items.get(position).getId());
                    firareas.setLevels(2);
                    firareas.setName("全部");
                    firareas.setPid(options1Items.get(position).getId());

                    cityList.add(firareas);
                    cityList.addAll(options2Items);
                    adapter2.notifyDataSetChanged();
                }
            });
            adapter2 = new BaseQuickAdapter<Areas>(HomeProduct.this, "myneed", R.layout.item_pop, cityList) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
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
                        item_last2.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.tx_primary));
                    }
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.colorPrimary));
                    v.getView(R.id.ll_item).setBackgroundColor(HomeProduct.this.getResources().getColor(R.color.page_bg));
                    item_last2 = v;

                    city = cityList.get(position).getName();
                    loadData();
                    params.put("aid",cityList.get(position).getId()+"");
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
            BaseQuickAdapter adapter = new BaseQuickAdapter(HomeProduct.this, "myneed", R.layout.item_single, data) {
                @Override
                protected void convert(BaseViewHolder holder, Object item) {
                    holder.setText(R.id.tv, item.toString());
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
                    typeList.clear();
                    seletedArr.clear();
                    popup.dismiss();
                }
            });
            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                             params.put("cid",appendList(typeList,","));
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
                    seletedArr.clear();
                }
            });
            final List<Labelinfo> data = labels(2);
            final RecyclerView rv1;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.gv);
            rv.setItemViewCacheSize(50); // 复用大小
            final GridLayoutManager manager1 = new GridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
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
                    if (isSelected(position, 1)) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                        typeList.remove(data.get(position));
                        seletedArr.remove((Object) position);
                    } else {
                        tv.setTextColor(HomeProduct.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        seletedArr.add(position);
                        typeList.add(data.get(position).getId()+"");
                    }
                }
            });
        } else if (i == 3) {
            //筛选
            final List<Labelinfo> data1 = labels(3);
            final List<Labelinfo> data2 = labels(7);
            final List<Labelinfo> data3 = labels(6);
            final List<Labelinfo> data4 = labels(4);
//            data1.add("医院临床");
//            data1.add("药店");
//            data1.add("第三终端");
//            data1.add("民营医院");
//            data1.add("电商");
//            data1.add("其他");
//            data2.add("医保");
//            data2.add("中标");
//            data2.add("不需中标");
//            data3.add("省级代理");
//            data3.add("市级代理");
//            data3.add("医院代理");
//            data3.add("医院上量");
            root = this.getLayoutInflater().inflate(R.layout.layout_popup_mutiple_gv, null);
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
                    typeList4_1.clear();
                    typeList4_2.clear();
                    typeList4_3.clear();
                    typeList4_4.clear();
                }
            });
            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    append = "";
                    for (String a : typeList4_1) {
                        append += a;
                    }
                    append += "\n" + "22222";
                    for (String b : typeList4_2) {
                        append += b;
                    }
                    append += "\n" + "3333333";
                    for (String c : typeList4_3) {
                        append += c;
                    }
                    Log.e("result", "" + append);
                    params.put("chid",appendList(typeList4_1,","));
                    params.put("mid",appendList(typeList4_2,","));
                    params.put("bid",appendList(typeList4_3,","));
                    params.put("did",appendList(typeList4_4,","));
                     loadData();
;                    popup.dismiss();
                    typeList4_1.clear();
                    typeList4_2.clear();
                    typeList4_3.clear();
                    typeList4_4.clear();
                }
            });
            final RecyclerView rv, rv2, rv3,rv4;
            rv = (RecyclerView) root.findViewById(R.id.gv);
            rv2 = (RecyclerView) root.findViewById(R.id.gv2);
            rv3 = (RecyclerView) root.findViewById(R.id.gv3);
            rv4 =(RecyclerView) root.findViewById(R.id.gv4);
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

      final BaseQuickAdapter<Labelinfo>      adapter = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data1) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder base, int position) {
                    TextView tv = (TextView) base.getView(R.id.tv_tag);
                    if (isSelected(position, 41)) {
                        changeTextBg(false, tv);
                        typeList4_1.remove(data1.get(position).getId()+"");
                        seletedArr4_1.remove((Object) position);
                    } else {
                        changeTextBg(true, tv);
                        seletedArr4_1.add(position);
                        typeList4_1.add(data1.get(position).getId()+"");
                    }

                }
            });

            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //医保
            BaseQuickAdapter<Labelinfo> adapter2 = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data2) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
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
                        typeList4_2.remove(data2.get(position).getId()+"");
                        seletedArr4_2.remove((Object) position);
                    } else {
                        changeTextBg(true, tv);
                        seletedArr4_2.add(position);
                        typeList4_2.add(data2.get(position).getId()+"");
                    }
                }
            });
            rv2.setAdapter(adapter2);

            //科室
            BaseQuickAdapter<Labelinfo> adapter4 = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data4) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
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
                    if (isSelected(position, 44)) {
                        changeTextBg(false, tv);
                        typeList4_4.remove(data4.get(position).getId()+"");
                        seletedArr4_4.remove((Object) position);
                    } else {
                        changeTextBg(true, tv);
                        typeList4_4.add(data4.get(position).getId()+"");
                        seletedArr4_4.add(position);
                    }
                }
            });
            rv4.setAdapter(adapter4);

            //业务
            BaseQuickAdapter<Labelinfo> adapter3 = new BaseQuickAdapter<Labelinfo>(HomeProduct.this, "myneed", R.layout.item_ptag, data3) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
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
                    if (isSelected(position, 43)) {
                        changeTextBg(false, tv);
                        typeList4_3.remove(data3.get(position).getId()+"");
                        seletedArr4_3.remove((Object) position);
                    } else {
                        changeTextBg(true, tv);
                        typeList4_3.add(data3.get(position).getId()+"");
                        seletedArr4_3.add(position);
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
                    cityList.clear();
                    options1Items.clear();
                    options2Items.clear();
                    MyToast.makeText(HomeProduct.this, province + "--" + city, 1000).show();
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
        popup.showAsDropDown(findViewById(R.id.ll_filter));
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
    protected void bindView(BaseViewHolder holder, Product item) {
        try {
            ImageView iv  = (ImageView) holder.getView(R.id.iv);
            holder.setText(R.id.tv_info, item.getName());
            holder.setText(R.id.tv_ago, common.dataToString(item.getCreationTime()) + "");
            holder.setText(R.id.tv_gg, item.getSpec());
            holder.setText(R.id.tv_adv, item.getAdvantage());
            Glide.with(EAPPApplication.getInstance()).load(Url.IMAGEPATH+splitString(item.getImg(), ";").get(0))
                    .error(R.mipmap.img_nothingbg2)
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .placeholder(R.mipmap.img_nothingbg2)
                    .fitCenter()
                    .skipMemoryCache(true)
                    .into(iv);

            List<String> tagList = new ArrayList<>();
            tagList.clear();
            if (item.getChannelName().equals("")) {
                tagList.add("暂无标签");
            } else {
                tagList .addAll(splitString(item.getChannelName(), "\\s+")) ;
            }
            RecyclerView rv = (RecyclerView) holder.getView(R.id.gridView);
            TagAdapter adapter = new TagAdapter(HomeProduct.this, tagList);
            GridLayoutManager manager = new GridLayoutManager(HomeProduct.this, 1, GridLayoutManager.HORIZONTAL, false);
            rv.setFocusable(false);
            rv.setFocusableInTouchMode(false);
            rv.setLayoutManager(manager);
            rv.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    protected void itemClick(View v, int i, Product item) {
        Intent intent = new Intent(HomeProduct.this, MyProductDtail.class);
        intent.putExtra("item",item);
        launchActivityWithIntent(intent);
    }
}