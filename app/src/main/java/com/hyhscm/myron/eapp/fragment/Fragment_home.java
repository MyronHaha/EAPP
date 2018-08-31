package com.hyhscm.myron.eapp.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.I;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.Home.ENewsDetail;
import com.hyhscm.myron.eapp.activity.Home.HallDetail;
import com.hyhscm.myron.eapp.activity.Home.HallList;
import com.hyhscm.myron.eapp.activity.Home.HomeNeed;
import com.hyhscm.myron.eapp.activity.Home.HomeProduct;
import com.hyhscm.myron.eapp.activity.Home.HomeSearch;
import com.hyhscm.myron.eapp.activity.Home.ENews;
import com.hyhscm.myron.eapp.activity.Home.ProductCategory;
import com.hyhscm.myron.eapp.activity.Home.ZS_Second;
import com.hyhscm.myron.eapp.activity.MainActivity;
import com.hyhscm.myron.eapp.activity.MyWeb;
import com.hyhscm.myron.eapp.activity.User.MyProductDtail;
import com.hyhscm.myron.eapp.adapter.GridViewAdapter;
import com.hyhscm.myron.eapp.adapter.NotifyAdapter;
import com.hyhscm.myron.eapp.adapter.RvSmallHallAdapter;
import com.hyhscm.myron.eapp.adapter.RvToutiaoAdapter;
import com.hyhscm.myron.eapp.adapter.RvZSAdapter;
import com.hyhscm.myron.eapp.adapter.SwitchPagerAdapter;
import com.hyhscm.myron.eapp.data.Advert;
import com.hyhscm.myron.eapp.data.Advnotify;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.SignResult;
import com.hyhscm.myron.eapp.data.TopNum;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.CustomSGLayoutManager;
import com.hyhscm.myron.eapp.view.DeviderGridItemDecoration;
import com.hyhscm.myron.eapp.view.FullyLinearLayoutManager;
import com.hyhscm.myron.eapp.view.ScrollRecyclerView;
import com.hyhscm.myron.eapp.view.ViewFindUtils;
import com.taobao.library.VerticalBannerView;

import org.apache.http.protocol.HTTP;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/11/20.
 * index
 */

public class Fragment_home extends Fragment {
    @BindView(R.id.gridView)
    GridView mGridView;
    @BindView(R.id.banner_guide_content)
    BGABanner mContentBanner;
    @BindView(R.id.scroll)
    NestedScrollView scrollView;
    @BindView(R.id.rv_zs)
    RecyclerView rv_zs;
//    @BindView(R.id.rv_small_hall)
//    ScrollRecyclerView rv_hall;
    @BindView(R.id.rv_news)
    RecyclerView rv_news;
//    @BindView(R.id.drag_recycler_view)
//    DragContainer horizontalView;
//    @BindView(R.id.vp_hall)
//    ViewPager vp_hall;
    @BindView(R.id.rv_hall)
    RecyclerView rv_hall;
    @BindView(R.id.notify_banner)
    VerticalBannerView advNotify;

//    @BindView(R.id.notify)
//    FrameLayout notify;

    @BindView(R.id.adv_banner)
    BGABanner advBanner;

    @BindView(R.id.fl_adv)
    FrameLayout fl_adv;
    @BindView(R.id.noAdsText)
            TextView tv_noAdsText;
    List<String> bannerList = new ArrayList<>();
    List<Advert> banners = new ArrayList<>();
    List<Advert> banners2 = new ArrayList<>();
    List<Advert> banners3 = new ArrayList<>();
    NotifyAdapter adapter_notify;
    int count = 1;
    private int[] imageRes = {
            R.mipmap.icon_yxtt,
            R.mipmap.icon_zsxq,
            R.mipmap.icon_hrzs,
            R.mipmap.icon_cpss,

    };
    private String[] itemName = {
            "医械头条",
            "最新商机",
            "火热招商",
            "产品代理",
    };

    List<Product> zs_data = new ArrayList();
    List<Merchants> sh_data = new ArrayList();
    List news_data = new ArrayList();
    List<TopNum> topNums = new ArrayList<>();
    //
    GridViewAdapter gvAdapter;
    SwitchPagerAdapter mAdapter;
    RvZSAdapter zs_adapter;
    RvSmallHallAdapter rvSmallHallAdapter;
    RvToutiaoAdapter rvToutiaoAdapter;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            Log.e("update", "update");
            if (NetUtil.getNetWorkState(getActivity()) == NetUtil.NETWORK_NONE) {
                MyToast.makeText(getActivity(), "请检查网络连接", 1000).show();
                return;
            } else {
                initData();
            }
        }
    };



    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yfx, null);
        ButterKnife.bind(this, view);
        L.e("yfx---onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable,1000);

        initView();
        if (NetUtil.getNetWorkState(getActivity()) == NetUtil.NETWORK_NONE) {
            MyToast.makeText(getActivity(), "请检查网络连接", 1000).show();
            return;
        } else {
            initData();
        }

    }

    private void initBanner() {
        mContentBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
//               L.e("model"+model);

                try {
                    Glide.with(EAPPApplication.getInstance())
                            .load(model)
                            .dontAnimate()
                            .centerCrop()
                            .into(itemView);


//                        mContentBanner.setData(bannerList, null);
//                        mContentBanner.setAutoPlayAble(true);
//                        mContentBanner.getViewPager().getAdapter().notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        mContentBanner.setAutoPlayAble(true); 0420
        if (bannerList.size() > 0) {
//            mContentBanner.setAutoPlayAble(true);//0420 maybe bug?
            mContentBanner.setData(bannerList, null);
            mContentBanner.getViewPager().getAdapter().notifyDataSetChanged();
        }
        mContentBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner bgaBanner, View view, Object o, int i) {
                Log.e("bannerclick", i + "");
                if(banners.get(i).getSourceParam()!=null){
                    ((MainActivity) getActivity()).getNewsById(banners.get(i).getSourceParam());
                }
            }
        });
    }

    //初始化和刷新;
    private void initData() {
        pd =  new ProgressDialog(getActivity()).show(getActivity(),"","加载中...");
        //获取banner 数据
       L.e("11111111"+new Date().toString());
        HttpCore.index(new IListResultHandler<Advert>() {
            @Override
            public void onSuccess(ListResult<Advert> rs) {
                if (rs.getSuccess() && rs.getBiz() != null) {
                    bannerList.clear();
                    banners.clear();
                    banners2.clear();
                    banners3.clear();
                    for (Advert ad : rs.getBiz()) {
                        switch (ad.getType()) {
                            case 1:
                                banners.add(ad);
                                break;
                            case 2:
                                if (!ad.getContent().equals("")) {
                                    banners2.add(ad);
                                }
                                break;
                            case 3:
                                if (!ad.getImg().equals("")) {
                                    banners3.add(ad);
                                }
                                break;
                            default:
                                break;
                        }
                        if (ad.getType() == 1 && !ad.getImg().equals("")) {
                            bannerList.add(Url.WEBPATH + ad.getImg());
                        }
                        L.e("img:" + Url.WEBPATH + ad.getImg());
                    }
                } else {
                    L.e("首页banner数据请求失败：" + rs.getMsg());
                }
//                mContentBanner.setAdapter(bannerAdapter);
//                mContentBanner.setData(bannerList, null);
//                mContentBanner.getViewPager().getAdapter().notifyDataSetChanged();

                initBanner();

                List<Advert> list = new ArrayList<>();
                list.addAll(banners2);
                banners2.clear();
                banners2.addAll(list);
                if (banners2.size() == 0) {
                    advNotify.setVisibility(View.GONE);
                } else {
                    advNotify.setVisibility(View.VISIBLE);
                    adapter_notify.setData(banners2);
                    adapter_notify.notifyChanged();
                    advNotify.start();
                }

                if (banners3.size() == 0) {
//                    fl_adv.setVisibility(View.GONE);
                    advBanner.setVisibility(View.GONE);
                    tv_noAdsText.setVisibility(View.VISIBLE);
                } else {
//                    fl_adv.setVisibility(View.VISIBLE);
                    advBanner.setVisibility(View.VISIBLE);
                    tv_noAdsText.setVisibility(View.GONE);
                    initAdvBanner();
                }


            }
        });
        L.e("2222222222222"+new Date().toString());
        HttpCore.getProductList(0, 3, new IListResultHandler<Product>() {
            @Override
            public void onSuccess(ListResult<Product> rs) {
                zs_data.clear();
                zs_data.addAll(rs.getBiz());
                zs_adapter.notifyDataSetChanged();
            }
        });
        L.e("33333333333"+new Date().toString());

        HttpCore.getHallList(0, 6, new IListResultHandler<Merchants>() {
            @Override
            public void onSuccess(ListResult<Merchants> rs) {
                if (rs.getSuccess()) {
                    sh_data.clear();
                    sh_data.addAll(rs.getBiz());
                    rvSmallHallAdapter.notifyDataSetChanged();
                }
            }
        });
        L.e("44444444"+new Date().toString());

        HttpCore.getNewsList(0, 5, new IListResultHandler<News>() {
            @Override
            public void onSuccess(ListResult<News> rs) {
                if (rs.getSuccess()) {
                    news_data.clear();
                    news_data.addAll(rs.getBiz());
                    rvToutiaoAdapter.notifyDataSetChanged();
                }
            }
        });

        HttpCore.getTopNum(new IListResultHandler<TopNum>() {
            @Override
            public void onSuccess(ListResult<TopNum> rs) {
                gvAdapter.refreshNum(rs.getBiz());
                gvAdapter.notifyDataSetChanged();
            }
        });
if(pd.isShowing()){
    pd.dismiss();
}
    }


    private void initView() {
//        common.hideObjs(getActivity(), new  int[]{R.id.iv_back});
//        common.changeTitle(getActivity(), "e链");
        initGrid();  //菜单
        initBanner();
//        initTabView(); //switch
        initVp();
        initRV_ZS(); //招商rv
        initRV_Hall();//明星展厅
        initRV_News();
        initAdvNotify();
        initAdvBanner();
    }

    private void initAdvBanner() {

        advBanner.setAdapter(new BGABanner.Adapter<ImageView, Advert>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, Advert model, int position) {
//               L.e("model"+model);
                try {
                    Glide.with(EAPPApplication.getInstance())
                            .load(Url.WEBPATH + model.getImg())
                            .error(R.mipmap.img_nothingbg2)
                            .into(itemView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        advBanner.setAutoPlayAble(true);
        if (banners3.size() > 0) {
            advBanner.setData(banners3, null);
            advBanner.getViewPager().getAdapter().notifyDataSetChanged();
        }
        advBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner bgaBanner, View view, Object o, int i) {
                Log.e("bannerclick", i + "");
                Advert data = banners3.get(i);
                switch (data.getSourceType()) {
                    //活动
                    case 1:
                        common.launchActivityWithIntent(getActivity(), new Intent(getActivity(), MyWeb.class).putExtra("url", Url.NEWS_DETAIL + "?id=" + data.getSourceParam()));
                        break;
                    //资讯
                    case 2:
                        ((MainActivity) getActivity()).getNewsById(data.getSourceParam());
                        break;
                    case 3:
                        //产品
                        ((MainActivity) getActivity()).getMerchantById(data.getSourceParam());
                        break;
                    case 4:
                        //商机
                        ((MainActivity) getActivity()).getDemandById(data.getSourceParam());
                        break;
                    case 5:
                        //代理
                        ((MainActivity) getActivity()).getDemandById(data.getSourceParam());
                        break;
                    case 6:
                        //其他
                        common.launchActivityWithIntent(getActivity(), new Intent(getActivity(), MyWeb.class).putExtra("url", Url.NEWS_DETAIL + "?id=" + data.getSourceParam()));
                        break;
                }
            }
        });
    }

    private void initAdvNotify() {
//        List<Advnotify> datas02 = new ArrayList<>();
//        datas02.add(new Advnotify("求购", "需要yilksjdfljadlfjasldkfjlaskdjflasdflkasdjflasjd。。。。。。。。。一个"));
//        datas02.add(new Advnotify("招商", "招商个江东父老骨科的枫蓝国际劳动法开个 感受到。。。。。。。。。一个"));
//        datas02.add(new Advnotify("求购", "双方都发士大夫撒旦法撒旦王特人人通 感受到。。。。。。。。。一个"));
//        datas02.add(new Advnotify("代理", "发的范德萨的发送到发送到发送到发生地方 感受到。。。。。。。。。一个"));
        //占位
        if (banners2.size() == 0) {
            banners2.add(new Advert());
        }
        adapter_notify = new NotifyAdapter(banners2, getActivity(), new NotifyAdapter.NotifyListener() {
            @Override
            public void onNotifyClick(View view, Advert data) {
                switch (data.getSourceType()) {
                    //活动
                    case 1:
                        common.launchActivityWithIntent(getActivity(), new Intent(getActivity(), MyWeb.class).putExtra("url", Url.NEWS_DETAIL + "?id=" + data.getSourceParam()));
                        break;
                    //资讯
                    case 2:
                        ((MainActivity) getActivity()).getNewsById(data.getSourceParam());
                        break;
                    case 3:
                        //产品
                        ((MainActivity) getActivity()).getMerchantById(data.getSourceParam());
                        break;
                    case 4:
                        //商机
                        ((MainActivity) getActivity()).getDemandById(data.getSourceParam());
                        break;
                    case 5:
                        //代理
                        ((MainActivity) getActivity()).getDemandById(data.getSourceParam());
                        break;
                    case 6:
                        //其他
                        common.launchActivityWithIntent(getActivity(), new Intent(getActivity(), MyWeb.class).putExtra("url", Url.NEWS_DETAIL + "?id=" + data.getSourceParam()));
                        break;

                }
            }
        });
        advNotify.setAdapter(adapter_notify);
        advNotify.start();
    }

    private void initRV_News() {
        rv_news.setFocusable(false);
        rv_news.setFocusableInTouchMode(false);
//        common.createData(sh_data);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_news.setLayoutManager(manager);
        rvToutiaoAdapter = new RvToutiaoAdapter(getActivity(), news_data);
        rvToutiaoAdapter.setOnItemClickListener(new RvToutiaoAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ENewsDetail.class);
                intent.putExtra("item", (News) news_data.get(position));
                common.launchActivityWithIntent(getActivity(), intent);
            }
        });
        rv_news.setAdapter(rvToutiaoAdapter);
    }


    public List getHallList(int position){
        List<Merchants> list = new ArrayList<>();

       list.add( sh_data.get(position));
       return list;
    }
    private void initRV_Hall() {
        rv_hall.setFocusable(false);
        rv_hall.setFocusableInTouchMode(false);
//        common.createData(sh_data);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);
        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);
        rv_hall.setLayoutManager(manager);
        rv_hall.setHasFixedSize(true);
        rv_hall.setNestedScrollingEnabled(false);
//        rv_hall.addItemDecoration(new DeviderGridItemDecoration(getActivity()));
        rvSmallHallAdapter = new RvSmallHallAdapter(getActivity(), sh_data);
        rvSmallHallAdapter.setOnItemClickListener(new RvSmallHallAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HallDetail.class);
                intent.putExtra("item", sh_data.get(position));
                common.launchActivityWithIntent(getContext(), intent);
            }
        });
        rv_hall.setAdapter(rvSmallHallAdapter);
//        rv_hall.addItemDecoration(new DeviderGridItemDecoration(getActivity()));

        //if you want to use your own custom footer, you should set your own footer to
        //the DragContainer like this
//        horizontalView.setFooterDrawer(new BezierFooterDrawer.Builder(getActivity(), 0xffffc000).setIconDrawable(getResources().getDrawable(R.drawable.left)).build());
        //set listener
//        horizontalView.setDragListener(new DragListener() {
//            @Override
//            public void onDragEvent() {
//                //do whatever you want,for example skip to the load more Activity.
//                Intent intent = new Intent(getActivity(), HallList.class);
//                startActivity(intent);
//            }
//        });
//        rv_hall.setFocusable(false);
//        rv_hall.setFocusableInTouchMode(false);
////        common.createData(sh_data);
////        GridLayoutManager manager = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
//        final CustomSGLayoutManager manager1 = new CustomSGLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        manager1.setSpeedRatio(1.0);// 滑动速度控制
//        rv_hall.setLayoutManager(manager1);
//        rvSmallHallAdapter = new RvSmallHallAdapter(getActivity(), sh_data);
//        rvSmallHallAdapter.setOnItemClickListener(new RvSmallHallAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), HallDetail.class);
//                intent.putExtra("item", sh_data.get(position));
//                common.launchActivityWithIntent(getContext(), intent);
//            }
//        });
//        rv_hall.setAdapter(rvSmallHallAdapter);
//        rv_hall.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                // 当不滑动时
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (isend) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                common.launchActivity(getActivity(), HallList.class);
//                            }
//                        }, 500);
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                // dx值大于0表示正在向左滑动，小于或等于0表示向右滑动或停止
//                isSlidingToLeft = dx > 0;
//                // 当不滑动时
//                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                View view = null;
//                View more = null;
//                // 获取最后一个完全显示的itemPosition
//                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
//                int itemCount = manager.getItemCount();
//                try {
//                    view = manager1.getChildAt(itemCount - 1);
//                    more = view.findViewById(R.id.more);
//                    if (!isSlidingToLeft) {
//                        more.setVisibility(View.GONE);
//                        isend = false;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // 判断是否滑动到了最后一个Item，并且是向左滑动
//                if (lastItemPosition == (itemCount - 1) && isSlidingToLeft) {
////                        common.launchActivity(getActivity(), HallList.class);
//                    more.setVisibility(View.VISIBLE);
//                    isend = true;
////                        MyToast.makeText(getActivity(),"fdsfsdf",1000).show();
////                       View view =  manager1.getChildAt(itemCount-1);
////                       view.findViewById(R.id.more).setVisibility(View.VISIBLE);
//                }
//
//            }
//
//        });
    }

    public void initVp() {
        float scale = this.getResources().getDisplayMetrics().scaledDensity;
        View decorView = getActivity().getWindow().getDecorView();
        final ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new SwitchPagerAdapter(getActivity(), getChildFragmentManager());
        vp.setAdapter(mAdapter);
        /** indicator固定宽度 */
        final SegmentTabLayout tabLayout_5 = ViewFindUtils.find(decorView, R.id.tablayout);
        tabLayout_5.setTextsize((getActivity().getResources().getDimension(R.dimen.x30) - 0.5F) / scale);
//        tabLayout_5.setTabSpaceEqual(true);
//        tabLayout_5.setIndicatorCornerRadius(2.0f);
//        tabLayout_5.setIndicatorHeight(3.0f);
//        tabLayout_5.setIndicatorWidth(16.0f);
//       tabLayout_5.setViewPager(vp);
//        tabLayout_5.setViewPager(vp, new String[]{"最新商机", "最新代理"});

        tabLayout_5.setTabData(new String[]{"最新商机", "最新代理"});

        tabLayout_5.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                vp.setCurrentItem(i);
            }

            @Override
            public void onTabReselect(int i) {

            }
        });
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout_5.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initGrid() {
        gvAdapter = new GridViewAdapter(getActivity(), imageRes, itemName, topNums);
        mGridView.setAdapter(gvAdapter);

        mGridView.setFocusable(false);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    common.launchActivity(getActivity(), ENews.class);
                }
                if (i == 1) {
                    common.launchActivityWithIntent(getActivity(), new Intent(getActivity(), HomeNeed.class).putExtra("from", 2));
                }
                if (i == 2) {
                    common.launchActivity(getActivity(), ZS_Second.class);

                }
                if (i == 3) {
                    common.launchActivityWithIntent(getActivity(), new Intent(getActivity(), HomeNeed.class).putExtra("from", 4));
                }
                if (view.findViewById(R.id.tv_count).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.tv_count).setVisibility(View.GONE);
                }
            }
        });
    }


    public void initRV_ZS() {
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_zs.setLayoutManager(manager);
        zs_adapter = new RvZSAdapter(getActivity(), zs_data);
        zs_adapter.setOnItemClickListener(new RvZSAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MyProductDtail.class);
                intent.putExtra("item", zs_data.get(position));
                common.launchActivityWithIntent(getActivity(), intent);
            }
        });
        rv_zs.setFocusable(false);
        rv_zs.setFocusableInTouchMode(false);
        rv_zs.setAdapter(zs_adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            scrollView.smoothScrollTo(0, 10);
            handler.post(runnable);
        }
        if (hidden) {
            advNotify.stop();
        }
        mAdapter.notifyDataSetChanged(); // fg switch refresh
    }

    @OnClick(R.id.need_more)
    public void moreNeed() {
        Intent intent = new Intent(getActivity(), HomeNeed.class);
        common.launchActivityWithIntent(getActivity(), intent);
    }

    @OnClick(R.id.more_product)
    public void product_more() {
        Intent intent = new Intent(getActivity(), HomeProduct.class);
        common.launchActivityWithIntent(getActivity(), intent);
    }

    @OnClick(R.id.more_hall)
    public void hall_more(){
        Intent intent = new Intent(getActivity(), HallList.class);
        common.launchActivityWithIntent(getActivity(), intent);
    }
    @OnClick(R.id.tv_num)
    public void news_more() {
        Intent intent = new Intent(getActivity(), ENews.class);
        common.launchActivityWithIntent(getActivity(), intent);
    }

    @OnClick(R.id.dial)
    public void dial() {
         String number = getActivity().getResources().getString(R.string.phone_num);
        ((MainActivity)getActivity()).dial(number);

    }

    public void showUiDialog(String msg, PromptButton sure) {

        PromptButton button = new PromptButton("取消", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton promptButton) {

            }
        });
        new PromptDialog(getActivity()).showWarnAlert(msg, sure, button);
    }

    // 显示提示框 拨号
    private void CallPhone(final String number) {

        PromptButton sure = new PromptButton("确定", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton promptButton) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + number);
                intent.setData(data);
                startActivity(intent);
            }
        });
        sure.setFocusBacColor(getResources().getColor(R.color.colorPrimary));
        sure.setTextColor(Color.parseColor("#ff5959"));
        showUiDialog("确认拨号给" + "\n\r" + number + "?",
                sure);

    }

    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    CallPhone(getActivity().getResources().getString(R.string.phone_num));
                } else {
                    // 授权失败！
                    MyToast.makeText(getActivity(), "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }

    public void refreshSwitch() {
        Log.e("fg_refresh","------");
        mAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        Log.e("visiblefr","true");
//        mAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        if (adapter_notify != null && advNotify != null) {
            advNotify.start();
        }
        if (mContentBanner != null) {
            mContentBanner.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter_notify != null && advNotify != null) {
            advNotify.stop();
        }
        if (mContentBanner != null) {
            mContentBanner.stopAutoPlay();
        }
    }

}
