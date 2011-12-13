package com.typemapper.namedresult.results;

import java.math.BigDecimal;

import com.typemapper.annotations.DatabaseField;

public class ClassWithBigDecimal {
	
	@DatabaseField(name="i")
	private BigDecimal i;

	public BigDecimal getI() {
		return i;
	}

	public void setI(BigDecimal i) {
		this.i = i;
	}

}
