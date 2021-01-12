package com.brightleaf.ruleservice.model;

import java.util.List;

public class CustomizeListFinder {
	
	public boolean findFromCustomizeList(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction, List<CustomizeList>customList) {
		boolean success = false;
		String defaultValue = "";
		int attributeId = params.getRule().getAttributeId();
		for (CustomizeList c1 : customList) {
			if (c1.getAttributeId() == attributeId) {
				if (c1.getDefaultValue())
					defaultValue = c1.getValue();
				if (params.getChunk().toLowerCase().contains(c1.getName().toLowerCase())) {
					ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
					e.setCompanyId(params.getCompanyId());
					e.setDocumentName(params.getDocumentName());
					e.setExtractedAttributeId(params.getRule().getAttributeId());
					e.setExtractedSentence(params.getSentence());
					e.setExtractedChunk(params.getChunk());
					e.setExtractedEntity(c1.getValue());
					e.setAppliedRule(params.getRule().getRuleId());
					e.setPageNumber(params.getPageNumber());
					listExtraction.add(e);
					success = true;
					break;
				}
			}
		}
		if (!success) {
			ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
			e.setCompanyId(params.getCompanyId());
			e.setDocumentName(params.getDocumentName());
			e.setExtractedAttributeId(params.getRule().getAttributeId());
			e.setExtractedSentence(params.getSentence());
			e.setExtractedChunk(params.getChunk());
			e.setExtractedEntity(defaultValue);
			e.setAppliedRule(params.getRule().getRuleId());
			e.setPageNumber(params.getPageNumber());
			listExtraction.add(e);
		}
		
		return true;
	}

}
