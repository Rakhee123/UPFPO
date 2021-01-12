package com.brightleaf.mongoservice.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.mongoservice.model.ApplicationExtractedValue;
import com.brightleaf.mongoservice.model.Attributes;
import com.brightleaf.mongoservice.model.ExtractedEntity;
import com.brightleaf.mongoservice.model.UserQC;
import com.brightleaf.mongoservice.repository.UserQcRepository;
import com.brightleaf.mongoservice.service.WriteMongoRecordsService;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class WriteMongoRecordsServiceImpl implements WriteMongoRecordsService {

	protected final static Logger logger = Logger.getLogger(WriteMongoRecordsServiceImpl.class);

	@Autowired
	UserQcRepository transactionRepository;

	public String mongoAddRecords(String dbName, List<ExtractedEntity> listExtraction, String transactionId,
			String qcLevels, Integer ruleSetId, HttpServletRequest request) {
		if (listExtraction == null || listExtraction.isEmpty())
			return "";

		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
			MongoDatabase db = mongo.getDatabase(dbName);
			MongoCollection<Document> collection = db.getCollection(transactionId);

			Set<String> uniqueDocuments = new LinkedHashSet<>();
			listExtraction.forEach(e -> uniqueDocuments.add(e.getDocumentName()));

			for (String docName : uniqueDocuments) {
				Document document = new Document();
				document.put("DocumentName", docName);
				List<Document> attributeList = new ArrayList<>();
				for (ExtractedEntity l : listExtraction) {
					String currentDocName = l.getDocumentName();
					Integer companyId = l.getCompanyId();
					if (currentDocName.contentEquals(docName)) {
						document.put("CompanyId", companyId);
						document.put("TransactionId", transactionId);
						document.put("TransactionDate", new Date());
						int qclevels = 0;
						qclevels = Integer.parseInt(qcLevels);
						for (int i = 1; i <= qclevels; i++) {
							document.put("QC" + i + "Status", "UNVERIFIED");
						}
						List<Attributes> listOfAttributes = l.getAttributes();
						for (Attributes asdf : listOfAttributes) {
							Document attribDocument = new Document();
							attribDocument.put("AttributeName", asdf.getAttributeName());
							attribDocument.put("AppliedRule", asdf.getAppliedRule());
							attribDocument.put("ExtractedSentence", asdf.getExtractedSentence());
							attribDocument.put("ExtractedChunk", asdf.getExtractedChunk());
							Integer pnum = asdf.getPageNumber();
							if (pnum == null)
								attribDocument.put("PageNumber", -1);
							else
								attribDocument.put("PageNumber", pnum);
							ApplicationExtractedValue aev = asdf.getApplicationExtractedValue();
							Document valueDocument = new Document();
							valueDocument.put("InitialValue", aev.getInitialValue());
							if (aev.getValueAddedBy() == null) {
								valueDocument.put("ValueAddedBy", "SYSTEM");
							}
							attribDocument.put("ApplicationExtractedValue", valueDocument);
							attributeList.add(attribDocument);
						}
						document.put("Attributes", attributeList);
						collection.insertOne(document);
					}
				}
			}
			insertTransactionForQC(transactionId, request.getHeader("username"), dbName, ruleSetId);
		} catch (Exception e) {
			logger.error("mongoAddRecords", e);
		}
		return new Gson().toJson(transactionId);
	}

	private void insertTransactionForQC(String transactionId, String emailAdd, String companyName, Integer ruleSetId) {
		UserQC userQc = new UserQC();
		userQc.setAssignedBy(emailAdd);
		userQc.setCompanyName(companyName);
		userQc.setQcLevel(1);
		userQc.setStatus("UNVERIFIED");
		userQc.setTransactionId(transactionId);
		userQc.setCreatedBy(emailAdd);
		userQc.setCreationDate(new Date());
		userQc.setLastModifiedBy(emailAdd);
		userQc.setLastModifiedDate(new Date());
		userQc.setRuleSetId(ruleSetId);
		transactionRepository.save(userQc);
	}
}
