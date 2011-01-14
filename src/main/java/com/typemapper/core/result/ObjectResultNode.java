package com.typemapper.core.result;

import java.util.List;

public class ObjectResultNode implements DbResultNode {
	
	private String type;
	private List<DbResultNode> children; 

	@Override
	public String getType() {
		return type;
	}

	@Override
	public DbResultNodeType getNodeType() {
		return DbResultNodeType.OBJECT;
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public List<DbResultNode> getChildren() {
		return children;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DbResultNode getChildByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
