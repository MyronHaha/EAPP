package com.hyhscm.myron.eapp.data;

import okhttp3.Response;

/**
 * Created by Jason on 2017/12/19.
 */

public interface IResultHandler<T> {
    void onSuccess(Result<T> rs);
}
