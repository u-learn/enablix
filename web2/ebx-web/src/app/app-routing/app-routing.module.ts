import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes} from '@angular/router';

import { CoreModule } from '../core/core.module';
import { AuthGuard } from './auth.guard';
import { LoginPageComponent } from '../login-page/login-page.component';
import { PageNotFoundComponent } from '../page-not-found/page-not-found.component';
import { LoginFormComponent } from '../login-page/login-form/login-form.component';
import { HomeComponent } from '../home/home.component';
import { NewContentEditComponent } from '../new-content-edit/new-content-edit.component';
import { BizContentComponent } from '../biz-content/biz-content.component';
import { BizContentListComponent } from '../biz-content/biz-content-list/biz-content-list.component';
import { ContentTemplateResolve } from './content-template.resolve';
import { ResourceVersionResolve } from './resource-version.resolve';
import { TenantResolve } from './tenant.resolve';
import { SearchBarResolve } from './search-bar.resolve';
import { ContentWFResolve } from './content-wf.resolve';
import { UserPreferenceResolve } from './user-prefs.resolve';
import { NavigationService } from './navigation.service';
import { BizDimensionListComponent } from '../biz-dimension/biz-dimension-list/biz-dimension-list.component';
import { BizDimensionDetailComponent } from '../biz-dimension/biz-dimension-detail/biz-dimension-detail.component';
import { BizDimensionComponent } from '../biz-dimension/biz-dimension.component';
import { ConsolidateContentComponent } from '../consolidate-content/consolidate-content.component';
import { MyDraftComponent } from '../consolidate-content/my-draft/my-draft.component';
import { ObsoleteContentComponent } from '../consolidate-content/obsolete-content/obsolete-content.component';
import { ContentRequestComponent } from '../consolidate-content/content-request/content-request.component';
import { FreetextSearchComponent } from '../freetext-search/freetext-search.component';
import { CompanyComponent } from '../company/company.component';
import { MembersComponent } from '../company/members/members.component';
import { IntegrationsComponent } from '../company/integrations/integrations.component';
import { AppUrlMapperComponent } from './app-url-mapper/app-url-mapper.component';
import { ContainerListUrlMapperComponent } from '../container-list-url-mapper/container-list-url-mapper.component';
import { ContainerDetailUrlMapperComponent } from '../container-detail-url-mapper/container-detail-url-mapper.component';
import { ForgotPasswordComponent } from '../forgot-password/forgot-password.component';
import { SetPasswordComponent } from '../set-password/set-password.component';
import { MyContentRequestComponent } from '../consolidate-content/my-content-request/my-content-request.component';
import { AllDimensionsComponent } from '../all-dimensions/all-dimensions.component';
import { ReportsHomeComponent } from '../reports/reports-home/reports-home.component';
import { RecoListComponent } from '../reco-content/reco-list/reco-list.component';
import { ActivityAuditComponent } from '../reports/activity-audit/activity-audit.component';
import { AuditActivityResolve } from './audit-activity.resolve';
import { ContainerTemplateComponent } from '../company/container-template/container-template.component';
import { CpWidgetDetailComponent } from '../ui-widget/content-pack-widget/cp-widget-detail/cp-widget-detail.component';
import { ContentEngagementComponent } from '../reports/content-engagement/content-engagement.component';
import { RecentContentListComponent } from '../recent-content/recent-content-list/recent-content-list.component';
import { MyaccountComponent } from '../myaccount/myaccount.component';
import { ContentAttributionComponent } from '../reports/content-attribution/content-attribution.component';
import { CompanyPropertiesComponent } from '../company/company-properties/company-properties.component';
import { SalesforceHomeComponent } from '../salesforce-home/salesforce-home.component';
 
const routes: Routes = [
  {
    path: 'app2.html',
    component: AppUrlMapperComponent
  },
  {
    path: '',
    redirectTo: '/portal',
    pathMatch: 'full'
  },
  {
      path: 'portal',
      canActivate: [AuthGuard],
      resolve: {
        contentTemplate: ContentTemplateResolve,
        resourceVersions: ResourceVersionResolve,
        tenant: TenantResolve,
        userPrefs: UserPreferenceResolve
      },
      children: [
        {
          path: '',
          component: HomeComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        },
        {
          // mapping from old application "#/portal/home"
          path: 'home',
          component: HomeComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        },
        {
          path: 'sfhome',
          component: SalesforceHomeComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        },
        {
          path: 'recentlist',
          component: RecentContentListComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        },
        {
          path: "alldims",
          component: AllDimensionsComponent
        },
        {
          // mapping from old application "#/portal/containerlist/:cQId"
          path: 'containerlist/:cQId',
          component: ContainerListUrlMapperComponent
        },
        {
          // mapping from old application "#/portal/container/:cQId/:recordIdentity"
          path: 'container/:cQId/:recordIdentity',
          component: ContainerDetailUrlMapperComponent
        },
        {
          // mapping from old application "#/portal/container/:cQId/:recordIdentity/c/single/:containerQId"
          // this and above url point to same page
          path: 'container/:cQId/:recordIdentity/c/single/:containerQId/',
          component: ContainerDetailUrlMapperComponent
        },
        {
          path: 'content',
          resolve: {
            sbData: SearchBarResolve,
            cwData: ContentWFResolve
          },
          children: [
            {
              path: 'new/:cQId',
              component: NewContentEditComponent
            },
            {
              path: 'detail/:cQId/:identity',
              component: BizContentComponent
            },
            {
              path: 'list/:cQId',
              component: BizContentListComponent
            },
            {
              path: 'request/:crIdentity',
              component: BizContentComponent
            },
            {
              path: 'request/:crIdentity/:action/',
              component: BizContentComponent
            }

          ]
        },
        {
          path: 'dim',
          component: BizDimensionComponent,
          resolve: {
            sbData: SearchBarResolve
          },
          children: [
            {
              path: 'list/:cQId',
              component: BizDimensionListComponent
            },
            {
              path: 'detail/:cQId/:identity',
              component: BizDimensionDetailComponent
            }
          ]
        },
        {
          path: 'cconsol',
          component: ConsolidateContentComponent,
          resolve: {
            sbData: SearchBarResolve,
            cwData: ContentWFResolve
          },
          children: [
            {
              path: '',
              component: MyDraftComponent
            },
            {
              path: 'requests',
              component: ContentRequestComponent
            },
            {
              path: 'myrequests',
              component: MyContentRequestComponent
            },
            {
              path: 'cverify',
              component: ObsoleteContentComponent
            }
          ]
        },
        {
          path: 'company',
          component: CompanyComponent,
          children: [
            {
              path: '',
              component: MembersComponent
            },
            {
              path: 'integrations',
              component: IntegrationsComponent
            },
            {
              path: 'props',
              component: CompanyPropertiesComponent
            },
            {
              path: 'cttemplate/dim',
              component: ContainerTemplateComponent,
              data: {
                bizCategory: 'dim'
              }
            },
            {
              path: 'cttemplate/content',
              component: ContainerTemplateComponent,
              data: {
                bizCategory: 'cntnt'
              }
            }
          ]
        },
        {
          path: 'search/:text',
          component: FreetextSearchComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        },
        {
          path: 'reco/list',
          component: RecoListComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        },
        {
          path: 'cpwdg/:widgetId',
          component: CpWidgetDetailComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        },
        {
          path: 'reports',
          pathMatch: 'full',
          redirectTo: '/portal/reports/d/all-activity'
        },
        {
          path: 'reports/d/:reportId',
          component: ReportsHomeComponent,
          resolve: {
            actTypes: AuditActivityResolve
          }
        },
        {
          path: 'myaccount',
          component: MyaccountComponent
        }
      ],
  },
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: "forgotpassword",
    component: ForgotPasswordComponent
  },
  {
    path: "setpassword",
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        component: SetPasswordComponent
      },
      {
        // for compatibility with the old application where url container user id
        path: ":userId",
        component: SetPasswordComponent
      }
    ]
  },
  {
    // mapping from old application
    path: "system/contentrequest/a/:action/:contentRequestId",
    redirectTo: "portal/content/request/:contentRequestId/:action"
  },
  {
    // mapping from old application
    path: "account/contentrequest/a/view/:contentRequestId",
    redirectTo: "portal/content/request/:contentRequestId"
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(
        routes, 
        { 
          /*enableTracing: true,*/
          onSameUrlNavigation: 'reload'
        }
      )
  ],
  declarations: [
    AppUrlMapperComponent
  ],
  providers: [
    AuthGuard,
    ContentTemplateResolve,
    ResourceVersionResolve,
    TenantResolve,
    SearchBarResolve,
    ContentWFResolve,
    UserPreferenceResolve,
    AuditActivityResolve,
    NavigationService
  ],
  exports: [
  	RouterModule,
    AppUrlMapperComponent
  ]
})
export class AppRoutingModule { }
