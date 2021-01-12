import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { AUTHENTICATE_USER } from '../../service/basic-authentication.service';
import { Permission, PermissionService } from '../../service/data/permission.service';
import { Role, RoleService } from '../../service/data/role.service';
import { UserInfo, UserinfoService } from '../../service/data/userinfo.service';
import { UserRole } from '../../service/data/userrole.service';
import { Company } from '../companylist/companylist.component';

@Component({
  selector: 'app-adduser',
  templateUrl: './adduser.component.html',
  styleUrls: ['./adduser.component.css']
})
export class AdduserComponent implements OnInit {

  checked = false;
  comid: number;
  comname: string;
  userId: number;
  userinfoId: number = -1;
  username: string;
  userRole: UserRole;
  userRoleId: number;
  encodePassword: string
  permissionlist = [];
  user: UserInfo;
  roles = [];
  permissions: Permission[]
  permission = []
  selectedPermissions: Permission[]
  role: Role;
  roleId: number;
  company: Company;
  currentpassword: string;
  hiddnRole = false;

  currentJustify = 'start';
  currentOrientation = 'horizontal';

  dropdownSettings = {};
  dropdownList = [];
  permissionId: number;
  isDisabled: boolean = false;
  // emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";
  genderValues = [{ id: "", name: 'Select Gender' },{ id: 'Male', name: 'Male' }, { id: "Female", name: 'Female' }];
  selectedGender = '';
  enable: boolean = true;

  constructor(private userService: UserinfoService,
    private route: ActivatedRoute,
    private router: Router,
    private roleService: RoleService,
    private permissionService: PermissionService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService) { }


  ngOnInit() {
    this.comid = this.route.snapshot.params['comid'];
    this.comname = this.route.snapshot.params['comname'];
    this.userId = this.route.snapshot.params['userid'];
    this.username = sessionStorage.getItem(AUTHENTICATE_USER);

    this.refreshFatchRole();
    this.refreshFatchPermission();

    this.user = new UserInfo(this.userId, '', '', '', '', '', '', '', '', '', '', new Date(), '', new Date());
    this.role = new Role(this.roleId, '', '');
    this.company = new Company(this.comid, '', '', '', '', '', 0, 1, 0, '', new Date(), '', new Date(), 0);
    this.userRole = new UserRole(this.userRoleId, this.user, this.role, this.company, '');
    if (this.userId != -1) {
      this.isDisabled = true;
      this.userService.getUserInfoByUserId(this.comid, this.userId).subscribe(
        response => {
          this.user = response['userInfo'];
          this.role = response['role'];
          this.currentpassword = this.user.userPassword;
          this.roleId = this.role.roleId;
          this.userRoleId = response['userRoleId'];
          this.selectedGender=response['userInfo'].gender;
          this.permissionService.getPermissionByPermissionId(response['permissionList'].toString()).subscribe(
            responseOfPermission => {
              this.permission = responseOfPermission;
            }

          )
        }
      )
    }
    else{
      this.getPermissionByRoleId(3);
    }

  }

  getPermissionByRoleId(roleid) {
    this.permissionService.getPermissionByRoleId(roleid).subscribe(
      response => {
        this.permission = response;
      }
    )
  }


  onPermissionSelect(e3) {
  }
  onPermissionUnselect(e) {
    delete this.permissionId;
  }
  refreshFatchPermission() {
    this.permissionService.getAllPermission().subscribe(
      response => {
        this.permissions = response;
        this.dropdownSettings = {
          singleSelection: false,
          idField: 'permissionId',
          textField: 'permissionName',
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          itemsShowLimit: 18,
          allowSearchFilter: true
        };
      }
    )
  }

  saveUser(userForm: NgForm) {
    for (let p in userForm.value['permissions']) {
      this.permissionlist.push(userForm.value['permissions'][p].permissionId);
    }
    this.userRole.userInfo.setuserId(userForm.value['hidnUserId']);
    this.userRole.userInfo.setUserPassword('');
    this.userRole.userInfo.setuserName(userForm.value['userName']);
    this.userRole.userInfo.setGender(userForm.value['gender']);
    this.userRole.userInfo.setUserTelephone(userForm.value['userTelephone']);
    this.userRole.userInfo.setfirstName(userForm.value['firstName']);
    this.userRole.userInfo.setlastName(userForm.value['lastName']);
    this.userRole.userInfo.setmiddleName(userForm.value['middleName']);
    this.userRole.userInfo.setuserAddress(userForm.value['userAddress']);
    this.userRole.userInfo.setuserEmail(userForm.value['userName']);
    this.userRole.setpermissionList(this.permissionlist.toString());
    if (userForm.value['hidnUserId'] == -1) {
      if (this.userId == -1) {
        this.userRole.userInfo.setCreatedBy(sessionStorage.getItem('authenticateUser'));
        this.userService.createUser(this.userRole, this.comid).subscribe(
          response => {
            let currentEmail = response['userInfo'].userName;
            this.createNewPasswordOnEmail(currentEmail, this.comname)
            this.router.navigate(['dashboard/userlist', this.comid, this.comname])
          }
        )
      }
      else {
        this.userRole.userInfo.setLastModifiedBy(sessionStorage.getItem('authenticateUser'));
        this.userRole.userInfo.setuserId(this.userId);
        this.userRole.role.setroleId(userForm.value['roleid'])
        this.userRole.userInfo.setUserPassword(this.currentpassword);
        this.userRole.setuserRoleId(this.userRoleId);
        let userEmail = this.user.userName;
        this.userRole.userInfo.setuserName(userEmail);
        this.userService.editUser(this.userId, this.comid, this.userRole).subscribe(
          response => {
            let initialState = {
              title: 'Update User',
              btn2: 'Ok',
              body: `User updated successfully.`,
              enableBtn2: true,
            };

            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            this.router.navigate(['dashboard/userlist', this.comid, this.comname])
          }
        )
      }
    }
    else {
      this.userRole.userInfo.setUserPassword(userForm.value['hidnPassword']);
      this.userService.createUser(this.userRole, this.comid).subscribe(
        response => {
          //let currentEmail = response['userInfo'].userName;
          //this.createNewPasswordOnEmail(currentEmail, this.comname)
          this.router.navigate(['dashboard/userlist', this.comid, this.comname])
        }
      )
    }
  }

  createNewPasswordOnEmail(email, currentCompany) {
    const newPassword = {
      username: email,
      currentcompany: currentCompany
    }
    this.userService.createPasswordSendEmail(newPassword).subscribe(
      response => {
      })

  }

  refreshFatchRole() {
    this.roleService.getAllRole().subscribe(
      response => {
        this.roles = response;
      })
  }
  resetUser(AddUserForm:NgForm)
  {
    AddUserForm.resetForm();
    $("#gender").prop("selected","false");
    $("#gender").val('');
  }

  emailCheck(email) {
    if (email != '' && email!=null) {
      this.userService.emailCheckByEmailAndComId(email, this.comid).subscribe(
        response => {
          if (response != null) {
            if (response.userId != null) {
              this.userinfoId = response.userId;
              this.user = response;
              this.enable = false;
            }
            else {
              this.userinfoId = -1;
              this.user = new UserInfo(this.userId, '', '', email, '', '', '', '', '', '', '', new Date(), '', new Date());
            }
          }
          else {
            let initialState = {
              title: 'Information',
              btn2: 'Ok',
              body: `User is already associated with this company.`,
              enableBtn2: true,
            };

            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            this.user = new UserInfo(null, '', '', '', '', '', '', '', '', '', '', new Date(), '', new Date());
          }
        }
      )
    }
  }
}
