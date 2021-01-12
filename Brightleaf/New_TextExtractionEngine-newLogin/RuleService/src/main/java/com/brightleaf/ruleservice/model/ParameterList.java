package com.brightleaf.ruleservice.model;

public class ParameterList {

	private String chunk;
	private Rule rule;
	private String sentence;
	private int companyId;
	private String documentName;
	private int pageNumber;
	
	public String getChunk() {
		return chunk;
	}
	public Rule getRule() {
		return rule;
	}
	public String getSentence() {
		return sentence;
	}
	public int getCompanyId() {
		return companyId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setChunk(String c) {
		chunk = c;
	}
	public void setRule(Rule r) {
		rule = r;
	}
	public void setSentence(String sent) {
		sentence = sent;
	}
	public void setCompanyId(int compId) {
		companyId = compId;
	}
	public void setDocumentName(String docName) {
		documentName = docName ;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}
