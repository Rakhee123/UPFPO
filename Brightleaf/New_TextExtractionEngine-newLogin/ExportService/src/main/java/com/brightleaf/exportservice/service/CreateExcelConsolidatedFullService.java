package com.brightleaf.exportservice.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;

public interface CreateExcelConsolidatedFullService {
	
	public Resource exportTransactionFullConsolidatedToExcel(final HttpServletResponse response, String getExcelData, String transactionId, Integer qcLevel);
}
