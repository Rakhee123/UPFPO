package com.brightleaf.executeservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface TempToSourceService {

	public List<String> moveTempToSource(final String transactionId, int companyId);
}
