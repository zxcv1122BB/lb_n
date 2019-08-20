/**
 * 
 */
package com.lb.betting.model;

import java.util.Date;
import java.util.List;

/**
 * @author wz
 * @describe 投注信息用户详情
 * @date 2017年10月9日
 */
public class UserDetail {

	private Integer uid;
	private String userName;//用户名
	//private Date loginTime;//上次登录时间
	//private String loginIp;//上次登录ip
	private Date registerTime;//注册时间
	private String registerIp;//注册ip
	private String grade;//等级
	private Integer score;//积分
	private Integer scoreTotal;//累计积分
	private String coin;//余额
	private String fcion;//冻结金额
	private String canGetCoin;//可取出金额
	private String notGetCoin;//不可取出金额
	private String vipGrade;//VIP等级
	private String phoneNumber;//手机号码
	private String QQ;//QQ号码
	private String weChat;//微信
	private List<LoginLog> loginLog;//用户登录日志列表  格式：时间--IP

	private Byte status;//1正常 

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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/*
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	*/
	/**
	 * @return the canGetCoin
	 */
	public String getCanGetCoin() {
		return canGetCoin;
	}

	/**
	 * @param canGetCoin the canGetCoin to set
	 */
	public void setCanGetCoin(String canGetCoin) {
		this.canGetCoin = canGetCoin;
	}

	/**
	 * @return the notGetCoin
	 */
	public String getNotGetCoin() {
		return notGetCoin;
	}

	/**
	 * @param notGetCoin the notGetCoin to set
	 */
	public void setNotGetCoin(String notGetCoin) {
		this.notGetCoin = notGetCoin;
	}

	/**
	 * @return the registerTime
	 */
	public Date getRegisterTime() {
		return registerTime;
	}

	/**
	 * @param registerTime the registerTime to set
	 */
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	/**
	 * @return the registerIp
	 */
	public String getRegisterIp() {
		return registerIp;
	}

	/**
	 * @param registerIp the registerIp to set
	 */
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
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
	 * @return the scoreTotal
	 */
	public Integer getScoreTotal() {
		return scoreTotal;
	}

	/**
	 * @param scoreTotal the scoreTotal to set
	 */
	public void setScoreTotal(Integer scoreTotal) {
		this.scoreTotal = scoreTotal;
	}

	/**
	 * @return the coin
	 */
	public String getCoin() {
		return coin;
	}

	/**
	 * @param coin the coin to set
	 */
	public void setCoin(String coin) {
		this.coin = coin;
	}

	/**
	 * @return the fcion
	 */
	public String getFcion() {
		return fcion;
	}

	/**
	 * @param fcion the fcion to set
	 */
	public void setFcion(String fcion) {
		this.fcion = fcion;
	}

	/**
	 * @return the vipGrade
	 */
	public String getVipGrade() {
		return vipGrade;
	}

	/**
	 * @param vipGrade the vipGrade to set
	 */
	public void setVipGrade(String vipGrade) {
		this.vipGrade = vipGrade;
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
	 * @return the qQ
	 */
	public String getQQ() {
		return QQ;
	}

	/**
	 * @param qQ the qQ to set
	 */
	public void setQQ(String qQ) {
		QQ = qQ;
	}

	/**
	 * @return the weChat
	 */
	public String getWeChat() {
		return weChat;
	}

	/**
	 * @param weChat the weChat to set
	 */
	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	

	/**
	 * @return the loginLog
	 */
	public List<LoginLog> getLoginLog() {
		return loginLog;
	}

	/**
	 * @param loginLog the loginLog to set
	 */
	public void setLoginLog(List<LoginLog> loginLog) {
		this.loginLog = loginLog;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserDetail [uid=" + uid + ", userName=" + userName 
				+ ", registerTime=" + registerTime + ", registerIp=" + registerIp + ", grade=" + grade + ", score="
				+ score + ", scoreTotal=" + scoreTotal + ", coin=" + coin + ", fcion=" + fcion + ", vipGrade="
				+ vipGrade + ", phoneNumber=" + phoneNumber + ", QQ=" + QQ + ", weChat=" + weChat + ", loginLog="
				+ loginLog + ", status=" + status + "]";
	}
	
	
	
}
class LoginLog{
	private Date loginTime;
	private String loginIp;
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
	 * @return the loginIP
	 */
	public String getLoginIp() {
		return loginIp;
	}
	/**
	 * @param loginIP the loginIP to set
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
	
}
