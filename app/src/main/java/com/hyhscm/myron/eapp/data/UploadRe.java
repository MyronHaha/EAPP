package com.hyhscm.myron.eapp.data;

/**
 * Created by Jason on 2017/12/22.
 */

public class UploadRe {

    private String path;
    private String id;
    private String data;
    private String postfix;
    private String newName;
    private String name;
    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String getData() {
        return data;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }
    public String getPostfix() {
        return postfix;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
    public String getNewName() {
        return newName;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}