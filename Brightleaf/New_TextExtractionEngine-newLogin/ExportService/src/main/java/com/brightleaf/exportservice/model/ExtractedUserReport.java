package com.brightleaf.exportservice.model;

public class ExtractedUserReport {
	private String companyName;
	private String userName;
	private int    totalAttributes_qc1;
	private int    changedAttributes_qc1;
	private String percent_qc1;
	private int    attributeChangedByNextQC_qc1;
	private int    totalAttributes_qc2;
	private int    changedAttributes_qc2;
	private String percent_qc2;
	private int    attributeChangedByNextQC_qc2;
	private int    totalAttributes_qc3;
	private int    changedAttributes_qc3;
	private String percent_qc3;
	private int    attributeChangedByNextQC_qc3;
	private String transactionId;
	private String changedAttributeList;
	private String changedByNextQCAttributeList;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	/***********************/
	public int getTotalAttributesqc1() {
		return totalAttributes_qc1;
	}

	public void setTotalAttributesqc1(int t) {
		this.totalAttributes_qc1 = t;
	}
	
	public int getTotalAttributesqc2() {
		return totalAttributes_qc2;
	}

	public void setTotalAttributesqc2(int t) {
		this.totalAttributes_qc2 = t;
	}
	
	public int getTotalAttributesqc3() {
		return totalAttributes_qc3;
	}

	public void setTotalAttributesqc3(int t) {
		this.totalAttributes_qc3 = t;
	}
	/*************************/
	public int getAttributeChangedByNextQCqc1() {
		return attributeChangedByNextQC_qc1;
	}

	public void setAttributeChangedByNextQCqc1(int t) {
		this.attributeChangedByNextQC_qc1 = t;
	}
	
	public int getAttributeChangedByNextQCqc2() {
		return attributeChangedByNextQC_qc2;
	}

	public void setAttributeChangedByNextQCqc2(int t) {
		this.attributeChangedByNextQC_qc2 = t;
	}
	
	public int getAttributeChangedByNextQCqc3() {
		return attributeChangedByNextQC_qc3;
	}

	public void setAttributeChangedByNextQCqc3(int t) {
		this.attributeChangedByNextQC_qc3 = t;
	}
	/***********************/
	public int getChangedAttributesqc1() {
		return changedAttributes_qc1;
	}

	public void setChangedAttributesqc1(int t) {
		this.changedAttributes_qc1 = t;
	}
	
	public int getChangedAttributesqc2() {
		return changedAttributes_qc2;
	}

	public void setChangedAttributesqc2(int t) {
		this.changedAttributes_qc2 = t;
	}
	
	public int getChangedAttributesqc3() {
		return changedAttributes_qc3;
	}

	public void setChangedAttributesqc3(int t) {
		this.changedAttributes_qc3 = t;
	}
	/****************************/
	public String getPercentqc1() {
		return percent_qc1;
	}

	public void setPercentqc1(String s) {
		this.percent_qc1 = s;
	}
	
	public String getPercentqc2() {
		return percent_qc2;
	}

	public void setPercentqc2(String s) {
		this.percent_qc2 = s;
	}
	
	public String getPercentqc3() {
		return percent_qc3;
	}

	public void setPercentqc3(String s) {
		this.percent_qc3 = s;
	}
	
	/****************************/
	public String getChangedAttributeList() {
		return changedAttributeList;
	}

	public void setChangedAttributeList(String s) {
		this.changedAttributeList = s;
	}
	
	/****************************/
	public String getChangedByNextQCAttributeList() {
		return changedByNextQCAttributeList;
	}

	public void setChangedByNextQCAttributeList(String s) {
		this.changedByNextQCAttributeList = s;
	}
}
