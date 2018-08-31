package com.hyhscm.myron.eapp.activity.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.circlePic.GlideCircleTransform;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.fragment.Fragment_halldetail1;
import com.hyhscm.myron.eapp.fragment.Fragment_halldetail2;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/12/13.
 */

public class HallDetail extends BaseActivity {
    //    @BindView(R.id.rv)
//    LRecyclerView rv;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_company)
    TextView company;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.iv_bg)
            ImageView bg;
    @BindView(R.id.dial)
    Button bt_dial;

    View decorView;
    ViewPager vp;
    SlidingTabLayout tabLayout;
    MyPagerAdapter mAdapter;
    String[] mTitles = new String[]{"招商产品", "展厅介绍"};
    List<Fragment> mFragments = new ArrayList<>();
    Fragment_halldetail1 hall1;
    Fragment_halldetail2 hall2;
    View header = null;
    Merchants merchants = null;
    public static int cid = -1;
    public static String companyInfo="";
    public static String phoneNum="";
    public HallDetail() {
        super(R.layout.layout_hall_detail2);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_hall_detail);
//        ButterKnife.bind(this);
//        initView();
//    }

    protected void initView() {
        super.initView();
        if (merchants == null) {
            merchants = (Merchants) getIntent().getSerializableExtra("item");
            cid  = merchants.getCreatorId();
            companyInfo = merchants.getContent();
            phoneNum = merchants.getLinkmanTel();
            tv_count.setText("招商产品（"+merchants.getPnum()+"）");
            company.setText(merchants.getName());
            Glide.with(EAPPApplication.getInstance())
                    .load(Url.IMAGEPATH+merchants.getImg())
                    .error(R.mipmap.img_head)
                    .fitCenter()
                    .dontAnimate()
                    .transform(new GlideCircleTransform(EAPPApplication.getInstance()))
                    .into(iv);

            Glide.with(EAPPApplication.getInstance())
                    .load(Url.IMAGEPATH+merchants.getImg())
                    .fitCenter()
                    .dontAnimate()
                    .into(bg);
            bt_dial.setText("展厅电话("+merchants.getLinkman()+")");
        }
        initFragment();
        initVp();
    }

    private void initHeader() {
        header = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.layout_hall_detail, (ViewGroup) this.findViewById(android.R.id.content), false);
    }

    private void initFragment() {
        if (hall1 == null) {
            hall1 = Fragment_halldetail1.instance();
            mFragments.add(hall1);
        }
        if (hall2 == null) {
            hall2 = Fragment_halldetail2.instance();
            mFragments.add(hall2);
        }

    }

    public void initVp() {
        decorView = getWindow().getDecorView();
        vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        /** indicator固定宽度 */
        tabLayout = ViewFindUtils.find(decorView, R.id.tab);
        tabLayout.setTabSpaceEqual(true);
//       tabLayout_5.setViewPager(vp);
        tabLayout.setViewPager(vp, mTitles, this, (ArrayList<Fragment>) mFragments);
        vp.setCurrentItem(0);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


    @OnClick(R.id.dial)
    public void call(){
        super.dial(merchants.getLinkmanTel());
    }
    @OnClick(R.id.ll_back)
    public void goback(){
        this.finish();
    }
//
//    private void refresh() {
////        pageCount = 1;
////        data.clear();
////        common.createData(data);
////        lRecyclerViewAdapter.notifyDataSetChanged();
//        rv.refreshComplete(10);
//    }
}
