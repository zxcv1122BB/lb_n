/**
 * 
 */
package com.lb.sys.model;

/**
 * @author wz
 * @describe 通用json数据实体类
 * @date 2017年10月16日
 */
public class MemeberJsonResult {
	
	private String id;
	private String name;
	private String data;
	private Byte status;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @param id
	 * @param name
	 */
	public MemeberJsonResult(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	/**
	 * 
	 */
	public MemeberJsonResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MemeberJsonResult [id=" + id + ", name=" + name + ", status=" + status + "]";
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	
}
