package com.brightleaf.ruleservice.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.model.RuleDto;
import com.brightleaf.ruleservice.repository.RuleRepository;
import com.brightleaf.ruleservice.repository.RuleSetRepository;
import com.brightleaf.ruleservice.service.RuleService;

@Service
public class RuleServiceImpl implements RuleService{
	
	@Autowired 
	RuleRepository ruleRepository;
	
	@Autowired 
	RuleSetRepository ruleSetRepository;
	
	@Override
	public Rule addRule(Rule rule) {
		return ruleRepository.save(rule);
	}

	@Override
	public List<Rule> getRuleList() {
		return ruleRepository.findAll();
	}

	@Override
	public Rule getRule(Integer ruleId) {
		return ruleRepository.getRule(ruleId);
	}

	@Override
	public void deleteRule(Rule rule) {
		ruleRepository.delete(rule);
		
	}

	@Override
	public List<Rule> getRulesByAttributeId(Integer attributeId) {
		return ruleRepository.getRulesByAttributeId(attributeId);
	}
	
	@Override
	public List<Rule> getRulesByDocumentTypeId(Integer documentTypeId) {
		return ruleRepository.getRulesByDocumentTypeId(documentTypeId);
	}

	@Override
	public Rule getRulesByDocumentTypeIdRuleId(Integer ruleId, Integer documentTypeId) {
		return ruleRepository.getRulesByDocumentTypeIdRuleId(ruleId, documentTypeId);
	}

	@Override
	public List<Rule> getRuleByAttributeAndDocId(Integer documentTypeId,Integer attributeId)
	{
		return ruleRepository.getRuleByAttributeAndDocId(documentTypeId,attributeId);
	}
	
	@Override
	public Rule convertRuleFromRuleDto(final RuleDto ruleDto) {
		Rule rule = new Rule();
		rule.setRuleId(ruleDto.getRuleId());
		rule.setDocumentTypeId(ruleDto.getDocumentTypeId());
		rule.setAttributeId(ruleDto.getAttributeId());

		rule.setTextBefore1(ruleDto.getTextBefore1());
		rule.setOpBefore1(ruleDto.getOpBefore1());
		rule.setTextBefore2(ruleDto.getTextBefore2());
		rule.setOpBefore2(ruleDto.getOpBefore2());
		rule.setTextBefore3(ruleDto.getTextBefore3());
		rule.setOpBefore3(ruleDto.getOpBefore3());
		rule.setTextBefore4(ruleDto.getTextBefore4());
		rule.setOpBefore4(ruleDto.getOpBefore4());
		rule.setTextBefore5(ruleDto.getTextBefore5());

		rule.setTextAfter1(ruleDto.getTextAfter1());
		rule.setOpAfter1(ruleDto.getOpAfter1());
		rule.setTextAfter2(ruleDto.getTextAfter2());
		rule.setOpAfter2(ruleDto.getOpAfter2());
		rule.setTextAfter3(ruleDto.getTextAfter3());
		rule.setOpAfter3(ruleDto.getOpAfter3());
		rule.setTextAfter4(ruleDto.getTextAfter4());
		rule.setOpAfter4(ruleDto.getOpAfter4());
		rule.setTextAfter5(ruleDto.getTextAfter5());

		rule.setCreatedBy(ruleDto.getCreatedBy());
		rule.setCreationDate(ruleDto.getCreationDate());
		rule.setLastModifiedBy(ruleDto.getLastModifiedBy());
		rule.setLastModifiedDate(ruleDto.getLastModifiedDate());
		rule.setIgnoreCase(ruleDto.getIgnoreCase());
		rule.setMerge(ruleDto.getMerge());
		rule.setRegex(ruleDto.getRegex());
		rule.setSearchword(ruleDto.getSearchword());
		rule.setFound(ruleDto.getFound());
		rule.setNotfound(ruleDto.getNotfound());
		return rule;
	}
}
