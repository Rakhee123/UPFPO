package com.brightleaf.mongoservice.service;

import org.json.JSONObject;

public interface WriteVerifyService {
	
	public Boolean mongoUpdateVerifyRecords(JSONObject jsonObject);

	public Boolean verifyTransaction(String companyName, String transactionId, Integer qCLevel, String qcDoneBy);

	public Boolean mongoUpdateVerifyDocument(JSONObject content);
	
	//public Boolean checkIfItsLastAttribute(JSONObject jsonObject);
	
}

