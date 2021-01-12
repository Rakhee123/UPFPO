package com.brightleaf.mongoservice.service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ReadMongoTransactionSummaryService {

    JSONObject getTransactionSummary(final String companyName, final String transactionId, final Integer qcLevel, final JSONArray documentArray, HttpServletRequest request);

}
