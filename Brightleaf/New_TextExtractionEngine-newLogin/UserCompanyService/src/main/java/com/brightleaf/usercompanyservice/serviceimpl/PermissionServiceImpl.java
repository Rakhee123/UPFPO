package com.brightleaf.usercompanyservice.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.model.Permission;
import com.brightleaf.usercompanyservice.repository.PermissionRepository;
import com.brightleaf.usercompanyservice.service.PermissionService;
@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	PermissionRepository permissionRepository;
	
	@Override
	public List<Permission> getPermissions() {
		
		return permissionRepository.findAll();
	}

	@Override
	public List<Permission> getPermissionList(String permissionlist) {
		
		List<Permission> list = new ArrayList<>();
		String[] plist = permissionlist.split(",");
		for(int i=0;i<plist.length;i++)
		{
			List<Permission> getpermission = permissionRepository.getPermissionName();
			for (Permission per : getpermission) {
				int value = Integer.parseInt(plist[i]);
				if (per.getPermissionId().equals(value)) {
					list.add(per);
				}
			}

		}
		
		return list;
	}

	
}
