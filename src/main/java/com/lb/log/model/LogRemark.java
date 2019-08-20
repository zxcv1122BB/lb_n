package com.lb.log.model;

import java.util.Date;

public class LogRemark {
    private Integer id;

    private String remarkUrl;

    private String url;

    private Date createDatetime;

    private Date updateDatetime;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemarkUrl() {
        return remarkUrl;
    }

    public void setRemarkUrl(String remarkUrl) {
        this.remarkUrl = remarkUrl == null ? null : remarkUrl.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}