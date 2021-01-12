package com.brightleaf.resultservice.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Brightleaf
 *
 */
@Entity
@Table(name = "user_qc", catalog = "textextractionengine")
@NamedQuery(name = "FIND_USER_QC_BY_QCID", query = "SELECT uq FROM UserQc uq WHERE uq.qcLevel = :qcTid AND uq.companyName = :companyName ORDER BY uq.qcId DESC")
@NamedQuery(name = "FIND_TX_BY_QC_TXID", query = "SELECT uq FROM UserQc uq WHERE uq.transactionId = :txName AND uq.qcLevel = :qcTid")
@NamedQuery(name = "GET_TX_BY_COMPANYID", query = "SELECT uq FROM UserQc uq")
@NamedQuery(name = "FIND_USER_QC_BY_QCID_TRANSACTIONID", query = "SELECT uq FROM UserQc uq WHERE uq.qcLevel = :qcTid AND uq.companyName = :companyName AND uq.transactionId = :trnxId ORDER BY uq.qcId DESC")

public class UserQc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "qc_id")
	private Integer qcId;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "qc_level")
	private Integer qcLevel;
	
	@Column(name = "assigned_by")
	private String assignedBy;
	
	@Column(name = "assigned_to")
	private String assignedTo;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_by",updatable=false)
	private String createdBy;

	@Column(name = "creation_date",updatable=false)
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


	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getQcLevel() {
		return qcLevel;
	}

	public void setQcLevel(Integer qcLevel) {
		this.qcLevel = qcLevel;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
