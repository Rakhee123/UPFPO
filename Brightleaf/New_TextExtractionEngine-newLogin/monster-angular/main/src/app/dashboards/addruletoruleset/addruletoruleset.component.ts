
import { Component, ElementRef, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { RulecreationService } from '../../service/data/rulecreation.service';
import { COMPANY_NAME } from '../../service/data/userinfo.service';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { UserroleService } from '../../service/data/userrole.service';
import { PermissionService } from '../../service/data/permission.service';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';

export const AUTHENTICATE_USER = 'authenticateUser';

export class Rule {
  public ruleId: number;
  public documentTypeId: number;
  public attributeId: number;

  constructor(
  ) { }
}

@Component({
  selector: 'app-addruletoruleset',
  templateUrl: './addruletoruleset.component.html',
  styleUrls: ['./addruletoruleset.component.css']
})
export class AddruletorulesetComponent implements OnInit {

  dropdownList = [];
  docType = [];
  attribute = [];
  ruleset = [];
  selectedItems = [];
  singleselectedItems = [];
  singleselectedItems1 = [];
  singleselectedItems2 = [];
  dropdownSettings = {};
  singledropdownSettings = {};
  singledropdownSettings1 = {};
  singledropdownSettings2 = {};
  closeDropdownSelection = true;
  ruleSetId: number;
  rule: Rule;
  show: boolean = false;
  pdfSrc: any;
  filename: any;
  docid = [];
  ruleTable: any;
  ruleTable2: any;
  editForm: any;
  ruleRuleList: any[] = [];
  rulesetRequire: Boolean = false;
  documentRequire: Boolean = false;
  attributeRequire: Boolean = false;
  isDisabled = false;
  deletedList: any;
  tempRuleTable2: any;
  removeList: any[] = [];
  forLoopList: any[] = [];
  appendList: any[] = [];
  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  doneCancelButton: any;
  ruleCreateButton = false;
  ruleViewButton = false;
  resp: any;

  constructor(private router: Router, private rulecreationService: RulecreationService, private elRef: ElementRef, private checkPermissionService: CheckPermissionService, private userRoleService: UserroleService,
    private permissionService: PermissionService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService) {
  }

  getRuleListByAttributeId() {
    this.rulecreationService.getRuleListByAttribute(this.rule.attributeId).subscribe(
      response => {
        var table = $('#myRuleTable').DataTable();
        table.destroy();
        this.ruleTable = [];
        this.ruleTable = response.rulelist;


        $(function () {
          $('#myRuleTable').DataTable({
            "retrieve": true,
            "paging": true,
            "searching": true,
            "pageLength": 5,
            "lengthChange": false,
            "order": [],   /* Disable initial sort */
            "dom": '<"pull-left"f><"pull-right"l>tip',
          });
          $("#myRuleTable_paginate").css("font-size", "14px");
          $("#myRuleTable_info").css("font-size", "14px");
        });

      }, error => {

      });
  }

  getRuleByDocId() {
    this.rulecreationService.getRuleByDocId(this.rule.documentTypeId).subscribe(
      response => {
        var table = $('#myRuleTable').DataTable();
        table.destroy();
        this.ruleTable = [];
        this.ruleTable = response.rulelist;
        $(function () {
          $('#myRuleTable').DataTable({
            "retrieve": true,
            "paging": true,
            "searching": true,
            "pageLength": 5,
            "lengthChange": false,
            "order": [],   /* Disable initial sort */
            "dom": '<"pull-left"f><"pull-right"l>tip',
          });
          $("#myRuleTable_paginate").css("font-size", "14px");
          $("#myRuleTable_info").css("font-size", "14px");
        });

      }, error => {

      });
  }

  getRuleByAttributeAndDocId() {
    this.rulecreationService.getRuleByAttributeAndDocId(this.rule.documentTypeId, this.rule.attributeId).subscribe(
      response => {
        var table = $('#myRuleTable').DataTable();
        table.destroy();
        this.ruleTable = [];
        this.ruleTable = response.rulelist;

        $(function () {
          $('#myRuleTable').DataTable({
            "retrieve": true,
            "paging": true,
            "searching": true,
            "pageLength": 5,
            "lengthChange": false,
            "order": [],   /* Disable initial sort */
            "dom": '<"pull-left"f><"pull-right"l>tip',
          });
          $("#myRuleTable_paginate").css("font-size", "14px");
          $("#myRuleTable_info").css("font-size", "14px");
        });

      }, error => {

      });
  }

  getRuleListByRuleSetId() {
    this.rulecreationService.getRuleListByRuleSetId(this.ruleSetId).subscribe(
      response => {
        var table = $('#myRuleTable2').DataTable();
        table.destroy();
        this.ruleTable2 = [];
        this.ruleTable2 = response.rulelist;
        for (let i = 0; i < this.ruleTable2.length; i++) {
          this.rulecreationService.getRulePriorityByRuleId(this.ruleTable2[i].rule.ruleId,this.ruleSetId).subscribe(
            data => {
              $("#priorId"+this.ruleTable2[i].rule.ruleId).val(data["rulePriority"]);
             
            }
          )
        }
        this.tempRuleTable2 = this.ruleTable2;

      }, error => {

      });
  }

  onRuleSetSelect(e) {
    this.ruleSetId = e.ruleSetId;
    this.getDocuments();
    this.getRuleListByRuleSetId();
    this.isDisabled = true;
    this.appendList=[];
    this.forLoopList=[];
  }

  onRuleSetUnSelect(event) {
    delete this.ruleSetId;
    var table = $('#myRuleTable2').DataTable();
    table.destroy();
    this.ruleTable2 = [];

    var table = $('#myRuleTable').DataTable();
    table.destroy();
    this.ruleTable = [];

    this.attribute = [];
    this.singleselectedItems1 = [];
    this.docType = [];
    this.singleselectedItems = [];
  }


  onAttributeSelect(e2) {
    this.rule.attributeId = e2.attributeId;
    this.documentRequire = false;
    if (this.rule.documentTypeId == undefined) {
      this.getRuleListByAttributeId();
    }
    else {
      this.getRuleByAttributeAndDocId();
    }
  }

  onAttributeUnSelect(event) {

    delete this.rule.attributeId;
    if (this.rule.documentTypeId != undefined) {
      this.getRuleByDocId();
    } else {
      var table = $('#myRuleTable').DataTable();
      table.destroy();
      this.ruleTable = [];
    }
  }

  onDocTypeSelect(e3) {
    this.getAttributes();
    this.rule.documentTypeId = e3.documentTypeId;
    this.attributeRequire = false;
    if (this.rule.attributeId == undefined) {
      this.getRuleByDocId();
    }
    else {
      this.getRuleByAttributeAndDocId();
    }
  }

  onDocTypeUnselect(event) {
    delete this.rule.documentTypeId;
    var table = $('#myRuleTable').DataTable();
    table.destroy();
    this.ruleTable = [];
    this.attribute = [];
    this.singleselectedItems1 = [];
  }

  select(ruleId, index) {
    for (let i = 0; i < this.ruleTable2.length; i++) {
      if (this.ruleTable2[i].rule.ruleId == ruleId) {
        let initialState = {
          title: 'Information',
          btn2: 'Ok',
          body: 'This rule already exists in the rule set',
          enableBtn2: true,
        };

        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        return false;
      }
    }
    if (this.appendList.includes(ruleId)) {
      let initialState = {
        title: 'Information',
        btn2: 'Ok',
        body: 'This rule already exists in the rule set',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      return false;
    }

    var template = "";
    template = template +
      "<tr id='ruleAppend" + index + "'><td> <input type='number' min='1' id='priorId2"+ruleId+"' value='1' style='width: 35px;'> </td><td>" + $("#ruleIdd" + index).text() + "</td><td>" + $("#ruleName" + index).text() + "</td><td> <button type='button' id='buttonId"+ruleId+"' class='btn' ><i class='ti-trash text-danger m-r-3'></i></button></td></tr>";
    $('#myRuleTable2 tbody').append(template);


    let obj1={"ruleId":ruleId,"rulePriority":$("#priorId2"+ruleId).val()}
    this.appendList.push(obj1);

    $("#priorId2" + ruleId).on('change', () => {
      for(let j=0;j<this.appendList.length;j++)
      {
        if(this.appendList[j].ruleId==ruleId)
        {
          this.appendList[j].rulePriority=$("#priorId2"+ruleId).val();
        }
      }
    });
    
    $("#buttonId" + ruleId).on('click', () => {
       $("#ruleAppend" + index).remove();
      for(let k=0;k<this.appendList.length;k++)
      {
        if(this.appendList[k].ruleId==ruleId)
        {
          this.appendList.splice(this.appendList.indexOf(this.appendList[k]), 1);
        }
      }
    });
  }

  remove2(ruleId, index) {
    this.removeList.push(ruleId);
    this.tempRuleTable2.splice(index, 1);
  }

  onDone(form) {
      for (let i = 0; i < this.tempRuleTable2.length; i++) {
        let obj2={"ruleId":this.tempRuleTable2[i].rule.ruleId,"rulePriority":$("#priorId"+this.tempRuleTable2[i].rule.ruleId).val()}
        this.forLoopList.push(obj2);
      }

      var mergeList = (this.forLoopList).concat(this.appendList);
      for(let l = 0; l < mergeList.length; l++)
      {
        if(mergeList[l].rulePriority=="")
        {
          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: 'Please select rule priority for rule',
            enableBtn2: true,
          };
          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          this.forLoopList = [];
          // this.appendList = [];
          return false;
         }
      }
      if (this.ruleSetId == undefined) {
        this.rulesetRequire = true;
        return false;
      }

      this.rulecreationService.deleteRulesFromRuleSetById(this.ruleSetId, this.removeList).subscribe(
        response => {
        },
        error => {
        });

      this.rulecreationService.addRuleToRuleset(mergeList, this.ruleSetId).subscribe(
        response => {
          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: 'Selected rules are saved successfully in the rule set',
            enableBtn2: true,
          };

          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

        },
        error => {
        });
        
      this.forLoopList = [];
      // this.appendList = [];
  }

  resetOnCancel() {
    this.forLoopList = [];
    this.appendList = [];
  
    $('#myRuleTable2').DataTable().clear().draw();
    $('#myRuleTable').DataTable().clear().draw();
    this.ruleTable2 = [];
    this.ruleTable = [];

    this.attribute = [];
    this.singleselectedItems1 = [];
    this.docType = [];
    this.singleselectedItems = [];

    $('#myRuleTable').DataTable().destroy();
    $('#myRuleTable2').DataTable().destroy();
    
  }

  ngOnInit() {

    this.userRoleService.getUserRoleByUsernameAndCompanyId(sessionStorage.getItem(AUTHENTICATE_USER), this.sessionCompanyName).subscribe(
      urResponse => {
        let permissionListString: string = urResponse['permissionList'];
        this.permissionService.getPermissionByPermissionId(permissionListString.toString()).subscribe(
          permissionData => {

            for (var p in permissionData) {

              if (permissionData[p]['permissionName'] == "RULE CREATE") {
                this.ruleCreateButton = true;
              }
              if (permissionData[p]['permissionName'] == "RULE VIEW") {
                this.ruleViewButton = true;
              }

            }
            if (this.ruleCreateButton == true && this.ruleViewButton == true) {
              this.doneCancelButton = true;
            }
            else if (this.ruleCreateButton == false && this.ruleViewButton == false) {
              this.doneCancelButton = false;
            }
            else if (this.ruleCreateButton == true && this.ruleViewButton == false) {
              this.doneCancelButton = true;
            }
            else if (this.ruleCreateButton == false && this.ruleViewButton == true) {
              this.doneCancelButton = false;
            }
          })
      })
    this.rule = new Rule();
    this.refreshRuleSetType();
    this.isDisabled = false;
  }

  getFormatedRule(rule, attributeName) {
    let before = '';
    let after = '';
    let regex;

    if (rule.regex !== null && rule.regex !== '') {
      regex = rule.regex;
      return regex;
    }


    if (rule.textBefore1 !== null) {
      before = before + "(" + rule.textBefore1 + ")";
      if (rule.opBefore1 !== null) {
        before = before + rule.opBefore1 + "(" + rule.textBefore2 + ")";
      }

      if (rule.opBefore2 !== null) {
        before = before + rule.opBefore2 + "(" + rule.textBefore3 + ")";
      }

      if (rule.opBefore3 !== null) {
        before = before + rule.opBefore3 + "(" + rule.textBefore4 + ")";
      }

      if (rule.opBefore4 !== null) {
        before = before + rule.opBefore4 + "(" + rule.textBefore5 + ")";
      }
    }

    if (rule.textAfter1 !== null) {
      after = after + "(" + rule.textAfter1 + ")";
      if (rule.opAfter1 !== null) {
        after = after + rule.opAfter1 + "(" + rule.textAfter2 + ")";
      }

      if (rule.opAfter2 !== null) {
        after = after + rule.opAfter2 + "(" + rule.textAfter3 + ")";
      }

      if (rule.opAfter3 !== null) {
        after = after + rule.opAfter3 + "(" + rule.textAfter4 + ")";
      }

      if (rule.opAfter4 !== null) {
        after = after + rule.opAfter4 + "(" + rule.textAfter5 + ")";
      }
    }
    return before + "[" + attributeName + "]" + after;
  }

  getAttributes() {
    this.rulecreationService.getAllAttribute().subscribe(
      response => {
        this.attribute = response;
        this.singledropdownSettings1 = {
          singleSelection: true,
          idField: 'attributeId',
          textField: 'attributeName',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          allowSearchFilter: true,
          closeDropDownOnSelection: this.closeDropdownSelection
        };
      }, error => {

      });
  }
  getDocuments() {
    this.rulecreationService.getAllDocuments().subscribe(
      response => {

        this.docType = response;
        this.singledropdownSettings = {
          singleSelection: true,
          idField: 'documentTypeId',
          textField: 'documentName',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          allowSearchFilter: true,
          closeDropDownOnSelection: this.closeDropdownSelection
        };

      }, error => {

      });
  }
  refreshRuleSetType() {
    this.rulecreationService.getAllRuleSet().subscribe(
      response => {
        this.ruleset = response;
        this.singledropdownSettings2 = {
          singleSelection: true,
          idField: 'ruleSetId',
          textField: 'ruleSetName',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          allowSearchFilter: true,
          closeDropDownOnSelection: this.closeDropdownSelection
        };
      }, error => {

      });
  }
}
