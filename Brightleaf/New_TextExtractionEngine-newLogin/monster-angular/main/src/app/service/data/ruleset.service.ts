import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export class RuleSet {
  constructor(
    public ruleSetId: number,
    public ruleSetName: string,
    public createdBy: string,
    public creationDate: Date,
    public lastModifiedBy: string,
    public lastModificationDate: Date

  ) { }
}

@Injectable({
  providedIn: 'root'
})
export class RulesetService {

  constructor(private http: HttpClient) { }

  getAllRuleSet() {
    return this.http.get<RuleSet[]>(`${environment.RULE_API_URL}/getRuleSetList`);
  }
  createRuleSet(ruleset) {
    return this.http.post(`${environment.RULE_API_URL}/addRuleSet`, ruleset);
  }
  UpdateRuleSetType(ruleset, RsId) {
    return this.http.put<RuleSet>(`${environment.RULE_API_URL}/updateRuleSet/${RsId}`, ruleset);
  }
  deleteRuleSetByRsId(RsId) {
    //console.log("document id "+DocId);
    return this.http.delete(`${environment.RULE_API_URL}/deleteRuleSet/${RsId}`);
  }
  
  
}
