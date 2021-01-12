package com.brightleaf.usercompanyservice.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.usercompanyservice.model.Company;
import com.brightleaf.usercompanyservice.model.UserRole;
import com.brightleaf.usercompanyservice.repository.UserRoleRepository;
import com.brightleaf.usercompanyservice.service.UserService;

@CrossOrigin(origins="*")
@RestController
public class UserCompanyResource {

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private UserService userService;

	@GetMapping("/user/{userId}/companies")
	public List<Company> getUser(@PathVariable("userId") final Integer userId) {
		List<UserRole> listOfUsersWithCompanies = userRoleRepository.selectUserById(userId);

		List<Company> companies = new ArrayList<>();
		for (UserRole companyList : listOfUsersWithCompanies) {
			Company fullCompanyDetail = companyList.getCompany();
			companies.add(fullCompanyDetail);
		}

		return companies;
	}
	
	@GetMapping("/user/{userId}/userRole")
	public List<UserRole> getUser1(@PathVariable("userId") final Integer userId) {
		return userService.getUserByUserId(userId);
	}
	
	@GetMapping("/company/{companyId}/user/{userId}")
	@ResponseBody
	public UserRole getUserDetails(@PathVariable("companyId") Integer companyId,@PathVariable("userId") Integer userId) {
		return userService.getUserRoleByCompanyIdUserId(companyId, userId);
	}
	
	
}
