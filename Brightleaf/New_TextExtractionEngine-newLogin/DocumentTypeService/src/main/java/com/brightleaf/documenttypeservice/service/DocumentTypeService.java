package com.brightleaf.documenttypeservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.documenttypeservice.model.DocumentType;
import com.brightleaf.documenttypeservice.model.DocumentTypeDto;

@Component
public interface DocumentTypeService {

	public DocumentType addDocumentType(DocumentType docType);

	public List<DocumentType> getDocumentTypeList();

	public DocumentType getDocumentType(Integer documentTypeId);

	public void deleteDocumentType(DocumentType docType);

	public DocumentType convertDocumentFromDocumentDto(final DocumentTypeDto documentTypeDto);

	public DocumentType convertEditDocumentFromDocumentDto(final DocumentType docTobeUpdated, final DocumentTypeDto documentTypeDto);
	
	Boolean isDocExists(final String docName);
	
	Boolean updatingSameRecord(final Integer documentTypeId, final String docName);

}
