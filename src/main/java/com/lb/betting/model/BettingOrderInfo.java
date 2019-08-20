/**
 * 
 */
package com.lb.betting.model;

import java.util.Date;

/**
 * @describe 投注信息实体类
 */
public class BettingOrderInfo {
	

	private Integer id;//主键id
	private String orderId;//随机订单号(YYYYMMDD+0000)
	private Integer uid;//投注用户ID
	private String userName;//投注人帐号
	private String userType;//NORMAL 正常 PROBATION 试玩
	private String name;//用户真实姓名
	private Integer type;//投注彩种; 与game_type表绑定
	private String typeName;//类型名称
	private Integer playedGroupId;//玩法组ID 与game_play_group表绑定
	private String playedGroupName;//玩法组名称
	private Integer playedId;//玩法ID 与game_played表绑定
	private String playedName;//玩法名称
	private String betType;//投注方式
	private String actionNo;//足球时对应场次号(逗号分隔)
	private String actionIp;//投注IP
	private String errormgs;//投注IP
	private Integer actionNum;//投注注数
	private String actionData;//投注号码
	private Date actionTime;//投注时间
	private Date inputtime;//投注时间
	private Date calTime;//开奖时间
	private Integer times;//倍数
	private Float amount;//投注金额
	private Integer getCount;//中奖注数
	private String bonus;//中奖金额
	private String actionDataResult;//中奖结果数据
	private Byte isCal;//是否开奖
	private Byte status;//状态 0未中奖 1中奖 2撤单 3成功
	private String openNo;//开奖号码
	private String betOdds;
	private Integer id3;
	private String name3;
	private String openIssue;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * @return the playedGroupId
	 */
	public Integer getPlayedGroupId() {
		return playedGroupId;
	}
	/**
	 * @param playedGroupId the playedGroupId to set
	 */
	public void setPlayedGroupId(Integer playedGroupId) {
		this.playedGroupId = playedGroupId;
	}
	/**
	 * @return the playedGroupName
	 */
	public String getPlayedGroupName() {
		return playedGroupName;
	}
	/**
	 * @param playedGroupName the playedGroupName to set
	 */
	public void setPlayedGroupName(String playedGroupName) {
		this.playedGroupName = playedGroupName;
	}
	/**
	 * @return the playedId
	 */
	public Integer getPlayedId() {
		return playedId;
	}
	/**
	 * @param playedId the playedId to set
	 */
	public void setPlayedId(Integer playedId) {
		this.playedId = playedId;
	}
	/**
	 * @return the playedName
	 */
	public String getPlayedName() {
		return playedName;
	}
	/**
	 * @param playedName the playedName to set
	 */
	public void setPlayedName(String playedName) {
		this.playedName = playedName;
	}
	/**
	 * @return the betType
	 */
	public String getBetType() {
		return betType;
	}
	/**
	 * @param betType the betType to set
	 */
	public void setBetType(String betType) {
		this.betType = betType!=null?betType.replace(",", " ").replace("*", "串"):"";
	}
	/**
	 * @return the actionNo
	 */
	public String getActionNo() {
		return actionNo;
	}
	/**
	 * @param actionNo the actionNo to set
	 */
	public void setActionNo(String actionNo) {
		this.actionNo = actionNo;
	}
	/**
	 * @return the actionIp
	 */
	public String getActionIp() {
		return actionIp;
	}
	/**
	 * @param actionIp the actionIp to set
	 */
	public void setActionIp(String actionIp) {
		this.actionIp = actionIp;
	}
	/**
	 * @return the actionNum
	 */
	public Integer getActionNum() {
		return actionNum;
	}
	/**
	 * @param actionNum the actionNum to set
	 */
	public void setActionNum(Integer actionNum) {
		this.actionNum = actionNum;
	}
	/**
	 * @return the actionData
	 */
	public String getActionData() {
		return actionData;
	}
	/**
	 * @param actionData the actionData to set
	 */
	public void setActionData(String actionData) {
		this.actionData = actionData;
	}
	/**
	 * @return the actionTime
	 */
	public Date getActionTime() {
		return actionTime;
	}
	/**
	 * @param actionTime the actionTime to set
	 */
	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}
	/**
	 * @return the times
	 */
	public Integer getTimes() {
		return times;
	}
	/**
	 * @param times the times to set
	 */
	public void setTimes(Integer times) {
		this.times = times;
	}
	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	/**
	 * @return the getCount
	 */
	public Integer getGetCount() {
		return getCount;
	}
	/**
	 * @param getCount the getCount to set
	 */
	public void setGetCount(Integer getCount) {
		this.getCount = getCount;
	}
	/**
	 * @return the bonus
	 */
	public String getBonus() {
		if(this.type != null &&  this.type == 2) {
			if(this.actionDataResult != null) {
				if("NotGet".equals(this.actionDataResult)) {
					bonus = "未中奖";
				}else {
					String bonusArr[] = this.actionDataResult.split("@");
					StringBuilder sbr = new StringBuilder();
					for(String str : bonusArr) {
						if(str.indexOf(":")>0) {
							sbr.append(","+str.replace("1:", "一等奖:").replace("2:", "二等奖:")+"注");
						}
					}
					if(sbr.length() > 0) {
						bonus = sbr.toString().substring(1);
					}
				}
			}
		} 
		return bonus;
	}
	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(String bonus) {
		this.bonus = bonus;
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
	 * @return the isCal
	 */
	public Byte getIsCal() {
		return isCal;
	}
	/**
	 * @param isCal the isCal to set
	 */
	public void setIsCal(Byte isCal) {
		this.isCal = isCal;
	}
	public String getErrormgs() {
		return errormgs;
	}
	public void setErrormgs(String errormsg) {
		this.errormgs = errormsg;
	}
	public Date getInputtime() {
		return inputtime;
	}
	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}
	/**
	 * @return the calTime
	 */
	public Date getCalTime() {
		return calTime;
	}
	/**
	 * @param calTime the calTime to set
	 */
	public void setCalTime(Date calTime) {
		this.calTime = calTime;
	}
	/**
	 * @return the actionDataResult
	 */
	public String getActionDataResult() {
		return actionDataResult;
	}
	/**
	 * @param actionDataResult the actionDataResult to set
	 */
	public void setActionDataResult(String actionDataResult) {
		this.actionDataResult = actionDataResult;
	}
	public String getOpenNo() {
		return openNo;
	}
	public void setOpenNo(String openNo) {
		this.openNo = openNo;
	}
	/**
	 * @return the betOdds
	 */
	public String getBetOdds() {
		return betOdds;
	}
	/**
	 * @param betOdds the betOdds to set
	 */
	public void setBetOdds(String betOdds) {
		this.betOdds = betOdds;
	}
	public Integer getId3() {
		return id3;
	}
	public void setId3(Integer id3) {
		this.id3 = id3;
	}
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}
	public String getOpenIssue() {
		return openIssue;
	}
	public void setOpenIssue(String openIssue) {
		this.openIssue = openIssue;
	}
	
}
