package com.typemapper.core.result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.typemapper.core.db.DbType;
import com.typemapper.core.db.DbTypeField;
import com.typemapper.core.db.DbTypeRegister;
import com.typemapper.parser.postgres.ParseUtils;

public class ObjectResultNode implements DbResultNode {
	
	private static final Logger LOG = Logger.getLogger(ObjectResultNode.class);
	
	private String name;
	private String type;
	private List<DbResultNode> children; 
	
	public ObjectResultNode(String value, String name, String typeName, Connection connection) throws SQLException {
		super();
		this.type = typeName;
		this.children = new ArrayList<DbResultNode>();
		this.name = name;
		List<String> values = ParseUtils.getStringList(value);
		DbType dbType = DbTypeRegister.getDbType(typeName, connection);
		int i = 1;
		for (String fieldValue : values) {
			DbTypeField fieldDef = dbType.getFieldByPos(i);
			String parsedValue = ParseUtils.getString(fieldValue);
			DbResultNode node = null;
			if (fieldDef == null) {
				LOG.error("Could not find field in " + dbType + " for pos " + i);
				continue;
			}
			if (fieldDef.getType().equals("USER-DEFINED")) {
				node = new ObjectResultNode(parsedValue, fieldDef.getName(), fieldDef.getTypeName(), connection);
			} else if (fieldDef.getType().equals("ARRAY")) {
				node = new ArrayResultNode(fieldDef.getName(), parsedValue, fieldDef.getTypeName().substring(1), connection);
			} else {
				node = new SimpleResultNode(parsedValue, fieldDef.getName());
			}
			this.children.add(node);
			i++;
		}
	}
	
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
		return this.name;
	}

	@Override
	public DbResultNode getChildByName(String name) {
		for (DbResultNode node : getChildren()) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}



	@Override
	public String toString() {
		return "ObjectResultNode [name=" + name + ", type=" + type
				+ ", children=" + children + "]";
	}

}
