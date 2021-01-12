package com.brightleaf.ruleservice.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.model.RuleRuleSet;
import com.brightleaf.ruleservice.model.RuleRuleSetDto;
import com.brightleaf.ruleservice.service.RuleRuleSetService;
	


@CrossOrigin(origins = "*")
@RestController
public class RuleRuleSetResource {
	
	
	
	
	@Autowired
	RuleRuleSetService ruleRuleSetService;
	
	// CREATE Rule Rule Set
	@PostMapping(value = "/addRuleRuleSet")
	@ResponseBody
	public RuleRuleSet addRuleRuleSet(@RequestBody RuleRuleSetDto ruleRuleSetDto)
	{
		RuleRuleSet ruleRuleSet=convertRuleFromRuleDto(ruleRuleSetDto);
		return ruleRuleSetService.addRuleRuleSet(ruleRuleSet);	
	}
	
	@PostMapping(value = "/addRuleRuleSetId/{ruleSetId}")
	@ResponseBody
	public RuleRuleSet addRuleRuleSetId(@PathVariable("ruleSetId")  Integer ruleSetId,@RequestBody  String mergeList)
	{
		JSONArray jsonArray=new JSONArray(mergeList);
		return ruleRuleSetService.addRuleRuleSetId(jsonArray,ruleSetId);	
	}
	
	
	// get RuleRuleSet Data List
	@GetMapping("getRuleRuleSetList")
	public List<RuleRuleSet> getRuleRuleSetList() {
		return ruleRuleSetService.getRuleRuleSetList();
	}
	
	// DELETE Rule From RuleSet
	@DeleteMapping(value = "/deleteRuleFromRuleSet/{ruleId}/{ruleSetId}")
	public RuleRuleSet deleteRuleFromRuleSet(@PathVariable("ruleId")  Integer ruleId,@PathVariable("ruleSetId")  Integer ruleSetId)
	{
		RuleRuleSet ruleRuleSet=ruleRuleSetService.getRuleFromRuleSet(ruleId,ruleSetId);
		 ruleRuleSetService.deleteRuleFromRuleSet(ruleRuleSet);
		 return ruleRuleSet;
	}
	
	@DeleteMapping(value = "/deleteRulesFromRuleSetById/{ruleSetId}")
	public RuleRuleSet deleteRulesFromRuleSetById(@PathVariable("ruleSetId")  Integer ruleSetId)
	{
		List<RuleRuleSet> object = ruleRuleSetService.getRuleRuleSetListById(ruleSetId);
		ArrayList<Integer> values = new ArrayList<Integer>();

		for (int i = 0; i < object.size(); i++) {
			Integer ruleObj  = object.get(i).getRuleRuleSetId();
			values.add(ruleObj);
			}
		System.out.println(values);
		ruleRuleSetService.deleteRulesFromRuleSetById(values);
		return null;
	}
	
	
	
	// get Rule List from RuleSet By RuleSet Id
	@GetMapping("getRuleRuleSetListById/{ruleSetId}")
	public List<RuleRuleSet> getRuleRuleSetListById(@PathVariable("ruleSetId")  Integer ruleSetId) {
		return ruleRuleSetService.getRuleRuleSetListById(ruleSetId);
	}
	
	
	@GetMapping("getRulePriorityByRuleId/{ruleId}/{ruleSetId}")
	public RuleRuleSet getRulePriorityByRuleId(@PathVariable("ruleId")  Integer ruleId,@PathVariable("ruleSetId")  Integer ruleSetId) {
		return ruleRuleSetService.getRuleFromRuleSet(ruleId,ruleSetId);
	}
	
	// CREATE Rule Rule Set List
	@PostMapping(value = "/addRuleRuleSetList")
	@ResponseBody
	public RuleRuleSet addRuleRuleSetList(@RequestBody RuleRuleSetDto ruleRuleSetDto)
	{
		RuleRuleSet ruleRuleSet=convertRuleFromRuleDto(ruleRuleSetDto);
		return ruleRuleSetService.addRuleRuleSetList(ruleRuleSet);	
	}
	
	
	public RuleRuleSet convertRuleFromRuleDto(final RuleRuleSetDto ruleRuleSetDto)
	{
		RuleRuleSet ruleRuleSet=new RuleRuleSet();
		
		ruleRuleSet.setRuleRuleSetId(ruleRuleSetDto.getRuleRuleSetId());
		
		ruleRuleSet.setRuleId(ruleRuleSetDto.getRuleId());
		
		ruleRuleSet.setRule(ruleRuleSetDto.getRule());
		
		ruleRuleSet.setRuleSet(ruleRuleSetDto.getRuleSet());
		
		ruleRuleSet.setRuleRuleSetList(ruleRuleSetDto.getRuleRuleSetList());
		
		ruleRuleSet.setRuleSetId(ruleRuleSetDto.getRuleSetId());
		
		ruleRuleSet.setRulePriority(ruleRuleSetDto.getRulePriority());

		return ruleRuleSet;
	}
}
