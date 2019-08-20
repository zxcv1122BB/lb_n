/**
 * 
 */
package com.lb.sys.model;

/**
 * @author wz
 * @describe 用户区间范围匹配值类
 * @date 2017年10月19日
 */
public class RangeModel {

	private Integer startRange;//上限值
	private Integer endRange;//下限值
	private Integer value;
	
	/**
	 * 
	 */
	public RangeModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param startRange
	 * @param endRange
	 * @param value
	 */
	public RangeModel(Integer startRange, Integer endRange, Integer value) {
		super();
		this.startRange = startRange;
		this.endRange = endRange;
		this.value = value;
	}

	/**
	 * @return the startRange
	 */
	public Integer getStartRange() {
		return startRange;
	}
	/**
	 * @param startRange the startRange to set
	 */
	public void setStartRange(Integer startRange) {
		this.startRange = startRange;
	}
	/**
	 * @return the endRange
	 */
	public Integer getEndRange() {
		return endRange;
	}
	/**
	 * @param endRange the endRange to set
	 */
	public void setEndRange(Integer endRange) {
		this.endRange = endRange;
	}
	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RangeModel [startRange=" + startRange + ", endRange=" + endRange + ", value=" + value + "]";
	}
	
	
}
