package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithObject {
	
	@DatabaseField(name="str")
	private String str;
	
	@DatabaseField(name="obj")
	private ClassWithPrimitives primitives;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public ClassWithPrimitives getPrimitives() {
		return primitives;
	}

	public void setPrimitives(ClassWithPrimitives primitives) {
		this.primitives = primitives;
	}

}
