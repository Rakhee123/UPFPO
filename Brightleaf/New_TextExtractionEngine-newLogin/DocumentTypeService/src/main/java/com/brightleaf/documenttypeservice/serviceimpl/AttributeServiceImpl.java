package com.brightleaf.documenttypeservice.serviceimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.documenttypeservice.model.Attribute;
import com.brightleaf.documenttypeservice.model.AttributeDto;
import com.brightleaf.documenttypeservice.repository.AttributeRepository;
import com.brightleaf.documenttypeservice.service.AttributeService;

@Service
public class AttributeServiceImpl implements AttributeService {
	@Autowired
	AttributeRepository attrRepository;

	@Override
	public Attribute addAttribute(Attribute attribute) {
		return attrRepository.save(attribute);
	}

	@Override
	public List<Attribute> getAttributeList() {

		return attrRepository.findAll();
	}

	@Override
	public Attribute getAttribute(Integer attributeId) {
		return attrRepository.getAttribute(attributeId);
	}

	@Override
	public void deleteAttribute(Attribute attribute) {
		attrRepository.delete(attribute);

	}

	@Override
	public Attribute getAttributeByname(String attributeIName) {
		return attrRepository.getAttributeByname(attributeIName);

	}

	@Override
	public Attribute convertAttributeFromAttributeDto(final AttributeDto attributeDto) {
		Attribute attribute = new Attribute();
		attribute.setAttributeId(attributeDto.getAttributeId());
		attribute.setAttributeName(attributeDto.getAttributeName());
		attribute.setAttributeDesc(attributeDto.getAttributeDesc());
		attribute.setAttributeType(attributeDto.getAttributeType());
		attribute.setFallbackValue(attributeDto.getFallbackValue());
		attribute.setCreatedBy(attributeDto.getCreatedBy());
		attribute.setCreationDate(new Date());
		attribute.setLastModifiedBy(attributeDto.getCreatedBy());
		attribute.setLastModifiedDate(new Date());
		attribute.setParagraph(attributeDto.getParagraph());

		return attribute;
	}
	
	@Override
	public Attribute convertAttributeFromAttributeDtoEdit(final Attribute attribute, final AttributeDto attributeDto) {
		attribute.setAttributeId(attributeDto.getAttributeId());
		attribute.setAttributeName(attributeDto.getAttributeName());
		attribute.setAttributeDesc(attributeDto.getAttributeDesc());
		attribute.setAttributeType(attributeDto.getAttributeType());
		attribute.setFallbackValue(attributeDto.getFallbackValue());
		attribute.setCreatedBy(attribute.getCreatedBy());
		attribute.setCreationDate(attribute.getCreationDate());
		attribute.setLastModifiedBy(attributeDto.getCreatedBy());
		attribute.setLastModifiedDate(new Date());
		attribute.setParagraph(attributeDto.getParagraph());

		return attribute;
	}

	@Override
	public Boolean isAttrExists(String attributeName) {
		boolean flag = false;
		Attribute findAttr = attrRepository.getAttributeByname(attributeName);
		return findAttr != null ? attributeName.toLowerCase().equals(findAttr.getAttributeName().toLowerCase()) : flag;
	}

	@Override
	public Boolean updatingSameRecord(Integer attributeId, String attName) {
		boolean flag = false;
		Attribute findAttr = attrRepository.getAttributeByname(attName);
		if(findAttr.getAttributeId().equals(attributeId)) {
			return true;
		}
		return false;
	}
}
