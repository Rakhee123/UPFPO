import { Component, AfterViewInit, OnInit } from '@angular/core';
import { NgbTabChangeEvent, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserinfoService, UserInfo, COMPANY_NAME } from '../service/data/userinfo.service';
import { BasicAuthenticationService,AUTHENTICATE_USER } from '../service/basic-authentication.service';
import { Company } from '../dashboards/companylist/companylist.component';
import { PermissionService } from '../service/data/permission.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {Base64} from 'base64-js';

@Component({
  templateUrl: './starter.component.html',
  styleUrls: ['./starter.component.css']
})
export class StarterComponent implements AfterViewInit,OnInit{
  subtitle: string;
  userinfo : UserInfo[];
  userInfo : UserInfo;
  userId: number;
  company : Company[];
  currentPassword:string;
  assignPermission=[];
  showEditProfilePage = false;
  hidePersonalDetail = true;

  newPassword: string
  confirmPassword: string
  encodePassword: string
  loginCurntPassword:string

  errmessage=false;
  message:string
  changepswd =[];
  genderValues = [{ id: 1, name: 'Male' }, { id: 2, name: 'Female' }];
  
  constructor(private userInfoService:UserinfoService,
    private permissionService :PermissionService,
    private basicAuthenticService:BasicAuthenticationService,    
    private modalService: NgbModal,
    private toastr: ToastrService,
    private router: Router,
    private userservice: UserinfoService) {
    this.subtitle = 'This is some text within a card block.';
  }


  AuthentiUsername = this.basicAuthenticService.getAuthenteUser();
  SessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  ngAfterViewInit() {}

  ngOnInit() {
    this.userInfo = new UserInfo(this.userId,'', '', '', '', '', '', '', '', '', '', new Date(), '', new Date());
    this.refreshUserInfo(this.AuthentiUsername);
  }

  refreshUserInfo(username){
    
    this.userInfoService.getUserCompanyInfoByUserName(username).subscribe(
      response=>{
        
         let resSTR = JSON.stringify(response);
         let jsonRes = JSON.parse(resSTR);
         this.company = jsonRes;
        
        for(let res of jsonRes){
          if(this.SessionCompanyName===res.company.companyName && this.AuthentiUsername===res.userInfo.userName)
          {
         
          this.userId = res.userInfo.userId;
          this.currentPassword = res.userInfo.userPassword;
         
           this.userinfo = [res];
           
           
           this.permissionService.getPermissionByPermissionId(res['permissionList']).subscribe(
            responsePer=>{
            for(let permission of responsePer){
              this.assignPermission.push(permission.permissionName);
            }
           
          })
          }
          else{
           //this.company = [res];
           //console.log("this.company ======== "+JSON.stringify(this.company));
          }

          
       }
        
      }
    )
  }
  editProfile(){
    this.showEditProfilePage = true;
    this.hidePersonalDetail = false;
  }
  cancelEditProfile(){
    this.showEditProfilePage = false;
    this.hidePersonalDetail = true;
   this.refreshUserInfo(this.AuthentiUsername);
  }
  updateUserProfile(profileForm :NgForm){
    this.userInfo.setuserId(this.userId);
    this.userInfo.setuserName(this.AuthentiUsername);
    this.userInfo.setUserPassword(this.currentPassword);
    this.userInfo.setGender(profileForm.value['gender']);
    this.userInfo.setUserTelephone(profileForm.value['userTelephone']);
    this.userInfo.setfirstName(profileForm.value['firstName']);
    this.userInfo.setlastName(profileForm.value['lastName']);
    this.userInfo.setmiddleName(profileForm.value['middleName']);
    this.userInfo.setuserAddress(profileForm.value['userAddress']);
    
    
    this.userInfoService.updateUserProfile(this.userInfo,this.AuthentiUsername).subscribe(
      response=>{
        this.showEditProfilePage = false;
        this.hidePersonalDetail = true;
      }
    )
  }
  openChangePasswordPopup(changePassword){
    this.modalService.open(changePassword, { ariaLabelledBy: 'modal-basic-title' });
  }
  onPopupClose(changePasswordForm:NgForm,changePassword){
    this.loginCurntPassword="";
    this.newPassword="";
    this.confirmPassword="";
    this.modalService.dismissAll(changePassword);
    this.errmessage = false;
  }

  saveChangePassword(PasswordForm: NgForm) {
    if (PasswordForm.value != null) {
      if (PasswordForm.value['newPassword']  == PasswordForm.value['confirmPassword']) {
      
        //this.errmessage = false
        this.encodePassword = btoa(PasswordForm.value['newPassword']);
        const resetpassword = {
          username: this.AuthentiUsername,
          password: this.encodePassword
        }
        this.userservice.changePasswordForUser(resetpassword).subscribe(
          response => {
            
            if (response != null) {
              this.modalService.dismissAll();
              this.toastr.success('Your password updated successfully.')
              this.router.navigate(['authentication/login'])
            }
          })
          // error=>{this.modalService.dismissAll();
          //   this.toastr.success('Your password not updated.')}
      }
      else {
        this.errmessage = true;
        this.message = "your new password and confirm password is not matched";
      }
    }
  }

  checkCurrentPassword(){
    this.encodePassword = btoa(this.loginCurntPassword);
    const checkPassword = {
      username: this.AuthentiUsername,
      password: this.encodePassword
    }
   
    this.userservice.checkCurrentPasswordCorrect(checkPassword).subscribe(
      response => {
        if(response==true){
           
            this.errmessage = false;
                    }
        else {
            this.errmessage = true;
            this.message = "your current password is incorrect";
                    }})
  }
  currentJustify = 'start';
  currentOrientation = 'horizontal';

  public beforeChange($event: NgbTabChangeEvent) {
    if ($event.nextId === 'tab-preventchange2') {
      $event.preventDefault();
    }
  }
}
