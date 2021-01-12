package com.brightleaf.mongoservice.serviceimpl;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.brightleaf.mongoservice.service.WriteAttributeService;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class WriteAttributeServiceImpl implements WriteAttributeService {
	
	private static final String DOCUMENT_NAME = "DocumentName";

	private static final String ATTRIBUTE_NAME = "AttributeName";

	private static final String APPLIED_RULE = "AppliedRule";

	private static final String INITIAL_VALUE = "InitialValue";

	private static final String NEW_VALUE = "NewValue";

	private static final String QC_DONE_BY = "QcDoneBy";

	private static final String QC_LEVEL = "QcLevel";

	private static final String COMPANY_NAME = "CompanyName";

	private static final String TRANSACTION_ID = "TransactionId";

	private static final String LOCALHOST = "localhost";

	private static final String ATTRIBUTES = "Attributes";

	private static final String APPLICATION_EXTRACTED_VALUE = "ApplicationExtractedValue";

	private static final String STATUS = "Status";

	private static final String VERIFIED = "VERIFIED";

	private static final String VALUE_CHANGED = "ValueChanged";

	private static final String QC_DATE = "QCDate";

	private static final String ATTRIBUTE_QC = "Attributes.$.QC";

	private static final String STATUS_DOT = ".Status";

	private static final String IGNORE_RESULT = "IgnoreResult";

	protected final static Logger logger = Logger.getLogger(WriteAttributeServiceImpl.class);
	
	@Override
	public String mongoUpdateAttribute(final String companyName, final String transactionId, Integer qcLevel,
			JSONObject json) {
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
			String documentName = json.getString("documentName");
			String attributeName = json.getString("attributeName");
			String initialValue = json.getString("attributeValue");
			String valueAddedBy = json.getString("qcDoneBy");
			MongoDatabase db = mongo.getDatabase(companyName);

			boolean exists = mongoCollectionExists(companyName, transactionId);
			if (exists) {
				MongoCollection<Document> collection = db.getCollection(transactionId);

				FindIterable<Document> document = collection.find(new BasicDBObject("DocumentName", documentName));

				// update QC*Status
				BasicDBObject searchQuery = new BasicDBObject().append(DOCUMENT_NAME, documentName).append("QC" + qcLevel + "Status",
						document.first().getString("QC" + qcLevel + "Status"));
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set", new BasicDBObject().append("QC" + qcLevel + "Status", "UNVERIFIED"));
				collection.updateOne(searchQuery, newDocument);

				Document attribDocument = new Document();
				attribDocument.put("AttributeName", attributeName);
				attribDocument.put("AppliedRule", 0);
				attribDocument.put("ExtractedSentence", "");
				attribDocument.put("ExtractedChunk", "");
				attribDocument.put("PageNumber", -1);

				Document applicationExtractedValue = new Document();
				applicationExtractedValue.put("InitialValue", initialValue);
				applicationExtractedValue.put("ValueAddedBy", valueAddedBy);
				attribDocument.put("ApplicationExtractedValue", applicationExtractedValue);

				BasicDBObject query = new BasicDBObject();
				query.put(DOCUMENT_NAME, documentName);

				BasicDBObject data = new BasicDBObject();
				data.put("Attributes", attribDocument);

				BasicDBObject command = new BasicDBObject();
				command.put("$push", data);
				collection.updateOne(query, command);

			}
		} catch (Exception e) {
			logger.error("mongoUpdateAttribute", e);
		}
		return new Gson().toJson(transactionId);
	}

	private boolean mongoCollectionExists(String dbName, String transactionId) {
		boolean found = false;
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
			MongoDatabase db = mongo.getDatabase(dbName);
			// Check if collection exists
			boolean exists = db.listCollectionNames().into(new ArrayList<String>()).contains(transactionId);
			if (exists)
				found = true;
		} catch (Exception e) {
			logger.error("mongoCollectionExists", e);
		}
		return found;
	}

	
	@Override
	public String addValueForAttribute(JSONObject jsonObject,String companyName) {
		String msg="";
		try (MongoClient mongo = new MongoClient("localhost", 27017)){
			String documentName = jsonObject.getString("documentName");
			String attributeName = jsonObject.getString("attributeName");
			String initialValue = jsonObject.getString("attributeValue");
			String valueAddedBy = jsonObject.getString("qcDoneBy");
			String transactionId=jsonObject.getString("transactionId");
			Integer qcLevel=jsonObject.getInt("qcLevel");
			MongoDatabase db = mongo.getDatabase(companyName);
			
			boolean exists = mongoCollectionExists(companyName, transactionId);
			
			if (exists) {
				MongoCollection<Document> collection = db.getCollection(transactionId);

				FindIterable<Document> document = collection.find(new BasicDBObject("DocumentName", documentName));

				// update QC*Status
//				BasicDBObject searchQuery = new BasicDBObject().append(DOCUMENT_NAME, documentName).append("QC"+qcLevel+"Status",
//						document.first().getString("QC"+qcLevel+"Status"));
//				BasicDBObject newDocument = new BasicDBObject();
//				newDocument.append("$set", new BasicDBObject().append("QC"+qcLevel+"Status","UNVERIFIED"));
//				collection.updateOne(searchQuery, newDocument);

				Document attribDocument = new Document();
				attribDocument.put("AttributeName", attributeName);
				attribDocument.put("AppliedRule", -1);
				attribDocument.put("ExtractedSentence", "");
				attribDocument.put("ExtractedChunk", "");
				attribDocument.put("PageNumber", -1);

				Document applicationExtractedValue = new Document();
				applicationExtractedValue.put("InitialValue", "");
				applicationExtractedValue.put("ValueAddedBy", valueAddedBy);
				attribDocument.put("ApplicationExtractedValue", applicationExtractedValue);
				
				Document valueDocument = new Document();
				valueDocument.put(INITIAL_VALUE, "");
				valueDocument.put(NEW_VALUE, initialValue);
				valueDocument.put(STATUS, "VERIFIED");
				valueDocument.put(VALUE_CHANGED, "Yes");
				valueDocument.put(QC_DONE_BY, valueAddedBy);
				valueDocument.put(QC_LEVEL, qcLevel);
				valueDocument.put(IGNORE_RESULT, "NO");
				valueDocument.put(QC_DATE, new Date());
				attribDocument.put("QC"+qcLevel, valueDocument);
				

				BasicDBObject query = new BasicDBObject();
				query.put(DOCUMENT_NAME, documentName);

				BasicDBObject data = new BasicDBObject();
				data.put("Attributes", attribDocument);

				BasicDBObject command = new BasicDBObject();
				command.put("$push", data);
				collection.updateOne(query, command);
				msg="sucess";

			}
			
		} catch(Exception e) {
			logger.error("addValueForAttribute", e);
		}
		return new Gson().toJson(msg) ;
	}
	
	
	
}