package com.brightleaf.mongoservice.resource;

import static com.brightleaf.mongoservice.jwt.Constants.DOCUMENT_LIST;
import static com.brightleaf.mongoservice.jwt.Constants.HEADER_STRING;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brightleaf.mongoservice.model.ExtractedEntity;
import com.brightleaf.mongoservice.model.ReportExtractCompanyWise;
import com.brightleaf.mongoservice.model.ReportExtractUserWise;
import com.brightleaf.mongoservice.service.DeleteTransactionService;
import com.brightleaf.mongoservice.service.IgnoreResultService;
import com.brightleaf.mongoservice.service.ReadMongoService;
import com.brightleaf.mongoservice.service.ReadMongoTransactionSummaryService;
import com.brightleaf.mongoservice.service.ReportExtractService;
import com.brightleaf.mongoservice.service.WriteAttributeService;
import com.brightleaf.mongoservice.service.WriteMongoRecordsService;
import com.brightleaf.mongoservice.service.WriteQCLevelService;
import com.brightleaf.mongoservice.service.WriteVerifyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

@CrossOrigin(origins = "*")
@RestController
public class MongoReadWriteResource {
	private static Logger logger = Logger.getLogger(MongoReadWriteResource.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ReadMongoService readMongoService;

	@Autowired
	private ReadMongoTransactionSummaryService readMongoTransactionSummaryService;

	@Autowired
	private WriteMongoRecordsService writeMongoRecordsService;

	@Autowired
	private WriteVerifyService writeVerifyService;

	@Autowired
	private WriteQCLevelService writeQCLevelService;

	@Autowired
	private WriteAttributeService writeAttributeService;

	@Autowired
	private IgnoreResultService ignoreResultService;

	@Autowired
	private DeleteTransactionService deleteTransactionService;
	
	@Autowired
	private ReportExtractService reportExtractService;

	@GetMapping("/getMongoData/{companyName}/{transactionId}/{QCLevel}")
	public String getMongoData(@PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, @PathVariable("QCLevel") Integer qcLevel,HttpServletRequest request) {
		List<ExtractedEntity> getExcelData = readMongoService.getMongoData(companyName, transactionId, qcLevel,request);
		return new Gson().toJson(getExcelData);
	}

	@PostMapping(path = "/postMongoDataInMongo/{companyName}/{transactionId}/{qcLevels}/{ruleSetId}", consumes = "application/json", produces = "application/json")
	public String postMongoDataInMongo(@PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, @PathVariable("qcLevels") String qcLevels,
			@PathVariable("ruleSetId") Integer ruleSetId,
			@RequestBody List<ExtractedEntity> extractedEntity, HttpServletRequest request) {
		return writeMongoRecordsService.mongoAddRecords(companyName, extractedEntity, transactionId, qcLevels, ruleSetId, request);
	}

	@PostMapping(path = "/postQCDataInMongo/{companyName}/{transactionId}", consumes = "application/json", produces = "application/json")
	public String postQCDataInMongo(@PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, @RequestBody String asd) {
		JSONObject jsonObject = new JSONObject(asd);
		return writeQCLevelService.mongoUpdateQARecords(companyName, transactionId, jsonObject);
	}

	@PostMapping(path = "/getSummaryForTransaction/{companyName}/{transactionId}/{QCLevel}", consumes = "application/json", produces = "application/json")
	public String getSummaryForTransaction(@PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, @PathVariable("QCLevel") Integer qcLevel,
			@RequestBody String transactionJson,HttpServletRequest request) {
		JSONArray documentArray = null;
		documentArray = new JSONObject(transactionJson).getJSONArray(DOCUMENT_LIST);
		JSONObject summaryData = readMongoTransactionSummaryService.getTransactionSummary(companyName, transactionId,
				qcLevel, documentArray,request);
		return new Gson().toJson(summaryData);
	}

	@PostMapping(path = "/verifyAttribute", consumes = "application/json", produces = "application/json")
	public Boolean verifyAttribute(@RequestBody String asd) {
		JSONObject jsonObject = new JSONObject(asd);
		return writeVerifyService.mongoUpdateVerifyRecords(jsonObject);
	}

	@PostMapping(path = "/verifyTransaction/{companyName}/{transactionId}/{QCLevel}/{qcDoneBy}")
	public Boolean verifyTransaction(@PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, @PathVariable("QCLevel") Integer qcLevel,
			@PathVariable("qcDoneBy") String qcDoneBy) {
		return writeVerifyService.verifyTransaction(companyName, transactionId, qcLevel, qcDoneBy);
	}

	@PostMapping(path = "/verifyDocument")
	public Boolean verifyDocument(@RequestBody String contentCame) {
		JSONObject content = new JSONObject(contentCame);
		return writeVerifyService.mongoUpdateVerifyDocument(content);
	}

	@PostMapping(path = "/postAttributeInMongo/{companyName}/{transactionId}/{qcLevel}", consumes = "application/json", produces = "application/json")
	public String postAttributeInMongo(@PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, @PathVariable("qcLevel") Integer qcLevel,
			@RequestBody String asd) {
		System.err.println(asd);
		JSONObject jsonObject = new JSONObject(asd);
		String postData = writeAttributeService.mongoUpdateAttribute(companyName, transactionId, qcLevel, jsonObject);
		return postData;
	}

	@GetMapping("/getTransactionVerifyStatus/{transactionId}/{QCLevel}/{companyName}")
	public String getTransactionVerifyStatus(@PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, @PathVariable("QCLevel") Integer qcLevel) {
		String status = readMongoService.getTransactionVerifyStatus(companyName, transactionId, qcLevel);
		return status;
	}

	@PostMapping(path = "/ignoreResult/{companyName}", consumes = "application/json", produces = "application/json")
	public String ignoreResult(@PathVariable("companyName") String companyName, @RequestBody String content) {
		System.err.println(content);
		JSONObject jsonObject = new JSONObject(content);
		return ignoreResultService.ignoreResult(companyName, jsonObject);
	}

	@DeleteMapping("/deleteTransaction")
	public String deleteTransaction(@RequestBody String content) {
		JSONObject jsonObject = new JSONObject(content);
		return new Gson().toJson(deleteTransactionService.deleteTransaction(jsonObject));
	}

	@DeleteMapping("/deleteTransactionsCompanywise")
	public void deleteTransactionsCompanywise(HttpServletRequest request, @RequestBody String content) {
		JSONObject jsonObject = new JSONObject(content);
		
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		String urlWebService1 = "http://localhost:8081/UserCompanyService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		String companyName = jsonObject.getString("companyName");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<Integer> resEntity = this.restTemplate.exchange(urlWebService1 + "companyName/"+ companyName, HttpMethod.GET, entity, Integer.class);
		Integer companyId = resEntity.getBody();
		deleteTransactionService.deleteTransactionsCompanywise(jsonObject, companyId);
	}

	@DeleteMapping("/deleteTransactionsDatewise")
	public void deleteTransactionsDatewise(@RequestBody String content) {
		JSONObject jsonObject = new JSONObject(content);
		deleteTransactionService.deleteTransactionsDatewise(jsonObject);
	}

	
	@PostMapping(path = "/addValue/{companyName}", consumes = "application/json", produces = "application/json")
	public String addValue(@PathVariable("companyName") String companyName,
			@RequestBody String content) {
		System.err.println("addValue>>  "+content);
		JSONObject jsonObject = new JSONObject(content);
		String postData = writeAttributeService.addValueForAttribute(jsonObject,companyName);
		return postData;
	}
	
	@PostMapping(path = "/changeCustemValue/{companyName}/{transactionId}")
	public String changeCustemValue(@RequestBody String content,@PathVariable("companyName") String companyName,@PathVariable("transactionId") String transactionId) {
		JSONObject object = new JSONObject(content);
		return writeQCLevelService.changeCustemValue(companyName, transactionId,object);
	}
	
	@GetMapping("/getCompanyWiseExtractionData")
	public String getCompanyWiseExtractionData(HttpServletRequest request) throws JsonProcessingException {
		logger.info("In getCompanyWiseExtractionData");
		List<ReportExtractCompanyWise> msgToReturn = new ArrayList<>();
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
				ReportExtractCompanyWise rep = reportExtractService.mongoReadVerifiedTransactions(arr.get(i).toString());
				String cName = rep.getCompanyName();
				if (cName != null && !cName.contentEquals(""))
					msgToReturn.add(rep);
			}
		}
		String s = new Gson().toJson(msgToReturn);
		logger.info("GSON.getjson = " + s);
		//return new Gson().toJson(msgToReturn);
		return s;
	}
	
	@GetMapping("/getUserWiseExtractionData")
	public String getUserWiseExtractionData(HttpServletRequest request) throws JsonProcessingException {
		List <ReportExtractUserWise> msgToReturn = new ArrayList<>();
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
				List <ReportExtractUserWise> lst = reportExtractService.mongoReadVerifiedTransactionsUser(arr.get(i).toString());
				for (ReportExtractUserWise rep: lst)  {
					msgToReturn.add(rep);
				}
				//msgToReturn.add(reportExtractService.mongoReadVerifiedTransactionsUser(dsfsadf.get(i).toString()));
			}
		}
		String s = new Gson().toJson(msgToReturn);
		logger.info("get userwise data = " + s);
		//return new Gson().toJson(msgToReturn);
		return s;
	}
	
}
