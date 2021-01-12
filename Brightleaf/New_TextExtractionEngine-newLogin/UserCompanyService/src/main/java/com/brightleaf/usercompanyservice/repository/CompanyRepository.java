package com.brightleaf.usercompanyservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightleaf.usercompanyservice.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

	@Query(name = "FIND_COMPANY_BY_ID")
	public Company findCompaniesById(@Param("companyId") Integer companyId);
	
	@Query(name = "FIND_BY_COMPANYNAME")
	public Company findCompanyByCompanyName(@Param("companyName") String companyName);

}
