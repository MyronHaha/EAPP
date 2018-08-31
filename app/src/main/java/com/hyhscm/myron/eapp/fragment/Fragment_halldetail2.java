package com.hyhscm.myron.eapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Home.HallDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 *和first_fragment 新闻首页公用布局 ，就一个list
 * Created by Jason on 2017/12/13.
 */

public class Fragment_halldetail2 extends Fragment {
  @BindView(R.id.tx)
  TextView tv;

    public static Fragment_halldetail2 instance() {
        Fragment_halldetail2 view = new Fragment_halldetail2();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_des_hall, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv.setText(HallDetail.companyInfo);
    }


}