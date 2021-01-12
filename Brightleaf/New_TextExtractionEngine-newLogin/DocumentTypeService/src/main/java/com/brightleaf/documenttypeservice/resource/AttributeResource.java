package com.brightleaf.documenttypeservice.resource;

import static com.brightleaf.documenttypeservice.jwt.Constants.HEADER_STRING;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.brightleaf.documenttypeservice.model.Attribute;
import com.brightleaf.documenttypeservice.model.AttributeDto;
import com.brightleaf.documenttypeservice.model.CustomizeList;
import com.brightleaf.documenttypeservice.service.AttributeService;
import com.brightleaf.documenttypeservice.service.CustomizeListService;

@CrossOrigin(origins = "*")
@RestController
public class AttributeResource {

	@Autowired
	private AttributeService attrService;

	@Autowired
	CustomizeListService customizeListService;

	@Autowired
	private RestTemplate restTemplate;

	// CREATE Attribute
	@PostMapping(value = "/addAttribute")
	@ResponseBody
	public Attribute addAttribute(@RequestBody AttributeDto attributeDto) {
		String attName = attributeDto.getAttributeName();
		if (attrService.isAttrExists(attName)) {
			return null;
		} else {
			Attribute attribute = attrService.convertAttributeFromAttributeDto(attributeDto);
			return attrService.addAttribute(attribute);
		}
	}

	// GET Attribute type
	@GetMapping("/getAttributeList")
	public List<Attribute> getAttributeList() {
		return attrService.getAttributeList();
	}

	// update Attribute
	@PutMapping(value = "/updateAttribute/{attributeId}")
	public Attribute updateAttribute(@RequestBody AttributeDto attributeDto,
			@PathVariable("attributeId") Integer attributeId) {
		Attribute toBeUpdated = attrService.getAttribute(attributeId);
		String attName = attributeDto.getAttributeName();
		if (attrService.isAttrExists(attName)) {
			//if attribute name already exists don't let it update
			//check if same attribute we are updating
			if(attrService.updatingSameRecord(attributeId, attName)) {
				Attribute attribute = attrService.convertAttributeFromAttributeDtoEdit(toBeUpdated, attributeDto);
				return attrService.addAttribute(attribute);
			}
			return null;
		}else {
			//if attribute name doesn't exists than update
		Attribute attribute = attrService.convertAttributeFromAttributeDtoEdit(toBeUpdated, attributeDto);
		return attrService.addAttribute(attribute);
		}
	}

	// DELETE Attribute
	@DeleteMapping(value = "/deleteAttribute/{attributeId}")
	public Attribute deleteAttribute(@PathVariable("attributeId") final Integer attributeId,
			HttpServletRequest request) {
		// hitting RuleService to check if any rule is associated with attribute
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
//		String urlWebService = "http://localhost:8004/";
		String urlWebService = "http://localhost:8004/RuleService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		ResponseEntity<JSONObject> resEntity = this.restTemplate.exchange(
				urlWebService + "getRulesByAttribute" + "/" + attributeId, HttpMethod.GET, entity, JSONObject.class);
		JSONObject ruleList = resEntity.getBody();
		List<String> listCustom = new ArrayList<>();
		listCustom = (List<String>) ruleList.get("rulelist");
		if (listCustom.isEmpty()) {
			Attribute attribute = attrService.getAttribute(attributeId);
			attrService.deleteAttribute(attribute);
			return attribute;
		} else {
			return null;
		}
	}
	
	public Attribute deleteAttribute(@PathVariable("attributeId") final Integer attributeId) {
		Attribute attribute = attrService.getAttribute(attributeId);
		attrService.deleteAttribute(attribute);
		if(attribute.getAttributeType().equals("Customized Attribute")) {
			customizeListService.deleteCustomizeListByAttributeId(attribute.getAttributeId());
		}
		return attribute;

	}

	@GetMapping("/getAttribute/{attributeId}")
	public JSONObject getAttribute(@PathVariable("attributeId") Integer attributeId) {
		Attribute att = attrService.getAttribute(attributeId);
		JSONObject obj = new JSONObject();
		obj.put("attributeId", att.getAttributeId());
		obj.put("attributeName", att.getAttributeName());
		obj.put("attributeType", att.getAttributeType());
		obj.put("fallbackValue", att.getFallbackValue());
		obj.put("paragraph", att.getParagraph());
		return obj;
	}

	@GetMapping("/getCustemizedList/{attributeIName}")
	public JSONObject getMessage(@PathVariable("attributeIName") String attributeName) {
		JSONObject obj = new JSONObject();
		Attribute attribute = attrService.getAttributeByname(attributeName);
		if (attribute != null) {
			if (attribute.getAttributeType().equals("Customized Attribute")) {
				List<CustomizeList> customList = customizeListService
						.getCustomizeListByAttributeId(attribute.getAttributeId());

				JSONArray arr = new JSONArray();
				for (int i = 0; i < customList.size(); i++) {
					CustomizeList cl = customList.get(i);
					arr.add(cl);
				}
				obj.put("customizeList", arr);
			}
		}
		return obj;
	}
	
	
	@GetMapping("/getAttributeByName/{attributeName}")
	Attribute getAttributeByName(@PathVariable("attributeName") String attributeName){
		return attrService.getAttributeByname(attributeName);
	}
}
