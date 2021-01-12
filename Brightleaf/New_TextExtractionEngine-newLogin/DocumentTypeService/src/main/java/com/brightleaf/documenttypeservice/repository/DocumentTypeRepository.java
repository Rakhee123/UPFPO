package com.brightleaf.documenttypeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightleaf.documenttypeservice.model.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer>{
	
	@Query(name = "FIND_DOCTYPE_BY_ID")
	DocumentType findDocTypeById(@Param("documentTypeId") Integer documentTypeId);

	@Query(name = "FIND_DOCTYPE_BY_NAME")
	DocumentType findDocTypeByName(@Param("documentName") String documentName);
}
