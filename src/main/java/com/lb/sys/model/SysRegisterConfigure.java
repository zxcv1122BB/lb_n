/**
 * 
 */
package com.lb.sys.model;

/**
 * @author wz
 * @describe 系统注册设置实体类
 * @date 2017年9月22日
 */
public class SysRegisterConfigure {
	private Integer id;
	private String attrName;//属性名称
	private Byte isShow;//是否显示 1为显示
	private Byte isInput;//是否必须输入 1为是 0为否
	private Byte isCheck;//是否验证 1为验证
	private Byte isOnly;//是否唯一 1为是唯一
	private Byte status;//状态
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
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}
	/**
	 * @param attrName the attrName to set
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	/**
	 * @return the isShow
	 */
	public Byte getIsShow() {
		if(isShow != null && isShow!=1 && isShow!=0) {
			return 0;
		}
		return isShow;
	}
	/**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(Byte isShow) {
		this.isShow = isShow;
	}
	/**
	 * @return the isInput
	 */
	public Byte getIsInput() {
		if(isInput != null && isInput!=1 && isInput!=0) {
			return 0;
		}
		return isInput;
	}
	/**
	 * @param isInput the isInput to set
	 */
	public void setIsInput(Byte isInput) {
		this.isInput = isInput;
	}
	/**
	 * @return the isCheck
	 */
	public Byte getIsCheck() {
		if(isCheck != null && isCheck!=1 && isCheck!=0) {
			return 0;
		}
		return isCheck;
	}
	/**
	 * @param isCheck the isCheck to set
	 */
	public void setIsCheck(Byte isCheck) {
		this.isCheck = isCheck;
	}
	/**
	 * @return the isOnly
	 */
	public Byte getIsOnly() {
		if(isOnly != null && isOnly!=1 && isOnly!=0) {
			return 0;
		}
		return isOnly;
	}
	/**
	 * @param isOnly the isOnly to set
	 */
	public void setIsOnly(Byte isOnly) {
		this.isOnly = isOnly;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SysRegisterConfigure [id=" + id + ", attrName=" + attrName + ", isShow=" + isShow + ", isInput="
				+ isInput + ", isCheck=" + isCheck + ", isOnly=" + isOnly + ", status=" + status + "]";
	}
	
	
}
