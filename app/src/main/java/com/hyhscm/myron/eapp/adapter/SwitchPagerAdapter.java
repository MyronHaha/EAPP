package com.hyhscm.myron.eapp.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.hyhscm.myron.eapp.activity.User.NeedPublish;
import com.hyhscm.myron.eapp.fragment.Fragment_switch;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jason on 2017/11/23.
 */

public class SwitchPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;
    private Context mContext;
    private List tagList = new ArrayList();

    public SwitchPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        int type;
        switch (position) {
            case 0:
                type = 1;
                break;
            case 1:
                type = 2;
                break;
            default:
                type = 1;
                break;
        }
        return Fragment_switch.newInstance(type);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "最新商机";
            case 1:
                return "最新代理";
            default:
                return "最新商机";
        }
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
//      notifyDataSetChanged();
        return super.getItemPosition(object);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment_switch fg = (Fragment_switch) super.instantiateItem(container, position);
//        fg.initList1(position==0?1:2);
//        return fg;
//    }
}
