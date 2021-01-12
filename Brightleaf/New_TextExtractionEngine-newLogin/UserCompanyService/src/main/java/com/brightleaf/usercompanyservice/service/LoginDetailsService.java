package com.brightleaf.usercompanyservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.usercompanyservice.model.LoginDetails;

@Component
public interface LoginDetailsService {

	void saveLoginDetails(LoginDetails loginDetail);

	List<LoginDetails> getLoginDetailsList();

	void logout(Integer userId, String logoutMethod, String sessionId);

	LoginDetails getLoggedInUser(Integer userId);

	Integer setCompanyIdInLoginDetails(Integer userId, String sessionId, Integer companyId);

	public void deleteByCompanyIdUserId(Integer userId, Integer companyId);
}