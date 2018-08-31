package com.hyhscm.myron.eapp.data;

/**
 * Created by Jason on 2017/12/19.
 */

public abstract class BaseResult {
    private String errorCode;
    private String msg;
    private boolean success;
    private String total;
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }


}
