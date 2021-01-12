import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserIdleService } from 'angular-user-idle';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { COMPANY_NAME, UserinfoService } from './data/userinfo.service';
import { GlobalModalComponent } from '../global-modal/global-modal.component';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

export class AuthenticationBean {
  constructor(public message: string) {
  }
}
export const TOKEN = 'token';
export const AUTHENTICATE_USER = 'authenticateUser';
export const SESSION_ID = 'sessionId';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthenticationService {
  flag: boolean = true;

  constructor(private http: HttpClient,
    private userService: UserinfoService,
    private userIdle: UserIdleService,
    private router: Router,
    private bsModalRef: BsModalRef,
    private bsModalService: BsModalService) { }

  executeJWTAuthenticationService(loginPayload) {
    return this.http.post<any>(
      `${environment.LOGIN_API_URL}/token/login`, loginPayload).pipe(
        map(
          data => {
            this.startUserIdle();
            sessionStorage.setItem(AUTHENTICATE_USER, loginPayload.username);
            sessionStorage.setItem(TOKEN, `${data.result.token}`);
            sessionStorage.setItem(SESSION_ID, `${data.result.sessionId}`);
            return data;
          }
        )
      );

  }

  getAuthenteUser() {
    return sessionStorage.getItem(AUTHENTICATE_USER);
  }

  getAuthenteToken() {
    if (this.getAuthenteUser())
      return sessionStorage.getItem(TOKEN);
  }


  isUserLoggedIn() {
    let user = sessionStorage.getItem(AUTHENTICATE_USER);
    return !(user == null);

  }

  logoutauto() {
    let userEmail = sessionStorage.getItem(AUTHENTICATE_USER);
    let sessionId = sessionStorage.getItem(SESSION_ID);

    const autoLogout = {
      "email": userEmail,
      "logoutMethod": "sessionTimeOut",
      "sessionId": sessionId
    };
    return this.http.post(`${environment.LOGIN_API_URL}/logoutUser`, autoLogout, { responseType: 'text' });

  }

  logout() {
    sessionStorage.removeItem(AUTHENTICATE_USER);
    sessionStorage.removeItem(TOKEN);
    sessionStorage.removeItem(sessionStorage.getItem(COMPANY_NAME));
    sessionStorage.removeItem(SESSION_ID);
  }

  startUserIdle() {
    //Start watching for user inactivity.
    this.userIdle.startWatching();

    this.userIdle.onTimerStart().subscribe(count => {
      var eventList = ["click", "mouseover", "keydown", "DOMMouseScroll", "mousewheel",
        "mousedown", "touchstart", "touchmove", "scroll", "keyup"];
      for (let event of eventList) {
        document.body.addEventListener(event, () => this.userIdle.resetTimer());
      }
    });

    // Start watch when time is up.
    this.flag = true;
    this.userIdle.onTimeout().subscribe(() => {
      if (this.flag === true) {
        this.logoutauto().subscribe(
          data => {
            let initialState = {
              title: 'Information',
              btn2: 'Ok',
              body: 'Session time out due to user inactivity!!!!',
              enableBtn2: true,
            };
        
            this.bsModalRef = this.bsModalService.show(GlobalModalComponent, Object.assign({ backdrop: true, ignoreBackdropClick: true, keyboard: false }, { initialState }));
            
            this.userIdle.stopWatching();
            this.userIdle.stopTimer();
            this.router.navigate(['/authentication/login']);
            this.logout();

          },
          error => {
            console.log(error);           
          });

        this.flag = false;
      }
    });

  }  // end of 

}
