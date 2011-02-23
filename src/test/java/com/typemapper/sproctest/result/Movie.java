package com.typemapper.sproctest.result;

import com.typemapper.annotations.DatabaseField;

public class Movie {
	
	@DatabaseField(name="i")
	private int i;
	@DatabaseField(name="l")
	private long l;
	@DatabaseField(name="c")
	private String c;
	
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
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}

}
