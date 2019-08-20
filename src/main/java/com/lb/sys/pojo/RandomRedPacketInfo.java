package com.lb.sys.pojo;

import java.util.Date;
import java.util.List;

import com.lb.activity.model.RedPacketManagement;

public class RandomRedPacketInfo {
	//随机红包
	private List<Float> list;
	//红包结束时间
	private Date endTime;
	
	//红包
	private RedPacketManagement redPacketManagement;
	
	public RandomRedPacketInfo(List<Float> list, Date endTime,RedPacketManagement redPacketManagement) {
		this.list = list;
		this.endTime = endTime;
		this.setRedPacketManagement(redPacketManagement);
	}

	public RandomRedPacketInfo() {
	}

	public List<Float> getList() {
		return list;
	}

	public void setList(List<Float> list) {
		this.list = list;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public RedPacketManagement getRedPacketManagement() {
		return redPacketManagement;
	}

	public void setRedPacketManagement(RedPacketManagement redPacketManagement) {
		this.redPacketManagement = redPacketManagement;
	}

	
}
