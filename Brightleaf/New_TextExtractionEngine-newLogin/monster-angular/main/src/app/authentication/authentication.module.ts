import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { NotFoundComponent } from './404/not-found.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { NumberOnlyDirective } from './login/number-only.directive'
import { AuthenticationRoutes } from './authentication.routing';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { ToastrModule } from 'ngx-toastr';

@NgModule({
  imports: [
    CommonModule,
    FormsModule, 
    NgbModule,
    RouterModule.forChild(AuthenticationRoutes),
    NgMultiSelectDropDownModule.forRoot(),
    ToastrModule.forRoot()],
  declarations: [NotFoundComponent, LoginComponent, SignupComponent, NumberOnlyDirective]
})
export class AuthenticationModule {}
