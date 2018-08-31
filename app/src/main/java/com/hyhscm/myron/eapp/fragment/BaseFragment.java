package com.hyhscm.myron.eapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.BaseActivity;
import com.hyhscm.myron.eapp.activity.Home.ENews;
import com.hyhscm.myron.eapp.activity.ListBaseBeanActivity;
import com.hyhscm.myron.eapp.data.IListResultHandler;
import com.hyhscm.myron.eapp.data.ListResult;
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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/12/27.
 */

public abstract class BaseFragment<T> extends Fragment implements IListResultHandler<T>{


    @BindView(R.id.rv)
    protected LRecyclerView rv;
    protected BaseQuickAdapter mAdapter;
    protected List<T> datas = new ArrayList<T>();
    protected LRecyclerViewAdapter mRecyclerViewAdapter;
    protected String url = "";
    protected String type = "";
    protected int count = 0;
    protected int pageSize = 0;
    protected  String multiType = "";
    int layoutResID;
    int itemResID;


    protected HashMap<String, Object> params = new HashMap();

    public BaseFragment() {
    }

    @SuppressLint("ValidFragment")
    public BaseFragment(int layoutResID,int itemResId) {
        this.layoutResID = layoutResID;
        this.itemResID = itemResId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResID, null);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    protected void initListView(int l) {
        mAdapter = new BaseQuickAdapter<T>(getActivity(), multiType, l, datas) {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.notifyDataSetChanged();
        //加载样式
        rv.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        rv.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        rv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetUtil.getNetWorkState(getActivity())==NetUtil.NETWORK_NONE){
                    MyToast.makeText(getActivity(),"请检查网络连接",1000).show();
                    rv.refreshComplete(pageSize);
                    return;
                }
                refreshData(0);
            }
        });
        rv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(NetUtil.getNetWorkState(getActivity())==NetUtil.NETWORK_NONE){
                    MyToast.makeText(getActivity(),"请检查网络连接",1000).show();
                    rv.refreshComplete(pageSize);
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
            if (data.size() == 0) {
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
        this.url = url;
        this.pageSize = pageSize;
        initData(this.url, this.type, pageSize);
    }

    protected void initData(String url, String type, int pageSize) {
        this.count = 0;
        this.pageSize = pageSize;
        this.url = Url.HOSTNAME+url;
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
        if ("1".equals(this.type)) {
            HttpCore.post(this.url, params, new GsonResHandler<ListResult<T>>() {
                @Override
                public void onFailed(int i, String s) {
                    Log.e("error---",s);
//                    common.promptDialog.showError(i + s);
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
                    if (biz != null && biz != "") {
                        T[] datas = (T[]) gson.fromJson(biz, getTclass());
                        List<T> datas1 = Arrays.asList(datas);
                        result.setBiz(datas1);
                    }
                    onResult(result);
                }
            });
        } else {
            HttpCore.get(this.url, params, new GsonResHandler<ListResult<T>>() {
                @Override
                public void onFailed(int i, String s) {
//                    common.promptDialog.showError(i + s);
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
                        if (biz != null && biz != "") {
                            T[] datas = (T[]) gson.fromJson(biz, getTclass());
                            List<T> datas1 = Arrays.asList(datas);
                            result.setBiz(datas1);
                        }
                        onResult(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    protected List splitString(String str,String regex){
        String[] arr = str.split(regex);
        return Arrays.asList(arr);
    }
    protected void launchActivityWithIntent(Intent intent) {
        startActivity(intent);
    }


    public void searchWithKey(){
        params.put("k",((ENews)getActivity()).keyword);
        loadData();
    }


}
