/**
 * 
 */
package com.lb.sys.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.lb.sys.service.ISysUserService;
import com.lb.sys.tools.IPSeeker;
import com.lb.sys.tools.SpringUtil;

/**
 * @author wz
 * @describe 会员信息实体类
 * @date 2017年10月16日
 */
public class UserVipModel {
	private Integer uid;
	private String username;//会员账号
	private String fullName; //会员姓名
	private String COIN; //余额
	private String agentCoin; //余额
	private String proxyName; //所属代理
	private String proxyId; //所属代理
	private String vipName;//会员等级
	private Date regTime; //注册时间
	private Date loginTime;//最近登录时间
	private String loginIp;//最后登录IP
	private String loginAddress;//最后登录地址
	private String remark; //备注信息
	private Integer vipId; //会员等级
	private Integer agentGrade;//代理等级
	private Byte onlineStatus = 0;//在线状态 1在线 0离线
	private Byte status; //状态 1启用状态2冻结状态
	private Integer CHAT_ADMIN;
	private String data;//返点配置
	private Integer userType;//用户类型 1为代理 2为会员
	private String autoSortUid;
	
	public Integer getUserType() {
		return userType;
	} 
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getData() {
		return data;
	} 
	public void setData(String data) {
		this.data = data;
	}
	public Integer getCHAT_ADMIN() {
		return CHAT_ADMIN;
	} 
	public void setCHAT_ADMIN(Integer cHAT_ADMIN) {
		CHAT_ADMIN = cHAT_ADMIN;
	}
	/**----------会员详情所需-------------*/
	private String phoneNumber;//手机号码
    private String qq;//qq
    private String line;
    private String weixin;//微信
    private String email;//邮箱
    private String bankAccount;//银行账号
    private String bankName;//取现银行名称
    private String bankAddress;//银行地址
    private BigDecimal withdrawNeedsum;//提款所需流水金额
    private BigDecimal betsum;//打码量
    private String regIp;//注册IP
    private String regSystem;//注册系统
    private String source;//来源
    private Integer score;//积分
    private String parentName;//所属上级
    private Byte channel;//所属上级
    
    //修改使用
    private String password;//密码
    private String rePassword;//确认密码
    private Byte bankBlackListStatus;//银行黑名单状态 1启用 0禁用
    //增加使用
    private String createBy;//创建所属
    private String rebateData;
    /**************标记该会员是否为锁定的状态*****************/
    private int isLocking;//1为锁定的状态 0为未锁定
	public int getIsLocking() {
		 //获取redis操作工具类
		ISysUserService iSysUserService= (ISysUserService)SpringUtil.getBean("sysUserServiceImpl");
		//判断该用户是否为锁定的状态
		boolean locking = iSysUserService.isLocking("lsIsLocking", this.username);
		if(locking==true) {
			return 1;
		}else {
			return 0;
		}
	}
	public void setIsLocking(int isLocking) {
		this.isLocking = isLocking;
	}
	/**
	 * @return the uid
	 */
	public Integer getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the name
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param name the name to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the cOIN
	 */
	public String getCOIN() {
		return COIN;
	}
	/**
	 * @param cOIN the cOIN to set
	 */
	public void setCOIN(String COIN) {
		this.COIN = COIN;
	}
	/**
	 * @return the proxyName
	 */
	public String getProxyName() {
		return proxyName;
	}
	/**
	 * @param proxyName the proxyName to set
	 */
	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}
	
	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}
	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the vipName
	 */
	public String getVipName() {
		return vipName;
	}
	/**
	 * @param vipName the vipName to set
	 */
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}
	/**
	 * @return the vipId
	 */
	public Integer getVipId() {
		return vipId;
	}
	/**
	 * @param vipId the vipId to set
	 */
	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}
	/**
	 * @return the regTime
	 */
	public Date getRegTime() {
		return regTime;
	}
	/**
	 * @param regTime the regTime to set
	 */
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	
	
	
	/**
	 * @return the onlineStatus
	 */
	public Byte getOnlineStatus() {
		return onlineStatus;
	}
	/**
	 * @param onlineStatus the onlineStatus to set
	 */
	public void setOnlineStatus(Byte onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	
	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}
	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}
	/**
	 * @return the weixin
	 */
	public String getWeixin() {
		return weixin;
	}
	/**
	 * @param weixin the weixin to set
	 */
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the bankAddress
	 */
	public String getBankAddress() {
		return bankAddress;
	}
	/**
	 * @param bankAddress the bankAddress to set
	 */
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	/**
	 * @return the withdrawNeedsum
	 */
	public BigDecimal getWithdrawNeedsum() {
		return withdrawNeedsum;
	}
	/**
	 * @param withdrawNeedsum the withdrawNeedsum to set
	 */
	public void setWithdrawNeedsum(BigDecimal withdrawNeedsum) {
		this.withdrawNeedsum = withdrawNeedsum;
	}
	/**
	 * @return the betsum
	 */
	public BigDecimal getBetsum() {
		return betsum;
	}
	/**
	 * @param betsum the betsum to set
	 */
	public void setBetsum(BigDecimal betsum) {
		this.betsum = betsum;
	}
	/**
	 * @return the regIp
	 */
	public String getRegIp() {
		return regIp;
	}
	/**
	 * @param regIp the regIp to set
	 */
	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	
	/**
	 * @return the regSystem
	 */
	public String getRegSystem() {
		return regSystem;
	}
	/**
	 * @param regSystem the regSystem to set
	 */
	public void setRegSystem(String regSystem) {
		this.regSystem = regSystem;
	}
	
	
	/**
	 * @return the pssword
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param pssword the pssword to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the rePssword
	 */
	public String getRePassword() {
		return rePassword;
	}
	/**
	 * @param rePssword the rePssword to set
	 */
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}
	
	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	
	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	/**
	 * @return the bankBlackListStatus
	 */
	public Byte getBankBlackListStatus() {
		return bankBlackListStatus;
	}
	/**
	 * @param bankBlackListStatus the bankBlackListStatus to set
	 */
	public void setBankBlackListStatus(Byte bankBlackListStatus) {
		this.bankBlackListStatus = bankBlackListStatus;
	}
	
	
	/**
	 * @return the proxyId
	 */
	public String getProxyId() {
		return proxyId;
	}
	/**
	 * @param proxyId the proxyId to set
	 */
	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}
	
	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	/**
	 * @return the loginIp
	 */
	public String getLoginIp() {
		return loginIp;
	}
	/**
	 * @param loginIp the loginIp to set
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginAddress() {
		String json_result = null;  
        try { 
        	if(!StringUtils.isEmpty(getLoginIp())) {
        		IPSeeker is = IPSeeker.getInstance();  
        		json_result =is.getAddress(getLoginIp());   
        	}
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		return json_result;
	}
	public void setLoginAddress(String loginAddress) {
		this.loginAddress = loginAddress;
	}
	public Byte getChannel() {
		return channel;
	}
	public void setChannel(Byte channel) {
		this.channel = channel;
	}
	public String getRebateData() {
		return rebateData;
	}
	public void setRebateData(String rebateData) {
		this.rebateData = rebateData;
	}
	@Override
	public String toString() {
		return "UserVipModel [uid=" + uid + ", username=" + username + ", fullName=" + fullName + ", COIN=" + COIN
				+ ", proxyName=" + proxyName + ", proxyId=" + proxyId + ", vipName=" + vipName + ", regTime=" + regTime
				+ ", loginTime=" + loginTime + ", loginIp=" + loginIp + ", loginAddress=" + loginAddress + ", remark="
				+ remark + ", vipId=" + vipId + ", onlineStatus=" + onlineStatus + ", status=" + status
				+ ", CHAT_ADMIN=" + CHAT_ADMIN + ", phoneNumber=" + phoneNumber + ", qq=" + qq + ", weixin=" + weixin
				+ ", email=" + email + ", bankAccount=" + bankAccount + ", bankName=" + bankName + ", bankAddress="
				+ bankAddress + ", withdrawNeedsum=" + withdrawNeedsum + ", betsum=" + betsum + ", regIp=" + regIp
				+ ", regSystem=" + regSystem + ", source=" + source + ", score=" + score + ", parentName=" + parentName
				+ ", channel=" + channel + ", password=" + password + ", rePassword=" + rePassword
				+ ", bankBlackListStatus=" + bankBlackListStatus + ", createBy=" + createBy + ", rebateData="
				+ rebateData + ", isLocking=" + isLocking + "]";
	}
	public String getAgentCoin() {
		return agentCoin;
	}
	public void setAgentCoin(String agentCoin) {
		this.agentCoin = agentCoin;
	}
	/**
	 * @return the agentGrade
	 */
	public Integer getAgentGrade() {
		return agentGrade;
	}
	/**
	 * @param agentGrade the agentGrade to set
	 */
	public void setAgentGrade(Integer agentGrade) {
		this.agentGrade = agentGrade;
	}
	public String getAutoSortUid() {
		return autoSortUid;
	}
	public void setAutoSortUid(String autoSortUid) {
		this.autoSortUid = autoSortUid;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	
	
}
