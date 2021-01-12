package com.brightleaf.ruleservice.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.brightleaf.ruleservice.model.RuleRuleSet;

public interface RuleRuleSetService {

	RuleRuleSet addRuleRuleSet(RuleRuleSet ruleRuleSet);

	List<RuleRuleSet> getRuleRuleSetList();

	RuleRuleSet getRuleFromRuleSet(Integer ruleId, Integer ruleSetId);

	void deleteRuleFromRuleSet(RuleRuleSet ruleRuleSet);

	List<RuleRuleSet> getRuleRuleSetListById(Integer ruleSetId);

	RuleRuleSet addRuleRuleSetList(RuleRuleSet ruleRuleSet);

	List<RuleRuleSet> getRuleListById(Integer ruleSetId, Integer documentTypeId); 
	
	List<RuleRuleSet> getRuleRuleSetByRuleId(Integer ruleSetId);

	RuleRuleSet addRuleRuleSetId(JSONArray jsonArray, Integer ruleSetId);

	void deleteRulesFromRuleSetById(ArrayList<Integer> values);



}
