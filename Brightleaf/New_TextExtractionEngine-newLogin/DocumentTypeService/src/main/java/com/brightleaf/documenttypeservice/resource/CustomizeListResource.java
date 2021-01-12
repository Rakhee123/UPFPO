package com.brightleaf.documenttypeservice.resource;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.documenttypeservice.model.CustomizeList;
import com.brightleaf.documenttypeservice.model.CustomizeListDto;
import com.brightleaf.documenttypeservice.service.CustomizeListService;

@CrossOrigin(origins = "*")
@RestController
public class CustomizeListResource {
	
	@Autowired
	CustomizeListService customizeListService;
	
	// GET CustomizeList
	@GetMapping("/getCustomizeList/{attributeId}")
	public JSONObject getCustomizeList(@PathVariable("attributeId") final Integer attributeId)
	{
		List<CustomizeList> customList = customizeListService.getCustomizeListByAttributeId(attributeId);
		
		JSONObject obj = new JSONObject();

		JSONArray arr = new JSONArray();
		for (int i = 0; i < customList.size(); i++) {
			CustomizeList cl = customList.get(i);
			arr.add(cl);
		}
		obj.put("CustomizeList", arr);
		return obj;
	}
	
	// Add CustomizeList
	@PostMapping(value = "/addCustomizeList")
	@ResponseBody
	public CustomizeList addCustomizeList(@RequestBody CustomizeListDto customizeListDto) {
		CustomizeList custList = convertCustomizeListFromCustomizeListDto(customizeListDto);
		custList.setCreationDate(new Date());
		return customizeListService.addCustomizeList(custList);
	}

	// Edit CustomizeList
	@PostMapping(value = "/editCustomizeList")
	@ResponseBody
	public CustomizeList editCustomizeList(@RequestBody CustomizeListDto customizeListDto) {
		CustomizeList custList = convertCustomizeListFromCustomizeListDto(customizeListDto);
		custList.setCustomizeListId(customizeListDto.getCustomizeListId());
		custList.setModificationDate(new Date());
		return customizeListService.addCustomizeList(custList);
	}

	// delete CustomizeList
	@DeleteMapping("/deleteCustomizeList/{customizeListId}")
	public void deleteCustomizeList(@PathVariable("customizeListId") final Integer customizeListId) {
		customizeListService.deleteCustomizeListById(customizeListId);
	}
	
	
	// delete CustomizeList by arttribute id
	@DeleteMapping("/deleteCustomizeListByAttributeId/{attributeId}")
	public void deleteCustomizeListByAttributeId(@PathVariable("attributeId") final Integer attributeId) {
		customizeListService.deleteCustomizeListByAttributeId(attributeId);
	}

	private CustomizeList convertCustomizeListFromCustomizeListDto(final CustomizeListDto customizeListDto) {
		CustomizeList customizeList = new CustomizeList();

		customizeList.setAttributeId(customizeListDto.getAttributeId());
		customizeList.setDefaultValue(customizeListDto.getDefaultValue());
		customizeList.setName(customizeListDto.getName());
		customizeList.setValue(customizeListDto.getValue());

		return customizeList;
	}
}
