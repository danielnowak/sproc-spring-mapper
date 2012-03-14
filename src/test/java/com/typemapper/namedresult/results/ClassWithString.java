package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithString {
	
	@DatabaseField(name="gender")
	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	
	

}
