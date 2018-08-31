package com.hyhscm.myron.eapp.data;

import java.util.List;

/**
 * Created by Jason on 2017/12/19.
 */

public interface IListResultHandler<T> {
    void onSuccess(ListResult<T> rs);
}
