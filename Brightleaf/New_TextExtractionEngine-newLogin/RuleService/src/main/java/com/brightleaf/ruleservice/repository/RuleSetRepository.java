package com.brightleaf.ruleservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightleaf.ruleservice.model.RuleSet;

public interface RuleSetRepository extends JpaRepository<RuleSet, Integer> {
	
	@Query(name = "FIND_RULESET_BY_ID")
	RuleSet getRuleSet(@Param("ruleSetId") Integer ruleSetId);

	@Query(name = "FIND_RULESET_BY_NAME")
	RuleSet getRuleSetByName(@Param("ruleSetName") String ruleSetName);
}
