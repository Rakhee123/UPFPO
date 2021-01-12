import { AfterViewInit, Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import 'datatables.net';
import * as $ from 'jquery';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { AUTHENTICATE_USER, BasicAuthenticationService } from '../../service/basic-authentication.service';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { CompanydataService, COMPANY_NAME } from '../../service/data/companydata.service';
import { PermissionService } from '../../service/data/permission.service';
import { UserInfo, UserinfoService } from '../../service/data/userinfo.service';
import { UserroleService } from '../../service/data/userrole.service';


export const COMPANY_ID = 'companyId';

export class Company {
  constructor(
    public companyId: number,
    public companyName: string,
    public companyAddress: string,
    public contactPerson: string,
    public contactPhone: string,
    public outputdateFormat: string,
    public mfa: number,
    public numberOfQcLevels: number,
    public isDeleted: number,
    public createdBy: string,
    public creationDate: Date,
    public lastModifiedBy: string,
    public lastModifiedDate: Date,
    public companyStatus: number

  ) { }
}

@Component({
  templateUrl: './companylist.component.html',
  styleUrls: ['./companylist.component.css']
})
export class CompanylistComponent implements AfterViewInit {

  companies: Company[];
  companyInfo = [];
  subtitle: string;
  modalReference: any;
  closeResult: string;
  companyId: number;
  otp: any
  OtpId: string
  SessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  currentCompanyName: string;
  showSwitchButtn = false;
  showAddCompanyButtn = false;
  showDeleteCompanyButtn = false;
  userInfo: UserInfo[]
  companyName: string;
  singledropdownSettings = {};
  closeDropdownSelection = true;
  continueButton = true;
  showUserListButton = false;
  showUpdateCompanyButton= false;

  constructor(private router: Router,
    private companyService: CompanydataService,
    private modalService: NgbModal,
    private userService: UserinfoService,
    private basicAuthService: BasicAuthenticationService,
    private userRoleService: UserroleService,
    private permissionService: PermissionService,
    private toastr: ToastrService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    private checkPermissionService: CheckPermissionService) {
  }

  ngOnInit() {
    this.refreshCompany(this.SessionCompanyName);
    this.refreshSwitchcompanyList(this.SessionCompanyName);
  }

  onCompanySelect(e3) {
    if (e3 == null) {
      this.continueButton = true;
    }
    else {
      this.continueButton = false;
    }
    this.companyId = e3.companyId;
  }
  onCompanyUnselect(event) {
    this.continueButton = true;
    delete this.companyId;
  }
  onOtpModalClose(otp2){
    this.companyName = "";
    this.otp="";
    this.modalService.dismissAll(otp2);
  }

  refreshSwitchcompanyList(sessionCompanyName) {
    this.userService.getUserCompanyInfoByUserName(sessionStorage.getItem(AUTHENTICATE_USER)).subscribe(
      response => {
        this.companyInfo = [];
        for (var c in response) {
          if (sessionCompanyName !== response[c].company.companyName) {
            this.companyInfo.push(response[c].company);
          }
        }
        this.singledropdownSettings = {
          singleSelection: true,
          idField: 'companyId',
          textField: 'companyName',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          itemsShowLimit: 1,
          allowSearchFilter: true,
          closeDropDownOnSelection: true
        };
      })
  }
  refreshCompany(sessionCompanyName) {
    this.SessionCompanyName = sessionCompanyName;
    this.userRoleService.getUserRoleByUsernameAndCompanyId(sessionStorage.getItem(AUTHENTICATE_USER), sessionCompanyName).subscribe(
      urResponse => {
        let permissionListString: string = urResponse['permissionList'];
        this.permissionService.getPermissionByPermissionId(permissionListString.toString()).subscribe(
          permissionData => {
            for (var p in permissionData) {
              if (permissionData[p]['permissionName'] == "MANAGE SUPER PERMISSIONS") {
                //this.showAddCompanyButtn = true;
                this.companyService.getAllCompanyListById().subscribe(
                  response1 => {
                    this.companies = response1;
                    $(function () {
                      $('#myTable').DataTable({
                        "retrieve": true,
                        "paging": true,
                        "searching": true,
                        "pageLength": 5,
                        "lengthChange": false,
                        "order": [],   /* Disable initial sort */
                        "dom": '<"pull-left"f><"pull-right"l>tip'
                      });
                      $("#myTable_paginate").css("font-size","14px");
                      $("#myTable_info").css("font-size","14px");
                    });
                  })
              }
              if (permissionData[p]['permissionName'] == "COMPANY CREATE") {
                this.showAddCompanyButtn = true;
              }
              if (permissionData[p]['permissionName'] == "COMPANY DELETE") {
                this.showDeleteCompanyButtn = true;
              }
              if (permissionData[p]['permissionName'] == "USER VIEW") {
                this.showUserListButton = true;
              }
              if (permissionData[p]['permissionName'] == "COMPANY UPDATE") {
                this.showUpdateCompanyButton = true;
              }
            }
          });
        this.companies = [urResponse.company];
      });
    this.userService.getUserCompanyInfoByUserName(this.basicAuthService.getAuthenteUser()).subscribe(
      response => {
        let companyCount = 0;
        for (var c in response) {
          companyCount = companyCount + 1;
        } if (companyCount > 1) {
          this.showSwitchButtn = true;
        } else {
          this.showSwitchButtn = false;
        }
      })
  }

  onConfigConfirm(id, name) {
    this.router.navigate(['dashboard/userlist', id, name]);
  }

  onDeleteCompany(id, name) {
    let initialState = {
      title: 'Delete Company',
      btn1: 'No',
      btn2: 'Yes',
      body: 'Are you sure you want to delete this company?',
      enableBtn: true,
      enableBtn2: true,
    };

    this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    this.bsModalRef.content.onClose.subscribe(result => {
      if (result) {
        this.companyService.deleteCompany(id).subscribe(
          response => {
            $.fn.dataTable.ext.errMode = 'none';
            let initialState = {
              title: 'Information',
              btn2: 'Ok',
              body: `${name} deleted successfully.`,
              enableBtn2: true,
            };

            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));

            this.refreshCompany(this.SessionCompanyName);
          })
      }
    })

  }

  onUpdateConfirm(id) {
    this.checkPermissionService.getUserRolePermission(this.SessionCompanyName);
    this.router.navigate(['dashboard/company', id]);
  }

  AddNewCompany() {
    this.router.navigate(['dashboard/company', -1]);
  }
  openSwitchCompany(company1) {
    this.modalService.open(company1, { ariaLabelledBy: 'modal-basic-title' });
  }
  closePopup(selectCompanyForm: NgForm, company1) {
    this.modalService.dismissAll(selectCompanyForm);
    delete this.companyId;
    this.companyName = "";
    this.continueButton = true;
  }

  selectedCompany(selectCompanyForm: NgForm, otp2) {
    console.log(selectCompanyForm.value["companyInfo"])
    if (selectCompanyForm.value["companyInfo"] == "") {
      this.continueButton = false;
    }
    this.companyService.getCompanyByIdForCurrentCompanyName(this.companyId).subscribe(
      response => {
        this.logOutBySwitchCompany(response['companyId'].toString());
        if (response['mfa'] == 0) {

          sessionStorage.setItem(COMPANY_NAME, response['companyName']);
          sessionStorage.setItem(COMPANY_ID, response['companyId'].toString());
          this.modalService.dismissAll();
          this.refreshCompany(sessionStorage.getItem(COMPANY_NAME));
          this.refreshSwitchcompanyList(sessionStorage.getItem(COMPANY_NAME));
          this.companyName = "";
          //this.navigation.setName(sessionStorage.getItem(COMPANY_NAME));
          document.getElementById('companyNameId').innerHTML = "Company-" + sessionStorage.getItem(COMPANY_NAME);
          this.toastr.success('Company switched successfully.')

        }
        else {
          sessionStorage.setItem(COMPANY_ID, response['companyId'].toString());
          this.currentCompanyName = response['companyName'];
          this.userService.generateOtp(this.basicAuthService.getAuthenteUser()).subscribe(
            responseotp => {
              this.toastr.info('OTP generated successfully')
              this.OtpId = responseotp;
            })
          this.modalService.dismissAll();
          this.modalService.open(otp2, { ariaLabelledBy: 'modal-basic-title' })
        }

      })
  }
  validateOtp(otpForm: NgForm) {
    this.userService.validateOtp(this.OtpId, otpForm.value.otp).subscribe(
      response => {
        if (response) {
          $.fn.dataTable.ext.errMode = 'none';
          this.otp = '';
          this.modalService.dismissAll();
          sessionStorage.setItem(COMPANY_NAME, otpForm.value.currentCompanyName);
          this.refreshCompany(sessionStorage.getItem(COMPANY_NAME));
          this.refreshSwitchcompanyList(sessionStorage.getItem(COMPANY_NAME));
          this.companyName = "";
          document.getElementById('companyNameId').innerHTML = "Company-" + sessionStorage.getItem(COMPANY_NAME);
          this.toastr.success('Company switched successfully.');
        }
        else {
          let initialState = {
            title: 'Information',
            btn2: 'Ok',
            body: `Entered OTP is incorrect.`,
            enableBtn2: true,
          };

          this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
        }
      })
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  ngAfterViewInit() { }

  logOutBySwitchCompany(companyId) {
    let userEmail = sessionStorage.getItem('authenticateUser');
    let sessionId = sessionStorage.getItem('sessionId');

    const content = {
      "email": userEmail,
      "logoutMethod": "switchCompany",
      "sessionId": sessionId,
      "companyId": companyId
    };


    this.companyService.logoutUserBySwitchCompany(content).subscribe(
      data => {
        sessionStorage.removeItem('sessionId');
        sessionStorage.removeItem('token');
        sessionStorage.removeItem(sessionStorage.getItem(COMPANY_NAME));
        sessionStorage.removeItem(sessionStorage.getItem(COMPANY_ID));

        sessionStorage.setItem('token', `${data.result.token}`);
        sessionStorage.setItem('sessionId', `${data.result.sessionId}`);

      },
      error => {
        console.log(error);
      });
  }
}