package com.brightleaf.usercompanyservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface OtpService  {

	boolean isValid(String sessionId, int otp);

	public List<String> generateOTP(String uname);

}
