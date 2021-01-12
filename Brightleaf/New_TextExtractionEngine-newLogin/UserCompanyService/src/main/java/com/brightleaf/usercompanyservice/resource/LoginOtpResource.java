package com.brightleaf.usercompanyservice.resource;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.usercompanyservice.model.UserInfo;
import com.brightleaf.usercompanyservice.repository.UserInfoRepository;
import com.brightleaf.usercompanyservice.service.OtpService;
import com.brightleaf.usercompanyservice.service.UserService;
import com.google.gson.Gson;

@CrossOrigin(origins = "*")
@RestController
public class LoginOtpResource {

	protected final static Logger logger = Logger.getLogger(LoginOtpResource.class);

	private static final String HOST = "host";

	@Autowired
	UserInfoRepository userInfoRepository;

	@Autowired
	OtpService otpservice;

	@Autowired
	UserService userService;

	// GENRATE OTP
	@GetMapping("/genrateOTP/{userName}")
	@ResponseBody
	public String generateOtp(@PathVariable("userName") String userName, HttpServletRequest request)
			throws MessagingException {

		List<String> otpModelList = otpservice.generateOTP(userName);

		Gson json = new Gson();
		UserInfo userInfoList = userInfoRepository.findByUserName(userName);
		if (userInfoList != null) {
			String resultPath = request.getScheme() + "://" + request.getHeader(HOST);// + request.getContextPath();

			//EmailUtility.postMail(userInfoList.getFirstName(), userName, resultPath, otpModelList.get(1));
			return json.toJson(otpModelList.get(0));

		} else {
			return "fail to genrate otp";
		}

	}

	// VALIDATE OTP
	@GetMapping("/validateOtp/{sessionId}/{otp}")
	public String validateOtp(@PathVariable("sessionId") String sessionId, @PathVariable("otp") int otp) {
		Gson gson = new Gson();
		final boolean SUCCESS = true;
		final boolean FAIL = false;
		boolean status = false;
		status = otpservice.isValid(sessionId, otp);
		if (status) {
			if (sessionId != "" && otp > 0) {
				return gson.toJson(SUCCESS);
			} else {
				return gson.toJson(FAIL);
			}
		} else {
			return gson.toJson(FAIL);
		}

	}

	// SEND EMAIL LINK PASSWORD
	@PostMapping("/sendEmailLinkPassword")
	public String sendEmailLinkPassword(@RequestBody String userName, HttpServletRequest request)
			throws MessagingException {
		Gson gson = new Gson();
		UserInfo userInfoList = userInfoRepository.findByUserName(userName);
		String resultPath = request.getScheme() + "://" + request.getHeader(HOST);// + request.getContextPath();
		logger.info(request.getHeader(HOST));
		//EmailUtility.postMail(userInfoList.getFirstName(), userName, resultPath, "");
		return "mail send successfully";
	}

	// SEND EMAIL LINK for create new PASSWORD for new user
	@PostMapping("/sendEmailLinkForNewPassword")
	public String sendEmailLinkForNewPassword(@RequestBody String newPasswordDetail, HttpServletRequest request)
			throws MessagingException {
		Gson gson = new Gson();
		JSONObject jsonObject = new JSONObject(newPasswordDetail);
		String userName = jsonObject.getString("username");
		String companyName = jsonObject.getString("currentcompany");
		UserInfo userInfoList = userInfoRepository.findByUserName(userName);
		String resultPath = request.getScheme() + "://" + request.getHeader(HOST);// + request.getContextPath();
		logger.info(request.getHeader(HOST));
		//EmailUtility.postMailForNewPassword(userInfoList.getFirstName(), userName, resultPath, "");
		return gson.toJson("Mail Send Successfully");
	}

	// CHANGE FORGOT PASSWORD
	@PostMapping("/changeForgotPassword")
	public UserInfo changeForgotPassword(@RequestBody String userInfo) {

		return userService.userForgotPassword(userInfo);

	}
}
