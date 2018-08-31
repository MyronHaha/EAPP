package com.hyhscm.myron.eapp.data;

/**
 * Created by Jason on 2018/3/27.
 */

public class Advnotify {
    private String type;
    private String content;

    public Advnotify() {
    }

    public Advnotify(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
