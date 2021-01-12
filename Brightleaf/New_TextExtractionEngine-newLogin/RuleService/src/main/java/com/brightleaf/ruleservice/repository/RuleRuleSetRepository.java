package com.brightleaf.ruleservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightleaf.ruleservice.model.RuleRuleSet;

public interface RuleRuleSetRepository extends JpaRepository<RuleRuleSet, Integer> {

	@Query(name = "FIND_RULERULESET_BY_RULE_ID")
	RuleRuleSet getRuleFromRuleSet(@Param("ruleId") Integer ruleId, @Param("ruleSetId") Integer ruleSetId);

	@Query(name = "FIND_RULERULESET_BY_RULESET_ID")
	List<RuleRuleSet> getRuleRuleSetListById(@Param("ruleSetId") Integer ruleSetId);

	@Query(name = "FIND_RULELIST_BY_RULESET_ID_DOC_ID")
	List<RuleRuleSet> getRuleListById(@Param("ruleSetId") Integer ruleSetId,
			@Param("documentTypeId") Integer documentTypeId);

	@Query(name = "FIND_RULERULESET_BY_RULEID")
	List<RuleRuleSet> getRuleRuleSetByRuleId(@Param("ruleId") Integer ruleId);

}
