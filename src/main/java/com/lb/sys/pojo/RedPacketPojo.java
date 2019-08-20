package com.lb.sys.pojo;

import java.util.Date;

public class RedPacketPojo {
	//红包钥匙
	private String redPacketKey;
	//红包标题
	private String redPacketTitle;
	//红包总金额
	private Float redPacketMoney;
	//红包开始时间
	private Date startTime;
	//红包结束时间
	private Date endTime;
	
	public RedPacketPojo() {
		super();
	}
	
	public RedPacketPojo(String redPacketKey,String redPacketTitle,Float redPacketMoney,Date startTime, Date endTime) {
		this.redPacketKey = redPacketKey;
		this.redPacketTitle=redPacketTitle;
		this.redPacketMoney=redPacketMoney;
		this.startTime=startTime;
		this.endTime = endTime;
	}

	public String getRedPacketKey() {
		return redPacketKey;
	}
	public void setRedPacketKey(String redPacketKey) {
		this.redPacketKey = redPacketKey;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getRedPacketTitle() {
		return redPacketTitle;
	}

	public void setRedPacketTitle(String redPacketTitle) {
		this.redPacketTitle = redPacketTitle;
	}

	public Float getRedPacketMoney() {
		return redPacketMoney;
	}

	public void setRedPacketMoney(Float redPacketMoney) {
		this.redPacketMoney = redPacketMoney;
	}
	
}
