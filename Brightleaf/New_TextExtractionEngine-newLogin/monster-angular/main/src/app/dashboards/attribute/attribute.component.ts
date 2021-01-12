import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { LocalDataSource } from 'ng2-smart-table';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { Attribute, AttributeService } from '../../service/data/attribute.service';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { COMPANY_NAME } from '../../service/data/userinfo.service';
import * as tableData from './attribute-data-table';

@Component({
  selector: 'app-attribute',
  templateUrl: './attribute.component.html',
  styleUrls: ['./attribute.component.css']
})
export class AttributeComponent implements OnInit {

  attributes: Attribute[];
  message: string
  isDisabled: boolean = true;
  source: LocalDataSource;
  source2: LocalDataSource;
  customSource: LocalDataSource;

  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);

  @Output() disableCustomizedButton = new EventEmitter();

  constructor(private attributeService: AttributeService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    private checkPermissionService: CheckPermissionService) {
  }

  settings = tableData.settings;

  ngOnInit() {
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    this.refreshAttributeType()
  }
  refreshAttributeType() {
    this.attributeService.getAllAttribute().subscribe(
      response => {
        this.attributes = response;
        this.source = new LocalDataSource(this.attributes);
      }
    )
  }


  onDeleteConfirm(event) {
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    if (this.checkPermissionService.ruleDeleteButton == false) {
      let initialState = {
        title: 'Information',
        btn2: 'Ok',
        body: 'You do not have a permission to delete the attribute.',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      let initialState = {
        title: 'Delete Attribute',
        btn1: 'No',
        btn2: 'Yes',
        body: 'Are you sure you want to delete this attribute?',
        enableBtn: true,
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      this.bsModalRef.content.onClose.subscribe(result => {
        if (result) {        
          this.attributeService.deleteAtrributeByAttId(event.data.attributeId).subscribe(
            response => {
              if (response != null) {
                event.confirm.resolve();
                let initialState = {
                  title: 'Delete Attribute',
                  btn2: 'Ok',
                  body: 'Attribute deleted successfully.',
                  enableBtn2: true
                };
                this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
              } else {
                let initialState = {
                  title: 'Information',
                  btn2: 'Ok',
                  body: 'Attribute is associated with rule. Can\'t be deleted.',
                  enableBtn2: true
                };

                this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
              }
            },
            error => {
              let initialState = {
                title: 'Information',
                btn2: 'Ok',
                body: 'Attribute is associated with rule.',
                enableBtn2: true
              };

              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            }

          )
        } else {
          event.confirm.reject();
        }
      });
    }
  }

  onSaveConfirm(event) {
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    if (this.checkPermissionService.ruleUpdateButton == false) {
      let initialState = {
        title: 'Information',
        btn2: 'Ok',
        body: 'You do not have a permission to update the rule set.',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    } else {
      if (event.newData.attributeName == "") {
        let initialState = {};
        initialState = {
          title: 'Attribute',
          btn2: 'OK',
          body: 'Attribute name is required.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      } else {
        event.newData['lastModifiedBy'] = sessionStorage.getItem('authenticateUser');
        this.attributeService.UpdateAttributeType(event.newData, event.data.attributeId).subscribe(
          response => {
            if (response != null) {
              event.confirm.resolve(event.newData);
              let initialState = {
                title: 'Attribute',
                btn2: 'Ok',
                body: 'Attribute updated successfully.',
                enableBtn2: true
              };
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            } else {
              let initialState = {
                title: 'Attribute',
                btn2: 'Ok',
                body: 'Attribute already exists',
                enableBtn2: true
              };
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
              event.newData == {};
            }
          }
        )
      }
    }
  }

  onCreateConfirm(event) {
    if (event.newData.attributeName == "") {
      let initialState = {};
      initialState = {
        title: 'Information',
        btn2: 'OK',
        body: 'Attribute name required.',
        enableBtn2: true
      };
      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      event.newData['createdBy'] = sessionStorage.getItem('authenticateUser');
      this.attributeService.createAttributeType(event.newData).subscribe(
        response => {
          if (response != null) {
            event.confirm.resolve(event.newData);
            this.refreshAttributeType()
            let initialState = {
              title: 'Attribute',
              btn2: 'Ok',
              body: 'Attribute created successfully.',
              enableBtn2: true
            };

            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          } else {
            let initialState = {
              title: 'Attribute',
              btn2: 'Ok',
              body: 'Attribute already exists',
              enableBtn2: true
            };
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            event.newData == {};
          }
        }
      )
    }
  }

  onSearch(query: string = '') {
    this.source.setFilter([
      // fields we want to include in the search
      {
        field: 'attributeName',
        search: query
      },
      {
        field: 'attributeDesc',
        search: query
      },
      {
        field: 'attributeType',
        search: query
      },
      {
        field: 'paragraph',
        search: query
      },
      {
        field: 'customizedAttribute',
        search: query
      }
    ], false);

    if (query == "") {
      this.source = new LocalDataSource(this.attributes);
    }
  }

  ngAfterViewInit() { }
}
