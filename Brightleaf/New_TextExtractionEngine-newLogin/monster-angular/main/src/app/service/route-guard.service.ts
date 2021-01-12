import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { HardcodedAuthenticationService } from './hardcoded-authentication.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService implements CanActivate {
  deactivateFlag:boolean=false;
  constructor(private service: HardcodedAuthenticationService,
    private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.service.isUserLoggedIn())
    {
      return true;
    }else{
      if(this.deactivateFlag==false){
        this.router.navigate(['authentication/login']);
        //this.router.navigate(['authentication/login','resetpassword/:email','createpassword/:email']);
        return false;
      }
      this.deactivateFlag=false;
      return false;
    }
}

canDeactivate(){
  this.deactivateFlag=true;
  }
}
