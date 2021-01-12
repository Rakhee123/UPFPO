import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalDataSource } from 'ng2-smart-table';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { DocumentService, DocumentType } from '../../service/data/document.service';
import * as tableData from './document-data-table';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { COMPANY_NAME } from '../../service/data/userinfo.service';

@Component({
  selector: 'app-configure',
  templateUrl: './configure.component.html',
  styleUrls: ['./configure.component.css']
})
export class ConfigureComponent implements OnInit {

  documents: DocumentType[];
  message: string

  source: LocalDataSource;
  source2: LocalDataSource;
  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  loggedInUser = [];

  constructor(private router: Router, private documentService: DocumentService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    private checkPermissionService: CheckPermissionService) {
    this.source = new LocalDataSource(tableData.document); // create the source
  }
  settings = tableData.settings;

  ngOnInit() {
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    this.refreshDocumentType()
  }
  refreshDocumentType() {
    this.documentService.getAllDocuments().subscribe(
      response => {
        this.documents = response;
        this.source = new LocalDataSource(this.documents);
      }
    )
  }

  onDeleteConfirm(event) {
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    if (this.checkPermissionService.ruleDeleteButton == false) {
      let initialState = {
        title: 'Document Type',
        btn2: 'Ok',
        body: 'You do not have a permission to delete the document type.',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      let initialState = {
        title: 'Delete Document Type',
        btn1: 'No',
        btn2: 'Yes',
        body: 'Are you sure you want to delete this document type?',
        enableBtn: true,
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      this.bsModalRef.content.onClose.subscribe(result => {
        if (result) {
          this.documentService.deleteDocumentByDocId(event.data.documentTypeId).subscribe(
            response => {
              if(response!=null){
                event.confirm.resolve();
                let initialState = {};
                initialState = {
                  title: 'Document Type',
                  btn2: 'OK',
                  body: 'Document type deleted successfully.',
                  enableBtn2: true
                };
                this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));  
              } else {
                let initialState = {
                  title: 'Information',
                  btn2: 'Ok',
                  body: 'Document type is associated with rule. Can\'t be deleted',
                  enableBtn2: true
                };

                this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

              }
            }
          )
        }
      });
    }
  }

  onSaveConfirm(event) {
    this.loggedInUser = event.newData;
    this.loggedInUser["createdBy"] = sessionStorage.getItem('authenticateUser');

    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    if (this.checkPermissionService.ruleUpdateButton == false) {
      let initialState = {
        title: 'Document Type',
        btn2: 'Ok',
        body: 'You do not have a permission to update the document type.',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      if (event.newData.documentName == "") {
        let initialState = {};
        initialState = {
          title: 'Document Type',
          btn2: 'OK',
          body: 'Document name is required.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      }
      else{
      this.documentService.UpdateDocumentType(event.newData, event.data.documentTypeId).subscribe(
        response => {
          if (response != null) {
          event.confirm.resolve(event.newData);
          let initialState = {};
          initialState = {
            title: 'Document Type',
            btn2: 'OK',
            body: 'Document type updated successfully.',
            enableBtn2: true
          };
          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        }
        else {
          let initialState = {
            title: 'Document Type',
            btn2: 'Ok',
            body: 'Document Type already exists.',
            enableBtn2: true
          };
          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        }
      }
        )
      }
    }
  }

  onCreateConfirm(event) {
    this.loggedInUser = event.newData;
    this.loggedInUser["createdBy"] = sessionStorage.getItem('authenticateUser');

    if (event.newData.documentName == "") {
      let initialState = {};
      initialState = {
        title: 'Document Type',
        btn2: 'OK',
        body: 'Document name is required.',
        enableBtn2: true
      };
      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      this.documentService.createDocumentType(event.newData).subscribe(
        response => {
          if (response != null) {
            event.confirm.resolve(event.newData);
            this.refreshDocumentType()
            let initialState = {};
            initialState = {
              title: 'Document Type',
              btn2: 'OK',
              body: 'Document type created successfully.',
              enableBtn2: true
            };
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          } else {
            let initialState = {
              title: 'Document Type',
              btn2: 'Ok',
              body: 'Document Type already exists.',
              enableBtn2: true
            };
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          }
        })
    }
  }

  onSearch(query: string = '') {
    this.source.setFilter([
      // fields we want to include in the search
      {
        field: 'documentName',
        search: query
      },
      {
        field: 'documentDesc',
        search: query
      }
    ], false);

    if (query == "") {
      this.source = new LocalDataSource(this.documents);
    }
  }
  ngAfterViewInit() { }
}
