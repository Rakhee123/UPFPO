import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { BasicAuthenticationService } from '../basic-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class HttpIntercepterAuthService implements HttpInterceptor {

  constructor(private basicAuthService: BasicAuthenticationService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    // let username="admin"
    // let password="dummy"
    let basicAuthString = this.basicAuthService.getAuthenteToken();
    let username = this.basicAuthService.getAuthenteUser();

    if (basicAuthString && username) {
      request = request.clone({
        setHeaders: {
          Authorization: basicAuthString
        }
      })
    }
    return next.handle(request);
  }
}
