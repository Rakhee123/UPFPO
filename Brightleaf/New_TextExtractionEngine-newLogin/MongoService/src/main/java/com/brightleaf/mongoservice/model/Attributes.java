package com.brightleaf.mongoservice.model;

import java.util.List;

public class Attributes {
	private String attributeName;
	private Integer appliedRule;
	private String extractedSentence;
	private String extractedChunk;
	private ApplicationExtractedValue applicationExtractedValue;
	private Integer pageNumber;
	private QCValidation qcValidation;
	private String customDefaultValue;
	private List customList;
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public Integer getAppliedRule() {
		return appliedRule;
	}
	public void setAppliedRule(Integer appliedRule) {
		this.appliedRule = appliedRule;
	}
	public String getExtractedSentence() {
		return extractedSentence;
	}
	public void setExtractedSentence(String extractedSentence) {
		this.extractedSentence = extractedSentence;
	}
	public String getExtractedChunk() {
		return extractedChunk;
	}
	public void setExtractedChunk(String extractedChunk) {
		this.extractedChunk = extractedChunk;
	}
	public ApplicationExtractedValue getApplicationExtractedValue() {
		return applicationExtractedValue;
	}
	public void setApplicationExtractedValue(ApplicationExtractedValue applicationExtractedValue) {
		this.applicationExtractedValue = applicationExtractedValue;
	}
	public QCValidation getQcValidation() {
		return qcValidation;
	}
	public void setQcValidation(QCValidation qcValidation) {
		this.qcValidation = qcValidation;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public List getCustomList() {
		return customList;
	}
	public void setCustomList(List customList) {
		this.customList = customList;
	}
	public String getCustomDefaultValue() {
		return customDefaultValue;
	}
	public void setCustomDefaultValue(String customDefaultValue) {
		this.customDefaultValue = customDefaultValue;
	}
	
	
}