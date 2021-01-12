package com.brightleaf.usercompanyservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightleaf.usercompanyservice.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	@Query(name="SELECT_BY_ROLEID")
	public List<Role> getByRoleId(@Param("roleId") Integer roleId);
}
