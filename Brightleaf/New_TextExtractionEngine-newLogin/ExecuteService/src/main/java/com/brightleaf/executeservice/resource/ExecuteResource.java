package com.brightleaf.executeservice.resource;

import static com.brightleaf.executeservice.jwt.Constants.HEADER_STRING;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.brightleaf.executeservice.service.ExecuteService;
import com.brightleaf.executeservice.service.TempToSourceService;
import com.google.gson.Gson;

@CrossOrigin(origins = "*")
@RestController
public class ExecuteResource {

	protected final static Logger logger = Logger.getLogger(ExecuteResource.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ExecuteService executeService;

	@Autowired
	TempToSourceService tempToSourceService;

	int documentTypeId = 0;

	int ruleSetId = 0;

	@PostMapping("/execute")
	public String execute(@RequestParam("file") List<MultipartFile> file,
			HttpServletRequest request) {

		String message = "";
		List<MultipartFile> filesUploaded = new ArrayList<>();
		int companyId = Integer.parseInt(request.getParameter("companyId"));
		documentTypeId = Integer.parseInt(request.getParameter("documetTypeId"));
		ruleSetId = Integer.parseInt(request.getParameter("ruleSetId"));
		String identifier = request.getParameter("transIdentifier");

		try {

			String transactionId = executeService.generateTransactionId(companyId, identifier);
			logger.info(transactionId);
			filesUploaded.addAll(file);
			message = executeService.uploadFilesFromLocal(filesUploaded, transactionId);
			List<String> docList = tempToSourceService.moveTempToSource(transactionId, companyId);

			String userLoggedIn = request.getParameter("userName");
			String authorizationHeaderValue = request.getHeader(HEADER_STRING);
//			String urlWebService = "http://localhost:8004/";
     		String urlWebService = "http://localhost:8004/RuleService/";
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_STRING, authorizationHeaderValue);
			headers.set("userName", userLoggedIn);
			HttpEntity<?> entity = new HttpEntity<>(docList, headers);
			Date dtStart = new Date();
			logger.info("execute resource"+urlWebService + "extractRule" + "/" + companyId + "/" + ruleSetId + "/" + documentTypeId + "/" + transactionId);
			ResponseEntity<String> ruleRead = this.restTemplate.exchange(
					urlWebService + "extractRule" + "/" + companyId + "/" + ruleSetId + "/" + documentTypeId + "/" + transactionId,
					HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
					});
			Date dtEnd = new Date();
			long diffSeconds = (dtEnd.getTime()  - dtStart.getTime()) / 1000 % 60;
			return new Gson().toJson(message + " " + ruleRead + " " + diffSeconds);
		} catch (Exception e) {
			logger.error("Error in execute", e);
			message = "Fail to upload Profile Picture" + filesUploaded + "!";
			return new Gson().toJson(message);
		}

	}
}