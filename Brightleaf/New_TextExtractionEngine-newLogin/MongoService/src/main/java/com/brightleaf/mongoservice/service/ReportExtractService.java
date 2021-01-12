package com.brightleaf.mongoservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.mongoservice.model.ReportExtractCompanyWise;
import com.brightleaf.mongoservice.model.ReportExtractUserWise;

@Component
public interface ReportExtractService {
	public ReportExtractCompanyWise mongoReadVerifiedTransactions(String companyName);
	
	public List<ReportExtractUserWise> mongoReadVerifiedTransactionsUser(String companyName);
}
