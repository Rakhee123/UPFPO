package com.brightleaf.documenttypeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightleaf.documenttypeservice.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

	UserInfo findByUserName(String userName);
}
