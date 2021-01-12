package com.brightleaf.documenttypeservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.documenttypeservice.model.Attribute;
import com.brightleaf.documenttypeservice.model.AttributeDto;

@Component
public interface AttributeService {

	public Attribute addAttribute(Attribute attribute);

	public List<Attribute> getAttributeList();

	public Attribute getAttribute(Integer attributeId);

	public void deleteAttribute(Attribute attribute);

	public Attribute getAttributeByname(String attributeIName);

	public Attribute convertAttributeFromAttributeDto(final AttributeDto attributeDto);
	
	public Attribute convertAttributeFromAttributeDtoEdit(final Attribute attribute, final AttributeDto attributeDto);

	Boolean isAttrExists(final String attributeIName);
	
	Boolean updatingSameRecord(final Integer attributeId, final String attName);

}
