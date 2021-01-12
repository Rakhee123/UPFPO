package com.brightleaf.ruleservice.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.ruleservice.model.RuleSet;
import com.brightleaf.ruleservice.repository.RuleSetRepository;
import com.brightleaf.ruleservice.service.RuleSetService;

@Service
public class RuleSetServiceImpl implements RuleSetService{
	
	@Autowired 
	RuleSetRepository ruleSetRepository;
	
	@Override
	public RuleSet addRuleSet(RuleSet ruleSet) {
		return ruleSetRepository.save(ruleSet);
	}

	@Override
	public List<RuleSet> getRuleSetList() {
		return ruleSetRepository.findAll();
	}

	@Override
	public RuleSet getRuleSet(Integer ruleSetId) {
		return ruleSetRepository.getRuleSet(ruleSetId);
	}

	@Override
	public void deleteRuleSet(RuleSet ruleSet) {
		ruleSetRepository.delete(ruleSet);
		
	}
	
	@Override
	public Boolean isRuleSetExists(String ruleSetName) {
		boolean flag = false;
		RuleSet findRuleSet = ruleSetRepository.getRuleSetByName(ruleSetName);
		return findRuleSet != null ? ruleSetName.equals(findRuleSet.getRuleSetName()) : flag;
	}

}
