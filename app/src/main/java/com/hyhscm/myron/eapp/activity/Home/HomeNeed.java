package com.hyhscm.myron.eapp.activity.Home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.design.MaterialDialog;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Im.SysMsg;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.activity.User.MyCorn;
import com.hyhscm.myron.eapp.activity.User.MyNeedDetail;
import com.hyhscm.myron.eapp.activity.User.MyNeedDetail;
import com.hyhscm.myron.eapp.activity.User.NeedPublish;
import com.hyhscm.myron.eapp.adapter.MyneedAdapter;
import com.hyhscm.myron.eapp.adapter.TagAdapter;
import com.hyhscm.myron.eapp.data.Areas;
import com.hyhscm.myron.eapp.data.CommitResult;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.JsonBean;
import com.hyhscm.myron.eapp.data.Labelinfo;
import com.hyhscm.myron.eapp.data.ListResult;
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
import com.hyphenate.chat.EMGCMListenerService;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hyhscm.myron.eapp.activity.Im.ContractManager.adapter;


/**
 * Created by Jason on 2017/12/1.
 */

public class HomeNeed extends ListBaseBeanActivity<Demand> {
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
//    String  k,  String  st,  String  aid,  String  did,  String  uid,  String  s
//    k:关键字搜索    cid:常用分类    fid:功能分类    did:科室分类    chid:渠道    mcid:管理类型      aid:区域    s:排序
//    t:类型        uid:用户id    pid:产地    bid:业务类型  mid:医保  st:需求分类

    private PopupWindow popup;


    //list1 position  地区
    int pos1 = -1;
    int pos2 = 0;
    String province = "广东省";
    String city = "广州市";
    BaseViewHolder item_last, item_last2;
    Areas firareas = new Areas();//全部
    //    List<String> data2 = new ArrayList<>();
    BaseQuickAdapter<Areas> adapter2;
    List<Areas> data_area_pro = new ArrayList<>();
    List<Areas> data_area_city = new ArrayList<>();
    //最新
    String selectDefault1 = "0";
    BaseViewHolder item_last3;
    //leibie   参数传 t  0 求购中，1 已解决
    List<Labelinfo> data_type = new ArrayList<>();
    String selectDefault3 = "-100";
    BaseViewHolder item_last4;
    //筛选
    List<Integer> seletedArr = new ArrayList<>();
    List<Labelinfo> data_sx = new ArrayList<>();
    List<Integer> roomList = new ArrayList<>();
    //
    private int enterType;
    public static HomeNeed mInstance = null;

    public HomeNeed() {
        super(R.layout.layout_homeneed);
        mInstance = this;
    }

    @OnClick(R.id.iv_right)
    public void publishNeed() {
        launchActivity(NeedPublish.class);
    }


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



        // 分享后回调刷新界面


//            if(HttpCore.isShared&&HttpCore.isTopShared){
//                L.e("分享成功 ，置顶，刷新界面");
//                MyToast.makeText(_mActivity, "置顶成功！", 1000).show();
//                HttpCore.isShared = false;
//                HttpCore.isMakedTop = false;
//            }
//            else {
//                if(HttpCore.isShared&&!HttpCore.isMakedTop){
//                    MyToast.makeText(_mActivity, "置顶失败！", 1000).show();
//
//                }
//                HttpCore.isShared = false;
//                HttpCore.isMakedTop = false ;
//            }




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
                rv.forceToRefresh();
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
        initListView(R.layout.item_homeneed2);   //item 布局
    }

    @Override
    protected void initData() {
        enterType = getIntent().getIntExtra("from", 2); //2 从最新商机 ；4.从 产品代理
        if (enterType == 2) {
            params.put("st", 1 + "");// 求购中
            tv_type.setText("求购中");
        } else {
            params.put("st", 2 + "");// 求购中
            tv_type.setText("招商中");
        }


        params.put("s", selectDefault1);//默认 最新
        initData(Url.DEMAND, 15);

        data_sx = labels(4);
        data_area_pro = province();
        data_type = labels(11);


        //改变默认选中
        if (data_type.size() > 0) {
            if (selectDefault3.equals("-100") && enterType == 2) {
                selectDefault3 = data_type.get(0).getId() + "";
            } else {
                selectDefault3 = data_type.get(1).getId() + "";
            }
        }

    }

    @Override
    protected void itemClick(View v, int i, Demand item) {
//        Intent intent = new Intent(this,MyNeedDetail.class);
//        intent.putExtra("item",item);
//        launchActivityWithIntent(intent);
        getDemandById(item.getId() + "");
    }

    public void popup(final int i) {
        //地区
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
            BaseQuickAdapter<Areas> adapter1 = new BaseQuickAdapter<Areas>(HomeNeed.this, "myneed", R.layout.item_pop, data_area_pro) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (holder.getPosition() == pos1) {
                        item_last = holder;
                        holder.getView(R.id.ll_item).setBackgroundColor(HomeNeed.this.getResources().getColor(R.color.page_bg));
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            //city
            adapter2 = new BaseQuickAdapter<Areas>(HomeNeed.this, "myneed", R.layout.item_pop, data_area_city) {
                @Override
                protected void convert(BaseViewHolder holder, Areas item) {
                    holder.setText(R.id.tv, item.getName());
                    if (pos2 == holder.getPosition()) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
                        item_last2 = holder;
                    }
                }

                @Override
                public void onBindViewHolder(BaseViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                }
            };
            adapter1.notifyDataSetChanged();
            adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder v, int position) {
                    //not first
                    if (item_last2 != null) {
                        //上次ui
//                        item_last2.getView(R.id.ll_item).setBackgroundColor(HomeNeed.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.tx_primary));
                    }
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
//                    v.getView(R.id.ll_item).setBackgroundColor(HomeNeed.this.getResources().getColor(R.color.page_bg));
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
//                    MyToast.makeText(HomeNeed.this, data_area_pro.get(position).getName() + "--" + position, 1000).show();
//                item_last2 = null;
                    //not first
                    if (item_last != null) {
                        //上次ui
                        item_last.getView(R.id.ll_item).setBackgroundColor(HomeNeed.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last.getView(R.id.tv);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.tx_primary));
                    }
                    if (item_last2 != null) {
                        //上次ui2
//                        item_last2.getView(R.id.ll_item).setBackgroundColor(HomeNeed.this.getResources().getColor(R.color.white));
                        TextView tv = (TextView) item_last2.getView(R.id.tv);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.tx_primary));
                    }
                    v.getView(R.id.ll_item).setBackgroundColor(HomeNeed.this.getResources().getColor(R.color.page_bg));
                    TextView tv = (TextView) v.getView(R.id.tv);
                    tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
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
            if (pos1 != -1) {
                rv1.smoothScrollToPosition(pos1);
            }
            if (pos2 != 0) {
                rv2.smoothScrollToPosition(pos2);
            }
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
            BaseQuickAdapter adapter = new BaseQuickAdapter(HomeNeed.this, "myneed", R.layout.item_single, data) {
                @Override
                protected void convert(BaseViewHolder holder, Object item) {
                    holder.setText(R.id.tv, item.toString());
                    if (holder.getPosition() == Integer.parseInt(selectDefault1.trim())) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
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
                    tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
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

            //商机 默认 type  求购中  st 不传label id ，传123


            //类别
            root = this.getLayoutInflater().inflate(R.layout.item_rv, null);
            final RecyclerView rv1;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv);
            rv.setItemViewCacheSize(100); // 复用大小
            final LinearLayoutManager manager1 = new LinearLayoutManager(this);
            rv.setLayoutManager(manager1);
            BaseQuickAdapter adapter = new BaseQuickAdapter<Labelinfo>(HomeNeed.this, "myneed", R.layout.item_single, data_type) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv, item.getText());
                    // 根据label id 判断
                    if (data_type.get(holder.getPosition()).getId() == Integer.parseInt(selectDefault3.trim())) {
                        TextView tv = (TextView) holder.getView(R.id.tv);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
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
                    tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.colorPrimary));
                    CheckBox cb = (CheckBox) base.getView(R.id.cb);
                    cb.setBackgroundResource(R.drawable.shape_check_bg);
                    item_last4 = base;
                    selectDefault3 = data_type.get(position).getId() + "";
                    params.put("st", position + 1 + "");
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
//                    MyToast.makeText(HomeNeed.this, appendList(roomList,",") + "----submit", 2000).show();
                    popup.dismiss();
                    roomList.clear();

                }
            });
            final RecyclerView rv1;
            RecyclerView rv = (RecyclerView) root.findViewById(R.id.gv);
            rv.setItemViewCacheSize(100); // 复用大小
            final GridLayoutManager manager1 = new GridLayoutManager(this, 4);
            rv.setLayoutManager(manager1);
            final BaseQuickAdapter adapter = new BaseQuickAdapter<Labelinfo>(HomeNeed.this, "myneed", R.layout.item_ptag, data_sx) {
                @Override
                protected void convert(BaseViewHolder holder, Labelinfo item) {
                    holder.setText(R.id.tv_tag, item.getText());

                    if (!item.isSelected()) {
                        TextView tv = (TextView) holder.getView(R.id.tv_tag);
                        tv.setTextColor(Color.parseColor("#808080"));
                        tv.setBackgroundResource(R.drawable.shape_tag_grey);
                    } else {
                        TextView tv = (TextView) holder.getView(R.id.tv_tag);
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.white));
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
                        tv.setTextColor(HomeNeed.this.getResources().getColor(R.color.white));
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
//                    MyToast.makeText(HomeNeed.this, province + "--" + city, 1000).show();
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
//        popup.showAsDropDown(findViewById(R.id.d2));
        //show popu

        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            findViewById(R.id.d2).getGlobalVisibleRect(rect);
            int h = (findViewById(R.id.d2)).getResources().getDisplayMetrics().heightPixels - rect.bottom;
            popup.setHeight(h);
            popup.showAsDropDown(findViewById(R.id.d2));
        } else {
            popup.showAsDropDown(findViewById(R.id.d2));
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            int[] a = new int[2];
//            findViewById(R.id.d2).getLocationInWindow(a);
//            popup.showAtLocation(findViewById(R.id.d2), Gravity.NO_GRAVITY, 0, a[1] +    findViewById(R.id.d2).getHeight() + 0);
//        } else {
//            popup.showAsDropDown(findViewById(R.id.d2));
//        }
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
    protected void bindView(final BaseViewHolder holder, final Demand item) {

//        holder.setText(R.id.iv_user,item.getCname());
//        holder.setText(R.id.tv_ago,item.getCreationTime());
//        @BindView(R.id.iv_user)
//        ImageView user;
//        @BindView(R.id.tv_content)
//        TextView tv_content;
//        @BindView(R.id.tv_user)
//        TextView tv_user;
//        @BindView(R.id.tv_ago)
//        TextView tv_ago;
//        @BindView(R.id.tv_see)
//        TextView tv_see;
//        @BindView(R.id.rv_areas)
//        RecyclerView rv;
        try {
            holder.setText(R.id.tv_content,item.getContent() );
            holder.setText(R.id.tv_user, item.getCname());
            holder.setRoundImageView(R.id.iv_user, Url.WEBPATH + item.getUimg());
            // 可能nullpoiont 进exception 最后进行；
            if(holder.getView(R.id.tv_ago)!=null){
                holder.setText(R.id.tv_ago, common.dataToString(item.getDisplayTime()));
            }
            if(holder.getView(R.id.tv_see)!=null){
                holder.setText(R.id.tv_see, item.getHit() + "");
            }
            if(item.getImg()!=""&&holder.getView(R.id.iv_adv)!=null){
                holder.setImage(R.id.iv_adv,Url.IMAGEPATH+item.getImg());
            }
            // ding tuijian huore icon, show or invisible
            if(item.getTops()>0){
                holder.getView(R.id.iv_ding).setVisibility(View.VISIBLE);
            }
            if(item.getIsCommend()>0){
                holder.getView(R.id.iv_tuijian).setVisibility(View.VISIBLE);
            }
            if(item.getHit()>50){
                holder.getView(R.id.iv_huore).setVisibility(View.VISIBLE);
            }

            // 展开
             TextView tv_expan = null;
            String content =  item.getContent().trim();
            if(holder.getView(R.id.tv_expan) != null){
                tv_expan = (TextView) holder.getView(R.id.tv_expan);
                if(content.length()>50){
                    tv_expan.setVisibility(View.VISIBLE);
                    String con = (item.getContent().substring(0,20)).concat("...");
                    holder.setText(R.id.tv_content,con );
                }else {
                    tv_expan.setVisibility(View.GONE);
                }
                final TextView finalTv_expan = tv_expan;
                tv_expan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finalTv_expan.setVisibility(View.GONE);
                        holder.setText(R.id.tv_content,item.getContent().trim() );
                    }
                });
            }




            // 广告图片、、、
            //地区标签
            if(holder.getView(R.id.rv_areas)!=null){
                RecyclerView    rv_area = (RecyclerView) holder.getView(R.id.rv_areas);
                List<String> tagList = new ArrayList<>();
                if (item.getAreas().equals("")) {
                    tagList.add("暂无地区");
                } else {
                    tagList.addAll(splitString(item.getAreas(), "\\s+"));
                }
                TagAdapter adapter = new TagAdapter(this, tagList);
                FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);

                rv_area.setFocusable(false);
                rv_area.setFocusableInTouchMode(false);
                rv_area.setLayoutManager(manager);
                rv_area.setAdapter(adapter);
            }

//            holder.setText(R.id.tv_title, item.getTitle());
//            holder.setRoundImageView(R.id.iv_user, Url.WEBPATH + item.getUimg());
            // 状态标签
//            if (item.getType() == 0) {
//                Glide.with(this).load(R.mipmap.need_status1).into((ImageView) holder.getView(R.id.iv_state));
//            } else if (item.getType() == 1) {
//                Glide.with(this).load(R.mipmap.need_status2).into((ImageView) holder.getView(R.id.iv_state));
//            }


            //下拉更多
            holder.getView(R.id.moreclick).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (HttpCore.isLogin) {

                        showStringListDialog(new String[]{"置顶", "分享"}, new MaterialDialog.OnClickListener() {
                            @Override
                            public boolean onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    final MaterialDialog dialog = new MaterialDialog.Builder(HomeNeed.this).create();
                                    View.OnClickListener listener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            switch (view.getId()) {
                                                case R.id.bt_share:
                                                    View v = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
                                                    showCustomDialog(v, item, 1, true);
//                                                MyToast.makeText(_mActivity, "分享一次", 1000).show();
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
//                                        MyToast.makeText(_mActivity, "分享", 1000).show();
                                    View view = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
                                    showCustomDialog(view, item, 1, false);
                                }
                                return false;
                            }
                        });
                    } else {
                        AutoLogin(new IResultHandler() {
                            @Override
                            public void onSuccess(Result rs) {

                            }
                        });
                    }

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

    public void getDemandById(final String param) {
        dialog.showInfo("加载中...");
        HttpCore.getDemandById(param, new IResultHandler() {
            @Override
            public void onSuccess(Result rs) {
                dialog.dismiss();
                if (rs.getSuccess()) {
                    launchActivityWithIntent(new Intent(HomeNeed.this, MyNeedDetail.class).putExtra("item", (Demand) rs.getBiz()));
                } else {
                    AutoLogin(new IResultHandler() {
                        @Override
                        public void onSuccess(Result rs) {
                            if (rs.getSuccess()) {
                                HttpCore.getDemandById(param, new IResultHandler() {
                                    @Override
                                    public void onSuccess(Result rs) {
                                        dialog.dismiss();
                                        if (rs.getSuccess()) {
                                            launchActivityWithIntent(new Intent(HomeNeed.this, MyNeedDetail.class).putExtra("item", (Demand) rs.getBiz()));
                                        } else {
                                            dialog.showWarn("登录超时，请重新登录！");
                                            launchActivity(LoginActivity.class);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }


    // 普通 金豆置顶
    public void makeTop(Demand item) {
        HttpCore.makeTop(item.getId() + "", "1", "", new IResultHandler<CommitResult>() {
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


    public void setTextView(TextView view,boolean isExpaned){
        if(isExpaned){
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = view.getWidth();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
        }else {
            view.setTextIsSelectable(true);
            view.setMaxEms(50);
        }
    }
}