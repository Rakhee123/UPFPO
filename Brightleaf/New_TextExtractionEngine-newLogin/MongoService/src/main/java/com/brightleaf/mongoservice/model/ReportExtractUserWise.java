package com.brightleaf.mongoservice.model;

import java.text.DecimalFormat;

public class ReportExtractUserWise {
	private String companyName;
	private String userName;
	private int totalAttributes_qc1;
	private int changedAttributes_qc1;
	private String percent_qc1;
	private int attributeChangedByNextQC_qc1;
	private int totalAttributes_qc2;
	private int changedAttributes_qc2;
	private String percent_qc2;
	private int attributeChangedByNextQC_qc2;
	private int totalAttributes_qc3;
	private int changedAttributes_qc3;
	private String percent_qc3;
	private int attributeChangedByNextQC_qc3;
	private String changedAttributeList;
	private String transactionId;
	private String changedByNextQCAttributeList;
	
	public String getCompanyName() {
		return this.companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getTotalAttributes(int i) {
		if (i == 1)
			return this.totalAttributes_qc1;
		if (i == 2)
			return this.totalAttributes_qc2;
		if (i == 3)
			return this.totalAttributes_qc3;
		return -1;
	}
	public void setTotalAttributes(int i, int total) {
		if (i == 1)
			this.totalAttributes_qc1 = total;
		else if (i == 2)
			this.totalAttributes_qc2 = total;
		else if (i == 3)
			this.totalAttributes_qc3 = total;
	}
	public int getChangedAttributes(int i) {
		if (i == 1)
			return this.changedAttributes_qc1;
		if (i == 2)
			return this.changedAttributes_qc2;
		if (i == 3)
			return this.changedAttributes_qc3;
		return -1;
	}
	public void setChangedAttributes(int i, int total) {
		if (i == 1)
			this.changedAttributes_qc1 = total;
		else if (i == 2)
			this.changedAttributes_qc2 = total;
		else if (i == 3)
			this.changedAttributes_qc3 = total;
	}
	public String getPercent(int i) {
		if (i == 1)
			return this.percent_qc1;
		if (i == 2)
			return this.percent_qc2;
		if (i == 3)
			return this.percent_qc3;
		return "";
	}
	public void setPercent(int i, String p) {
		if (i == 1)
			this.percent_qc1 = p;
		else if (i == 2)
			this.percent_qc2 = p;
		else if (i == 3)
			this.percent_qc3 = p;
	}
	public int getAttributeChangedByNextQC(int i) {
		if (i == 1)
			return this.attributeChangedByNextQC_qc1;
		if (i == 2)
			return this.attributeChangedByNextQC_qc2;
		if (i == 3)
			return this.attributeChangedByNextQC_qc3;
		return -1;
	}
	public void setAttributeChangedByNextQC(int i, int total) {
		if (i == 1)
			this.attributeChangedByNextQC_qc1 = total;
		else if (i == 2)
			this.attributeChangedByNextQC_qc2 = total;
		else if (i == 3)
			this.attributeChangedByNextQC_qc3 = total;
	}
	public String getChangedAttributeList() {
		return this.changedAttributeList;
	}
	public void setChangedAttributeList(String str) {
		this.changedAttributeList = str;
	}
	public String getTransactionId() {
		return this.transactionId;
	}
	public void setTransactionId(String str) {
		this.transactionId = str;
	}
	public String getChangedByNextQCAttributeList() {
		return this.changedByNextQCAttributeList;
	}
	public void setChangedByNextQCAttributeList(String str) {
		this.changedByNextQCAttributeList = str;
	}
}
