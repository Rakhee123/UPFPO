package com.brightleaf.usercompanyservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightleaf.usercompanyservice.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
	@Query(name = "FIND_PERMISION_NAME")
	public List<Permission> findPermissionNameById(@Param("permissionId") Integer permissionId);
	
	@Query(name = "SELECT_PERMISSION")
	public List<Permission> getPermissionName();
}
