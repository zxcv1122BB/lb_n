package com.lb.game.model;

import java.util.Date;

public class GamePlayed {
    private Integer id;

    private String name;

    private Byte type;

    private Short groupid;

    private Integer sort;

    private Integer maxcount;

    private Integer maxbet;

    private Float minamount;

    private Boolean satatus;

    private Date createDate;

    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Short getGroupid() {
        return groupid;
    }

    public void setGroupid(Short groupid) {
        this.groupid = groupid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(Integer maxcount) {
        this.maxcount = maxcount;
    }

    public Integer getMaxbet() {
        return maxbet;
    }

    public void setMaxbet(Integer maxbet) {
        this.maxbet = maxbet;
    }

    public Float getMinamount() {
        return minamount;
    }

    public void setMinamount(Float minamount) {
        this.minamount = minamount;
    }

    public Boolean getSatatus() {
        return satatus;
    }

    public void setSatatus(Boolean satatus) {
        this.satatus = satatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}