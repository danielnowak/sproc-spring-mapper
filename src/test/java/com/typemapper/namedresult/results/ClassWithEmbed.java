package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;
import com.typemapper.annotations.Embed;

public class ClassWithEmbed {
	
	@Embed
	private ClassWithPrimitives primitives;
	
	@DatabaseField(name="str")
	private String str;

	public ClassWithPrimitives getPrimitives() {
		return primitives;
	}

	public void setPrimitives(ClassWithPrimitives primitives) {
		this.primitives = primitives;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
