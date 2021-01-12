package com.brightleaf.ruleservice.model;

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
@NamedQuery(name = "FIND_RULE_BY_ID", query = "SELECT rule FROM Rule rule WHERE rule.ruleId = :ruleId")
@NamedQuery(name = "FIND_RULES_BY_ATTRIBUTE_ID", query = "SELECT rule FROM Rule rule WHERE rule.attributeId = :attributeId ORDER BY rule.ruleId DESC")
@NamedQuery(name = "FIND_RULES_BY_DOCUMENT_ID", query = "SELECT rule FROM Rule rule WHERE rule.documentTypeId = :documentTypeId ORDER BY rule.ruleId DESC")
@NamedQuery(name = "FIND_RULES_BY_DOCUMENT_ID_RULE_ID", query = "SELECT rule FROM Rule rule WHERE rule.documentTypeId = :documentTypeId AND rule.ruleId = :ruleId")
@NamedQuery(name = "FIND_RULES_BY_DOCUMENT_ID_ATTRIBUTE_ID", query = "SELECT rule FROM Rule rule WHERE rule.documentTypeId = :documentTypeId AND rule.attributeId = :attributeId ORDER BY rule.ruleId DESC")
@Table(name = "rule", catalog = "textextractionengine")
public class Rule implements Serializable, Cloneable  {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "rule_id")
	private Integer ruleId;
	
	@Column(name = "document_type_id")
	private Integer documentTypeId;
	 
	@Column(name = "attribute_id")
	private Integer attributeId;
	
	@Column(name = "text_before1")
	private String textBefore1;
	
	@Column(name = "op_before1")
	private String opBefore1;
	
	@Column(name = "text_before2")
	private String textBefore2;
	
	@Column(name = "op_before2")
	private String opBefore2;

	@Column(name = "text_before3")
	private String textBefore3;
	
	@Column(name = "op_before3")
	private String opBefore3;
	
	@Column(name = "text_before4")
	private String textBefore4;
	
	@Column(name = "op_before4")
	private String opBefore4;
	
	@Column(name = "text_before5")
	private String textBefore5;
	
	@Column(name = "text_after1")
	private String textAfter1;

	@Column(name = "op_after1")
	private String opAfter1;
	
	@Column(name = "text_after2")
	private String textAfter2;
	
	@Column(name = "op_after2")
	private String opAfter2;
	
	@Column(name = "text_after3")
	private String textAfter3;
	
	@Column(name = "op_after3")
	private String opAfter3;
	
	@Column(name = "text_after4")
	private String textAfter4;
	
	@Column(name = "op_after4")
	private String opAfter4;
 
	@Column(name = "text_after5")
	private String textAfter5;
	
	@Column(name = "created_by", updatable=false)
	private String createdBy;
	
	@Column(name = "creation_date", updatable=false)
	private Date creationDate;
	
	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
	
	@Column(name = "ignore_case")
	private Boolean ignoreCase=false;
	
	@Column(name = "merge")
	private Boolean merge=false;
	
	@Column(name = "regex")
	private String regex;

	@Column(name= "searchword")
	private String searchword;
	
	@Column(name= "found")
	private String found;
	
	@Column(name= "notfound")
	private String notfound;
    
	public String getFound() {
		return found;
	}

	public void setFound(String found) {
		this.found = found;
	}
	
	public String getNotfound() {
		return notfound;
	}

	public void setNotfound(String notfound) {
		this.notfound = notfound;
	}

	public String getSearchword() {
		return searchword;
	}

	public void setSearchword(String searchword) {
		this.searchword = searchword;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	public String getTextBefore1() {
		return textBefore1;
	}

	public void setTextBefore1(String textBefore1) {
		this.textBefore1 = textBefore1;
	}

	public String getOpBefore1() {
		return opBefore1;
	}

	public void setOpBefore1(String opBefore1) {
		this.opBefore1 = opBefore1;
	}

	public String getTextBefore2() {
		return textBefore2;
	}

	public void setTextBefore2(String textBefore2) {
		this.textBefore2 = textBefore2;
	}

	public String getOpBefore2() {
		return opBefore2;
	}

	public void setOpBefore2(String opBefore2) {
		this.opBefore2 = opBefore2;
	}

	public String getTextBefore3() {
		return textBefore3;
	}

	public void setTextBefore3(String textBefore3) {
		this.textBefore3 = textBefore3;
	}

	public String getOpBefore3() {
		return opBefore3;
	}

	public void setOpBefore3(String opBefore3) {
		this.opBefore3 = opBefore3;
	}

	public String getTextBefore4() {
		return textBefore4;
	}

	public void setTextBefore4(String textBefore4) {
		this.textBefore4 = textBefore4;
	}

	public String getOpBefore4() {
		return opBefore4;
	}

	public void setOpBefore4(String opBefore4) {
		this.opBefore4 = opBefore4;
	}

	public String getTextBefore5() {
		return textBefore5;
	}

	public void setTextBefore5(String textBefore5) {
		this.textBefore5 = textBefore5;
	}

	public String getTextAfter1() {
		return textAfter1;
	}

	public void setTextAfter1(String textAfter1) {
		this.textAfter1 = textAfter1;
	}

	public String getOpAfter1() {
		return opAfter1;
	}

	public void setOpAfter1(String opAfter1) {
		this.opAfter1 = opAfter1;
	}

	public String getTextAfter2() {
		return textAfter2;
	}

	public void setTextAfter2(String textAfter2) {
		this.textAfter2 = textAfter2;
	}

	public String getOpAfter2() {
		return opAfter2;
	}

	public void setOpAfter2(String opAfter2) {
		this.opAfter2 = opAfter2;
	}

	public String getTextAfter3() {
		return textAfter3;
	}

	public void setTextAfter3(String textAfter3) {
		this.textAfter3 = textAfter3;
	}

	public String getOpAfter3() {
		return opAfter3;
	}

	public void setOpAfter3(String opAfter3) {
		this.opAfter3 = opAfter3;
	}

	public String getTextAfter4() {
		return textAfter4;
	}

	public void setTextAfter4(String textAfter4) {
		this.textAfter4 = textAfter4;
	}

	public String getOpAfter4() {
		return opAfter4;
	}

	public void setOpAfter4(String opAfter4) {
		this.opAfter4 = opAfter4;
	}

	public String getTextAfter5() {
		return textAfter5;
	}

	public void setTextAfter5(String textAfter5) {
		this.textAfter5 = textAfter5;
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

	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public Boolean getMerge() {
		return merge;
	}

	public void setMerge(Boolean merge) {
		this.merge = merge;
	}
	
	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public Rule clone() {
		Rule clone = null;
		try {
			clone = (Rule) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e); // won't happen
		}
		return clone;
	}
}
