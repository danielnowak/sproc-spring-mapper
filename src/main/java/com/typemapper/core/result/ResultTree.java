package com.typemapper.core.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ResultTree implements DbResultNode {
	
	
	private Map<String, DbResultNode> children = new HashMap<String, DbResultNode>();
	private static final Logger LOG = Logger.getLogger(ResultTree.class);

	@Override
	public String getType() {
		return null;
	}

	@Override
	public DbResultNodeType getNodeType() {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public List<DbResultNode> getChildren() {
		List<DbResultNode> result = new ArrayList<DbResultNode>();
		for (DbResultNode node : children.values()) {
			result.add(node);
		}
		return result;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public DbResultNode getChildByName(String name) {
		return children.get(name);
	}
	
	public void addChild(DbResultNode node) {
		if (node != null) {
			children.put(node.getName(), node);
		} else {
			LOG.warn("Trying to add null value to result tree");
		}
	}

}
