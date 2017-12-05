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
import { AppRoutingModule } from './app-routing/app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LoginFormComponent } from './login-page/login-form/login-form.component';
import { FeaturedContentComponent } from './featured-content/featured-content.component';
import { DimensionsComponent } from './dimensions/dimensions.component';
import { RecentActivitiesComponent } from './recent-activities/recent-activities.component';
import { RecentContentComponent } from './recent-content/recent-content.component';
import { DimensionComponent } from './dimensions/dimension/dimension.component';
import { RecentContentService } from './recent-content/recent-content.service';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { UploadButtonComponent } from './upload-button/upload-button.component';
import { UserIconComponent } from './user-icon/user-icon.component';
import { DataCardComponent } from './data-card/data-card.component';
import { UploadFileComponent } from './upload/upload-file/upload-file.component';
import { UploadUrlComponent } from './upload/upload-url/upload-url.component';
import { UploadTextComponent } from './upload/upload-text/upload-text.component';
import { EmbedInfoService } from './upload/embed-info.service';
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
import { SearchBarService } from './search-bar/search-bar.service';
import { BizContentListComponent } from './biz-content/biz-content-list/biz-content-list.component';
import { BoundedItemsDSBuilderService } from './search-bar/bounded-items-dsbuilder.service';
import { TableComponent } from './table/table.component';
import { ConsolidateContentComponent } from './consolidate-content/consolidate-content.component';
import { MyDraftComponent } from './consolidate-content/my-draft/my-draft.component';
import { ContentRequestComponent } from './consolidate-content/content-request/content-request.component';
import { CardPreviewComponent } from './data-card/card-preview/card-preview.component';
import { ConsolidateContentService } from './consolidate-content/consolidate-content.service';
import { PillComponent } from './pill/pill.component';
import { TypePillComponent } from './type-pill/type-pill.component';
import { SortTableComponent } from './table/sort-table/sort-table.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    LoginPageComponent,
    PageNotFoundComponent,
    LoginFormComponent,
    FeaturedContentComponent,
    DimensionsComponent,
    RecentActivitiesComponent,
    RecentContentComponent,
    DimensionComponent,
    SearchBarComponent,
    UploadButtonComponent,
    UserIconComponent,
    DataCardComponent,
    UploadFileComponent,
    UploadUrlComponent,
    UploadTextComponent,
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
    SortTableComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    CoreModule,
    AppRoutingModule,
    RouterModule,
    ClickOutsideModule,
    MatDialogModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    EllipsisModule,
    NgProgressModule
  ],
  providers: [
    RecentContentService,
    EmbedInfoService,
    ActivityAuditService,
    TenantService,
    SearchBarService,
    BoundedItemsDSBuilderService,
    ConsolidateContentService,
    { 
        provide: HTTP_INTERCEPTORS, 
        useClass: NgProgressInterceptor, 
        multi: true 
    }
  ],
  entryComponents: [
    UploadFileComponent,
    UploadUrlComponent,
    UploadTextComponent,
    EditBizDimensionComponent,
    ConfirmDialogComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
