package com.brightleaf.documenttypeservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.documenttypeservice.model.CustomizeList;

@Component
public interface CustomizeListService {
	
	public List<CustomizeList> getCustomizeListByAttributeId(Integer attributeId);

	public CustomizeList addCustomizeList(CustomizeList custList);

	public void deleteCustomizeListById(Integer customizeListId);

	public void deleteCustomizeListByAttributeId(Integer attributeId);

}
