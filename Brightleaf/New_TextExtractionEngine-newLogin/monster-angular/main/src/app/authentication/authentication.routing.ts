import { Routes } from '@angular/router';

import { NotFoundComponent } from './404/not-found.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';

export const AuthenticationRoutes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    // children: [
    //   {
    //     path: '404',
    //     component: NotFoundComponent
    //   },
    //   {
    //     path: 'login',
    //     component: LoginComponent
    //   },
    //   {
    //     path: 'signup',
    //     component: SignupComponent
    //   }
    // ]
  }
];
