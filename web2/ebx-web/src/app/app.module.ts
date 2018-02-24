import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { RouterModule, RouterLink } from '@angular/router';
import { ClickOutsideModule } from 'ng-click-outside';
import { MatDialogModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EllipsisModule } from 'ngx-ellipsis';
import { NgProgressModule, NgProgressInterceptor } from 'ngx-progressbar';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { CoreModule } from './core/core.module';
import { ContentEditableDirective } from './core/directives/content-editable/content-editable.directive';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LoginFormComponent } from './login-page/login-form/login-form.component';
import { FeaturedContentComponent } from './featured-content/featured-content.component';
import { DimensionsComponent } from './dimensions/dimensions.component';
import { RecentActivitiesComponent } from './recent-activities/recent-activities.component';
import { RecentContentComponent } from './recent-content/recent-content.component';
import { DimensionComponent } from './dimensions/dimension/dimension.component';
import { RecentContentService } from './recent-content/recent-content.service';

import { DataCardComponent } from './data-card/data-card.component';
import { NewContentEditComponent } from './new-content-edit/new-content-edit.component';
import { ContentPreviewComponent } from './content-preview/content-preview.component';
import { UrlContentPreviewComponent } from './content-preview/url-content-preview/url-content-preview.component';
import { ImagePreviewComponent } from './content-preview/image-preview/image-preview.component';
import { FileImagesPreviewComponent } from './content-preview/file-images-preview/file-images-preview.component';
import { ContentItemComponent } from './content-item/content-item.component';
import { BizContentComponent } from './biz-content/biz-content.component';
import { ContentTagsComponent } from './content-tags/content-tags.component';
import { AddContentTagsComponent } from './content-tags/add-content-tags/add-content-tags.component';
import { TextContentPreviewComponent } from './content-preview/text-content-preview/text-content-preview.component';
import { BizDimensionDetailComponent } from './biz-dimension/biz-dimension-detail/biz-dimension-detail.component';
import { BizDimensionListComponent } from './biz-dimension/biz-dimension-list/biz-dimension-list.component';
import { BizDimensionComponent } from './biz-dimension/biz-dimension.component';
import { ActivityAuditService } from './services/activity-audit.service';
import { TenantService } from './services/tenant.service';
import { EditBizDimensionComponent } from './biz-dimension/edit-biz-dimension/edit-biz-dimension.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { ContentDeleteButtonComponent } from './content-action/content-delete-button/content-delete-button.component';
import { DocDownloadButtonComponent } from './content-action/doc-download-button/doc-download-button.component';
import { CopyPortalUrlButtonComponent } from './content-action/copy-portal-url-button/copy-portal-url-button.component';
import { ContentEmailButtonComponent } from './content-action/content-email-button/content-email-button.component';
import { BizContentListComponent } from './biz-content/biz-content-list/biz-content-list.component';
import { TableComponent } from './table/table.component';
import { ConsolidateContentComponent } from './consolidate-content/consolidate-content.component';
import { MyDraftComponent } from './consolidate-content/my-draft/my-draft.component';
import { ContentRequestComponent } from './consolidate-content/content-request/content-request.component';
import { CardPreviewComponent } from './data-card/card-preview/card-preview.component';
import { ConsolidateContentService } from './consolidate-content/consolidate-content.service';
import { PillComponent } from './pill/pill.component';
import { TypePillComponent } from './type-pill/type-pill.component';
import { SortTableComponent } from './table/sort-table/sort-table.component';
import { ContentWorkflowService } from './services/content-workflow.service';
import { FreetextSearchService } from './services/freetext-search.service';
import { FreetextSearchComponent } from './freetext-search/freetext-search.component';
import { CompanyComponent } from './company/company.component';
import { MembersComponent } from './company/members/members.component';
import { IntegrationsComponent } from './company/integrations/integrations.component';
import { MembersService } from './company/members/members.service';
import { MemberDetailComponent } from './company/members/member-detail/member-detail.component';
import { PaginationComponent } from './table/pagination/pagination.component';
import { TPIntegrationsComponent } from './company/integrations/tpintegrations/tpintegrations.component';
import { DocStoreIntegrationComponent } from './company/integrations/doc-store-integration/doc-store-integration.component';
import { DocStoreConfigService } from './company/integrations/doc-store-integration/doc-store-config.service';
import { ConfigInfoService } from './services/config-info.service';
import { EntityPillComponent } from './entity-pill/entity-pill.component';
import { DocUpdateButtonComponent } from './content-action/doc-update-button/doc-update-button.component';
import { ContentReqApproveButtonComponent } from './content-action/content-req-approve-button/content-req-approve-button.component';
import { ContentReqRejectButtonComponent } from './content-action/content-req-reject-button/content-req-reject-button.component';
import { EmailSharePopupComponent } from './content-action/content-email-button/email-share-popup/email-share-popup.component';
import { ContentShareService } from './services/content-share.service';
import { ContainerListUrlMapperComponent } from './container-list-url-mapper/container-list-url-mapper.component';
import { ContainerDetailUrlMapperComponent } from './container-detail-url-mapper/container-detail-url-mapper.component';
import { InfiniteScrollerDirective } from './directives/infinite-scroller.directive';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { SetPasswordComponent } from './set-password/set-password.component';
import { MyContentRequestComponent } from './consolidate-content/my-content-request/my-content-request.component';
import { AllDimensionsComponent } from './all-dimensions/all-dimensions.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { ReportsModule } from './reports/reports.module';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginPageComponent,
    PageNotFoundComponent,
    LoginFormComponent,
    FeaturedContentComponent,
    DimensionsComponent,
    RecentActivitiesComponent,
    RecentContentComponent,
    DimensionComponent,
    DataCardComponent,
    NewContentEditComponent,
    ContentPreviewComponent,
    UrlContentPreviewComponent,
    ImagePreviewComponent,
    FileImagesPreviewComponent,
    ContentItemComponent,
    BizContentComponent,
    ContentTagsComponent,
    AddContentTagsComponent,
    TextContentPreviewComponent,
    BizDimensionDetailComponent,
    BizDimensionListComponent,
    BizDimensionComponent,
    EditBizDimensionComponent,
    ConfirmDialogComponent,
    ContentDeleteButtonComponent,
    DocDownloadButtonComponent,
    CopyPortalUrlButtonComponent,
    ContentEmailButtonComponent,
    BizContentListComponent,
    TableComponent,
    ConsolidateContentComponent,
    MyDraftComponent,
    ContentRequestComponent,
    CardPreviewComponent,
    PillComponent,
    TypePillComponent,
    SortTableComponent,
    FreetextSearchComponent,
    CompanyComponent,
    MembersComponent,
    IntegrationsComponent,
    MemberDetailComponent,
    PaginationComponent,
    TPIntegrationsComponent,
    DocStoreIntegrationComponent,
    EntityPillComponent,
    DocUpdateButtonComponent,
    ContentReqApproveButtonComponent,
    ContentReqRejectButtonComponent,
    EmailSharePopupComponent,
    ContainerListUrlMapperComponent,
    ContainerDetailUrlMapperComponent,
    InfiniteScrollerDirective,
    ForgotPasswordComponent,
    SetPasswordComponent,
    MyContentRequestComponent,
    AllDimensionsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    CoreModule,
    RouterModule,
    AppRoutingModule,
    ClickOutsideModule,
    MatDialogModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    EllipsisModule,
    NgProgressModule,
    ReportsModule
  ],
  providers: [
    RecentContentService,
    ActivityAuditService,
    TenantService,
    ConsolidateContentService,
    ContentWorkflowService,
    FreetextSearchService,
    MembersService,
    DocStoreConfigService,
    ConfigInfoService,
    ContentShareService,
    { 
        provide: HTTP_INTERCEPTORS, 
        useClass: NgProgressInterceptor, 
        multi: true 
    }
  ],
  entryComponents: [
    EditBizDimensionComponent,
    ConfirmDialogComponent,
    MemberDetailComponent,
    EmailSharePopupComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
