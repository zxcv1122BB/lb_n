package com.lb.sys.tools.model;

/***
 * 返回状态码信息
 * 
 * @author ASUS
 */
public class Message {
	
	public Message() {
		
	}

	public Message(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public Message(int code, String msg,Object Object) {
		super();
		this.code = code;
		this.msg = msg;
		this.Object=Object;
	}

	private int code;
	private String msg;
	private Integer row;// 影响行数
	private Object Object;

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObject() {
		return Object;
	}

	public void setObject(Object object) {
		Object = object;
	}

}
