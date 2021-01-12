package com.brightleaf.documenttypeservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "FIND_ATTRIBUTE_BY_ID", query = "SELECT attr FROM Attribute attr WHERE attr.attributeId = :attributeId")
@NamedQuery(name = "FIND_ATTRIBUTE_BY_NAME", query = "SELECT attr FROM Attribute attr WHERE attr.attributeName = :attributeName")
@Table(name = "attribute", catalog = "textextractionengine")
public class Attribute {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "attribute_id")
	private Integer attributeId;
	
	@Column(name = "attribute_name")
	private String attributeName;
	
	@Column(name = "attribute_desc")
	private String attributeDesc;

	@Column(name = "paragraph")
    private String paragraph; 
	
	@Column(name = "attribute_type")
	private String attributeType;
	
	@Column(name = "fallback_value")
	private String fallbackValue;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;

	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeDesc() {
		return attributeDesc;
	}

	public void setAttributeDesc(String attributeDesc) {
		this.attributeDesc = attributeDesc;
	}

	public String getParagraph() {
		return paragraph;
	}

	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}

	
	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	
	public String getFallbackValue() {
		return fallbackValue;
	}

	public void setFallbackValue(String fallbackValue) {
		this.fallbackValue = fallbackValue;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
