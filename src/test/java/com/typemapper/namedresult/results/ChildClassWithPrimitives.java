package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ChildClassWithPrimitives extends ParentClassWithPrimitives {
	
	@DatabaseField(name="l")
	public long l;
	
	@DatabaseField(name="c")
	public char c;

	public long getL() {
		return l;
	}

	public void setL(long l) {
		this.l = l;
	}

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}

}
