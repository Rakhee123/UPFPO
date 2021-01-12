import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserIdleModule } from 'angular-user-idle';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { BsModalRef, ModalModule } from 'ngx-bootstrap/modal';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { NgxNavigationWithDataComponent } from "ngx-navigation-with-data";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ErrorpageComponent } from './errorpage/errorpage.component';
import { GlobalModalComponent } from './global-modal/global-modal.component';
import { HttpErrorInterceptor } from './http-error-interceptor';
import { BlankComponent } from './layouts/blank/blank.component';
import { FullComponent } from './layouts/full/full.component';
import { HttpIntercepterAuthService } from './service/http/http-intercepter-auth.service';
import { BreadcrumbComponent } from './shared/breadcrumb/breadcrumb.component';
import { NavigationComponent } from './shared/header-navigation/navigation.component';
import { RightSidebarComponent } from './shared/right-sidebar/rightsidebar.component';
import { SIDEBAR_TOGGLE_DIRECTIVES } from './shared/sidebar.directive';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { SpinnerComponent } from './shared/spinner.component';
import {MultiselectComponent} from './form/multiselect/multiselect.component'
import * as bootstrap from "bootstrap";
import * as $ from "jquery";
import { NgxLoadingModule } from 'ngx-loading';


const routes: Routes = [

];
@NgModule({
  declarations: [
    AppComponent,
    SpinnerComponent,
    FullComponent,
    BlankComponent,
    NavigationComponent,
    BreadcrumbComponent,
    SidebarComponent,
    RightSidebarComponent,
    SIDEBAR_TOGGLE_DIRECTIVES,
    ErrorpageComponent,
    GlobalModalComponent,
    MultiselectComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    NgMultiSelectDropDownModule.forRoot(),
    HttpClientModule,
    AppRoutingModule,
    NgxExtendedPdfViewerModule,
    AppRoutingModule, UserIdleModule.forRoot({ idle: 500, timeout: 2, ping: 0 }),
    NgbModule.forRoot(),
    ModalModule.forRoot() ,
    RouterModule.forRoot(routes, { useHash: true }),
    NgxLoadingModule.forRoot({})
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: HttpIntercepterAuthService, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true },
    NgxNavigationWithDataComponent,BsModalRef],
  bootstrap: [AppComponent]
}) 
export class AppModule { }
