package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ParentClassWithPrimitives {
	
	@DatabaseField(name="i")
	public int i;

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

}
