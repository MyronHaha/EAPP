package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.Home.HomeNeed;
import com.hyhscm.myron.eapp.activity.Home.ProductDetail;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Labelinfo;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UploadRe;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;
import com.hyhscm.myron.eapp.view.FullyLinearLayoutManager;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/12/5.
 * 发布招商
 */

public class ProductPublish extends BaseActivity {
    List<String> photos = new ArrayList<>();
    List<String>
            imgPathResults = new ArrayList<>();
    Product product = new Product();
    @BindView(R.id.iv_add)
    ImageView iv_add;
    @BindView(R.id.gv14)
    RecyclerView nineGrid;
    @BindView(R.id.rv)
    RecyclerView tag_adg;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.city)
    TextView area;
    @BindView(R.id.tv_10)
    TextView tv_room;
    @BindView(R.id.gv)
    RecyclerView rv1;

    @BindView(R.id.rv1)
    RecyclerView rv_preTag;

    @BindView(R.id.gv2)
    RecyclerView rv2;
    @BindView(R.id.gv3)
    RecyclerView rv3;
    @BindView(R.id.gv4)
    RecyclerView rv4;
    @BindView(R.id.gv5)
    RecyclerView rv5;
    @BindView(R.id.gv6)
    RecyclerView rv6;
    @BindView(R.id.gv7)
    RecyclerView rv7;
    @BindView(R.id.tv_2)
    EditText et_name;
    @BindView(R.id.tv_3)
    EditText et_gg;
    @BindView(R.id.tv_4)
    EditText et_xh;
    @BindView(R.id.tv_5)
    EditText et_factory;
    @BindView(R.id.tv_6)
    EditText et_pzwh;
    @BindView(R.id.tv_7)
    EditText et_zszc;
    @BindView(R.id.tv_8)
    EditText et_contract;
    @BindView(R.id.tv_9)
    EditText et_phonecall;

    //

    //    tv_2 name  tv_3 gg  tv_4 xh   tv_5 factory  tv_6 pzwh  tv_7 zszc
//    tv_8 contract tv_9 phonecall  tv_10 keshi result  rv tag
    // 自定义
    String result = "";
    private BaseQuickAdapter<String> adapter;
    List<String> tempList = new ArrayList<>();
    List<String> cusTagList = new ArrayList();
    private List seletedArr = new ArrayList();
    //类型 单选
    private List<String> typeList = new ArrayList(); //id
    private List tagList = new ArrayList(); //点击位置记录
    private String selectDefault1 = "";
    //常用分类
    private List<String> ctypeList = new ArrayList(); //id
    private List tagList2 = new ArrayList();
    //渠道
    private List<String> wayList = new ArrayList(); //id
    private List tagList3 = new ArrayList();
    //医保属性
    private List<String> ybaoList = new ArrayList(); //id
    private List tagList4 = new ArrayList();
    //业务类型
    private List<String> ytypeList = new ArrayList<>();
    private List tagList5 = new ArrayList();
    //管理类别 单选
    private List<String> gtypeList = new ArrayList<>();
    private List tagList6 = new ArrayList();
    private BaseViewHolder item_last6 = null;
    private String selectDefault6 = "";
    //生产地区 单选
    private BaseViewHolder item_last7 = null;
    private String selectDefault7 = "";
    private List<String> areatypeList = new ArrayList<>();
    private List tagList7 = new ArrayList();
    //prelist advantage

    private List<Labelinfo> data2 = new ArrayList<>(); // 常用分类
    private List<Labelinfo> data1 = new ArrayList<>();//类型；
    private List<Labelinfo> data3 = new ArrayList<>();
    private List<Labelinfo> data4 = new ArrayList<>();
    private List<Labelinfo> data5 = new ArrayList<>();
    private List<Labelinfo> data6 = new ArrayList<>();
    private List<Labelinfo> data7 = new ArrayList<>();
    private List<Labelinfo> data8 = new ArrayList<>();
    private List<String> preChoose = new ArrayList<>();
    private PromptDialog dialog;

    public ProductPublish() {
        super(R.layout.layout_publish_product);
    }

    public static ProductPublish mActivty = null;
    public Handler productHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                ProductPublish.this.finish();
            }
        }
    };

    private Product editProduct = null;
    BaseQuickAdapter<Labelinfo> adapter1;
    BaseQuickAdapter<Labelinfo> adapter2;
    BaseQuickAdapter<Labelinfo> adapter3;
    BaseQuickAdapter<Labelinfo> adapter4;
    BaseQuickAdapter<Labelinfo> adapter5;
    BaseQuickAdapter<Labelinfo> adapter6;
    BaseQuickAdapter<Labelinfo> adapter7;
    BaseQuickAdapter<Labelinfo> adapter8;

    @Override
    protected void initData() {
        super.initData();
        try {
            editProduct = (Product) getIntent().getSerializableExtra("item");
            if (editProduct != null) {
                changeTitle("产品编辑");
                ((TextView) getToolView(R.id.tv_right)).setText("重新上架");
                // 常用分类  mutiple
                ctypeList.addAll(splitString(editProduct.getCclassid() + "", ","));
                refreshLabelInfoSeleted(data2, ctypeList, tagList2, new ArrayList<String>());
//                L.e(tagList2.get(0)+"---current_position2");
                adapter2.notifyDataSetChanged();
                // top 分类
                typeList.add(editProduct.getClassId() + "");
                selectDefault1 = editProduct.getClassId() + "";
                refreshLabelInfoSeleted(data1, typeList, tagList, new ArrayList<String>());
//                L.e(tagList.get(0) + "---current_position1" + "classid" + editProduct.getClassId());
                adapter1.notifyDataSetChanged();
                //渠道

                wayList.addAll(splitString(editProduct.getChannelid(), ","));
                refreshLabelInfoSeleted(data3, wayList, tagList3, new ArrayList<String>());
                adapter3.notifyDataSetChanged();
                //医保属性
                ybaoList.add(editProduct.getMedid());
                refreshLabelInfoSeleted(data4, ybaoList, tagList4, new ArrayList<String>());
                adapter4.notifyDataSetChanged();
                //业务类型
                ytypeList.addAll(splitString(editProduct.getBtypeid(), ","));
                refreshLabelInfoSeleted(data5, ytypeList, tagList5, new ArrayList<String>());
                adapter5.notifyDataSetChanged();
                //管理类别
                selectDefault6 = editProduct.getCategoryid();
                gtypeList.add(editProduct.getCategoryid());
                refreshLabelInfoSeleted(data6, gtypeList, tagList6, new ArrayList<String>());
                adapter6.notifyDataSetChanged();
                //生产地区
                selectDefault7 = editProduct.getPtypeid();
                areatypeList.add(editProduct.getPtypeid());
                refreshLabelInfoSeleted(data7, areatypeList, tagList7, new ArrayList<String>());
                adapter7.notifyDataSetChanged();
                //name
                et_name.setText(editProduct.getName());
                et_gg.setText(editProduct.getSpec());
                et_xh.setText(editProduct.getCode());
                et_factory.setText(editProduct.getManufacturer());
                et_pzwh.setText(editProduct.getWarrant());
                et_zszc.setText(editProduct.getPolicy());
                et_contract.setText(editProduct.getLinkman());
                et_phonecall.setText(editProduct.getLinkmanTel());

                // area  provice city ect...
                // get the LIst<Area> by id to given cityList
                refreshAreaSeletedById(2, areas(), splitString(editProduct.getAreasid(), ","), citysList);

                area.setText("已选中" + citysList.size() + "个区域");
                roomArr = editProduct.getDeptsid(); // roomArr 是科室id
                tv_room.setText(editProduct.getDepts()); // show  selected room String
                //优势标签；
                //1 可选
                List[] listArray = getSelectedAdvantages(data8, splitString(editProduct.getAdvantage(), ","));
                preChoose.addAll(listArray[0]);
                adapter8.notifyDataSetChanged();
                //2自定义
                cusTagList.addAll(listArray[1]);
                adapter.notifyDataSetChanged();
                //图片
                imgPathResults.clear();
                imgPathResults = splitString(editProduct.getImg(),";");
                if(imgPathResults.size()>0){
//                    graAdapter.notifyDataSetChanged();
                    L.e("size:"+imgPathResults.size()+"  "+imgPathResults.get(0));
                }
            }
            initPhotoView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initView() {
        super.initView();
        mActivty = this;
        dialog = new PromptDialog(this);
        changeTitle("招商发布");
        ((TextView) getToolView(R.id.tv_right)).setText("发布");
//        common.hideObjs(this, new int[]{R.id.tv_right});
        initChooseList();
        initRv_adv();

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

    public void initChooseList() {
        data1 = labels(1);
        data2 = labels(2);
        data3 = labels(3);
        data4 = labels(7);
        data5 = labels(6);
        data6 = labels(5);
        data7 = labels(8);
        data8 = labels(9);

        rv1.setItemViewCacheSize(20);
        rv2.setItemViewCacheSize(20);
        rv3.setItemViewCacheSize(20);
        rv4.setItemViewCacheSize(20);
        rv5.setItemViewCacheSize(20);
        rv6.setItemViewCacheSize(20);
        rv7.setItemViewCacheSize(20);
        rv_preTag.setItemViewCacheSize(20);
        final FullyGridLayoutManager manager7 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager1 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager2 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager3 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager4 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager5 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager6 = new FullyGridLayoutManager(this, 4);
        final FullyGridLayoutManager manager8 = new FullyGridLayoutManager(this, 4);
        rv1.setLayoutManager(manager1);
        rv2.setLayoutManager(manager2);
        rv3.setLayoutManager(manager3);
        rv4.setLayoutManager(manager4);
        rv5.setLayoutManager(manager5);
        rv6.setLayoutManager(manager6);
        rv7.setLayoutManager(manager7);
        rv_preTag.setLayoutManager(manager8);
        adapter8 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data8) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                holder.setText(R.id.tv_tag, item.getText());
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                if (item.isSelected()) {
                    tv.setTextColor(ProductPublish.this.getResources().getColor(R.color.white));
                    tv.setBackgroundResource(R.drawable.shape_tag_blue);
                } else {
                    tv.setTextColor(Color.parseColor("#808080"));
                    tv.setBackgroundResource(R.drawable.shape_tag_grey);
                }
            }
        };
        adapter8.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                if (data8.get(position).isSelected()) {
                    data8.get(position).setSelected(false);
                    preChoose.remove(data8.get(position).getText());
                } else {
                    data8.get(position).setSelected(true);
                    preChoose.add(data8.get(position).getText());
                }
                adapter8.notifyDataSetChanged();
            }
        });
        rv_preTag.setAdapter(adapter8);
        //类型
        adapter1 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data1) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                holder.setText(R.id.tv_tag, item.getText());
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                if (isSelected(holder.getPosition(), 1)) {
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
        adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                typeList.clear();
                tagList.clear();
                if (isSelected(position, 1)) {
                    changeTextBg(false, tv);
//                    typeList.remove(data1.get(position).getId() + "");
//                    tagList.remove((Object) position);
                } else {
                    changeTextBg(true, tv);
                    tagList.add(position);
                    typeList.add(data1.get(position).getId() + "");
                    selectDefault1 = data1.get(position).getId() + "";
                }
                adapter1.notifyDataSetChanged();
            }
        });
        rv1.setAdapter(adapter1);
        //常用分类
        adapter2 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data2) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                holder.setText(R.id.tv_tag, item.getText());
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                if (isSelected(holder.getPosition(), 2)) {
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
                if (isSelected(position, 2)) {
                    changeTextBg(false, tv);
                    ctypeList.remove(data2.get(position).getId() + "");
                    tagList2.remove((Object) position);
                } else {
                    changeTextBg(true, tv);
                    tagList2.add(position);
                    ctypeList.add(data2.get(position).getId() + "");
                }
            }
        });
        rv2.setAdapter(adapter2);
        //渠道3
        adapter3 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data3) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                tv.setText(item.getText());
                if (isSelected(holder.getPosition(), 3)) {
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
        adapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                if (isSelected(position, 3)) {
                    changeTextBg(false, tv);
                    wayList.remove(data3.get(position).getId() + "");
                    tagList3.remove((Object) position);
                } else {
                    changeTextBg(true, tv);
                    tagList3.add(position);
                    wayList.add(data3.get(position).getId() + "");
                }
            }
        });
        rv3.setAdapter(adapter3);
        //医保4
        adapter4 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data4) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                tv.setText(item.getText());
                if (isSelected(holder.getPosition(), 4)) {
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
        adapter4.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                if (isSelected(position, 4)) {
                    changeTextBg(false, tv);
                    ybaoList.remove(data4.get(position).getId() + "");
                    tagList4.remove((Object) position);
                } else {
                    changeTextBg(true, tv);
                    tagList4.add(position);
                    ybaoList.add(data4.get(position).getId() + "");
                }
            }
        });
        rv4.setAdapter(adapter4);
//业务类型
        adapter5 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data5) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                tv.setText(item.getText());
                if (isSelected(holder.getPosition(), 5)) {
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
        adapter5.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                if (isSelected(position, 5)) {
                    changeTextBg(false, tv);
                    ytypeList.remove(data5.get(position).getId() + "");
                    tagList5.remove((Object) position);
                } else {
                    changeTextBg(true, tv);
                    tagList5.add(position);
                    ytypeList.add(data5.get(position).getId() + "");
                }
            }
        });
        rv5.setAdapter(adapter5);
        //管理
        adapter6 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data6) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                tv.setText(item.getText());
                tv.setText(item.getText());
                if (isSelected(holder.getPosition(), 6)) {
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
        adapter6.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                gtypeList.clear();
                tagList6.clear();
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                if (isSelected(position, 6)) {
                    changeTextBg(false, tv);
//                    gtypeList.remove(data6.get(position).getId() + "");
//                    tagList6.remove((Object) position);
                } else {
                    selectDefault6 = data6.get(position).getId() + "";
                    changeTextBg(true, tv);
                    tagList6.add(position);
                    gtypeList.add(data6.get(position).getId() + "");
                }
                adapter6.notifyDataSetChanged();
//
//                if (item_last6 != null) {
//                    TextView tv = (TextView) item_last6.getView(R.id.tv_tag);
//                    tv.setTextColor(Color.parseColor("#808080"));
//                    tv.setBackgroundResource(R.drawable.shape_tag_grey);
//                }
//
//                TextView tv = (TextView) base.getView(R.id.tv_tag);
//                tv.setTextColor(ProductPublish.this.getResources().getColor(R.color.white));
//                tv.setBackgroundResource(R.drawable.shape_tag_blue);
//                item_last6 = base;
//

            }
        });
        rv6.setAdapter(adapter6);
        //地区
        adapter7 = new BaseQuickAdapter<Labelinfo>(ProductPublish.this, "", R.layout.item_ptag, data7) {
            @Override
            protected void convert(BaseViewHolder holder, Labelinfo item) {
                TextView tv = (TextView) holder.getView(R.id.tv_tag);
                tv.setText(item.getText());
                if (isSelected(holder.getPosition(), 7)) {
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
        adapter7.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder base, int position) {
                tagList7.clear();
                areatypeList.clear();
                TextView tv = (TextView) base.getView(R.id.tv_tag);
                if (isSelected(position, 7)) {
                    changeTextBg(false, tv);
//                    areatypeList.remove(data7.get(position).getId() + "");
//                    tagList7.remove((Object) position);
                } else {
                    selectDefault7 = data7.get(position).getId() + "";
                    changeTextBg(true, tv);
                    tagList7.add(position);
                    areatypeList.add(data7.get(position).getId() + "");
                }
                adapter7.notifyDataSetChanged();

//
//
//                if (item_last7 != null) {
//                    TextView tv = (TextView) item_last7.getView(R.id.tv_tag);
//                    tv.setTextColor(Color.parseColor("#808080"));
//                    tv.setBackgroundResource(R.drawable.shape_tag_grey);
//                }
//
//                TextView tv = (TextView) base.getView(R.id.tv_tag);
//                tv.setTextColor(ProductPublish.this.getResources().getColor(R.color.white));
//                tv.setBackgroundResource(R.drawable.shape_tag_blue);
//                item_last7 = base;
//                selectDefault7 = data1.get(position).getId() + "";

            }
        });
        rv7.setAdapter(adapter7);
    }

    @OnClick(R.id.iv_add)
    public void add() {
        if(imgPathResults.size()==3){
            MyToast.makeText(ProductPublish.this,"最多只能选择3张图片！",1500).show();
            return;
        }
        GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(3-imgPathResults.size())
                .singlePhoto(false)
                .hintOfPick("最多还可选"+(3-imgPathResults.size())+"张")
                .filterMimeTypes(new String[]{})
                .build();
        GalleryActivity.openActivity(ProductPublish.this, 800, config);
    }
//
//    @OnClick(R.id.iv_type)
//    public void showTypePicker() {
//        showLBPicker();
//    }
//

private SingleItemAdapter  graAdapter;
    public void initPhotoView() {
        graAdapter =new SingleItemAdapter(this, imgPathResults);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false);
        nineGrid.setLayoutManager(manager);
        nineGrid.setAdapter(graAdapter);
    }

    class SingleItemAdapter extends RecyclerView.Adapter<SingleItemAdapter.MyViewHolder> {
        private Context mContext;
        private List data = new ArrayList<>();//数据

        //适配器初始化
        public SingleItemAdapter(Context context, List datas) {
            mContext = context;
            this.data = datas;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //根据item类别加载不同ViewHolder
            Log.e("onCreateViewHolder", "");
            View view = LayoutInflater.from(mContext
            ).inflate(R.layout.item_photo, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);
//
//        //给布局设置点击和长点击监听
//        view.setOnClickListener(this);
//        view.setOnLongClickListener(this);
            return holder;
        }

        //bindData
        @Override
        public void onBindViewHolder(MyViewHolder viewholder, final int i) {
            if (imgPathResults.size() == 3) {
                iv_add.setVisibility(View.GONE);
                nineGrid.setPadding(110, 0, 0, 0);
                nineGrid.postInvalidate();
            } else {
                nineGrid.setPadding(0, 0, 0, 0);
                nineGrid.postInvalidate();
            }
            try{
                Glide.with(ProductPublish.this)
                        .load(Url.IMAGEPATH+imgPathResults.get(i))
                        .dontAnimate()
                        .into(viewholder.iv);
            }catch(Exception e){
//                viewholder.iv.setImageURI(Uri.parse(photos.get(i)));
             L.e(e.toString());
            }

        }

        @Override
        public int getItemCount() {
            return data.size();//获取数据的个数
        }


        //自定义ViewHolder，用于加载图片
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv;
            ImageView de;

            public MyViewHolder(View view) {
                super(view);
                iv = (ImageView) view.findViewById(R.id.imageView);
                de = (ImageView) view.findViewById(R.id.iv_delete);
              de.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        iv.setImageURI(null);
                        data.remove(getPosition());
//                        photos.remove(getPosition());
//                        imgPathResults.remove(i);
                        notifyItemRemoved(getPosition());
                        notifyItemRangeChanged(0, data.size());
//                        demand.setImg(appendImagePath());
                        if(iv_add.getVisibility() != View.VISIBLE){
                            iv_add.setVisibility(View.VISIBLE);
                        }

                    }

                });
            }


        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }

    @OnClick(R.id.tv_right)
    public void submit() {
        dialog.showInfo("提交中...");
//        leibie = type.getText().toString();
//        title = et_2.getText().toString();
//        content = et_3.getText().toString();
//        phoneNum = et_4.getText().toString();
//        area = et_5.getText().toString();
//// 提交接口
        if (checkNull()) {
            if (preChoose.size() > 0) {
                cusTagList.addAll(preChoose);
            }
            product.setAdvantage(appendList(cusTagList, ","));
            product.setBtypeid(appendList(ytypeList, ",")); //业务类型
            product.setCategoryid(selectDefault6);//guanli
            product.setCclassid(appendList(ctypeList, ","));
            product.setChannelid(appendList(wayList, ","));
            product.setClassId(Integer.valueOf(selectDefault1)); // 类别
            product.setName(et_name.getText().toString());
            product.setSpec(et_gg.getText().toString());
            product.setCode(et_xh.getText().toString());
            product.setManufacturer(et_factory.getText().toString());
            product.setWarrant(et_pzwh.getText().toString());
            product.setPolicy(et_zszc.getText().toString());
            product.setLinkman(et_contract.getText().toString());
            product.setLinkmanTel(et_phonecall.getText().toString());
            product.setMedid(appendList(ybaoList, ","));
            product.setPtypeid(selectDefault7);
            product.setImg(appendList(imgPathResults, ";"));

            product.setAreasid(appendListArea(citysList, ","));
            product.setDeptsid(roomArr);

            if (editProduct!=null) {
               HttpCore.product_invisible(product, product.getId()+"", new IResultHandler<Product>() {
                   @Override
                   public void onSuccess(Result<Product> rs) {
                       if(rs.getSuccess()){
                           dialog.showSuccess("上架成功!");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProductPublish.this.finish();
                            }
                        }, 500);
                       }
                   }
               });
            } else{
                HttpCore.productAdd(product, new IResultHandler<Product>() {
                    @Override
                    public void onSuccess(Result<Product> rs) {
                        if (rs.getSuccess()) {
                            dialog.showSuccess("发布成功!");
                            View view = ProductPublish.this.getLayoutInflater().inflate(R.layout.wx_share_dialog_success, null);
                            preChoose.clear();
                            showCustomDialog(view, (Product) rs.getBiz(), 1, false);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                ProductPublish.this.finish();
//                            }
//                        }, 1000);
                        } else {
                            dialog.showError(rs.getMsg());
                        }
                    }
                });
        }
        }
    }

    // 判断多选；
    public boolean isSelected(int find, int type) {
        if (type == 1) {
            seletedArr = tagList;
        } else if (type == 2) {
            seletedArr = tagList2;
        } else if (type == 3) {
            seletedArr = tagList3;
        } else if (type == 4) {
            seletedArr = tagList4;
        } else if (type == 5) {
            seletedArr = tagList5;
        } else if (type == 6) {
            seletedArr = tagList6;
        } else if (type == 7) {
            seletedArr = tagList7;
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
            tv.setTextColor(this.getResources().getColor(R.color.white));
            tv.setBackgroundResource(R.drawable.shape_tag_blue);
        }

    }

    // custom advantage tags
    private void initRv_adv() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);
        tag_adg.setLayoutManager(manager);
//        cusTagList.add("易上量");
//        cusTagList.add("空间大");
//        cusTagList.add("质量好");
//        cusTagList.add("不需中标");


        adapter = new BaseQuickAdapter<String>(ProductPublish.this, "", R.layout.item_ptag_delete, cusTagList) {
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

    @OnClick(R.id.r13)
    public void chooseCity() {
        Intent intent = new Intent(ProductPublish.this, ChooseCanDe.class);
        intent.putExtra("type", "区域");
        if (citysList.size() > 0) {
            intent.putExtra("selectedCities", (Serializable) citysList);
        }
        startActivityForResult(intent, 105);
    }

    @OnClick(R.id.r10)
    public void chooseRoom() {
        Intent intent = new Intent(ProductPublish.this, ChooseFirst.class);
        intent.putExtra("type", "科室");
        startActivityForResult(intent, 103);
    }

    String roomArr = "";
    List citysList = new ArrayList();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 200) {  //代理城市 多选
            citysList = (List) data.getSerializableExtra("result");
            area.setText("" + citysList.size() + "");
        } else if (resultCode == 112) {
            roomArr = data.getStringExtra("roomarr");
            L.e(roomArr + "");
            tv_room.setText("" + ((List) data.getStringArrayListExtra("room")).size());

        } else if (requestCode == 800) {
            try {

                photos = ((List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS));
                for (String str : photos) {
                    HttpCore.UploadImage(str.trim(), new IResultHandler<UploadRe>() {
                        @Override
                        public void onSuccess(Result<UploadRe> rs) {
                            L.e(rs.getBiz().getPath());
                            if (rs.getSuccess()) {
                                imgPathResults.add(rs.getBiz().getPath());
                                graAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

//                graAdapter.notifyDataSetChanged();
                //
            } catch (Exception e) {
                Log.e("null", "null");
            }

        }
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
            Toast.makeText(ProductPublish.this, "标签：" + err + "字符长度大于4", Toast.LENGTH_SHORT).show();
        }
        cusTagList.addAll(tempList);
        adapter.notifyDataSetChanged();
        et.setText("");
        et.clearFocus();
    }

    public boolean checkNull() {
        if (selectDefault1.equals("")) {
            dialog.showWarn("请选择类型！");
            return false;
        } else if (ctypeList.size() == 0) {
            dialog.showWarn("请选择常用分类！");
            return false;
        } else if (wayList.size() == 0) {
            dialog.showWarn("请选择渠道！");
            return false;
        }
//        else if(ybaoList.size()==0){
//            dialog.showWarn("请选择常医保属性！");
//        }else if(ytypeList.size()==0){
//           dialog.showWarn("请选择业务类型！");
//        }else if(selectDefault6.equals("")){
//            dialog.showWarn("请选择管理类别！");
//        }else if(selectDefault7.equals("")){
//            dialog.showWarn("请选择生产地区！");
//        }
        else if (et_name.getText().equals("")) {
            dialog.showWarn("产品名称不能为空！");
            return false;
        } else if (et_xh.getText().toString().equals("")) {
            dialog.showWarn("产品型号不能为空！");
            return false;
        } else if (et_factory.getText().toString().equals("")) {
            dialog.showWarn("生产厂家不能为空！");
            return false;
        } else if (et_pzwh.getText().toString().equals("")) {
            dialog.showWarn("产品批准文号不能为空！");
            return false;
        } else if (et_zszc.getText().toString().equals("")) {
            dialog.showWarn("产品招商政策不能为空！");
            return false;
        } else if (et_contract.getText().toString().equals("")) {
            dialog.showWarn("联系人不能为空！");
            return false;
        } else if (et_phonecall.getText().toString().equals("")) {
            dialog.showWarn("联系电话不能为空！");
            return false;
        } else if (roomArr.equals("")) {
            dialog.showWarn("请选择科室！");
            return false;
        } else if (citysList.size() == 0) {
            dialog.showWarn("请选择区域！");
            return false;
        }
        return true;
    }

}