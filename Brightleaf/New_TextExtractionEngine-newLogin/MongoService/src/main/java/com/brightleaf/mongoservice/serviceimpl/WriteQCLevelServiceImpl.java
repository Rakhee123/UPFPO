package com.brightleaf.mongoservice.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.mongoservice.model.UserQC;
import com.brightleaf.mongoservice.repository.UserQcRepository;
import com.brightleaf.mongoservice.service.WriteQCLevelService;
import com.brightleaf.mongoservice.jwt.Constants;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class WriteQCLevelServiceImpl implements WriteQCLevelService {
	@Autowired
	UserQcRepository userQcRepository;

	protected final static Logger logger = Logger.getLogger(WriteQCLevelServiceImpl.class);
	private static final String DOCUMENT_NAME = "DocumentName";

	private static final String STATUS = "Status";

	@Override
	public String mongoUpdateQARecords(final String companyName, final String transactionId, JSONObject json) {
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
			String documentName = json.getString(DOCUMENT_NAME);
			String attributeName = json.getString("AttributeName");
			Integer appliedRule = json.getInt("AppliedRule");
			String initialValue = json.getString(Constants.INITIALVALUE);
			Integer qCLevel = json.getInt(Constants.QCLEVEL);
			String newValue = json.getString(Constants.NEWVALUE);
			String status = json.getString(Constants.STATUS);
			String qcDoneBy = json.getString(Constants.QCDONEBY);
			String ignoreResult = "NO";
			if (json.has(Constants.IGNORERESULT))
				ignoreResult = json.getString(Constants.IGNORERESULT);
			if (!(initialValue == newValue || initialValue.equals(newValue))) {
				// Calling Mongo
				MongoDatabase db = mongo.getDatabase(companyName);

				boolean exists = mongoCollectionExists(companyName, transactionId);
				if (exists) {
					MongoCollection<Document> collection = db.getCollection(transactionId);

					FindIterable<Document> document = collection.find(new BasicDBObject(DOCUMENT_NAME, documentName)
							.append("Attributes.AppliedRule", appliedRule));

					for (Document d : document) {

						BasicDBObject searchQuery = new BasicDBObject().append(DOCUMENT_NAME, documentName).append("QC" + qCLevel + STATUS,
								d.getString("QC" + qCLevel + STATUS));
						BasicDBObject newDocument = new BasicDBObject();
						newDocument.append("$set", new BasicDBObject().append("QC" + qCLevel + STATUS, "UNVERIFIED"));
						collection.updateOne(searchQuery, newDocument);

						List<Document> attributesDocs = (List<Document>) d.get("Attributes");

						for (Document att : attributesDocs) {
							if ((att.getString("AttributeName").equals(attributeName))
									&& (att.getInteger("AppliedRule").equals(appliedRule))) {
								Document QCDocuments = (Document) att.get("QC" + qCLevel);
								if (QCDocuments != null) {
									String qcInititalValue = QCDocuments.getString("InitialValue");

									String latestValue = QCDocuments.getString("NewValue");
									Document valueDocument = new Document();

									valueDocument.put(Constants.INITIALVALUE, initialValue);
									valueDocument.put(Constants.NEWVALUE, newValue);
									valueDocument.put(Constants.STATUS, status);
									valueDocument.put("ValueChanged",
											initialValue.contentEquals(newValue) ? "No" : "Yes");
									valueDocument.put(Constants.QCDONEBY, qcDoneBy);
									valueDocument.put(Constants.QCLEVEL, qCLevel);
									valueDocument.put(Constants.IGNORERESULT, ignoreResult);
									valueDocument.put("QCDate", new Date());
									if (latestValue.equals(initialValue) || latestValue == initialValue
											|| qcInititalValue.contentEquals(initialValue)) {
										BasicDBObject query = new BasicDBObject();
										query.put("Attributes", att);
										query.put(DOCUMENT_NAME, documentName);

										BasicDBObject data = new BasicDBObject();
										data.put("Attributes.$.QC" + qCLevel, valueDocument);

										BasicDBObject command = new BasicDBObject();
										command.put("$set", data);
										collection.updateOne(query, command);
										// break;
										if (status.contentEquals("UNVERIFIED")) {
											boolean done = false;
											int qLevel = qCLevel + 1;
											while (!done) {
												List<Document> docs = collection
														.find(new BasicDBObject("DocumentName", documentName)
																.append("QC" + qLevel + "Status", "VERIFIED"))
														.into(new ArrayList<Document>());
												if (docs != null && docs.size() > 0) {
													BasicDBObject query1 = new BasicDBObject();
													query1.put("DocumentName", documentName);

													BasicDBObject data1 = new BasicDBObject();

													data1.put("QC" + qLevel + "Status", "UNVERIFIED");

													BasicDBObject command1 = new BasicDBObject();
													command1.put("$set", data1);

													collection.updateOne(query1, command1);
													qLevel++;
												} else {
													done = true;
												}
											}
										}
									}
								} else {
									String latestValue = null;
									if(qCLevel == 1) {
										Document applicationExtractedValuesDocs = (Document) att
												.get("ApplicationExtractedValue");
										latestValue = applicationExtractedValuesDocs.getString("InitialValue");
									} else {
										Document prevQCDocument = (Document) att.get("QC" + (qCLevel - 1));
										if (prevQCDocument != null) {
											latestValue = prevQCDocument.getString("NewValue");
										}
									}

									if (latestValue.equals(initialValue) || latestValue == initialValue) {
										Document valueDocument = new Document();

										valueDocument.put(Constants.INITIALVALUE, initialValue);
										valueDocument.put(Constants.NEWVALUE, newValue);
										valueDocument.put(Constants.STATUS, status);
										valueDocument.put("ValueChanged",
												initialValue.contentEquals(newValue) ? "No" : "Yes");
										valueDocument.put(Constants.QCDONEBY, qcDoneBy);
										valueDocument.put(Constants.QCLEVEL, qCLevel);
										valueDocument.put(Constants.IGNORERESULT, "NO");
										valueDocument.put("QCDate", new Date());

										BasicDBObject query = new BasicDBObject();
										query.put("Attributes", att);
										query.put("DocumentName", documentName);

										BasicDBObject data = new BasicDBObject();
										data.put("Attributes.$.QC" + qCLevel, valueDocument);
										data.put("QC" + qCLevel + "Status", status);

										BasicDBObject command = new BasicDBObject();
										command.put("$set", data);

										collection.updateMany(query, command);
									}
								}
								if (status.contentEquals("UNVERIFIED")) {
									updateStatusToUnverifiedinSQL(transactionId, qCLevel);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("mongoUpdateQARecords", e);
		}
		return new Gson().toJson(transactionId);
	}

	private void updateStatusToUnverifiedinSQL(String transactionId, Integer qcLevel) {
		Integer qLevel = qcLevel;
		UserQC qcRecord = userQcRepository.getUserQc(transactionId, qLevel);
		if (qcRecord != null) {
			qcRecord.setStatus("UNVERIFIED");
			userQcRepository.save(qcRecord);
		}
		qLevel++;
		boolean done = false;
		while (!done) {
			qcRecord = userQcRepository.getUserQc(transactionId, qLevel);
			if (qcRecord != null) {
				userQcRepository.delete(qcRecord);
				qLevel++;
			} else {
				done = true;
			}
		}
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
	public String changeCustemValue(String companyName, String transactionId, JSONObject object) {
		// TODO Auto-generated method stub
		return mongoUpdateQARecords(companyName,transactionId,object);
	}
}