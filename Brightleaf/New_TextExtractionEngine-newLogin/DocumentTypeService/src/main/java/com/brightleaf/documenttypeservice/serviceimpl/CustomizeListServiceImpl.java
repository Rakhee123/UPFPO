package com.brightleaf.documenttypeservice.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.documenttypeservice.model.CustomizeList;
import com.brightleaf.documenttypeservice.repository.CustomizeListRepository;
import com.brightleaf.documenttypeservice.service.CustomizeListService;

@Service
public class CustomizeListServiceImpl implements CustomizeListService {

	@Autowired
	CustomizeListRepository customizeListRepository;
	
	@Override
	public List<CustomizeList> getCustomizeListByAttributeId(Integer attributeId) {
		return customizeListRepository.getCustomizeListByAttributeId(attributeId);
	}

	@Override
	public CustomizeList addCustomizeList(CustomizeList custList) {
		return customizeListRepository.save(custList);
	}

	@Override
	public void deleteCustomizeListById(Integer customizeListId) {
		CustomizeList c = customizeListRepository.getCustomizeListById(customizeListId);
		customizeListRepository.delete(c);
	}

	@Override
	public void deleteCustomizeListByAttributeId(Integer attributeId) {
		List<CustomizeList> list=customizeListRepository.getCustomizeListByAttributeId(attributeId);
		for(CustomizeList object:list) {
			customizeListRepository.delete(object);
		}
		
	}
}
