import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ClickOutsideModule } from 'ng-click-outside';
import { RouterModule, RouterLink } from '@angular/router';
import { MatDialogModule, MatDatepickerModule, MatInputModule, MatNativeDateModule } from '@angular/material';
import { QuillModule } from 'ngx-quill';

import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { UserIconComponent } from './user-icon/user-icon.component';
import { UploadButtonComponent } from './upload-button/upload-button.component';
import { UploadFileComponent } from './upload/upload-file/upload-file.component';
import { UploadUrlComponent } from './upload/upload-url/upload-url.component';
import { UploadTextComponent } from './upload/upload-text/upload-text.component';
import { TableComponent } from './table/table.component';
import { SortTableComponent } from './table/sort-table/sort-table.component';
import { PaginationComponent } from './table/pagination/pagination.component';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { EmbedInfoService } from './upload/embed-info.service';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { ApiUrlService } from './api-url.service';
import { AuthService } from './auth/auth.service';
import { UserService } from './auth/user.service';
import { DataSearchService } from './data-search/data-search.service';
import { HttpErrorInterceptor } from './auth/http-error.interceptor';
import { CommonHeadersInterceptor } from './common-headers.interceptor'; 
import { ResourceVersionService } from './versioning/resource-version.service';
import { ContentTemplateService } from './content-template.service';
import { ContentService } from './content/content.service';
import { ContentPreviewService } from './content/content-preview.service';
import { AlertComponent } from './alert/alert.component';
import { AlertService } from './alert/alert.service';
import { SelectComponent } from './select/select.component';
import { NewContentService } from './content/new-content.service';
import { SafeUrlPipe } from './pipes/safe-url.pipe';
import { SafeHtmlPipe } from './pipes/safe-html.pipe';
import { ContentEditableDirective } from './directives/content-editable/content-editable.directive';
import { FileService } from './file/file.service';
import { EbxDatePipe } from './pipes/ebx-date.pipe';
import { EbxDateOnlyPipe } from './pipes/ebx-date-only.pipe';
import { TextInputComponent } from './content/text-input/text-input.component';
import { RichTextInputComponent } from './content/rich-text-input/rich-text-input.component';
import { BoundedInputComponent } from './content/bounded-input/bounded-input.component';
import { DateInputComponent } from './content/date-input/date-input.component';
import { ContentStackInputComponent } from './content/content-stack-input/content-stack-input.component';
import { TextFileComponent } from './file/text-file/text-file.component';
import { AccessDirective } from './auth/access.directive';
import { SearchBarService } from './search-bar/search-bar.service';
import { BoundedItemsDSBuilderService } from './search-bar/bounded-items-dsbuilder.service';
import { LinkedContainerDsbuilderService } from './search-bar/linked-container-dsbuilder.service';
import { DataFiltersComponent } from './data-filters/data-filters.component';
import { UserPreferenceService } from './user-preference.service';
import { EbxDateTimezonePipe } from './pipes/ebx-date-timezone.pipe';
import { AppMessageService } from './app-message/app-message.service';
import { AppIntroMsgComponent } from './app-message/app-intro-msg/app-intro-msg.component';
import { ActivityAuditService } from './activity-audit.service';
import { RebootService } from './reboot.service';
import { BulkImportComponent } from './bulk-import/bulk-import.component';
import { GoogleDriveService } from './bulk-import/google-drive.service';
import { GdriveImportComponent } from './bulk-import/gdrive-import/gdrive-import.component';
import { ImportEditComponent } from './bulk-import/import-edit/import-edit.component';
import { ContentTagsComponent } from './content-tags/content-tags.component';
import { AddContentTagsComponent } from './content-tags/add-content-tags/add-content-tags.component';
import { EntityPillComponent } from './entity-pill/entity-pill.component';
import { BulkSelectTypeComponent } from './bulk-import/bulk-select-type/bulk-select-type.component';
import { BulkAddTagsComponent } from './bulk-import/bulk-add-tags/bulk-add-tags.component';
import { MessageDialogComponent } from './message-dialog/message-dialog.component';
import { UserTaskComponent } from './user-task/user-task/user-task.component';
import { UserTaskService } from './user-task/user-task.service';
import { ProgressCircleComponent } from './progress-circle/progress-circle.component';
import { AppEventService } from './app-event.service';
import { YoutubeImportComponent } from './bulk-import/youtube-import/youtube-import.component';
import { ContentQualityService } from './content/content-quality.service';
import { LayoutService } from './layout.service';
import { ContentConnService } from './content-conn.service';
import { ProgressLineComponent } from './progress-line/progress-line.component';
import { GlobalSearchControllerService } from './search-bar/global-search-controller.service';
import { EbxLinkifyPipe } from './pipes/ebx-linkify.pipe';
import { UploadAssetComponent } from './upload/upload-asset/upload-asset.component';
import { DndModule } from 'ng2-dnd';
import { CollapsibleModule } from 'ngx-collapsible';

import Quill from 'quill';

const Clipboard = Quill.import('modules/clipboard')
const Delta = Quill.import('delta')

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ClickOutsideModule,
    RouterModule,
    MatDialogModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule,
    QuillModule,
    DndModule.forRoot(),
    CollapsibleModule
  ],
  providers: [
    RebootService,
    AlertService,
  	ApiUrlService,
  	AuthService,
    UserService,
    EmbedInfoService,
    ContentTemplateService,
    ResourceVersionService,
    DataSearchService,
    ContentService,
    ContentPreviewService,
    NewContentService,
    FileService,
    DatePipe,
    EbxDatePipe,
    EbxDateOnlyPipe,
    SearchBarService,
    BoundedItemsDSBuilderService,
    LinkedContainerDsbuilderService,
    UserPreferenceService,
    EbxDateTimezonePipe,
    AppMessageService,
    ActivityAuditService,
    GoogleDriveService,
    UserTaskService,
    AppEventService,
    ContentQualityService,
    LayoutService,
    ContentConnService,
    GlobalSearchControllerService,
    EbxLinkifyPipe,
  	{
  	  provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
  	},
  	{
      provide: HTTP_INTERCEPTORS,
      useClass: CommonHeadersInterceptor,
      multi: true
  	}
  ],
  entryComponents: [
    UploadFileComponent,
    UploadUrlComponent,
    UploadTextComponent,
    UploadAssetComponent,
    AppIntroMsgComponent,
    ConfirmDialogComponent,
    BulkImportComponent,
    BulkSelectTypeComponent,
    BulkAddTagsComponent,
    MessageDialogComponent
  ],
  declarations: [
    HeaderComponent,
    FooterComponent,
    UserIconComponent,
    TableComponent,
    SortTableComponent,
    PaginationComponent,
    ConfirmDialogComponent,
    UploadButtonComponent,
    UploadFileComponent,
    UploadUrlComponent,
    UploadTextComponent,
    UploadAssetComponent,
    AppIntroMsgComponent,
    SearchBarComponent,
    AlertComponent,
    SelectComponent,
    SafeUrlPipe,
    SafeHtmlPipe,
    ContentEditableDirective,
    EbxDatePipe,
    EbxDateOnlyPipe,
    TextInputComponent,
    RichTextInputComponent,
    BoundedInputComponent,
    DateInputComponent,
    ContentStackInputComponent,
    TextFileComponent,
    AccessDirective,
    DataFiltersComponent,
    EbxDateTimezonePipe,
    AppIntroMsgComponent,
    BulkImportComponent,
    GdriveImportComponent,
    ImportEditComponent,
    ContentTagsComponent,
    AddContentTagsComponent,
    EntityPillComponent,
    BulkSelectTypeComponent,
    BulkAddTagsComponent,
    MessageDialogComponent,
    UserTaskComponent,
    ProgressCircleComponent,
    YoutubeImportComponent,
    ProgressLineComponent,
    EbxLinkifyPipe,
    UploadAssetComponent
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    UserIconComponent,
    TableComponent,
    SortTableComponent,
    PaginationComponent,
    ConfirmDialogComponent,
    AlertComponent,
    SelectComponent,
    TextInputComponent,
    RichTextInputComponent,
    BoundedInputComponent,
    ContentStackInputComponent,
    DateInputComponent,
    SafeUrlPipe,
    SafeHtmlPipe,
    ContentEditableDirective,
    EbxDatePipe,
    EbxDateOnlyPipe,
    TextFileComponent,
    AccessDirective,
    DataFiltersComponent,
    EbxDateTimezonePipe,
    ContentTagsComponent,
    AddContentTagsComponent,
    EntityPillComponent,
    UserTaskComponent,
    ProgressCircleComponent,
    ProgressLineComponent,
    SearchBarComponent,
    EbxLinkifyPipe,
    DndModule,
    CollapsibleModule
  ]
})
export class CoreModule { 
    constructor() {
        console.log("Core Module initializing...");
        Quill.register('modules/clipboard', PlainClipboard, true);
    }
}

class PlainClipboard extends Clipboard {
  /*convert(html = null) {
    if (typeof html === 'string') {
      this.container.innerHTML = html;
    }
    let text = this.container.innerText;
    this.container.innerHTML = '';
    return new Delta().insert(text);
  }*/
  onPaste (e) {
    e.preventDefault()
    const range = this.quill.getSelection()
    const text = e.clipboardData.getData('text/plain')
    const delta = new Delta()
    .retain(range.index)
    .delete(range.length)
    .insert(text)
    const index = text.length + range.index
    const length = 0
    this.quill.updateContents(delta, 'silent')
    this.quill.setSelection(index, length, 'silent')
    this.quill.scrollIntoView()
  }
}