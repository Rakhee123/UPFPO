package com.brightleaf.usercompanyservice.serviceimpl;

import java.security.SecureRandom;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService {
	
	protected final Log logger = LogFactory.getLog(getClass());

	private static final Integer EXPIRE_MINS = 1;

	private Map<String, OTPModel> map;
	SecureRandom random = new SecureRandom();

	public OtpServiceImpl() {
		map = new ConcurrentHashMap<>();
	}

	public void createOTP(String otpID, int otp, LocalDateTime expireTime)  {

		map.put(otpID, new OTPModel(otp, expireTime));

	}

	// generate OTP and pass to method createOTP
	@Override
	public List<String> generateOTP(String userName) {

		List<String> otpList = new ArrayList<>();
		String otpID = randomString().substring(0, 16);
		logger.info("new genrated session " + otpID);

		int otp = 0;
		otp = 100000 + random.nextInt(900000);
		String otpval = String.valueOf(otp);
		logger.info(otp);

		otpList.add(otpID);
		otpList.add(otpval);
		createOTP(otpID, otp, LocalDateTime.now().plusMinutes(EXPIRE_MINS));

		return otpList;
	}

	private static String randomString() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	// validate OTP and expire time and return status
	@Override
	public boolean isValid(String otpID, int otp)  {
		OTPModel otpModel = map.get(otpID);

		if (otpModel == null) {
			return false;
		} else if (otpModel.otp == otp && LocalDateTime.now().isBefore(otpModel.expireTime)) {

			map.remove(otpID);
			return true;
		}
		return false;
	}

	public static class OTPModel {
		int otp;
		LocalDateTime expireTime;

		OTPModel(int otp, LocalDateTime expireTime) {
			this.otp = otp;
			this.expireTime = expireTime;
		}
	}

}