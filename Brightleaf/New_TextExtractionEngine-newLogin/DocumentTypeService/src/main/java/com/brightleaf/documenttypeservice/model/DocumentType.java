package com.brightleaf.documenttypeservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "FIND_DOCTYPE_BY_ID", query = "SELECT dt FROM DocumentType dt WHERE dt.documentTypeId = :documentTypeId")
@NamedQuery(name = "FIND_DOCTYPE_BY_NAME", query = "SELECT dt1 FROM DocumentType dt1 WHERE dt1.documentName = :documentName")
@Table(name = "document_type", catalog = "textextractionengine")
public class DocumentType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "document_type_id")
	private Integer documentTypeId;
	
	@Column(name = "document_desc")
	private String documentDesc;

	@Column(name = "document_name")
	private String documentName;
	
	@Column(name = "created_by",updatable=false)
	private String createdBy;

	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private Date lastModifiedDate;


	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentDesc() {
		return documentDesc;
	}

	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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
