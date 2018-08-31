package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.data.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class ProductCategoryItemAdapter extends RecyclerView.Adapter<ProductCategoryItemAdapter.MyViewHolder> {
    private static Context context;
    List<Product> data = new ArrayList();
    public final int ITEM_2 = 2;  //0 0
    public final int ITEM_3 = 3;  //0,1,1
    public final int ITEM_4 = 4; //0,1 2 ,2
    public final int ITEM_5 = 5; //1,1,1,2,2
    public final int ITEM_6 = 6;//1,2,2,1,2,2
    public int[] views = {R.layout.product_item_big,R.layout.product_item_mid,R.layout.product_item_small};
    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    private static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public ProductCategoryItemAdapter(Context context, List datas) {
        this.context = context;
        this.data = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder", "");
        View view = null;
        view = LayoutInflater.from(context
        ).inflate(R.layout.item_small_hall3, parent,
                false);
//        }

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    //bindData
    @Override
    public void onBindViewHolder(MyViewHolder viewholder, int i) {

        if (data.size() > 0) {
            Product product = data.get(i);
//            Glide.with(context)
//                    .load(Url.IMAGEPATH + product.getImg())
//                    .error(R.mipmap.img_nothingbg3)
//                    .dontAnimate()
//                    .into(viewholder.iv);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (data.size()) {
            case 2:
                return ITEM_2;
            case 3:
                return ITEM_3;
            case 4:
                return ITEM_4;
            case 5:
                return ITEM_5;
            case 6:
                return ITEM_6;
        }
        return super.getItemViewType(position);
    }
}
