package com.brightleaf.usercompanyservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.brightleaf.usercompanyservice.model.LoginDetails;

@Repository
public interface LoginDetailsServiceRepository extends JpaRepository<LoginDetails, Integer> {

	@Modifying
	@Query(name = "UPDATE_LOGIN_DETAILS")
	@Transactional
	void logout(@Param("userId") Integer userId, @Param("logoutMethod") String logoutMethod,
			@Param("logoutTime") Date logoutTime, @Param("sessionId") String sessionId);

	@Query(name = "GET_LOGIN_DETAILS_BY_USERID")
	LoginDetails getLoggedInUser(@Param("userId") Integer userId);

	@Modifying
	@Query(name = "UPDATE_LOGIN_DETAILS_FOR_COMPANY_ID")
	@Transactional
	Integer setCompanyIdInLoginDetails(@Param("userId") Integer userId, @Param("sessionId") String sessionId,
			@Param("companyId") Integer companyId);

	@Query(name = "DELETE_LOGIN_DETAILS_FOR_USERID_COMPANYID")
	@Transactional
	List<LoginDetails> deleteByCompanyIdUserId(@Param("userId") Integer userId, @Param("companyId") Integer companyId);
}