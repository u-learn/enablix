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
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

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
import { TextContentPreviewComponent } from './content-preview/text-content-preview/text-content-preview.component';
import { BizDimensionDetailComponent } from './biz-dimension/biz-dimension-detail/biz-dimension-detail.component';
import { BizDimensionListComponent } from './biz-dimension/biz-dimension-list/biz-dimension-list.component';
import { BizDimensionComponent } from './biz-dimension/biz-dimension.component';
import { TenantService } from './services/tenant.service';
import { EditBizDimensionComponent } from './biz-dimension/edit-biz-dimension/edit-biz-dimension.component';
import { ContentDeleteButtonComponent } from './content-action/content-delete-button/content-delete-button.component';
import { DocDownloadButtonComponent } from './content-action/doc-download-button/doc-download-button.component';
import { CopyPortalUrlButtonComponent } from './content-action/copy-portal-url-button/copy-portal-url-button.component';
import { ContentEmailButtonComponent } from './content-action/content-email-button/content-email-button.component';
import { BizContentListComponent } from './biz-content/biz-content-list/biz-content-list.component';
import { ConsolidateContentComponent } from './consolidate-content/consolidate-content.component';
import { MyDraftComponent } from './consolidate-content/my-draft/my-draft.component';
import { ContentRequestComponent } from './consolidate-content/content-request/content-request.component';
import { CardPreviewComponent } from './data-card/card-preview/card-preview.component';
import { ConsolidateContentService } from './consolidate-content/consolidate-content.service';
import { PillComponent } from './pill/pill.component';
import { TypePillComponent } from './type-pill/type-pill.component';
import { ContentWorkflowService } from './services/content-workflow.service';
import { FreetextSearchService } from './services/freetext-search.service';
import { FreetextSearchComponent } from './freetext-search/freetext-search.component';
import { CompanyComponent } from './company/company.component';
import { MembersComponent } from './company/members/members.component';
import { IntegrationsComponent } from './company/integrations/integrations.component';
import { MembersService } from './company/members/members.service';
import { MemberDetailComponent } from './company/members/member-detail/member-detail.component';
import { TPIntegrationsComponent } from './company/integrations/tpintegrations/tpintegrations.component';
import { DocStoreIntegrationComponent } from './company/integrations/doc-store-integration/doc-store-integration.component';
import { DocStoreConfigService } from './company/integrations/doc-store-integration/doc-store-config.service';
import { ConfigInfoService } from './services/config-info.service';
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
import { CopyLinkUrlButtonComponent } from './content-action/copy-link-url-button/copy-link-url-button.component';
import { OpenUrlButtonComponent } from './content-action/open-url-button/open-url-button.component';
import { RecoContentComponent } from './reco-content/reco-content.component';
import { RecoContentService } from './reco-content/reco-content.service';
import { CopyDownloadUrlButtonComponent } from './content-action/copy-download-url-button/copy-download-url-button.component';
import { RecoListComponent } from './reco-content/reco-list/reco-list.component';
import { ContainerTemplateComponent } from './company/container-template/container-template.component';
import { ContentStackPreviewComponent } from './content-preview/content-stack-preview/content-stack-preview.component';
import { ObsoleteContentComponent } from './consolidate-content/obsolete-content/obsolete-content.component';
import { CopyEmbedCodeButtonComponent } from './content-action/copy-embed-code-button/copy-embed-code-button.component';
import { DefaultLayoutComponent } from './biz-dimension/biz-dimension-detail/default-layout/default-layout.component';
import { ContentMappingLayoutComponent } from './biz-dimension/biz-dimension-detail/content-mapping-layout/content-mapping-layout.component';
import { BizDimLayoutSwitchComponent } from './biz-dimension/biz-dimension-detail/biz-dim-layout-switch/biz-dim-layout-switch.component';
import { UiWidgetComponent } from './ui-widget/ui-widget.component';
import { UiWidgetService } from './services/ui-widget.service';
import { ContentPackWidgetComponent } from './ui-widget/content-pack-widget/content-pack-widget.component';
import { CpWidgetDetailComponent } from './ui-widget/content-pack-widget/cp-widget-detail/cp-widget-detail.component';
import { EmbedHtmlPreviewComponent } from './content-preview/embed-html-preview/embed-html-preview.component';
import { ContentPickerButtonComponent } from './content-action/content-picker-button/content-picker-button.component';
import { ContentBrowserComponent } from './content-browser/content-browser.component';
import { ContentBrowserSearchControllerService } from './content-browser/content-browser-search-controller.service';
import { AddBizContentDialogButtonComponent } from './content-action/add-biz-content-dialog-button/add-biz-content-dialog-button.component';
import { RecentContentListComponent } from './recent-content/recent-content-list/recent-content-list.component';

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
    TextContentPreviewComponent,
    BizDimensionDetailComponent,
    BizDimensionListComponent,
    BizDimensionComponent,
    EditBizDimensionComponent,
    ContentDeleteButtonComponent,
    DocDownloadButtonComponent,
    CopyPortalUrlButtonComponent,
    ContentEmailButtonComponent,
    BizContentListComponent,
    ConsolidateContentComponent,
    MyDraftComponent,
    ContentRequestComponent,
    CardPreviewComponent,
    PillComponent,
    TypePillComponent,
    FreetextSearchComponent,
    CompanyComponent,
    MembersComponent,
    IntegrationsComponent,
    MemberDetailComponent,
    TPIntegrationsComponent,
    DocStoreIntegrationComponent,
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
    AllDimensionsComponent,
    CopyLinkUrlButtonComponent,
    OpenUrlButtonComponent,
    RecoContentComponent,
    CopyDownloadUrlButtonComponent,
    RecoListComponent,
    ContainerTemplateComponent,
    ContentStackPreviewComponent,
    ObsoleteContentComponent,
    CopyEmbedCodeButtonComponent,
    DefaultLayoutComponent,
    ContentMappingLayoutComponent,
    BizDimLayoutSwitchComponent,
    UiWidgetComponent,
    ContentPackWidgetComponent,
    CpWidgetDetailComponent,
    EmbedHtmlPreviewComponent,
    ContentBrowserComponent,
    ContentPickerButtonComponent,
    AddBizContentDialogButtonComponent,
    RecentContentListComponent
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
    ReportsModule,
    InfiniteScrollModule
  ],
  providers: [
    RecentContentService,
    TenantService,
    ConsolidateContentService,
    ContentWorkflowService,
    FreetextSearchService,
    MembersService,
    DocStoreConfigService,
    ConfigInfoService,
    ContentShareService,
    RecoContentService,
    UiWidgetService,
    ContentBrowserSearchControllerService,
    { 
        provide: HTTP_INTERCEPTORS, 
        useClass: NgProgressInterceptor, 
        multi: true 
    }
  ],
  entryComponents: [
    EditBizDimensionComponent,
    MemberDetailComponent,
    EmailSharePopupComponent,
    ContentBrowserComponent,
    BizContentComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
