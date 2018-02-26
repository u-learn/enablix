import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ClickOutsideModule } from 'ng-click-outside';
import { RouterModule, RouterLink } from '@angular/router';
import { MatDialogModule } from '@angular/material';

import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { UserIconComponent } from './user-icon/user-icon.component';
import { UploadButtonComponent } from './upload-button/upload-button.component';
import { UploadFileComponent } from './upload/upload-file/upload-file.component';
import { UploadUrlComponent } from './upload/upload-url/upload-url.component';
import { UploadTextComponent } from './upload/upload-text/upload-text.component';
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
import { TextInputComponent } from './content/text-input/text-input.component';
import { RichTextInputComponent } from './content/rich-text-input/rich-text-input.component';
import { BoundedInputComponent } from './content/bounded-input/bounded-input.component';
import { DateInputComponent } from './content/date-input/date-input.component';
import { ContentStackInputComponent } from './content/content-stack-input/content-stack-input.component';
import { TextFileComponent } from './file/text-file/text-file.component';
import { AccessDirective } from './auth/access.directive';
import { SearchBarService } from './search-bar/search-bar.service';
import { BoundedItemsDSBuilderService } from './search-bar/bounded-items-dsbuilder.service';
import { DataFiltersComponent } from './data-filters/data-filters.component';
import { UserPreferenceService } from './user-preference.service';
import { EbxDateTimezonePipe } from './pipes/ebx-date-timezone.pipe';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ClickOutsideModule,
    RouterModule,
    MatDialogModule
  ],
  providers: [
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
    SearchBarService,
    BoundedItemsDSBuilderService,
    UserPreferenceService,
    EbxDateTimezonePipe,
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
  ],
  declarations: [
    HeaderComponent,
    FooterComponent,
    UserIconComponent,
    UploadButtonComponent,
    UploadFileComponent,
    UploadUrlComponent,
    UploadTextComponent,
    SearchBarComponent,
    AlertComponent,
    SelectComponent,
    SafeUrlPipe,
    SafeHtmlPipe,
    ContentEditableDirective,
    EbxDatePipe,
    TextInputComponent,
    RichTextInputComponent,
    BoundedInputComponent,
    DateInputComponent,
    ContentStackInputComponent,
    TextFileComponent,
    AccessDirective,
    DataFiltersComponent,
    EbxDateTimezonePipe
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    UserIconComponent,
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
    TextFileComponent,
    AccessDirective,
    DataFiltersComponent,
    EbxDateTimezonePipe
  ]
})
export class CoreModule { }
