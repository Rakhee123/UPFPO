package com.brightleaf.mongoservice.model;

import java.util.Date;

public class QCValidation {
	private String initialValue;
	private String newValue;
	private int qcLevel;
	private Date qcdate;
	private Boolean valueChanges;
	private String valueaddedBy;
	private String status;
	private String ignoreResult;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInitialValue() {
		return initialValue;
	}
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public int getQcLevel() {
		return qcLevel;
	}
	public void setQcLevel(int qcLevel) {
		this.qcLevel = qcLevel;
	}
	public Date getQcdate() {
		return qcdate;
	}
	public void setQcdate(Date qcdate) {
		this.qcdate = qcdate;
	}
	public Boolean getValueChanges() {
		return valueChanges;
	}
	public void setValueChanges(Boolean valueChanges) {
		this.valueChanges = valueChanges;
	}
	public String getValueaddedBy() {
		return valueaddedBy;
	}
	public void setValueaddedBy(String valueaddedBy) {
		this.valueaddedBy = valueaddedBy;
	}
	public String getIgnoreResult() {
		return ignoreResult;
	}
	public void setIgnoreResult(String ignoreResult) {
		this.ignoreResult = ignoreResult;
	}
	
	
}
