package com.brightleaf.exportservice.model;

import java.util.List;

public class ExtractedConsolidatedEntity {
	
	private List<AttributeConsolidated> listOfAttribute;
	private String documentName;
	
	public List<AttributeConsolidated> getListOfAttribute() {
		return listOfAttribute;
	}
	public void setListOfAttribute(List<AttributeConsolidated> listOfAttribute) {
		this.listOfAttribute = listOfAttribute;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
}