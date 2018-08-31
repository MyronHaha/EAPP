package com.hyhscm.myron.eapp.data;

import java.io.Serializable;


/**
 * @author jack chen
 */
public class Bizinfo implements Serializable {

    private int id;
    private String agents;
    private String channels;
    private String residents;
    private String depts;
    private java.util.Date creationTime;
    private int creatorId;
    private String areas;
    private String areas1;
    private String areas2;
    private String hosp1;
    private String hosp2;
    private int btype;
    private String mtype;
    private String ext;
    private String labels; //优势
    private String deptsid; //keshi
    private String channelid; //qudao
    private String areasid; //
    private String agentsid; //daili
    private String residentsid;//常驻地
    private String areasid1;// 省
    private String areasid2;//city
    private String hospid1;  //医院代理
    private String hospid2;  //医院上量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgents() {
        return agents;
    }

    public void setAgents(String agents) {
        this.agents = agents;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getResidents() {
        return residents;
    }

    public void setResidents(String residents) {
        this.residents = residents;
    }

    public String getDepts() {
        return depts;
    }

    public void setDepts(String depts) {
        this.depts = depts;
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

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getAreas1() {
        return areas1;
    }

    public void setAreas1(String areas1) {
        this.areas1 = areas1;
    }

    public String getAreas2() {
        return areas2;
    }

    public void setAreas2(String areas2) {
        this.areas2 = areas2;
    }

    public String getHosp1() {
        return hosp1;
    }

    public void setHosp1(String hosp1) {
        this.hosp1 = hosp1;
    }

    public String getHosp2() {
        return hosp2;
    }

    public void setHosp2(String hosp2) {
        this.hosp2 = hosp2;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getDeptsid() {
        return deptsid;
    }

    public void setDeptsid(String deptsid) {
        this.deptsid = deptsid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getAreasid() {
        return areasid;
    }

    public void setAreasid(String areasid) {
        this.areasid = areasid;
    }

    public String getAgentsid() {
        return agentsid;
    }

    public void setAgentsid(String agentsid) {
        this.agentsid = agentsid;
    }

    public String getResidentsid() {
        return residentsid;
    }

    public void setResidentsid(String residentsid) {
        this.residentsid = residentsid;
    }

    public String getAreasid1() {
        return areasid1;
    }

    public void setAreasid1(String areasid1) {
        this.areasid1 = areasid1;
    }

    public String getAreasid2() {
        return areasid2;
    }

    public void setAreasid2(String areasid2) {
        this.areasid2 = areasid2;
    }

    public String getHospid1() {
        return hospid1;
    }

    public void setHospid1(String hospid1) {
        this.hospid1 = hospid1;
    }

    public String getHospid2() {
        return hospid2;
    }

    public void setHospid2(String hospid2) {
        this.hospid2 = hospid2;
    }

}
