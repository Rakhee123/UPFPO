package com.brightleaf.ruleservice.model;

import java.util.List;

public class ExtractedEntityForSingleAttribute {
	private String documentName;
	private Integer companyId;
	private Integer extractedAttributeId;
	private String extractedSentence;
	private String extractedChunk;
	private String extractedEntity;
	private Integer ruleId;
	private Integer pageNumber;
	List<QCValidation> qcList;

	public int getCompanyId() {
		return companyId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public String getExtractedSentence() {
		return extractedSentence;
	}

	public String getExtractedChunk() {
		return extractedChunk;
	}

	public String getExtractedEntity() {
		return extractedEntity;
	}

	public int getAppliedRule() {
		return ruleId;
	}
	
	public List<QCValidation> getQCValidationList() {
		return qcList;
	}

	public Integer getExtractedAttributeId() {
		return extractedAttributeId;
	}
	
	public Integer getRuleId() {
		return getAppliedRule();
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setExtractedAttributeId(Integer extractedAttributeId) {
		this.extractedAttributeId = extractedAttributeId;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public List<QCValidation> getQcList() {
		return qcList;
	}

	public void setQcList(List<QCValidation> qcList) {
		this.qcList = qcList;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public void setDocumentName(String s) {
		documentName = s;
	}

	public void setExtractedSentence(String s) {
		extractedSentence = s;
	}

	public void setExtractedChunk(String s) {
		extractedChunk = s;
	}

	public void setExtractedEntity(String s) {
		extractedEntity = s;
	}

	public void setAppliedRule(int id) {
		ruleId = id;
	}

	public void setCompanyId(int id) {
		companyId = id;
	}

	public void setQCValidationList(List<QCValidation> lst) {
		qcList = lst;
	}
}
