import { Injectable } from '@angular/core';
import { CompanydataService, COMPANY_NAME } from './companydata.service';
import { UserroleService } from './userrole.service';
import { PermissionService } from './permission.service';
import { AUTHENTICATE_USER } from '../basic-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class CheckPermissionService {

  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  showAddUserButton = false;
  showUpdateUserButton = false;
  showUserListButton = false;
  showUpdateCompanyButton = false;
  ruleCreateButton = false;
  ruleViewButton = false;
  ruleUpdateButton = false;
  ruleDeleteButton = false;
  transactionDeleteButton = false;
  transactionViewButton = false;
  transactionExecuteButton = false;
  managePermission = false;

  constructor(private companyService: CompanydataService,
    private userRoleService: UserroleService,
    private permissionService: PermissionService,
  ) { }

  getUserRolePermission(sessionCompanyName) {
    this.sessionCompanyName = sessionCompanyName;
    this.userRoleService.getUserRoleByUsernameAndCompanyId(sessionStorage.getItem(AUTHENTICATE_USER), sessionCompanyName).subscribe(
      urResponse => {
        let permissionListString: string = urResponse['permissionList'];
        this.permissionService.getPermissionByPermissionId(permissionListString.toString()).subscribe(
          permissionData => {
           
            for (var p in permissionData) {
              if (permissionData[p]['permissionName'] == "USER CREATE") {
                this.showAddUserButton = true;
              }
              if (permissionData[p]['permissionName'] == "USER UPDATE") {
                this.showUpdateUserButton = true;
              }
              if (permissionData[p]['permissionName'] == "USER VIEW") {
                this.showUserListButton = true;
              }
              if (permissionData[p]['permissionName'] == "COMPANY UPDATE") {
                this.showUpdateCompanyButton = true;
              }
              if (permissionData[p]['permissionName'] == "RULE CREATE") {
                this.ruleCreateButton = true;
              }
              if (permissionData[p]['permissionName'] == "RULE VIEW") {
                this.ruleViewButton = true;
              }
              if (permissionData[p]['permissionName'] == "RULE UPDATE") {
                this.ruleUpdateButton = true;
              }
              if (permissionData[p]['permissionName'] == "RULE DELETE") {
                this.ruleDeleteButton = true;
              }
              if (permissionData[p]['permissionName'] == "TRANSACTION DELETE") {
                this.transactionDeleteButton = true;
              }
              if (permissionData[p]['permissionName'] == "TRANSACTION EXECUTE") {
                this.transactionExecuteButton = true;
              }
              if (permissionData[p]['permissionName'] == "TRANSACTION VIEW") {
                this.transactionViewButton = true;
              }
              if (permissionData[p]['permissionName'] == "MANAGE PERMISSIONS") {
                this.managePermission = true;
              }
            }
          })
      })
  }

}
