/**
 * 
 */
package com.lb.betting.model;

import java.util.Date;
import java.util.List;

/**
 * @describe 投注订单详情
 */
public class BettingOrderDetail {
	private String orderId;//订单ID
	private String userName;//订单ID
	private Date actionTime;//投注时间
	private Float amount;//投注金额
	private String typeName;//类型名称
	private String groupName;//二级玩法名称
	private Integer type;
	private String actionIp;//投注IP
	private String actionData;//投注号码
	private String actionNo;//期号
	private String matchIds;
	private String actionDataResult;//中奖结果数据
	private String returnAwardRate;
	private String bonus;//中奖金额
	private String winningDetails;//中奖明细
	private Byte isCal;//是否开奖
	private boolean isShow;//展示结果 false不展示期号展示赔率比分投注方式 true展示期号不展示投注方式赔率比分 
	private Byte ticketStatus;//出票状态 0未出票，1已出票
	private Byte status;//状态 0未中奖 1中奖 2撤单 3成功
	private List<BettingInfoDetail> list;//投注详情列表
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
	 * @return the winningDetails
	 */
	public String getWinningDetails() {
		return winningDetails;
	}

	/**
	 * @param winningDetails the winningDetails to set
	 */
	public void setWinningDetails(String winningDetails) {
		this.winningDetails = winningDetails;
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
	 * @return the isShow
	 */
	public boolean isShow() {
		if(type != null && type == 2) {
			isShow = true;
		}
		return isShow;
	}

	/**
	 * @return the ticketStatus
	 */
	public Byte getTicketStatus() {
		return ticketStatus;
	}

	/**
	 * @param ticketStatus the ticketStatus to set
	 */
	public void setTicketStatus(Byte ticketStatus) {
		this.ticketStatus = ticketStatus;
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
	 * @return the list
	 */
	public List<BettingInfoDetail> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<BettingInfoDetail> list) {
		this.list = list;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the matchIds
	 */
	public String getMatchIds() {
		return matchIds;
	}

	/**
	 * @param matchIds the matchIds to set
	 */
	public void setMatchIds(String matchIds) {
		this.matchIds = matchIds;
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

	/**
	 * @return the returnAwardRate
	 */
	public String getReturnAwardRate() {
		return returnAwardRate;
	}

	/**
	 * @param returnAwardRate the returnAwardRate to set
	 */
	public void setReturnAwardRate(String returnAwardRate) {
		this.returnAwardRate = returnAwardRate;
	}
	
}
