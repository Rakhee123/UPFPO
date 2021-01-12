package com.brightleaf.usercompanyservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.brightleaf.usercompanyservice.model.Permission;
import com.brightleaf.usercompanyservice.model.Role;

@Component
public interface RoleService {

	public List<Role> getRoles();

	public Optional<Role> getRoleById(Integer roleId);

	public List<Permission> getPermissionByRole(Integer roleId);

}
