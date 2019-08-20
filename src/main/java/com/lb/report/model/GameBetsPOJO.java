package com.lb.report.model;

public class GameBetsPOJO {

	/**
	 * 今日投注金额
	 */
	private Double toDayAmount;
	
	/**
	 * 今日中奖金额
	 */
	private Double toDayBouns;
	
	/**
	 * 今日中奖金额百分比
	 */
	private Double toDayBounsPercent;
	
	/**
	 * 今日盈亏金额
	 */
	private Double toDayLoss;
	
	/**
	 * 今日盈亏金额百分比
	 */
	private Double toDayLossPercent;
	
	/**
	 * 昨日投注金额
	 */
	private Double yesterDayAmount;
	
	/**
	 * 昨日中奖金额
	 */
	private Double yesterDayBouns;
	
	/**
	 * 昨日中奖金额百分比
	 */
	private Double yesterDayBounsPercent;
	
	/**
	 * 昨日盈亏金额
	 */
	private Double yesterDayLoss;
	
	/**
	 * 昨日盈亏金额百分比
	 */
	private Double yesterDayLossPercent;

	public Double getToDayAmount() {
		return toDayAmount;
	}

	public void setToDayAmount(Double toDayAmount) {
		this.toDayAmount = toDayAmount;
	}

	public Double getToDayBouns() {
		return toDayBouns;
	}

	public void setToDayBouns(Double toDayBouns) {
		this.toDayBouns = toDayBouns;
	}

	public Double getToDayLoss() {
		return toDayLoss;
	}

	public void setToDayLoss(Double toDayLoss) {
		this.toDayLoss = toDayLoss;
	}

	public Double getYesterDayAmount() {
		return yesterDayAmount;
	}

	public void setYesterDayAmount(Double yesterDayAmount) {
		this.yesterDayAmount = yesterDayAmount;
	}

	public Double getYesterDayBouns() {
		return yesterDayBouns;
	}

	public void setYesterDayBouns(Double yesterDayBouns) {
		this.yesterDayBouns = yesterDayBouns;
	}

	public Double getYesterDayLoss() {
		return yesterDayLoss;
	}

	public void setYesterDayLoss(Double yesterDayLoss) {
		this.yesterDayLoss = yesterDayLoss;
	}

	public Double getToDayBounsPercent() {
		return toDayBounsPercent;
	}

	public void setToDayBounsPercent(Double toDayBounsPercent) {
		this.toDayBounsPercent = toDayBounsPercent;
	}

	public Double getToDayLossPercent() {
		return toDayLossPercent;
	}

	public void setToDayLossPercent(Double toDayLossPercent) {
		this.toDayLossPercent = toDayLossPercent;
	}

	public Double getYesterDayBounsPercent() {
		return yesterDayBounsPercent;
	}

	public void setYesterDayBounsPercent(Double yesterDayBounsPercent) {
		this.yesterDayBounsPercent = yesterDayBounsPercent;
	}

	public Double getYesterDayLossPercent() {
		return yesterDayLossPercent;
	}

	public void setYesterDayLossPercent(Double yesterDayLossPercent) {
		this.yesterDayLossPercent = yesterDayLossPercent;
	}
}
