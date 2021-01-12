package com.brightleaf.usercompanyservice.model;

import java.io.Serializable;
import java.util.Date;

public class CompanyDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer companyId;
	
	private String companyName;
	
	private String companyAddress;

	private String contactPerson;

	private String contactPhone;

	private String outputdateFormat;

	private Integer mfa;

	private Integer numberOfQcLevels;

	private Integer isDeleted;

	private String createdBy;

	private Date creationDate;

	private String lastModifiedBy;

	private Date lastModifiedDate;

	private Integer companyStatus;

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

	public void setmfa(Integer mfa) {
		this.mfa = mfa;
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

	public Integer getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(Integer companyStatus) {
		this.companyStatus = companyStatus;
	}



}
