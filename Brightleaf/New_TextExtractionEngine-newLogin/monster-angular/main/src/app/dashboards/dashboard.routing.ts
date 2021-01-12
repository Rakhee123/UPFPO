import { Routes } from '@angular/router';
import { CompanylistComponent } from './companylist/companylist.component';
import { AddcompanyComponent } from './addcompany/addcompany.component';
import { ConfigureComponent } from './configure/configure.component';
import { RouteGuardService } from '../service/route-guard.service';
import { UserlistComponent } from './userlist/userlist.component';
import { AdduserComponent } from './adduser/adduser.component';
import { AddconfigureComponent } from './addconfigure/addconfigure.component';
import { AttributeComponent } from './attribute/attribute.component';
import { RulesetComponent } from './ruleset/ruleset.component';
import { ResetpasswordComponent } from './resetpassword/resetpassword.component';
import { RuleCreationComponent } from './rule-creation/rule-creation.component';
import { CreatepasswordComponent } from './createpassword/createpassword.component';
import { RuleexecutionComponent } from './ruleexecution/ruleexecution.component';
import { ResultDocumentComponent } from './result-document/result-document.component';
import { ResultConsolidatedComponent } from './result-consolidated/result-consolidated.component';
import { ResultComponent } from './result/result.component';

import { ParentComponent } from './parent/parent.component';
import { ChildComponent } from './child/child.component';
import { AddruletorulesetComponent } from './addruletoruleset/addruletoruleset.component';
import { CustomisedAttributeComponent } from './customised-attribute/customised-attribute.component';
import { ReportComponent } from './report/report.component';

export const DashboardRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'companylist',
        component: CompanylistComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Home', url: '/index' },
            { title: 'Dashboard' }
          ]
        },
        canActivate: [RouteGuardService]
      },



      {
        path: 'company/:comid',
        component: AddcompanyComponent,
        data: {
          title: 'Text Extraction Engine',
          //     urls: [
          //       { title: 'Home', url: '/index' },
          //       { title: 'dashboard' }
          //     ]
        },
        canActivate: [RouteGuardService]
      },
      {
        path: 'userlist/:comid/:comname',
        component: UserlistComponent,
        data: {
          title: 'Text Extraction Engine',
          //   urls: [
          //     { title: 'CompanyList', url: '/dashboard/companylist' },
          //     { title: 'Users' }
          //   ]
        },
        canActivate: [RouteGuardService]
      },
      {
        path: 'user/:userid/:comid/:comname',
        component: AdduserComponent,
        data: {
          title: 'Text Extraction Engine',
          //   urls: [
          //     { title: 'CompanyList', url: '/dashboard/companylist' },
          //     { title: 'Users' }
          //   ]
        },
        canActivate: [RouteGuardService]
      },
      {
        path: 'configure',
        component: ConfigureComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Home', url: '/index' },
            { title: 'Document Type List' }
          ]
        }, canActivate: [RouteGuardService]
      },
      {
        path: 'attribute',
        component: AttributeComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Home', url: '/index' },
            { title: 'Attribute List' }
          ]
        }, canActivate: [RouteGuardService]
      },
      {
        path: 'ruleset',
        component: RulesetComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Home', url: '/index' },
            { title: 'Rule Set List' }
          ]
        }, canActivate: [RouteGuardService]
      },
      {
        path: 'result',
        component: ResultComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Home', url: '/index' },
            { title: 'Transactions' }
          ]
        }, canActivate: [RouteGuardService]
      },
      {
        path: 'report',
        component: ReportComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Home', url: '/index' },
            { title: 'Report' }
          ]
        }, canActivate: [RouteGuardService]
      },
      {
        path: 'resetpassword/:email',
        component: ResetpasswordComponent,
        data: {
          title: 'Text Extraction Engine',
          // urls: [
          //   { title: 'Home', url: '/index' },
          //   { title: 'RuleSetList' }
          // ]
        }, canDeactivate: [RouteGuardService]
      },
      {
        path: 'createpassword/:email',
        component: CreatepasswordComponent,
        data: {
          title: 'Text Extraction Engine',
          // urls: [
          //   { title: 'Home', url: '/index' },
          //   { title: 'RuleSetList' }
          // ]
        }, canDeactivate: [RouteGuardService]
      },
      // {
      //   path: 'config/:conid',
      //   component: AddconfigureComponent,
      //   data: {
      //     title: 'Company',
      //     urls: [
      //       { title: 'Home', url: '/index' },
      //       { title: 'dashboard' }
      //     ]
      // },
      //   canActivate:[RouteGuardService]
      // },
      {
        path: 'ruleexecution',
        component: RuleexecutionComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Dashboard', url: '/dashboard/companylist' },
            { title: 'Execute Rules' }
          ]
        }
      },
      {
        path: 'resultdocument',
        component: ResultDocumentComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Result', url: '/dashboard/result' },
            { title: 'Result Document' }
          ]
        }
      },
      {
        path: 'resultconsolidated',
        component: ResultConsolidatedComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Result', url: '/dashboard/result' },
            { title: 'Result Consolidated' }
          ]
        },
        canActivate: [RouteGuardService]
      },
      {
        path: 'rulecreation',
        component: RuleCreationComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Dashboard', url: '/dashboard/companylist' },
            { title: 'Rule Creation' }
          ]
        },
        canActivate: [RouteGuardService]
      },
      {
        path: 'addruletoruleset',
        component: AddruletorulesetComponent,
        data: {
          title: 'Text Extraction Engine',
          urls: [
            { title: 'Dashboard', url: '/dashboard/companylist' },
            { title: 'Add Rules to Rule set' }
          ]
        },
        canActivate: [RouteGuardService]
      },
      {
        path: 'customised-attribute',
        component: CustomisedAttributeComponent,
        canActivate: [RouteGuardService]
      },
      {
        path: 'parent', component: ParentComponent, canActivate: [RouteGuardService]
      },
      {
        path: 'child', component: ChildComponent, canActivate: [RouteGuardService]
      }
    ]
  }
];
