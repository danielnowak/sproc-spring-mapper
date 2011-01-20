package com.typemapper.sproctest.result;

import com.typemapper.annotations.DatabaseField;

public class PrimitiveResult {
	
	@DatabaseField(name="id")
	private Integer id;
	@DatabaseField(name="msg")
	private String msg;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
