package com.brightleaf.usercompanyservice.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.usercompanyservice.model.Permission;
import com.brightleaf.usercompanyservice.service.PermissionService;

@CrossOrigin(origins = "*")
@RestController
public class PermissionResource {

	@Autowired
	PermissionService permissionService;

	@GetMapping("/permissions")
	public List<Permission> getPermissions() {

		return permissionService.getPermissions();
	}
	
	@PostMapping("/permissionList")
	public List<Permission> getPermissionListbyId(@RequestBody String permissionList)
	{
		return permissionService.getPermissionList(permissionList);	
	}

}
