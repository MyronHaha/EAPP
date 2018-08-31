package com.hyhscm.myron.eapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.Home.HomeProduct;
import com.hyhscm.myron.eapp.activity.Home.ProductCategory;
import com.hyhscm.myron.eapp.data.Merchants;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.ProductCategoryBean;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static Context context;
    List<List<ProductCategoryBean>> data = new ArrayList();
    public final int ITEM_2 = 2;  //0 0
    public final int ITEM_3 = 3;  //0,1,1
    public final int ITEM_4 = 4; //0,1 2 ,2
    public final int ITEM_5 = 5; //1,1,1,2,2
    public final int ITEM_6 = 6;//1,2,2,1,2,2
    public int[] views = {R.layout.item_2, R.layout.item_3, R.layout.item_4, R.layout.item_5, R.layout.item_6};
    public int[] bgs = {R.mipmap.bg_hong_cp, R.mipmap.bg_zi_cp, R.mipmap.bg_cheng_cp, R.mipmap.bg_lv_cp}; //4种背景
    public int[] dians = {R.mipmap.bg_dian_hong, R.mipmap.bg_dian_zi, R.mipmap.bg_dian_cheng, R.mipmap.bg_dian_lv};
    public String[] colors = {"#ff4e98", "#5888ff", "#ff6240", "#00cea7"};
    public int[] mores = {R.mipmap.icon_gengduo_hong, R.mipmap.icon_gengduo_zi, R.mipmap.icon_gengduo_cheng, R.mipmap.icon_gengduo_lv};

    public ProductCategoryAdapter(Context context, List<List<ProductCategoryBean>> datas) {
        this.context = context;
        this.data = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder", "");
        View view = null;
        RecyclerView.ViewHolder holder = null;
        if (viewType == 3) {
            view = LayoutInflater.from(context
            ).inflate(views[1], parent,
                    false);
            holder = new MyViewHolder3(view);
        } else if (viewType == 2) {
            view = LayoutInflater.from(context
            ).inflate(views[0], parent,
                    false);
            holder = new MyViewHolder2(view);
        } else if (viewType == 4) {
            view = LayoutInflater.from(context
            ).inflate(views[2], parent,
                    false);
            holder = new MyViewHolder4(view);
        } else if (viewType == 5) {
            view = LayoutInflater.from(context
            ).inflate(views[3], parent,
                    false);
            holder = new MyViewHolder5(view);
        } else if (viewType == 6) {
            view = LayoutInflater.from(context
            ).inflate(views[4], parent,
                    false);
            holder = new MyViewHolder6(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position % 4) {
            case 0:
                initHeaderUi(holder, 0, position);
                break;
            case 1:
                initHeaderUi(holder, 1, position);
                break;
            case 2:
                initHeaderUi(holder, 2, position);
                break;
            case 3:
                initHeaderUi(holder, 3, position);
                break;
            default:
                initHeaderUi(holder, 0, position);
                break;
        }
//  position 下子view
        List items = new ArrayList();
        items.clear();
        if (holder instanceof MyViewHolder2) {

            items.add(((MyViewHolder2) holder).item1);
            items.add(((MyViewHolder2) holder).item2);

        } else if (holder instanceof MyViewHolder3) {
            items.add(((MyViewHolder3) holder).item1);
            items.add(((MyViewHolder3) holder).item2);
            items.add(((MyViewHolder3) holder).item3);

        } else if (holder instanceof MyViewHolder4) {

            items.add(((MyViewHolder4) holder).item1);
            items.add(((MyViewHolder4) holder).item2);
            items.add(((MyViewHolder4) holder).item3);
            items.add(((MyViewHolder4) holder).item4);
        } else if (holder instanceof MyViewHolder5) {

            items.add(((MyViewHolder5) holder).item1);
            items.add(((MyViewHolder5) holder).item2);
            items.add(((MyViewHolder5) holder).item3);
            items.add(((MyViewHolder5) holder).item4);
            items.add(((MyViewHolder5) holder).item5);
        } else if (holder instanceof MyViewHolder6) {

            items.add(((MyViewHolder6) holder).item1);
            items.add(((MyViewHolder6) holder).item2);
            items.add(((MyViewHolder6) holder).item3);
            items.add(((MyViewHolder6) holder).item4);
            items.add(((MyViewHolder6) holder).item5);
            items.add(((MyViewHolder6) holder).item6);
        }
        setData(items, position);
        itemClick(items, position);

    }

    @Override
    public int getItemCount() {
        return data.size();//获取数据的个数
    }

    public static class MyBaseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_bg)
        RelativeLayout item_bg;
        @BindView(R.id.tv_category)
        TextView tv_category;
        @BindView(R.id.more)
        TextView more;

        public MyBaseHolder(View view) {
            super(view);
        }
    }

    /*22222*/
    public static class MyViewHolder2 extends MyBaseHolder {

        @BindView(R.id.item1)
        View item1;
        @BindView(R.id.item2)
        View item2;

        public MyViewHolder2(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    /*3333*/
    public static class MyViewHolder3 extends MyBaseHolder {
        @BindView(R.id.item1)
        View item1;
        @BindView(R.id.item2)
        View item2;
        @BindView(R.id.item3)
        View item3;

        public MyViewHolder3(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    /*44444*/
    public static class MyViewHolder4 extends MyBaseHolder {
        @BindView(R.id.item1)
        View item1;
        @BindView(R.id.item2)
        View item2;
        @BindView(R.id.item3)
        View item3;
        @BindView(R.id.item4)
        View item4;

        public MyViewHolder4(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    /*5555*/
    public static class MyViewHolder5 extends MyBaseHolder {
        @BindView(R.id.item1)
        View item1;
        @BindView(R.id.item2)
        View item2;
        @BindView(R.id.item3)
        View item3;
        @BindView(R.id.item4)
        View item4;
        @BindView(R.id.item5)
        View item5;

        public MyViewHolder5(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }


    }

    /*66666*/
    public static class MyViewHolder6 extends MyBaseHolder {
        @BindView(R.id.item1)
        View item1;
        @BindView(R.id.item2)
        View item2;
        @BindView(R.id.item3)
        View item3;
        @BindView(R.id.item4)
        View item4;
        @BindView(R.id.item5)
        View item5;
        @BindView(R.id.item6)
        View item6;

        public MyViewHolder6(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

    @Override
    public int getItemViewType(int position) {
        switch (data.get(position).size()) {
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

    //设置每一个itme
    public void setData(List<View> item_views, int pid) {
        List<ProductCategoryBean> list = data.get(pid);
        int s = item_views.size();
        for (int i = 0; i < s; i++) {
            ((TextView) item_views.get(i).findViewById(R.id.tv_title)).setText(list.get(i).getName());


            final TextView tv_name = ((TextView) item_views.get(i).findViewById(R.id.tv_content));

            ((TextView) item_views.get(i).findViewById(R.id.tv_content)).setText(list.get(i).getLabels());


            //获取视图树的全局事件改变时得到通知
            ViewTreeObserver vto = tv_name.getViewTreeObserver();
            //监听获取回掉函数
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //获取text View 的高度
                    L.e("width:"+tv_name.getWidth()+"height:"+tv_name.getHeight());
                    LinearGradient mLinearGradient = new LinearGradient(0, 0, tv_name.getWidth(), tv_name.getHeight(),
                            new int[]{Color.parseColor("#00de8d"), Color.parseColor("#00cfb9")},
                            null, Shader.TileMode.REPEAT);
                    tv_name.getPaint().setShader(mLinearGradient);
                    return true;
                }
            });


//            ((TextView)item_views.get(i).findViewById(R.id.tv_content)).setTextColor(Color.parseColor(colors[(pid%4)]));


            Glide
                    .with(context)
                    .load(Url.IMAGEPATH + list.get(i).getImg())
                    .dontAnimate()
                    .error(R.mipmap.img_nothingbg3)
                    .into((ImageView) item_views.get(i).findViewById(R.id.iv));
        }
    }

    public void addItem(RecyclerView.ViewHolder holder) {

    }

    public void itemClick(final List<View> items, final int pid) {
        int size = items.size();
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            items.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    MyToast.makeText(context,"类别"+(pid+1)+"，项"+(finalI+1),1000).show();
                    Intent intent = new Intent(context, HomeProduct.class);
                    intent.putExtra("from", "ProductCategory");
                    Bundle bundle = new Bundle();
                    bundle.putString("type", data.get(pid).get(finalI).getType() + "");
                    bundle.putString("sourceId", data.get(pid).get(finalI).getSourceId() + "");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
//                    mOnItemClickListener.onItemClick(items.get(finalI), finalI);
                }
            });
        }
    }

    public void initHeaderUi(RecyclerView.ViewHolder holder, int tid, final int pid) {
        if (holder instanceof MyBaseHolder) {
            L.e("typeText:" + data.get(pid).get(0).getTypeText());
            TextView tv_category = ((MyBaseHolder) holder).tv_category;
            tv_category.setText(data.get(pid).get(0).getTypeText());

            ((MyBaseHolder) holder).item_bg.setBackgroundResource(bgs[tid]);
            Drawable more = context.getResources().getDrawable(mores[tid]);
            more.setBounds(0, 0, more.getMinimumWidth(), more.getMinimumWidth());
            ((MyBaseHolder) holder).more.setCompoundDrawables(null, null, more, null);
            Drawable dian = context.getResources().getDrawable(dians[tid]);
            dian.setBounds(0, 0, dian.getMinimumWidth(), dian.getMinimumWidth());
            ((MyBaseHolder) holder).tv_category.setCompoundDrawables(dian, null, dian, null);
            ((MyBaseHolder) holder).more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, HomeProduct.class);
                    intent.putExtra("from", "ProductCategory");
                    Bundle bundle = new Bundle();
                    bundle.putString("type", data.get(pid).get(0).getType() + "");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
