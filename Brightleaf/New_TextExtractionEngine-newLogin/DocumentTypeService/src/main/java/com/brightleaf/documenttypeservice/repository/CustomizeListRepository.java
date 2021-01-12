package com.brightleaf.documenttypeservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightleaf.documenttypeservice.model.CustomizeList;

public interface CustomizeListRepository extends JpaRepository<CustomizeList, Integer> {
	
	@Query(name = "FIND_CUSTOMIZELIST_BY_ATTRIBUTE_ID")
	List<CustomizeList> getCustomizeListByAttributeId(@Param("attributeId") Integer attributeId) ;

	@Query(name = "FIND_CUSTOMIZELIST_BY_ID")
	CustomizeList getCustomizeListById(@Param("customizeListId") Integer customizeListId) ;
}
