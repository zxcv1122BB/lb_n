package com.lb.ip.model;

import java.util.Date;


/****
 * ip白名单实体类
 * @author ASUS
 */

public class IpWhiteList {
    private Integer id;//ip白名单记录编号

    private String ipaddress;//ip地址

    private Date inputtime;//录入时间

    private Date updatetime;//修改时间

    private Integer status;//状态  0:正常 1:删除/禁用

    private String remark;//备注信息

//    private Integer userid;//使用人编号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress == null ? null : ipaddress.trim();
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

//    public Integer getUserid() {
//        return userid;
//    }
//
//    public void setUserid(Integer userid) {
//        this.userid = userid;
//    }
}