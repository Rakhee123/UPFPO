import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { FullComponent } from './layouts/full/full.component';
import { BlankComponent } from './layouts/blank/blank.component';
import { LoginComponent } from './authentication/login/login.component';
import { RouteGuardService } from './service/route-guard.service';
import { ErrorpageComponent } from './errorpage/errorpage.component';
import { GlobalModalComponent } from './global-modal/global-modal.component';
import { RouterTestingModule } from '@angular/router/testing';

export const routes: Routes = [
  {
    path: '',
    component: FullComponent,
    children: [
      // { path: '', redirectTo: '/dashboard/dashboard1', pathMatch: 'full' },
      { path: '', redirectTo: '/authentication/login', pathMatch: 'full', canActivate: [RouteGuardService] },
      {
        path: 'dashboard',
        loadChildren: './dashboards/dashboard.module#DashboardModule'
      },
      {
        path: 'index',
        loadChildren: './starter/starter.module#StarterModule',
        canActivate: [RouteGuardService]
      },
      {
        path: 'component',
        loadChildren: './component/component.module#ComponentsModule',
        canActivate: [RouteGuardService]
      },
      { path: 'icons', loadChildren: './icons/icons.module#IconsModule', canActivate: [RouteGuardService] },
      { path: 'forms', loadChildren: './form/forms.module#FormModule', canActivate: [RouteGuardService] },
      { path: 'tables', loadChildren: './table/tables.module#TablesModule', canActivate: [RouteGuardService] },
      { path: 'charts', loadChildren: './charts/charts.module#ChartModule', canActivate: [RouteGuardService] },
      {
        path: 'widgets',
        loadChildren: './widgets/widgets.module#WidgetsModule',
        canActivate: [RouteGuardService]
      },
      {
        path: 'extra-component',
        loadChildren:
          './extra-component/extra-component.module#ExtraComponentModule',
        canActivate: [RouteGuardService]
      },
      { path: 'apps', loadChildren: './apps/apps.module#AppsModule', canActivate: [RouteGuardService] },
      {
        path: 'apps/email',
        loadChildren: './apps/email/mail.module#MailModule',
        canActivate: [RouteGuardService]
      },
      {
        path: 'sample-pages',
        loadChildren: './sample-pages/sample-pages.module#SamplePagesModule',
        canActivate: [RouteGuardService]
      }
    ]
  },
  {
    path: '',
    component: BlankComponent,
    children: [
      {
        path: 'authentication',
        loadChildren:
          './authentication/authentication.module#AuthenticationModule'
      }
    ]
  },
  {
    path:'errorpage',
    component: ErrorpageComponent,
    canActivate: [RouteGuardService]
  },
  {
    path:'global-modal',
    component: GlobalModalComponent,
    canActivate: [RouteGuardService]
  },
  { path: 'logout', redirectTo: '/authentication/login', canActivate: [RouteGuardService] },
  {
    path: '**',
    redirectTo: '404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes), NgbModule.forRoot()],
  exports: [RouterModule]
})
export class AppRoutingModule { }
