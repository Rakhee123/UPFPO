package com.brightleaf.ruleservice.model;

public class CustomizeList {
	
	private Integer customizeListId;
	
	private Integer attributeId;
	
	private String name;
	
	private String value;
	
	private Boolean defaultValue;
	
	public Integer getCustomizeListId() {
		return customizeListId;
	}

	public void setCustomizeListId(Integer customizeListId) {
		this.customizeListId = customizeListId;
	}
	
	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Boolean getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Boolean defaultValue) {
		this.defaultValue = defaultValue;
	}
}
