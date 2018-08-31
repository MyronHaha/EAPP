package com.hyhscm.myron.eapp.data;

// 置顶类提交接口 返回积分
public class CommitResult {

    private String name;
    private int qty;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
    public int getQty() {
        return qty;
    }

}