package com.brightleaf.ruleservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightleaf.ruleservice.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {


	UserInfo findByUserName(String userName);
}
