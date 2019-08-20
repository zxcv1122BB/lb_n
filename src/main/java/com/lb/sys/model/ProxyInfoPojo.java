package com.lb.sys.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.lb.sys.tools.IPSeeker;

public class ProxyInfoPojo {
    private Integer id;
    private Integer pid;
    private String loginAccount;
    private Byte status;
    private String password;
    private String proxyName;
    private String parentName;
    private String coinPssword;
    private String tell;
    private String email;
    private String qq;
    private String bankName;
    private String bankAccount;
    private String bankAddress;
    private BigDecimal rebateRatio;
    private BigDecimal coin;
    private String regIp;
    private Date regTime;
    private Byte delStatus;
    private Byte proxyLevel;
    private String createdUser;
    private String updataUser;
    private Date createTime;
    private Date updataTime;
    private String lastLoginAddress;
    private String lastLoginIp;
    private Date lastLoginTime;
    public String getLastLoginAddress() {
    	String json_result = null;  
        try { 
        	if(!StringUtils.isEmpty(getLastLoginIp())) {
        		IPSeeker is = IPSeeker.getInstance();  
        		json_result =is.getAddress(getLastLoginIp());   
        	}
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		return json_result;
	}
	public void setLastLoginAddress(String lastLoginAddress) {
		this.lastLoginAddress = lastLoginAddress;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getLoginAccount() {
		return loginAccount;
	}
	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProxyName() {
		return proxyName;
	}
	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}
	public String getCoinPssword() {
		return coinPssword;
	}
	public void setCoinPssword(String coinPssword) {
		this.coinPssword = coinPssword;
	}
	public String getTell() {
		return tell;
	}
	public void setTell(String tell) {
		this.tell = tell;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public BigDecimal getRebateRatio() {
		return rebateRatio;
	}
	public void setRebateRatio(BigDecimal rebateRatio) {
		this.rebateRatio = rebateRatio;
	}
	public BigDecimal getCoin() {
		return coin;
	}
	public void setCoin(BigDecimal coin) {
		this.coin = coin;
	}
	public String getRegIp() {
		return regIp;
	}
	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public Byte getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Byte delStatus) {
		this.delStatus = delStatus;
	}
	public Byte getProxyLevel() {
		return proxyLevel;
	}
	public void setProxyLevel(Byte proxyLevel) {
		this.proxyLevel = proxyLevel;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getUpdataUser() {
		return updataUser;
	}
	public void setUpdataUser(String updataUser) {
		this.updataUser = updataUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdataTime() {
		return updataTime;
	}
	public void setUpdataTime(Date updataTime) {
		this.updataTime = updataTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
    
}