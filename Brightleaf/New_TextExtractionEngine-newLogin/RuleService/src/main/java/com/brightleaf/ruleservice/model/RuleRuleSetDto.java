package com.brightleaf.ruleservice.model;

import java.io.Serializable;
import java.util.List;

public class RuleRuleSetDto implements Serializable  {
	
	private static final long serialVersionUID = 1L;

	private Integer ruleRuleSetId;
	private Integer ruleId;
	private Integer ruleSetId;
	private Integer rulePriority;
	private List<RuleRuleSet> ruleRuleSetList;
    private Rule rule;
    private RuleSet ruleSet;
    
    
	public Integer getRuleRuleSetId() {
		return ruleRuleSetId;
	}

	public void setRuleRuleSetId(Integer ruleRuleSetId) {
		this.ruleRuleSetId = ruleRuleSetId;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(Integer ruleSetId) {
		this.ruleSetId = ruleSetId;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}

	public void setRuleSet(RuleSet ruleSet) {
		this.ruleSet = ruleSet;
	}

	public List<RuleRuleSet> getRuleRuleSetList() {
		return ruleRuleSetList;
	}

	public void setRuleRuleSetList(List<RuleRuleSet> ruleRuleSetList) {
		this.ruleRuleSetList = ruleRuleSetList;
	}
	
	public Integer getRulePriority() {
		return rulePriority;
	}

	public void setRulePriority(Integer rulePriority) {
		this.rulePriority = rulePriority;
	}
}
