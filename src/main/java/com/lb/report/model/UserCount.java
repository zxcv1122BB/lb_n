package com.lb.report.model;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.druid.util.StringUtils;

public class UserCount {

	private String user_name;
	
	private String agent_count;
	
	private Date reg_time;
	
	private BigDecimal coin;
	
	private BigDecimal betsum;
	
	private BigDecimal withdraw_needsum;
	
	private String cLog;
	private BigDecimal regGivingSum;
	private BigDecimal depositGivingSum;
	private BigDecimal addMoneySum;
	private BigDecimal subtractMoneySum;
	private BigDecimal activityBonusCoin;
	private BigDecimal rgGivingMoneySumCoin;
	private BigDecimal proxyAddMoneySumCoin ;// 代理加款
	private BigDecimal proxySubtractMoneySumCoin ;// 代理减款 
	private BigDecimal betRebate;//投注返利
	
	private String gBet;
	private BigDecimal fCSumCoin;
	private BigDecimal hisFSumCoin;
	private BigDecimal fBSumCoin;
	private BigDecimal bCSumCoin;
	private BigDecimal gSumCoin;
	private BigDecimal nSumCoin;
	private BigDecimal qSumCoin;
	
	private BigDecimal depositSum;
	private BigDecimal depositRgSum;
	
	private String lastDeposit;
	private BigDecimal lastDepositAwamount;
	private String lastDepositTime;
	private String lastDepositInfo;
	
	private BigDecimal withdrawSum;
	
	private String lastWithdraw;
	private BigDecimal lastWithdrwAwamount;
	private String lastWithdrawTime;
	private String lastWithdrawInfo;


	public BigDecimal getProxyAddMoneySumCoin() {
		return proxyAddMoneySumCoin;
	}
	public void setProxyAddMoneySumCoin(BigDecimal proxyAddMoneySumCoin) {
		this.proxyAddMoneySumCoin = proxyAddMoneySumCoin;
	}
	public BigDecimal getProxySubtractMoneySumCoin() {
		return proxySubtractMoneySumCoin;
	}
	public void setProxySubtractMoneySumCoin(BigDecimal proxySubtractMoneySumCoin) {
		this.proxySubtractMoneySumCoin = proxySubtractMoneySumCoin;
	}
	public BigDecimal getRgGivingMoneySumCoin() {
		return rgGivingMoneySumCoin;
	}
	public void setRgGivingMoneySumCoin(BigDecimal rgGivingMoneySumCoin) {
		this.rgGivingMoneySumCoin = rgGivingMoneySumCoin;
	}
	public String getcLog() {
		
		return null;
	}
	public void setcLog(String cLog) {
		if(StringUtils.isEmpty(cLog)) {
			return;
		}
		String[] split = cLog.split(",");
		regGivingSum=new BigDecimal(split[0]);
		depositGivingSum=new BigDecimal(split[1]);
		addMoneySum=new BigDecimal(split[2]);
		subtractMoneySum=new BigDecimal(split[3]);
		rgGivingMoneySumCoin=new BigDecimal(split[4]);
		activityBonusCoin=new BigDecimal(split[5]);
		proxyAddMoneySumCoin=new BigDecimal(split[6]);
		proxySubtractMoneySumCoin=new BigDecimal(split[7]);
		betRebate=new BigDecimal(split[8]);
		depositSum = new BigDecimal(split[9]);
		withdrawSum = new BigDecimal(split[10]);
		this.cLog = null;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAgent_count() {
		return agent_count;
	}
	public void setAgent_count(String agent_count) {
		this.agent_count = agent_count;
	}
	public Date getReg_time() {
		return reg_time;
	}
	public void setReg_time(Date reg_time) {
		this.reg_time = reg_time;
	}
	public BigDecimal getCoin() {
		return coin;
	}
	public void setCoin(BigDecimal coin) {
		this.coin = coin;
	}
	public BigDecimal getBetsum() {
		return betsum;
	}
	public void setBetsum(BigDecimal betsum) {
		this.betsum = betsum;
	}
	public BigDecimal getWithdraw_needsum() {
		return withdraw_needsum;
	}
	public void setWithdraw_needsum(BigDecimal withdraw_needsum) {
		this.withdraw_needsum = withdraw_needsum;
	}
	public BigDecimal getRegGivingSum() {
		return regGivingSum;
	}
	public void setRegGivingSum(BigDecimal regGivingSum) {
		this.regGivingSum = regGivingSum;
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
	public String getgBet() {
		return gBet;
	}
	public void setgBet(String gBet) {
		if(StringUtils.isEmpty(gBet)) {
			return ;
		}
		String[] split = gBet.split(",");
		fCSumCoin=new BigDecimal(split[0]);
		hisFSumCoin=new BigDecimal(split[1]);
		fBSumCoin=new BigDecimal(split[2]);
		setbCSumCoin(new BigDecimal(split[3]));
		nSumCoin = new BigDecimal(split[4]);
		qSumCoin = new BigDecimal(split[5]);
		gSumCoin=fCSumCoin.add(hisFSumCoin).add(fBSumCoin).add(getbCSumCoin()).add(nSumCoin).add(qSumCoin);
		this.gBet = null;
	}
	public BigDecimal getfCSumCoin() {
		return fCSumCoin;
	}
	public void setfCSumCoin(BigDecimal fCSumCoin) {
		this.fCSumCoin = fCSumCoin;
	}
	public BigDecimal getHisFSumCoin() {
		return hisFSumCoin;
	}
	public void setHisFSumCoin(BigDecimal hisFSumCoin) {
		this.hisFSumCoin = hisFSumCoin;
	}
	public BigDecimal getfBSumCoin() {
		return fBSumCoin;
	}
	public void setfBSumCoin(BigDecimal fBSumCoin) {
		this.fBSumCoin = fBSumCoin;
	}
	public BigDecimal getgSumCoin() {
		return gSumCoin;
	}
	public void setgSumCoin(BigDecimal gSumCoin) {
		this.gSumCoin = gSumCoin;
	}
	
	public BigDecimal getDepositSum() {
		return depositSum;
	}
	public void setDepositSum(BigDecimal depositSum) {
		this.depositSum = depositSum;
	}
	public BigDecimal getDepositRgSum() {
		return depositRgSum;
	}
	public void setDepositRgSum(BigDecimal depositRgSum) {
		this.depositRgSum = depositRgSum;
	}
	public String getLastDeposit() {
		return lastDeposit;
	}
	public void setLastDeposit(String lastDeposit) {
		if(StringUtils.isEmpty(lastDeposit)) {
			return ;
		}
		String[] split = lastDeposit.split(",");
		lastDepositAwamount=new BigDecimal(split[0]);
		lastDepositTime=split[1];
		lastDepositInfo=split[2];
		this.lastDeposit = null;
	}
	public BigDecimal getLastDepositAwamount() {
		return lastDepositAwamount;
	}
	public void setLastDepositAwamount(BigDecimal lastDepositAwamount) {
		this.lastDepositAwamount = lastDepositAwamount;
	}
	public String getLastDepositTime() {
		return lastDepositTime;
	}
	public void setLastDepositTime(String lastDepositTime) {
		this.lastDepositTime = lastDepositTime;
	}
	public String getLastDepositInfo() {
		return lastDepositInfo;
	}
	public void setLastDepositInfo(String lastDepositInfo) {
		this.lastDepositInfo = lastDepositInfo;
	}
	public BigDecimal getWithdrawSum() {
		return withdrawSum;
	}
	public void setWithdrawSum(BigDecimal withdrawSum) {
		this.withdrawSum = withdrawSum;
	}
	public String getLastWithdraw() {
		return lastWithdraw;
	}
	public void setLastWithdraw(String lastWithdraw) {
		if(StringUtils.isEmpty(lastWithdraw)) {
			return ;
		}
		String[] split = lastWithdraw.split(",");
		lastWithdrwAwamount=new BigDecimal(split[0]);
		lastWithdrawTime=split[1];
		lastWithdrawInfo=split[2];
		this.lastWithdraw = null;
	}
	public BigDecimal getLastWithdrwAwamount() {
		return lastWithdrwAwamount;
	}
	public void setLastWithdrwAwamount(BigDecimal lastWithdrwAwamount) {
		this.lastWithdrwAwamount = lastWithdrwAwamount;
	}
	public String getLastWithdrawTime() {
		return lastWithdrawTime;
	}
	public void setLastWithdrawTime(String lastWithdrawTime) {
		this.lastWithdrawTime = lastWithdrawTime;
	}
	public String getLastWithdrawInfo() {
		return lastWithdrawInfo;
	}
	public void setLastWithdrawInfo(String lastWithdrawInfo) {
		this.lastWithdrawInfo = lastWithdrawInfo;
	}
	public BigDecimal getbCSumCoin() {
		return bCSumCoin;
	}
	public void setbCSumCoin(BigDecimal bCSumCoin) {
		this.bCSumCoin = bCSumCoin;
	}
	public BigDecimal getnSumCoin() {
		return nSumCoin;
	}
	public void setnSumCoin(BigDecimal nSumCoin) {
		this.nSumCoin = nSumCoin;
	}
	public BigDecimal getBetRebate() {
		return betRebate;
	}
	public void setBetRebate(BigDecimal betRebate) {
		this.betRebate = betRebate;
	}
	public BigDecimal getqSumCoin() {
		return qSumCoin;
	}
	public void setqSumCoin(BigDecimal qSumCoin) {
		this.qSumCoin = qSumCoin;
	}
	
	

}
