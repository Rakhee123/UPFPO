package com.brightleaf.reportservice.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ReportExtractService {
	public JSONObject mongoReadVerifiedTransactions(String dbName);

	JSONArray mongoReadVerifiedTransactionsUser(String dbName);
}
