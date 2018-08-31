package com.hyhscm.myron.eapp.view;

import android.view.View;
import android.widget.GridView;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.adapter.GridViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Jason on 2017/11/22.
 */

public class HomeHeaderViewHolder {
    @BindView(R.id.banner_guide_content)
    public BGABanner banner;
    @BindView(R.id.gridView)
    public GridView gridView;
    public HomeHeaderViewHolder(View header) {
        ButterKnife.bind(this, header);
    }
}
