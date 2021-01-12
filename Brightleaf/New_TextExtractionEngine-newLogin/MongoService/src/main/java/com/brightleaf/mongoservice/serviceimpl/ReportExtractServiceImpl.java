package com.brightleaf.mongoservice.serviceimpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.brightleaf.mongoservice.model.ReportExtractCompanyWise;
import com.brightleaf.mongoservice.model.ReportExtractUserWise;
import com.brightleaf.mongoservice.service.ReportExtractService;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

@Service
public class ReportExtractServiceImpl implements ReportExtractService {
	private static Logger logger = Logger.getLogger(ReportExtractServiceImpl.class);
	@Override
	public ReportExtractCompanyWise mongoReadVerifiedTransactions(String companyName) {
		MongoClient mongo = null;
		ReportExtractCompanyWise rep =  new ReportExtractCompanyWise();
		try {
			mongo = new MongoClient("localhost", 27017);
			MongoDatabase db = mongo.getDatabase(companyName);
			MongoIterable<String> transactions = db.listCollectionNames();
			int maxQCLevels = 5;
			if (transactions == null) {
				return null;
			}
			MongoCursor<String> cursor = transactions.iterator();
			int transactionCount = 0;
			int totalExtraction = 0;
			int correctExtraction = 0;
			int incorrectExtraction = 0;
			while (cursor.hasNext()) {
				String transactionId = cursor.next();
				logger.info("Transaction ID = " + transactionId);
				if (transactionId.contains("TXN")) {
					MongoCollection<Document> collection = db.getCollection(transactionId);
					FindIterable<Document> documents = collection.find();
					boolean allVerified = true;
					for (Document d : documents) {
						for (int i = 1; i <= maxQCLevels; i++) {
							Object o = d.get("QC" + i + "Status");
							if (o == null) {
								maxQCLevels = i - 1;
								break;
							}
							if (o != null) {
								if (!o.toString().contentEquals("VERIFIED")) {
									allVerified = false;
									break;
								}
							}
						}
						if (!allVerified)
							break;
					}
					if (allVerified && maxQCLevels > 0) {
						logger.info("In allverified maxQCLevels = " + maxQCLevels);
						for (Document d : documents) {
							logger.info("Transaction Id = " + transactionId);
							List<Document> attributesDocs = (List<Document>) d.get("Attributes");
							if (attributesDocs == null)
								logger.info("Attribute docs = null");
							for (Document att : attributesDocs) {
								Document QCDocuments = (Document) att.get("QC" + maxQCLevels);
								String newValueOfMaxQc = (String) QCDocuments.get("NewValue");

								Document EXV = (Document) att.get("ApplicationExtractedValue");
								String initialValue = (String) EXV.get("InitialValue");
								if (QCDocuments == null || EXV == null)
									break;
								if (initialValue.contentEquals(newValueOfMaxQc)) {
									correctExtraction++;
								} else {
									incorrectExtraction++;
								}
								totalExtraction++;
							}
						}
						// Create ReportCompanyWise
						rep.setCompanyName(companyName);
						transactionCount++;
						rep.setTotalTransaction(transactionCount);
		                rep.setTotalExtractions(totalExtraction);
						rep.setCorrectExtraction(correctExtraction);
						rep.setIncorrectExtraction(incorrectExtraction);
						String comPercentage = (100 * correctExtraction / totalExtraction) + "%";
						rep.setAccuracyPercentage(comPercentage);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error in mongoReadVerifiedTransactions", e);
		} finally {
			mongo.close();
		}
		return rep;
	}
	
	@Override
	public List<ReportExtractUserWise> mongoReadVerifiedTransactionsUser(String companyName) {
		MongoClient mongo = null;
		List<ReportExtractUserWise> list = new ArrayList<>();
		try {
			mongo = new MongoClient("localhost", 27017);
			MongoDatabase db = mongo.getDatabase(companyName);
			MongoIterable<String> transactions = db.listCollectionNames();
			if (transactions == null) {
				return null;
			}
			MongoCursor<String> cursor = transactions.iterator();
			int maxQCLevels = 5;
			//int totalAttributes = 0;
			//int totalChangedAttributes = 0;
			class personStats {
				String companyName = "";
				String personName = "";
				List<Integer> totalAttributes = new ArrayList<>(Collections.nCopies(5, 0)); // To initialize the list Assuming maximum QC levels are 5
				List<Integer> changedAttributes = new ArrayList<>(Collections.nCopies(5, 0));
				ArrayList<String> changedAttributeList = new ArrayList<String>();
				List<Integer> attributeChangedByNextQC = new ArrayList<>(Collections.nCopies(5, 0));
				ArrayList<String> changedByNextQCAttributeList = new ArrayList<String>();
				ArrayList<String> transactionIds = new ArrayList<String>();
			};
			List<personStats> pstats = new ArrayList<>();
			
			while (cursor.hasNext()) {
				String transactionId = cursor.next();
				if (transactionId.contains("TXN")) {
					MongoCollection<Document> collection = db.getCollection(transactionId);
					FindIterable<Document> documents = collection.find();
					boolean allVerified = true;
					for (Document d : documents) {
						for (int i = 1; i <= maxQCLevels; i++) {
							Object o = d.get("QC" + i + "Status");
							if (o == null) {
								maxQCLevels = i - 1;
								break;
							}
							if (o != null) {
								if (!o.toString().contentEquals("VERIFIED")) {
									allVerified = false;
									break;
								}
							}
						}
						if (!allVerified)
							break;
					}
					// Transaction is verified
					if (allVerified && maxQCLevels > 0) {
						//jsonstats.put("companyName", dbName);
						for (Document d : documents) {
							List<Document> attributesDocs = (List<Document>) d.get("Attributes");
							for (Document att : attributesDocs) {
								//totalAttributes++; // This will give total attributes for the selected company
								String attributeName = att.getString("AttributeName");
								for (int qcLevel = 1; qcLevel <= maxQCLevels; qcLevel++) {
									Document QCDocuments = (Document) att.get("QC" + qcLevel);
									if (QCDocuments == null)
										break;
									Object vc = QCDocuments.get("ValueChanged");
									String qcDoneBy = QCDocuments.getString("QcDoneBy");
									personStats ps = null;
									for (personStats s : pstats) {
										if (s.personName.contentEquals(qcDoneBy)) {
											ps = s;
											break;
										}
									}
									if (ps == null) {
										ps = new personStats();
										ps.personName = qcDoneBy;
										ps.companyName = companyName;
										pstats.add(ps);
									}
									//SHAMA jsonstats.put("userName", qcDoneBy); // User Name
									ps.totalAttributes.set(qcLevel - 1, ps.totalAttributes.get(qcLevel-1)+1);

									if (vc != null && vc.toString().contentEquals("Yes")) { // Value is changed
										//totalChangedAttributes++; // This will give total changed attributes for the
																	// selected company
										ps.changedAttributes.set(qcLevel - 1,
												ps.changedAttributes.get(qcLevel - 1) + 1); // This will give
										// the total attributes changed by this QC person
										if (!ps.transactionIds.contains(transactionId))
											ps.transactionIds.add(transactionId);
										if (!ps.changedAttributeList.contains(attributeName))
											ps.changedAttributeList.add(attributeName);
										
									}
									if (qcLevel + 1 <= maxQCLevels) {
										Document nextQCDocuments = (Document) att.get("QC" + (qcLevel + 1));
										if (nextQCDocuments != null
												&& nextQCDocuments.getString("ValueChanged").contentEquals("Yes")) {
											// This will give total attributes changed by the next QC person
											ps.attributeChangedByNextQC.set(qcLevel - 1,
													ps.attributeChangedByNextQC.get(qcLevel - 1) + 1);
											if (!ps.changedByNextQCAttributeList.contains(attributeName))
												ps.changedByNextQCAttributeList.add(attributeName);
											//attributeList.add(att.get("AttributeName").toString());
										}
									}
								}
								//jsonstats.put("totalChangedAttributes", totalChangedAttributes);
								//jsonstats.put("totalAttributes", totalAttributes);
								//float attrPerFloat = (float) (100 * totalChangedAttributes / totalAttributes);
								//DecimalFormat decimalFormat = new DecimalFormat("#.00");
								//String numberAsString = decimalFormat.format(attrPerFloat);
								//String Percentage_of_Changes_Performed = numberAsString + "%";
								//jsonstats.put("percent", Percentage_of_Changes_Performed);
							}
							//jsonstats.put("attributeList", attributeList.toString());
						}
						//transactionIds.add(transactionId);
						//jsonstats.put("transactionId", transactionIds.toString());
					}
				}
			}
			for (personStats p : pstats) {
				ReportExtractUserWise rep = new ReportExtractUserWise();
				rep.setCompanyName(p.companyName);
				rep.setUserName(p.personName);
				for (int i = 1; i <= maxQCLevels; i++) {
					int totalAttributes = p.totalAttributes.get(i-1);
					int changedAttributes = p.changedAttributes.get(i-1);
					rep.setTotalAttributes(i, totalAttributes);
					rep.setChangedAttributes(i, changedAttributes);
					float attrPerFloat = 0f;
					if (totalAttributes > 0)
						attrPerFloat = (float) (100 * changedAttributes / totalAttributes);
					DecimalFormat decimalFormat = new DecimalFormat("#.00");
					String numberAsString = decimalFormat.format(attrPerFloat);
					String correctPercentage = numberAsString + "%";
					rep.setPercent(i, correctPercentage);
					rep.setAttributeChangedByNextQC(i, p.attributeChangedByNextQC.get(i-1));
				}
				if (maxQCLevels < 3) {
					for (int i = maxQCLevels+1; i <= 3; i++) {
						rep.setTotalAttributes(i, 0);
						rep.setChangedAttributes(i, 0);
						rep.setPercent(i, "0 %");
						rep.setAttributeChangedByNextQC(i, 0);
					}
				}
				rep.setChangedAttributeList(p.changedAttributeList.toString());
				rep.setTransactionId(p.transactionIds.toString());
				rep.setChangedByNextQCAttributeList(p.changedByNextQCAttributeList.toString());	
				list.add(rep);
			}
		} catch (Exception e) {
			logger.error("Error in mongoReadVerifiedTransactionsUser", e);
		} finally {
			mongo.close();
		}
		return list;
	}
}
