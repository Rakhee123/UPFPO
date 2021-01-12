package com.brightleaf.exportservice.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;

public interface CreateCompanyWiseReportService {

	public Resource exportCreateCompanyWiseReportToExcel(final HttpServletResponse response, String getExcelData);

	public Resource exportCreateUserWiseReportToExcel(final HttpServletResponse response, String getExcelData);
}
