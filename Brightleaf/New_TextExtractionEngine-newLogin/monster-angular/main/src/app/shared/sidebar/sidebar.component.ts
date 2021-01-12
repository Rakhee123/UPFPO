import { Component, AfterViewInit, OnInit } from '@angular/core';
import { HardcodedAuthenticationService } from '../../service/hardcoded-authentication.service';
import { BasicAuthenticationService, AUTHENTICATE_USER } from '../../service/basic-authentication.service';
import { CheckPermissionService } from '../../service/data/check-permission.service';
import { COMPANY_NAME } from '../../service/data/userinfo.service';
import { UserroleService } from '../../service/data/userrole.service';
import { PermissionService } from '../../service/data/permission.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements AfterViewInit {
  // this is for the open close
  isActive = true;
  showMenu = '';
  showSubMenu = '';
  isUserLoggedIn: boolean = false;
  sessionCompanyName = sessionStorage.getItem(COMPANY_NAME);
  ruleCreateButton = false;
  ruleViewButton = false;
  reportViewButton = false;
 
  constructor(public service: HardcodedAuthenticationService,
              public basicAuthService:BasicAuthenticationService,
              public checkPermissionService : CheckPermissionService,
              private userRoleService: UserroleService,
              private permissionService: PermissionService,) { }
  ngOnInit() {
    this.isUserLoggedIn = this.service.isUserLoggedIn();
    this.userRoleService.getUserRoleByUsernameAndCompanyId(sessionStorage.getItem(AUTHENTICATE_USER), this.sessionCompanyName).subscribe(
      urResponse => {
        let permissionListString: string = urResponse['permissionList'];
        this.permissionService.getPermissionByPermissionId(permissionListString.toString()).subscribe(
          permissionData => {
            for (var p in permissionData) {
              if (permissionData[p]['permissionName'] == "RULE CREATE") {
                this.ruleCreateButton = true;
              }
              if (permissionData[p]['permissionName'] == "RULE VIEW") {
                this.ruleViewButton = true;
              }
              if (permissionData[p]['permissionName'] == "REPORT VIEW") {
                this.reportViewButton = true;
              }
            }
          })
        })
  }

  addExpandClass(element: any) {
    if (element === this.showMenu) {
      this.showMenu = '0';
    } else {
      this.showMenu = element;
    }
  }
  addActiveClass(element: any) {
    if (element === this.showSubMenu) {
      this.showSubMenu = '0';
    } else {
      this.showSubMenu = element;
    }
  }
  eventCalled() {
    this.isActive = !this.isActive;
  }
  // End open close
  ngAfterViewInit() {
    $(function() {
      $('.sidebartoggler').on('click', function() {
        if ($('body').hasClass('mini-sidebar')) {
          $('body').trigger('resize');
          $('.scroll-sidebar, .slimScrollDiv')
            .css('overflow', 'hidden')
            .parent()
            .css('overflow', 'visible');
          $('body').removeClass('mini-sidebar');
          $('.navbar-brand span').show();
          // $(".sidebartoggler i").addClass("ti-menu");
        } else {
          $('body').trigger('resize');
          $('.scroll-sidebar, .slimScrollDiv')
            .css('overflow-x', 'visible')
            .parent()
            .css('overflow', 'visible');
          $('body').addClass('mini-sidebar');
          $('.navbar-brand span').hide();
          // $(".sidebartoggler i").removeClass("ti-menu");
        }
      });
    });
  }
}
