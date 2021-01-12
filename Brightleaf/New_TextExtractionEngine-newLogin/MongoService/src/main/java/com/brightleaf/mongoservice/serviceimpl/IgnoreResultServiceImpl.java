package com.brightleaf.mongoservice.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.brightleaf.mongoservice.service.IgnoreResultService;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class IgnoreResultServiceImpl implements IgnoreResultService {
	protected final static Logger logger = Logger.getLogger(IgnoreResultServiceImpl.class);
	@Override
	public String ignoreResult(String companyName, JSONObject jsonObject) {
		MongoClient mongo = null;
		try {
			String documentName = jsonObject.getString("documentName");
			String attributeName = jsonObject.getString("attributeName");
			String transactionId = jsonObject.getString("transactionId");
			Integer qcLevel = jsonObject.getInt("qcLevel");
			String ignoreResult = jsonObject.getString("ignoreResult");
			String attributeValue = jsonObject.getString("attributeValue");

			// Calling Mongo
			mongo = new MongoClient("localhost", 27017);
			MongoDatabase db = mongo.getDatabase(companyName);
			boolean exists = mongoCollectionExists(companyName, transactionId);
			if (exists) {
				MongoCollection<Document> collection = db.getCollection(transactionId);

				FindIterable<Document> document = collection.find(new BasicDBObject("DocumentName", documentName)
						.append("Attributes.AttributeName", attributeName));

				for (Document d : document) {
					List<Document> attributesDocs = (List<Document>) d.get("Attributes");
					for (Document att : attributesDocs) {
						if (att.getString("AttributeName").equals(attributeName)) {
							Document QCDocuments = (Document) att.get("QC" + qcLevel);
							if (QCDocuments != null) {
								if (QCDocuments.getString("NewValue").equals(attributeValue)) {
									BasicDBObject query = new BasicDBObject();
									query.put("Attributes", att);
									query.put("DocumentName", documentName);

									System.err.println("query" + query);

									BasicDBObject data = new BasicDBObject();
									data.put("Attributes.$.QC" + qcLevel + ".IgnoreResult", ignoreResult);

									BasicDBObject command1 = new BasicDBObject();
									command1.put("$set", data);

									collection.updateOne(query, command1);
									break;
								}
							} else {
								break;
							}
						}
					}
				}

			}

		} catch (Exception e) {
			logger.error("error in ignoreResult", e);
		}

		return null;
	}

	private boolean mongoCollectionExists(String dbName, String transactionId) {
		MongoClient mongo = null;
		boolean found = false;
		try {
			mongo = new MongoClient("localhost", 27017);
			MongoDatabase db = mongo.getDatabase(dbName);
			// Check if collection exists
			boolean exists = db.listCollectionNames().into(new ArrayList<String>()).contains(transactionId);
			if (exists)
				found = true;
		} catch (Exception e) {

		} finally {
			mongo.close();
		}
		return found;
	}

}
