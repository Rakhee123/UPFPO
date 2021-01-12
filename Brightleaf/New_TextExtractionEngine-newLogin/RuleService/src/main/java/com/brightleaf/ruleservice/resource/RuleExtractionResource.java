 package com.brightleaf.ruleservice.resource;

import static com.brightleaf.ruleservice.jwt.Constants.HEADER_STRING;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brightleaf.ruleservice.model.ApplicationExtractedValue;
import com.brightleaf.ruleservice.model.Attributes;
import com.brightleaf.ruleservice.model.ExtractedEntity;
import com.brightleaf.ruleservice.model.ExtractedEntityForSingleAttribute;
import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.model.RuleRuleSet;
import com.brightleaf.ruleservice.service.FileTypeFactoryService;
import com.brightleaf.ruleservice.service.RuleRuleSetService;
import com.brightleaf.ruleservice.serviceimpl.DocFileTypeServiceImpl;
import com.brightleaf.ruleservice.serviceimpl.PdfFileTypeServiceImpl;
import com.brightleaf.ruleservice.serviceimpl.PstFileTypeServiceImpl;
import com.brightleaf.ruleservice.util.AttributeUtil;

@CrossOrigin(origins = "*")
@RestController
public class RuleExtractionResource {
	private static Logger logger = Logger.getLogger(RuleExtractionResource.class);
	@Autowired
	RuleRuleSetService ruleRuleSetService;

	@Autowired
	RestTemplate restTemplate;

	@PostMapping(value = "/extractRule/{companyId}/{ruleSetId}/{documentTypeId}/{transactionId}")
	public String getRuleListById(@PathVariable("companyId") Integer companyId,
			@PathVariable("ruleSetId") Integer ruleSetId, @PathVariable("transactionId") String transactionId,
			@PathVariable("documentTypeId") Integer documentTypeId, @RequestBody List<String> docList,
			HttpServletRequest request) {
		logger.info("In extract rule");
		List<ExtractedEntityForSingleAttribute> listExtraction = new ArrayList<>();

		// find company Name from company id
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		// String urlWebService = "http://localhost:8081/";

		String urlWebService = "http://localhost:8081/UserCompanyService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		logger.info("ruleExtraction resource" + urlWebService + "company" + "/" + companyId);
		ResponseEntity<String> getCompanydata = this.restTemplate.exchange(urlWebService + "company" + "/" + companyId,
				HttpMethod.GET, entity, String.class);
		String companyData = getCompanydata.getBody();
		JSONParser parser = new JSONParser();
		JSONObject companyJSON = null;
		try {
			companyJSON = (JSONObject) parser.parse(companyData);
		} catch (ParseException e1) {
			logger.error("Error in getRuleListById", e1);
		}
		if (companyJSON == null) {
			return "Error";
		}

		String companyName = (String) companyJSON.get("companyName");// companyData.substring(0,
																		// companyData.indexOf('|'));
		logger.info("compannyy" + companyData + "frdfdf" + companyName);
		int qcLevels = 0;
		try {
			// qcLevels = Integer.parseInt(companyData.substring(companyData.indexOf('|') +
			// 1));
			qcLevels = (int)((long) companyJSON.get("numberOfQcLevels"));
			logger.info("qc levelsssss" + qcLevels);
		} catch (Exception e) {
			logger.error("Error in getRuleListById", e);
		}
		String outputDateFormat = (String) companyJSON.get("outputdateFormat");

		// Get List of rules
		List<RuleRuleSet> a = ruleRuleSetService.getRuleListById(ruleSetId, documentTypeId);
		List<Rule> listRule = new ArrayList<>();
		List<RuleRuleSet> tmpRules = new ArrayList<>();
		Set<Integer> uniqueAttrIds = new LinkedHashSet<>();
		a.forEach(r -> { 
			uniqueAttrIds.add(r.getRule().getAttributeId());
		});
		//Sort according to priority
		for (int attrId : uniqueAttrIds) {
			for (int i = 0; i < a.size(); i++) {
				RuleRuleSet ruleSetI = a.get(i);
				int attrI = ruleSetI.getRule().getAttributeId();
				if (attrI == attrId) {
					RuleRuleSet rrSet = new RuleRuleSet();
					rrSet.setRule(ruleSetI.getRule());
					rrSet.setRuleId(ruleSetI.getRuleId());
					rrSet.setRulePriority(ruleSetI.getRulePriority());
					rrSet.setRuleRuleSetId(ruleSetI.getRuleRuleSetId());
					rrSet.setRuleRuleSetList(ruleSetI.getRuleRuleSetList());
					rrSet.setRuleSet(ruleSetI.getRuleSet());
					rrSet.setRuleSetId(ruleSetI.getRuleSetId());
					tmpRules.add(rrSet);
				}
			}
		}
		
		for (int i = 0; i < tmpRules.size(); i++) {
			for (int j = i+1; j < tmpRules.size(); j++) {
				RuleRuleSet ruleSetI = tmpRules.get(i);
				RuleRuleSet ruleSetJ = tmpRules.get(j);
				int attrI = ruleSetI.getRule().getAttributeId();
				int attrJ = ruleSetJ.getRule().getAttributeId();
				if (attrI == attrJ) {
					if (ruleSetI.getRulePriority() != null && ruleSetJ.getRulePriority() != null && 
							ruleSetI.getRulePriority() > ruleSetJ.getRulePriority()) {
						RuleRuleSet tmp = ruleSetI;
						tmpRules.set(i, ruleSetJ);
						tmpRules.set(j, tmp);
					}
				}
			}
		}
		
		for (RuleRuleSet rulefrom : tmpRules) {
			Rule fullRules = rulefrom.getRule();
			listRule.add(fullRules);
		}

		for (String docName : docList) {
			File file = new File(docName);
			String ext = docName.substring(docName.lastIndexOf('.') + 1);
			if (ext.contentEquals("pdf")) {
				FileTypeFactoryService fileTypeFactoryService = new PdfFileTypeServiceImpl();
				fileTypeFactoryService.extractAttributes(file, companyId, listExtraction, listRule, request,
						outputDateFormat);
			} else if (ext.contentEquals("pst")) {
				FileTypeFactoryService fileTypeFactoryService = new PstFileTypeServiceImpl();
				fileTypeFactoryService.extractAttributes(file, companyId, listExtraction, listRule, request,
						outputDateFormat);
			} else if (ext.contains("doc")) {
				FileTypeFactoryService fileTypeFactoryService = new DocFileTypeServiceImpl();
				fileTypeFactoryService.extractAttributes(file, companyId, listExtraction, listRule, request,
						outputDateFormat);
			}
		}

		listExtraction.forEach(l -> {
			logger.info(l.getExtractedAttributeId());
			logger.info(l.getExtractedSentence());
			logger.info(l.getExtractedChunk());
			logger.info(l.getExtractedEntity());
			logger.info("***********************************");
		});
		logger.info("Done Extraction");
		Set<String> uniqueAttributes = new LinkedHashSet<>();
		listRule.forEach(r -> {
			AttributeUtil attributeUtil = new AttributeUtil();
			JSONObject attributeJSON = attributeUtil.getAttributeByAttributeId(r.getAttributeId(), request);
			String name = (String) attributeJSON.get("attributeName");
			String fallbackValue = (String) attributeJSON.get("fallbackValue");
			if (fallbackValue != null && !fallbackValue.contentEquals(""))
				uniqueAttributes.add(name + "|||" + fallbackValue);
			else
				uniqueAttributes.add(name);
		});

		List<ExtractedEntity> completeExtractedList = getAllAttributes(listExtraction, uniqueAttributes, companyId,
				docList, qcLevels, request);

		// call mongo service to add the data
		// String authorizationHeaderValue1 = request.getHeader(HEADER_STRING);
//		String urlWebService1 = "http://localhost:8098/";
		String urlWebService1 = "http://localhost:8098/MongoService/";
		HttpHeaders headers1 = new HttpHeaders();
		headers1.set(HEADER_STRING, authorizationHeaderValue);
		headers1.set("userName", request.getHeader("userName"));

		HttpEntity<?> entity1 = new HttpEntity<>(completeExtractedList, headers1);
		ResponseEntity<String> addInMongo = this.restTemplate.exchange(
				urlWebService1 + "postMongoDataInMongo" + "/" + companyName + "/" + transactionId + "/" + qcLevels + "/" + ruleSetId,
				HttpMethod.POST, entity1, new ParameterizedTypeReference<String>() {
				});

		return addInMongo.getBody();
	}

	private static List<ExtractedEntity> getAllAttributes(List<ExtractedEntityForSingleAttribute> listExtraction,
			Set<String> uniqueAttributes, Integer companyId, List<String> docList, int qcLevels,
			HttpServletRequest request) {
		List<ExtractedEntity> lst = new ArrayList<>();
		// Make sure all the attributes are covered and are in order of rules
		docList.forEach(docName -> {
			logger.info("document name" + docName);
			logger.info("qcLevels: " + qcLevels);
			String origDocName = docName.contains("\\") ? docName.substring(docName.lastIndexOf('\\') + 1)
					: docName.substring(docName.lastIndexOf('/') + 1);

			ExtractedEntity extEntity = new ExtractedEntity();
			extEntity.setCompanyId(companyId);
			extEntity.setDocumentName(origDocName);
			List<Attributes> listAttr = new ArrayList<>();
			extEntity.setAttributes(listAttr);
			uniqueAttributes.forEach(attributeName -> {
				String attrName = "";
				String fallbackValue = "N/A";
				if (attributeName.contains("|||")) {
					attrName = attributeName.substring(0, attributeName.indexOf("|||"));
					fallbackValue = attributeName.substring(attributeName.indexOf("|||") + 3);
				} else {
					attrName = attributeName;
				}
				boolean added = false;
				for (ExtractedEntityForSingleAttribute e : listExtraction) {
					Integer entityAttrId = e.getExtractedAttributeId();
					AttributeUtil attributeUtil = new AttributeUtil();
					JSONObject attributeJSON = attributeUtil.getAttributeByAttributeId(entityAttrId, request);
					String name = (String) attributeJSON.get("attributeName");

					String ed = e.getDocumentName();
					String eDocuName = ed.contains("\\") ? ed.substring(ed.lastIndexOf('\\') + 1)
							: ed.substring(ed.lastIndexOf('/') + 1);

					if (name.equals(attrName) && eDocuName.contentEquals(origDocName)) {
						Attributes attr = new Attributes();
						attr.setAttributeName(attrName);
						attr.setExtractedChunk(e.getExtractedChunk());
						attr.setAppliedRule(e.getAppliedRule());
						attr.setExtractedSentence(e.getExtractedSentence());
						attr.setPageNumber(e.getPageNumber());
						ApplicationExtractedValue appExtVal = new ApplicationExtractedValue();
						appExtVal.setInitialValue(e.getExtractedEntity());
						appExtVal.setValueAddedBy("SYSTEM");
						attr.setApplicationExtractedValue(appExtVal);
						listAttr.add(attr);
						added = true;
					}
				}
				if (!added) {
					Attributes attr = new Attributes();
					attr.setAttributeName(attrName);
					attr.setExtractedChunk("");
					attr.setAppliedRule(-1);
					attr.setExtractedSentence("");
					ApplicationExtractedValue appExtVal = new ApplicationExtractedValue();
					appExtVal.setInitialValue(fallbackValue);
					appExtVal.setValueAddedBy("SYSTEM");
					attr.setApplicationExtractedValue(appExtVal);
					listAttr.add(attr);
				}
			});
			lst.add(extEntity);
		});
		return lst;
	}
}
