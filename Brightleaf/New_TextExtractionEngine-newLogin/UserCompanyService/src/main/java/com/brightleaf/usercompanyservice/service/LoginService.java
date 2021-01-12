package com.brightleaf.usercompanyservice.service;

import org.springframework.stereotype.Component;

import com.brightleaf.usercompanyservice.model.UserInfo;

@Component
public interface LoginService {
	UserInfo findByUserName(String userName);
	

}
