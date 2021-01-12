package com.brightleaf.mongoservice.model;

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
@NamedQuery(name = "FIND_USER_QC", query = "SELECT uq1 FROM UserQC uq1 WHERE uq1.qcLevel = :qcLevel AND uq1.transactionId = :transactionId")
@NamedQuery(name = "FIND_TANSACTIONS", query = "SELECT uq2 FROM UserQC uq2 WHERE uq2.transactionId = :transactionId")

@Table(name = "user_qc", catalog = "textextractionengine")

public class UserQC implements Serializable{
	
	private static final long serialVersionUID = -8842156248395112906L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "qc_id")
	private Integer qcId;

	@Column(name = "assigned_by")
	private String assignedBy;

	@Column(name = "assigned_to")
	private String assignedTo;

	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "qc_level")
	private Integer qcLevel;

	@Column(name = "status")
	private String status;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
	
	@Column(name = "company_id")
	private Integer companyId;
	
	@Column(name = "rule_set_id")
	private Integer ruleSetId;

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getQcId() {
		return qcId;
	}

	public void setQcId(Integer qcId) {
		this.qcId = qcId;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getQcLevel() {
		return qcLevel;
	}

	public void setQcLevel(Integer qcLevel) {
		this.qcLevel = qcLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	
	public Integer getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(Integer ruleSetId) {
		this.ruleSetId = ruleSetId;
	}
}
