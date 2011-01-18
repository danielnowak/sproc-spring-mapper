package com.typemapper.namedresult.results;

import java.util.List;

import com.typemapper.annotations.DatabaseField;

public class ObjectWithArray {
	
	@DatabaseField(name="arr")
	private List<ClassWithPrimitives> array;
	@DatabaseField(name="str")
	private String str;
	
	public List<ClassWithPrimitives> getArray() {
		return array;
	}
	public void setArray(List<ClassWithPrimitives> array) {
		this.array = array;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	

}
