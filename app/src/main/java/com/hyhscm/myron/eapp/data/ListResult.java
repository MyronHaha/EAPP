package com.hyhscm.myron.eapp.data;

import java.util.List;

/**
 * Created by Jason on 2017/12/19.
 */

public class ListResult<T> extends BaseResult {

    private List<T> biz;

    public void setBiz(List<T> biz) {
        this.biz = biz;
    }

    public List<T> getBiz() {
        return biz;
    }
}