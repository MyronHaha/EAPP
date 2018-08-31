package com.hyhscm.myron.eapp.activity.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.E;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.common.design.MaterialDialog;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.google.gson.Gson;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseBeanActivity;
import com.hyhscm.myron.eapp.activity.Home.HomeNeed;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.adapter.MyneedAdapter;
import com.hyhscm.myron.eapp.adapter.NeedDetailAdapter;
import com.hyhscm.myron.eapp.adapter.TagAdapter;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.data.Comment;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.JsonBean;
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
import com.hyhscm.myron.eapp.view.FullyGridLayoutManager;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;


/**
 * Created by Jason on 2017/12/1.
 */

public class MyNeedDetail extends ListBaseBeanActivity<Comment> {

    ImageView iv_user;

    TextView tv_user;

    TextView tv_ago;

    TextView tv_see;

    TextView tv_title;

    TextView tv_content;

    NineGridView gv;
    private View header;
    private Demand demand;

    private BaseQuickAdapter<Demand> adapter;
    RecyclerView rv_more;
    Context context;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl;
    public MyNeedDetail() {
        super(R.layout.layout_need_detail);
        context = this;
    }

    protected void initView() {
        try {
            demand = (Demand) getIntent().getSerializableExtra("item");
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

        } catch (Exception e) {

        }
//        common.hideObjs(this, new int[]{R.id.tv_right});
        common.changeTitle(this, "需求详情");
        //initxRefresh();
        initListView(R.layout.item_liuyan);
        if (demand != null) {
            initHeader();
        }
        HttpCore.getDemandList("0", 0, 4, new IListResultHandler<Demand>() {
            @Override
            public void onSuccess(ListResult<Demand> rs) {
                if (rs.getSuccess()) {
                    final List<Demand> demands = rs.getBiz();
                    if (demands.contains(demand)) {
                        demands.remove(demand);
                    } else {
                        demands.remove(demands.get(demands.size() - 1));
                    }
                    adapter = new BaseQuickAdapter<Demand>(context, "", R.layout.item_homeneed2, demands) {
                        @Override
                        protected void convert(final BaseViewHolder holder, final Demand item) {
                            try {
                                holder.getView(R.id.moreclick).setVisibility(View.INVISIBLE);
                                holder.setText(R.id.tv_content, item.getContent());
                                holder.setText(R.id.tv_user, item.getCname());
                                holder.setRoundImageView(R.id.iv_user, Url.WEBPATH + item.getUimg());
                                // 可能nullpoiont 进exception 最后进行；
                                if (holder.getView(R.id.tv_ago) != null) {
                                    holder.setText(R.id.tv_ago, common.dataToString(item.getCreationTime()));
                                }
                                if (holder.getView(R.id.tv_see) != null) {
                                    holder.setText(R.id.tv_see, item.getHit() + "");
                                }
                                if (item.getImg() != "" && holder.getView(R.id.iv_adv) != null) {
                                    holder.setImage(R.id.iv_adv, Url.IMAGEPATH + item.getImg());
                                }
                                // ding tuijian huore icon, show or invisible
                                if (item.getTops() > 0) {
                                    holder.getView(R.id.iv_ding).setVisibility(View.VISIBLE);
                                }
                                if (item.getIsCommend() > 0) {
                                    holder.getView(R.id.iv_tuijian).setVisibility(View.VISIBLE);
                                }
                                if (item.getHit() > 50) {
                                    holder.getView(R.id.iv_huore).setVisibility(View.VISIBLE);
                                }
                                // 广告图片、、、
                                //地区标签
                                if (holder.getView(R.id.rv_areas) != null) {
                                    RecyclerView rv_area = (RecyclerView) holder.getView(R.id.rv_areas);
                                    List<String> tagList = new ArrayList<>();
                                    if (item.getAreas().equals("")) {
                                        tagList.add("暂无地区");
                                    } else {
                                        tagList.addAll(splitString(item.getAreas(), "\\s+"));
                                    }
                                    TagAdapter adapter = new TagAdapter(context, tagList);
                                    FullyGridLayoutManager manager = new FullyGridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false);

                                    rv_area.setFocusable(false);
                                    rv_area.setFocusableInTouchMode(false);
                                    rv_area.setLayoutManager(manager);
                                    rv_area.setAdapter(adapter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    LinearLayoutManager manager = new LinearLayoutManager(context);
                    rv_more.setLayoutManager(manager);
                    rv_more.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseViewHolder base, int position) {
                            Intent intent = new Intent(context, MyNeedDetail.class);
                            intent.putExtra("item", demands.get(position));
                            launchActivityWithIntent(intent);
                        }
                    });
                }
            }
        });
    }

    protected void initData() {//解析数据
        params.put("id", demand.getId() + "");
        initData(Url.DEMAND_COMMENT, 6);


    }

    private void initHeader() {
        header = LayoutInflater.from(this).inflate(R.layout.layout_need_detail_head, (ViewGroup) findViewById(android.R.id.content), false);
        mRecyclerViewAdapter.addHeaderView(header);
        iv_user = (ImageView) header.findViewById(R.id.iv_user);
        tv_user = (TextView) header.findViewById(R.id.tv_user);
        tv_ago = (TextView) header.findViewById(R.id.tv_ago);
        tv_see = (TextView) header.findViewById(R.id.tv_see);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        tv_content = (TextView) header.findViewById(R.id.tv_content);

        tv_user.setText(demand.getCname());
        tv_title.setText(demand.getTitle());
        tv_content.setText(demand.getContent());
        tv_ago.setText(common.dataToString(demand.getCreationTime()));
        tv_see.setText(demand.getHit() + "");
        //contact info wechat...ect
        ((TextView)header.findViewById(R.id.tv_contact)).setText(String.format(context.getResources().getString(R.string.contact_info),demand.getContacts().equals("")?"暂无":demand.getContacts()));
        if (!demand.getUimg().equals("")) {
            Glide.with(this).load(Url.IMAGEPATH + demand.getUimg())
                    .transform(new GlideCircleTransform(this))
                    .into(iv_user);
        } else {
            Glide.with(this).load(R.mipmap.img_head)
                    .transform(new GlideCircleTransform(this))
                    .into(iv_user);
        }
//        ll_dh = (LinearLayout) header.findViewById(R.id.ll_dh);

        if (!demand.getImg().equals("")) {
            initGV();
        }


        //
        rv_more = (RecyclerView) header.findViewById(R.id.rv_more);
        rv_more.setFocusable(false);
        rv_more.setFocusableInTouchMode(false);
        ((TextView) header.findViewById(R.id.tv_title_more)).setText(String.format(context.getResources().getString(R.string.str_more), "求购商机"));

        TextView more = (TextView) header.findViewById(R.id.tomore);
        more.setText("还有" + MainActivity.allneedCount + "条新需求，赶紧去看看吧");
        header.findViewById(R.id.iv_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(HomeNeed.class);
            }
        });

        //地区标签
        RecyclerView rv_area = (RecyclerView) header.findViewById(R.id.rv_areas);
        List<String> tagList = new ArrayList<>();
        if (demand.getAreas().equals("")) {
            tagList.add("暂无地区");
        } else {
            tagList.addAll(splitString(demand.getAreas(), "\\s+"));
        }
        TagAdapter adapter = new TagAdapter(context, tagList);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false);
        rv_area.setFocusable(false);
        rv_area.setFocusableInTouchMode(false);
        rv_area.setLayoutManager(manager);
        rv_area.setAdapter(adapter);


    }

    private void initGV() {
        gv = (NineGridView) header.findViewById(R.id.nineGrid);
//        String[] urls = {
//                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3807720874,2433149685&fm=27&gp=0.jpg",
//                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1602552054,373587514&fm=27&gp=0.jpg",
//                "https://ss0.baidu.com/73x1bjeh1BF3odCf/it/u=3039422686,2423978616&fm=85&s=391E1FD06801C24316993F0C0300F0D5"
//        };
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
//        List<String> images = Arrays.asList(urls);
        List<String> images = splitImgArr();
//        L.e("gv--"+ demand.getImg());
//        images.add(Url.IMAGEPATH + demand.getImg());
//        List<String> images = Arrays.asList(urls);
        if (images != null) {
            for (String image : images) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(image);
                info.setBigImageUrl(image);
                imageInfo.add(info);
            }
        }
        gv.setAdapter(new NineGridViewClickAdapter(this, imageInfo));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent d) {
//        super.onActivityResult(requestCode, resultCode, d);
        if (requestCode == 100 && resultCode == 100) {
//            String liuyan = d.getStringExtra("liuyan");
            L.e("onActivityResult", "iiiiiiiiii");
            // 留言提交接口
            rv.forceToRefresh();
            loadData();
        }
    }

    @Override
    protected void bindView(final BaseViewHolder holder, final Comment item) {
        holder.setText(R.id.tv_content, item.getContent());
        holder.setText(R.id.tv_user, item.getCname());
        holder.setText(R.id.tv_ago, common.dataToString(item.getCreationTime()));
        holder.setRoundImageView(R.id.iv_user, Url.IMAGEPATH + item.getUimg());

        if (item.getCname().equals(HttpCore.name)) {
            Log.e("namename", HttpCore.name + "item.getname==" + item.getCname());
            holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
            holder.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                    final PromptDialog dialog = new PromptDialog(MyNeedDetail.this);
                    HttpCore.commentDel(item.getId(), new IResultHandler<String>() {
                        @Override
                        public void onSuccess(Result<String> rs) {
                            if (rs.getSuccess()) {
//                              mRecyclerViewAdapter.notifyItemRemoved(holder.getAdapterPosition());
                                dialog.showSuccess("删除成功!");
                                loadData();
                                L.e("delete---" + rs.getMsg());
                            }
                        }
                    });
                }
            });

        } else {
            holder.getView(R.id.iv_delete).setVisibility(View.GONE);
        }
    }

    public List splitImgArr() {
        String[] arr = demand.getImg().split(";");
        List<String> list = new ArrayList<>();
        for (String url : arr) {
            list.add(Url.IMAGEPATH + url);
        }
        return list;
    }

    @OnClick(R.id.ll_right)
    public void share() {
        //bottom
        View view = getLayoutInflater().inflate(R.layout.wx_share_dialog2, null);
        showCustomDialog(view, demand, 0, false);

//        shareToWx(0,demand.getId()+"",demand.getTitle(),demand.getContent(),0);
    }

    @OnClick(R.id.ll_dh)
    public void dial(){
        //电话
        dial(demand.getPhone());
    }
    @OnClick(R.id.ll_liuyan)
    public void comment(){
        Intent intent = new Intent(MyNeedDetail.this, Liuyan.class);
        intent.putExtra("from", "need");
        intent.putExtra("demand_id", demand.getId() + "");
        launchActivityWithIntent(intent, 100);
    }

}