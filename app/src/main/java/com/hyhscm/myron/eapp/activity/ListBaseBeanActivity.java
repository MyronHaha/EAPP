package com.hyhscm.myron.eapp.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.net.NetUtil;
import com.hyhscm.myron.eapp.net.Url;
import com.hyhscm.myron.eapp.utils.L;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;
import com.hyhscm.myron.eapp.view.BaseQuickAdapter;
import com.hyhscm.myron.eapp.view.BaseViewHolder;
import com.mph.okdroid.response.GsonResHandler;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import butterknife.BindView;

/**
 * Created by jack chen on 2017-12-19.
 */

public abstract class ListBaseBeanActivity<T> extends BaseActivity implements IListResultHandler<T>, View.OnClickListener {
    @BindView(R.id.rv)
    protected LRecyclerView rv;

    protected BaseQuickAdapter mAdapter;
    protected List<T> datas = new ArrayList<T>();
    protected LRecyclerViewAdapter mRecyclerViewAdapter;
    protected String url = "";
    protected String type = "";
    protected int count = 0;
    protected int pageSize = 0;
    protected String multiType = "";
    public ListBaseBeanActivity() {
    }

    public ListBaseBeanActivity(int layoutResID) {
        this.layoutResID = layoutResID;
    }


    protected void initListView(int l) {

        mAdapter = new BaseQuickAdapter<T>(this, multiType, l, datas) {
            @Override
            protected void convert(BaseViewHolder holder, T item) {
                bindView(holder, item);
            }
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }
        };
        mRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(mRecyclerViewAdapter);
//        mRecyclerViewAdapter.notifyDataSetChanged();
        //加载样式
        rv.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        rv.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        rv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetUtil.getNetWorkState(ListBaseBeanActivity.this) == NetUtil.NETWORK_NONE) {
                    MyToast.makeText(ListBaseBeanActivity.this, "请检查网络连接", 1000).show();
                    rv.refreshComplete(pageSize);
                    return;
                }
                refreshData(0);
            }
        });
        rv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (NetUtil.getNetWorkState(ListBaseBeanActivity.this) == NetUtil.NETWORK_NONE) {
                    MyToast.makeText(ListBaseBeanActivity.this, "请检查网络连接", 1000).show();
                    return;
                }
                refreshData(1);
            }
        });
        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int i) {
                if (datas.size() > 0) {
                    T d = datas.get(i);
                    itemClick(view, i, d);
                }
            }
        });
        mRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int i) {
                if (datas.size() > 0) {
                    T d = datas.get(i);
                    itemLongClick(view, i, d);
                }
            }
        });
    }

    private void refreshData(int type) {
        isrefresh = true;
        this.count = mAdapter.getItemCount() - 1;
        isrefreshComplete = type;
        if (isrefreshComplete == 0) {
            this.count = 0;
        }
        loadData(isrefreshComplete, this.count);
    }

    boolean isrefresh = false;
    int isrefreshComplete = 0;

    protected void refresh(List<T> data) {
        if (isrefreshComplete == 0) {
            mAdapter.addAll(data);
            if (data.size() == 0) {
                mAdapter.setEmptyView(R.layout.layout_empty);
            }
        } else {
            if (data.size() < pageSize) {
                rv.setNoMore(true);
            } else {
                mAdapter.appendList(data);
            }
        }
        isrefreshComplete = 0;
        count = 0;

        rv.refreshComplete(pageSize);
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    protected void loadData(int t, int count) {
        loadData(count);
    }

    protected void loadData(int count) {
        loadData();
    }

    protected void itemClick(View v, int i, T item) {

    }

    protected void itemLongClick(View v, int i, T item) {

    }

    protected void bindView(BaseViewHolder holder, T item) {

    }

    protected void initData(String url, int pageSize) {
        this.url = Url.HOSTNAME + url;
        initData(this.url, this.type, pageSize);
    }

    protected void initData(String url, String type, int pageSize) {
        this.count = 0;
        this.pageSize = pageSize;
        this.url = url;
        this.type = type;
        loadData();
    }

    Class<?> _tclass = null;

    Class<?> getTclass() {
        if (_tclass == null) {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Object o = Array.newInstance((Class<?>) entityClass, 0);
            _tclass = o.getClass();
        }
        return _tclass;
    }

    protected void loadData() {
        if (pageSize != 0) {
//            if(!isrefresh){
//                count=0;
//            } else {
//                isrefresh=false;
//            }
            params.put("count", count + "");
            params.put("limit", pageSize + "");
        }
        //  more
        if ("1".equals(this.type)) {
            dialog.showInfo("加载中...");
            HttpCore.post(this.url, params, new GsonResHandler<ListResult<T>>() {
                @Override
                public void onFailed(int i, String s) {
//                    common.promptDialog.showError(NetUtil.getErrorString(s));
                    L.e("onfailed:" + s);
                }

                @Override
                public void onSuccess(int i, ListResult<T> result) {
                    GsonBuilder builder = new GsonBuilder();
                    builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                    // Register an adapter to manage the date types as long values
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return new Date(json.getAsJsonPrimitive().getAsLong());
                        }
                    });
                    Gson gson = builder.create();
                    String biz = gson.toJson(result.getBiz());
                    if (result.getSuccess()) {
                        if (biz != null && biz != "") {
                            T[] datas = (T[]) gson.fromJson(biz, getTclass());
                            List<T> datas1 = Arrays.asList(datas);
                            L.e("sizeziez" + datas1.size());
                            result.setBiz(datas1);
                        }
                        onResult(result);
                    } else {
                        AutoLogin(new IResultHandler() {
                            @Override
                            public void onSuccess(Result rs) {
                                if (rs.getSuccess()) {
                                    loadData();
                                }
                            }
                        });
                    }

                }
            });
        } else {
            //refresh
            HttpCore.get(this.url, params, new GsonResHandler<ListResult<T>>() {
                @Override
                public void onFailed(int i, String s) {
//                    dialog.showError(NetUtil.getErrorString(s));
                    L.e("onfailed" + s);
                }

                @Override
                public void onSuccess(int i, ListResult<T> result) {
                    GsonBuilder builder = new GsonBuilder();
                    builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                    Gson gson = builder.create();
                    try {
                        String biz = "";
                        if (result != null) {
                            biz = gson.toJson(result.getBiz());
                        }
                        if (result.getSuccess()) {
                            if (biz != null && biz != "") {
                                T[] datas = (T[]) gson.fromJson(biz, getTclass());
                                List<T> datas1 = Arrays.asList(datas);
                                L.e("sizeziez" + datas1.size());
                                result.setBiz(datas1);
                            }
                            onResult(result);
                        } else {
                            AutoLogin(new IResultHandler() {
                                @Override
                                public void onSuccess(Result rs) {
                                    if (rs.getSuccess()) {
                                        loadData();
                                    }
                                }
                            });
                        }
//                        if (biz != null && biz != "") {
//                            T[] datas = (T[]) gson.fromJson(biz, getTclass());
//                            List<T> datas1 = Arrays.asList(datas);
//                            result.setBiz(datas1);
//                        }
//                        onResult(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    // 非手刷时刷新回到顶部
//                    if(!rv.isOnTop()){
//                        rv.scrollToPosition(0);
//                    }
                }
            });
        }

    }

    @Override
    public void onSuccess(ListResult<T> rs) {

    }

    protected void onResult(ListResult<T> rs) {
        onSuccess(rs);
        refresh(rs.getBiz());
        L.e("onResultsize:" + rs.getBiz().size() + "");
    }

    public LRecyclerViewAdapter getmRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

}
