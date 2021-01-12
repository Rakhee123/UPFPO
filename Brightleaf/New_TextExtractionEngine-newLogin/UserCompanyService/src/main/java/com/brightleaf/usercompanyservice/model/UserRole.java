package com.brightleaf.usercompanyservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "user_role", catalog = "textextractionengine")
@NamedQuery(name = "FIND_USER_ROLE_BY_COMPANY_ID", query = "SELECT ur FROM UserRole ur WHERE ur.company.companyId = :companyId")
@NamedQuery(name = "SELECT_USER_ROLE_BY_USER_ID", query = "SELECT uri FROM UserRole uri WHERE uri.userInfo.userId = :userId")
@NamedQuery(name = "SELECT_USER_ROLE_BY_COMPANY_ID_ROLE_ID", query = "SELECT uri FROM UserRole uri WHERE uri.userInfo.userId = :userId AND uri.company.companyId = :companyId")
@NamedQuery(name = "FIND_BY_USER_NAME_COMPANY_ID", query = "SELECT uri FROM UserRole uri WHERE uri.userInfo.userName = :userName AND uri.company.companyId = :companyId")
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_role_id")
	private Integer userRoleId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private UserInfo userInfo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	@Column(name = "permission_list")
	private String permissionList;

	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private Date lastModifiedDate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(String permissionList) {
		this.permissionList = permissionList;
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

	@Override
	public String toString() {
		return "UserRole [userRoleId=" + userRoleId + ", userInfo=" + userInfo + ", role=" + role + ", company="
				+ company + ", permissionList" + permissionList + "]";
	}

}
