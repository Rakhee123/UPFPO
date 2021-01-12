package com.brightleaf.reportservice.resource;

import static com.brightleaf.reportservice.constants.Constants.HEADER_STRING;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brightleaf.reportservice.service.ReportExtractService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

@CrossOrigin(origins = "*")
@RestController
public class ReportResource {
	private static Logger logger = Logger.getLogger(ReportResource.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ReportExtractService reportExtractService;

//	GET COMPANY WISE EXTRACTION FROM MONGO
	@GetMapping("/getCompanyWiseExtractionData")
	public String extractStatistics(HttpServletRequest request) throws JsonProcessingException {
		logger.info("In getCompanyWiseExtractionData");
		JSONArray msgToReturn = new JSONArray();
		String userLoggedIn = request.getParameter("userName");
		//logger.info("userLoggedIn = " + userLoggedIn);
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
//			String urlWebService1 = "http://localhost:8081/";
		String urlWebService1 = "http://localhost:8081/UserCompanyService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		//headers.set("userName", userLoggedIn);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		//ResponseEntity<JSONArray> resEntity = this.restTemplate.exchange(urlWebService1 + "companyNameList" + "/",
		//		HttpMethod.GET, entity, JSONArray.class);
		ResponseEntity<String> resEntity = this.restTemplate.exchange(urlWebService1 + "companyNameList" + "/",
				HttpMethod.GET, entity, String.class);
		logger.info("Resentity companyList " + resEntity.getBody());
		//JSONObject obj1 = new JSONObject(resEntity.getBody());
		JSONArray arr = new JSONArray(resEntity.getBody());//obj1.get("myArrayList");
		for (int i = 0; i < arr.length(); i++) {
			if (arr.get(i) != null) {
				JSONObject obj = reportExtractService.mongoReadVerifiedTransactions(arr.get(i).toString());
				if (!obj.isEmpty())
					msgToReturn.put(obj);
			}
		}
		String s = msgToReturn.toString();
		logger.info("GSON.getjson = " + s);
		//return new Gson().toJson(msgToReturn);
		return s;
	}

//	GET COMPANY WISE EXTRACTION FROM MONGO
	@GetMapping("/getUserWiseExtractionData")
	public String extractStatisticsUser(HttpServletRequest request) throws JsonProcessingException {
		JSONArray msgToReturn = new JSONArray();
		String userLoggedIn = request.getParameter("userName");
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
//			String urlWebService1 = "http://localhost:8081/";
		String urlWebService1 = "http://localhost:8081/UserCompanyService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		headers.set("userName", userLoggedIn);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> resEntity = this.restTemplate.exchange(urlWebService1 + "companyNameList" + "/",
				HttpMethod.GET, entity, String.class);
		JSONArray arr = new JSONArray(resEntity.getBody());

		for (int i = 0; i < arr.length(); i++) {
			if (arr.get(i) != null) {
				JSONArray array = reportExtractService.mongoReadVerifiedTransactionsUser(arr.get(i).toString());
				Iterator iter = array.iterator();
				while (iter.hasNext()) {
					msgToReturn.put(iter.next());
				}
				//msgToReturn.add(reportExtractService.mongoReadVerifiedTransactionsUser(dsfsadf.get(i).toString()));
			}
		}
		String s = msgToReturn.toString();
		logger.info("get userwise data = " + s);
		//return new Gson().toJson(msgToReturn);
		return s;
	}
}