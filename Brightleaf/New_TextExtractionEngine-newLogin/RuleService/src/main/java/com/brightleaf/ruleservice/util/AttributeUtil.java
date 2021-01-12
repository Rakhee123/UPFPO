package com.brightleaf.ruleservice.util;

import static com.brightleaf.ruleservice.jwt.Constants.HEADER_STRING;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.brightleaf.ruleservice.model.CustomizeList;

public class AttributeUtil {

	public JSONObject getAttributeByAttributeId(Integer attribId, HttpServletRequest request) {
		
		RestTemplate restTemplate = new RestTemplate();
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
		String urlWebService = "http://localhost:8082/DocumentTypeService/";
//		String urlWebService = "http://localhost:8082/DocumentTypeService/";

		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		ResponseEntity<JSONObject> resEntity = restTemplate.exchange(urlWebService + "getAttribute" + "/" + attribId, HttpMethod.GET, entity, JSONObject.class);
		
		return resEntity.getBody();
	}

	public void getCustomList(HttpServletRequest request, Integer attributeId, List<CustomizeList> customList) {
		RestTemplate restTemplate = new RestTemplate();
		String authorizationHeaderValue = request.getHeader(HEADER_STRING);
//		String urlWebService = "http://localhost:8082/";
		String urlWebService = "http://localhost:8082/DocumentTypeService/";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_STRING, authorizationHeaderValue);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<JSONObject> resEntity = restTemplate.exchange(urlWebService + "getCustomizeList" + "/" + attributeId, HttpMethod.GET, entity, JSONObject.class);
		JSONObject lst  = resEntity.getBody();
		ArrayList<LinkedHashMap> list = (ArrayList<LinkedHashMap>) lst.get("CustomizeList");
		Iterator iter = list.iterator();
		//while (iter.hasNext()) {
		list.forEach(items -> {
			CustomizeList c = new CustomizeList();
			Set entrySet = items.entrySet();
			List<LinkedHashMap> list1 = new ArrayList(entrySet);
			for (int i = 0; i < list1.size(); i++) {
				Map.Entry m1 = (Entry) list1.get(i);
				String s1 = (String) m1.getKey();
				switch (s1) {
				case "customizeListId":
					c.setCustomizeListId((int) m1.getValue());
					break;
				case "attributeId":
					c.setAttributeId((int) m1.getValue());
					break;
				case "name":
					c.setName((String) m1.getValue());
					break;
				case "value":
					c.setValue((String) m1.getValue());
					break;
				case "defaultValue":
					c.setDefaultValue((Boolean) m1.getValue());
					break;
				}
			}
			customList.add(c);
		});
	}
}
