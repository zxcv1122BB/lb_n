package com.lb.download.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PreferentialCardRecord {
    private String batch;//批次

    private Integer orderNumber;//序号
    
    private String device;//设备

    private String vips;//限制vip
    
    private String account;//优惠卡账号

    private String password;//优惠卡密码

    private Integer money;//优惠卡面额

    private String status;//优惠卡启用状态：1启用，0禁用
    
    private String flag;//优惠卡使用状态：1使用，0未使用

    private String startTime;//优惠卡开始时间
   
    private String endTime;//优惠卡结束时间

    private String createTime;
    
    private String updateTime;
    
    private String userName;//使用者账号
    
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getVips() {
		return vips;
	}

	public void setVips(String vips) {
		this.vips = vips;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		if(status==1) {
			this.status = "启用";
		}else if(status==0){
			this.status = "禁用";
		}
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(Byte flag) {
		if(flag==1) {
			this.flag = "已使用";
		}else if(flag==0){
			this.flag = "未使用";
		}
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime);
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdate_time(Date updateTime) {
		this.updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime);
	}

  
}