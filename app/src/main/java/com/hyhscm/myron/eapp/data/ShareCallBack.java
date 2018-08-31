package com.hyhscm.myron.eapp.data;

/**
 * Created by Jason on 2018/3/30.
 *
 */

public class ShareCallBack {
    private String content;
    private int id;
    private String name;
    private String qty;
    private String remark;
    private int state;
    private int type;
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
    public String getQty() {
        return qty;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }
}
