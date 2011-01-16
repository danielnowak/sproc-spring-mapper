package com.typemapper.core.db;

public class DbTypeField {
	
	private String name;
	private int position;
	private String type;
	
	public DbTypeField(String fieldName, int fieldPosition, String fieldType) {
		this.name = new String(fieldName);
		this.position = fieldPosition;
		this.type = new String(fieldType);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "DbTypeField [name=" + name + ", position=" + position
				+ ", type=" + type + "]";
	}
}
