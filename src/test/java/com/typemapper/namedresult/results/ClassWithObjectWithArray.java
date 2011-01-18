package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithObjectWithArray {
	
	@DatabaseField(name="str")
	private String str;
	
	@DatabaseField(name="obj")
	private ObjectWithArray obj;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public ObjectWithArray getObj() {
		return obj;
	}

	public void setObj(ObjectWithArray obj) {
		this.obj = obj;
	}
}
