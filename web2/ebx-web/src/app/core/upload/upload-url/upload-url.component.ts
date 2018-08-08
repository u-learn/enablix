import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialogRef } from '@angular/material';

import { EmbedInfoService } from '../embed-info.service';
import { EmbedInfo } from '../../../model/embed-info.model';
import { SelectOption } from '../../select/select.component';
import { ContentTemplateService } from '../../content-template.service';
import { ContentService } from '../../content/content.service';
import { UrlCaptureInfo } from '../../../model/upload-info.model';
import { NewContentService } from '../../content/new-content.service';
import { NavigationService } from '../../../app-routing/navigation.service';

@Component({
  selector: 'ebx-upload-url',
  templateUrl: './upload-url.component.html',
  styleUrls: ['./upload-url.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UploadUrlComponent implements OnInit {

  previewContent = false;
  loadingPreview = false;

  embedInfo: EmbedInfo;
  urlInfo: UrlCaptureInfo;

  thumbnailUrl: string;
  defaultImg: boolean;

  contentTypeCtrl: FormControl;
  contentTypes: SelectOption[];

  errors: any = {};

  constructor(private dialogRef: MatDialogRef<UploadUrlComponent>,
              private embedInfoService: EmbedInfoService,
              private contentTemplateService: ContentTemplateService,
              private navService: NavigationService,
              private newContentService: NewContentService) { 

    this.urlInfo = new UrlCaptureInfo();
    
    this.contentTypeCtrl = new FormControl();
    this.contentTypeCtrl.valueChanges.subscribe(
        val => {
          if (val && val[0]) {
            this.urlInfo.containerQId = val[0].id;
            this.errors.contentType = null;
          }
        }
      );
  }

  ngOnInit() {
    this.contentTypes = this.contentTemplateService.getBizContentSelectOptions();
    this.contentTypes.sort((a, b) => (a.label > b.label ? 1 : -1));
    this.newContentService.clear();
  }

  onTitleChange() {
    if (this.urlInfo.title.trim().length > 0) {
      this.errors.title = null;
    }
  }

  showPreview() {

    if (this.urlInfo.url) {
      
      console.log("Fetching embed info for: " + this.urlInfo.url);
      this.loadingPreview = true;

      this.embedInfoService.getEmbedInfo(this.urlInfo.url)
          .subscribe(
              result => {
                
                if (result) {
                  
                  this.embedInfo = result;

                  this.thumbnailUrl = this.embedInfoService.getThumbnailUrl(this.embedInfo);
                  this.urlInfo.title = this.embedInfo.title;

                  this.urlInfo.thumbnailUrl = this.thumbnailUrl;
                  
                } else {
                  this.thumbnailUrl = "/assets/images/icons/url-icon.svg";
                  this.defaultImg = true;
                }

                this.loadingPreview = false;
                this.previewContent = true;
              },
              error => {
                this.loadingPreview = false;
                if (error.error && error.error.errorCode == "oembed_provider_error") {
                  this.thumbnailUrl = "/assets/images/icons/url-icon.svg";
                  this.previewContent = true;
                  this.defaultImg = true;
                }
              }
            );
    }
  }

  focusOnTitle(id: string) {
    var elem = document.getElementById(id);
    if (elem) {
      setTimeout(() => {
        elem.focus();
      }, 0)
    }
  }

  captureUrl() {
    
    let hasErrors = false;
    if (!this.urlInfo.title || this.urlInfo.title.trim().length == 0) {
      this.errors.title = { required: true };
      hasErrors = true;
    }

    if (!this.urlInfo.containerQId) {
      this.errors.contentType = { required: true };
      hasErrors = true;
    }

    if (hasErrors) {
      return;
    }

    this.newContentService.captureUrlContent(this.urlInfo, this.embedInfo);
    this.navService.goToNewContentEdit(this.urlInfo.containerQId);
    this.dialogRef.close();
  }

}
