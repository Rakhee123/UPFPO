package com.brightleaf.usercompanyservice.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.model.UserInfo;
import com.brightleaf.usercompanyservice.repository.UserInfoRepository;
import com.brightleaf.usercompanyservice.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public UserInfo findByUserName(String userName) {
		return userInfoRepository.findByUserName(userName);
	}	

}