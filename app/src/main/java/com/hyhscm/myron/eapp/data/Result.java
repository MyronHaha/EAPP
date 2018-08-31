package com.hyhscm.myron.eapp.data;


import java.util.List;

public class Result<T> extends BaseResult {

    private T biz;

    public void setBiz(T biz) {
        this.biz = biz;
    }

    public T getBiz() {
        return biz;
    }
}

