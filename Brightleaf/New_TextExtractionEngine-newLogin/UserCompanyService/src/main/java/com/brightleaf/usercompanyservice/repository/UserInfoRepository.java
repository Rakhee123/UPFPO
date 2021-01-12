package com.brightleaf.usercompanyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightleaf.usercompanyservice.model.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

	@Query(name = "FIND_USER_INFO_BY_USER_ID")
	public UserInfo findUserByUserId(@Param("userId") Integer userId);
	
	@Query(name = "FIND_BY_USERNAME")
	UserInfo findByUserName(String userName);

	@Query(name = "FIND_USER_INFO_BY_USER_NAME")
	public UserInfo getUserInfoByUserName(@Param("userName") String userName);
	

}
