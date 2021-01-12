package com.brightleaf.ruleservice.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.ruleservice.model.RuleRuleSet;
import com.brightleaf.ruleservice.repository.RuleRuleSetRepository;
import com.brightleaf.ruleservice.service.RuleRuleSetService;

@Service
public class RuleRuleSetServiceImpl implements RuleRuleSetService{
	
	@Autowired 
	RuleRuleSetRepository ruleRuleSetRepository;
	
	@Override
	public RuleRuleSet addRuleRuleSet(RuleRuleSet ruleRuleSet) {
		return ruleRuleSetRepository.save(ruleRuleSet);
	}

	@Override
	public List<RuleRuleSet> getRuleRuleSetList() {
		return ruleRuleSetRepository.findAll();
	}

	@Override
	public RuleRuleSet getRuleFromRuleSet(Integer ruleId,Integer ruleSetId) {
		return ruleRuleSetRepository.getRuleFromRuleSet(ruleId,ruleSetId);
	}

	@Override
	public void deleteRuleFromRuleSet(RuleRuleSet ruleRuleSet) {
		 ruleRuleSetRepository.delete(ruleRuleSet);
	}

	@Override
	public List<RuleRuleSet> getRuleRuleSetListById(Integer ruleSetId) {
		return ruleRuleSetRepository.getRuleRuleSetListById(ruleSetId);
		}

	@Override
	public RuleRuleSet addRuleRuleSetList(RuleRuleSet ruleRuleSet) {
		List<RuleRuleSet> ruleRuleSetList=ruleRuleSet.getRuleRuleSetList();
		for(RuleRuleSet object : ruleRuleSetList) {
			ruleRuleSetRepository.save(object);
		}
		return ruleRuleSet;
	}

	@Override
	public List<RuleRuleSet> getRuleListById(Integer ruleSetId, Integer documentTypeId) {
		return ruleRuleSetRepository.getRuleListById(ruleSetId, documentTypeId);
	}

	@Override
	public List<RuleRuleSet> getRuleRuleSetByRuleId(Integer ruleId) {
		
		return ruleRuleSetRepository.getRuleRuleSetByRuleId(ruleId);
		
	}

	@Override
	public RuleRuleSet addRuleRuleSetId(JSONArray jsonArray, Integer ruleSetId) {
		
		   for (int i = 0; i < jsonArray.length(); i++) {
			   org.json.JSONObject obj=(org.json.JSONObject) jsonArray.get(i);
				RuleRuleSet ruleRuleSet = new RuleRuleSet();
				ruleRuleSet.setRuleId(obj.getInt("ruleId"));
				ruleRuleSet.setRuleSetId(ruleSetId);
				ruleRuleSet.setRulePriority(obj.getInt("rulePriority"));
				 ruleRuleSetRepository.save(ruleRuleSet);
	        }
		   return null;
	}


	@Override
	public void deleteRulesFromRuleSetById(ArrayList<Integer> values) {
		for(int i=0;i<values.size();i++) {
			System.out.println(values.get(i));
		ruleRuleSetRepository.deleteById(values.get(i));
	}
	}
}