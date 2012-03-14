package com.typemapper.namedresult.results;

import com.typemapper.annotations.Embed;

public class ClassWithStringSetWithEmbed {
	
	@Embed
	private ClassWithStringSet set;

	public ClassWithStringSet getSet() {
		return set;
	}

	public void setSet(ClassWithStringSet set) {
		this.set = set;
	}



}
