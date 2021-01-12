package com.brightleaf.documenttypeservice.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity 
@NamedQuery(name = "FIND_CUSTOMIZELIST_BY_ATTRIBUTE_ID", query = "SELECT customizelist FROM CustomizeList customizelist WHERE customizelist.attributeId = :attributeId")
@NamedQuery(name = "FIND_CUSTOMIZELIST_BY_ID", query = "SELECT customizelist FROM CustomizeList customizelist WHERE customizelist.customizeListId = :customizeListId")

@Table(name = "customize_list", catalog = "textextractionengine")
public class CustomizeList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customize_list_id")
	private Integer customizeListId;
	
	@Column(name = "attribute_id")
	private Integer attributeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "default_value")
	private Boolean defaultValue;
	
	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "modification_date")
	private Date modificationDate;

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

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

}
