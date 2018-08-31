package com.hyhscm.myron.eapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hyhscm.myron.eapp.EAPPApplication;
import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.common;
import com.mph.okdroid.response.GsonResHandler;

import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by jack chen on 2017-12-19.
 */

public abstract class BaseBeanActivity<T> extends BaseActivity   implements IResultHandler<T>, View.OnClickListener {

    protected String url="";
    protected String type="";
    public BaseBeanActivity() {

    }

    public BaseBeanActivity(int layoutResID) {
        this.layoutResID = layoutResID;
    }
    protected   void initData(String url) {
        this.url=url;
        initData(this.url,this.type);
    }
    protected   void initData(String url,String type) {
        this.url=url;
        this.type=type;
        params.clear();
        loadData();
    }

    protected   void loadData() {
        if("1".equals(this.type))
        {
            HttpCore.post(this.url, params, new GsonResHandler<Result<T>>() {
                @Override
                public void onFailed(int i, String s) {
                    common.promptDialog.showError(i + s);
                }
                @Override
                public void onSuccess(int i, Result<T> result) {
                    onResult(result);
                }
            });
        }else {
            HttpCore.get(this.url, params, new GsonResHandler<Result<T>>() {
                @Override
                public void onFailed(int i, String s) {
                    common.promptDialog.showError(i + s);
                }
                @Override
                public void onSuccess(int i, Result<T> result) {
                    onResult(result);
                }
            });
        }

    }

    @Override
    public void onSuccess(Result<T> rs) {

    }

    protected  void onResult(Result<T> rs){
        onSuccess(rs);
    }
}
