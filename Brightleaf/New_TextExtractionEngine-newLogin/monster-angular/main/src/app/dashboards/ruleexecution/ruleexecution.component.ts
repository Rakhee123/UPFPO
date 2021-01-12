import { Component, OnInit, Input, ViewChild,ViewContainerRef } from '@angular/core';
import { NgbTabTitle, NgbTab, NgbTabset, NgbTabChangeEvent,NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { DocumentService, DocumentType } from '../../service/data/document.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { NgForm, FormBuilder, FormGroup } from '@angular/forms';
import { Alert } from 'selenium-webdriver';
import { FileuploadService } from '../../service/fileupload.service';
import { DocexecutionService } from '../../service/data/docexecution.service';
import { COMPANY_ID } from '../../service/data/companydata.service';
import { forEach } from '@angular/router/src/utils/collection';
import { AUTHENTICATE_USER } from '../../service/hardcoded-authentication.service';
import { NgxNavigationWithDataComponent } from 'ngx-navigation-with-data';
import { Router } from '@angular/router';
import { error } from 'util';


@Component({
  selector: 'app-ruleexecution',
  templateUrl: './ruleexecution.component.html',
  styleUrls: ['./ruleexecution.component.css'],
  providers: [NgbTabset]
})
export class RuleexecutionComponent implements OnInit {
  subtitle: string;
  documents: DocumentType[];
  ruleset: any;
  setng: NgbTabset;
  currentJustify = 'justified';
  nextId: String;
  currentOrientation = 'horizontal';
  addressForm: any;
  tabSet: any;
  activeTab = "tab1";
  tabId: string;
  model: any = {};
  filename: String;
  isDisabled: boolean = true;
  pdf: any;
  isEnabled: boolean = true;
  tbset: NgbTabset;
  testfile: any;
  upFileList = [];
  fileList = [];

  configobj: {};
  selectedFiles: FileList;
  fileName: any;
  doclist: any;
  docTypeId: any;
  rulesetId: any;
  exeFuction: any;
  transIdentifier: any;
  transactionDiv: boolean = false;
  transactionErrorDiv: boolean = false;
  transactionId: string="";
  seletQcLevel: number;
  seletTransactionId: string;
  executionTime: string="";
  public loading = false;
  isLoading = false;
  public documentwise:any = 1;

  public beforeChange($event: NgbTabChangeEvent) {
    if ($event.nextId === 'tab-selectbyid3') {
      $event.preventDefault();
    }
  }
  upFileForm: FormGroup;
  constructor(
    private documentService: DocumentService,
    private tabset: NgbTabset,
    private fb: FormBuilder,
    private fileService: FileuploadService,
    private docExecuteService: DocexecutionService,
    private modalService: NgbModal,
    public router2: NgxNavigationWithDataComponent,
    private router: Router,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService) {

    this.subtitle = 'This is rule execution block.';

  }

  ngOnInit() {
    this.upFileForm = this.fb.group({
      name: [''],
      upFiles: ['']
    });
    this.loaddocumnets();
  }

  onSelectFile(event) {

    this.selectedFiles = event.target.files;
    
    if (this.selectedFiles.length != 0) {
      this.isDisabled = false;
      $("#tab-selectbyid1").attr("class", "nav-link active");
      $("#tab-selectbyid2").attr("class", "nav-link");
      $("#tab-selectbyid2").attr("aria-expanded", "true");
      $("#tab-selectbyid2").attr("aria-controls", "tab-selectbyid2-panel");
      $("tab-selectbyid2").click();

    }

  }

  loaddocumnets() {
    this.documentService.getAllDocuments().subscribe(
      result => {
        
        this.documents = result;
      })
  }

  public onSubmit(form: NgForm) {
    const formData = new FormData();
  }

  changedocumnettype(documentTypeId: any) {
    if(documentTypeId =="0: undefined"){
      delete this.ruleset;
      return false;
    }
    else{
    this.documentService.getRuleSetByDocumnetId(documentTypeId).subscribe(
      response => {
        this.ruleset = response;
      })
    }
  }
  selectNextTab($event: NgbTabChangeEvent) {

    if ($event.nextId === 'tab-selectbyid2') {
      if ($event.activeId) {
        this.isDisabled = false;
        this.transactionErrorDiv=false;
        this.transactionDiv=false;
      }
    }
    if($event.nextId ==="tab-selectbyid3"){
      this.transactionErrorDiv=false;
      this.transactionDiv=false;
    }
  }

  openExecuteTab(form: NgForm) {
    if ($("docType").val() != "" && $("rulesetNm").val() != "" && form.valid) {
      this.isEnabled = false;
      $("#tab-selectbyid1").attr("class", "nav-link");
      $("#tab-selectbyid2").attr("class", "nav-link");
      $("#tab-selectbyid3").attr("class", "nav-link active");
      $("#tab-selectbyid3").attr("aria-expanded", "true");
      $("#tab-selectbyid3").attr("aria-controls", "tab-selectbyid3-panel");
      $("tab-selectbyid3").click();
      
      this.docTypeId = form.value.docType;
      this.rulesetId = form.value.rulesetNm;
      this.transIdentifier = form.value.tsName;
    }
  }
  testing(rf:any) {
    let company_id = sessionStorage.getItem(COMPANY_ID);
    let userName = sessionStorage.getItem(AUTHENTICATE_USER);
    if(this.transIdentifier==null){
      this.transIdentifier=undefined;
    }
 
    if (this.selectedFiles.length == 0 || this.docTypeId == 0 || this.rulesetId == 0) {
      let initialState = {
        title: 'Rule Execution',
        btn2: 'Ok',
        body: 'Please select files, document type and rule set before execution',
        enableBtn2: true
      };
      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      
    } else {
    for (var i = 0; i < this.selectedFiles.length; i++) {
      this.fileList.push(this.selectedFiles.item(i).name);
    };
    this.loading = true;
    this.isLoading = true;
    this.docExecuteService.docExecutionByFileAndRule(this.selectedFiles, userName, company_id, this.docTypeId, this.rulesetId, this.transIdentifier).subscribe(
      response => {
        this.doclist = response;
        this.transactionDiv=true;
        var str = JSON.stringify(response);
        var id = str.split("\\\"")[1];
        this.transactionId=id;
        var index = str.lastIndexOf(" ");
        var index1 = str.lastIndexOf("\"");
        this.executionTime=str.substring(index, index1).trim();
        if(this.transactionId=="" || this.transactionId==undefined)
        {
          this.transactionErrorDiv=true;
          this.transactionDiv=false;
        }else{
          this.transactionDiv=true;
          this.transactionErrorDiv=false;
        }
        //this.model.tsName="";
        this.ruleset=[];
        this.loaddocumnets();
        $("#fileUpload").val(null);
        rf.resetForm();
        this.docTypeId = 0;
        this.rulesetId = 0;
         //rf.reset();
         this.loading = false;
         this.isLoading = false;
        
      },
      error=>{
        this.transactionErrorDiv=true;
        this.transactionDiv=false;
        this.loading = false;
        this.isLoading = false;
      }
      )
    }
  }

  popForTranId(consolDocumentPop, qclevel, transactionId) {
  
     this.seletQcLevel = qclevel;
     this.seletTransactionId = transactionId;
    this.modalService.open(consolDocumentPop, { ariaLabelledBy: 'modal-basic-title' });
  }

  openTrnsactionConsoliAndDocument(consolDocumentPop: NgForm) {
    if (consolDocumentPop.value['transactionType'] == undefined) {
      return false;
    }
    this.modalService.dismissAll();
    if (consolDocumentPop.value['transactionType'] == 1) {
      // this.router2.navigate('dashboard/resultdocument', {"transactionId":consolDocumentPop.value['seletTransactionId'],
      //                                 "qcLevel":consolDocumentPop.value['seletQcLevel']});
     
      sessionStorage.setItem('transactionId', consolDocumentPop.value['seletTransactionId']);
      sessionStorage.setItem('qcLevel', consolDocumentPop.value['seletQcLevel']);
      this.router.navigate(['dashboard/resultdocument']);
    }
    else {
      sessionStorage.setItem('transaction_id', consolDocumentPop.value['seletTransactionId']);
      sessionStorage.setItem('qc_level', consolDocumentPop.value['seletQcLevel']);
      this.router.navigate(['dashboard/resultconsolidated']);
    }
  }

  private tabSet2: ViewContainerRef;
  @ViewChild(NgbTabset) set content(content: ViewContainerRef) {
    this.tabSet2 = content;
  };


}


