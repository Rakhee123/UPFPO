package com.brightleaf.usercompanyservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "permission", catalog = "textextractionengine")
@NamedQuery(name = "FIND_PERMISION_NAME", query = "SELECT per FROM Permission per WHERE per.permissionId = :permissionId")
@NamedQuery(name = "SELECT_PERMISSION", query = "SELECT per FROM Permission per")

public class Permission {

	@Id
	@Column(name = "permission_id")
	private Integer permissionId;

	@Column(name = "permission_name")
	private String permissionName;

	@Column(name = "permission_description")
	private String permissionDescription;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "creation_date")
	private String creationDate;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private String lastModifiedDate;

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionDescription() {
		return permissionDescription;
	}

	public void setPermissionDescription(String permissionDescription) {
		this.permissionDescription = permissionDescription;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
