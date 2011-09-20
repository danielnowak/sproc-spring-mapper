package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithObject2 {
	
	@DatabaseField(name="str")
	private String str;
	
	@DatabaseField(name="obj")
	private ClassWithPrimitives2 primitives;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public ClassWithPrimitives2 getPrimitives() {
		return primitives;
	}

	public void setPrimitives(ClassWithPrimitives2 primitives) {
		this.primitives = primitives;
	}

}
