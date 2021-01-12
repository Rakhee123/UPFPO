import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserinfoService } from '../../service/data/userinfo.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { GlobalModalComponent } from '../../global-modal/global-modal.component';

@Component({
  selector: 'app-createpassword',
  templateUrl: './createpassword.component.html',
  styleUrls: ['./createpassword.component.css']
})
export class CreatepasswordComponent implements OnInit {

  userName: string
  newPassword: string
  confirmPassword: string
  encodePassword: string

  constructor(private route: ActivatedRoute,
    private router: Router,
    private userservice: UserinfoService,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService) { }

  ngOnInit() {
    this.userName = this.route.snapshot.params['email'];
  }

  createNewPassword(createPassForm: NgForm) {
    if (createPassForm.value != null) {
      if (createPassForm.value['newPassword'] === createPassForm.value['confirmPassword']) {
        this.encodePassword = btoa(createPassForm.value['newPassword']);
        const resetpassword = {
          username: this.userName,
          password: this.encodePassword
        }
        this.userservice.changePasswordForUser(resetpassword).subscribe(
          response => {
            if (response != null) {
              let initialState = {
                title: 'Information',
                btn2: 'OK',
                body: 'Your password is created successfully.',
                enableBtn2: true
              };
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
              this.router.navigate(['authentication/login'])

            }
            else {
              let initialState = {
                title: 'Information',
                btn2: 'OK',
                body: 'Your password is not created.',
                enableBtn2: true
              };
              this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            }
          })
      }
      else {
        let initialState = {
          title: 'Information',
          btn2: 'OK',
          body: 'Your new password and confirm password do not match.',
          enableBtn2: true
        };
        this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
      }
    }
  }
}
