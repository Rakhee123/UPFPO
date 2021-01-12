package com.brightleaf.mongoservice.service;

import org.json.JSONObject;

public interface WriteQCLevelService {

	public String mongoUpdateQARecords(final String companyName, final String transactionId, JSONObject jsonObject);

	public String changeCustemValue(String companyName, String transactionId, JSONObject object);
}
