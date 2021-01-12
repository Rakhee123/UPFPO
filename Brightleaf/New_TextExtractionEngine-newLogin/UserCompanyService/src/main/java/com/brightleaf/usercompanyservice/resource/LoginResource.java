package com.brightleaf.usercompanyservice.resource;

import static com.brightleaf.usercompanyservice.constants.Constants.TOKEN_PREFIX;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.usercompanyservice.jwt.JwtTokenUtil;
import com.brightleaf.usercompanyservice.model.ApiResponse;
import com.brightleaf.usercompanyservice.model.AuthToken;
import com.brightleaf.usercompanyservice.model.Company;
import com.brightleaf.usercompanyservice.model.LoginDetails;
import com.brightleaf.usercompanyservice.model.LoginUser;
import com.brightleaf.usercompanyservice.model.UserInfo;
import com.brightleaf.usercompanyservice.repository.UserInfoRepository;
import com.brightleaf.usercompanyservice.service.CompanyService;
import com.brightleaf.usercompanyservice.service.LoginDetailsService;
import com.brightleaf.usercompanyservice.service.OtpService;
import com.brightleaf.usercompanyservice.service.UserService;

@CrossOrigin(origins="*")
@RestController
public class LoginResource {
	
	protected final static Logger logger = Logger.getLogger(LoginResource.class);

	@Autowired
	UserInfoRepository userInfoRepository;

	@Autowired
	OtpService otpservice;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CompanyService companyService;

	@Autowired private LoginDetailsService loginDetailsService;
	 
	private static final String SESSION_ID="sessionId";
	
	private static final String EMAIL="email";

	// code for jwt
	@PostMapping(value = "/token/login")
	public ApiResponse register(@RequestBody LoginUser loginUser, HttpServletRequest request, HttpServletResponse res) {
		Integer companyId = 1;
		String ipAddress = null;
		String sessionId = "";
		sessionId=this.getSessionID();
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
		final UserInfo user = userService.getUserInfoByUserName(loginUser.getUsername());
		final String token = jwtTokenUtil.generateToken(user);
		ipAddress = this.getIpAddress(request);
		this.saveLoginDetails(companyId, ipAddress, user.getUserId(), sessionId);
		return new ApiResponse(200, "success", new AuthToken(TOKEN_PREFIX + token, user.getUserName(),sessionId));
	}

	// WELCOME URL
	@GetMapping({ "/", "/welcome" })
	public String welcome(Model model) {
		return "welcome";
	}
	
	// logout the user
	@PostMapping(value="/logoutUser")
	public String logout(@RequestBody String logoutDetails) {
		JSONObject jsonObject = new JSONObject(logoutDetails);
		String email = jsonObject.getString(EMAIL);
		String logoutMethod = jsonObject.getString("logoutMethod");
		String sessionId = jsonObject.getString(SESSION_ID);
		UserInfo userInfo=userService.getUserInfoByUserName(email);
		loginDetailsService.logout(userInfo.getUserId(),logoutMethod,sessionId);
		return "logout successfully";	
	}
	
	
	// logout the user
	@PostMapping(value="/logoutUserBySwitchCompany")
	public ApiResponse logoutUserBySwitchCompany(@RequestBody String logoutDetails,HttpServletRequest request) {

		JSONObject jsonObject = new JSONObject(logoutDetails);
		String email = jsonObject.getString(EMAIL);
		String logoutMethod = jsonObject.getString("logoutMethod");
		String oldSessionId = jsonObject.getString(SESSION_ID);
		Integer companyId=jsonObject.getInt("companyId");
		UserInfo userInfo=userService.getUserInfoByUserName(email);
		loginDetailsService.logout(userInfo.getUserId(),logoutMethod,oldSessionId);
		
		String sessionId=this.getSessionID();
		final String token = jwtTokenUtil.generateToken(userInfo);
		String ipAddress = this.getIpAddress(request);
		this.saveLoginDetails(companyId, ipAddress, userInfo.getUserId(), sessionId);
		return new ApiResponse(200, "success", new AuthToken(TOKEN_PREFIX + token, userInfo.getUserName(),sessionId));
	}
	
	@PostMapping(value = "/setCompanyIdInLoginDetails/{companyId}")
	public Company setCompanyIdInLoginDetails(@RequestBody String loginDetails,@PathVariable("companyId") Integer companyId) {
		JSONObject jsonObject = new JSONObject(loginDetails);
		String email = jsonObject.getString(EMAIL);
		String sessionId = jsonObject.getString(SESSION_ID);
		UserInfo userInfo=userService.getUserInfoByUserName(email);
		loginDetailsService.setCompanyIdInLoginDetails(userInfo.getUserId(),sessionId,companyId);
		return companyService.getCompany(companyId);
	}
	
	public String getSessionID()
	{
		UUID uid = UUID.randomUUID();
		return String.valueOf(uid);
	}
	
	public String getIpAddress(HttpServletRequest request)
	{
		String ipAddress = null;
		ipAddress = request.getHeader("X-Forwarded-For");
		final String unknown = "unknown";

		if (ipAddress == null || ipAddress.length() == 0 || unknown.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("127.0.0.1"))
			{
				 InetAddress ipAddr;
				try {
					ipAddr = InetAddress.getLocalHost();
					ipAddress=ipAddr.getHostAddress();
					logger.info("ip  >>   "+ipAddress);
				} catch (UnknownHostException e) {
					logger.error("getIpAddress", e);
				}
			}
		}
		return ipAddress;
	}
	
	public void saveLoginDetails(Integer companyId,String ipAddress,Integer userId,String sessionId)
	{
		LoginDetails loginDetail = new LoginDetails();
		loginDetail.setCompanyId(companyId);
		loginDetail.setIpAddress(ipAddress);
		loginDetail.setUserId(userId);
		loginDetail.setSessionId(sessionId);
		loginDetail.setLoginTime(new Date());
		loginDetailsService.saveLoginDetails(loginDetail);
	}
}
