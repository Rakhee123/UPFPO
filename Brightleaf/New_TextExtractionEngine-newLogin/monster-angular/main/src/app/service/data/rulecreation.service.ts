import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RuleSet } from './ruleset.service';
import { Rule } from '../../dashboards/rule-creation/rule-creation.component';
import { environment } from '../../../environments/environment';


export class Attribute {
  constructor(
    public attributeId: number,
    public attributeName: string,
    public attributeDesc: string,
    public qcLevels: number,
    public attributeType: string,
    public createdBy: string,
    public creationDate: Date,
    public lastModifiedBy: string,
    public lastModificationDate: Date

  ) { }
}

@Injectable({
  providedIn: 'root'
})
export class RulecreationService {
 

  constructor(private http: HttpClient) { }

  getAllAttribute() {
    return this.http.get<Attribute[]>(`${environment.DOC_API_URL}/getAttributeList`);
  }

  getAllDocuments() {
    return this.http.get<DocumentType[]>(`${environment.DOC_API_URL}/getDocumentTypeList`);
  }

  getAllRuleSet() {
    return this.http.get<RuleSet[]>(`${environment.RULE_API_URL}/getRuleSetList`);
  }

  getRuleList(){
    return this.http.get<Rule[]>(`${environment.RULE_API_URL}/getRuleList`);
  }

  saveRule(rule,ruleSetId)
  {
    return this.http.post<any>(`${environment.RULE_API_URL}/addRule/${ruleSetId}`,rule);
  }

  saveRuleToRule(rule)
  {
    return this.http.post<any>(`${environment.RULE_API_URL}/addRuleToRule/`,rule);
  }

  getRuleListByRuleSetId(ruleSetId){
    return this.http.get<any>(`${environment.RULE_API_URL}/getRuleListByRuleSetId/${ruleSetId}`);
  }

  getRuleListByAttribute(attributeId)
  {
    return this.http.get<any>(`${environment.RULE_API_URL}/getRulesByAttribute/${attributeId}`);
  }

  getRuleByDocId(documentTypeId)
  {
    return this.http.get<any>(`${environment.RULE_API_URL}/getRuleByDocId/${documentTypeId}`);
  }

  getRuleByAttributeAndDocId(documentTypeId,attributeId)
  {
    return this.http.get<any>(`${environment.RULE_API_URL}/getRuleByAttributeAndDocId/${documentTypeId}/${attributeId}`);
  }

  getRuleByRuleId(ruleId){
    return this.http.get<any>(`${environment.RULE_API_URL}/getRuleByRuleId/${ruleId}`);
  }

  addRuleToRuleset(mergeList, ruleSetId) {
    return this.http.post<any>(`${environment.RULE_API_URL}/addRuleRuleSetId/${ruleSetId}`,mergeList);
  }

  deleteRuleFromRuleset(ruleId,ruleSetId,ruleIdList)
  {
    return this.http.delete<any>(`${environment.RULE_API_URL}/deleteRuleFromRuleSet/${ruleId}/${ruleSetId}`,ruleIdList);
  }

  deleteRulesFromRuleSetById(ruleSetId,ruleIdList)
  {
    return this.http.delete<any>(`${environment.RULE_API_URL}/deleteRulesFromRuleSetById/${ruleSetId}`,ruleIdList);
  }

  getRulePriorityByRuleId(ruleId,ruleSetId)
  {
    return this.http.get<any>(`${environment.RULE_API_URL}/getRulePriorityByRuleId/${ruleId}/${ruleSetId}`);
  }





}
