package com.brightleaf.usercompanyservice.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
import com.brightleaf.usercompanyservice.model.CompanyDto;
import com.brightleaf.usercompanyservice.model.LoginDetails;
import com.brightleaf.usercompanyservice.model.UserRole;
import com.brightleaf.usercompanyservice.service.CompanyService;
import com.brightleaf.usercompanyservice.service.LoginDetailsService;
import com.brightleaf.usercompanyservice.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class CompanyResource {

	protected final static Logger logger = Logger.getLogger(CompanyResource.class);

	@Autowired
	private CompanyService companyService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginDetailsService loginDetailsService;

	@GetMapping("/companies")
	public List<CompanyDto> getCompanyList() {
		List<CompanyDto> companyDtoList = new ArrayList<>();
		List<Company> companyList = companyService.getCompanies();
		CompanyDto companyDto = null;
		for (Company company : companyList) {
			companyDto = companyService.convertCompanyDtoFromCompany(company);
			companyDtoList.add(companyDto);
		}
		return companyDtoList;
	}

	@GetMapping("/companyNameList")
	public String getCompanyNameList() {
		org.json.JSONObject obj = new org.json.JSONObject();
		org.json.JSONArray arr = new org.json.JSONArray();
		List<Company> companyList = companyService.getCompanies();
		for (Company company : companyList) {
			// obj.put("companynames", company.getCompanyName());
			arr.put(company.getCompanyName());
		}
		//return new Gson().toJson(arr);
		return arr.toString();
		//return arr;
	}

	@GetMapping(value = "/company/{companyId}")
	@ResponseBody
	public Company getCompanyById(@PathVariable("companyId") Integer companyId) {
		return companyService.getCompany(companyId);
	}

	@GetMapping(value = "/checkCompanyName/{companyName}")
	@ResponseBody
	public Boolean checkCompanyName(@PathVariable("companyName") final String companyName) {
		if (companyService.isCompanyExists(companyName)) {
			logger.error("Company already Exists");
			return true;
		} else {
			return false;
		}
	}

	// CREATE COMPANY
	@PostMapping(value = "/addCompany")
	@ResponseBody
	public Boolean addCompany(@RequestBody CompanyDto companyDto) {
		Company company = companyService.convertCompanyFromCompanyDto(companyDto);

		if (!companyService.isCompanyExists(company.getCompanyName())) {
			companyService.addCompany(company);
			return true;
		} else {
			return false;
		}
	}

	@PutMapping(value = "/updateCompany/{companyId}")
	public CompanyDto updateCompany(@RequestBody CompanyDto companyDto, @PathVariable("companyId") Integer companyId) {
		Company companyTobeModified = companyService.getCompany(companyDto.getCompanyId());
		Company company = companyService.convertCompanyFromCompanyDtoForEdit(companyTobeModified, companyDto);
		companyService.addCompany(company);
		return companyDto;
	}

	// DELETE COMPANY
	@DeleteMapping(value = "/deleteCompany/{companyId}")
	public Company deleteCompany(@PathVariable("companyId") final Integer companyId) {
		List<UserRole> companyDetailsList = userService.getUserInfoByCompanyId(companyId);
		Company comp = companyService.getCompany(companyId);
		if (companyDetailsList.isEmpty()) {
			companyService.deleteCompany(comp);
		}

		for (int i = 0; i < companyDetailsList.size(); i++) {
			UserRole ur = companyDetailsList.get(i);
			List<UserRole> userAssociatedWithOtherCompanies = userService.getUserByUserId(ur.getUserInfo().getUserId());
			userService.deleteUserRole(ur.getUserRoleId());
			//delete from login table
			loginDetailsService.deleteByCompanyIdUserId(ur.getUserInfo().getUserId(), companyId);
			if (userAssociatedWithOtherCompanies.size() == 1) {
				userService.deleteUserInfo(ur.getUserInfo().getUserId());
			}
		}
		companyService.deleteCompany(comp);
		return comp;
	}

	@GetMapping(value = "/companyId/{companyId}")
	@ResponseBody
	public String getCompanyNameById(@PathVariable("companyId") Integer companyId) {
		return companyService.getCompany(companyId).getCompanyName();
	}

	@GetMapping(value = "/companyNameAndQCLevels/{companyId}")
	@ResponseBody
	public String getCompanyNameAndQCLevelsById(@PathVariable("companyId") Integer companyId) {
		Company company = companyService.getCompany(companyId);
		String retStr = company.getCompanyName();
		retStr += "|" + company.getNumberOfQcLevels();
		logger.info("user service" + retStr);
		return retStr;
	}

	@GetMapping(value = "/companyName/{companyName}")
	@ResponseBody
	public Integer getCompanyIdByName(@PathVariable("companyName") final String companyName) {
		return companyService.getCompanyIdByName(companyName).getCompanyId();
	}
}
