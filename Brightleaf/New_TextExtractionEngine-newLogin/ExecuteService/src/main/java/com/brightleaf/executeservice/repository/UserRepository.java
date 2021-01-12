package com.brightleaf.executeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightleaf.executeservice.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

	UserInfo findByUserName(String userName);
}
