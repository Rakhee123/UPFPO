package com.brightleaf.ruleservice.resource;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.ruleservice.model.RuleSet;
import com.brightleaf.ruleservice.model.RuleSetDto;
import com.brightleaf.ruleservice.service.RuleRuleSetService;
import com.brightleaf.ruleservice.service.RuleSetService;

@CrossOrigin(origins = "*")
@RestController
public class RuleSetResource {
	
	@Autowired
	RuleSetService ruleSetService;
	
	@Autowired
	RuleRuleSetService ruleRuleSetService;
	
	// CREATE Rule Set
	@PostMapping(value = "/addRuleSet")
	public RuleSet addRuleSet(@RequestBody RuleSetDto ruleSetDto)
	{
		String ruleSetName = ruleSetDto.getRuleSetName();
		if(ruleSetService.isRuleSetExists(ruleSetName)) {
			return null;
		} else {
			RuleSet ruleSet = convertRuleSetFromRuleSetDto(ruleSetDto);
			ruleSet.setCreationDate(new Date());
			ruleSet.setLastModifiedDate(new Date());
			ruleSet.setLastModifiedBy(ruleSetDto.getCreatedBy());
			return ruleSetService.addRuleSet(ruleSet);	
		}
	}
	
	// GET RuleSet List
	@GetMapping("/getRuleSetList")
	public List<RuleSet> getRuleSetList()
	{
		return ruleSetService.getRuleSetList();		
	}
	
	//update ruleset data 
	@PutMapping(value = "/updateRuleSet/{ruleSetId}")
	public RuleSet updateRuleSet(@RequestBody RuleSetDto ruleSetDto, @PathVariable("ruleSetId") final Integer ruleSetId)
	{
		String ruleSetName = ruleSetDto.getRuleSetName();
		if(ruleSetService.isRuleSetExists(ruleSetName)) {
			return null;
		}else {
		RuleSet ruleSet=convertRuleSetFromRuleSetDto(ruleSetDto);
		ruleSet.setRuleSetId(ruleSetId);
		ruleSet.setLastModifiedDate(new Date());
		ruleSet.setCreationDate(ruleSet.getCreationDate());
		ruleSet.setCreatedBy(ruleSet.getCreatedBy());
		ruleSet.setLastModifiedBy(ruleSetDto.getLastModifiedBy());
		return ruleSetService.addRuleSet(ruleSet);
		}
	}
	
	// DELETE RULESET
	@DeleteMapping(value = "/deleteRuleSet/{ruleSetId}")
	public RuleSet deleteRuleSet(@PathVariable("ruleSetId") final Integer ruleSetId)
	{
		RuleSet ruleSet=ruleSetService.getRuleSet(ruleSetId);
		if(!ruleRuleSetService.getRuleRuleSetListById(ruleSetId).isEmpty())
		{
			 ruleSet=null;
		}
		if(ruleRuleSetService.getRuleRuleSetListById(ruleSetId).isEmpty()) {
		
		ruleSetService.deleteRuleSet(ruleSet);
		
		}
		return ruleSet;
	}
	
	
	public RuleSet convertRuleSetFromRuleSetDto(final RuleSetDto ruleSetDto)
	{
		RuleSet ruleSet=new RuleSet();
		ruleSet.setRuleSetId(ruleSetDto.getRuleSetId());
		ruleSet.setRuleSetName(ruleSetDto.getRuleSetName());
		ruleSet.setCreatedBy(ruleSetDto.getCreatedBy());
		ruleSet.setCreationDate(ruleSetDto.getCreationDate());
		ruleSet.setLastModifiedBy(ruleSetDto.getLastModifiedBy());
		ruleSet.setLastModifiedDate(ruleSetDto.getLastModifiedDate());
		
		return ruleSet;
	}

}
