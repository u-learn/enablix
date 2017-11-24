import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ClickOutsideModule } from 'ng-click-outside';

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

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ClickOutsideModule
  ],
  providers: [
    AlertService,
  	ApiUrlService,
  	AuthService,
    UserService,
    ContentTemplateService,
    ResourceVersionService,
    DataSearchService,
    ContentService,
    ContentPreviewService,
    NewContentService,
    FileService,
    DatePipe,
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
  declarations: [
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
    ContentStackInputComponent
  ],
  exports: [
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
    EbxDatePipe
  ]
})
export class CoreModule { }
