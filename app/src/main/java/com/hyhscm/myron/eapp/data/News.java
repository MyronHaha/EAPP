package com.hyhscm.myron.eapp.data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author jack chen
 */
public class News implements Serializable {

    private int id;
    private String title;
    private String content;
    private String img;
    private int isCommend;
    private String keys;
    private int sort;
    private int state;
    private int hit;
    private java.util.Date creationTime;
    private int creatorId;
    private int updateId;
    private java.util.Date updateTime;
    private int type;
    private java.util.Date dataDate;
    private java.util.Date realTime;
    private int praise;
    private String ext;
    private String ct;
    private int ctype;
    private Date displayTime;

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }
    public int getHasad() {
        return hasad;
    }

    public void setHasad(int hasad) {
        this.hasad = hasad;
    }

    private int hasad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getIsCommend() {
        return isCommend;
    }

    public void setIsCommend(int isCommend) {
        this.isCommend = isCommend;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public java.util.Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(java.util.Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getUpdateId() {
        return updateId;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }

    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public java.util.Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(java.util.Date dataDate) {
        this.dataDate = dataDate;
    }

    public java.util.Date getRealTime() {
        return realTime;
    }

    public void setRealTime(java.util.Date realTime) {
        this.realTime = realTime;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

}
