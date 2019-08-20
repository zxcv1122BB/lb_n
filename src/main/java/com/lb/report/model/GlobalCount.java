package com.lb.report.model;

import java.math.BigDecimal;

import com.alibaba.druid.util.StringUtils;

public class GlobalCount {

	private int betSum;//投注总计
	
	private String gBet;//相关投注、派奖总计
	
	private String cLog;//相关总计
	
	private BigDecimal rebateSumCoin;//返点总计
	
	private BigDecimal withdrawSumCoin;//提款总计
	
	//注册赠送
	private BigDecimal regGivingSumCoin;
	//充值赠送
	private BigDecimal depositGivingSumCoin;
	//加款
	private BigDecimal addMoneySumCoin;
	//减款
	private BigDecimal subtractMoneySumCoin; 
	//代理加款
	private BigDecimal proxyAddMoneySumCoin;
	//代理减款 
	private BigDecimal proxySubtractMoneySumCoin;
	//投注返利
	private BigDecimal betRebate;
	//活动中奖
	private BigDecimal bonusSumCoin;
	//人工赠送
	private BigDecimal rgGivingMoneySumCoin;
	//充值总计 
	private BigDecimal depositSumCoin;
	// 手动确认充值
	private BigDecimal depositRgSumCoin;
	
	//竞彩足球投注额
	private BigDecimal fCSumCoin;
	private BigDecimal fCPJSumCoin;
	private BigDecimal fCPJWin;
	
	//传统足球投注额 
	private BigDecimal hisFSumCoin;
	private BigDecimal hisFPJSumCoin;
	private BigDecimal hisFPJWin;
	
	//足球单场投注额
	private BigDecimal fBSumCoin;
	private BigDecimal fBPJSumCoin;
	private BigDecimal fBPJWin;
	
	//竞彩篮球投注额 
	private BigDecimal bCSumCoin;
	private BigDecimal bCPJSumCoin;
	private BigDecimal bCPJWin;
	//数字彩
	private BigDecimal numSumCoin;
	private BigDecimal numPJSumCoin;
	private BigDecimal numPJWin;
	
	//棋牌
	private BigDecimal numQpSumCoin;
	private BigDecimal numQpPJSumCoin;
	private BigDecimal numQpPJWin;
	
	//总输赢
	private BigDecimal sumWin;

	
	public BigDecimal getNumPJWin() {
		return numPJWin;
	}

	public void setNumPJWin(BigDecimal numPJWin) {
		this.numPJWin = numPJWin;
	}

	public BigDecimal getNumSumCoin() {
		return numSumCoin;
	}

	public void setNumSumCoin(BigDecimal numSumCoin) {
		this.numSumCoin = numSumCoin;
	}

	public BigDecimal getNumPJSumCoin() {
		return numPJSumCoin;
	}

	public void setNumPJSumCoin(BigDecimal numPJSumCoin) {
		this.numPJSumCoin = numPJSumCoin;
	}

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

	public int getBetSum() {
		return betSum;
	}

	public void setBetSum(int betSum) {
		this.betSum = betSum;
	}

	public String getgBet() {
		return null;
	}

	public void setgBet(String gBet) {
		if(StringUtils.isEmpty(gBet)) {
			return ;
		}
		String[] split = gBet.split(",");
		fCSumCoin=new BigDecimal(split[0]);
		fCPJSumCoin=new BigDecimal(split[1]);
		fCPJWin=fCSumCoin.subtract(fCPJSumCoin);
		
		hisFSumCoin=new BigDecimal(split[2]);
		hisFPJSumCoin=new BigDecimal(split[3]);
		hisFPJWin=hisFSumCoin.subtract(hisFPJSumCoin);
		
		fBSumCoin=new BigDecimal(split[4]);
		fBPJSumCoin=new BigDecimal(split[5]);
		fBPJWin=fBSumCoin.subtract(fBPJSumCoin);
		
		bCSumCoin=new BigDecimal(split[6]);
		bCPJSumCoin=new BigDecimal(split[7]);
		bCPJWin=bCSumCoin.subtract(bCPJSumCoin);

		numSumCoin=new BigDecimal(split[8]);
		numPJSumCoin=new BigDecimal(split[9]);
		numPJWin=numSumCoin.subtract(numPJSumCoin);
		
		numQpSumCoin=new BigDecimal(split[10]);
		numQpPJSumCoin=new BigDecimal(split[11]);
		numQpPJWin=numQpSumCoin.subtract(numQpPJSumCoin);
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
		regGivingSumCoin=new BigDecimal(split[0]);
		depositGivingSumCoin=new BigDecimal(split[1]);
		addMoneySumCoin=new BigDecimal(split[2]);
		subtractMoneySumCoin=new BigDecimal(split[3]);
		rgGivingMoneySumCoin=new BigDecimal(split[4]);
		bonusSumCoin=new BigDecimal(split[5]);
		proxyAddMoneySumCoin=new BigDecimal(split[6]);
		proxySubtractMoneySumCoin=new BigDecimal(split[7]);
		betRebate=new BigDecimal(split[8]);
		BigDecimal reabet1 = new BigDecimal(split[9]);
		BigDecimal reabet2 = new BigDecimal(split[10]);
		rebateSumCoin=reabet1.subtract(reabet2);
		depositSumCoin = new BigDecimal(split[11]);
		withdrawSumCoin = new BigDecimal(split[12]);
		this.cLog = null;
	}

	public BigDecimal getRebateSumCoin() {
		return rebateSumCoin;
	}

	public void setRebateSumCoin(BigDecimal rebateSumCoin) {
		this.rebateSumCoin = rebateSumCoin;
	}

	public BigDecimal getWithdrawSumCoin() {
		return withdrawSumCoin;
	}

	public void setWithdrawSumCoin(BigDecimal withdrawSumCoin) {
		this.withdrawSumCoin = withdrawSumCoin;
	}

	public BigDecimal getfCSumCoin() {
		return fCSumCoin;
	}

	public void setfCSumCoin(BigDecimal fCSumCoin) {
		this.fCSumCoin = fCSumCoin;
	}

	public BigDecimal getfCPJSumCoin() {
		return fCPJSumCoin;
	}

	public void setfCPJSumCoin(BigDecimal fCPJSumCoin) {
		this.fCPJSumCoin = fCPJSumCoin;
	}

	public BigDecimal getfCPJWin() {
		return fCPJWin;
	}

	public void setfCPJWin(BigDecimal fCPJWin) {
		this.fCPJWin = fCPJWin;
	}

	public BigDecimal getHisFSumCoin() {
		return hisFSumCoin;
	}

	public void setHisFSumCoin(BigDecimal hisFSumCoin) {
		this.hisFSumCoin = hisFSumCoin;
	}

	public BigDecimal getHisFPJSumCoin() {
		return hisFPJSumCoin;
	}

	public void setHisFPJSumCoin(BigDecimal hisFPJSumCoin) {
		this.hisFPJSumCoin = hisFPJSumCoin;
	}

	public BigDecimal getHisFPJWin() {
		return hisFPJWin;
	}

	public void setHisFPJWin(BigDecimal hisFPJWin) {
		this.hisFPJWin = hisFPJWin;
	}

	public BigDecimal getfBSumCoin() {
		return fBSumCoin;
	}

	public void setfBSumCoin(BigDecimal fBSumCoin) {
		this.fBSumCoin = fBSumCoin;
	}

	public BigDecimal getfBPJSumCoin() {
		return fBPJSumCoin;
	}

	public void setfBPJSumCoin(BigDecimal fBPJSumCoin) {
		this.fBPJSumCoin = fBPJSumCoin;
	}

	public BigDecimal getfBPJWin() {
		return fBPJWin;
	}

	public void setfBPJWin(BigDecimal fBPJWin) {
		this.fBPJWin = fBPJWin;
	}

	public BigDecimal getRegGivingSumCoin() {
		return regGivingSumCoin;
	}

	public void setRegGivingSumCoin(BigDecimal regGivingSumCoin) {
		this.regGivingSumCoin = regGivingSumCoin;
	}

	public BigDecimal getDepositGivingSumCoin() {
		return depositGivingSumCoin;
	}

	public void setDepositGivingSumCoin(BigDecimal depositGivingSumCoin) {
		this.depositGivingSumCoin = depositGivingSumCoin;
	}

	public BigDecimal getAddMoneySumCoin() {
		return addMoneySumCoin;
	}

	public void setAddMoneySumCoin(BigDecimal addMoneySumCoin) {
		this.addMoneySumCoin = addMoneySumCoin;
	}

	public BigDecimal getSubtractMoneySumCoin() {
		return subtractMoneySumCoin;
	}

	public void setSubtractMoneySumCoin(BigDecimal subtractMoneySumCoin) {
		this.subtractMoneySumCoin = subtractMoneySumCoin;
	}

	public BigDecimal getBonusSumCoin() {
		return bonusSumCoin;
	}

	public void setBonusSumCoin(BigDecimal bonusSumCoin) {
		this.bonusSumCoin = bonusSumCoin;
	}

	public BigDecimal getDepositSumCoin() {
		return depositSumCoin;
	}

	public void setDepositSumCoin(BigDecimal depositSumCoin) {
		this.depositSumCoin = depositSumCoin;
	}

	public BigDecimal getDepositRgSumCoin() {
		return depositRgSumCoin;
	}

	public void setDepositRgSumCoin(BigDecimal depositRgSumCoin) {
		this.depositRgSumCoin = depositRgSumCoin;
	}

	public BigDecimal getSumWin() {
		return sumWin;
	}

	public void setSumWin(BigDecimal sumWin) {
		this.sumWin = sumWin;
	}

	public BigDecimal getbCSumCoin() {
		return bCSumCoin;
	}

	public void setbCSumCoin(BigDecimal bCSumCoin) {
		this.bCSumCoin = bCSumCoin;
	}

	public BigDecimal getbCPJSumCoin() {
		return bCPJSumCoin;
	}

	public void setbCPJSumCoin(BigDecimal bCPJSumCoin) {
		this.bCPJSumCoin = bCPJSumCoin;
	}

	public BigDecimal getbCPJWin() {
		return bCPJWin;
	}

	public void setbCPJWin(BigDecimal bCPJWin) {
		this.bCPJWin = bCPJWin;
	}
	public BigDecimal getBetRebate() {
		return betRebate;
	}

	public void setBetRebate(BigDecimal betRebate) {
		this.betRebate = betRebate;
	}

	public BigDecimal getNumQpSumCoin() {
		return numQpSumCoin;
	}

	public void setNumQpSumCoin(BigDecimal numQpSumCoin) {
		this.numQpSumCoin = numQpSumCoin;
	}
 
	public BigDecimal getNumQpPJSumCoin() {
		return numQpPJSumCoin;
	}

	public void setNumQpPJSumCoin(BigDecimal numQpPJSumCoin) {
		this.numQpPJSumCoin = numQpPJSumCoin;
	}


	public BigDecimal getNumQpPJWin() {
		return numQpPJWin;
	}

	public void setNumQpPJWin(BigDecimal numQpPJWin) {
		this.numQpPJWin = numQpPJWin;
	}

}
