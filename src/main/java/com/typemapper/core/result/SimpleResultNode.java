package com.typemapper.core.result;

import java.util.ArrayList;
import java.util.List;

public class SimpleResultNode implements DbResultNode {
	
	protected String value;
	protected String name;

	public SimpleResultNode(Object obj, String name) {
		this.value = obj.toString();
		this.name = name;
	}

	@Override
	public DbResultNodeType getNodeType() {
		return DbResultNodeType.SIMPLE;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public List<DbResultNode> getChildren() {
		return new ArrayList<DbResultNode>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public DbResultNode getChildByName(String name) {
		return null;
	}

	@Override
	public String toString() {
		return "SimpleResultNode [value=" + value
				+ ", name=" + name + "]";
	}

}
