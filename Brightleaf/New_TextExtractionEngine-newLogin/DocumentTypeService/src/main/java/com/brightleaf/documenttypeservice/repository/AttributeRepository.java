package com.brightleaf.documenttypeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightleaf.documenttypeservice.model.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
	@Query(name = "FIND_ATTRIBUTE_BY_ID")
	Attribute getAttribute(@Param("attributeId") Integer attributeId);

	@Query(name = "FIND_ATTRIBUTE_BY_NAME")
	Attribute getAttributeByname(@Param("attributeName") String attributeName);

}
