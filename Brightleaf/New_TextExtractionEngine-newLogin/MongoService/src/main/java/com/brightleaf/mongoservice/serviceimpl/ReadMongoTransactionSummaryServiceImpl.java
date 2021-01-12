package com.brightleaf.mongoservice.serviceimpl;

import static com.brightleaf.mongoservice.jwt.Constants.DOCUMENT_NAME;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.brightleaf.mongoservice.model.Attributes;
import com.brightleaf.mongoservice.model.ExtractedEntity;
import com.brightleaf.mongoservice.service.ReadMongoTransactionSummaryService;

@Service
public class ReadMongoTransactionSummaryServiceImpl implements ReadMongoTransactionSummaryService {
	protected final static Logger logger = Logger.getLogger(ReadMongoTransactionSummaryServiceImpl.class);
	
	@Override
	public JSONObject getTransactionSummary(String companyName, String transactionId, Integer qcLevel, JSONArray documentArray,HttpServletRequest request) {

		JSONObject resultJson = new JSONObject();
		
		// make documentList from document Array
		List<String> documentList = new ArrayList<>();
		documentList = getDocumentList(documentArray, documentList);
		
		ReadMongoServiceImpl rms = new ReadMongoServiceImpl();

		List<ExtractedEntity> dataReadByMongo = rms.getMongoData(companyName, transactionId, qcLevel,request);
		

		//Get the list of Attributes
		 List<String> finalAttributeList = getAttributeList(dataReadByMongo);
		 
		//make the result Json
		 resultJson.put("Attributes", finalAttributeList);
		 resultJson.put("Result_list", dataReadByMongo);
				
		return resultJson;
	}

	private List<String> getDocumentList(JSONArray documentArray, List<String> documentList) {
		if (documentArray != null) {
			for (int i = 0; i < documentArray.length(); i++) {
				try {
					documentList.add(documentArray.getJSONObject(i).getString(DOCUMENT_NAME));
				} catch (JSONException e) {
					logger.error("Error in getDocumentList", e);
				}
			}
		}
		return documentList;
	}

	private List<String> getAttributeList(List<ExtractedEntity> extractedConsolidatedEntityList) {
		List<String> setOfAttributeNames = new ArrayList<>();
		for (ExtractedEntity extractedEntityDocument : extractedConsolidatedEntityList) {
			List<Attributes> attributeConsolidated = extractedEntityDocument.getAttributes();
			for (Attributes sdfsf : attributeConsolidated) {
				if (!setOfAttributeNames.contains(sdfsf.getAttributeName())) {
					setOfAttributeNames.add(sdfsf.getAttributeName());
				}
			}
		}
		return setOfAttributeNames;
	}
}
