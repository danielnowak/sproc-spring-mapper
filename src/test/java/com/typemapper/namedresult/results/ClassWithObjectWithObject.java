package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithObjectWithObject {
	
	@DatabaseField(name="str")
	private String str;

	@DatabaseField(name="obj")
	private ClassWithObject withObj;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public ClassWithObject getWithObj() {
		return withObj;
	}

	public void setWithObj(ClassWithObject withObj) {
		this.withObj = withObj;
	}

}
