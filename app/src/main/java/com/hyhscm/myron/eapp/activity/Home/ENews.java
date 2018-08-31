package com.hyhscm.myron.eapp.activity.Home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.fragment.Fragment_News_1;
import com.hyhscm.myron.eapp.fragment.Fragment_News_2;
import com.hyhscm.myron.eapp.fragment.Fragment_News_3;
import com.hyhscm.myron.eapp.fragment.Fragment_News_4;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.ViewFindUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jason on 2017/12/06.
 * e械新闻
 */

public class ENews extends BaseActivity implements OnTabSelectListener {
    @BindView(R.id.query)
    EditText query;
    @BindView(R.id.search_clear)
    ImageButton clearText;
    private Fragment_News_1 mFirstFragment = null;
    private Fragment_News_2 mSecondFragment = null;
    private Fragment_News_3 mThirdFragment = null;
    private Fragment_News_4 mFourthFragment = null;

    public static String keyword = "";

    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"e链头条","行业动态","政策法规"
            ,"职场精英"};
    private MyPagerAdapter mAdapter;
    private void initFragments() {
        if (mFirstFragment == null) {
            mFirstFragment = Fragment_News_1.instance();
        }
        if (mSecondFragment == null) {
            mSecondFragment = Fragment_News_2.instance();
        }
        if (mThirdFragment == null) {
            mThirdFragment = Fragment_News_3.instance();
        }
        if (mFourthFragment == null) {
            mFourthFragment = Fragment_News_4.instance();
        }
        mFragments.add(mFirstFragment);
        mFragments.add(mSecondFragment);
        mFragments.add(mThirdFragment);
        mFragments.add(mFourthFragment);
    }

    public ENews(){
       super(R.layout.activity_sliding_tab);
    }
    protected void initView(){
        common.changeTitle(this, "医械头条");
        common.hideObjs(this, new int[]{R.id.tv_right});
        initFragments();
        initVp();
        query.clearFocus();
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!"".equals(editable.toString())){
                    clearText.setVisibility(View.VISIBLE);
                    keyword = editable.toString();
                    if(mFirstFragment!=null){
                        mFirstFragment.searchWithKey();
                    }
                    if(mSecondFragment!=null){
                        mSecondFragment.searchWithKey();
                    }
                    if(mThirdFragment!=null){
                        mThirdFragment.searchWithKey();
                    }
                    if(mFourthFragment!=null){
                        mFourthFragment.searchWithKey();
                    }

                }else{
                    keyword = editable.toString();
                    clearText.setVisibility(View.INVISIBLE);
                    if(mFirstFragment!=null){
                        mFirstFragment.searchWithKey();
                    }
                    if(mSecondFragment!=null){
                        mSecondFragment.searchWithKey();
                    }
                    if(mThirdFragment!=null){
                        mThirdFragment.searchWithKey();
                    }
                    if(mFourthFragment!=null){
                        mFourthFragment.searchWithKey();
                    }
                }
            }
        });
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sliding_tab);
//        common.goBack(this);
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorPrimary);
//
//    }

    public void initVp() {
        float scale = this.getResources().getDisplayMetrics().scaledDensity;
        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setOffscreenPageLimit(4);
        vp.setAdapter(mAdapter);

        /** indicator固定宽度 */
        SlidingTabLayout tabLayout_5 = ViewFindUtils.find(decorView, R.id.tl_5);
//        tabLayout_5.setTextsize((getResources().getDimension(R.dimen.x28)-0.5F)/scale);
//        tabLayout_5.setTabSpaceEqual(false);
//       tabLayout_5.setViewPager(vp);
        tabLayout_5.setViewPager(vp, mTitles, this, mFragments);
        vp.setCurrentItem(0);

    }

    @Override
    public void onTabSelect(int position) {
     Log.e("hahah",position+"");
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.search_clear)
    public void clearString(){
        query.getText().clear();
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


}
