package com.brightleaf.exportservice.resource;

import static com.brightleaf.exportservice.jwt.Constants.HEADER_STRING;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brightleaf.exportservice.service.CreateCompanyWiseReportService;
import com.brightleaf.exportservice.service.CreateExcelConsolidatedFullService;
import com.brightleaf.exportservice.service.CreateExcelConsolidatedService;
import com.brightleaf.exportservice.service.CreateExcelDocumentWiseService;

@CrossOrigin(origins = "*")
@RestController
public class ExportResource {
	private static Logger logger = Logger.getLogger(ExportResource.class);

//	private static final String URL_WEB = "http://localhost:8098/";
	private static final String URL_WEB = "http://localhost:8098/MongoService/";
	private static final String URL_WEB_REPORT = "http://localhost:8086/ReportService/";
	private static final String PARAMETERS = "parameters";
	private static final String GETMONGODATA = "getMongoData";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private CreateExcelDocumentWiseService createExcelDocumentWiseService;

	@Autowired
	private CreateExcelConsolidatedService createExcelConsolidatedService;

	@Autowired
	private CreateExcelConsolidatedFullService createExcelConsolidatedFullService;

	@Autowired
	private CreateCompanyWiseReportService createCompanyWiseReportService;

	@GetMapping(path = "/exportDocumentWise/{companyName}/{transactionId}/{QCLevel}", produces = "application/octet-stream")
	public Resource exportDcumentWise(HttpServletResponse response, @PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, HttpServletRequest request,
			@PathVariable("QCLevel") Integer qcLevel) {
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> entity11 = this.restTemplate.exchange(
				URL_WEB + "getMongoData" + "/" + companyName + "/" + transactionId + "/" + qcLevel, HttpMethod.GET,
				entity, String.class);
		Resource excelCreated = createExcelDocumentWiseService.exportTransactionDocumentwiseToExcel(response,
				entity11.getBody(), transactionId, qcLevel);
		return excelCreated;
	}

	@GetMapping(path = "/exportConsolidated/{companyName}/{transactionId}/{QCLevel}", produces = "application/octet-stream")
	public Resource exportConsolidatede(HttpServletResponse response, @PathVariable("companyName") String companyName,
			@PathVariable("transactionId") String transactionId, HttpServletRequest request,
			@PathVariable("QCLevel") Integer qcLevel) {
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> entity11 = this.restTemplate.exchange(
				URL_WEB + "getMongoData" + "/" + companyName + "/" + transactionId + "/" + qcLevel, HttpMethod.GET,
				entity, String.class);
		Resource excelCreated = createExcelConsolidatedService.exportTransactionConsolidatedToExcel(response,
				entity11.getBody(), transactionId, qcLevel);
		return excelCreated;
	}

	@GetMapping(path = "/exportConsolidatedFullInformation/{companyName}/{transactionId}/{QCLevel}", produces = "application/octet-stream")
	public Resource exportConsolidatedFullInformation(HttpServletResponse response,
			@PathVariable("companyName") String companyName, @PathVariable("transactionId") String transactionId,
			HttpServletRequest request, @PathVariable("QCLevel") Integer qcLevel) {
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> entity11 = this.restTemplate.exchange(
				URL_WEB + "getMongoData" + "/" + companyName + "/" + transactionId + "/" + qcLevel, HttpMethod.GET,
				entity, String.class);
		Resource excelCreated = createExcelConsolidatedFullService.exportTransactionFullConsolidatedToExcel(response,
				entity11.getBody(), transactionId, qcLevel);
		return excelCreated;
	}

	@GetMapping(path = "/exportExtractionCompanyWise", produces = "application/octet-stream")
	public Resource exportCompanyWise(HttpServletResponse response, HttpServletRequest request) {
		logger.info("In exportCompanyWise");
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> entity11 = this.restTemplate.exchange(URL_WEB + "getCompanyWiseExtractionData",
				HttpMethod.GET, entity, String.class);
		Resource excelCompany = createCompanyWiseReportService.exportCreateCompanyWiseReportToExcel(response,
				entity11.getBody());
		return excelCompany;
	}

	@GetMapping(path = "/exportExtractionUserWise", produces = "application/octet-stream")
	public Resource exportUsertWise(HttpServletResponse response, HttpServletRequest request) {
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> entity11 = this.restTemplate.exchange(URL_WEB + "getUserWiseExtractionData",
				HttpMethod.GET, entity, String.class);
		//entity11.getBody();
		Resource excelCompany = createCompanyWiseReportService.exportCreateUserWiseReportToExcel(response,
				entity11.getBody());
		return excelCompany;
	}
}
