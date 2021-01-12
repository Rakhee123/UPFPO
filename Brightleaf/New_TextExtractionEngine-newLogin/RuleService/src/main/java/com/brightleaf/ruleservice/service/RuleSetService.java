package com.brightleaf.ruleservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.ruleservice.model.RuleSet;

@Component
public interface RuleSetService {

	public RuleSet addRuleSet(RuleSet ruleSet);

	public List<RuleSet> getRuleSetList();

	public RuleSet getRuleSet(Integer ruleSetId);

	public void deleteRuleSet(RuleSet ruleSet);
	
	public Boolean isRuleSetExists(String ruleSetName);
}
