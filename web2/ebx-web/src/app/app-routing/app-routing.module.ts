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
import { NavigationService } from './navigation.service';
import { BizDimensionListComponent } from '../biz-dimension/biz-dimension-list/biz-dimension-list.component';
import { BizDimensionDetailComponent } from '../biz-dimension/biz-dimension-detail/biz-dimension-detail.component';
import { BizDimensionComponent } from '../biz-dimension/biz-dimension.component';
import { ConsolidateContentComponent } from '../consolidate-content/consolidate-content.component';
import { MyDraftComponent } from '../consolidate-content/my-draft/my-draft.component';
import { ContentRequestComponent } from '../consolidate-content/content-request/content-request.component';
import { FreetextSearchComponent } from '../freetext-search/freetext-search.component';

const routes: Routes = [
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
        tenant: TenantResolve
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
          path: 'content',
          resolve: {
            sbData: SearchBarResolve
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
            sbData: SearchBarResolve
          },
          children: [
            {
              path: '',
              component: MyDraftComponent
            },
            {
              path: 'pending',
              component: ContentRequestComponent
            }
          ]
        },
        {
          path: 'search/:text',
          component: FreetextSearchComponent,
          resolve: {
            sbData: SearchBarResolve
          }
        }
      ],
  },
  {
    path: 'login',
    component: LoginPageComponent
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
        { enableTracing: true }
      )
  ],
  declarations: [],
  providers: [
    AuthGuard,
    ContentTemplateResolve,
    ResourceVersionResolve,
    TenantResolve,
    SearchBarResolve,
    NavigationService
  ],
  exports: [
  	RouterModule
  ]
})
export class AppRoutingModule { }
