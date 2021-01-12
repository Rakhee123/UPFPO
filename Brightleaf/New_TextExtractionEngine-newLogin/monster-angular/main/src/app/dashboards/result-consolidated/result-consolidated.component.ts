import { AfterViewInit, Component, ElementRef, Injectable, OnInit, Renderer2, ViewChild } from '@angular/core';
import 'datatables.net';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { ResultService } from '../../service/data/result.service';
import { ResultconsolidatedDirective } from '../result-consolidated/resultconsolidated.directive';

export const COMPANY_NAME = 'companyName';

@Component({
  selector: 'app-result-consolidated',
  templateUrl: './result-consolidated.component.html',
  styleUrls: ['./result-consolidated.component.css']
})

@Injectable({
  providedIn: 'root'
})
export class ResultConsolidatedComponent implements OnInit, AfterViewInit {
  attributeData: any = {};
  dataForCon: {};
  consol = [];
  attributeList = [];
  transactionId: string = "";
  singleselectedItems = [];
  singleselectedItems1 = [];
  singledropdownSettings = {};
  docList = [];
  documentNameValid:boolean=false;
  singleselectedItems2=[];
  currentPage:number=0;
  merge:boolean=false;
  isIgnore:boolean=false;
  fallbackValue:string="";

  @ViewChild(ResultconsolidatedDirective) dirs;
  @ViewChild('ViewRuleSetContentTemp') viewRuleSetContentTemp: ElementRef;
  constructor(private resultService: ResultService, private bsModalRef: BsModalRef,
    private bsModalService: BsModalService, private renderer: Renderer2) {
  }

  ngOnInit() {
    let txn_id = sessionStorage.getItem('transaction_id');
    let qc_level = sessionStorage.getItem('qc_level');
    this.transactionId = txn_id;
    this.getResultDataConsolidated(txn_id, qc_level);
  }

  getResultDataConsolidated(transactionId, qcLevel): Boolean {
    this.resultService.getResultDataConsolidated(transactionId, qcLevel)
      .subscribe(
        response => {
          this.consol = response;
          $(() => {
            $('#myTable').DataTable({
              "retrieve": true,
              "paging": true,
              "searching": true,
              "pageLength": 5,
              "lengthChange": false,
              "columnDefs": [
                { orderable: false, targets: '_all' }
              ],
              "ordering": false,
              "dom": '<"pull-left"f><"pull-right"l>tip',
            }).page(this.currentPage).draw(false);
            $("#myTable_paginate").css("font-size","14px");
          $("#myTable_info").css("font-size","14px");
          });
          this.setDocumentListDropdown(response);
          return true;
        }, error => {
          return false;
        });
    return true;
  }


  setDocumentListDropdown(response) {
    this.docList = response;
    this.singledropdownSettings = {
      singleSelection: true,
      idField: 'documentName',
      textField: 'documentName',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      allowSearchFilter: true,
      closeDropDownOnSelection: true
    };
  }

  verifyAllDocs() {
    let txnId = sessionStorage.getItem('transaction_id');
    let qcLevel = sessionStorage.getItem('qc_level');
    let userName = sessionStorage.getItem('authenticateUser');
    this.resultService.verifyAllDoc(txnId, qcLevel, userName)
      .subscribe(
        response => {

          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: 'Record verified successfully.',
            enableBtn2: true,
          };
          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          $(".verifyDone").html("Verified");
          $(".verifyDone").css("color", "green");
          $(".btnVrify").css("display", "none");
          this.dirs.showHideVerifyTxnButton("hidden");
          let txn_id = sessionStorage.getItem('transaction_id');
          let qc_level = sessionStorage.getItem('qc_level');
          $('#myTable').DataTable().destroy();
          this.getResultDataConsolidated(txn_id, qc_level);
        }, error => {
        });
  }

  setTextArea(id) {
    $("#" + id).css("height", "100px");
    $("#" + id).css("border", "1px solid #2B60DE");
  }

  onBlurMethod(id) {
    $("#" + id).css("height", "30px");
    $("#" + id).css("border", "none");
  }

  ngAfterViewInit() {
    this.dirs.showHideVerifyTxnButton("hidden");
    this.getTransactionVerifyStatus();

    $('#myTable').on('page.dt', () => {
      var table = $('#myTable').DataTable();
      var info = table.page.info();
      this.currentPage = info.page;
    });
  }

  verifyAttribute(index) {

    let docName = $("#docName" + index).html();
    let newValue = $("#myTextArea" + index).val();
    if (newValue == undefined) {
      newValue = $("#customizeList" + index).val();
    }
    let attributeName = $("#attributeName" + index).html();
    let appliedRule = $("#appliedRule" + index).val();
    let qcLevel = sessionStorage.getItem('qc_level');
    let initialValue = $("#myTextAreaHidden" + index).html();
    let userName = sessionStorage.getItem('authenticateUser');
    let txnId = sessionStorage.getItem('transaction_id');
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    let content = {
      "DocumentName": docName,
      "AttributeName": attributeName,
      "AppliedRule": appliedRule,
      "InitialValue": initialValue,
      "NewValue": newValue,
      "QcDoneBy": userName,
      "CompanyName": companyName,
      "TransactionId": txnId,
      "QcLevel": qcLevel
    }

    this.resultService.verifyAttribute(content)
      .subscribe(
        response => {
          $("#verifyDone" + index).html("Verified");
          $("#verifyDone" + index).css("color", "green");
          $("#btnVrify" + index).css("display", "none");
          $("#ignoreResult" + index).removeAttr('disabled');
          this.getTransactionVerifyStatus();
        }, error => {

        });
  }

  addAttribute(form) {
    if (this.singleselectedItems1.toString() == "") {
      this.documentNameValid = true;
      return false;
    }
    this.attributeData.qcDoneBy = sessionStorage.getItem('authenticateUser');
    let txnId = sessionStorage.getItem('transaction_id');
    let qcLevel = sessionStorage.getItem('qc_level');
    this.attributeData.documentName = this.singleselectedItems1.toString();
    if (this.checkAttributAvailable()) {
      return false;
    }
    this.resultService.addAttribute(txnId, qcLevel, this.attributeData)
      .subscribe(
        response => {
          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: 'Record added successfully.',
            enableBtn2: true,
          };

          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

          $('#myTable').DataTable().destroy();
          this.getResultDataConsolidated(txnId, qcLevel);
          this.dirs.showHideVerifyTxnButton("visible");
          $('#modalForAddAttribute').modal('hide');
        }, error => {

        });

    form.resetForm();
  }

  resetFormData(form) {
    form.resetForm();
  }

  resetFormDataForAttribute(form) {
    form.resetForm();
    this.documentNameValid = false;
  }

  resetFormDataForAddValue(form) {
    form.resetForm();
    this.documentNameValid = false;
  }

  upadteQCLevel(index) {
    let docName = $("#docName" + index).html();
    let newValue = $("#myTextArea" + index).val();
    let attributeName = $("#attributeName" + index).html();
    let appliedRule = $("#appliedRule" + index).val();
    let qcLevel = sessionStorage.getItem('qc_level');
    let initialValue = $("#myTextAreaHidden" + index).html();
    let userName = sessionStorage.getItem('authenticateUser');
    let txnId = sessionStorage.getItem('transaction_id');
    let ignoreResult = "NO";
    if ($("#ignoreResult" + index).is(":checked")) {
      ignoreResult = "YES";
    }
    let content = {
      "DocumentName": docName,
      "AttributeName": attributeName,
      "AppliedRule": appliedRule,
      "InitialValue": initialValue,
      "QcLevel": qcLevel,
      "NewValue": newValue,
      "Status": "UNVERIFIED",
      "QcDoneBy": userName,
      "IgnoreResult": ignoreResult
    }

    this.resultService.upadteQCLevel(content, txnId)
      .subscribe(
        response => {

          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: 'Record updated successfully.',
            enableBtn2: true,
          };

          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

          $("#verifyDone" + index).html("Pending Verification");
          $("#verifyDone" + index).css("color", "red");
          $("#btnVrify" + index).css("display", "block");
          this.dirs.showHideVerifyTxnButton("visible");
          $("#ignoreResult" + index).removeAttr('disabled');
        }, error => {

        });
  }

  getInitialValue(object) {
    if (object.applicationExtractedValue == undefined) {
      return object.qcValidation.newValue;
    } else {
      return object.applicationExtractedValue.initialValue;
    }
  }

  callFunction(object) {

    if (object.qcValidation == undefined) {
      return "UNVERIFIED";
    } else {
      return object.qcValidation.status;
    }
  }

  getTransactionVerifyStatus() {
    let txn_id = sessionStorage.getItem('transaction_id');
    let qc_level = sessionStorage.getItem('qc_level');

    this.resultService.getTransactionVerifyStatus(txn_id, qc_level).subscribe(
      response => {

        if (response == "UNVERIFIED") {
          this.dirs.showHideVerifyTxnButton("visible");
        } else {
          this.dirs.showHideVerifyTxnButton("hidden");
        }
      }, error => {
      });
  }

  ignoreResult(index) {
    let documentName = $("#docName" + index).html();
    let attributeName = $("#attributeName" + index).html();
    let ignoreResult = "NO";
    let attributeValue = $("#myTextArea" + index).val();
    if (attributeValue == undefined) {
      attributeValue = $("#customizeList" + index).val();
    }
    if ($("#ignoreResult" + index).is(":checked")) {
      ignoreResult = "YES";
    }
    let qcLevel = sessionStorage.getItem('qc_level');
    if ($("#ignoreResulttt" + index).is(":checked")) {
      ignoreResult = "YES";
    }
    let content = {
      "transactionId": this.transactionId,
      "qcLevel": qcLevel,
      "documentName": documentName,
      "attributeName": attributeName,
      "ignoreResult": ignoreResult,
      "attributeValue": attributeValue
    };
    this.resultService.ignoreResult(content)
      .subscribe(
        response => {
          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: 'Record saved successfully.',
            enableBtn2: true,
          };

          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

          $('#myTable').DataTable().destroy();
          this.getResultDataConsolidated(this.transactionId, qcLevel);
        }, error => {
        });
  }

  onSubmit(f) {
  }

  addValue(f) {
    if (this.singleselectedItems2.toString() == "") {
      this.documentNameValid = true;
      return false;
    }
    let qcLevel = sessionStorage.getItem('qc_level');
    this.attributeData.transactionId = this.transactionId;
    this.attributeData.qcLevel = qcLevel;
    this.attributeData.documentName = this.singleselectedItems.toString();
    this.attributeData.qcDoneBy = sessionStorage.getItem('authenticateUser');
    if (this.checkAttributValueAvailable()) {
      return false;
    }
    this.resultService.addValue(this.attributeData).subscribe(
      response => {
        let initialState = {
          title: 'Information',
          btn2: 'Ok',
          body: 'Record saved successfully.',
          enableBtn2: true,
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        $('#myTable').DataTable().destroy();
        this.getResultDataConsolidated(this.transactionId, qcLevel);
        $('#myModalForAddValue').modal('hide');
      }, error => {
      });
    f.resetForm();
  }

  onDocTypeSelect(event) {
    if (this.singleselectedItems.toString() != "") {
      return false;
    }
  }

  onDocTypeSelectForAttribute(event) {
    if (this.singleselectedItems1.toString() != "") {
      this.documentNameValid = false;
      return false;
    }
  }

  onDocTypeSelectForAddValue(event) {
    if (this.singleselectedItems2.toString() != "") {
      this.documentNameValid = false;
      return false;
    }
  }

  clearAttributeData(attributeName) {
    $("#attributeNameForAddValue").html(attributeName);
    this.attributeData.attributeName = attributeName;
  }

  changeCustemValue(index) {
    let docName = $("#docName" + index).html();
    let newValue = $("#customizeList" + index).val();
    let attributeName = $("#attributeName" + index).html();
    let appliedRule = $("#appliedRule" + index).val();
    let qcLevel = sessionStorage.getItem('qc_level');
    let initialValue = $("#myTextAreaHidden" + index).html();
    let userName = sessionStorage.getItem('authenticateUser');
    let txnId = sessionStorage.getItem('transaction_id');
    let ignoreResult = "NO";
    if ($("#ignoreResult" + index).is(":checked")) {
      ignoreResult = "YES";
    }
    let content = {
      "DocumentName": docName,
      "AttributeName": attributeName,
      "AppliedRule": appliedRule,
      "InitialValue": initialValue,
      "QcLevel": qcLevel,
      "NewValue": newValue,
      "Status": "UNVERIFIED",
      "QcDoneBy": userName,
      "IgnoreResult": ignoreResult
    }

    this.resultService.changeCustemValue(content, txnId).subscribe(
      response => {
        let initialState = {
          title: 'Information',
          btn2: 'Ok',
          body: 'Record updated successfully.',
          enableBtn2: true,
        };

        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

        $("#verifyDone" + index).html("Pending Verification");
        $("#verifyDone" + index).css("color", "red");
        $("#btnVrify" + index).css("display", "block");
        this.dirs.showHideVerifyTxnButton("visible");
        $("#ignoreResult" + index).removeAttr('disabled');
      },
      error => {
      });
  }

  checkAttributAvailable() {
    for (let i = 0; i < this.consol.length; i++) {
      if (this.consol[i].documentName == this.attributeData.documentName) {
        for (let j = 0; j < this.consol[i].attributes.length; j++) {
          if (this.attributeData.attributeName.toLowerCase() == this.consol[i].attributes[j].attributeName.toLowerCase()) {

            let initialState = {
              title: 'Information',
              btn2: 'Ok',
              body: 'Attibute already exists.',
              enableBtn2: true,
            };
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            return true;
          }
        }
      }
    }
    return false;
  }

  checkAttributValueAvailable() {
    for (let i = 0; i < this.consol.length; i++) {
      if (this.consol[i].documentName == this.attributeData.documentName) {
        for (let j = 0; j < this.consol[i].attributes.length; j++) {
          if (this.attributeData.attributeName == this.consol[i].attributes[j].attributeName) {
            let value = "";
            if (this.consol[i].attributes[j].applicationExtractedValue == undefined) {
              value = this.consol[i].attributes[j].qcValidation.newValue;
            } else {
              value = this.consol[i].attributes[j].applicationExtractedValue.initialValue;
            }

            if (value.toLowerCase() == this.attributeData.attributeValue.toLowerCase()) {
              let initialState = {
                title: 'Information',
                btn2: 'Ok',
                body: 'Attibute value already exists.',
                enableBtn2: true,
              };
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
              return true;
            }
          }
        }
      }
    }
    return false;
  }

viewRuleSet(ruleId,attributeName){
  if(ruleId==0 || ruleId==-1 ){
    this.renderer.setProperty(this.viewRuleSetContentTemp.nativeElement, 'innerHTML', 'no rule');
    this.fallbackValue="N/A";
  }
  else{
        this.resultService.getRuleById(ruleId).subscribe(
          response => {

          let rule = this.getFormatedRule(response, attributeName);
          this.renderer.setProperty(this.viewRuleSetContentTemp.nativeElement, 'innerHTML', rule);
        },
        error =>{
      });

      
      this.resultService.getAttributeByName(attributeName).subscribe(
        response => {
          console.log(response);
        if(response['fallbackValue']==="" || response['fallbackValue']===null ){
          this.fallbackValue="N/A";
        }else{
          this.fallbackValue=response['fallbackValue'];
        }
       },
       error =>{
     });
  }
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
    return before + "<b style='font-size: 18px;'>[" + attributeName + "]</b>" + after;
  }
}
