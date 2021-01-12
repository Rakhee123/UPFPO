package com.brightleaf.mongoservice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.brightleaf.mongoservice.model.ExtractedEntity;
import com.brightleaf.mongoservice.model.UserQC;

@Component
public interface ReadMongoService {

	public List<ExtractedEntity> getMongoData(final String companyName, final String transactionId,
			final Integer qcLevel, HttpServletRequest request);

	public String getTransactionVerifyStatus(String companyName, String transactionId, Integer qCLevel);


}
