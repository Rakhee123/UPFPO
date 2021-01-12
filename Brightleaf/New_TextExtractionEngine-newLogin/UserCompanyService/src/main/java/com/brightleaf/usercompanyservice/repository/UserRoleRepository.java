package com.brightleaf.usercompanyservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightleaf.usercompanyservice.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

	@Query(name = "FIND_USER_ROLE_BY_COMPANY_ID")
	public List<UserRole> findUsersByCompanyId(@Param("companyId") Integer companyId);
	
	@Query(name = "SELECT_USER_ROLE_BY_USER_ID")
	public List<UserRole> selectUserById(@Param("userId") Integer userId);
	
	@Query(name = "SELECT_USER_ROLE_BY_COMPANY_ID_ROLE_ID")
	public UserRole selectUserByCompanyIdUserId(@Param("companyId") Integer companyId, @Param("userId") Integer userId);
	
	@Query(name = "FIND_BY_USER_NAME_COMPANY_ID")
	public List<UserRole> findByUserNameAndCompanyId(@Param("userName") String userName,@Param("companyId") Integer companyId);
}
