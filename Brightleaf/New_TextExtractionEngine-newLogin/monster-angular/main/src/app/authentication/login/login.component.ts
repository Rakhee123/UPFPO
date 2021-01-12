import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { BasicAuthenticationService } from '../../service/basic-authentication.service';
import { CompanydataService, COMPANY_ID, COMPANY_NAME } from '../../service/data/companydata.service';
import { UserinfoService } from '../../service/data/userinfo.service';
import { HardcodedAuthenticationService } from '../../service/hardcoded-authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage = "Invalid Credentials"
  invalidLogin = false
  login = { username: '', password: '' }

  modalReference: any;
  closeResult: string;
  message: string;
  invalidMsg = false
  companyInfo = []
  companyName: string
  otp: string
  OtpId: string
  companyId: number
  singledropdownSettings = {};
  closeDropdownSelection = true;
  disableContinueButton = true;
  selectedObj = [];

  constructor(private router: Router,
    private service: HardcodedAuthenticationService,
    private basicAuthService: BasicAuthenticationService,
    private userService: UserinfoService,
    private modalService: NgbModal,
    private companyService: CompanydataService,
    private toastr: ToastrService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService) { }

  ngOnInit() {
  }
  //this.service.logout();
  onCompanySelect(e3) {
    if (e3 == null) {
      this.disableContinueButton = true;
    }
    else {
      this.disableContinueButton = false;
    }
    this.companyId = e3.companyId;

  }
  onCompanyUnselect(event) {
    this.selectedObj = event;
    this.disableContinueButton = true;
    delete this.companyId;
  }

  onModalClose(company1) {
    this.companyName = ""
    this.onCompanyUnselect(this.selectedObj)
    this.modalService.dismissAll(company1);
  }

  onOtpModalClose(otp2) {
    this.companyName = "";
    this.onCompanyUnselect(this.selectedObj);
    this.otp = "";
    this.modalService.dismissAll(otp2);
  }

  handleLoginWithJWTAuthentication(company1, otp2) {
    this.invalidMsg = false
    const loginPayload = {
      username: this.login.username,
      password: this.login.password
    }
    this.basicAuthService.executeJWTAuthenticationService(loginPayload)
      .subscribe(
        data => {
          this.userService.getUserCompanyInfoByUserName(this.basicAuthService.getAuthenteUser()).subscribe(
            response => {
              this.companyInfo = [];
              let companyCount = 0;
              for (var c in response) {
                companyCount = companyCount + 1;
              }
              if (companyCount > 1) {
                for (var c in response) {
                  this.companyInfo.push(response[c].company);
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
                this.modalService.open(company1, { ariaLabelledBy: 'modal-basic-title' });
              } else {
                sessionStorage.setItem(COMPANY_NAME, response[0].company.companyName)
                sessionStorage.setItem(COMPANY_ID, response[0].company.companyId)
                this.companyService.getSetCompanyById(response[0].company.companyId).subscribe(
                  response => {
                    if (response['mfa'] == 0) {
                      this.modalService.dismissAll();
                      this.router.navigate(['/index'])
                      this.invalidLogin = false
                    }
                    else {
                      this.userService.generateOtp(this.basicAuthService.getAuthenteUser()).subscribe(
                        responseotp => {
                          this.OtpId = responseotp;
                        })
                      this.modalService.dismissAll();
                      this.modalService.open(otp2, Object.assign({ backdrop: false, ariaLabelledBy: 'modal-basic-title', keyboard: false }));
                    }
                  })
              }
            })
        },
        error => {
          console.log(error)
          this.invalidLogin = true
        }
      );
  }

  selectedCompany(selectCompanyForm: NgForm, otp2) {
    console.log("selectedCompany");
    if (selectCompanyForm['companyInfo'] != undefined) {
      console.log("selectedCompany if");
      this.disableContinueButton = false;
    }
    console.log("selectedCompany else");
    this.companyService.getSetCompanyById(this.companyId).subscribe(
      response => {
        if (response['mfa'] == 0) {
          console.log("selectedCompany index");
          this.modalService.dismissAll();
          this.router.navigate(['/index'])
          this.invalidLogin = false
        }
        else {
          console.log("selectedCompany else");
          this.userService.generateOtp(this.basicAuthService.getAuthenteUser()).subscribe(
            responseotp => {
              this.OtpId = responseotp;
            })
          this.modalService.dismissAll();
          this.modalService.open(otp2, Object.assign({ backdrop: false, ariaLabelledBy: 'modal-basic-title', keyboard: false }));
        }
      })
  }
  validateOtp(otpForm: NgForm) {
    this.userService.validateOtp(this.OtpId, otpForm.value.otp).subscribe(
      response => {
        if (response) {
          this.modalService.dismissAll();
          this.router.navigate(['/index'])
          this.invalidMsg = false
        }
        else {
          this.invalidMsg = true
          this.message = "Entered OTP is incorrect";
          //this.toastr.success('Enter OTP is wrong.')
        }

      })
  }

  openFrgtPassModel(forgotpassword) {
    this.modalService.open(forgotpassword, { ariaLabelledBy: 'modal-basic-title' });
  }

  forgotPasswordOnEmail(frgtPassForm: NgForm) {
    this.userService.forgotPasswordSendEmail(frgtPassForm.value['email']).subscribe(
      response => {
        this.modalService.dismissAll();
        let initialState = {
          title: 'Information',
          btn2: 'Ok',
          body: 'Reset password link has been sent to  ' + '<b>' + '"' + frgtPassForm.value['email'] + '".' + '</b>',
          enableBtn2: true,
        };

        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      },
      error => {
        console.log(error)
      })
  }
}
