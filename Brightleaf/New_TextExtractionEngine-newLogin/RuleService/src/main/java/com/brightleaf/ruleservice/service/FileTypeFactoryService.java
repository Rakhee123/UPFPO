package com.brightleaf.ruleservice.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brightleaf.ruleservice.model.ExtractedEntityForSingleAttribute;
import com.brightleaf.ruleservice.model.Rule;

public interface FileTypeFactoryService {
	void extractAttributes(File file, Integer companyId, List<ExtractedEntityForSingleAttribute> listExtraction,
			List<Rule> listRule, HttpServletRequest request, String outputDateFormat);
}