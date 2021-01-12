package com.brightleaf.usercompanyservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.usercompanyservice.model.Company;
import com.brightleaf.usercompanyservice.model.CompanyDto;

@Component
public interface CompanyService {
	
	public Company getCompany(Integer companyId);
	
	public List<Company> getCompanies();
	
	public Company addCompany(Company company);
	
	public void deleteCompany(Company company);

	Boolean checkUserNameExistsInCompany(String userName, Integer companyId);

	Boolean isCompanyExists(String companyName); 
	
	public Company getCompanyIdByName(String companyName);
	
	public Company convertCompanyFromCompanyDto(final CompanyDto companyDto);
	
	public CompanyDto convertCompanyDtoFromCompany(final Company company);
	
	public Company convertCompanyFromCompanyDtoForEdit(Company companyTobeModified, final CompanyDto companyDto);

}
