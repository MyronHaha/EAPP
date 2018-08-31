package com.hyhscm.myron.eapp.activity.Home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.platform.comapi.map.H;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.adapter.HomeAdapter;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ZS_Second extends BaseActivity {
    @BindView(R.id.rv_guide)
    RecyclerView guide;
    @BindView(R.id.paddingView)
            View paddingView;
    GridLayoutManager manager;
    String[] guideTitles = {"医疗设备", "医用耗材", "手术器械", "IVD", "家庭保健", "医疗服务", "康复设备", "其他"};
    int[] guideImgs = {R.mipmap.yiliaoshebei, R.mipmap.yiyonghaocai, R.mipmap.shoushuqixie, R.mipmap.ivd, R.mipmap.jiatingbaojian, R.mipmap.yiliaofuwu, R.mipmap.kangfu, R.mipmap.else_guide};
    List<Map> guideList = new ArrayList();

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_zs__second);
//    }
    public ZS_Second() {
        super(R.layout.activity_zs__second);
    }


    @Override
    public void changeStatusStyle() {


    }

    @Override
    protected void initView() {
        super.initView();
        ViewGroup.LayoutParams params = paddingView.getLayoutParams();
        params.height = getStatusBarHeight(this);
        initGuideView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void initGuideData() {
        for (int i = 0; i < guideTitles.length; i++) {
            Map map = new HashMap();
            map.put("title", guideTitles[i]);
            map.put("img", guideImgs[i]);
            guideList.add(map);
        }
    }

    private void initGuideView() {

        initGuideData();
        manager = new GridLayoutManager(this, 4);
        BaseQuickAdapter adapter = new BaseQuickAdapter(ZS_Second.this, "", R.layout.item_guide, guideList) {
            @Override
            protected void convert(BaseViewHolder holder, Object item) {
                L.e("aklsfjsldfjls");
                HashMap map = (HashMap) item;
                TextView title = (TextView) holder.getView(R.id.tv_title);
                title.setText((CharSequence) map.get("title"));
                ImageView img = (ImageView) holder.getView(R.id.iv);
                img.setImageResource((Integer) map.get("img"));
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }
        };
        guide.setAdapter(adapter);
        guide.setLayoutManager(manager);
    }
}
