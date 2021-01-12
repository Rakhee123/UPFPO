package com.brightleaf.mongoservice.serviceimpl;

import static com.brightleaf.mongoservice.jwt.Constants.IGNORERESULT;
import static com.brightleaf.mongoservice.jwt.Constants.INITIALVALUE;
import static com.brightleaf.mongoservice.jwt.Constants.NEWVALUE;
import static com.brightleaf.mongoservice.jwt.Constants.QC;
import static com.brightleaf.mongoservice.jwt.Constants.QCDONEBY;
import static com.brightleaf.mongoservice.jwt.Constants.QCLEVEL;
import static com.brightleaf.mongoservice.jwt.Constants.STATUS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brightleaf.mongoservice.model.ApplicationExtractedValue;
import com.brightleaf.mongoservice.model.Attributes;
import com.brightleaf.mongoservice.model.ExtractedEntity;
import com.brightleaf.mongoservice.model.QCValidation;
import com.brightleaf.mongoservice.service.ReadMongoService;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class ReadMongoServiceImpl implements ReadMongoService {

	protected final static Logger logger = Logger.getLogger(ReadMongoServiceImpl.class);

	
	
	private static final String URL_WEB_SERVICE ="http://localhost:8082/DocumentTypeService/";
	private static final String PARAMETERS = "parameters";
	private static final String GET_DOC_ATTR = "getDocAndAttribute";
	private static final String RULELIST = "rulelist";
	private static final String DOC_AND_ATTR = "docAndAttribute";
	@Autowired
	private RestTemplate restTemplate;


	@Override
	public List<ExtractedEntity> getMongoData(String companyName, String transactionId, Integer qcLevel,HttpServletRequest request) {
		MongoClient mongo = null;
		List<ExtractedEntity> list = new ArrayList<ExtractedEntity>();
		List<ExtractedEntity> summaryData = new ArrayList<ExtractedEntity>();
		try {
			mongo = new MongoClient("localhost", 27017);
			MongoDatabase db = mongo.getDatabase(companyName);
			MongoCollection<Document> collection = db.getCollection(transactionId);
			List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());
			for (Document d1 : documents) {
				ExtractedEntity e = new ExtractedEntity();
				e.setDocumentName(d1.getString("DocumentName"));
				e.setCompanyId(d1.getInteger("CompanyId"));
				e.setQcStatus(d1.getString(QC + qcLevel + STATUS));
				List<Document> attributesDocs = (List<Document>) d1.get("Attributes");
				List<Attributes> listOfAttributes = new ArrayList<Attributes>();
				for (Document att : attributesDocs) {
					String customDefaultValue="";
					Attributes attributes = new Attributes();
					attributes.setAttributeName(att.getString("AttributeName"));
					
					String authorizationHeaderValue = request.getHeader("Authorization");
					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", authorizationHeaderValue);
					HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
					ResponseEntity<JSONObject> resEntity = this.restTemplate
							.exchange(URL_WEB_SERVICE + "getCustemizedList/"+att.getString("AttributeName"), HttpMethod.GET, entity, JSONObject.class);
					JSONObject customObj=resEntity.getBody();
					List listCustom=new ArrayList();
					listCustom.add(resEntity.getBody());
					attributes.setCustomList(listCustom);
					org.json.JSONObject object=new org.json.JSONObject(customObj);
					org.json.JSONArray cutArrayList=null;
					if(object.has("customizeList"))
					{
						cutArrayList=object.getJSONArray("customizeList");
					
					}
					attributes.setAppliedRule(att.getInteger("AppliedRule"));
					attributes.setExtractedSentence(att.getString("ExtractedSentence"));
					attributes.setExtractedChunk(att.getString("ExtractedChunk"));
					int pageNumber = att.getInteger("PageNumber");
					attributes.setPageNumber(pageNumber);

					Document applicationExtractedValuesDocs = (Document) att.get("ApplicationExtractedValue");
					Document QCDocuments = (Document) att.get(QC + qcLevel);
					// If QC0
					if (QCDocuments == null) {
						if (qcLevel == 1) {
							ApplicationExtractedValue applicationExtractedValues = new ApplicationExtractedValue();
							applicationExtractedValues
									.setInitialValue(applicationExtractedValuesDocs.getString(INITIALVALUE));
							attributes.setApplicationExtractedValue(applicationExtractedValues);
							if(object.has("customizeList"))
							{
								customDefaultValue=setCustomDefaultValue(cutArrayList,applicationExtractedValuesDocs.getString(INITIALVALUE));
								if(customDefaultValue==null) {
									attributes.setCustomDefaultValue("-");
								}else {
									attributes.setCustomDefaultValue(customDefaultValue);
								}
							}
						} else {
							Document QCDocuments1 = (Document) att.get(QC + (qcLevel - 1));
							if (QCDocuments1 != null) {
								QCValidation qcValidation1 = new QCValidation();
								qcValidation1.setNewValue(QCDocuments1.getString(NEWVALUE));
								qcValidation1.setInitialValue(QCDocuments1.getString(NEWVALUE));// QCDocuments1.getString(INITIALVALUE));
								qcValidation1.setValueaddedBy(QCDocuments1.getString(QCDONEBY));
								qcValidation1.setQcLevel(QCDocuments1.getInteger(QCLEVEL));
//								qcValidation1.setValueStatus(QCDocuments1.getString(STATUS));
								qcValidation1.setIgnoreResult(QCDocuments1.getString(IGNORERESULT));
								qcValidation1.setStatus("UNVERIFIED");

								attributes.setQcValidation(qcValidation1);
								if(object.has("customizeList"))
								{
									customDefaultValue=setCustomDefaultValue(cutArrayList,QCDocuments1.getString(NEWVALUE));
									if(customDefaultValue==null) {
										attributes.setCustomDefaultValue("-");
									}else {
										attributes.setCustomDefaultValue(customDefaultValue);
									}
								}
							}
						}
					} else if (QCDocuments != null) {
						switch (qcLevel) {
						case 1:
							Document QCDocuments1 = (Document) att.get(QC + qcLevel);
							QCValidation qcValidation1 = new QCValidation();

							qcValidation1.setNewValue(QCDocuments1.getString(NEWVALUE));
							qcValidation1.setInitialValue(QCDocuments1.getString(INITIALVALUE));
							qcValidation1.setValueaddedBy(QCDocuments1.getString(QCDONEBY));
							qcValidation1.setQcLevel(QCDocuments1.getInteger(QCLEVEL));
							qcValidation1.setStatus(QCDocuments1.getString(STATUS));
							qcValidation1.setIgnoreResult(QCDocuments1.getString(IGNORERESULT));
							attributes.setQcValidation(qcValidation1);
							if(object.has("customizeList"))
							{
								customDefaultValue=setCustomDefaultValue(cutArrayList,QCDocuments1.getString(NEWVALUE));
								if(customDefaultValue==null) {
									attributes.setCustomDefaultValue("-");
								}else {
									attributes.setCustomDefaultValue(customDefaultValue);
								}
							}
							break;
						case 2:
							Document QCDocuments2 = (Document) att.get(QC + qcLevel);
							QCValidation qcValidation2 = new QCValidation();

							qcValidation2.setNewValue(QCDocuments2.getString(NEWVALUE));
							qcValidation2.setInitialValue(QCDocuments2.getString(INITIALVALUE));
							qcValidation2.setValueaddedBy(QCDocuments2.getString(QCDONEBY));
							qcValidation2.setQcLevel(QCDocuments2.getInteger(QCLEVEL));
							qcValidation2.setStatus(QCDocuments2.getString(STATUS));
							qcValidation2.setIgnoreResult(QCDocuments2.getString(IGNORERESULT));

							attributes.setQcValidation(qcValidation2);
							if(object.has("customizeList"))
							{
								customDefaultValue=setCustomDefaultValue(cutArrayList,QCDocuments2.getString(NEWVALUE));
								if(customDefaultValue==null) {
									attributes.setCustomDefaultValue("-");
								}else {
									attributes.setCustomDefaultValue(customDefaultValue);
								}
							}
							break;
						case 3:
							Document QCDocuments3 = (Document) att.get(QC + qcLevel);
							QCValidation qcValidation3 = new QCValidation();
							qcValidation3.setNewValue(QCDocuments3.getString(NEWVALUE));
							qcValidation3.setInitialValue(QCDocuments3.getString(INITIALVALUE));
							qcValidation3.setValueaddedBy(QCDocuments3.getString(QCDONEBY));
							qcValidation3.setQcLevel(QCDocuments3.getInteger(QCLEVEL));
							qcValidation3.setStatus(QCDocuments3.getString(STATUS));
							qcValidation3.setIgnoreResult(QCDocuments3.getString(IGNORERESULT));

							attributes.setQcValidation(qcValidation3);
							if(object.has("customizeList"))
							{
								customDefaultValue=setCustomDefaultValue(cutArrayList,QCDocuments3.getString(NEWVALUE));
								if(customDefaultValue==null) {
									attributes.setCustomDefaultValue("-");
								}else {
									attributes.setCustomDefaultValue(customDefaultValue);
								}
							}
							break;
						case 4:
							Document QCDocuments4 = (Document) att.get(QC + qcLevel);
							QCValidation qcValidation4 = new QCValidation();
							qcValidation4.setNewValue(QCDocuments4.getString(NEWVALUE));
							qcValidation4.setInitialValue(QCDocuments4.getString(INITIALVALUE));
							qcValidation4.setValueaddedBy(QCDocuments4.getString(QCDONEBY));
							qcValidation4.setQcLevel(QCDocuments4.getInteger(QCLEVEL));
							qcValidation4.setStatus(QCDocuments4.getString(STATUS));
							qcValidation4.setIgnoreResult(QCDocuments4.getString(IGNORERESULT));

							attributes.setQcValidation(qcValidation4);
							if(object.has("customizeList"))
							{
								customDefaultValue=setCustomDefaultValue(cutArrayList,QCDocuments4.getString(NEWVALUE));
								if(customDefaultValue==null) {
									attributes.setCustomDefaultValue("-");
								}else {
									attributes.setCustomDefaultValue(customDefaultValue);
								}
							}
							break;
						case 5:
							Document QCDocuments5 = (Document) att.get(QC + qcLevel);
							QCValidation qcValidation5 = new QCValidation();
							qcValidation5.setNewValue(QCDocuments5.getString(NEWVALUE));
							qcValidation5.setInitialValue(QCDocuments5.getString(INITIALVALUE));
							qcValidation5.setValueaddedBy(QCDocuments5.getString(QCDONEBY));
							qcValidation5.setQcLevel(QCDocuments5.getInteger(QCLEVEL));
							qcValidation5.setStatus(QCDocuments5.getString(STATUS));
							qcValidation5.setIgnoreResult(QCDocuments5.getString(IGNORERESULT));
							attributes.setQcValidation(qcValidation5);
							if(object.has("customizeList"))
							{
								customDefaultValue=setCustomDefaultValue(cutArrayList,QCDocuments5.getString(NEWVALUE));
								if(customDefaultValue==null) {
									attributes.setCustomDefaultValue("-");
								}else {
									attributes.setCustomDefaultValue(customDefaultValue);
								}
							}
							break;

						}
					}
					listOfAttributes.add(attributes);
				}
				e.setAttributes(listOfAttributes);
				list.add(e);
			}

			summaryData = sortDocumentName(list);
		} catch (Exception e) {
			logger.error("Error in getMongoData", e);
			e.printStackTrace();
		} finally {
			mongo.close();
		}
		return summaryData;
	}

	private List<ExtractedEntity> sortDocumentName(List<ExtractedEntity> documentList) {
		for (int i = 0; i < documentList.size(); i++) {
			for (int j = i + 1; j < documentList.size(); j++) {
				String s1 = documentList.get(i).getDocumentName();
				String s2 = documentList.get(j).getDocumentName();
				s1 = s1.toLowerCase();
				s2 = s2.toLowerCase();
				if (compare(s1, s2) > 0) {
					ExtractedEntity temp = documentList.get(i);
					documentList.set(i, documentList.get(j));
					documentList.set(j, temp);
				}
			}
		}
		return documentList;
	}

	public static int compare(String o1, String o2) {
		Pattern NUMBERS = Pattern.compile("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		if (o1 == null || o2 == null) {
			return o1 == null ? o2 == null ? 0 : -1 : 1;
		}
		String[] split1 = NUMBERS.split(o1);
		String[] split2 = NUMBERS.split(o2);
		for (int i = 0; i < Math.min(split1.length, split2.length); i++) {
			char c1 = split1[i].charAt(0);
			char c2 = split2[i].charAt(0);
			int cmp = 0;

			if (Character.isDigit(c1) && Character.isDigit(c2)) {
				cmp = new BigInteger(split1[i]).compareTo(new BigInteger(split2[i]));
			}
			if (cmp == 0) {
				cmp = split1[i].compareTo(split2[i]);
			}

			if (cmp != 0) {
				return cmp;
			}
		}
		return split1.length - split2.length;
	}

	@Override
	public String getTransactionVerifyStatus(String companyName, String transactionId, Integer qCLevel) {
		MongoClient mongo = null;
		String status = "UNVERIFIED";
		try {
			mongo = new MongoClient("localhost", 27017);
			MongoDatabase db = mongo.getDatabase(companyName);
			MongoCollection<Document> collection = db.getCollection(transactionId);
			List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());
			for (Document d1 : documents) {
				System.err.println(d1.getString(QC + qCLevel + STATUS));
				if (d1.getString(QC + qCLevel + STATUS).equals("UNVERIFIED")) {
					status = "UNVERIFIED";
					break;
				} else {
					status = "VERIFIED";
				}

			}
		} catch (Exception e) {
			logger.error("Error in getTransactionVerifyStatus", e);
		}
		return status;
	}
	
	public String setCustomDefaultValue(org.json.JSONArray cutArrayList, String intialValue)
	{
		for(int i=0;i<cutArrayList.length();i++)
		{
			org.json.JSONObject obj=(org.json.JSONObject) cutArrayList.get(i);
			if(obj.get("value").equals(intialValue)) {
				return intialValue;
			}		
		}
		return null;
	}
}