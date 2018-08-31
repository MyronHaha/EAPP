package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.common.design.MaterialDialog;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Home.HallDetail;
import com.hyhscm.myron.eapp.activity.Home.HomeNeed;
import com.hyhscm.myron.eapp.activity.Home.ProductDetail;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.adapter.ProductDetailAdapter;
import com.hyhscm.myron.eapp.adapter.TagAdapter;
import com.hyhscm.myron.eapp.circlePic.BigPic;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.data.Comment;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.DensityUtil;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.StatusBarUtils;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import me.leefeng.promptlibrary.PromptDialog;
import uk.co.senab.photoview.PhotoView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;


/**
 * Created by Jason on 2017/12/1.
 */

public class MyProductDtail extends ListBaseBeanActivity<Comment> {

    BGABanner mContentBanner;
    private View header;
    private Product mproduct;
    private int CreatorId = -1;
    private int pNum = -1;
    private Merchants merchants;
    List img = new ArrayList();

    private RecyclerView rv_more;
    private BaseQuickAdapter adapter;
    Context context;

    @BindView(R.id.rl_bottom)
    RelativeLayout rl;

    public MyProductDtail() {
        super(R.layout.layout_product_detail);
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initView() {
        super.initView();
        // 底部菜单动画
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(dy>20&&recyclerView.getScrollState()==SCROLL_STATE_DRAGGING){
                    if(rl.getVisibility() == View.VISIBLE){
                        AnimationSet outAn = (AnimationSet) AnimationUtils.loadAnimation(context,R.anim.down_in);
                        rl.startAnimation(outAn);
                        rl.setVisibility(View.INVISIBLE);
                    }
                }else if (dy<0&&Math.abs(dy)>20&&recyclerView.getScrollState() == SCROLL_STATE_DRAGGING) {
                    if(rl.getVisibility()!= View.VISIBLE){
                        AnimationSet outAn = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.up_out);
                        rl.startAnimation(outAn);
                        rl.setVisibility(View.VISIBLE);
                    }
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
        changeTitle("招商详情");
//        hideObjs(new int[]{R.id.tv_right});
        if (mproduct == null) {
            mproduct = (Product) getIntent().getSerializableExtra("item");
            CreatorId = mproduct.getCreatorId();
        }
        initTopData();

        initListView(R.layout.item_liuyan);
        initHeader();
        initBanner();

        HttpCore.getProductList(0, 3, new IListResultHandler<Product>() {
            @Override
            public void onSuccess(ListResult<Product> rs) {
                if (rs.getSuccess()) {
                    final List<Product> products = rs.getBiz();
                    adapter = new BaseQuickAdapter<Product>(context, "", R.layout.item_homeproduct2, products) {
                        @Override
                        protected void convert(final BaseViewHolder holder, final Product item) {

                            try {
                                holder.getView(R.id.moreclick).setVisibility(View.INVISIBLE);
                                holder.setIsRecyclable(false);    //0518
                                if (holder.getView(R.id.tv_content) != null) {
                                    holder.setText(R.id.tv_content, item.getName());
                                }

//            holder.setText(R.id.tv_info, item.getName());
                                if (holder.getView(R.id.tv_ago) != null) {
                                    holder.setText(R.id.tv_ago, common.TimeDifference(item.getCreationTime()));
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
                                    Glide.with(context).load(Url.IMAGEPATH + url)
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
                                    TagAdapter adapter = new TagAdapter(context, tagList);
                                    GridLayoutManager manager = new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false);
                                    rv.setFocusable(false);
                                    rv.setFocusableInTouchMode(false);
                                    rv.setLayoutManager(manager);
                                    rv.setAdapter(adapter);
                                }
//            holder.setImage(R.id.iv_product, Url.IMAGEPATH + splitString(item.getImg(), ";").get(0));

//            adapter.notifyDataSetChanged();
                            } catch (Exception e) {

                            }
                        }
                    };
                    LinearLayoutManager manager = new LinearLayoutManager(context);
                    rv_more.setLayoutManager(manager);
                    rv_more.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseViewHolder base, int position) {
                            //skip
                            Intent intent = new Intent(MyProductDtail.this, MyProductDtail.class);
                            intent.putExtra("item", products.get(position));
                            launchActivityWithIntent(intent);
                        }
                    });
                }
            }
        });
//        initxRefresh();
    }


    private void initTopData() {
        HttpCore.getHallDetail(CreatorId + "", new IResultHandler<Merchants>() {   //m
            @Override
            public void onSuccess(Result<Merchants> rs) {
                if (rs.getSuccess()) {
                    merchants = rs.getBiz();
                    pNum = merchants.getPnum();
                    L.e("numlflll" + pNum);
                    ((TextView) header.findViewById(R.id.tv_ago)).setText("招商产品（" + pNum + "）");
                    ((TextView) header.findViewById(R.id.tv_user)).setText(merchants.getName());

                    String imgpath = Url.IMAGEPATH + merchants.getImg();
                    Glide.with(EAPPApplication.getInstance())
                            .load(imgpath)
                            .transform(new GlideCircleTransform(EAPPApplication.getInstance()))
                            .error(R.mipmap.img_head)
                            .placeholder(R.mipmap.img_head)
                            .into(((ImageView) header.findViewById(R.id.iv_user)));

                } else if (rs.getErrorCode().equals("-3")) {
                    AutoLogin(new IResultHandler() {
                        @Override
                        public void onSuccess(Result rs) {
                            if (rs.getSuccess()) {
                                initTopData();
                            }

                        }
                    });
                }
            }
        });
    }

    protected void initData() {
        params.put("id", mproduct.getId() + "");
        initData(Url.MERCHANTCOMMENT, 6);
    }

    private void initHeader() {
        header = LayoutInflater.from(this).inflate(R.layout.layout_product_header, (ViewGroup) findViewById(android.R.id.content), false);
        mRecyclerViewAdapter.addHeaderView(header);
        mContentBanner = (BGABanner) header.findViewById(R.id.banner_guide_content);
        header.findViewById(R.id.tv_liuyan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductDtail.this, Liuyan.class);
                intent.putExtra("from", "product");
                intent.putExtra("product_id", mproduct.getId() + "");
                startActivityForResult(intent, 101);
            }
        });
        ((TextView) header.findViewById(R.id.tv_name)).setText(mproduct.getName().equals("") ? "暂无信息" : mproduct.getName());
        ((TextView) header.findViewById(R.id.tv_xh)).setText(mproduct.getCode().equals("") ? "暂无信息" : mproduct.getCode());//xinghao ？
        ((TextView) header.findViewById(R.id.tv_gg)).setText(mproduct.getSpec().equals("") ? "暂无信息" : mproduct.getSpec());
        ((TextView) header.findViewById(R.id.tv_gg2)).setText(mproduct.getCode().equals("") ? "暂无信息" : mproduct.getCode());
        ((TextView) header.findViewById(R.id.tv_factory)).setText(mproduct.getManufacturer().equals("") ? "暂无信息" : mproduct.getManufacturer());
        ((TextView) header.findViewById(R.id.tv_zc)).setText(mproduct.getPolicy().equals("") ? "暂无信息" : mproduct.getPolicy());
        ((TextView) header.findViewById(R.id.tv_area)).setText(mproduct.getAreas().equals("") ? "暂无信息" : mproduct.getAreas());

        ((TextView) header.findViewById(R.id.tv_gg2)).setText(mproduct.getAdvantage().equals("") ? "暂无信息" : mproduct.getAdvantage());
        ((TextView) header.findViewById(R.id.tv_code)).setText(mproduct.getWarrant().equals("") ? "暂无信息" : mproduct.getWarrant());
        ((TextView) header.findViewById(R.id.tv_room)).setText(mproduct.getDepts().equals("") ? "暂无信息" : mproduct.getDepts());
        ((TextView) header.findViewById(R.id.tv_qudao)).setText(mproduct.getChannelName().equals("") ? "暂无信息" : mproduct.getChannelName());
        ((TextView) header.findViewById(R.id.tv_yibao)).setText(mproduct.getMed().equals("") ? "暂无信息" : mproduct.getMed());
        ((TextView) header.findViewById(R.id.tv_yw)).setText(mproduct.getBtype().equals("") ? "暂无信息" : mproduct.getBtype());
        ((TextView) header.findViewById(R.id.tv_gllb)).setText(mproduct.getCategory().equals("") ? "暂无信息" : mproduct.getCategory());
        ((TextView) header.findViewById(R.id.tv_scdq)).setText(mproduct.getPtype().equals("") ? "暂无信息" : mproduct.getPtype());

        ((TextView) header.findViewById(R.id.tv_title_more)).setText(String.format(context.getResources().getString(R.string.str_more), "招商产品"));

        header.findViewById(R.id.top4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductDtail.this, HallDetail.class);
                intent.putExtra("item", merchants);
                launchActivityWithIntent(intent);
            }
        });
        header.findViewById(R.id.tv_more_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = header.findViewById(R.id.rl_more_info);
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.VISIBLE);
                }

            }
        });
        //
        rv_more = (RecyclerView) header.findViewById(R.id.rv_more);
        rv_more.setFocusable(false);
        rv_more.setFocusableInTouchMode(false);
        TextView more = (TextView) header.findViewById(R.id.tomore);
        more.setText("还有" + MainActivity.allproductCount + "条招商产品，赶紧去看看吧");
        header.findViewById(R.id.iv_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(com.hyhscm.myron.eapp.activity.Home.HomeProduct.class);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent d) {
        super.onActivityResult(requestCode, resultCode, d);
        if (requestCode == 101 && resultCode == 101) {
            initData();
        }
    }

    private void initBanner() {

        if (mproduct.getImg().equals("")) {
            mContentBanner.setData(R.mipmap.img_nothingbg2);
        } else {
            img = splitString(mproduct.getImg(), ";");
            mContentBanner.setData(img, null);
        }
        mContentBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                if (model != null) {
//                    itemView.setAdjustViewBounds(true);

                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    itemView.setLayoutParams(params);
                    itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    itemView.setMaxWidth(DensityUtil.getDisplay(MyProductDtail.this).getWidth());
//                    itemView.setMaxHeight(DensityUtil.getDisplay(MyProductDtail.this).getWidth());
                }

                Glide.with(MyProductDtail.this.getApplicationContext())
                        .load(Url.IMAGEPATH + model)
                        .placeholder(R.mipmap.img_nothingbg3)
                        .error(R.mipmap.img_nothingbg3)
                        .dontAnimate()
                        .into(itemView);
            }


        });
//mContentBanner.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        Log.e("gogogoo","gogooogogogogo");
//        mContentBanner.stopAutoPlay();
//        launchActivityWithIntent( new Intent(MyProductDtail.this, BigPic.class).putExtra("url", (String) img.get(mContentBanner.getCurrentItem())));
//
//    }
//});

        mContentBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
//                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
//                        Log.e("gogogoo","gogooogogogogo");
                mContentBanner.stopAutoPlay();
                launchActivityWithIntent(new Intent(MyProductDtail.this, BigPic.class).putExtra("url", model));
            }
        });
//        mContentBanner.setData(common.createURlsData(1), null);
    }

    @Override
    protected void bindView(BaseViewHolder holder, final Comment item) {
        holder.setText(R.id.tv_user, item.getCname());
        holder.setText(R.id.tv_ago, common.dataToString(item.getCreationTime()));
        holder.setText(R.id.tv_content, item.getContent());
        holder.setRoundImageView(R.id.iv_user, Url.WEBPATH + item.getUimg());
        if (item.getCname().equals(HttpCore.name)) {
            holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.iv_delete).setVisibility(View.GONE);
        }
        holder.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpCore.pcommentDel(item.getId(), new IResultHandler<String>() {
                    @Override
                    public void onSuccess(Result<String> rs) {
                        if (rs.getSuccess()) {
                            new PromptDialog(MyProductDtail.this).showSuccess("删除成功");
                            rv.forceToRefresh();
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.ll_right)
    public void share() {
        //bottom
        View view = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
        showCustomDialog(view, mproduct, 0, false);

//        shareToWx(0,demand.getId()+"",demand.getTitle(),demand.getContent(),0);
    }

    @OnClick(R.id.ll_dh)
    public void dial(){
        //电话
        dial(mproduct.getLinkmanTel());
    }
    @OnClick(R.id.ll_liuyan)
    public void comment(){
        Intent intent = new Intent(MyProductDtail.this, Liuyan.class);
        intent.putExtra("from", "product");
        intent.putExtra("product_id", mproduct.getId() + "");
        startActivityForResult(intent, 101);
    }
}