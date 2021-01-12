import { DatePipe } from '@angular/common';
import { Component, ElementRef, OnInit, QueryList, Renderer2, ViewChild, ViewChildren, ViewContainerRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal, NgbTabChangeEvent, NgbTabset, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import 'datatables.net';
import * as fileSaver from 'file-saver'; // npm i --save file-saver
import * as $ from 'jquery';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { CompanydataService, COMPANY_ID } from '../../service/data/companydata.service';
import { COMPANY_NAME, ResultService } from '../../service/data/result.service';
import { RulecreationService } from '../../service/data/rulecreation.service';
import { TransactionService } from '../../service/data/transaction.service';
import { UserInfo, UserinfoService } from '../../service/data/userinfo.service';
import { Rule } from '../rule-creation/rule-creation.component';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { throwMatDialogContentAlreadyAttachedError } from '@angular/material';
import { S_IFDIR } from 'constants';
import { UserroleService } from '../../service/data/userrole.service';
import { PermissionService } from '../../service/data/permission.service';
import { AUTHENTICATE_USER } from '../../service/basic-authentication.service';
import { data } from './result-data-table';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css'],
  providers: [DatePipe]
})

export class ResultComponent implements OnInit {
  @ViewChildren('txnTemp') txnTempList: QueryList<ElementRef>;
  @ViewChildren('rowLevelTemp') rowLevelTempList: QueryList<ElementRef>;
  docType = [];
  ruleset = [];
  attribute = [];
  docid = [];
  documentTypeId: number;
  Transactions = [];
  users: UserInfo[];
  QcLevelInCompany: number;
  QcLevelInCompanyArr = [];
  userId: number;
  message: string
  company_id = sessionStorage.getItem(COMPANY_ID);

  seletTransactionId: string;
  seletQcLevel: number;
  transactionStatus:string;
  qcid: number
  singleselectedItems = [];
  singleselectedItems1 = [];
  singleselectedItems2 = [];
  dropdownSettings = {};
  singledropdownSettings = {};
  singledropdownSettings1 = {};
  singledropdownSettings2 = {};
  singledropdownSettings3 = {};
  closeDropdownSelection = true;

  currentJustify = 'start';
  currentOrientation = 'horizontal';

  fromdate: any;
  todate: any;
  error: any = { isError: false, errorMessage: '' };
  rule: Rule;
  ruleSetId: number;
  ruleTable: any;
  disable: boolean = true;
  selected: number = 1;
  maxDate = undefined;
  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  sessionUserName = sessionStorage.getItem(AUTHENTICATE_USER);
  disables = false;
  transactionExecute = false;
  public documentwise:any = 1;

  constructor(private transactionService: TransactionService,
    private modalService: NgbModal,
    private userService: UserinfoService,
    private companyService: CompanydataService,
    private resultService: ResultService,
    private router: Router,
    private rulecreationService: RulecreationService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    private renderer: Renderer2,
    private datePipe: DatePipe,
    private config: NgbDatepickerConfig,
    public checkPermissionService: CheckPermissionService,
    private userRoleService: UserroleService,
    private permissionService: PermissionService) {

    const current = new Date();
    this.maxDate = {
      year: current.getFullYear(),
      month: current.getMonth() + 1,
      day: current.getDate()
    };
  }

  ngOnInit() {
    let qcTabId = 1;
    this.qcid = 1;
    this.refreshUserlistList(this.company_id);
    this.refreshCompanyInfoById(this.company_id);
    this.refreshDocumentType();
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
      
  }

  onUserSelect(event, transactionId, qcLevel, index, status) {
    const selectEl = event.target;
    const userId = selectEl.options[selectEl.selectedIndex].getAttribute('data-user');
    let userName = selectEl.value
    this.userId = userId;

    this.updateAssingendUser(userName, transactionId, qcLevel, index, status);
  }

  onUserUnselect() {
    delete this.userId;
  }

  refreshUserlistList(companyId) {
    this.userService.getUserListByCompanyId(companyId).subscribe(
      response => {
        this.users = response;
        this.refreshTransactionList(this.qcid, sessionStorage.getItem(COMPANY_NAME));
      })
  }
  refreshCompanyInfoById(companyId) {
    this.companyService.getCompanyById(companyId).subscribe(
      response => {
        this.QcLevelInCompany = response.numberOfQcLevels;
        this.QcLevelInCompanyArr = new Array(this.QcLevelInCompany);
      })
  }
  openPopupForDeleteTransaction(deleteTransactnPopup) {
    this.modalService.open(deleteTransactnPopup, { ariaLabelledBy: 'modal-basic-title', size: 'lg' });
  }

  onExportConfirm(fullConsolDocumentPop, qclevel, transactionId) {
    this.seletQcLevel = qclevel;
    this.seletTransactionId = transactionId;
    this.modalService.open(fullConsolDocumentPop, { ariaLabelledBy: 'modal-basic-title', size: 'lg' });
  }

  deleteIndividualTransaction(transactionId) {
    
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    let userName = sessionStorage.getItem('authenticateUser');
    let content = {
      'transactionId': transactionId,
      'companyName': companyName,
      'transactionDeletedBy': userName
    };

    this.resultService.getUserQcByTxnIdAndQcLevel(transactionId,this.qcid).subscribe(
      response => {
        if(userName != response["createdBy"]){
          let initialState = {};
          initialState = {
            title: 'Delete Transaction',
            btn2: 'OK',
            body: "You can not delete another user's transaction",
            enableBtn2: true
          };
          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          return false;
        }
        else{
          let initialState = {
            title: 'Delete Transaction',
            btn1: 'No',
            btn2: 'Yes',
            body: 'You have selected to delete the transaction ' + '<b>' + transactionId + '</b>' + ' . This action will delete all the related information. Are you sure you want to proceed?',
            enableBtn: true,
            enableBtn2: true,
          };
      
          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          this.bsModalRef.content.onClose.subscribe(result => {
            if(result){
            this.resultService.deleteSingleTransaction(content).subscribe(
              response => {
                let initialState = {};
                initialState = {
                  title: 'Delete transaction',
                  btn2: 'OK',
                  body: 'Transaction ' + '<b>' + transactionId + '</b>' + ' is deleted successfully.',
                  enableBtn2: true
                };
                this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
                this.refreshUserlistList(this.company_id);
              }
            )
            }
            else{
              this.modalService.dismissAll();
            }
          })
        }
      }
    )
  }

  deleteTransactionCompanyWise() {
    let companyName = sessionStorage.getItem(COMPANY_NAME);
    let userName = sessionStorage.getItem('authenticateUser');

    let initialState = {
      title: 'Delete transaction(s) for company',
      btn1: 'No',
      btn2: 'Yes',
      body: 'You have selected to delete all the transactions in company ' + '<b>' + companyName + '</b>' + ' . This action will delete all the related information. Are you sure you want to proceed?',
      enableBtn: true,
      enableBtn2: true,
    };
    $('#date_wise').css("display", "none");
    let content = {
      'companyName': companyName,
      'transactionDeletedBy': userName
    };

    this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    this.bsModalRef.content.onClose.subscribe(result => {
      if(result){
      this.resultService.deleteTransactionForCompany(content).subscribe(
        response => {
          let initialState = {};
          initialState = {
            title: 'Delete transaction(s) company wise',
            btn2: 'OK',
            body: 'All the transactions from company ' + '<b>' + companyName + '</b>' + ' are deleted successfully.',
            enableBtn2: true
          };
          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          this.refreshUserlistList(this.company_id);
          this.modalService.dismissAll();
        }
      )
      }
      else{
        this.bsModalService.hide;
      }
    })
  }

  deleteTransactionDateWise(from, to) {

    let companyName = sessionStorage.getItem(COMPANY_NAME);
    let userName = sessionStorage.getItem('authenticateUser');

    let initialState = {
      title: 'Delete transaction(s) date wise',
      btn1: 'No',
      btn2: 'Yes',
      body: 'You have selected to delete all the transactions between ' + '<b>' + from + '</b>' + ' and ' + '<b>' + to + '</b>' + '. This action will delete all the related information. Are you sure you want to proceed?',
      enableBtn: true,
      enableBtn2: true,
    };
    $('#date_wise').css("display", "block");
    let content = {
      'companyName': companyName,
      'transactionDeletedBy': userName,
      'startDate': from,
      'endDate': to
    };

    this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    this.bsModalRef.content.onClose.subscribe(result => {
      if(result){
        this.resultService.deleteTransactionsDatewise(content).subscribe(
          response => {
            let initialState = {};
            initialState = {
              title: 'Delete transaction(s) company wise',
              btn2: 'OK',
              body: 'All Transactions from company ' + '<b>' + companyName + '</b>' + ' are deleted successfully.',
              enableBtn2: true
            };
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            this.refreshUserlistList(this.company_id);
            this.modalService.dismissAll();
          }
        )
      }
      else{
        this.bsModalService.hide;
      }
     
    })
  }

  exportFileConsoliAndDocument(fullConsol: NgForm) {
    this.modalService.dismissAll();
    console.log(fullConsol.value['transactionType']);
    let tranType = fullConsol.value['transactionType'];
    let qclvl = fullConsol.value['seletQcLevel'];
    let tranId = fullConsol.value['seletTransactionId'];

    if (tranType == 1) {
      this.resultService.exportFileDocWise(qclvl, tranId).subscribe(
        response => {
          this.saveFile(response.body, response.headers.get("Content-Disposition"));
        }
      )
    }
    if (tranType == 2) {
      this.resultService.exportFileConsolWise(qclvl, tranId).subscribe(
        response => {
          this.saveFile(response.body, response.headers.get("Content-Disposition"));
        }
      )
    }
    if (tranType == 3) {
      this.resultService.exportFileFullConsolWise(qclvl, tranId).subscribe(
        response => {
          this.saveFile(response.body, response.headers.get("Content-Disposition"));
        }
      )
    }
  }

  saveFile(data: any, filename?: string) {
    const blob = new Blob([data], { type: 'application/octet-stream' });
    fileSaver.saveAs(blob, filename);
  }

  private tabSet: ViewContainerRef;
  @ViewChild(NgbTabset) set content(content: ViewContainerRef) {
    this.tabSet = content;
  };

  fetchNews(event: any) {
    console.log(event.nextId);
    this.qcid = event.nextId;
    this.refreshUserlistList(this.company_id);
  }
  refreshTransactionList(qcId, companyName) {
    this.transactionService.getAllTransaction(qcId, companyName).subscribe(
      response => {
        this.Transactions = response;
        if (this.Transactions.length == 0) {
          $('#enableDisableButton').css("visibility", "hidden");
        } else {
          $('#enableDisableButton').css("visibility", "visible");
        }
        $(function () {
          $('#myTable').DataTable({
            "retrieve": true,
            "paging": true,
            "searching": true,
            "pageLength": 5,
            "lengthChange": false,
            "order": [],   /* Disable initial sort */
            "dom": '<"pull-left"f><"pull-right"l>tip',
          });
           $("#myTable_paginate").css("font-size","14px");
           $("#myTable_info").css("font-size","14px");
        });
        for (var i = 0; i < this.Transactions.length; i++) {
          var transformDate = this.datePipe.transform(this.Transactions[i].creationDate, "medium");
          this.Transactions[i].creationDate = transformDate;
        }
      });
    this.userRoleService.getUserRoleByUsernameAndCompanyId(sessionStorage.getItem(AUTHENTICATE_USER), this.sessionCompanyName).subscribe(
      urResponse => {
        let permissionListString: string = urResponse['permissionList'];
        this.permissionService.getPermissionByPermissionId(permissionListString.toString()).subscribe(
          permissionData => {
           
            for (var p in permissionData) {
              if (permissionData[p]['permissionName'] == "TRANSACTION EXECUTE") {
                this.transactionExecute = true;
              }
              if (permissionData[p]['permissionName'] == "TRANSACTION VIEW") {
                this.transactionExecute = true;
              }
            }
          })
      });
  }

  popForTranId(consolDocumentPop, qclevel, transactionId,transactionStatus) {
    if (this.checkPermissionService.transactionViewButton == true && this.checkPermissionService.transactionExecuteButton == false) {
      this.disables = true;
    }
    else if (this.checkPermissionService.transactionViewButton == true && this.checkPermissionService.transactionExecuteButton == true) {
      this.disables = false;
    }
    else if (this.checkPermissionService.transactionViewButton == false && this.checkPermissionService.transactionExecuteButton == false) {
      this.disables = true;
    }
    else if (this.checkPermissionService.transactionViewButton == false && this.checkPermissionService.transactionExecuteButton == true) {
      this.disables = false;
    }


    if (this.disables == true) {
      this.modalService.dismissAll(consolDocumentPop)
    }
    else if (this.disables == false) {
      this.seletQcLevel = qclevel;
      this.seletTransactionId = transactionId;
      this.transactionStatus=transactionStatus
      this.modalService.open(consolDocumentPop, { ariaLabelledBy: 'modal-basic-title' });
    }
  }

  openTrnsactionConsoliAndDocument(consolDocumentPop: NgForm) {
    if (consolDocumentPop.value['transactionType'] == undefined) {
      let initialState = {
        title: 'Information',
        btn2: 'Ok',
        body: 'please select either option.',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

      return false;
    }
    this.modalService.dismissAll();
    if (consolDocumentPop.value['transactionType'] == 1) {
      sessionStorage.setItem('transactionId', consolDocumentPop.value['seletTransactionId']);
      sessionStorage.setItem('qcLevel', consolDocumentPop.value['seletQcLevel']);
      sessionStorage.setItem('transactionStatus', this.transactionStatus);
      
      this.router.navigate(['dashboard/resultdocument']);
    }
    else {
      sessionStorage.setItem('transaction_id', consolDocumentPop.value['seletTransactionId']);
      sessionStorage.setItem('qc_level', consolDocumentPop.value['seletQcLevel']);
      this.router.navigate(['dashboard/resultconsolidated']);
    }
  }
  public beforeChange($event: NgbTabChangeEvent) {
    if ($event.nextId === 'tab-preventchange2') {
      $event.preventDefault();
    }
  }

  selectedCompany(form: NgForm, opt: any, from: any, to: any, modalDelete: NgbModal) {
    if (form.value['delteTransactn'] == 1) {
      this.deleteTransactionCompanyWise();
    } else if (form.value['delteTransactn'] == 3) {
      var frm = new Date(from.year, from.month, from.day);
      var t = new Date(to.year, to.month, to.day);

      if (frm > t) {
        let initialState = {
          title: 'Information',
          btn2: 'Ok',
          body: 'End date is smaller than from date.Please select valid date.',
          enableBtn2: true,
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

      }
      else {
        // From date
        from.month = from.month - 1;
        var frm = new Date(from.year, from.month, from.day);
        var datePipes = new DatePipe('en-us');
        from = datePipes.transform(frm, 'yyyy-MM-dd');

        // to date
        to.month = to.month - 1;
        var t = new Date(to.year, to.month, to.day);
        var datePipes = new DatePipe('en-us');
        to = datePipes.transform(t, 'yyyy-MM-dd');

        this.deleteTransactionDateWise(from, to);
      }
    }
  }
  
  onCompanyWiseSelect(){
    $('#date_wise').css("display", "none");
  }

  onDateWiseSelect() {
    $('#rule_wise').css("display", "none");
    $('#date_wise').css("display", "block");
  }


  onDocTypeSelect(e3) {
    this.rule.documentTypeId = e3.documentTypeId;
  }

  onDocTypeUnselect() {
    delete this.rule.documentTypeId;
  }

  onAttributeSelect(e2) {
    this.rule.attributeId = e2.attributeId;
  }

  onAttributeUnSelect() {
    delete this.rule.attributeId;
  }


  onRuleSetSelect(e) {
    this.ruleSetId = e.ruleSetId;
  }

  onRuleSetUnSelect() {
    delete this.ruleSetId;
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

    this.rulecreationService.getRuleList().subscribe(
      response => {
        for (let res of response) {
          this.docid.push(res.documentTypeId);
        }
      }, error => {

      });
  }

  ngAfterViewInit() { }

  onSubmit() {
  }

  updateAssingendUser(userName, transactionId, qcLevel, index, status) {
    this.resultService.updateAssingedUser(userName, transactionId, qcLevel).subscribe(
      response => {
        if (userName == "") {
          $('#myTable').DataTable().destroy();
         // this.refreshUserlistList(sessionStorage.getItem(COMPANY_NAME));
         this.refreshUserlistList(this.company_id);

          return false;
        }
        if (status == 'VERIFIED') {
          let id = 'tranIdcs' + index;
          console.log(this.txnTempList.find(el => el.nativeElement.id === id));
          $("#" + id).empty();
          const p = this.renderer.createElement('p');
          const text = this.renderer.createText(transactionId);
          this.renderer.appendChild(p, text);
          this.renderer.addClass(p, 'tranIdcs');
          this.renderer.appendChild(this.txnTempList.find(el => el.nativeElement.id === id).nativeElement, p);
          this.renderer.setStyle(this.rowLevelTempList.find(el => el.nativeElement.id === "rowLevel" + index).nativeElement, 'background-color', '#f3f3f3');
        }
        error => {

        };
      })
  }
}
