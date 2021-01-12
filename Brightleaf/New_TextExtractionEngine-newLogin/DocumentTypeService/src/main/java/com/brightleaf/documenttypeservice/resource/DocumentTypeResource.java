package com.brightleaf.documenttypeservice.resource;

import static com.brightleaf.documenttypeservice.jwt.Constants.HEADER_STRING;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.brightleaf.documenttypeservice.model.DocumentType;
import com.brightleaf.documenttypeservice.model.DocumentTypeDto;
import com.brightleaf.documenttypeservice.service.DocumentTypeService;

@CrossOrigin(origins = "*")
@RestController
public class DocumentTypeResource {

	@Autowired
	private DocumentTypeService docTypeService;

	@Autowired
	private RestTemplate restTemplate;

	// CREATE Document
	@PostMapping(value = "/addDocumentType")
	@ResponseBody
	public DocumentType addDocumentType(@RequestBody DocumentTypeDto documentTypeDto) {
		String docName = documentTypeDto.getDocumentName();
		if (docTypeService.isDocExists(docName)) {
			return null;
		} else {
			DocumentType docType = docTypeService.convertDocumentFromDocumentDto(documentTypeDto);
			return docTypeService.addDocumentType(docType);
		}
	}

	// GET All Documents type
	@GetMapping("/getDocumentTypeList")
	public List<DocumentType> getDocumentTypeList() {
		try {
			return docTypeService.getDocumentTypeList();
		} catch (Exception e) {
			return null;
		}
	}

	@PutMapping(value = "/updateDocumentType/{documentTypeId}")
	public DocumentType updateDocumentType(@RequestBody DocumentTypeDto documentTypeDto,
			@PathVariable("documentTypeId") Integer documentTypeId) {
		DocumentType docTobeUpdated = docTypeService.getDocumentType(documentTypeId);
		String docName = documentTypeDto.getDocumentName();
		if (docTypeService.isDocExists(docName)) {
			
			if(docTypeService.updatingSameRecord(documentTypeId, docName)) {
			DocumentType docType = docTypeService.convertEditDocumentFromDocumentDto(docTobeUpdated, documentTypeDto);
			return docTypeService.addDocumentType(docType);
			}
			return null;
			
		}else {
		DocumentType docType = docTypeService.convertEditDocumentFromDocumentDto(docTobeUpdated, documentTypeDto);
		return docTypeService.addDocumentType(docType);
		}
	}

	@DeleteMapping(value = "/deleteDocumentType/{documentTypeId}")
	public DocumentType deleteDocumentType(@PathVariable("documentTypeId") final Integer documentTypeId,
			HttpServletRequest request) {
		// hitting RuleService to check if any rule is associated with attribute
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
//		String urlWebService = "http://localhost:8004/";
		String urlWebService = "http://localhost:8004/RuleService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		ResponseEntity<JSONObject> resEntity = this.restTemplate.exchange(
				urlWebService + "getRuleByDocId" + "/" + documentTypeId, HttpMethod.GET, entity, JSONObject.class);
		JSONObject docList = resEntity.getBody();
		List<String> listRule = new ArrayList<>();
		listRule = (List<String>) docList.get("rulelist");
		if (listRule.isEmpty()) {
			DocumentType docType = docTypeService.getDocumentType(documentTypeId);
			docTypeService.deleteDocumentType(docType);
			return docType;
		} else {
			return null;
		}
	}

	// GET RULE SET NAME BY DOCUMENT TYPE ID
	@GetMapping(value = "/getRuleSetByDocumentId/{documentTypeId}")
	public String getRuleSetByDocumentId(@PathVariable("documentTypeId") Integer documentTypeId,
			HttpServletRequest request) {

		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
//		String urlWebService = "http://localhost:8004/";
		String urlWebService = "http://localhost:8004/RuleService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		ResponseEntity<String> resEntity = this.restTemplate
				.exchange(urlWebService + "getRuleById" + "/" + documentTypeId, HttpMethod.GET, entity, String.class);
		return resEntity.getBody();
	}
}
