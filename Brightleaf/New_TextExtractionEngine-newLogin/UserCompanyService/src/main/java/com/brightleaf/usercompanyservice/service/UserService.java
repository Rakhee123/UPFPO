package com.brightleaf.usercompanyservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.usercompanyservice.model.UserInfo;
import com.brightleaf.usercompanyservice.model.UserInfoDto;
import com.brightleaf.usercompanyservice.model.UserRole;
import com.brightleaf.usercompanyservice.model.UserRoleDto;

@Component
public interface UserService {

	public List<UserRole> getUserInfoByCompanyId(Integer companyId);

	public UserInfo addUsers(UserInfo userInfo);

	public List<UserRole> getUserByUserId(Integer userId);

	public UserRole getUserRoleByCompanyIdUserId(Integer companyId, Integer userId);

	public void deleteUserRole(Integer userRoleId);

	public void deleteUserInfo(Integer userId);

	public UserInfo getUserInfoByUserId(Integer userId);

	public UserInfo getUserInfoByUserName(String userName);

	Boolean isUserExists(final String userName);

	void updateUserInUserRole(UserRole userRole);

	UserRole createUserInCompany(UserInfo userInfo, UserRole userRole);

	public UserInfo userForgotPassword(String resetPassword);

	Boolean userCheckPasswordCorrect(String checkPassword);

	UserRole updateUserInCompany(UserInfo userInfo, UserRole userRole);

	UserInfo editProfile(UserInfo userInfo);

	public UserRole convertUserRoleFromUserRoleDto(final UserRoleDto userRoleDto);

	public UserInfoDto convertUserInfoDtoFromUserInfo(final UserInfo userinfo);

	public UserInfo convertUserInfoFromUserInfoDto(final UserInfoDto userinfoDto);
	
	public UserRole convertUserRoleFromUserRoleEditDto(final UserRole userRoleTobeModified, final UserRoleDto userRoleDto);

}
