package com.brightleaf.usercompanyservice.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.usercompanyservice.model.Permission;
import com.brightleaf.usercompanyservice.model.Role;
import com.brightleaf.usercompanyservice.service.RoleService;

@CrossOrigin(origins = "*")
@RestController
public class RoleResource {

	@Autowired
	RoleService roleService;

	@GetMapping("/roles")
	public List<Role> getRoles() {
		return roleService.getRoles();
	}

	@GetMapping("/getPermissionsByRoleId")
	public String getPermissionByRoleId() {
		return null;
	}

	@GetMapping("role/{roleId}")
	public Optional<Role> getRoleById(@PathVariable Integer roleId) {
		return roleService.getRoleById(roleId);
	}

	// GET PERMISSION BY ROLEID
	@GetMapping("/getPermission/{roleId}")
	@ResponseBody
	public List<Permission> getPermissionListByRole(@PathVariable("roleId") Integer roleId) {

		return roleService.getPermissionByRole(roleId);

	}

}
