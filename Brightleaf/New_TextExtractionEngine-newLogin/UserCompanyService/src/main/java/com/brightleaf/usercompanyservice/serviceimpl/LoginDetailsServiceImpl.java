package com.brightleaf.usercompanyservice.serviceimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.model.LoginDetails;
import com.brightleaf.usercompanyservice.repository.LoginDetailsServiceRepository;
import com.brightleaf.usercompanyservice.service.LoginDetailsService;

@Service
public class LoginDetailsServiceImpl implements LoginDetailsService {

	@Autowired
	LoginDetailsServiceRepository loginDetailsServiceRepository;

	public void saveLoginDetails(LoginDetails loginDetail) {
		loginDetailsServiceRepository.save(loginDetail);
	}

	@Override
	public List<LoginDetails> getLoginDetailsList() {
		return loginDetailsServiceRepository.findAll();
	}

	@Override
	public void logout(Integer userId, String logoutMethod, String sessionId) {
		loginDetailsServiceRepository.logout(userId, logoutMethod, new Date(), sessionId);

	}

	@Override
	public LoginDetails getLoggedInUser(Integer userId) {
		return loginDetailsServiceRepository.getLoggedInUser(userId);
	}

	@Override
	public Integer setCompanyIdInLoginDetails(Integer userId, String sessionId, Integer companyId) {
		return loginDetailsServiceRepository.setCompanyIdInLoginDetails(userId, sessionId, companyId);
	}

	@Override
	public void deleteByCompanyIdUserId(Integer userId, Integer companyId) {
		List<LoginDetails> ppl = loginDetailsServiceRepository.deleteByCompanyIdUserId(userId, companyId);
		loginDetailsServiceRepository.deleteAll(ppl);
	}
}
