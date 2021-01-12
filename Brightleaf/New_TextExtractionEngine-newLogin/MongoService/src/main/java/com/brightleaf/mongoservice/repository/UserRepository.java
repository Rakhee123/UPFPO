package com.brightleaf.mongoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightleaf.mongoservice.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

	
	UserInfo findByUserName(String userName);
}
