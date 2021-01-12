package com.brightleaf.ruleservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.model.RuleDto;

@Component
public interface RuleService {

	public Rule addRule(Rule rule);

	public List<Rule> getRuleList();

	public Rule getRule(Integer ruleId);

	public void deleteRule(Rule rule);

	public List<Rule> getRulesByAttributeId(Integer attributeId);

	public List<Rule> getRulesByDocumentTypeId(Integer documentTypeId);
	
	public Rule getRulesByDocumentTypeIdRuleId(Integer ruleSetId, Integer documentTypeId);
	
	List<Rule> getRuleByAttributeAndDocId(Integer attributeId, Integer documentTypeId);
	
	public Rule convertRuleFromRuleDto(final RuleDto ruleDto);

}
