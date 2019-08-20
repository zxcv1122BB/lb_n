package com.lb.excel;

/*
 * excel表头信息处理对象
 */
public class ExcelModel {

	public ExcelModel() {
	}
	
	

	public ExcelModel(String[] arrKey, String[] arrValue) {
		this.arrKey = arrKey;
		this.arrValue = arrValue;
	}

	
	public ExcelModel(String[] arrKey, String[] arrValue, String arrStr) {
		super();
		this.arrKey = arrKey;
		this.arrValue = arrValue;
		this.arrStr = arrStr;
	}


	private String[] arrKey;
	private String[] arrValue;
	private String arrStr;
	
	
	public String getArrStr() {
		return arrStr;
	}



	public void setArrStr(String arrStr) {
		this.arrStr = arrStr;
	}



	public String[] getArrKey() {
		return arrKey;
	}
	public void setArrKey(String[] arrKey) {
		this.arrKey = arrKey;
	}
	public String[] getArrValue() {
		return arrValue;
	}
	public void setArrValue(String[] arrValue) {
		this.arrValue = arrValue;
	}
	
	

}
