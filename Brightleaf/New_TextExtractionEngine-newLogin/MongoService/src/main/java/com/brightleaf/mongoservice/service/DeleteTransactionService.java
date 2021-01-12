package com.brightleaf.mongoservice.service;

import org.json.JSONObject;

public interface DeleteTransactionService {

	public String deleteTransaction(JSONObject content);

	public String deleteTransactionsCompanywise(JSONObject content, Integer companyId);

	public String deleteTransactionsDatewise(JSONObject content);

}