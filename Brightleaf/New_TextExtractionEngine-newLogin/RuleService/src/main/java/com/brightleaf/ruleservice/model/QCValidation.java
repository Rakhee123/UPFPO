package com.brightleaf.ruleservice.model;

public class QCValidation {
	private String oldValue;
	private String newValue;
	private int qcLevel;
	private String status;
	private String qcDoneBy;
	
	public void setOldValue(String val) {
		oldValue = val;
	}
	public void setNewValue(String val) {
		newValue = val;
	}
	public void setStatus(String val) {
		status = val;
	}
	public String getOldValue() {
		return oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public String getStatus() {
		return status;
	}
	public int getQcLevel() {
		return qcLevel;
	}
	public void setQcLevel(int qcLevel) {
		this.qcLevel = qcLevel;
	}
	public String getQcDoneBy() {
		return qcDoneBy;
	}
	public void setQcDoneBy(String qcDoneBy) {
		this.qcDoneBy = qcDoneBy;
	}
	
}
