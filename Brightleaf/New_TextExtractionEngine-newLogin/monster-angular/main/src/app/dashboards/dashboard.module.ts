import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ChartistModule } from 'ng-chartist';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { ChartsModule } from 'ng2-charts';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { ToastrModule } from 'ngx-toastr';
import { ToastrComponent } from '../extra-component/toastr/toastr.component';
import { BasicComponent } from '../table/basic/basic.component';
import { AddcompanyComponent } from './addcompany/addcompany.component';
import { NumbersOnlyDirective } from './addcompany/numbers-only.directive';
import { AddconfigureComponent } from './addconfigure/addconfigure.component';
import { AddruletorulesetComponent } from './addruletoruleset/addruletoruleset.component';
import { AdduserComponent } from './adduser/adduser.component';
import { AlphabetsOnlyDirective } from './alphabets-only.directive';
import { AttributeComponent } from './attribute/attribute.component';
import { ChildComponent } from './child/child.component';
import { CompanylistComponent } from './companylist/companylist.component';
import { ConfigureComponent } from './configure/configure.component';
import { CreatepasswordComponent } from './createpassword/createpassword.component';
import { CustomisedAttributeComponent } from './customised-attribute/customised-attribute.component';
import { CustomerSupportComponent } from './dashboard-components/customer-support/cs.component';
import { FeedsComponent } from './dashboard-components/feeds/feeds.component';
import { IncomeCounterComponent } from './dashboard-components/income-counter/income-counter.component';
import { PageAnalyzerComponent } from './dashboard-components/page-analyzer/pa.component';
import { ProfileComponent } from './dashboard-components/profile/profile.component';
import { ProjectCounterComponent } from './dashboard-components/project-counter/project-counter.component';
import { ProjectComponent } from './dashboard-components/project/project.component';
import { RecentcommentComponent } from './dashboard-components/recent-comment/recent-comment.component';
import { RecentmessageComponent } from './dashboard-components/recent-message/recent-message.component';
import { SocialSliderComponent } from './dashboard-components/social-slider/social-slider.component';
import { TodoComponent } from './dashboard-components/to-do/todo.component';
import { TotalEarningComponent } from './dashboard-components/total-earnings/te.component';
import { WidgetComponent } from './dashboard-components/widget/widget.component';
import { DashboardRoutes } from './dashboard.routing';
import { ParentComponent } from './parent/parent.component';
import { ReportComponent } from './report/report.component';
import { ResetpasswordComponent } from './resetpassword/resetpassword.component';
import { IgnoreResultConsolidatedCheckPipe } from './result-consolidated/ignore.result.check.pipe';
import { IgnoreResultConsolidatedPipe } from './result-consolidated/ignore.result.pipe';
import { ResultConsolidatedComponent } from './result-consolidated/result-consolidated.component';
import { ResultconsolidatedDirective } from './result-consolidated/resultconsolidated.directive';
import { ResultDocumentComponent } from './result-document/result-document.component';
import { ResultComponent } from './result/result.component';
import { RuleCreationComponent } from './rule-creation/rule-creation.component';
import { RuleexecutionComponent } from './ruleexecution/ruleexecution.component';
import { RulesetComponent } from './ruleset/ruleset.component';
import { UserlistComponent } from './userlist/userlist.component';
import { NgxLoadingModule } from 'ngx-loading';


@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    NgbModule,
    ChartsModule,
    ChartistModule,
    RouterModule.forChild(DashboardRoutes),
    FormsModule,
    Ng2SmartTableModule,
    NgMultiSelectDropDownModule.forRoot(),
    NgxExtendedPdfViewerModule,
    ToastrModule.forRoot(),
    MatTabsModule,
    ReactiveFormsModule,
    NgxLoadingModule.forRoot({})
  ],
  declarations: [
    ConfigureComponent,
    IncomeCounterComponent,
    ProjectCounterComponent,
    ProjectComponent,
    RecentcommentComponent,
    RecentmessageComponent,
    SocialSliderComponent,
    TodoComponent,
    ProfileComponent,
    PageAnalyzerComponent,
    WidgetComponent,
    CustomerSupportComponent,
    TotalEarningComponent,
    FeedsComponent,
    BasicComponent,
    CompanylistComponent,
    AddcompanyComponent,
    UserlistComponent,
    AdduserComponent,
    AddconfigureComponent,
    AttributeComponent,
    RulesetComponent,
    ResetpasswordComponent,
    RuleCreationComponent,
    ToastrComponent,
    CreatepasswordComponent,
    RuleexecutionComponent,
    ResultDocumentComponent,
    ResultConsolidatedComponent,
    ResultComponent,
    NumbersOnlyDirective,
    AlphabetsOnlyDirective,
    ParentComponent,
    ChildComponent,
    ResultconsolidatedDirective,
    AddruletorulesetComponent,
    IgnoreResultConsolidatedPipe,
    IgnoreResultConsolidatedCheckPipe,
    CustomisedAttributeComponent,
    ReportComponent
  ],
  exports: [
    CustomisedAttributeComponent,
    IgnoreResultConsolidatedPipe,
    IgnoreResultConsolidatedCheckPipe

  ]
})
export class DashboardModule { }
