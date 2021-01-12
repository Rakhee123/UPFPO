package com.brightleaf.reportservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightleaf.reportservice.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

	UserInfo findByUserName(String userName);
}
