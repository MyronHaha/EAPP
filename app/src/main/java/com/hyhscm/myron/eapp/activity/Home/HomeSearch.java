package com.hyhscm.myron.eapp.activity.Home;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.User.MyNeedDetail;
import com.hyhscm.myron.eapp.activity.User.NeedPublish;
import com.hyhscm.myron.eapp.data.Goods;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Jason on 2017/12/1.
 */

public class HomeSearch extends ListBaseBeanActivity<Goods> {
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
    @BindView(R.id.lastest)
    TextView tv_latest;
    private PopupWindow popup;

    //最新
    String selectDefault1 = "0";
    BaseViewHolder item_last3;
    //分类

    List<Integer> seletedArr = new ArrayList<>();//mutiple
    List<String> typeList = new ArrayList<>();//mutiple
    String append = "";//多选追加的字符串；
    List<Integer> seletedArr1 = new ArrayList<>();//mutiple
    List list2 = new ArrayList();
    //筛选
    //4-1   渠道；
    List<Integer> seletedArr4_1 = new ArrayList<>();//mutiple
    List<Integer> seletedArr4_2 = new ArrayList<>();//mutiple

    List<String> typeList4_1 = new ArrayList<>();//mutiple
    List<String> typeList4_2 = new ArrayList<>();//mutiple


    //科室
    private List<String> roomList = new ArrayList<>();//mutiple
    private List<Integer> seletedArr5 = new ArrayList<>();//mutiple

    private List<Labelinfo> data_room; //存储选择的科室，最后post请求
    private List<Labelinfo> data10;
    private List<Labelinfo> data5;

    public HomeSearch() {
        super(R.layout.layout_search);
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
                popup(5);
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
        initListView(R.layout.item_cp);
        hideObjs(new int[]{R.id.tv_right});
        changeTitle("产品检索");
    }

    /*地区picker*/
    protected void initData() {//解析数据
        initData(Url.GOODS, 10);
        list2 = labels(2);
        data10 = labels(10);
        data5 = labels(5);
        data_room = labels(4);

    }

    public void popup(final int i) {
        if (popup != null && popup.isShowing()) {
            return;
        }
        append = "";
        View root = null;
        if (i == 1) {
            final List<String> data = new ArrayList();
            data.add("最新发布");
            data.add("综合");
            data.add("推荐");
            root = this.getLayoutInflater().inflate(R.layout.item_rv, null);
            final RecyclerView rv1;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv);
            rv.setItemViewCacheSize(100); // 复用大小
//        rv3 = (RecyclerView) root.findViewById(R.id.lv3);
            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter adapter = new BaseQuickAdapter(HomeSearch.this, "myneed", R.layout.item_single, data) {
                @Override
                protected void convert(BaseViewHolder holder, Object item) {
                    holder.setText(R.id.tv, item.toString());
                    if (holder.getPosition() == Integer.parseInt(selectDefault1.trim())) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeSearch.this.getResources().getColor(R.color.colorPrimary));
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
                    tv.setTextColor(HomeSearch.this.getResources().getColor(R.color.colorPrimary));
                    CheckBox cb = (CheckBox) base.getView(R.id.cb);
                    cb.setBackgroundResource(R.drawable.shape_check_bg);
                    item_last3 = base;
                    selectDefault1 = position + "";
                    params.put("s", selectDefault1);
                    loadData();
                    popup.dismiss();
                    if (position == 0) {
                        tv_latest.setText("最新");
                    } else {
                        tv_latest.setText(data.get(position));
                    }

                }
            });
        } else if (i == 2) {
            root = this.getLayoutInflater().inflate(R.layout.layout_popup_gv, null);
            ((TextView) root.findViewById(R.id.title)).setText("分类");
            ((TextView) root.findViewById(R.id.seltype)).setText("可多选");
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    typeList.clear();
//                    seletedArr.clear();
//                    popup.dismiss();
                }
            });
            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    params.put("cid", appendList(typeList, ","));
                    loadData();
                    popup.dismiss();
                    typeList.clear();
                }
            });
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popup.dismiss();
                }
            });

            final List<Labelinfo> data = list2;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.gv);
            rv.setFocusableInTouchMode(false);
            rv.setFocusable(false);
            rv.setItemViewCacheSize(100); // 复用大小
            final GridLayoutManager manager1 = new GridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(HomeSearch.this, "myneed", R.layout.item_ptag, data) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(HomeSearch.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        if (!typeList.contains(item.getId() + ""))
                            typeList.add(item.getId() + "");
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
                    if (data.get(position).isSelected()) {
                        changeTextBg(false, tv);
                        typeList.remove(data.get(position).getId() + "");
                        data.get(position).setSelected(false);
                    } else {
                        changeTextBg(true, tv);
                        typeList.add(data.get(position).getId() + "");
                        data.get(position).setSelected(true);
                    }
                }
            });
        } else if (i == 3) {
            final List<Labelinfo> data1 = data10;
            final List<Labelinfo> data2 = data5;
            root = this.getLayoutInflater().inflate(R.layout.layout_popup_double_gv, null);
            ((TextView) root.findViewById(R.id.title)).setText("功能分类");
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
//                    typeList4_1.clear();
//                    typeList4_2.clear();

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
//
//                    Log.e("result", "" + append);
                    params.put("fid", appendList(typeList4_1, ",")); //功能类别
                    params.put("mcid", appendList(typeList4_2, ","));//管理类别
                    loadData();
                    popup.dismiss();
                    typeList4_1.clear();
                    typeList4_2.clear();

                }
            });
            final RecyclerView rv, rv2, rv3;
            rv = (RecyclerView) root.findViewById(R.id.gv);
            rv2 = (RecyclerView) root.findViewById(R.id.gv2);
            rv.setItemViewCacheSize(20); // 复用大小
            rv2.setItemViewCacheSize(20); // 复用大小

            final FullyGridLayoutManager manager1 = new FullyGridLayoutManager(this, 4);
            final FullyGridLayoutManager manager2 = new FullyGridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            rv2.setLayoutManager(manager2);
            //渠道
            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(HomeSearch.this, "myneed", R.layout.item_ptag, data1) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());
                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(HomeSearch.this.getResources().getColor(R.color.white));
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
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //医保
            BaseQuickAdapter<Labelinfo> adapter2 = new BaseQuickAdapter<Labelinfo>(HomeSearch.this, "myneed", R.layout.item_ptag, data2) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(HomeSearch.this.getResources().getColor(R.color.white));
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
            adapter2.notifyDataSetChanged();
        } else if (i == 5) {
//            科室
            root = this.getLayoutInflater().inflate(R.layout.layout_popup_gv, null);
            ((TextView) root.findViewById(R.id.title)).setText("科室");
            ((TextView) root.findViewById(R.id.seltype)).setText("可多选");
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    seletedArr5.clear();
                    popup.dismiss();
                }
            });
            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        params.put("did", appendList(roomList, ","));
                        loadData();
                    } catch (Exception e) {
                        L.e("iiiii", "null");
                    }
                    popup.dismiss();
                    roomList.clear();
                    seletedArr5.clear();
                }
            });

            final RecyclerView rv1;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.gv);
            rv.setItemViewCacheSize(50); // 复用大小
            final GridLayoutManager manager1 = new GridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter<Labelinfo> adapter = new BaseQuickAdapter<Labelinfo>(HomeSearch.this, "homesearch", R.layout.item_ptag, data_room) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    TextView tv = (TextView) holder.getView(R.id.tv_tag);
                    if (!item.isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        tv.setTextColor(HomeSearch.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        if (!roomList.contains(item.getId() + ""))
                            roomList.add(item.getId() + "");
                        Log.e("roomlistsize", "" + roomList.size());
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
                    if (data_room.get(position).isSelected()) {
                        changeTextBg(false, tv);
                        roomList.remove(data_room.get(position).getId() + "");
                        data_room.get(position).setSelected(false);
                    } else {
                        changeTextBg(true, tv);
                        roomList.add(data_room.get(position).getId() + "");
                        data_room.get(position).setSelected(true);
                        Log.e("seletedNow", "now");
                    }
                }
            });
        }

//popup new
        try {
            ScrollView sv = (ScrollView) root.findViewById(R.id.sv);
            sv.scrollTo(0, 0);
        } catch (Exception e) {

        }

        popup = new CustomedPopu(this).getPopupView(root);

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (i == 5) {

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
        if (i == 5) {
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
        } else if (type == 5) {
            seletedArr = seletedArr5;
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
            tv.setTextColor(HomeSearch.this.getResources().getColor(R.color.white));
            tv.setBackgroundResource(R.drawable.shape_tag_blue);
        }

    }

    @Override
    protected void bindView(BaseViewHolder holder, Goods item) {
        holder.setText(R.id.title, item.getName());
        holder.setText(R.id.tv_xh, item.getSpec());
        holder.setText(R.id.tv_gg, item.getCode());
        holder.setText(R.id.company, item.getManufacturer());
        holder.setImage(R.id.iv, Url.WEBPATH + item.getImg1());
    }

    @Override
    protected void itemClick(View v, int i, Goods item) {
        Intent intent = new Intent(HomeSearch.this, ProductDetail.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }
}