package com.lb.download.model;


/***
 * 用户下载实体类
 * 
 * @author ASUS
 */
public class UserDownload {
	
	public UserDownload() {
		
	}
	public UserDownload(String username, String fullName, Float cOIN, String vipName, String regTime, String loginTime,
			String loginIp, String phoneNumber, String qq, String weixin, String email, String bankAccount,
			String bankName, String bankAddress, String regIp) {
		this.username = username;
		this.fullName = fullName;
		this.COIN = cOIN;
		this.vipName = vipName;
		this.regTime = regTime;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
		this.phoneNumber = phoneNumber;
		this.qq = qq;
		this.weixin = weixin;
		this.email = email;
		this.bankAccount = bankAccount;
		this.bankName = bankName;
		this.bankAddress = bankAddress;
		this.regIp = regIp;
	}
	private String username;// 会员账号
	private String fullName; // 会员姓名
	private Float COIN; // 余额
	private String vipName;// 会员等级
	private String regTime; // 注册时间
	private String loginTime;// 最近登录时间
	private String loginIp;// 最后登录IP
	private String phoneNumber;// 手机号码
	private String qq;// qq
	private String weixin;// 微信
	private String email;// 邮箱
	private String bankAccount;// 银行账号
	private String bankName;// 取现银行名称
	private String bankAddress;// 银行地址
	private String regIp;// 注册IP
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Float getCOIN() {
		return COIN;
	}
	public void setCOIN(Float cOIN) {
		this.COIN = cOIN;
	}
	public String getVipName() {
		return vipName;
	}
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getRegIp() {
		return regIp;
	}
	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}
   

}
