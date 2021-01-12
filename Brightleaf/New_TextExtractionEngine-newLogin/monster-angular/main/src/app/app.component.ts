import { Component } from '@angular/core';
import { environment } from './../environments/environment';
import { UserIdleService } from 'angular-user-idle';
import { BasicAuthenticationService } from './../app/service/basic-authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor( private userIdle: UserIdleService,private basicAuthService: BasicAuthenticationService) {
    // console.log(environment.production); // Logs false for default environment
  }
  title = 'app';

  ngOnInit() {
    if(sessionStorage.getItem('token')!==null)
    {
      // for page refresh
      this.basicAuthService.startUserIdle();
    }
  }
}
