package com.brightleaf.usercompanyservice.model;

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
@Table(name="company", catalog= "textextractionengine")
@NamedQuery(name = "FIND_COMPANY_BY_ID", query = "SELECT cmp FROM Company cmp WHERE cmp.companyId = :companyId")
@NamedQuery(name = "FIND_BY_COMPANYNAME", query = "SELECT cmp FROM Company cmp WHERE cmp.companyName = :companyName")

public class Company implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name= "company_id")
	private Integer companyId;
	
	@Column(name= "company_name")
	private String companyName;

	@Column(name= "company_address")
	private String companyAddress;
	
	@Column(name= "contact_person")
	private String contactPerson;
	
	@Column(name= "contact_phone")
	private String contactPhone;

	@Column(name= "output_date_format")
	private String outputdateFormat;
	
	@Column(name= "MFA")
	private Integer mfa;
	
	@Column(name= "number_of_qc_levels")
	private Integer numberOfQcLevels;
	
	@Column(name= "isdeleted")
	private Integer isDeleted;
	
	@Column(name= "created_by")
	private String createdBy;
	
	@Column(name= "creation_date")
	private Date creationDate;
	
	@Column(name= "last_modified_by")
	private String lastModifiedBy;
	
	@Column(name= "last_modified_date")
	private Date lastModifiedDate;
	
	@Column(name= "company_status")
	private Integer companyStatus;

	public Integer getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(Integer companyStatus) {
		this.companyStatus = companyStatus;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getOutputdateFormat() {
		return outputdateFormat;
	}

	public void setOutputdateFormat(String outputdateFormat) {
		this.outputdateFormat = outputdateFormat;
	}

	public Integer getmfa() {
		return mfa;
	}

	public void setmfa(Integer mFA) {
		mfa = mFA;
	}

	public Integer getNumberOfQcLevels() {
		return numberOfQcLevels;
	}

	public void setNumberOfQcLevels(Integer numberOfQcLevels) {
		this.numberOfQcLevels = numberOfQcLevels;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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
}
