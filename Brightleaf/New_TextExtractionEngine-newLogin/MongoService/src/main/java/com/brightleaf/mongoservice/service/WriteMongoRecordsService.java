package com.brightleaf.mongoservice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brightleaf.mongoservice.model.ExtractedEntity;
import com.brightleaf.mongoservice.model.UserQC;

public interface WriteMongoRecordsService {

	public String mongoAddRecords(String dbName, List<ExtractedEntity> listExtraction, String transactionId, String qcLevels, Integer ruleSetId, HttpServletRequest request);
}
