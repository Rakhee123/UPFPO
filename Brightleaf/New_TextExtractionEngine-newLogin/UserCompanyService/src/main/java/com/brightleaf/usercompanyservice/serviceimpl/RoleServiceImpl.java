package com.brightleaf.usercompanyservice.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.model.Permission;
import com.brightleaf.usercompanyservice.model.Role;
import com.brightleaf.usercompanyservice.repository.PermissionRepository;
import com.brightleaf.usercompanyservice.repository.RoleRepository;
import com.brightleaf.usercompanyservice.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Optional<Role> getRoleById(Integer roleId) {

		return roleRepository.findById(roleId);
	}

	@Override
	public List<Permission> getPermissionByRole(Integer roleId) {

		List<Role> roleList = roleRepository.getByRoleId(roleId);
		List<Permission> list1 = new ArrayList<>();

		for (Role list : roleList) {
			String values = list.getPermissionList();
			String[] s = values.split(",");

			for (int i = 0; i < s.length; i++) {
				List<Permission> getpermission = permissionRepository.getPermissionName();
				for (Permission per : getpermission) {
					int value = Integer.parseInt(s[i]);
					if (per.getPermissionId().equals(value)) {
						list1.add(per);
					}
				}

			}

		}

		return list1;

	}

}
