package com.typemapper.namedresult.results;

import java.util.Set;

import com.typemapper.annotations.DatabaseField;

public class ClassWithStringSet {
	
	@DatabaseField(name="str")
	private String str;
	
	@DatabaseField(name="arr")
	private Set<String> array;

	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Set<String> getArray() {
		return array;
	}
	public void setArray(Set<String> array) {
		this.array = array;
	}


}
