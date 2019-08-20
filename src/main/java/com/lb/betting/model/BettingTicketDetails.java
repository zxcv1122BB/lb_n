/**
 * 
 */
package com.lb.betting.model;

import java.util.List;

/**
 * @author wz
 * @describe 
 * @date 2017年11月27日
 */
public class BettingTicketDetails {
	
	private String seriesName;//投注M串N
	private Integer times;//倍数
	private List<Object> list;
	/**
	 * @return the seriesName
	 */
	public String getSeriesName() {
		return seriesName;
	}
	/**
	 * @param seriesName the seriesName to set
	 */
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
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
	 * @return the list
	 */
	public List<Object> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Object> list) {
		this.list = list;
	}
	/**
	 * 
	 */
	public BettingTicketDetails() {
		super();
	}
	/**
	 * @param seriesName
	 * @param times
	 * @param list
	 */
	public BettingTicketDetails(String seriesName, Integer times) {
		super();
		this.seriesName = seriesName;
		this.times = times;
	}
	/**
	 * @param seriesName
	 * @param times
	 * @param list
	 */
	public BettingTicketDetails(String seriesName, Integer times, List<Object> list) {
		super();
		this.seriesName = seriesName;
		this.times = times;
		this.list = list;
	}
	
}
