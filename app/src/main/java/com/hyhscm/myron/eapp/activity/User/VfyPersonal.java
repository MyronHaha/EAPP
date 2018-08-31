package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.data.AccredInfo;
import com.hyhscm.myron.eapp.data.Accreditation;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UploadRe;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.UriToPathUtil;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;
import com.hyhscm.myron.eapp.view.FullyLinearLayoutManager;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/30.
 * 深度认证
 * 图片？ img 1234
 */

public class VfyPersonal extends BaseActivity {
    @BindView(R.id.gv)
    RecyclerView nineGrid;
    @BindView(R.id.iv_add)
    ImageView iv_add;
    @BindView(R.id.et_name)
    EditText name;
    @BindView(R.id.et_ad)
    EditText address;
    @BindView(R.id.et_phone)
    EditText phone;
@BindView(R.id.et_reason)
        EditText et_reason;
    BaseQuickAdapter adapter;
    private List<String> photos = new ArrayList<>();
    List<String> imgPathResults = new ArrayList<>();
    AccredInfo object = null;

    public VfyPersonal() {
        super(R.layout.layout_authorize_personal);
    }

    protected void initView() {
        super.initView();
        common.changeTitle(this, "个人认证");
        common.hideObjs(this, new int[]{R.id.tv_right});
        object = new AccredInfo();
        dialog = new PromptDialog(VfyPersonal.this);
    }

    @OnClick(R.id.iv_add)
    public void add() {
        GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(2)
                .singlePhoto(false)
                .hintOfPick("请选择身份证正反面")
                .filterMimeTypes(new String[]{"image/jpeg"})
                .build();
        GalleryActivity.openActivity(VfyPersonal.this, 200, config);
    }


    //提交
    @OnClick(R.id.bt_change)
    public void submit() {
        if (checkNull()) {
            dialog.showInfo("认证中...");
            object.setName(name.getText().toString());
            object.setAddress(address.getText().toString());
            object.setLinkmanTel(phone.getText().toString());
            object.setCtype(2);
            object.setContent(et_reason.getText().toString());//认证理由；
            //身份证正反
            if (imgPathResults.size()==2) {
                object.setImg1(imgPathResults.get(0));
                object.setImg2(imgPathResults.get(1));
            }else{
                object.setImg1(imgPathResults.get(0));
            }

//            object.setAccountNo(et_ac_company.getText().toString());
//            object.setAddress(et_dizhi.getText().toString());
//            object.setName(et_qiye.getText().toString());
//            object.setLinkman(et_contract.getText().toString());
//            object.setLinkmanTel(et_phone.getText().toString());
//            object.setLegalperson(et_faren.getText().toString());
            HttpCore.accredApply(object, new IResultHandler<AccredInfo>() {
                @Override
                public void onSuccess(Result<AccredInfo> rs) {
                    if (rs.getSuccess()) {
                        dialog.showSuccess("认证成功");
                        VfyPersonal.this.finish();
                    } else {
                        dialog.showError(rs.getMsg());
                    }
                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            try {
                photos = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
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
        }
    }


    public boolean checkNull() {
        if (name.getText().toString().equals("")) {
            dialog.showWarn("姓名不能为空！");
            return false;
        } else if (address.getText().toString().equals("")) {
            dialog.showWarn("地址不能为空！");
            return false;
        } else if (phone.getText().toString().equals("")) {
            dialog.showWarn("联系电话不能为空！");
            return false;
        }else if(et_reason.getText().toString().equals("")){
            dialog.showWarn("认证理由不能为空！");
            return false;
        }
        return true;
    }

//    public void initGvData() {
//        adapter = new BaseQuickAdapter(VfyPersonal.this, "", R.layout.item_photo, photos) {
//            @Override
//            protected void convert(final BaseViewHolder holder, Object item) {
//                if (photos.size() == 2) {
//                    iv_add.setVisibility(View.GONE);
//                    nineGrid.setPadding(110, 0, 0, 0);
//                    nineGrid.postInvalidate();
//                } else {
//
//                    nineGrid.setPadding(0, 0, 0, 0);
//                    nineGrid.postInvalidate();
//                }
//                holder.setImageUrl(R.id.imageView, photos.get(holder.getPosition()));
//                ImageView de = (ImageView) holder.getView(R.id.iv_delete);
//                de.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        holder.setImageUrl(R.id.imageView, null);
//                        photos.remove(holder.getPosition());
//                        imgPathResults.remove(holder.getPosition());
//
//                        notifyItemRemoved(holder.getPosition());
//                        notifyItemRangeChanged(0, photos.size());
////                        demand.setImg(appendImagePath());
//                        if (iv_add.getVisibility() == View.GONE) {
//                            iv_add.setVisibility(View.VISIBLE);
//                        }
//
//                    }
//
//                });
//            }
//
//            @Override
//            public void onBindViewHolder(final BaseViewHolder holder, final int position) {
//                super.onBindViewHolder(holder, position);
//
//            }
//        };
//        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        nineGrid.setLayoutManager(manager);
//        nineGrid.setAdapter(adapter);
//    }

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
            ).inflate(R.layout.item_photo2, parent,
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

            if (photos.size() == 2) {
                iv_add.setVisibility(View.GONE);
                nineGrid.setPadding(110, 0, 0, 0);
                nineGrid.postInvalidate();
            } else {

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

}
