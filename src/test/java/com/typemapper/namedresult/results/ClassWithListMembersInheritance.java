package com.typemapper.namedresult.results;

import java.util.List;

import com.typemapper.annotations.DatabaseField;

public class ClassWithListMembersInheritance {
	
	@DatabaseField(name="arr")
	private List<ChildClassWithPrimitives> array;

	public List<ChildClassWithPrimitives> getArray() {
		return array;
	}

	public void setArray(List<ChildClassWithPrimitives> array) {
		this.array = array;
	}	

}
