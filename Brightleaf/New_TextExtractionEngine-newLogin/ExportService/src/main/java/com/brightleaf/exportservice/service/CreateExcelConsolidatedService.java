package com.brightleaf.exportservice.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;

public interface CreateExcelConsolidatedService {
	
	public Resource exportTransactionConsolidatedToExcel(final HttpServletResponse response, String getExcelData, String transactionId, Integer qcLevel);
}
