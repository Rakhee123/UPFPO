import { Component, OnInit, Input } from '@angular/core';
import { AngularWaitBarrier } from 'blocking-proxy/built/lib/angular_wait_barrier';
import { ErrorInterceptService } from '../error-intercept.service';

@Component({
  selector: 'app-errorpage',
  templateUrl: './errorpage.component.html',
  styleUrls: ['./errorpage.component.css']
})
export class ErrorpageComponent  {
  
  public errorStatus : any;
  public errorMessage : any;
  constructor(private errorInterceptorService: ErrorInterceptService) { }

  ngOnInit() { 

    this.errorInterceptorService.onNotify().subscribe((result : any)=> {
          this.errorStatus = result.status;
         // this.errorMessage = result.message;
          if(this.errorStatus == 400){
            this.errorMessage = "Bad Request";
          }
          else if(this.errorStatus == 401){
            this.errorMessage = "Unauthorized";
          }
          else if(this.errorStatus == 403){
            this.errorMessage = "Forbidden";
          }
          else if(this.errorStatus == 404){
            this.errorMessage = "Page Not Found";
          }
          else if(this.errorStatus == 500){
            this.errorMessage = "Internal Server Error";
          }
          else if(this.errorStatus == 502){
            this.errorMessage = "Bad Gateway";
          }
          else if(this.errorStatus == 503){
            this.errorMessage = "Service Unavailable";
          }
          else if(this.errorStatus == 504){
            this.errorMessage = "Gateway Timeout";
          }
          else {
            this.errorMessage = "Something went wrong. Please contact your administrator";
          }

        })
      }
}
