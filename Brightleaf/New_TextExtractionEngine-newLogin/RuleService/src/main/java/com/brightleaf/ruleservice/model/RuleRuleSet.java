package com.brightleaf.ruleservice.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity 
@NamedQuery(name = "FIND_RULERULESET_BY_RULESET_ID", query = "SELECT ruleruleset FROM RuleRuleSet ruleruleset WHERE ruleruleset.ruleSetId = :ruleSetId")
@NamedQuery(name = "FIND_RULERULESET_BY_RULE_ID", query = "SELECT ruleruleset FROM RuleRuleSet ruleruleset WHERE ruleruleset.ruleId = :ruleId AND ruleruleset.ruleSetId = :ruleSetId")
@NamedQuery(name = "FIND_RULELIST_BY_RULESET_ID_DOC_ID", query = "SELECT ruleruleset FROM RuleRuleSet ruleruleset WHERE ruleruleset.ruleSetId = :ruleSetId AND ruleruleset.rule.documentTypeId = :documentTypeId")
@NamedQuery(name = "FIND_RULERULESET_BY_RULEID", query = "SELECT ruleruleset FROM RuleRuleSet ruleruleset WHERE ruleruleset.ruleId = :ruleId")

@Table(name = "rule_ruleset", catalog = "textextractionengine")
public class RuleRuleSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "rule_ruleset_id")
	private Integer ruleRuleSetId;
	
	@Column(name = "rule_id")
	private Integer ruleId;
	
	@Column(name = "rule_set_id")
	private Integer ruleSetId;
	
	@Column(name = "rule_priority")
	private Integer rulePriority;
	
	@Transient
	private List<RuleRuleSet> ruleRuleSetList;
	
    @ManyToOne(optional = false)
	@JoinColumn(name = "rule_id", nullable = false, insertable=false, updatable=false)
    private Rule rule;

    @ManyToOne(optional = false)
 	@JoinColumn(name = "rule_set_id", nullable = false, insertable=false, updatable=false)
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
