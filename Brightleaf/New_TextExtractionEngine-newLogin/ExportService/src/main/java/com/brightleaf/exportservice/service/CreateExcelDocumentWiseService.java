package com.brightleaf.exportservice.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
@Component
public interface CreateExcelDocumentWiseService {
	
	public Resource exportTransactionDocumentwiseToExcel(HttpServletResponse response,String getExcelData, String transactionId, Integer qcLevel);
}
