package com.typemapper.core.result;

import java.util.List;

public interface DbResultNode {
	
	String getName();
	
	String getType();
	
	DbResultNodeType getNodeType();
	
	String getValue();
	
	List<DbResultNode> getChildren();
	
	DbResultNode getChildByName(String name);

}
