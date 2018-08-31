package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UploadRe;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.UriToPathUtil;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/12/5.
 * 发布需求
 */

public class NeedPublish extends BaseActivity {
    List<String> photos = new ArrayList<>();
    @BindView(R.id.gv)
    RecyclerView nineGrid;
    @BindView(R.id.iv_add)
    ImageView iv_add;
    @BindView(R.id.tv_1)
    TextView type;
    @BindView(R.id.tv_2)
    EditText et_2;
    @BindView(R.id.tv_3)
    EditText et_3;
    @BindView(R.id.tv_4)
    EditText et_4;
    @BindView(R.id.se5)
    TextView et_5;
    @BindView(R.id.select_room)
    TextView tv_room;
    @BindView(R.id.tv_8)
    EditText et_8;

    //商机拆分
    @BindView(R.id.check_group)
    RadioGroup group;
    @BindView(R.id.ck1)
    RadioButton ck1;
    @BindView(R.id.ck2)
    RadioButton ck2;
    public static String param_sjcf = ""; //商机拆分参数

    public static String leibie = "";
    public static String title = "";
    public static String content = "";
    public static String phoneNum = "";
    public static String area = "";
    public static String roomStr = "";
    public static String contracts = "";//联系信息
    public int cityId = -1;
    public int catogoryPos = 0;
    PromptDialog dialog;
    Demand demand = new Demand();
    List<String> imgPathResults = new ArrayList<>();
    int publishType = -1;
    private List citysList = new ArrayList();
    public static NeedPublish mActivty = null;

    //record isconvenient
    private boolean isConvenient = false;
    @BindView(R.id.bt_isCon)
    ImageButton bt_isCon;
    @OnClick(R.id.bt_isCon)
    public void setConvenient(){
        isConvenient = !isConvenient;
        if(isConvenient){
            bt_isCon.setImageResource(R.mipmap.icon_yxz);
        }else{
            bt_isCon.setImageResource(R.mipmap.icon_wxz);
        }

        L.e("isConvenient:"+isConvenient);
    }
    public Handler needHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                NeedPublish.this.finish();
            }
        }
    };

    public NeedPublish() {
        super(R.layout.layout_publish_need);
    }
    protected void initView() {
        super.initView();
        mActivty = this;
        //快速入口
        try {
            publishType = getIntent().getIntExtra("type", -1);
            if (publishType != -1) {
                findViewById(R.id.iv_type).setVisibility(View.GONE);
            }
            if (publishType == 0) { //求购
                type.setText("求购");
                catogoryPos = 0;
            } else {
                type.setText("招商");
                catogoryPos = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        changeTitle("发布商机");
//        hideObjs(new int[]{R.id.tv_right});
        ((TextView) getToolView(R.id.tv_right)).setText("发布");
        dialog = new PromptDialog(this);

        //商机拆分
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.ck1) {
                    ck1.setBackgroundResource(R.drawable.shape_tag_blue);
                    ck1.setTextColor(Color.parseColor("#ffffff"));
                    param_sjcf = "1";
                } else {
                    ck1.setBackgroundResource(R.drawable.shape_tag_grey);
                    ck1.setTextColor(Color.parseColor("#808080"));

                }
                if (id == R.id.ck2) {
                    ck2.setBackgroundResource(R.drawable.shape_tag_blue);
                    ck2.setTextColor(Color.parseColor("#ffffff"));
                    param_sjcf = "2";
                } else {
                    ck2.setBackgroundResource(R.drawable.shape_tag_grey);
                    ck2.setTextColor(Color.parseColor("#808080"));
                }

            }

        });
    }

    @OnClick(R.id.iv_add)
    public void add() {
        GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(3)
                .singlePhoto(false)
                .hintOfPick("最多可选3张")
                .filterMimeTypes(new String[]{}) //"image/jpeg"
                .build();
        GalleryActivity.openActivity(NeedPublish.this, 200, config);
    }

    @OnClick(R.id.iv_type)
    public void showTypePicker() {
        showLBPicker();
    }

    @OnClick(R.id.r5)
    public void chooseCity() {
        Intent intent = new Intent(NeedPublish.this, ChooseCanDe.class);
        intent.putExtra("type", "区域");
        startActivityForResult(intent, 105);
    }

    @OnClick(R.id.r7)
    public void chooseRoom() {
        Intent intent = new Intent(NeedPublish.this, ChooseFirst.class);
        intent.putExtra("type", "科室");
        launchActivityWithIntent(intent, 101);
    }

    //接受返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 200) {
            try {
                photos = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
                if (imgPathResults.size() != 3) {

                }
                for (String str : photos) {
                    HttpCore.UploadImage(str.trim(), new IResultHandler<UploadRe>() {
                        @Override
                        public void onSuccess(Result<UploadRe> rs) {
                            L.e(rs.getBiz().getPath());
                            imgPathResults.add(rs.getBiz().getPath());
                        }
                    });
                }
                //
            } catch (Exception e) {
                Log.e("null", "null");
            }
//            L.e("size", photos.size() + "");
            initGvData();
        } else if (data != null) {
            if (requestCode == 105 && resultCode == 200) {

                citysList = (List) data.getSerializableExtra("result");
                et_5.setText("" + citysList.size() + "");
                demand.setAreasid(appendListArea(citysList, ","));

//                L.e(data.getStringExtra("province") + data.getStringExtra("city") + data.getIntExtra("city_id", -1));
//                et_5.setText(data.getStringExtra("province") + data.getStringExtra("city"));
//                cityId = data.getIntExtra("city_id", -1);
            } else if (requestCode == 101 && resultCode == 112) {
                List list = data.getStringArrayListExtra("room");
                String str = appendList(data.getStringArrayListExtra("room"), "  ");
                tv_room.setText(str);
                roomStr = data.getStringExtra("roomarr");
//                MyToast.makeText(NeedPublish.this, data.getStringExtra("roomarr"), 1000).show();
            }
        }
    }

    @OnClick(R.id.tv_right)
    public void submit() {
        leibie = type.getText().toString();
        title = et_2.getText().toString();
        content = et_3.getText().toString();
        phoneNum = et_4.getText().toString();
        area = et_5.getText().toString();
        contracts = et_8.getText().toString();
// 提交接口
        if (checkNotNull()) {

            demand.setType(catogoryPos);
            demand.setImg(appendImagePath());
//            demand.setAreasid(cityId + "");
            demand.setDeptsid(roomStr);
            demand.setTitle(title);
            demand.setContent(content);
            demand.setPhone(phoneNum);
            demand.setContacts(contracts);

            HttpCore.demandAdd(demand, new IResultHandler<Demand>() {
                @Override
                public void onSuccess(Result rs) {
                    L.e(leibie + "\n" + title + "\n" + content + "\n" + phoneNum + "\n" + cityId + "\n" + roomStr + "\n" + contracts + "\n" + appendImagePath());
                    if (rs.getSuccess()) {
                        new PromptDialog(NeedPublish.this).showSuccess("需求提交成功！");
                        View view = NeedPublish.this.getLayoutInflater().inflate(R.layout.wx_share_dialog_success, null);
                        showCustomDialog(view, (Demand) rs.getBiz(), 1, false);

                    } else {

                        L.e("demandadd" + rs.getMsg() + "code:" + rs.getErrorCode());
                    }


                }
            });
        }

    }

    public void initGvData() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        nineGrid.setLayoutManager(manager);
        nineGrid.setAdapter(new SingleItemAdapter(this, photos));
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
        public void onBindViewHolder(MyViewHolder viewholder, int i) {

            if (photos.size() == 3) {
                iv_add.setVisibility(View.GONE);
                nineGrid.setPadding((int) getResources().getDimension(R.dimen.x110), 0, 0, 0);
                nineGrid.postInvalidate();
            } else {
                iv_add.setVisibility(View.VISIBLE);
                nineGrid.setPadding(0, 0, 0, 0);
                nineGrid.postInvalidate();
            }
            viewholder.iv.setImageURI(Uri.parse(photos.get(i)));
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

                        iv.setImageURI(null);
                        data.remove(getPosition());
                        imgPathResults.remove(getPosition());

                        notifyItemRemoved(getPosition());
                        notifyItemRangeChanged(0, data.size());
//                        demand.setImg(appendImagePath());
                        if (iv_add.getVisibility() == View.GONE) {
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


    /*类别picker*/
    public void showLBPicker() {
        final ArrayList<String> options1 = new ArrayList<>();
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int op, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1.get(op);
                catogoryPos = op;
                type.setText(tx);
            }
        })
                .setTitleText("选择需求类型")
                .build();

        options1.add("求购");
        options1.add("招商");
        pvOptions.setPicker(options1);
        pvOptions.show();
    }

    public boolean checkNotNull() {
        if (leibie.equals("")) {
            dialog.showWarn("请选择类别");
            return false;
        }
        if (content.equals("")) {
            dialog.showWarn("请输入内容");
            et_3.requestFocus();
            return false;
        }
//        if (phoneNum.equals("")) {
//            dialog.showWarn("请输入手机号");
//            et_4.requestFocus();
//            return false;
//        }
//        if (area.equals("")) {
//            dialog.showWarn("请输入区域");
//            et_5.requestFocus();
//            return false;
//        }
        if (title.equals("")) {
            dialog.showWarn("请输入标题");
            et_2.requestFocus();
            return false;
        }
        if (contracts.equals("")) {
            dialog.showWarn("请输入联系信息");
            et_8.requestFocus();
            return false;
        }
        return true;
    }

    public String appendImagePath() {
        String result = "";
        for (String re : imgPathResults) {
            result += re + ";";
        }
        L.e(result);
        return result;
    }

}
