import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalDataSource } from 'ng2-smart-table';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { RuleSet, RulesetService } from '../../service/data/ruleset.service';
import * as tableData from './ruleset-data-table';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { COMPANY_NAME } from '../../service/data/userinfo.service';

@Component({
  selector: 'app-ruleset',
  templateUrl: './ruleset.component.html',
  styleUrls: ['./ruleset.component.css']
})
export class RulesetComponent implements OnInit {
  rulesets: RuleSet[];
  message: string
  messageDanger: string
  source: LocalDataSource;
  source2: LocalDataSource;

  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);

  constructor(private router: Router,
    private ruleSetService: RulesetService,
    private toastr: ToastrService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    private checkPermissionService: CheckPermissionService) {
  }
  settings = tableData.settings;

  ngOnInit() {
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    this.refreshRuleSetType()
  }
  refreshRuleSetType() {
    this.ruleSetService.getAllRuleSet().subscribe(
      response => {
        this.rulesets = response;
        this.source = new LocalDataSource(this.rulesets);
      }
    )
  }

  onDeleteConfirm(event) {
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
    if (this.checkPermissionService.ruleDeleteButton == false) {
      let initialState = {
        title: 'Rule Set',
        btn2: 'Ok',
        body: 'You do not have a permission to delete the rule set.',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      let initialState = {
        title: 'Delete Rule Set',
        btn1: 'No',
        btn2: 'Yes',
        body: 'Are you sure you want to delete this rule set?',
        enableBtn: true,
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      this.bsModalRef.content.onClose.subscribe(result => {
        if (result) {
          this.ruleSetService.deleteRuleSetByRsId(event.data.ruleSetId).subscribe(
            response => {
              if (response == null) {
                this.messageDanger = `This rule set can't be deleted, as it has associated rules.`;
                return false;
              }
              this.message = `Rule set deleted successfully `;
              event.confirm.resolve();
              let initialState = {};
              initialState = {
                title: 'Rule Set',
                btn2: 'OK',
                body: 'Rule set deleted successfully.',
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
        title: 'Rule Set',
        btn2: 'Ok',
        body: 'You do not have a permission to update the rule set.',
        enableBtn2: true,
      };

      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      if (event.newData.ruleSetName == "") {
        let initialState = {};
        initialState = {
          title: 'Rule Set',
          btn2: 'OK',
          body: 'Rule set name is required.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      }
      else{
          event.newData['lastModifiedBy'] = sessionStorage.getItem('authenticateUser');
          this.ruleSetService.UpdateRuleSetType(event.newData, event.data.ruleSetId).subscribe(
            response => {
              if (response != null) {
              event.confirm.resolve(event.newData);
              let initialState = {};
              initialState = {
                title: 'Rule Set',
                btn2: 'OK',
                body: 'Rule set updated successfully.',
                enableBtn2: true
              };
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            }
            else {
              let initialState = {
                title: 'Rule  Set',
                btn2: 'Ok',
                body: 'RuleSet already exists.',
                enableBtn2: true
              }
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            }
          }
          )
        }      
    }
  }

  onCreateConfirm(event) {
    if (event.newData.ruleSetName == "") {
      let initialState = {};
      initialState = {
        title: 'Rule Set',
        btn2: 'OK',
        body: 'Rule set name is required.',
        enableBtn2: true
      };
      this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    }
    else {
      event.newData['createdBy'] = sessionStorage.getItem('authenticateUser');
      this.ruleSetService.createRuleSet(event.newData).subscribe(
        response => {
          if (response != null) {
            event.confirm.resolve(event.newData);
            this.refreshRuleSetType();
            let initialState = {};
            initialState = {
              title: 'Rule Set',
              btn2: 'OK',
              body: 'Rule set created successfully.',
              enableBtn2: true
            };
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          } else {
            let initialState = {
              title: 'Rule  Set',
              btn2: 'Ok',
              body: 'RuleSet already exists.',
              enableBtn2: true
            }
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
          }
        },
        error => {
        })
    }
  }

  onSearch(query: string = '') {
    this.source.setFilter([
      // fields we want to include in the search
      {
        field: 'ruleSetName',
        search: query
      }
    ], false);

    if (query == "") {
      this.source = new LocalDataSource(this.rulesets);
    }
  }
  ngAfterViewInit() { }
}
