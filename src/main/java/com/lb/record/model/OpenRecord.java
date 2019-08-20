package com.lb.record.model;

import java.util.Date;

public class OpenRecord {

	private String matchType;    //比赛类型
	private String matchId;      //比赛id
	private Integer dealNum;     //处理条数
	private Integer dealErrorNum;//异常条数
	private Long dealTime;       //处理时间（毫秒）
	private String dealMsg;      //处理异常信息
	private String openState;    //开奖状态,0 未处理 1 已处理
	private Date updateTime;     //开奖时间
	private Date createTime;     //比赛完成时间
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	public Integer getDealNum() {
		return dealNum;
	}
	public void setDealNum(Integer dealNum) {
		this.dealNum = dealNum;
	}
	public Integer getDealErrorNum() {
		return dealErrorNum;
	}
	public void setDealErrorNum(Integer dealErrorNum) {
		this.dealErrorNum = dealErrorNum;
	}
	public Long getDealTime() {
		return dealTime;
	}
	public void setDealTime(Long dealTime) {
		this.dealTime = dealTime;
	}
	public String getDealMsg() {
		return dealMsg;
	}
	public void setDealMsg(String dealMsg) {
		this.dealMsg = dealMsg;
	}
	public String getOpenState() {
		return openState;
	}
	public void setOpenState(String openState) {
		this.openState = openState;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
