package com.brightleaf.usercompanyservice.serviceimpl;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.model.Company;
import com.brightleaf.usercompanyservice.model.UserInfo;
import com.brightleaf.usercompanyservice.model.UserInfoDto;
import com.brightleaf.usercompanyservice.model.UserRole;
import com.brightleaf.usercompanyservice.model.UserRoleDto;
import com.brightleaf.usercompanyservice.repository.CompanyRepository;
import com.brightleaf.usercompanyservice.repository.UserInfoRepository;
import com.brightleaf.usercompanyservice.repository.UserRoleRepository;
import com.brightleaf.usercompanyservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	UserRoleRepository userRoleRepository;
	@Autowired
	UserInfoRepository userInfoRepository;
	@Autowired
	CompanyRepository companyRepository;

	@Override
	public List<UserRole> getUserInfoByCompanyId(Integer companyId) {
		return userRoleRepository.findUsersByCompanyId(companyId);
	}

	@Override
	public UserInfo addUsers(UserInfo userInfo) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePassword = userInfo.getUserPassword();
		Decoder decoder = Base64.getDecoder();
		byte[] decodedByte = decoder.decode(encodePassword);
		String decodedString = new String(decodedByte);

		logger.info(decodedString);
		String bcryptPassword = encoder.encode(decodedString);
		logger.info(bcryptPassword);
		userInfo.setUserPassword(bcryptPassword);
		return userInfoRepository.save(userInfo);
	}

	@Override
	public List<UserRole> getUserByUserId(Integer userId) {
		return userRoleRepository.selectUserById(userId);
	}

	@Override
	public void deleteUserRole(Integer userRoleId) {
		userRoleRepository.deleteById(userRoleId);
	}

	@Override
	public void deleteUserInfo(Integer userId) {
		userInfoRepository.deleteById(userId);

	}

	@Override
	public UserInfo getUserInfoByUserId(Integer userId) {
		return userInfoRepository.findUserByUserId(userId);
	}

	@Override
	public UserRole createUserInCompany(UserInfo userInfo, UserRole userRole) {
		userInfoRepository.save(userInfo);
		UserInfo userLatestUpdate = userInfoRepository.findByUserName(userInfo.getUserName());
		Integer userIdFromUserInfo = userLatestUpdate.getUserId();
		userInfo.setUserId(userIdFromUserInfo);
		Company company = userRole.getCompany();
		if (company.getCompanyStatus() == 0) {
			company = companyRepository.findCompaniesById(userRole.getCompany().getCompanyId());
			company.setCompanyStatus(1);
			companyRepository.save(company);
		}
		return userRoleRepository.save(userRole);
	}
	
	@Override
	public UserRole updateUserInCompany(UserInfo userInfo, UserRole userRole) {
		userInfoRepository.save(userInfo);
		userRoleRepository.save(userRole);
		return userRole;
	}
	
	@Override
	public UserInfo editProfile(UserInfo userInfo) {
		userInfoRepository.saveAndFlush(userInfo);
		return userInfo;
	}
	
	@Override
	public void updateUserInUserRole(UserRole userRole) {
		userRoleRepository.save(userRole);
	}

	@Override
	public Boolean isUserExists(String userName) {
		boolean flag = false;
		UserInfo findUserInUserInfo = userInfoRepository.getUserInfoByUserName(userName);
		return findUserInUserInfo != null ? findUserInUserInfo.equals(getUserInfoByUserName(userName)) : flag;
	}

	@Override
	public UserInfo getUserInfoByUserName(String userName) {
		return userInfoRepository.getUserInfoByUserName(userName);
	}

	@Override
	public UserRole getUserRoleByCompanyIdUserId(Integer companyId, Integer userId) {
		return userRoleRepository.selectUserByCompanyIdUserId(companyId, userId);
	}

	@Override
	public UserInfo userForgotPassword(String userInfo) {

		JSONObject jsonObject = new JSONObject(userInfo);
		String userName = jsonObject.getString("username");
		String encodePassword = jsonObject.getString("password");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		UserInfo userInfoDetails = userInfoRepository.getUserInfoByUserName(userName);
		Decoder decoder = Base64.getDecoder();
		byte[] decodedByte = decoder.decode(encodePassword);
		String decodedString = new String(decodedByte);
		String bcryptPassword = encoder.encode(decodedString);
		userInfoDetails.setUserPassword(bcryptPassword);
		if (userInfoDetails.getUserName().equals(userName)) {
			userInfoRepository.save(userInfoDetails);
		}
		return userInfoDetails;

	}

	@Override
	public Boolean userCheckPasswordCorrect(String checkPassword) {
		JSONObject jsonObject = new JSONObject(checkPassword);
		String userName = jsonObject.getString("username");
		String myPassword = jsonObject.getString("password");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		UserInfo userInfoDetails = userInfoRepository.getUserInfoByUserName(userName);
		
		Decoder decoder = Base64.getDecoder();
		byte[] decodedByte = decoder.decode(myPassword);
		String decodedString = new String(decodedByte);
		
		return encoder.matches(decodedString, userInfoDetails.getUserPassword());
	}
	
	@Override
	public UserRole convertUserRoleFromUserRoleDto(final UserRoleDto userRoleDto) {
		UserRole userRole = new UserRole();
		userRole.setUserRoleId(userRoleDto.getUserRoleId());
		userRole.setCompany(userRoleDto.getCompany());
		userRole.setPermissionList(userRoleDto.getPermissionList());
		userRole.setRole(userRoleDto.getRole());
		//add LastModifiedBy in userInfo.
		userRoleDto.getUserInfo().setLastModifiedBy(userRoleDto.getUserInfo().getCreatedBy());
		userRole.setUserInfo(userRoleDto.getUserInfo());
		userRole.setCreatedBy(userRoleDto.getUserInfo().getCreatedBy());
		userRole.setCreationDate(new Date());
		userRole.setLastModifiedBy(userRoleDto.getUserInfo().getCreatedBy());
		userRole.setLastModifiedDate(new Date());
		return userRole;
	}
	
	@Override
	public UserRole convertUserRoleFromUserRoleEditDto(final UserRole userRoleTobeModified, final UserRoleDto userRoleDto) {
		userRoleTobeModified.setUserRoleId(userRoleDto.getUserRoleId());
		userRoleTobeModified.setCompany(userRoleDto.getCompany());
		userRoleTobeModified.setPermissionList(userRoleDto.getPermissionList());
		userRoleTobeModified.setRole(userRoleDto.getRole());
		//add LastModifiedBy in userInfo.
		userRoleDto.getUserInfo().setLastModifiedBy(userRoleDto.getUserInfo().getLastModifiedBy());
		userRoleDto.getUserInfo().setLastModifiedDate(new Date());
		userRoleDto.getUserInfo().setCreationDate(userRoleTobeModified.getUserInfo().getCreationDate());
		userRoleTobeModified.setUserInfo(userRoleDto.getUserInfo());
		userRoleTobeModified.setCreatedBy(userRoleTobeModified.getCreatedBy());
		userRoleTobeModified.setCreationDate(userRoleTobeModified.getCreationDate());
		userRoleTobeModified.setLastModifiedBy(userRoleDto.getUserInfo().getLastModifiedBy());
		userRoleTobeModified.setLastModifiedDate(new Date());
		return userRoleTobeModified;
	}
	
	@Override
	public UserInfoDto convertUserInfoDtoFromUserInfo(final UserInfo userinfo) {

		UserInfoDto userinfoDto = new UserInfoDto();
		userinfoDto.setUserId(userinfo.getUserId());
//		userinfoDto.setUserEmail(userinfo.getUserEmail());
		userinfoDto.setUserPassword(userinfo.getUserPassword());
		userinfoDto.setFirstName(userinfo.getFirstName());
		userinfoDto.setMiddleName(userinfo.getMiddleName());
		userinfoDto.setLastName(userinfo.getLastName());
		userinfoDto.setGender(userinfo.getGender());
		userinfoDto.setUserName(userinfo.getUserName());
		userinfoDto.setUserTelephone(userinfo.getUserTelephone());
		userinfoDto.setUserAddress(userinfo.getUserAddress());
		userinfoDto.setCreatedBy(userinfo.getCreatedBy());
		userinfoDto.setCreationDate(userinfo.getCreationDate());
		userinfoDto.setLastModifiedBy(userinfo.getLastModifiedBy());
		userinfoDto.setLastModifiedDate(userinfo.getLastModifiedDate());

		return userinfoDto;
	}

	@Override
	public UserInfo convertUserInfoFromUserInfoDto(final UserInfoDto userinfoDto) {

		UserInfo userinfo = new UserInfo();

		userinfo.setUserId(userinfoDto.getUserId());
//		userinfo.setUserEmail(userinfoDto.getUserEmail());
		userinfo.setUserPassword(userinfoDto.getUserPassword());
		userinfo.setFirstName(userinfoDto.getFirstName());
		userinfo.setMiddleName(userinfoDto.getMiddleName());
		userinfo.setLastName(userinfoDto.getLastName());
		userinfo.setGender(userinfoDto.getGender());
		userinfo.setUserName(userinfoDto.getUserName());
		userinfo.setUserTelephone(userinfoDto.getUserTelephone());
		userinfo.setUserAddress(userinfoDto.getUserAddress());
		userinfo.setCreatedBy(userinfoDto.getCreatedBy());
		userinfo.setCreationDate(userinfoDto.getCreationDate());
		userinfo.setLastModifiedBy(userinfoDto.getLastModifiedBy());
		userinfo.setLastModifiedDate(userinfoDto.getLastModifiedDate());

		return userinfo;
	}
}
