import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { UserIdleService } from 'angular-user-idle';

export const TOKEN = 'token';
export const AUTHENTICATE_USER = 'authenticateUser';
export const SESSION_ID = 'sessionId';


@Injectable({
  providedIn: 'root'
})
export class HardcodedAuthenticationService {

  constructor(private http: HttpClient,private userIdle: UserIdleService) { }


  executeAuthenticationService(loginPayload) {
    let basicAuthString = 'Basic ' + window.btoa(loginPayload);

    let headers = new HttpHeaders({
      Authorization: basicAuthString
    })
    return this.http.get<any>(`${environment.LOGIN_API_URL}/basicauth`,
      { headers }).pipe(
        map(
          data => {
            sessionStorage.setItem(AUTHENTICATE_USER, loginPayload.username);
            sessionStorage.setItem(TOKEN, basicAuthString);
            return data;
          }
        )
      );

  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('authenticateUser');
    return !(user == null);

  }


  logout() {
    this.logoutUser().subscribe(
      data => {
        this.userIdle.stopWatching();
        this.userIdle.stopTimer();
        sessionStorage.removeItem(AUTHENTICATE_USER);
        sessionStorage.removeItem(TOKEN);
      },
      error => {
        console.log(error)
      });
  
  }

  logoutUser(){
    let userEmail = sessionStorage.getItem(AUTHENTICATE_USER);
    let sessionId = sessionStorage.getItem(SESSION_ID);
    const autoLogout ={"email":userEmail,
                      "logoutMethod":"userLogout",
                      "sessionId":sessionId};
    return this.http.post(`${environment.LOGIN_API_URL}/logoutUser`,autoLogout,{responseType: 'text'});

  }
}
