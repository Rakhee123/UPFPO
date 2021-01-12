import { HttpEvent, 
    HttpInterceptor, 
    HttpHandler, 
    HttpRequest, 
    HttpResponse,
    HttpErrorResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router, ActivatedRoute } from '@angular/router';
import { Injectable, Output } from '@angular/core';
import { isThisSecond } from 'date-fns';
import { Component, EventEmitter} from '@angular/core';
import { ErrorInterceptService } from './error-intercept.service';

@Injectable()

export class HttpErrorInterceptor implements HttpInterceptor { 

constructor(private router : Router, private errorInterceptorService : ErrorInterceptService){}

intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
  
return next.handle(request)
  .pipe(
    catchError( (error: HttpErrorResponse) => { 
       let errMsg = '';
       // Client Side Error
       if (error instanceof ErrorEvent) {        
          errMsg = `Error: ${error.error.message}`;
        this.errorInterceptorService.notify.next(error); 
        this.router.navigate(['/errorpage']);     
       }     
       else {  
       // Server Side Error       
        errMsg = `Error Code: ${error.status},  Message: ${error.message}`; 
        this.errorInterceptorService.notify.next(error); 
        this.router.navigate(['/errorpage']);     
       }
       return throwError(errMsg);
     })
  )
    }
}   