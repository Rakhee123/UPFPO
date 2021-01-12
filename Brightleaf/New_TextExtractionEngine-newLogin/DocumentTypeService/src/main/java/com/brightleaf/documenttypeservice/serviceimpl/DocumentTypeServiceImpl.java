package com.brightleaf.documenttypeservice.serviceimpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.documenttypeservice.model.Attribute;
import com.brightleaf.documenttypeservice.model.DocumentType;
import com.brightleaf.documenttypeservice.model.DocumentTypeDto;
import com.brightleaf.documenttypeservice.repository.DocumentTypeRepository;
import com.brightleaf.documenttypeservice.service.DocumentTypeService;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

	@Autowired
	DocumentTypeRepository docTypeRepository;

	@Override
	public DocumentType addDocumentType(DocumentType docType) {
		return docTypeRepository.save(docType);

	}

	@Override
	public List<DocumentType> getDocumentTypeList() {

		return docTypeRepository.findAll();
	}

	@Override
	public DocumentType getDocumentType(Integer documentTypeId) {
		return docTypeRepository.findDocTypeById(documentTypeId);
	}

	@Override
	public void deleteDocumentType(DocumentType docType) {
		docTypeRepository.delete(docType);

	}

	@Override
	public DocumentType convertDocumentFromDocumentDto(final DocumentTypeDto documentTypeDto) {
		DocumentType documentType = new DocumentType();
		documentType.setDocumentTypeId(documentTypeDto.getDocumentTypeId());
		documentType.setDocumentDesc(documentTypeDto.getDocumentDesc());
		documentType.setDocumentName(documentTypeDto.getDocumentName());
		documentType.setCreatedBy(documentTypeDto.getCreatedBy());
		documentType.setCreationDate(new Date());
		documentType.setLastModifiedBy(documentTypeDto.getCreatedBy());
		documentType.setLastModifiedDate(new Date());

		return documentType;
	}
	
	@Override
	public DocumentType convertEditDocumentFromDocumentDto(final DocumentType docTobeUpdated, final DocumentTypeDto documentTypeDto) {

		docTobeUpdated.setDocumentTypeId(documentTypeDto.getDocumentTypeId());
		docTobeUpdated.setDocumentDesc(documentTypeDto.getDocumentDesc());
		docTobeUpdated.setDocumentName(documentTypeDto.getDocumentName());
		docTobeUpdated.setCreatedBy(docTobeUpdated.getCreatedBy());
		docTobeUpdated.setCreationDate(docTobeUpdated.getCreationDate());
		docTobeUpdated.setLastModifiedBy(documentTypeDto.getCreatedBy());
		docTobeUpdated.setLastModifiedDate(new Date());

		return docTobeUpdated;
	}

	@Override
	public Boolean isDocExists(String docName) {
		boolean flag = false;
		DocumentType findDoc = docTypeRepository.findDocTypeByName(docName);
		return findDoc != null ? docName.toLowerCase().equals(findDoc.getDocumentName().toLowerCase()) : flag;
	}

	@Override
	public Boolean updatingSameRecord(Integer documentTypeId, String docName) {
		boolean flag = false;
		DocumentType findDoc = docTypeRepository.findDocTypeByName(docName);
		if(findDoc.getDocumentTypeId().equals(documentTypeId)) {
			return true;
		}
		return false;
	}
}
