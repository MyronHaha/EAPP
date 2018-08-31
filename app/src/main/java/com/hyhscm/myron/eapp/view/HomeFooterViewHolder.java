package com.hyhscm.myron.eapp.view;

import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.hyhscm.myron.eapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Jason on 2017/11/23.
 */

public class HomeFooterViewHolder {
    @BindView(R.id.rv_footer)
    public LRecyclerView rv;
    public HomeFooterViewHolder(View header) {
        ButterKnife.bind(this, header);
    }
}
