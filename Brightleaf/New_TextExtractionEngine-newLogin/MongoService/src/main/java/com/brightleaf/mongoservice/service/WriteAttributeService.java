package com.brightleaf.mongoservice.service;

import org.json.JSONObject;

public interface WriteAttributeService {

	public String mongoUpdateAttribute(final String companyName, final String transactionId, Integer qcLevel, JSONObject jsonObject);

	public String addValueForAttribute(JSONObject jsonObject,String companyName);
}
