package com.brightleaf.ruleservice.model;

public class Attributes {
	private String attributeName;
	private Integer appliedRule;
	private String extractedSentence;
	private String extractedChunk;
	private ApplicationExtractedValue applicationExtractedValue;
	private Integer pageNumber;
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
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
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
}
