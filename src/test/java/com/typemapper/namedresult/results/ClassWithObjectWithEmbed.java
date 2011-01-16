package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithObjectWithEmbed {
	
	@DatabaseField(name="str")
	private String str;
	
	@DatabaseField(name="obj")
	private ClassWithEmbed classWithEmbed;

	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public ClassWithEmbed getClassWithEmbed() {
		return classWithEmbed;
	}
	public void setClassWithEmbed(ClassWithEmbed classWithEmbed) {
		this.classWithEmbed = classWithEmbed;
	}
}
