package com.brightleaf.ruleservice.resource;

import static com.brightleaf.ruleservice.jwt.Constants.HEADER_STRING;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
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

import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.model.RuleDto;
import com.brightleaf.ruleservice.model.RuleRuleSet;
import com.brightleaf.ruleservice.model.RuleSet;
import com.brightleaf.ruleservice.service.RuleRuleSetService;
import com.brightleaf.ruleservice.service.RuleService;
import com.brightleaf.ruleservice.service.RuleSetService;

@CrossOrigin(origins = "*")
@RestController
public class RuleResource {

	protected final static Logger logger = Logger.getLogger(RuleResource.class);
	@Autowired
	private RuleService ruleService;

	@Autowired
	private RuleSetService ruleSetService;

	@Autowired
	private RuleRuleSetService ruleRuleSetService;

	@Autowired
	private RestTemplate restTemplate;
//	private static final String URL_WEB_SERVICE = "http://localhost:8082/";
	private static final String URL_WEB_SERVICE = "http://localhost:8082/DocumentTypeService/";
	private static final String PARAMETERS = "parameters";
	private static final String GET_DOC_ATTR = "getDocAndAttribute";
	private static final String RULELIST = "rulelist";
	private static final String DOC_AND_ATTR = "docAndAttribute";

	// CREATE Rule
	@PostMapping(value = "/addRule/{ruleSetId}")
	@ResponseBody
	public Rule addRule(@RequestBody RuleDto ruleDto, @PathVariable("ruleSetId") Integer ruleSetId) {
		Rule rule = ruleService.convertRuleFromRuleDto(ruleDto);
		rule.setCreationDate(new Date());
		Rule ruleObject = ruleService.addRule(rule);
		if (ruleObject != null) {
			RuleRuleSet ruleRuleSet = new RuleRuleSet();
			ruleRuleSet.setRuleSetId(ruleSetId);
			ruleRuleSet.setRuleId(ruleObject.getRuleId());
			ruleRuleSetService.addRuleRuleSet(ruleRuleSet);
		}
		return ruleObject;
	}

	@PostMapping(value = "/addRuleToRule/")
	@ResponseBody
	public Rule addRuleToRule(@RequestBody RuleDto ruleDto) {
		if(ruleDto.getRuleId() == null) {
			Rule rule = ruleService.convertRuleFromRuleDto(ruleDto);
			rule.setCreationDate(new Date());
			rule.setLastModifiedDate(new Date());
			rule.setLastModifiedBy(ruleDto.getCreatedBy());
			return ruleService.addRule(rule);
		} else {
			Rule rule = ruleService.convertRuleFromRuleDto(ruleDto);
			rule.setCreationDate(rule.getCreationDate());
			rule.setLastModifiedDate(new Date());
			rule.setLastModifiedBy(ruleDto.getCreatedBy());
			return ruleService.addRule(rule);
		}
	}

	// GET Rule List
	@GetMapping("/getRuleList")
	public List<Rule> getRuleList() {
		return ruleService.getRuleList();
	}

	// update Rule
	@PutMapping(value = "/updateRule/{ruleId}")
	public Rule updateRule(@RequestBody RuleDto ruleDto, @PathVariable("ruleId") Integer ruleId) {
		Rule rule = ruleService.convertRuleFromRuleDto(ruleDto);
		rule.setRuleId(ruleId);
		rule.setCreatedBy(rule.getCreatedBy());
		rule.setLastModifiedBy(ruleDto.getCreatedBy());
		rule.setLastModifiedDate(new Date());
		rule.setCreationDate(rule.getCreationDate());
		return ruleService.addRule(rule);
	}

	// DELETE RULE
	@DeleteMapping(value = "/deleteRule/{ruleId}")
	public Rule deleteRule(@PathVariable("ruleId") final Integer ruleId) {
		Rule rule = ruleService.getRule(ruleId);
		ruleService.deleteRule(rule);
		return rule;
	}

	// GET Rule List BY Rule Set Id
	@GetMapping("/getRuleListByRuleSetId/{ruleSetId}")
	public JSONObject getRuleListByRuleSetId(@PathVariable("ruleSetId") final Integer ruleSetId,
			HttpServletRequest request) {
		List<RuleRuleSet> object = ruleRuleSetService.getRuleRuleSetListById(ruleSetId);
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < object.size(); i++) {
			Rule ruleObj = object.get(i).getRule();
			JSONObject obj2 = new JSONObject();
			obj2.put("rule", ruleObj);
			String authorizationHeaderValue = request.getHeader(HEADER_STRING);
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_STRING, authorizationHeaderValue);
			HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
			ResponseEntity<JSONObject> resEntity = this.restTemplate.exchange(
					URL_WEB_SERVICE + GET_DOC_ATTR + "/" + ruleObj.getAttributeId() + "/" + ruleObj.getDocumentTypeId(),
					HttpMethod.GET, entity, JSONObject.class);
			// logger.info("message:" + resEntity.getBody());
			obj2.put(DOC_AND_ATTR, resEntity.getBody());
			arr.add(obj2);
		}
		obj.put(RULELIST, arr);
		return obj;
	}

	@GetMapping("/getRulesByAttribute/{attributeId}")
	public JSONObject getRulesByAttribute(@PathVariable("attributeId") final Integer attributeId,
			HttpServletRequest request) {
		List<Rule> object = ruleService.getRulesByAttributeId(attributeId);
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < object.size(); i++) {
			Rule ruleObj = object.get(i);
			JSONObject obj2 = new JSONObject();
			obj2.put("rule", ruleObj);
			String authorizationHeaderValue = request.getHeader(HEADER_STRING);
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_STRING, authorizationHeaderValue);
			HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
			ResponseEntity<JSONObject> resEntity = this.restTemplate.exchange(
					URL_WEB_SERVICE + GET_DOC_ATTR + "/" + ruleObj.getAttributeId() + "/" + ruleObj.getDocumentTypeId(),
					HttpMethod.GET, entity, JSONObject.class);
			// logger.info("message " + resEntity.getBody());
			obj2.put(DOC_AND_ATTR, resEntity.getBody());
			arr.add(obj2);
		}
		obj.put(RULELIST, arr);
		return obj;
	}

	// get rule by rule id
	@GetMapping("/getRuleByRuleId/{ruleId}")
	public JSONObject getRuleByRuleId(@PathVariable("ruleId") final Integer ruleId, HttpServletRequest request) {
		Rule object = ruleService.getRule(ruleId);
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		Rule ruleObj = object;
		JSONObject obj2 = new JSONObject();
		obj2.put("rule", ruleObj);
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
		ResponseEntity<JSONObject> resEntity = this.restTemplate.exchange(
				URL_WEB_SERVICE + GET_DOC_ATTR + "/" + ruleObj.getAttributeId() + "/" + ruleObj.getDocumentTypeId(),
				HttpMethod.GET, entity, JSONObject.class);
		logger.info("msg" + resEntity.getBody());
		obj2.put(DOC_AND_ATTR, resEntity.getBody());
		arr.add(obj2);
		obj.put(RULELIST, arr);
		return obj;
	}

	@GetMapping("getRuleByDocId/{documentTypeId}")
	public JSONObject getRuleByDocumentId(@PathVariable("documentTypeId") Integer documentTypeId,
			HttpServletRequest request) {

		List<Rule> object = ruleService.getRulesByDocumentTypeId(documentTypeId);
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < object.size(); i++) {
			Rule ruleObj = object.get(i);
			JSONObject obj2 = new JSONObject();
			obj2.put("rule", ruleObj);
			String authorizationHeaderValue = request.getHeader(HEADER_STRING);
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_STRING, authorizationHeaderValue);
			HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
			ResponseEntity<JSONObject> resEntity = this.restTemplate.exchange(
					URL_WEB_SERVICE + GET_DOC_ATTR + "/" + ruleObj.getAttributeId() + "/" + ruleObj.getDocumentTypeId(),
					HttpMethod.GET, entity, JSONObject.class);
			// logger.info("msgg" + resEntity.getBody());
			obj2.put(DOC_AND_ATTR, resEntity.getBody());
			arr.add(obj2);
		}
		obj.put(RULELIST, arr);
		return obj;
	}

	@GetMapping("getRuleByAttributeAndDocId/{documentTypeId}/{attributeId}")
	public JSONObject getRuleByAttributeAndDocId(@PathVariable("documentTypeId") Integer documentTypeId,
			@PathVariable("attributeId") final Integer attributeId, HttpServletRequest request) {
		List<Rule> object = ruleService.getRuleByAttributeAndDocId(documentTypeId, attributeId);
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < object.size(); i++) {
			Rule ruleObj = object.get(i);
			JSONObject obj2 = new JSONObject();
			obj2.put("rule", ruleObj);
			String authorizationHeaderValue = request.getHeader(HEADER_STRING);
			HttpHeaders headers = new HttpHeaders();
			headers.set(HEADER_STRING, authorizationHeaderValue);
			HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
			ResponseEntity<JSONObject> resEntity = this.restTemplate.exchange(
					URL_WEB_SERVICE + GET_DOC_ATTR + "/" + ruleObj.getAttributeId() + "/" + ruleObj.getDocumentTypeId(),
					HttpMethod.GET, entity, JSONObject.class);
			// logger.info("message to:" + resEntity.getBody());
			obj2.put(DOC_AND_ATTR, resEntity.getBody());
			arr.add(obj2);
		}
		obj.put(RULELIST, arr);
		return obj;
	}

	@GetMapping("getRuleListById/{ruleSetId}/{documentTypeId}")
	public List<Rule> getsdfasdfg(@PathVariable("ruleSetId") Integer ruleSetId,
			@PathVariable("documentTypeId") Integer documentTypeId) {
		RuleSet ruleSet = ruleSetService.getRuleSet(ruleSetId);
		List<RuleRuleSet> getListOfRuleRuleSet = ruleRuleSetService.getRuleRuleSetListById(ruleSet.getRuleSetId());
		List<Rule> lisOfRules = new ArrayList<>();
		for (RuleRuleSet ggg : getListOfRuleRuleSet) {
			Integer oneRule = ggg.getRuleId();
			Rule oneRuleRetrive = ruleService.getRulesByDocumentTypeIdRuleId(oneRule, documentTypeId);
			lisOfRules.add(oneRuleRetrive);
		}
		return lisOfRules;
	}

	@GetMapping("/getAttributeName/{attributeId}")
	public String getAttributeName(@PathVariable("attributeId") final Integer attributeId, HttpServletRequest request) {
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);
		ResponseEntity<String> resEntity = this.restTemplate
				.exchange(URL_WEB_SERVICE + "getAttribute" + "/" + attributeId, HttpMethod.GET, entity, String.class);
		return resEntity.getBody();
	}

	@GetMapping("getRuleById/{documentTypeId}")
	public List<RuleSet> getRuleByDocumentId(@PathVariable("documentTypeId") Integer documentTypeId) {
		int ruleSetId = 0;
		RuleSet ruleSet = null;
		List<RuleSet> rulesetName = new ArrayList<>();
		List<RuleRuleSet> ruleset = new ArrayList<>();
		List<Rule> ruleList = ruleService.getRulesByDocumentTypeId(documentTypeId);
		for (Rule list : ruleList) {
			ruleSetId = list.getRuleId();
			List<RuleRuleSet> ruleruleList = ruleRuleSetService.getRuleRuleSetByRuleId(ruleSetId);
			ruleset.addAll(ruleruleList);
		}
		for (RuleRuleSet set : ruleset) {
			ruleSet = ruleSetService.getRuleSet(set.getRuleSetId());
			rulesetName.add(ruleSet);
		}
		return rulesetName.stream().distinct().collect(Collectors.toList());
	}

	// get rule by rule id
	@GetMapping("/getRuleUsingRuleId/{ruleId}")
	public Rule getRuleUsingRuleId(@PathVariable("ruleId") final Integer ruleId, HttpServletRequest request) {
		return ruleService.getRule(ruleId);

	}
}
