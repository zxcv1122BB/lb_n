package com.lb.report.model;

import java.math.BigDecimal;

import com.alibaba.druid.util.StringUtils;

public class TeamCount {

	private String vipName;
	
	private String userType;
	
	private int uid;
	
	private String user_name;
	
	private BigDecimal coin;
	
	private BigDecimal fandianSum;
	
	private String cLog;
	private BigDecimal regGivingSum;//注册赠送
	private BigDecimal depositGivingSum;//充值赠送
	private BigDecimal addMoneySum;//加款
	private BigDecimal subtractMoneySum;//扣款
	private BigDecimal activityBonusCoin;//活动中奖
	private BigDecimal betRebate;//投注返利
	private BigDecimal rgGivingMoneySum;//人工赠送
	
	private String gBet;
	private Integer bSum;
	private BigDecimal betSum;
	private BigDecimal bonusSum;
	
	private BigDecimal depositSum;
	
	private BigDecimal withdrawSum;
	
	public BigDecimal getCoin() {
		return coin;
	}

	public void setCoin(BigDecimal coin) {
		this.coin = coin;
	}

	public BigDecimal getRgGivingMoneySum() {
		return rgGivingMoneySum;
	}

	public void setRgGivingMoneySum(BigDecimal rgGivingMoneySum) {
		this.rgGivingMoneySum = rgGivingMoneySum;
	}

	public String getgBet() {
		return null;
	}

	public void setgBet(String gBet) {
		if(StringUtils.isEmpty(gBet)) {
			return ;
		}
		String[] split = gBet.split(",");
		bSum=Integer.valueOf(split[0]);
		betSum=new BigDecimal(split[1]);
		bonusSum=new BigDecimal(split[2]);
		this.gBet = null;
	}

	public String getcLog() {
		return null;
	}
	public void setcLog(String cLog) {
		if(StringUtils.isEmpty(cLog)) {
			return ;
		}
		String[] split = cLog.split(",");
		
		addMoneySum=new BigDecimal(split[0]);
		subtractMoneySum=new BigDecimal(split[1]);
		if(split.length>2) {
			rgGivingMoneySum=new BigDecimal(split[2]);
			regGivingSum=new BigDecimal(split[3]);
			depositGivingSum=new BigDecimal(split[4]);
			activityBonusCoin=new BigDecimal(split[5]);
			betRebate=new BigDecimal(split[6]);
		}
		this.cLog = null;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public BigDecimal getFandianSum() {
		return fandianSum;
	}

	public void setFandianSum(BigDecimal fandianSum) {
		this.fandianSum = fandianSum;
	}

	public BigDecimal getDepositGivingSum() {
		return depositGivingSum;
	}

	public void setDepositGivingSum(BigDecimal depositGivingSum) {
		this.depositGivingSum = depositGivingSum;
	}

	public BigDecimal getAddMoneySum() {
		return addMoneySum;
	}

	public void setAddMoneySum(BigDecimal addMoneySum) {
		this.addMoneySum = addMoneySum;
	}

	public BigDecimal getSubtractMoneySum() {
		return subtractMoneySum;
	}

	public void setSubtractMoneySum(BigDecimal subtractMoneySum) {
		this.subtractMoneySum = subtractMoneySum;
	}

	public BigDecimal getActivityBonusCoin() {
		return activityBonusCoin;
	}

	public void setActivityBonusCoin(BigDecimal activityBonusCoin) {
		this.activityBonusCoin = activityBonusCoin;
	}

	public BigDecimal getBetSum() {
		return betSum;
	}

	public void setBetSum(BigDecimal betSum) {
		betSum = betSum;
	}

	public BigDecimal getBonusSum() {
		return bonusSum;
	}

	public void setBonusSum(BigDecimal bonusSum) {
		this.bonusSum = bonusSum;
	}

	public BigDecimal getDepositSum() {
		return depositSum;
	}

	public void setDepositSum(BigDecimal depositSum) {
		this.depositSum = depositSum;
	}

	public BigDecimal getWithdrawSum() {
		return withdrawSum;
	}

	public void setWithdrawSum(BigDecimal withdrawSum) {
		this.withdrawSum = withdrawSum;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	public BigDecimal getRegGivingSum() {
		return regGivingSum;
	}

	public void setRegGivingSum(BigDecimal regGivingSum) {
		this.regGivingSum = regGivingSum;
	}

	public Integer getbSum() {
		return bSum;
	}

	public void setbSum(Integer bSum) {
		this.bSum = bSum;
	}

	public BigDecimal getBetRebate() {
		return betRebate;
	}

	public void setBetRebate(BigDecimal betRebate) {
		this.betRebate = betRebate;
	}

	
}
