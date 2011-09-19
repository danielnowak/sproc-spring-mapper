package com.typemapper.sproctest.result;

import com.typemapper.annotations.DatabaseField;

public class PrimitiveResult2 {

	@DatabaseField(name = "id")
	private Integer id;
	@DatabaseField(name = "msg")
	private String msg;
	@DatabaseField(name = "msg2")
	private String msg2;	

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

	public String getMsg2() {
		return msg2;
	}

	public void setMsg2(String msg2) {
		this.msg2 = msg2;
	}

}
