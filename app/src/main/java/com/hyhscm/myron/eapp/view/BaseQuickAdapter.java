package com.hyhscm.myron.eapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.adapter.HomeAdapter;
import com.hyhscm.myron.eapp.data.Demand;
import com.hyhscm.myron.eapp.data.News;
import com.hyhscm.myron.eapp.data.Product;
import com.hyhscm.myron.eapp.data.SMsg;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.DensityUtil;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/12/4.
 */

public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {


    private Context mContext;
    private int mLayoutResID;
    private List<T> data;

    private static final int EMPTY_VIEW_TYPE = -1000;
    private static final int FOOTER_VIEW_TYPE = -2000;
    private static final int FIRSTNEWSTYPE = -3000;
    private static final int MSGBIG = -4000;
    private static final int MSGSMALL = -5000;
    private int mEmptyLayoutResID = 0;
    //广告
    private static final int IMG_ADV_TYPE = -6000; //图片广告
    private static final int NORMAL_ADV_TYPE = -7000;// 需求招商广告


    private OnItemClickListener mItemClickListener = null;
    private int pageCount;
    private int currentState;
    private final int STATE_LOADING = 1;
    private final int STATE_LASTED = 2;
    private OnPageLoadListener mOnPageLoadListener;
    private boolean isHasMore = true;
    String type;

    public boolean isHasMore() {
        return isHasMore;
    }

    public void setHasMore(boolean hasMore) {
        isHasMore = hasMore;
    }

    public BaseQuickAdapter(Context context, String type, int layoutResID, List<T> data) {
        mContext = context;
        this.mLayoutResID = layoutResID;
        this.type = type;
        this.data = data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder;
        View view;
        //load empty view
        if (viewType == EMPTY_VIEW_TYPE) {
             view = LayoutInflater.from(mContext).inflate(mEmptyLayoutResID, parent, false);
            holder = new BaseViewHolder(view);
        } else if (viewType == FOOTER_VIEW_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.rcv_footer_item, parent, false);
            holder = new BaseViewHolder(view);
        } else if (viewType == FIRSTNEWSTYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_toutiao, parent, false);
            holder = new BaseViewHolder(view);

        } else if (viewType == MSGBIG) {
             view = LayoutInflater.from(mContext).inflate(R.layout.item_sysmsg, parent, false);
            holder = new MsgView1(view);
        } else if (viewType == MSGSMALL) {
             view = LayoutInflater.from(mContext).inflate(R.layout.item_sysmsg_small, parent, false);
            holder = new MsgView2(view);
        } else if (viewType == IMG_ADV_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.test, parent, false);
            holder = new NewsAdv(view);
        } else if(viewType == NORMAL_ADV_TYPE){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_adv_normal, parent, false);
            holder = new BaseViewHolder(view);
        }else {
            view = LayoutInflater.from(mContext).inflate(mLayoutResID, parent, false);
            holder = new BaseViewHolder(view);
            //添加点击事件
            if (mItemClickListener != null) {
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mItemClickListener.onItemClick(holder, holder.getAdapterPosition());
//                    }
//                });
                setListener(view, holder);
            }
        }
        return holder;
    }

    public void setListener(View view, final BaseViewHolder holder) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(holder, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == FOOTER_VIEW_TYPE) {
//            switch (currentState) {
//                case STATE_LOADING:
//                    holder.getView(R.id.pb_footer).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.tv_footer).setVisibility(View.GONE);
//                    mOnPageLoadListener.onPageLoad();//请求下一页数据
//                    break;
//                case STATE_LASTED:
//                    holder.getView(R.id.tv_footer).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.pb_footer).setVisibility(View.GONE);
//                    holder.setText(R.id.tv_footer, "无更多数据");
//                    break;
//            }
        } else if (getItemViewType(position) == FIRSTNEWSTYPE) {
            ImageView iv = (ImageView) holder.getView(R.id.iv);
            TextView title = (TextView) holder.getView(R.id.tv_title);
            TextView see = (TextView) holder.getView(R.id.tv_see);
            TextView ago = (TextView) holder.getView(R.id.tv_ago);
            News news = (News) data.get(position);
            title.setText(news.getTitle());
            ago.setText(common.dataToStringSimple(news.getCreationTime()));
            see.setText(news.getHit() + "");
            common.loadIntoUseFitWidth(mContext, Url.WEBPATH + news.getImg(), R.mipmap.img_nothingbg2, iv);

        } else {
            if (data != null && data.size() > 0) {
                convert(holder, data.get(position));
            }
        }
    }

    //去加载更多
    public final void isLoadingMore() {
        if (currentState == STATE_LOADING) {
            return;
        }
        currentState = STATE_LOADING;
        notifyItemRangeChanged(data.size(), 1);//刷新最后一项的内容
    }

    public void addAll(List<T> beanList) {
        data.clear();
        data.addAll(beanList);
    }

    //将获取到的数据集合加到之前的集合中来
    public void appendList(List<T> beanList) {
        if (beanList.size() == pageCount) {
            currentState = STATE_LOADING;
            isHasMore = true;
        } else {
            currentState = STATE_LASTED;
            isHasMore = false;
        }
        int positionStart = data.size();
        data.addAll(beanList);
        int itemCount = beanList.size();
        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            //notifyDataSetChanged();
            notifyItemRangeChanged(positionStart, itemCount + 1);
        }
    }


    @Override
    public int getItemCount() {
        if (data.size() == 0 && mEmptyLayoutResID != 0) {
            return 1;
        }
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.size() == 0 && mEmptyLayoutResID != 0) {
            return EMPTY_VIEW_TYPE;
        } else if (data.size() == position) {
            return FOOTER_VIEW_TYPE;
        } else if (type.equals("news")) {
            News news = null;
            if (data.size() > 0) {
                news = (News) data.get(position);
                //新闻大图；
                if (position == 0 || news.getIsCommend() == 1) {
                    return FIRSTNEWSTYPE;
                }
                //广告
                if (news.getHasad()==1) {
                    return IMG_ADV_TYPE;
                }
            }
        }
//        else if(((SMsg)data.get(position)).getBtype()==1||((SMsg)data.get(position)).getBtype()==2||((SMsg)data.get(position)).getBtype()==3||((SMsg)data.get(position)).getBtype()==4||((SMsg)data.get(position)).getBtype()==5||((SMsg)data.get(position)).getBtype()==7){
//            return MSGBIG;
//        }
        else if (data.get(position) instanceof SMsg) {
            if (!((SMsg) data.get(position)).getContent().equals("")) {
                return MSGBIG;
            } else if (((SMsg) data.get(position)).getContent().equals("")) {
                return MSGSMALL;
            }

        }else if(data.get(position) instanceof Demand){
            Demand  de = (Demand) data.get(position);
            if(de.getHasad()==1) {
                return NORMAL_ADV_TYPE;
            }
        }else if(data.get(position) instanceof Product){
            Product po = (Product) data.get(position);
        if(po.getHasad()==1 && !type.equals("hall")){
                return NORMAL_ADV_TYPE;
            }
        }
        return super.getItemViewType(position);
    }

    //set EmptyView
    public void setEmptyView(int layoutResID) {
        this.mEmptyLayoutResID = layoutResID;
    }

    //setOnItemClickListener
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(BaseViewHolder base, int position);
    }

    protected abstract void convert(BaseViewHolder holder, T item);


    public void setOnPageLoadListener(OnPageLoadListener onPageLoadListener, int pageCount) {
        mOnPageLoadListener = onPageLoadListener;
        this.pageCount = pageCount;
    }

    public interface OnPageLoadListener {
        void onPageLoad();
    }

    public class MsgView1 extends BaseViewHolder {

        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.date)
        public TextView date;
        @BindView(R.id.content)
        public TextView content;
        @BindView(R.id.iv)
        public ImageView image;

        public MsgView1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MsgView2 extends BaseViewHolder {

        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.date)
        public TextView date;
        @BindView(R.id.iv)
        public ImageView image;

        public MsgView2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class NewsAdv extends BaseViewHolder {
        @BindView(R.id.iv)
        ImageView iv;

        public NewsAdv(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
//    public class NormalAdv extends BaseViewHolder {
//        @BindView(R.id.iv_user)
//        ImageView iv;
//        @BindView(R.id.tv_user)
//        TextView tv_user;
//        @BindView(R.id.tv_content)
//        TextView tv_content;
//        @BindView(R.id.tv_expan)
//        TextView expan;
//
//        public NormalAdv(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
}
