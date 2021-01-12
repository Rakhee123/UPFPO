package com.brightleaf.usercompanyservice.serviceimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.model.Company;
import com.brightleaf.usercompanyservice.model.CompanyDto;
import com.brightleaf.usercompanyservice.model.UserRole;
import com.brightleaf.usercompanyservice.repository.CompanyRepository;
import com.brightleaf.usercompanyservice.repository.UserRoleRepository;
import com.brightleaf.usercompanyservice.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Override
	public Company getCompany(Integer companyId) {
		return companyRepository.findCompaniesById(companyId);
	}

	@Override
	public List<Company> getCompanies() {
		return companyRepository.findAll();
	}

	@Override
	public Company addCompany(Company company) {
		return companyRepository.saveAndFlush(company);
	}

	@Override
	public void deleteCompany(Company company) {
		companyRepository.delete(company);
	}

	@Override
	public Boolean checkUserNameExistsInCompany(String userName, Integer companyId) {
		List<UserRole> findByUserName=userRoleRepository.findByUserNameAndCompanyId(userName, companyId);
		if (findByUserName != null && !findByUserName.isEmpty())
			return true;
		return false;
	}
	
	@Override
	public Boolean isCompanyExists(String companyName) 
	{
		//boolean flag=false;
		Company findCompanyByCompany=companyRepository.findCompanyByCompanyName(companyName);
		if (findCompanyByCompany != null && !findCompanyByCompany.equals(null))
			return true;
		return false;
 		//return findCompanyByCompany !=null ? findCompanyByCompany.equals(null) :flag;
	}

	@Override
	public Company getCompanyIdByName(String companyName) {
		return companyRepository.findCompanyByCompanyName(companyName);
	}
	
	@Override
	public Company convertCompanyFromCompanyDto(final CompanyDto companyDto) {

		Company company = new Company();

		company.setCompanyStatus(companyDto.getCompanyStatus());
		company.setCompanyId(companyDto.getCompanyId());
		company.setCompanyName(companyDto.getCompanyName());
		company.setCompanyAddress(companyDto.getCompanyAddress());
		company.setContactPerson(companyDto.getContactPerson());
		company.setContactPhone(companyDto.getContactPhone());
		company.setOutputdateFormat(companyDto.getOutputdateFormat());
		company.setmfa(companyDto.getmfa());
		company.setNumberOfQcLevels(companyDto.getNumberOfQcLevels());
		company.setIsDeleted(companyDto.getIsDeleted());
		company.setCreatedBy(companyDto.getCreatedBy());
		company.setCreationDate(companyDto.getCreationDate());
		company.setLastModifiedBy(companyDto.getCreatedBy());
		company.setLastModifiedDate(companyDto.getLastModifiedDate());
		return company;
	}
	
	@Override
	public CompanyDto convertCompanyDtoFromCompany(final Company company) {

		CompanyDto companyDto = new CompanyDto();

		companyDto.setCompanyStatus(company.getCompanyStatus());
		companyDto.setCompanyId((company.getCompanyId()));
		companyDto.setCompanyName((company.getCompanyName()));
		companyDto.setCompanyAddress((company.getCompanyAddress()));
		companyDto.setContactPerson((company.getContactPerson()));
		companyDto.setContactPhone((company.getContactPhone()));
		companyDto.setOutputdateFormat(company.getOutputdateFormat());
		companyDto.setmfa(company.getmfa());
		companyDto.setNumberOfQcLevels(company.getNumberOfQcLevels());
		companyDto.setIsDeleted(company.getIsDeleted());
		companyDto.setCreatedBy(company.getCreatedBy());
		companyDto.setCreationDate(company.getCreationDate());
		companyDto.setLastModifiedBy(company.getLastModifiedBy());
		companyDto.setLastModifiedDate(company.getLastModifiedDate());

		return companyDto;
	}
	
	@Override
	public Company convertCompanyFromCompanyDtoForEdit(Company companyTobeModified, final CompanyDto companyDto) {

		companyTobeModified.setCompanyStatus(companyDto.getCompanyStatus());
		companyTobeModified.setCompanyId(companyDto.getCompanyId());
		companyTobeModified.setCompanyName(companyDto.getCompanyName());

		companyTobeModified.setCompanyAddress(companyDto.getCompanyAddress());
		companyTobeModified.setContactPerson(companyDto.getContactPerson());
		companyTobeModified.setContactPhone(companyDto.getContactPhone());
		companyTobeModified.setOutputdateFormat(companyDto.getOutputdateFormat());
		companyTobeModified.setmfa(companyDto.getmfa());
		companyTobeModified.setNumberOfQcLevels(companyDto.getNumberOfQcLevels());
		companyTobeModified.setIsDeleted(companyDto.getIsDeleted());
		companyTobeModified.setCreatedBy(companyTobeModified.getCreatedBy());
		companyTobeModified.setCreationDate(companyTobeModified.getCreationDate());
		companyTobeModified.setLastModifiedBy(companyDto.getLastModifiedBy());
		companyTobeModified.setLastModifiedDate(new Date());
		return companyTobeModified;
	}
}
