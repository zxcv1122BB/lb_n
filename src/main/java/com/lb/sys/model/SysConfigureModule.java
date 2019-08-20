/**
 * 
 */
package com.lb.sys.model;

/**
 * @author wz
 * @describe 系统设置模块类
 * @date 2017年9月26日
 */
public class SysConfigureModule {
	
	private Integer id;
	private String name;
	private Byte status;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the module
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param module the module to set
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
	
	
}
