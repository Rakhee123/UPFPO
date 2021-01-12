package com.brightleaf.usercompanyservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.usercompanyservice.model.Permission;

@Component
public interface PermissionService {
	public List<Permission> getPermissions();
	public List<Permission> getPermissionList(String permissionlist);
}

