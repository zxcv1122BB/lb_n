package com.lb.download.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WithdrawRecord {
    private String userName;//会员账号
    private String orderId;//会员单号
    private String userType;//用户类型
    private Float amount;//提现金额
    private String accountId;//银行名字
    private String accountName;//存款人姓名
    private String account;//账号后10位
    private String applyTime;//申请时间
    private String operateTime;//处理时间
    private String stateName;//状态
    private String operateUser;//操作人
    private String info;//备注
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(applyTime);
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(operateTime);
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getOperateUser() {
		return operateUser;
	}
	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(Byte userType) {
		if(userType>1) {
			this.userType = "会员";
		}else {
			this.userType = "代理";
		}
	}
}
