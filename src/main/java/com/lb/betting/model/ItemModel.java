/**
 * 
 */
package com.lb.betting.model;

/**
 * @author wz
 * @describe 
 * @date 2018年1月8日
 */
public class ItemModel {

	private String itemId;
	private String itemName;
	private String rxp1;
	private Byte status;
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	 * @return the rxp1
	 */
	public String getRxp1() {
		return rxp1;
	}
	/**
	 * @param rxp1 the rxp1 to set
	 */
	public void setRxp1(String rxp1) {
		this.rxp1 = rxp1;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemModel [itemId=" + itemId + ", itemName=" + itemName + ", rxp1=" + rxp1 + ", status=" + status + "]";
	}
	
	
}
