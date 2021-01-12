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
import com.brightleaf.mongoservice.service.WriteVerifyService;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class WriteVerifyServiceImpl implements WriteVerifyService {

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

	protected final static Logger logger = Logger.getLogger(WriteVerifyServiceImpl.class);

	@Autowired
	UserQcRepository userQcRepository;

	@Override
	public Boolean mongoUpdateVerifyRecords(JSONObject json) {

		Boolean lastattribute = false;
		// Reading json and taking values in variables
		String documentName = json.getString(DOCUMENT_NAME);
		String attributeName = json.getString(ATTRIBUTE_NAME);
		Integer appliedRule = json.getInt(APPLIED_RULE);
		String initialValue = json.getString(INITIAL_VALUE);
		String newValue = json.getString(NEW_VALUE);
		String qcDoneBy = json.getString(QC_DONE_BY);
		Integer qcLevel = json.getInt(QC_LEVEL);
		String companyName = json.getString(COMPANY_NAME);
		String transactionId = json.getString(TRANSACTION_ID);

		try (MongoClient mongo = new MongoClient(LOCALHOST, 27017)) {
			// Calling Mongo
			MongoDatabase db = mongo.getDatabase(companyName);

			boolean exists = mongoCollectionExists(companyName, transactionId);
			if (exists) {
				MongoCollection<Document> collection = db.getCollection(transactionId);

				FindIterable<Document> document = collection.find(
						new BasicDBObject(DOCUMENT_NAME, documentName).append("Attributes.AttributeName", attributeName)
								.append("Attributes.AppliedRule", appliedRule));

				for (Document d : document) {
					List<Document> attributesDocs = (List<Document>) d.get(ATTRIBUTES);
					for (Document att : attributesDocs) {
						if (att.getString(ATTRIBUTE_NAME).equals(attributeName)
								&& (att.getInteger(APPLIED_RULE).equals(appliedRule))) {

							Document applicationExtractedValuesDocs = (Document) att.get(APPLICATION_EXTRACTED_VALUE);
							String latestValue = applicationExtractedValuesDocs.getString(INITIAL_VALUE);

							Document qcDocuments = (Document) att.get("QC" + qcLevel);

							Document valueDocument = new Document();
							valueDocument.put(INITIAL_VALUE, initialValue);
							valueDocument.put(NEW_VALUE, newValue);
							valueDocument.put(STATUS, "VERIFIED");
							valueDocument.put(VALUE_CHANGED, initialValue.contentEquals(newValue) ? "No" : "Yes");
							valueDocument.put(QC_DONE_BY, qcDoneBy);
							valueDocument.put(QC_LEVEL, qcLevel);
							valueDocument.put(IGNORE_RESULT, "NO");
							valueDocument.put(QC_DATE, new Date());

							if (qcDocuments == null) {
								if (qcLevel == 1) {
									if (latestValue.equals(initialValue) || latestValue == initialValue) {
										BasicDBObject query = new BasicDBObject();
										query.put(ATTRIBUTES, att);
										query.put(DOCUMENT_NAME, documentName);

										BasicDBObject data = new BasicDBObject();
										data.put(ATTRIBUTE_QC + qcLevel, valueDocument);

										BasicDBObject command = new BasicDBObject();
										command.put("$set", data);

										collection.updateOne(query, command);
										break;
									}
								} else {
									Document prevQCDocument = (Document) att.get("QC" + (qcLevel - 1));
									if (prevQCDocument != null) {
										String qcInititalValue = prevQCDocument.getString(INITIAL_VALUE);
										String changedValue = prevQCDocument.getString(NEW_VALUE);
										if (changedValue.equals(initialValue) || changedValue == initialValue
												|| qcInititalValue.contentEquals(initialValue)) {
											BasicDBObject query = new BasicDBObject();
											query.put(ATTRIBUTES, att);
											query.put(DOCUMENT_NAME, documentName);

											BasicDBObject data = new BasicDBObject();
											data.put(ATTRIBUTE_QC + qcLevel, valueDocument);

											BasicDBObject command = new BasicDBObject();
											command.put("$set", data);

											collection.updateOne(query, command);
											break;
										}
									}
								}
							} else {
								String qcInititalValue = qcDocuments.getString(INITIAL_VALUE);
								String changedValue = qcDocuments.getString(NEW_VALUE);
								if (changedValue.equals(initialValue) || changedValue == initialValue
										|| qcInititalValue.contentEquals(initialValue)) {
									BasicDBObject query1 = new BasicDBObject();
									query1.put(ATTRIBUTES, att);
									query1.put(DOCUMENT_NAME, documentName);

									BasicDBObject data1 = new BasicDBObject();
									data1.put(ATTRIBUTE_QC + qcLevel + STATUS_DOT, VERIFIED);
									data1.put(ATTRIBUTE_QC + qcLevel + ".NewValue", newValue);

									BasicDBObject command1 = new BasicDBObject();
									command1.put("$set", data1);

									collection.updateOne(query1, command1);
									break;
								}
							}
						}
					}
					if (checkIfItsLastAttribute(attributesDocs, qcLevel).equals(true)) {
						BasicDBObject query1 = new BasicDBObject();
						query1.put(DOCUMENT_NAME, documentName);

						BasicDBObject data1 = new BasicDBObject();
						data1.put("QC" + qcLevel + STATUS, "VERIFIED");

						BasicDBObject command1 = new BasicDBObject();
						command1.put("$set", data1);

						collection.updateOne(query1, command1);
						lastattribute = true;
						updateTransactionForQC(transactionId, qcLevel, qcDoneBy, companyName);
					}
				}
			}
		} catch (Exception e) {
			logger.error("mongoUpdateVerifyRecords" + e);
		}
		return lastattribute;
	}

	@Override
	public Boolean verifyTransaction(String companyName, String transactionId, Integer qcLevel, String qcDoneBy) {
		try (MongoClient mongo = new MongoClient(LOCALHOST, 27017)) {
			MongoDatabase db = mongo.getDatabase(companyName);
			boolean exists = mongoCollectionExists(companyName, transactionId);
			if (exists) {
				MongoCollection<Document> collection = db.getCollection(transactionId);
				FindIterable<Document> document = collection.find();
				for (Document d : document) {
					List<Document> attributesDocs = (List<Document>) d.get(ATTRIBUTES);
					for (Document att : attributesDocs) {
						Document qcDocuments = (Document) att.get("QC" + qcLevel);
						Document applicationExtractedValuesDocs = (Document) att.get(APPLICATION_EXTRACTED_VALUE);
						String initialValue = applicationExtractedValuesDocs.getString(INITIAL_VALUE);
						Document valueDocument = new Document();

						valueDocument.put(INITIAL_VALUE, initialValue);
						valueDocument.put(NEW_VALUE, initialValue);
						valueDocument.put(STATUS, "VERIFIED");
						valueDocument.put(VALUE_CHANGED, "No");
						valueDocument.put(QC_DONE_BY, qcDoneBy);
						valueDocument.put(QC_LEVEL, qcLevel);
						valueDocument.put(QC_DATE, new Date());
						valueDocument.put(IGNORE_RESULT, "NO");

						if (qcDocuments == null) {
							BasicDBObject query = new BasicDBObject();
							query.put(ATTRIBUTES, att);

							BasicDBObject data = new BasicDBObject();
							data.put(ATTRIBUTE_QC + qcLevel, valueDocument);
							data.put("QC" + qcLevel + STATUS, "VERIFIED");

							BasicDBObject command = new BasicDBObject();
							command.put("$set", data);

							collection.updateMany(query, command);
						} else {
							BasicDBObject query1 = new BasicDBObject();
							query1.put(ATTRIBUTES, att);

							BasicDBObject data1 = new BasicDBObject();
							data1.put(ATTRIBUTE_QC + qcLevel + STATUS_DOT, "VERIFIED");
							data1.put("QC" + qcLevel + STATUS, "VERIFIED");

							BasicDBObject command1 = new BasicDBObject();
							command1.put("$set", data1);

							collection.updateMany(query1, command1);
						}
					}
				}
				updateTransactionForQC(transactionId, qcLevel, qcDoneBy, companyName);
			}
		} catch (Exception e) {
			logger.error("verifyTransaction" + e);
		}
		return true;
	}

	@Override
	public Boolean mongoUpdateVerifyDocument(JSONObject json) {

		// Taking data from json to variables
		String documentName = json.getString(DOCUMENT_NAME);
		Integer qcLevel = json.getInt(QC_LEVEL);
		String companyName = json.getString(COMPANY_NAME);
		String transactionId = json.getString(TRANSACTION_ID);
		String qcDoneBy = json.getString(QC_DONE_BY);

		try (MongoClient mongo = new MongoClient(LOCALHOST, 27017)) {
			MongoDatabase db = mongo.getDatabase(companyName);
			boolean exists = mongoCollectionExists(companyName, transactionId);
			if (exists) {
				MongoCollection<Document> collection = db.getCollection(transactionId);
				FindIterable<Document> document = collection.find(new BasicDBObject(DOCUMENT_NAME, documentName));
				for (Document d : document) {
					List<Document> attributesDocs = (List<Document>) d.get(ATTRIBUTES);
					for (Document att : attributesDocs) {
						Document qcDocuments = (Document) att.get("QC" + qcLevel);
						Document applicationExtractedValuesDocs = (Document) att.get(APPLICATION_EXTRACTED_VALUE);
						String initialValue = applicationExtractedValuesDocs.getString(INITIAL_VALUE);
						Document valueDocument = new Document();

						valueDocument.put(INITIAL_VALUE, initialValue);
						valueDocument.put(NEW_VALUE, initialValue);
						valueDocument.put(STATUS, "VERIFIED");
						valueDocument.put(VALUE_CHANGED, "No");
						valueDocument.put(QC_DONE_BY, qcDoneBy);
						valueDocument.put(QC_LEVEL, qcLevel);
						valueDocument.put(QC_DATE, new Date());
						valueDocument.put(IGNORE_RESULT, "NO");

						if (qcDocuments == null) {
							BasicDBObject query = new BasicDBObject();
							query.put(ATTRIBUTES, att);
							query.put(DOCUMENT_NAME, documentName);

							BasicDBObject data = new BasicDBObject();
							data.put(ATTRIBUTE_QC + qcLevel, valueDocument);
							data.put("QC" + qcLevel + STATUS, "VERIFIED");

							BasicDBObject command = new BasicDBObject();
							command.put("$set", data);

							collection.updateMany(query, command);
							// continue;
						} else {
							BasicDBObject query1 = new BasicDBObject();
							query1.put(ATTRIBUTES, att);

							logger.info("query" + query1);

							BasicDBObject data1 = new BasicDBObject();
							data1.put(ATTRIBUTE_QC + qcLevel + STATUS_DOT, "VERIFIED");
							data1.put("QC" + qcLevel + STATUS, "VERIFIED");

							logger.info("data" + data1);

							BasicDBObject command1 = new BasicDBObject();
							command1.put("$set", data1);

							logger.info("command" + command1);

							collection.updateMany(query1, command1);
							// continue;
						}
						BasicDBObject query1 = new BasicDBObject();
						query1.put(DOCUMENT_NAME, documentName);

						BasicDBObject data1 = new BasicDBObject();
						data1.put("QC" + qcLevel + STATUS, "VERIFIED");

						BasicDBObject command1 = new BasicDBObject();
						command1.put("$set", data1);

						collection.updateOne(query1, command1);
					}
					updateTransactionForQC(transactionId, qcLevel, qcDoneBy, companyName);
				}
			}
		} catch (Exception e) {
			logger.error("mongoUpdateVerifyDocument" + e);
		}
		return true;
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

	private boolean checkTransactionStatus(String transactionId, Integer qcLevel, String companyName) {
		try (MongoClient mongo = new MongoClient(LOCALHOST, 27017)) {
			MongoDatabase db = mongo.getDatabase(companyName);
			boolean exists = mongoCollectionExists(companyName, transactionId);
			if (exists) {
				MongoCollection<Document> collection = db.getCollection(transactionId);
				FindIterable<Document> document = collection.find();
				for (Document d : document) {
					String status = d.getString("QC" + qcLevel + "Status");
					if (status.contentEquals("UNVERIFIED"))
						return false;
				}
			}
		} catch (Exception e) {
			logger.error("checkTransactionStatus", e);
		}
		return true;
	}
	
	private String updateTransactionForQC(String transactionId, Integer qcLevel, String qcDoneBy, String companyName) {
		if (checkTransactionStatus(transactionId, qcLevel, companyName)) {
			UserQC qcRecord = userQcRepository.getUserQc(transactionId, qcLevel);
			qcRecord.setStatus(VERIFIED);
			userQcRepository.save(qcRecord);

			UserQC qcRecord1 = userQcRepository.getUserQc(transactionId, qcLevel + 1);
			if (qcRecord1 == null) {
				UserQC userQc = new UserQC();
				userQc.setAssignedBy(qcDoneBy);
				userQc.setCompanyName(companyName);
				userQc.setQcLevel(qcLevel + 1);
				userQc.setStatus("UNVERIFIED");
				userQc.setTransactionId(transactionId);
				userQc.setCreatedBy(qcDoneBy);
				userQc.setCreationDate(new Date());
				userQc.setLastModifiedBy(qcDoneBy);
				userQc.setLastModifiedDate(new Date());
				userQcRepository.save(userQc);
			} else {
				qcRecord1.setStatus("UNVERIFIED");
				userQcRepository.save(qcRecord1);
			}
		}
		return "Updated Row to verified and added a new Row";
	}

	private Boolean checkIfItsLastAttribute(List<Document> attributesDocs, Integer qcLevel) {
		int unverified = 0;
		for (Document att : attributesDocs) {
			Document qcDocuments = (Document) att.get("QC" + qcLevel);
			if (qcDocuments == null) {
				unverified++;
				continue;
			}
			String status = qcDocuments.getString(STATUS);
			if (!status.contentEquals("VERIFIED")) {
				unverified++;
			}
		}
		if (unverified == 1) {
			return true;
		}
		return false;
	}
}