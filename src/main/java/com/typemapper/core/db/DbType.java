package com.typemapper.core.db;

import java.util.HashMap;
import java.util.Map;

public class DbType {
	
	private final String schema;
	private final String name;
	private Map<Integer, DbTypeField> fields = new HashMap<Integer, DbTypeField>();
	
	public DbType(String schema, String name) {
		this.schema = schema;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getSchema() {
		return schema;
	}
	@Override
	public String toString() {
		return "DbType [schema=" + schema + ", name=" + name + ", fields="
				+ fields + "]";
	}
	public DbTypeField getFieldByPos(int i) {
		return fields.get(i);
	}
	public void addField(DbTypeField dbTypeField) {
		fields.put(dbTypeField.getPosition(), dbTypeField);
		
	}
}
