import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import 'datatables.net';
import * as $ from 'jquery';
import { AUTHENTICATE_USER } from '../../service/basic-authentication.service';
import { UserInfo, UserinfoService, COMPANY_NAME } from '../../service/data/userinfo.service';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { CheckPermissionService } from '../../service/data/check-permission.service';

@Component({
  selector: 'app-userlist',
  templateUrl: './userlist.component.html',
  styleUrls: ['./userlist.component.css']
})
export class UserlistComponent implements OnInit {
  users: UserInfo[];
  companyId: number;
  companyName: string;
  sessionUserName = sessionStorage.getItem(AUTHENTICATE_USER);
  message: any;
  showbutton = false;
  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);

  constructor(private route: ActivatedRoute,
    private userservice: UserinfoService,
    private router: Router,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService,
    public checkPermissionService: CheckPermissionService) { }

  ngOnInit() {
    this.companyId = this.route.snapshot.params['comid'];
    this.companyName = this.route.snapshot.params['comname'];
    this.refreshUserList(this.companyId);
  }

  refreshUserList(comId) {

    this.userservice.getUserListByCompanyId(comId).subscribe(
      response => {
        this.users = response;
        $(function () {
          $('#myTable').DataTable({
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
      }
    )
    this.checkPermissionService.getUserRolePermission(this.sessionCompanyName);
  }

  onDeleteUser(comId, uId, uName) {
    let initialState = {
      title: 'Delete User',
      btn1: 'No',
      btn2: 'Yes',
      body: 'Are you sure you want to delete this user?',
      enableBtn: true,
      enableBtn2: true,
    };

    this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
    this.bsModalRef.content.onClose.subscribe(result => {
      if (result) {
        this.userservice.deleteUserByCompanyIdAndUserId(comId, uId).subscribe(
          response => {
            $.fn.dataTable.ext.errMode = 'none';
            let initialState = {
              title: 'Information',
              btn2: 'Ok',
              body: `${uName} deleted successfully. `,
              enableBtn2: true,
            };

            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            this.refreshUserList(comId);
          })
      }
    })
  }

  onUpdateConfirm(comId, comname, uId) {   
      this.router.navigate(['dashboard/user', uId, comId, comname]);  
  }

  AddNewUser(comId, comname) {
    this.router.navigate(['dashboard/user', -1, comId, comname]);
  }

  onConfigConfirm(uId) {
    //this.router.navigate(['dashboard/userlist',uId]);
  }

  ngAfterViewInit() { }

}
