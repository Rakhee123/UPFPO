import { Component, ElementRef, HostListener, Injectable, OnInit, Renderer2, ViewChild } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { NgxNavigationWithDataComponent } from 'ngx-navigation-with-data';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { ResultService } from '../../service/data/result.service';
var alertify = require('alertifyjs');

export const COMPANY_NAME = 'companyName';
@Component({
  selector: 'app-result-document',
  templateUrl: './result-document.component.html',
  styleUrls: ['./result-document.component.css']
})

@Injectable({
  providedIn: 'root'
})

export class ResultDocumentComponent implements OnInit {
  @ViewChild('btnVrifyAllDocWise') btnVrifyAll: ElementRef;
  @ViewChild('addAttributeTemp') addAttributeTemp: ElementRef;
  @ViewChild('ViewRuleSetContentTemp') viewRuleSetContentTemp: ElementRef;
  attributeData: any = {};
  dataForCon: {};
  resultList = [];
  docList = [];
  status: string = "N";
  singledropdownSettings = {};
  singleselectedItems : string = "";
  docName: string = "";
  transactionId: string = "";
  rootStatus: string;
  public currentPdf: string = null;
  qcLevel: number;
  currentPage:number=0;
  merge:boolean=false;
  isIgnore:boolean=false;
  fallbackValue:string="";
  docCount:number=0;
  constructor(private resultService: ResultService, public router2: NgxNavigationWithDataComponent, private renderer: Renderer2, private bsModalRef: BsModalRef,
    private bsModalService: BsModalService) {
  }

  @HostListener("window:beforeunload", ["$event"]) unloadHandler(event: Event) {
    sessionStorage.setItem('transactionId', this.transactionId);
    sessionStorage.setItem('qcLevel', this.qcLevel.toString());
  }

  ngOnInit() {

    this.transactionId = sessionStorage.getItem('transactionId');
    this.qcLevel = parseInt(sessionStorage.getItem('qcLevel'));
    this.deleleteSesionData();
  }

  onClickView(index) {
    let docName = this.docName;
    let newValue = $("#myTextArea" + index).val();
    let pageNumber = $("#pageNumber" + index).val();
    let newChunk = $("#extractedChunk" + index).val();
    let findText = newChunk + "##" + pageNumber;

    $("#findInput").val(findText);
    $("#findHighlightAll").prop("checked", false);
    $('#findHighlightAll').click();
  }

  onDocTypeSelect() {
    this.docName = this.singleselectedItems;
    this.getResultDataDocumentWise2(this.transactionId, this.qcLevel);
    this.getDocumentFromServer(this.docName, this.transactionId);
    let table = $('#myTable').DataTable();
    table.destroy();
   
  }

  onDocTypeUnselect(e) {
    this.resultList = [];
    let table = $('#myTable').DataTable();
    table.clear().draw();
    this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'hidden');
    const blob = new Blob([""], { type: 'text' });
    this.currentPdf = URL.createObjectURL(blob);
  }

  getResultDataDocumentWise(transactionId, qcLevel): Boolean {
    this.resultService.getResultDataDocumentWise(transactionId, qcLevel)
      .subscribe(
        data => {
          this.setDocumentListDropdown(data);
          this.resultList = data;
          $(() => {
            let table = $('#myTable').DataTable({
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
            });
            $("#myTable_paginate").css("font-size","14px");
          $("#myTable_info").css("font-size","14px");
          });
          this.setVerifyDocButton(this.docName);
          return true;
        }, error => {
          return false;
        });
    return true;
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

  ngAfterViewInit() {
    let  transactionStatus=sessionStorage.getItem('transactionStatus');
    if(transactionStatus=='VERIFIED'){
      $("#VerifyDocCheck").prop("checked",true);
    }
    this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'hidden');
    this.getResultDataDocumentWise(this.transactionId, this.qcLevel);

    $('#myTable').on('page.dt', () => {
      var table = $('#myTable').DataTable();
      var info = table.page.info();
      this.currentPage = info.page;
    });
  }


  verifyAttribute(index) {
    let docName = this.docName;
    let newValue = $("#myTextArea" + index).val();
    if (newValue == undefined) {
      newValue = $("#customizeList" + index).val();
    }
    let attributeName = $("#attributeName" + index).val();
    let appliedRule = $("#appliedRule" + index).val();
    let initialValue = $("#myTextAreaHidden" + index).html();
    let userName = sessionStorage.getItem('authenticateUser');
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    let txn_id = this.transactionId;
    let qc_level = this.qcLevel;
    let content = {
      "DocumentName": docName,
      "AttributeName": attributeName,
      "AppliedRule": appliedRule,
      "InitialValue": initialValue,
      "NewValue": newValue,
      "QcDoneBy": userName,
      "CompanyName": companyName,
      "TransactionId": txn_id,
      "QcLevel": qc_level
    }

    this.resultService.verifyAttribute(content)
      .subscribe(
        response => {

          $("#verifyDone" + index).html("Verified");
          $("#verifyDone" + index).css("color", "green");
          $("#btnVrify" + index).css("display", "none");
          //$("#ignoreResult"+index).prop("disabled",false);
          $("#ignoreResulttt" + index).removeAttr('disabled');
          if (response) {
            this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'hidden');
            this.changeDocument()
          }
        }, error => {
        });

  }

  verifyAllDocs() {
    let txn_id = this.transactionId;
    let qc_level = this.qcLevel;
    let docName = this.docName;
    let userName = sessionStorage.getItem('authenticateUser');
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    let content = {
      "DocumentName": docName,
      "QcDoneBy": userName,
      "QcLevel": qc_level,
      "CompanyName": companyName,
      "TransactionId": txn_id
    }
    this.resultService.verifyDocument(content)
      .subscribe(
        response => {

          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: docName + ' verified successfully.',
            enableBtn2: true,
          };

          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

          $(".verifyDone").html("Verified");
          $(".verifyDone").css("color", "green");
          $(".btnVrify").css("display", "none");
          this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'hidden');
          $('#myTable').DataTable().destroy();
          //this.getResultDataDocumentWise2(this.transactionId, this.qcLevel);
          this.changeDocument();
        }, error => {

        });
  }


  resetFormData(form) {
    form.resetForm();
  }

  setTextArea(id) {
    $("#" + id).css("height", "150px");
    $("#" + id).css("border", "1px solid #2B60DE");
  }

  onBlurMethod(id) {
    $("#" + id).css("height", "70px");
    $("#" + id).css("border", "none");
  }

  setDocumentListDropdown(data) {

    if ($("#VerifyDocCheck").is(":checked")) {
      this.setVerifyUnverifyDocs("VERIFIED", data);
    } else {
      this.setVerifyUnverifyDocs("UNVERIFIED", data);
    }
    this.docName = this.docList[0].documentName;
    this.singleselectedItems =this.docList[0].documentName;
  
    this.singledropdownSettings = {
      singleSelection: true,
      idField: 'documentName2',
      textField: 'documentName',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      allowSearchFilter: true,
      closeDropDownOnSelection: true
    };
    setTimeout(() => {
      this.getDocumentFromServer(this.docName, this.transactionId);
    }, 500);
  }

  setVerifyUnverifyDocs(qcStatus, data) {
    let list = [];
    for (let i = 0; i < data.length; i++) {
      if (data[i].qcStatus == qcStatus) {
        list.push({ 'documentName': data[i].documentName, 'documentName2': data[i].documentName });
      }
    }
    this.docList = list;
    if (list.length == 0) {
      this.renderer.setStyle(this.addAttributeTemp.nativeElement, 'visibility', 'hidden');
      const blob = new Blob([""], { type: 'text' });
      this.currentPdf = URL.createObjectURL(blob);
      setTimeout(() => {    //<<<---    using ()=> syntax
        $("#errorClose").click();
      }, 500);
    } else {
      this.renderer.setStyle(this.addAttributeTemp.nativeElement, 'visibility', 'visible');
    }
    this.docCount=this.docList.length;
  }


  getInitialValue(object) {
    if (object.applicationExtractedValue == undefined) {
      return object.qcValidation.newValue;
    } else {
      return object.applicationExtractedValue.initialValue;
    }
  }


  upadteQCLevel(index) {
    let docName = this.docName;
    //=$("#docName"+index).html();
    let newValue = $("#myTextArea" + index).val();
    let attributeName = $("#attributeName" + index).val();
    let appliedRule = $("#appliedRule" + index).val();
    let initialValue = $("#myTextAreaHidden" + index).html();
    let userName = sessionStorage.getItem('authenticateUser');
    let txn_id = this.transactionId;
    let qc_level = this.qcLevel;
    let ignoreResult = "NO";
    if ($("#ignoreResulttt" + index).is(":checked")) {
      ignoreResult = "YES";
    }

    let content = {
      "DocumentName": docName,
      "AttributeName": attributeName,
      "AppliedRule": appliedRule,
      "InitialValue": initialValue,
      "QcLevel": qc_level,
      "NewValue": newValue,
      "Status": "UNVERIFIED",
      "QcDoneBy": userName,
      "IgnoreResult": ignoreResult
    }
    this.resultService.upadteQCLevel(content, txn_id)
      .subscribe(
        response => {
          alertify.success('Record updated successfully.');

          $("#verifyDone" + index).html("Pending Verification");
          $("#verifyDone" + index).css("color", "red");
          $("#btnVrify" + index).css("display", "block");
          this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'visible');
          $("#ignoreResulttt" + index).removeAttr('disabled');
          this.checkVerifyCheckbox();

        }, error => {

        });
  }


  callFunction(object, index) {
    if (object.qcValidation == undefined) {
      return "UNVERIFIED";
    } else {
      return object.qcValidation.status;
    }

  }

  getDocumentFromServer(docName, txn_id) {
      this.resultService.getDocumentFromServer(docName, txn_id).subscribe(
      response => {
        this.saveFile(response.body, docName);
       
      }, error => {

      });
  }

  saveFile(data: any, filename?: string) {
    const blob = new Blob([data], { type: 'application/octet-stream' });
    this.currentPdf = URL.createObjectURL(blob);
  }

  deleleteSesionData() {
    sessionStorage.removeItem('transactionId');
    sessionStorage.removeItem('qcLevel');
  }

  clearAttributeData(attributeName) {
    $("#attributeNameForAddValue").html(attributeName);
    this.attributeData.attributeName = attributeName;
  }

  addValue(f) {
    this.attributeData.transactionId = this.transactionId;
    this.attributeData.qcLevel = this.qcLevel;
    this.attributeData.documentName = this.docName;
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
        this.getResultDataDocumentWise2(this.transactionId, this.qcLevel);
        $('#myModalForAddValue').modal('hide');
      }, error => {
      });
    f.resetForm();
  }

  checkAttributValueAvailable() {
    for (let i = 0; i < this.resultList.length; i++) {
      if (this.resultList[i].documentName == this.attributeData.documentName) {
        for (let j = 0; j < this.resultList[i].attributes.length; j++) {
          if (this.attributeData.attributeName == this.resultList[i].attributes[j].attributeName) {
            let value = "";
            if (this.resultList[i].attributes[j].applicationExtractedValue == undefined) {
              value = this.resultList[i].attributes[j].qcValidation.newValue;
            } else {
              value = this.resultList[i].attributes[j].applicationExtractedValue.initialValue;
            }

            if (value == this.attributeData.attributeValue) {
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

  addAttribute(form) {
    if (this.singleselectedItems.length == 0) {
      alertify.warning("Please select document first..!");
      return false;
    }
    this.attributeData.qcDoneBy = sessionStorage.getItem('authenticateUser');
    let txnId = this.transactionId;
    let qcLevel = this.qcLevel;
    this.attributeData.documentName = this.docName;
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
          if ($("#VerifyDocCheck").is(":checked") == false) {
            $('#myTable').DataTable().destroy();
            this.getResultDataDocumentWise2(this.transactionId, this.qcLevel);
          }

          $('#modalForAddAttribute').modal('hide');
          this.checkVerifyCheckbox();
        }, error => {

        });

    form.resetForm();
  }

  setVerifyDocButton(documentName) {
    for (let i = 0; i < this.resultList.length; i++) {
      if (this.resultList[i].documentName == documentName) {
        if (this.resultList[i].qcStatus == "UNVERIFIED") {
          this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'visible');
        } else {
          this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'hidden');
        }
      }
    }
  }

  ignoreResult(index) {
    let documentName = this.docName;
    let attributeName = $("#attributeName" + index).val();
    let ignoreResult = "NO";
    if ($("#ignoreResulttt" + index).is(":checked")) {
      ignoreResult = "YES";
    }
    let attributeValue = $("#myTextArea" + index).val();
    if (attributeValue == undefined) {
      attributeValue = $("#customizeList" + index).val();
    }

    let content = {
      "transactionId": this.transactionId,
      "qcLevel": this.qcLevel,
      "documentName": documentName,
      "attributeName": attributeName,
      "ignoreResult": ignoreResult,
      "attributeValue": attributeValue
    };
    this.resultService.ignoreResult(content)
      .subscribe(
        response => {
          alertify.success("Record saved for " + attributeName + "  successfully....!");
          $('#myTable').DataTable().destroy();
          this.getResultDataDocumentWise2(this.transactionId, this.qcLevel);
        }, error => {

        });
  }

  getResultDataDocumentWise2(transactionId, qcLevel): Boolean {
    this.resultService.getResultDataDocumentWise(transactionId, qcLevel)
      .subscribe(
        data => {
          this.resultList = data;
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

          });
          this.setVerifyDocButton(this.docName);
          return true;
        }, error => {
          return false;
        });
    return true;
  }

  onSubmit(f) {

  }

  changeCustemValue(index) {
    let docName = this.docName;
    //=$("#docName"+index).html();
    let newValue = $("#customizeList" + index).val();
    let attributeName = $("#attributeName" + index).val();
    let appliedRule = $("#appliedRule" + index).val();
    let initialValue = $("#myTextAreaHidden" + index).html();
    let userName = sessionStorage.getItem('authenticateUser');
    let txnId = this.transactionId;
    let qcLevel = this.qcLevel;
    let ignoreResult = "NO";
    if ($("#ignoreResulttt" + index).is(":checked")) {
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
        alertify.success('Record updated successfully.');

        $("#verifyDone" + index).html("Pending Verification");
        $("#verifyDone" + index).css("color", "red");
        $("#btnVrify" + index).css("display", "block");
        this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'visible');
        $("#ignoreResulttt" + index).removeAttr('disabled');
        this.checkVerifyCheckbox();
      },
      error => {
      });
  }

  checkAttributAvailable() {
    for (let i = 0; i < this.resultList.length; i++) {
      if (this.resultList[i].documentName == this.attributeData.documentName) {
        for (let j = 0; j < this.resultList[i].attributes.length; j++) {
          if (this.attributeData.attributeName.toLowerCase() == this.resultList[i].attributes[j].attributeName.toLowerCase()) {

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

  changeDocument() {
    this.resultList = [];
    this.docList = [];
    this.singleselectedItems = "";
    $('#myTable').DataTable().destroy();
    this.getResultDataDocumentWise(this.transactionId, this.qcLevel);
  }


  checkVerifyCheckbox() {
    if ($("#VerifyDocCheck").is(":checked")) {
      this.resultList = [];
      this.docList = [];
      this.singleselectedItems = "";
      $('#myTable').DataTable().destroy();
      this.getResultDataDocumentWise(this.transactionId, this.qcLevel);
      this.renderer.setStyle(this.btnVrifyAll.nativeElement, 'visibility', 'hidden');
    }
  }



viewRuleSet(ruleId,attributeName){
      if(ruleId==0 || ruleId==-1){
        this.renderer.setProperty(this.viewRuleSetContentTemp.nativeElement, 'innerHTML', 'no rule');
        this.fallbackValue="N/A";
      }
      else{
            this.resultService.getRuleById(ruleId).subscribe(
              response => {
               let rule= this.getFormatedRule(response,attributeName);
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
  this.merge=rule.merge;
  this.isIgnore=rule.ignoreCase;
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
