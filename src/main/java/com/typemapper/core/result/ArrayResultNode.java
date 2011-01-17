package com.typemapper.core.result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.typemapper.core.db.DbType;
import com.typemapper.core.db.DbTypeRegister;
import com.typemapper.parser.postgres.ParseUtils;

public class ArrayResultNode implements DbResultNode {
	
	private static final Logger LOG = Logger.getLogger(ArrayResultNode.class); 
	
	private String name;
	private String type;
	private List<DbResultNode> children;
	private DbType typeDef;

	public ArrayResultNode(String name, String value, String typeName, Connection connection) throws SQLException {
		this.name = name;
		this.type = typeName;
		this.typeDef = DbTypeRegister.getDbType(typeName, connection);
		this.children = new ArrayList<DbResultNode>();
		List<String> elements = ParseUtils.getArrayElements(value);
		for (String element : elements) {
			LOG.info(element);
			children.add(new ObjectResultNode(element, "", typeName, connection));
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public DbResultNodeType getNodeType() {
		return DbResultNodeType.ARRAY;
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
	public DbResultNode getChildByName(String name) {
		return null;
	}

	@Override
	public String toString() {
		return "ArrayResultNode [name=" + name + ", type=" + type
				+ ", children=" + children + ", typeDef=" + typeDef + "]";
	}
	

}
