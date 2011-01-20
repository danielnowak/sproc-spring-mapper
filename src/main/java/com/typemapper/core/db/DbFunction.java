package com.typemapper.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbFunction {
	
	private final String schema;
	private final String name;
	private final Map<Integer, DbTypeField> outParams = new HashMap<Integer, DbTypeField>();
	
	public DbFunction(String functionSchema, String functionName) {
		super();
		this.schema = functionSchema;
		this.name = functionName;
	}

	void addOutParam(final DbTypeField field) {
		outParams.put(field.getPosition(), field);
	}
	public String getName() {
		return name;
	}
	public String getSchema() {
		return schema;
	}

	public DbTypeField getFieldByPos(int i) {
		return outParams.get(i);
	}

	@Override
	public String toString() {
		return "DbFunction [schema=" + schema + ", name=" + name
				+ ", outParams=" + outParams + "]";
	}

}
