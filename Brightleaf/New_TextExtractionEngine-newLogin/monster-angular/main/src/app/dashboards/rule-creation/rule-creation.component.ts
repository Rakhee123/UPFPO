import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { PermissionService } from '../../service/data/permission.service';
import { RulecreationService } from '../../service/data/rulecreation.service';
import { COMPANY_NAME } from '../../service/data/userinfo.service';
import { UserroleService } from '../../service/data/userrole.service';

export const AUTHENTICATE_USER = 'authenticateUser';

export class Rule {
  public ruleId: number;
  public documentTypeId: number;
  public attributeId: number;
  public textBefore1: string;
  public opBefore1: string;
  public textBefore2: string;
  public opBefore2: string;
  public textBefore3: string;
  public opBefore3: string;
  public textBefore4: string;
  public opBefore4: string;
  public textBefore5: string;
  public textAfter1: string;
  public opAfter1: string;
  public textAfter2: string;
  public opAfter2: string;
  public textAfter3: string;
  public opAfter3: string;
  public textAfter4: string;
  public opAfter4: string;
  public textAfter5: string;
  public createdBy: string;
  public creationDate: Date;
  public ignoreCase: boolean;
  public merge: boolean;
  public regex: string;
  public searchword: string;
  public found: string;
  public notfound: string;
  constructor(

  ) { }
}

@Component({
  selector: 'app-rule-creation',
  templateUrl: './rule-creation.component.html',
  styleUrls: ['./rule-creation.component.css']
})
export class RuleCreationComponent implements OnInit {
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
  editForm: any;
  message: any;
  textBeforeRequire1: boolean = false;

  textBeforeRequire2: boolean = false;
  textBefore2Disable: boolean = true;
  textBeforeRequire3: boolean = false;
  textBefore3Disable: boolean = true;
  textBeforeRequire4: boolean = false;
  textBefore4Disable: boolean = true;
  textBeforeRequire5: boolean = false;
  textBefore5Disable: boolean = true;

  textAfterRequire1: boolean = false;
  textAfterRequire2: boolean = false;
  textAfter2Disable: boolean = true;
  textAfterRequire3: boolean = false;
  textAfter3Disable: boolean = true;
  textAfterRequire4: boolean = false;
  textAfter4Disable: boolean = true;
  textAfterRequire5: boolean = false;
  textAfter5Disable: boolean = true;


  textBefore2checkedand: boolean = false;
  textBefore2checkedor: boolean = false;
  textBefore3checkedand: boolean = false;
  textBefore3checkedor: boolean = false;
  textBefore4checkedand: boolean = false;
  textBefore4checkedor: boolean = false;

  textAfter2checkedand: boolean = false;
  textAfter2checkedor: boolean = false;
  textAfter3checkedand: boolean = false;
  textAfter3checkedor: boolean = false;
  textAfter4checkedand: boolean = false;
  textAfter4checkedor: boolean = false;

  documentRequire: boolean = false;
  attributeRequire: boolean = false;
  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  submitCancelButton = false;
  ruleCreateButton = false;
  ruleViewButton = false;

  constructor(private router: Router, private rulecreationService: RulecreationService, private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    private checkPermissionService: CheckPermissionService,
    private userRoleService: UserroleService,
    private permissionService: PermissionService) {
  }

  onSubmit(form) {
    if (this.singleselectedItems.length == 0 || this.singleselectedItems1.length == 0) {
      if (this.singleselectedItems.length == 0) {
        this.documentRequire = true;
      }
      if (this.singleselectedItems1.length == 0) {
        this.attributeRequire = true;
      }
      return false;
    }

    if ((this.rule.textBefore1 == undefined || this.rule.textBefore1 == "") &&
      (this.rule.textAfter1 == undefined || this.rule.textAfter1 == "") &&
      (this.rule.regex == undefined || this.rule.regex == "")) {
      this.show = true;
      return false;
    }

    if (this.rule.opBefore1 == undefined || this.rule.opBefore1==null) {
      delete this.rule.textBefore2;
      delete this.rule.opBefore2;

      delete this.rule.textBefore3;
      delete this.rule.opBefore3;

      delete this.rule.textBefore4;
      delete this.rule.opBefore4;

      delete this.rule.textBefore5;
    }

    if (this.rule.opBefore2 == undefined || this.rule.opBefore2==null) {
      delete this.rule.textBefore3;
      delete this.rule.opBefore3;

      delete this.rule.textBefore4;
      delete this.rule.opBefore4;

      delete this.rule.textBefore5;
    }

    if (this.rule.opBefore3 == undefined || this.rule.opBefore3==null) {
      delete this.rule.textBefore4;
      delete this.rule.opBefore4;

      delete this.rule.textBefore5;
    }

    if (this.rule.opBefore4 == undefined || this.rule.opBefore4==null) {
      delete this.rule.textBefore5;
    }

    if (this.rule.regex !== undefined && this.rule.regex !== null) {
      this.rule.regex.trim();
    }

    if (this.rule.regex !== '' && this.rule.regex !== undefined && this.rule.regex !== null) {
      let temp = Object.assign({}, this.rule);
      Object.keys(this.rule).forEach((prop) => delete this.rule[prop]);
      this.rule.ruleId = temp.ruleId;
      this.rule.merge = temp.merge;
      this.rule.ignoreCase = temp.ignoreCase;
      this.rule.regex = temp.regex;
      this.rule.documentTypeId = temp.documentTypeId;
      this.rule.attributeId = temp.attributeId;
    }

    if (this.rule.opAfter1 == undefined || this.rule.opAfter1==null) {
      delete this.rule.textAfter2;
      delete this.rule.opAfter2;

      delete this.rule.textAfter3;
      delete this.rule.opAfter3;

      delete this.rule.textAfter4;
      delete this.rule.opAfter4;

      delete this.rule.textAfter5;
    }

    if (this.rule.opAfter2 == undefined || this.rule.opAfter2==null) {
      delete this.rule.textAfter3;
      delete this.rule.opAfter3;

      delete this.rule.textAfter4;
      delete this.rule.opAfter4;

      delete this.rule.textAfter5;
    }

    if (this.rule.opAfter3 == undefined || this.rule.opAfter3==null) {
      delete this.rule.textAfter4;
      delete this.rule.opAfter4;

      delete this.rule.textAfter5;
    }

    if (this.rule.opAfter4 == undefined || this.rule.opAfter4==null) {
      delete this.rule.textAfter5;
    }

    this.saveRule();

  }

  saveRule() {
    this.rule.createdBy = sessionStorage.getItem(AUTHENTICATE_USER);
    // this.rulecreationService.saveRule(this.rule,this.ruleSetId).subscribe(response => {
    this.rulecreationService.saveRuleToRule(this.rule).subscribe(response => {
      if (this.rule.ruleId == undefined) {
        let initialState = {
          title: 'Information',
          btn2: 'Ok',
          body: 'Rule created successfully.',
          enableBtn2: true,
        };

        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      } else {
        let initialState = {
          title: 'Information',
          btn2: 'Ok',
          body: 'Rule updated successfully.',
          enableBtn2: true,
        };

        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      }
      this.clearFields();
      this.getRuleByAttributeAndDocId();
    }, error => {
      alert("error.!saveRule");
    });
  }

  clearFields() {
    $("#saveUpdateButton").html("Submit");
    this.show = false;

    $('input[name=opBefore15]').prop('checked', false);
    $('input[name=opBefore16]').prop('checked', false);

    $('input[name=opBefore25]').prop('checked', false);
    $('input[name=opBefore26]').prop('checked', false);

    $('input[name=opBefore35]').prop('checked', false);
    $('input[name=opBefore36]').prop('checked', false);

    $('input[name=opBefore45]').prop('checked', false);
    $('input[name=opBefore46]').prop('checked', false);

    $('input[name=opAfter15]').prop('checked', false);
    $('input[name=opAfter16]').prop('checked', false);

    $('input[name=opAfter25]').prop('checked', false);
    $('input[name=opAfter26]').prop('checked', false);

    $('input[name=opAfter35]').prop('checked', false);
    $('input[name=opAfter36]').prop('checked', false);

    $('input[name=opAfter45]').prop('checked', false);
    $('input[name=opAfter46]').prop('checked', false);

    $("#customCheck1").prop("checked", false);
    $("#customCheck2").prop("checked", false);


    delete this.rule.textBefore1;
    delete this.rule.opBefore1;

    delete this.rule.textBefore2;
    delete this.rule.opBefore2;

    delete this.rule.textBefore3;
    delete this.rule.opBefore3;

    delete this.rule.textBefore4;
    delete this.rule.opBefore4;

    delete this.rule.textBefore5;

    delete this.rule.textAfter1;
    delete this.rule.opAfter1;

    delete this.rule.textAfter2;
    delete this.rule.opAfter2;

    delete this.rule.textAfter3;
    delete this.rule.opAfter3;

    delete this.rule.textAfter4;
    delete this.rule.opAfter4;

    delete this.rule.textAfter5;

    delete this.rule.regex;
    delete this.rule.searchword;
    delete this.rule.found;
    delete this.rule.notfound;

    delete this.rule.ignoreCase;
    delete this.rule.merge;

    delete this.rule.ruleId;

    this.textBefore2Disable = true;
    this.textBefore3Disable = true;
    this.textBefore4Disable = true;
    this.textBefore5Disable = true;

    this.textAfter2Disable = true;
    this.textAfter3Disable = true;
    this.textAfter4Disable = true;
    this.textAfter5Disable = true;

    this.textAfterRequire1 = false;
    this.textBeforeRequire1 = false;
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
          $("#myRuleTable_paginate").css("font-size","14px");
          $("#myRuleTable_info").css("font-size","14px");
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
          
          $("#myRuleTable_paginate").css("font-size","14px");
          $("#myRuleTable_info").css("font-size","14px");

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
          $("#myRuleTable_paginate").css("font-size","14px");
          $("#myRuleTable_info").css("font-size","14px");
        });

      }, error => {

      });
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

  setAndOrBefore1(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textBeforeRequire1 = true;
      this.textBeforeRequire2 = true;
      this[andOr.getAttribute('data-text-type')] = false;
    }
    else {
      this.textBefore2checkedand = false;
      this.textBefore2checkedor = false;
      this.textBefore3checkedand = false;
      this.textBefore3checkedor = false;
      this.textBefore4checkedand = false;
      this.textBefore4checkedor = false;

      this.textBeforeRequire1 = false;
      this.textBeforeRequire2 = false;

      this.textBefore2Disable = true;
      this.textBefore3Disable = true;
      this.textBefore4Disable = true;
      this.textBefore5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }

  setAndOrBefore2(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textBeforeRequire2 = true;
      this[andOr.getAttribute('data-text-type')] = false;
    }
    else {
      delete this.rule[andOr.name];
      this.textBefore3checkedand = false;
      this.textBefore3checkedor = false;
      this.textBefore4checkedand = false;
      this.textBefore4checkedor = false;

      this.textBeforeRequire2 = true;
      this.textBefore3Disable = true;
      this.textBefore4Disable = true;
      this.textBefore5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }

  setAndOrBefore3(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textBeforeRequire3 = true;
      this[andOr.getAttribute('data-text-type')] = false;
    }
    else {
      delete this.rule[andOr.name];
      this.textBefore4checkedand = false;
      this.textBefore4checkedor = false;

      this.textBeforeRequire3 = true;
      this.textBefore4Disable = true;
      this.textBefore5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }

  setAndOrBefore4(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textBeforeRequire4 = true;
      this[andOr.getAttribute('data-text-type')] = false;
    }
    else {
      delete this.rule[andOr.name];
      this.textBeforeRequire4 = true;
      this.textBefore5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }


  setAndOrAfter1(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textAfterRequire1 = true;
    }
    else {
      delete this.rule[andOr.name];
      this.textAfter2checkedand = false;
      this.textAfter2checkedor = false;
      this.textAfter3checkedand = false;
      this.textAfter3checkedor = false;
      this.textAfter4checkedand = false;
      this.textAfter4checkedor = false;

      this.textAfterRequire1 = false;
      this.textAfter2Disable = true;
      this.textAfter3Disable = true;
      this.textAfter4Disable = true;
      this.textAfter5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }

  setAndOrAfter2(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textAfterRequire2 = true;
    }
    else {
      delete this.rule[andOr.name];
      this.textAfter3checkedand = false;
      this.textAfter3checkedor = false;
      this.textAfter4checkedand = false;
      this.textAfter4checkedor = false;

      this.textAfterRequire2 = true;
      this.textAfter3Disable = true;
      this.textAfter4Disable = true;
      this.textAfter5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }

  setAndOrAfter3(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textAfterRequire3 = true;
    }
    else {
      delete this.rule[andOr.name];
      this.textAfter4checkedand = false;
      this.textAfter4checkedor = false;

      this.textAfterRequire3 = true;
      this.textAfter4Disable = true;
      this.textAfter5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }
  setAndOrAfter4(andOr) {
    if (andOr.checked) {
      this.rule[andOr.name] = andOr.value;
      this.textAfterRequire4 = true;
    }
    else {
      delete this.rule[andOr.name];
      this.textAfterRequire4 = true;
      this.textAfter5Disable = true;
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }
  setAndOr(andOr) {
    if (andOr.checked) {
      this.rule[andOr.getAttribute('data-text-type2')] = andOr.value;

      this.textBeforeRequire2 = true;
      this.textBeforeRequire3 = true;
      this.textBeforeRequire4 = true;
      this.textBeforeRequire5 = true;

      this.textAfterRequire2 = true;
      this.textAfterRequire3 = true;
      this.textAfterRequire4 = true;
      this.textAfterRequire5 = true;


      this[andOr.getAttribute('data-text-type')] = false;

    } else {
      delete this.rule[andOr.getAttribute('data-text-type2')];
      this[andOr.getAttribute('data-text-type')] = true;
    }
  }

  resetAllField() {
    $('input[name=opBefore15]').prop('checked', false);
    $('input[name=opBefore16]').prop('checked', false);

    $('input[name=opBefore25]').prop('checked', false);
    $('input[name=opBefore26]').prop('checked', false);

    $('input[name=opBefore35]').prop('checked', false);
    $('input[name=opBefore36]').prop('checked', false);

    $('input[name=opBefore45]').prop('checked', false);
    $('input[name=opBefore46]').prop('checked', false);

    $('input[name=opAfter15]').prop('checked', false);
    $('input[name=opAfter16]').prop('checked', false);

    $('input[name=opAfter25]').prop('checked', false);
    $('input[name=opAfter26]').prop('checked', false);

    $('input[name=opAfter35]').prop('checked', false);
    $('input[name=opAfter36]').prop('checked', false);

    $('input[name=opAfter45]').prop('checked', false);
    $('input[name=opAfter46]').prop('checked', false);

    $("#customCheck1").prop("checked", false);
    $("#customCheck2").prop("checked", false);

    this.textBefore2Disable = true;
    this.textBefore3Disable = true;
    this.textBefore4Disable = true;
    this.textBefore5Disable = true;

    this.textAfter2Disable = true;
    this.textAfter3Disable = true;
    this.textAfter4Disable = true;
    this.textAfter5Disable = true;

    delete this.rule.ruleId;
    this.show = false;

  }

  resetOnCancel() {
    $('input[name=opBefore15]').prop('checked', false);
    $('input[name=opBefore16]').prop('checked', false);

    $('input[name=opBefore25]').prop('checked', false);
    $('input[name=opBefore26]').prop('checked', false);

    $('input[name=opBefore35]').prop('checked', false);
    $('input[name=opBefore36]').prop('checked', false);

    $('input[name=opBefore45]').prop('checked', false);
    $('input[name=opBefore46]').prop('checked', false);

    $('input[name=opAfter15]').prop('checked', false);
    $('input[name=opAfter16]').prop('checked', false);

    $('input[name=opAfter25]').prop('checked', false);
    $('input[name=opAfter26]').prop('checked', false);

    $('input[name=opAfter35]').prop('checked', false);
    $('input[name=opAfter36]').prop('checked', false);

    $('input[name=opAfter45]').prop('checked', false);
    $('input[name=opAfter46]').prop('checked', false);

    $("#customCheck1").prop("checked", false);
    $("#customCheck2").prop("checked", false);

    this.textBefore2Disable = true;
    this.textBefore3Disable = true;
    this.textBefore4Disable = true;
    this.textBefore5Disable = true;

    this.textAfter2Disable = true;
    this.textAfter3Disable = true;
    this.textAfter4Disable = true;
    this.textAfter5Disable = true;

    delete this.rule.ruleId;
    this.show = false;

    var table = $('#myRuleTable').DataTable();
    table.destroy();
    this.ruleTable = [];
    $("#saveUpdateButton").html("Submit");
  }

  editButton(ruleId) {
    this.rulecreationService.getRuleByRuleId(ruleId).subscribe(
      response => {

        let object = response.rulelist[0];
        this.resetAllField();
        this.clearFields();
        this.rule.ruleId = ruleId;

        this.singleselectedItems1 = [{ attributeId: object.rule.attributeId, attributeName: object.docAndAttribute.attributeName }];
        this.singleselectedItems = [{ documentTypeId: object.rule.documentTypeId, documentName: object.docAndAttribute.documentType }];

        this.rule.attributeId = object.rule.attributeId;
        this.rule.documentTypeId = object.rule.documentTypeId;

        this.rule.textBefore1 = object.rule.textBefore1;
        this.rule.textBefore2 = object.rule.textBefore2;
        this.rule.textBefore3 = object.rule.textBefore3;
        this.rule.textBefore4 = object.rule.textBefore4;
        this.rule.textBefore5 = object.rule.textBefore5;

        this.rule.textAfter1 = object.rule.textAfter1;
        this.rule.textAfter2 = object.rule.textAfter2;
        this.rule.textAfter3 = object.rule.textAfter3;
        this.rule.textAfter4 = object.rule.textAfter4;
        this.rule.textAfter5 = object.rule.textAfter5;

        this.rule.opBefore1=object.rule.opBefore1;
        this.rule.opBefore2=object.rule.opBefore2;
        this.rule.opBefore3=object.rule.opBefore3;
        this.rule.opBefore4=object.rule.opBefore4;

        this.rule.opAfter1=object.rule.opAfter1;
        this.rule.opAfter2=object.rule.opAfter2;
        this.rule.opAfter3=object.rule.opAfter3;
        this.rule.opAfter4=object.rule.opAfter4;

        this.rule.regex = object.rule.regex;
        this.rule.searchword = object.rule.searchword;
        this.rule.found = object.rule.found;
        this.rule.notfound = object.rule.notfound;

        this.rule.ignoreCase=object.rule.ignoreCase;
        this.rule.merge=object.rule.merge;

        if (object.rule.ignoreCase == true) {
          $("#customCheck1").prop("checked", true);
        }
        else {
          $("#customCheck1").prop("checked", false);
        }


        if (object.rule.merge == true) {
          $("#customCheck2").prop("checked", true);
        }
        else {
          $("#customCheck2").prop("checked", false);
        }

        if (object.rule.opBefore1 != null) {
          $('input[name=opBefore16]').prop('checked', false);
          $('input[name=opBefore15]').prop('checked', false);
          if (object.rule.opBefore1 == "OR") {
            $('input[name=opBefore16]').prop('checked', true);
          } else {
            $('input[name=opBefore15]').prop('checked', true);
          }
          this.textBefore2Disable = false;
        }


        if (object.rule.opBefore2 != null) {
          $('input[name=opBefore26]').prop('checked', false);
          $('input[name=opBefore25]').prop('checked', false);
          if (object.rule.opBefore2 == "OR") {
            $('input[name=opBefore26]').prop('checked', true);
          } else {
            $('input[name=opBefore25]').prop('checked', true);
          }
          this.textBefore3Disable = false;
        }

        if (object.rule.opBefore3 != null) {
          $('input[name=opBefore36]').prop('checked', false);
          $('input[name=opBefore35]').prop('checked', false);
          if (object.rule.opBefore3 == "OR") {
            $('input[name=opBefore36]').prop('checked', true);
          } else {
            $('input[name=opBefore35]').prop('checked', true);
          }
          this.textBefore4Disable = false;
        }

        if (object.rule.opBefore4 != null) {
          $('input[name=opBefore46]').prop('checked', false);
          $('input[name=opBefore45]').prop('checked', false);
          if (object.rule.opBefore4 == "OR") {
            $('input[name=opBefore46]').prop('checked', true);
          } else {
            $('input[name=opBefore45]').prop('checked', true);
          }
          this.textBefore5Disable = false;
        }

        if (object.rule.opAfter1 != null) {
          $('input[name=opAfter15]').prop('checked', false);
          $('input[name=opAfter16]').prop('checked', false);
          if (object.rule.opAfter1 == "OR") {
            $('input[name=opAfter16]').prop('checked', true);
          } else {
            $('input[name=opAfter15]').prop('checked', true);
          }
          this.textAfter2Disable = false;
        }

        if (object.rule.opAfter2 != null) {
          $('input[name=opAfter25]').prop('checked', false);
          $('input[name=opAfter26]').prop('checked', false);
          if (object.rule.opAfter2 == "OR") {
            $('input[name=opAfter26]').prop('checked', true);
          } else {
            $('input[name=opAfter25]').prop('checked', true);
          }
          this.textAfter3Disable = false;
        }

        if (object.rule.opAfter3 != null) {
          $('input[name=opAfter35]').prop('checked', false);
          $('input[name=opAfter36]').prop('checked', false);
          if (object.rule.opAfter3 == "OR") {
            $('input[name=opAfter36]').prop('checked', true);
          } else {
            $('input[name=opAfter35]').prop('checked', true);
          }
          this.textAfter4Disable = false;
        }

        if (object.rule.opAfter4 != null) {
          $('input[name=opAfter45]').prop('checked', false);
          $('input[name=opAfter46]').prop('checked', false);
          if (object.rule.opAfter4 == "OR") {
            $('input[name=opAfter46]').prop('checked', true);
          } else {
            $('input[name=opAfter45]').prop('checked', true);
          }
          this.textAfter5Disable = false;
        }
        $("#saveUpdateButton").html("Update");
        $(window).scrollTop(0);
      }, error => {

      });
  }

  onAttributeSelect(e2) {
    this.rule.attributeId = e2.attributeId;
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
    this.rule.documentTypeId = e3.documentTypeId;
    if (this.rule.attributeId == undefined) {
      this.getRuleByDocId();
    }
    else {
      this.getRuleByAttributeAndDocId();
    }
  }

  onDocTypeUnselect(event) {
    delete this.rule.documentTypeId;
    if (this.rule.attributeId != undefined) {
      this.getRuleListByAttributeId();
    }
    else {
      var table = $('#myRuleTable').DataTable();
      table.destroy();
      this.ruleTable = [];
    }
  }

  onFileSelected() {
    let $img: any = document.querySelector('#file');
    this.filename = $img.files[0].name;

    if (typeof (FileReader) !== 'undefined') {
      let reader = new FileReader();

      reader.onload = (e: any) => {
        this.pdfSrc = e.target.result;
      };

      reader.readAsArrayBuffer($img.files[0]);
    }
  }

  ngOnInit() {
    this.rule = new Rule();
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
              this.submitCancelButton = true;
            }
            else if (this.ruleCreateButton == false && this.ruleViewButton == false) {
              this.submitCancelButton = false;
            }
            else if (this.ruleCreateButton == true && this.ruleViewButton == false) {
              this.submitCancelButton = true;
            }
            else if (this.ruleCreateButton == false && this.ruleViewButton == true) {
              this.submitCancelButton = false;
            }

          })
      })
    this.refreshDocumentType();
  }

  ngAfterViewChecked() {
    document.getElementById('scaleSelectContainer').style.setProperty('min-width', '82px');
    document.getElementById('scaleSelectContainer').style.setProperty('max-width', '110px');
    document.getElementById('toolbarViewerMiddle').style.setProperty('margin-left', '63px');
    $("#mainContainer > .toolbar").css("position", "initial");
    $('#userNameNone').css("display", "none");
    $('#passwordNone').css("display", "none");
    $(window).on('load', function () {
      $('#userNameNone').css("display", "none");
      $('#passwordNone').css("display", "none");
    });
  }

 
  refreshDocumentType() {
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
}
