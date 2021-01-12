package com.brightleaf.usercompanyservice.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.usercompanyservice.model.Company;
import com.brightleaf.usercompanyservice.model.UserInfo;
import com.brightleaf.usercompanyservice.model.UserInfoDto;
import com.brightleaf.usercompanyservice.model.UserRole;
import com.brightleaf.usercompanyservice.model.UserRoleDto;
import com.brightleaf.usercompanyservice.service.CompanyService;
import com.brightleaf.usercompanyservice.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

//	GET USERS FROM COMPANY
	@GetMapping("/company/{companyId}/users")

	public List<UserInfoDto> getUser(@PathVariable("companyId") final Integer companyId) {
		List<UserInfoDto> usersDtoByCompany = new ArrayList<>();
		UserInfoDto userinfoDto = null;

		for (UserRole userList : userService.getUserInfoByCompanyId(companyId)) {
			UserInfo fullUserDetail = userList.getUserInfo();
			userinfoDto = userService.convertUserInfoDtoFromUserInfo(fullUserDetail);
			usersDtoByCompany.add(userinfoDto);
		}
		return usersDtoByCompany;
	}

	// Create User In Specific Company
	@PostMapping("/{companyId}/createUser")
	@ResponseBody
	public UserRole createUserInCompany(@RequestBody UserRoleDto userRoleDto) {
		UserRole userRole = userService.convertUserRoleFromUserRoleDto(userRoleDto);
		UserInfo userInfo = userRole.getUserInfo();
		return userService.createUserInCompany(userInfo, userRole);
	}

	// create user condition
	@GetMapping("company/{companyId}/checkMailId/{userName}")
	public UserInfo createUser(@PathVariable("userName") final String userName,
			@PathVariable("companyId") Integer companyId) {
		/* check user exist in userInfo */
		if (userService.isUserExists(userName)) {
			/* check user exist in the company */
			if (companyService.checkUserNameExistsInCompany(userName, companyId)) {
				return null;
			}
			return userService.getUserInfoByUserName(userName);

		} else {
			return new UserInfo();
		}
	}

	@PutMapping("/company/{companyId}/edituser/{userId}")
	@ResponseBody
	public UserRole updateUser(@RequestBody UserRoleDto userRoleDto, @PathVariable Integer userId,
			@PathVariable Integer companyId) {
		UserRole userRoleTobeModified = userService.getUserRoleByCompanyIdUserId(companyId, userId);
		UserRole userRole = userService.convertUserRoleFromUserRoleEditDto(userRoleTobeModified, userRoleDto);
		UserInfo userInfo = userRole.getUserInfo();
		userService.updateUserInCompany(userInfo, userRole);
		return userRole;

	}

	@PutMapping("/editProfile/{userName}")
	@ResponseBody
	public UserInfo editProfile(@RequestBody UserInfoDto userinfoDto, @PathVariable String userName) {
		UserInfo userInfo = userService.convertUserInfoFromUserInfoDto(userinfoDto);
		userService.editProfile(userInfo);
		return userInfo;
	}

	// DELETE USER-1
	@DeleteMapping("/company/{companyId}/deleteUser/{userId}")
	@ResponseBody
	public List<UserRole> selectUserByUserId(@PathVariable("companyId") Integer companyId,
			@PathVariable("userId") Integer userId) {
		List<UserRole> userDetailsList = userService.getUserByUserId(userId);
		if (!userDetailsList.isEmpty()) {
			if (userDetailsList.size() > 1) {
				for (UserRole userDetails : userDetailsList) {
					if (userDetails.getCompany().getCompanyId().equals(companyId)) {
						userService.deleteUserRole(userDetails.getUserRoleId());
					}
				}
			} else {
				userService.deleteUserRole(userDetailsList.get(0).getUserRoleId());
				userService.deleteUserInfo(userId);
			}
		}

		return userDetailsList;
	}

	// GET USERNAMELIST BY USERNAME and COMPANY_NAME
	@GetMapping("/getUserName/{userName}/company/{companyName}")
	@ResponseBody
	public UserRole getUserNameById(@PathVariable("userName") String userName,
			@PathVariable("companyName") String companyName) {

		UserInfo userInfo = userService.getUserInfoByUserName(userName);
		Company list = companyService.getCompanyIdByName(companyName);
		UserRole userRole = null;
		if (userInfo != null && list != null) {
			int userId = userInfo.getUserId();
			int companyId = list.getCompanyId();
			userRole = userService.getUserRoleByCompanyIdUserId(companyId, userId);
		}
		return userRole;
	}

	// GET USER
	@GetMapping("/user/{userName}")
	@ResponseBody
	public UserInfo getUser(@PathVariable("userName") final String userName) {
		return userService.getUserInfoByUserName(userName);
	}

	// GET USER
	@GetMapping("/userCompany/{userName}")
	@ResponseBody
	public List<UserRole> getUserCompanyList(@PathVariable("userName") final String userName) {
		UserInfo userInfo = userService.getUserInfoByUserName(userName);
		return userService.getUserByUserId(userInfo.getUserId());
	}

	// CHANGE FORGOT PASSWORD
	@PostMapping("/checkPassword")
	boolean checkCurrentPassword(@RequestBody String checkpassword) {
		return userService.userCheckPasswordCorrect(checkpassword);
	}

}