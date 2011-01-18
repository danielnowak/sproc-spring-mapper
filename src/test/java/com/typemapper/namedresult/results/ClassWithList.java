package com.typemapper.namedresult.results;

import java.util.List;

import com.typemapper.annotations.DatabaseField;

public class ClassWithList {
	
	@DatabaseField(name="str")
	private String str;
	
	@DatabaseField(name="arr")
	private List<ClassWithPrimitives> array;

	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public List<ClassWithPrimitives> getArray() {
		return array;
	}
	public void setArray(List<ClassWithPrimitives> array) {
		this.array = array;
	}

}
