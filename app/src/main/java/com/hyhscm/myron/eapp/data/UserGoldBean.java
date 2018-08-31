package com.hyhscm.myron.eapp.data;

/**
 * Created by Jason on 2018/4/1.
 */

import java.util.List;

/**
 * Copyright 2018 bejson.com
 * 用户jf
 */

public class UserGoldBean {

    private int amount;
    private int credit;
    private List<String> details;
    private int id;
    private int userId;
    private String userName;
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
    public int getCredit() {
        return credit;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
    public List<String> getDetails() {
        return details;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

}