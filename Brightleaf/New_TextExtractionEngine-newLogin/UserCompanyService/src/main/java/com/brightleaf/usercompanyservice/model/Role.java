package com.brightleaf.usercompanyservice.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "role", catalog = "textextractionengine")
@NamedQuery(name = "SELECT_BY_ROLEID", query = "SELECT r FROM Role r WHERE r.roleId= :roleId")

public class Role implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Role() {

	}

	@Id
	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "permission_list")
	private String permissionList;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(String permissionList) {
		this.permissionList = permissionList;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", permissionList=" + permissionList + "]";
	}

}
