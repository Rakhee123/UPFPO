package com.brightleaf.ruleservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightleaf.ruleservice.model.Rule;

public interface RuleRepository extends JpaRepository<Rule, Integer>{
	
	@Query(name = "FIND_RULE_BY_ID")
	Rule getRule(@Param("ruleId") Integer ruleId);
	
	@Query(name = "FIND_RULES_BY_ATTRIBUTE_ID")
	List<Rule> getRulesByAttributeId(@Param("attributeId") Integer attributeId);
	
	@Query(name = "FIND_RULES_BY_DOCUMENT_ID")
	List<Rule> getRulesByDocumentTypeId(@Param("documentTypeId") Integer documentTypeId);

	@Query(name = "FIND_RULES_BY_DOCUMENT_ID_RULE_ID")
	Rule getRulesByDocumentTypeIdRuleId(@Param("ruleId") Integer ruleId, @Param("documentTypeId") Integer documentTypeId);
	
	@Query(name = "FIND_RULES_BY_DOCUMENT_ID_ATTRIBUTE_ID")
	List<Rule> getRuleByAttributeAndDocId(@Param("documentTypeId") Integer documentTypeId, @Param("attributeId") Integer attributeId);
}
