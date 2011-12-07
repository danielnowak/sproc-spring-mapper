package com.typemapper.core.db;

public class DbTypeField {
	
	private String name;
	private int position;
	private String type;
	private String typeName;
	
	public DbTypeField(String fieldName, int fieldPosition, String fieldType, String fieldTypeName) {
		this.name = new String(fieldName);
		this.position = fieldPosition;
		this.type = new String(fieldType);
		this.typeName = String.valueOf(fieldTypeName);
	}
	public String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	public int getPosition() {
		return position;
	}
	void setPosition(int position) {
		this.position = position;
	}
	public String getType() {
		return type;
	}
	void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	@Override
	public String toString() {
		return "DbTypeField [name=" + name + ", position=" + position
				+ ", type=" + type + ", typeName=" + typeName + "]";
	}
}
