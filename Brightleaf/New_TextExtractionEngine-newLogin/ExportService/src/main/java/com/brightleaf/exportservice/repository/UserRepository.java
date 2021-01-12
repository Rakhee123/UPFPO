package com.brightleaf.exportservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightleaf.exportservice.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

	UserInfo findByUserName(String userName);
}
