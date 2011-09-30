package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithEnum {
	
	@DatabaseField(name="a")
	Enumeration value1;
	
	@DatabaseField(name="b")
	Enumeration value2;

	public Enumeration getValue1() {
		return value1;
	}

	public void setValue1(Enumeration value1) {
		this.value1 = value1;
	}

	public Enumeration getValue2() {
		return value2;
	}

	public void setValue2(Enumeration value2) {
		this.value2 = value2;
	}
	

}
