package com.hyhscm.myron.eapp.activity.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.hyhscm.myron.eapp.activity.Home.HomeNeed;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.adapter.MyneedAdapter;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.JsonBean;
import com.hyhscm.myron.eapp.data.Labelinfo;
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

import org.json.JSONArray;
import org.w3c.dom.Text;

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

public class MyNeed extends ListBaseBeanActivity<Demand> {
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
    @BindView(R.id.type)
    TextView tv_type;
    private ArrayList<Areas> options1Items = new ArrayList<>();
    private ArrayList<Areas> options2Items = new ArrayList<>();
    private PopupWindow popup;
    //list1 position  地区
    int pos1 = 0;
    int pos2 = 0;
    Areas firareas = new Areas();
    String province = "广东省";
    String city = "广州市";
    BaseViewHolder item_last, item_last2;
    List<Areas> cityList = new ArrayList<>();
    BaseQuickAdapter<Areas> adapter2;
    private List<Areas> data_area_city = new ArrayList<>();
    private List<Areas> data_area_pro = new ArrayList();
    //最新
    String selectDefault1 = "-1";
    BaseViewHolder item_last3;
    //leibie
    private List<Labelinfo> data_type = new ArrayList<>();
    String selectDefault3 = "-1";
    BaseViewHolder item_last4;
    //筛选
    List<Integer> seletedArr = new ArrayList<>();
    List<Labelinfo> data_sx = new ArrayList<>();
    List<Integer> roomList = new ArrayList<>();


    public MyNeed() {
        super(R.layout.layout_myneed);
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_myneed);
//        ButterKnife.bind(this);
//        common.createData(data);
//        initView();
//        initJsonData();
//    }

    @OnClick(R.id.iv_right)
    public void publishNeed() {
        launchActivity(NeedPublish.class);
    }

    protected void initView() {
        super.initView();
        initListView(R.layout.item_myneed);
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
                rv.forceToRefresh();
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
    }

    protected void initData() {
        initData(Url.DEMAND_MY, 6);
        data_sx = labels(4);
    }

    public void popup(final int i) {
        //地区
        View root = null;
        if (i == 0) {
            data_area_pro = province();
            root = this.getLayoutInflater().inflate(R.layout.layout_popup, null);
            final RecyclerView rv1, rv2;
            rv1 = (RecyclerView) root.findViewById(R.id.lv1);
            rv1.setItemViewCacheSize(100); // 复用大小
            rv2 = (RecyclerView) root.findViewById(R.id.lv2);
            rv2.setItemViewCacheSize(100);// 复用大小
            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
            rv1.setLayoutManager(manager1);
            rv2.setLayoutManager(new LinearLayoutManager(this));
            BaseQuickAdapter<Areas> adapter1 = new BaseQuickAdapter<Areas>(MyNeed.this, "myneed", R.layout.item_pop, data_area_pro) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (holder.getPosition() == pos1) {
                        item_last = holder;
                        holder.getView(R.id.ll_item).setBackgroundColor(MyNeed.this.getResources().getColor(R.color.page_bg));
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            //city
            adapter2 = new BaseQuickAdapter<Areas>(MyNeed.this, "myneed", R.layout.item_pop, data_area_city) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (pos2 == holder.getPosition()) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
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
                        item_last2.getView(R.id.ll_item).setBackgroundColor(MyNeed.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.tx_primary));
                    }
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
                    v.getView(R.id.ll_item).setBackgroundColor(MyNeed.this.getResources().getColor(R.color.page_bg));
                    item_last2 = v;
                    pos2 = position;
                    province = data_area_pro.get(pos1).getName();
                    city = data_area_city.get(pos2).getName();
                    params.put("aid", data_area_city.get(pos2).getId() + "");
                    tv_area.setText(data_area_city.get(pos2).getName());
                    loadData();
                    /*params.put("areaid","0");
                    loadData();*/
                    popup.dismiss();

                }
            });
            adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder v, int position) {
                    pos2 = 0;
//                    MyToast.makeText(MyNeed.this, data_area_pro.get(position).getName() + "--" + position, 1000).show();
//                item_last2 = null;
                    //not first
                    if (item_last != null) {
                        //上次ui
                        item_last.getView(R.id.ll_item).setBackgroundColor(MyNeed.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last.getView(R.id.tv);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.tx_primary));
                    }
                    if (item_last2 != null) {
                        //上次ui2
                        item_last2.getView(R.id.ll_item).setBackgroundColor(MyNeed.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.tx_primary));
                    }
                    v.getView(R.id.ll_item).setBackgroundColor(MyNeed.this.getResources().getColor(R.color.page_bg));
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
                    item_last = v;
                    pos1 = position;
                    data_area_city.clear();
                    //第一个 “全部”
                    firareas.setId(data_area_pro.get(position).getId());
                    firareas.setLevels(2);
                    firareas.setName("全部");
                    firareas.setPid(data_area_pro.get(position).getId());

                    data_area_city.add(firareas);
                    data_area_city.addAll(city(data_area_pro.get(position).getId()));
                    adapter2.notifyDataSetChanged();
                }
            });

            rv1.setAdapter(adapter1);
            rv2.setAdapter(adapter2);
        } else if (i == 1) {
            //最新
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
            BaseQuickAdapter adapter = new BaseQuickAdapter(MyNeed.this, "myneed", R.layout.item_single, data) {
                @Override
                protected void convert(BaseViewHolder holder, Object item) {
                    holder.setText(R.id.tv, item.toString());
                    if (holder.getPosition() == Integer.parseInt(selectDefault1.trim())) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
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
                    tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
                    CheckBox cb = (CheckBox) base.getView(R.id.cb);
                    cb.setBackgroundResource(R.drawable.shape_check_bg);
                    item_last3 = base;
                    selectDefault1 = position + "";
                    params.put("s", selectDefault1);
                    tv_lastest.setText(data.get(position).substring(0,2));
                    loadData();
                    popup.dismiss();
                }
            });
        } else if (i == 2) {
            //类别
            data_type = labels(11);
            root = this.getLayoutInflater().inflate(R.layout.item_rv, null);
            final RecyclerView rv1;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv);
            rv.setItemViewCacheSize(100); // 复用大小
            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter adapter = new BaseQuickAdapter<Labelinfo>(MyNeed.this, "myneed", R.layout.item_single, data_type) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv, item.getText());
                    if (data_type.get(holder.getPosition()).getId() == Integer.parseInt(selectDefault3.trim())) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
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
                    if (item_last4 != null) {
                        TextView tv = (TextView) item_last4.getView(R.id.tv);
                        tv.setTextColor(Color.parseColor("#4d4d4d"));
                        CheckBox cb = (CheckBox) item_last4.getView(R.id.cb);
                        cb.setBackgroundResource(R.drawable.shape_uncheck_bg);
                    }
                    TextView tv = (TextView) base.getView(R.id.tv);
                    tv.setTextColor(MyNeed.this.getResources().getColor(R.color.colorPrimary));
                    CheckBox cb = (CheckBox) base.getView(R.id.cb);
                    cb.setBackgroundResource(R.drawable.shape_check_bg);
                    item_last4 = base;
                    selectDefault3 = data_type.get(position).getId() + "";
                    params.put("st", selectDefault3);
                    tv_type.setText(data_type.get(position).getText());
                    loadData();
                    popup.dismiss();
                }
            });
        } else if (i == 3) {
            //筛选
            root = this.getLayoutInflater().inflate(R.layout.layout_popup_gv, null);
            ((TextView) root.findViewById(R.id.title)).setText("科室");
            ((TextView) root.findViewById(R.id.seltype)).setText("可多选");
            root.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
                }
            });
            root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    params.put("did", appendList(roomList, ","));
                    loadData();
//                    MyToast.makeText(MyNeed.this, appendList(roomList,",") + "----submit", 2000).show();
                    popup.dismiss();
                    roomList.clear();

                }
            });
            final RecyclerView rv1;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.gv);
            rv.setItemViewCacheSize(100); // 复用大小
            final GridLayoutManager manager1 = new GridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            final BaseQuickAdapter adapter = new BaseQuickAdapter<Labelinfo>(MyNeed.this, "myneed", R.layout.item_ptag, data_sx) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    if (!item.isSelected()) {
                        TextView tv = (TextView) holder.getView(R.id.tv_tag);
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        TextView tv = (TextView) holder.getView(R.id.tv_tag);
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        roomList.add(data_sx.get(holder.getPosition()).getId());

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
                    if (data_sx.get(position).isSelected()) {
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                        roomList.remove((Object) data_sx.get(position).getId());

                        data_sx.get(position).setSelected(false);
                    } else {
                        tv.setTextColor(MyNeed.this.getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.shape_tag_blue);
                        roomList.add(data_sx.get(position).getId());

                        data_sx.get(position).setSelected(true);
                    }
                }
            });
        }
        popup = new CustomedPopu(this).getPopupView(root);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (i == 0) {
                    //清除二级数据，防止下次与一级对应不上；
//                    data_area_city.clear();
//                    MyToast.makeText(MyNeed.this, province + "--" + city, 1000).show();
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
                    roomList.clear();
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
            int h =  (findViewById(R.id.d2)).getResources().getDisplayMetrics().heightPixels - rect.bottom;
            popup.setHeight(h);
            popup.showAsDropDown(findViewById(R.id.d2));
        }else{
            popup.showAsDropDown(findViewById(R.id.d2));
        }
//  -----------
        if (i == 0) {
            iv2.setRotation(180);
        } else if (i == 1) {
            iv.setRotation(180);
        } else if (i == 2) {
            iv3.setRotation(180);
        } else if (i == 2) {
            iv4.setRotation(180);
        }

    }

    @Override
    protected void itemClick(View v, int i, Demand item) {
        Intent intent = new Intent(this, MyNeedDetail.class);
        intent.putExtra("item", item);
        launchActivityWithIntent(intent);
    }

    @Override
    protected void bindView(BaseViewHolder holder, final Demand item) {

//        holder.setText(R.id.iv_user,item.getCname());
//        holder.setText(R.id.tv_ago,item.getCreationTime());
        try {
            holder.setText(R.id.tv_content, item.getContent());
            holder.setText(R.id.tv_ago, common.dataToString(item.getCreationTime()));
            holder.setText(R.id.tv_user, item.getCname());
            holder.setText(R.id.tv_liulan, item.getHit() + "");
            holder.setText(R.id.tv_title, item.getTitle());
            holder.setRoundImageView(R.id.iv_user, Url.IMAGEPATH + item.getUimg());
            holder.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showNormalDialog("确认"+"\n\r"+"删除该需求?", new promptListener() {
                        @Override
                        public void cancel(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void prompt(MaterialDialog dialog) {
                            HttpCore.demandDel(item.getId(), new IResultHandler<String>() {
                                @Override
                                public void onSuccess(Result<String> rs) {
                                    new PromptDialog(MyNeed.this).showSuccess("删除成功");
                                    rv.forceToRefresh();
                                }
                            });
                            dialog.dismiss();
                        }
                    });

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 判断多选；
    public boolean isSelected(int find, int type) {

        if (seletedArr.contains(find)) {
            return true;
        } else {
            return false;
        }
    }
}