package com.lb.sys.model;

import java.util.ArrayList;
import java.util.List;

import com.lb.sys.tools.StringUtil;

/**
 * 系统开关配置类
 * */
public class SysConfigure {
	/**
	 * 说明：一下所以byte开关以0为关闭 1为开启  默认关闭
	 * */
	private Integer id;
	private String configure;//配置名称的英文名称
	private String configureName;//配置名称
	private Byte onOff;//开关 1为开启 0为关闭
	private String configureExplain;//输入字段说明
	private Byte isInput;//是否必须输入 0为纯开关 1为开关开启时必须输入一个数据 2为关闭时
	//3为开启时必须输入两个 4为关闭时必须输入两个
	private Byte dataType;//数值类型  0为无要求 1为数字类型 2为时间格式（08:00）3为list集合
	private String sysConfig1;//输入字段1
	private String sysConfig2;//输入字段2
	private List<RangeModel> list;
	private List<MemeberJsonResult> list2;
	private Byte status;//状态 1为正常
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
	 * @return the configure
	 */
	public String getConfigure() {
		return configure;
	}
	/**
	 * @param configure the configure to set
	 */
	public void setConfigure(String configure) {
		this.configure = configure;
	}
	/**
	 * @return the configureName
	 */
	public String getConfigureName() {
		return configureName;
	}
	/**
	 * @param configureName the configureName to set
	 */
	public void setConfigureName(String configureName) {
		this.configureName = configureName;
	}
	/**
	 * @return the onOff
	 */
	public Byte getOnOff() {
		return onOff;
	}
	/**
	 * @param onOff the onOff to set
	 */
	public void setOnOff(Byte onOff) {
		this.onOff = onOff;
	}
	/**
	 * @return the isInput
	 */
	public Byte getIsInput() {
		return isInput;
	}
	/**
	 * @param isInput the isInput to set
	 */
	public void setIsInput(Byte isInput) {
		this.isInput = isInput;
	}
	/**
	 * @return the sysConfig1
	 */
	public String getSysConfig1() {
		if(StringUtil.isBlank(sysConfig1)) {
			return "无配置";
		}
		return sysConfig1;
	}
	/**
	 * @param sysConfig1 the sysConfig1 to set
	 */
	public void setSysConfig1(String sysConfig1) {
		this.sysConfig1 = sysConfig1;
	}
	/**
	 * @return the sysConfig2
	 */
	public String getSysConfig2() {
		return sysConfig2;
	}
	/**
	 * @param sysConfig2 the sysConfig2 to set
	 */
	public void setSysConfig2(String sysConfig2) {
		this.sysConfig2 = sysConfig2;
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
	 * @return the explain
	 */
	public String getConfigureExplain() {
		return configureExplain;
	}
	/**
	 * @param explain the explain to set
	 */
	public void setConfigureExplain(String explain) {
		this.configureExplain = explain;
	}
	/**
	 * @return the dataType
	 */
	public Byte getDataType() {
		return dataType;
	}
	
	/**
	 * @return the list
	 */
	public List<RangeModel> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<RangeModel> list) {
		this.list = list;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(Byte dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the list2
	 */
	public List<MemeberJsonResult> getList2() {
		return list2;
	}
	/**
	 * @param list2 the list2 to set
	 */
	public void setList2(List<MemeberJsonResult> list2) {
		this.list2 = list2;
	}
	/**将String串转为list*/
	public void calcConfigureList()
	{
		//sysConfig1 ="1-500-1#501-1000-2#100-1500-5";
		//dataType = 3;
		if(dataType != null && dataType == 3 && sysConfig1 != null && !"".equals(sysConfig1.trim())) {
			String arr1[] = sysConfig1.split("#");
			list = new ArrayList<RangeModel>();
			for (String str : arr1) {
				if(str != null && !"".equals(str.trim())) {
					String arr2[] = str.split("-");
					if (arr2 != null && arr2.length>2) {
						list.add(new RangeModel(Integer.valueOf(arr2[0]), 
								Integer.valueOf(arr2[1]), Integer.valueOf(arr2[2])));
					}
				}
			}
			sysConfig1 = null;
		}else if(dataType != null && dataType == 4 && sysConfig2 != null && !"".equals(sysConfig2.trim())) {
			String arr1[] = sysConfig2.split("#");
			list2 = new ArrayList<MemeberJsonResult>();
			for (String str : arr1) {
				if(str != null && !"".equals(str.trim())) {
					String arr2[] = str.split("-");
					if (arr2 != null && arr2.length>1) {
						list2.add(new MemeberJsonResult(arr2[0],arr2[1]));
						if(sysConfig1.equals(arr2[0])) {
							sysConfig2=arr2[1];
						}
					}
				}
			}
		}
	}
	/**将list转为String串*/
	public void calcConfigureRange()
	{
		if(dataType != null && dataType == 3 && list != null && list.size()>0) {
			sysConfig1="";
			for (RangeModel rangeModel : list) {
				sysConfig1+="#"+rangeModel.getStartRange()+"-"+rangeModel.getEndRange()+"-"+rangeModel.getValue();
			}
			sysConfig1=sysConfig1.substring(1);
			list = null;
		}else if(dataType != null && dataType == 4 && list2 != null && list2.size()>0) {
			sysConfig2 = "";
			for (MemeberJsonResult result : list2) {
				sysConfig2+="#"+result.getId()+"-"+result.getName();
			}
			sysConfig2=sysConfig2.length()>0?sysConfig1.substring(1):"";
			list2 = null;
		}
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SysConfigure [id=" + id + ", configure=" + configure + ", configureName=" + configureName + ", onOff="
				+ onOff + ", configureExplain=" + configureExplain + ", isInput=" + isInput + ", dataType=" + dataType
				+ ", sysConfig1=" + sysConfig1 + ", sysConfig2=" + sysConfig2 + ", list=" + list + ", list2=" + list2
				+ ", status=" + status + "]";
	}
	
}