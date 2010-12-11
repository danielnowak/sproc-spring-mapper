package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithPrimitives {
	
	@DatabaseField(name="i")
	public int i;
	
	@DatabaseField(name="l")
	public long l;
	
	@DatabaseField(name="c")
	public char c;
	
	

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

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
