package com.brightleaf.resultservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brightleaf.resultservice.model.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

	UserInfo findByUserName(String userName);
}
