import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormControl } from '@angular/forms';

import { SelectOption } from '../../select/select.component';
import { FileService } from '../../file/file.service';
import { AlertService } from '../../alert/alert.service';
import { ContentPreviewService } from '../../content/content-preview.service';
import { ContentTemplateService } from '../../content-template.service';
import { NewContentService } from '../../content/new-content.service';
import { FileUploadInfo } from '../../../model/upload-info.model';
import { NavigationService } from '../../../app-routing/navigation.service';

@Component({
  selector: 'ebx-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UploadFileComponent implements OnInit {

  previewImageUrl: string;

  loading: boolean = false;

  contentTypeCtrl: FormControl;
  contentTypes: SelectOption[];

  filename: string;
  fileInfo: FileUploadInfo;

  newContent: boolean = true;

  errors: any = {};

  constructor(private dialogRef: MatDialogRef<UploadFileComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private fileService: FileService, private alert: AlertService,
              private contentPreviewService: ContentPreviewService,
              private contentTemplateService: ContentTemplateService,
              private navService: NavigationService,
              private newContentService: NewContentService) { }

  ngOnInit() {
    
    if (!this.data.file) {

      this.dialogRef.close();

    } else {
      
      this.newContent = !this.data.updateOperation;

      this.fileInfo = new FileUploadInfo();
      this.fileInfo.title = this.data.file.name;
      this.uploadFile();

      this.contentTypeCtrl = new FormControl();
      this.contentTypeCtrl.valueChanges.subscribe(
          val => {
            if (val && val[0]) {
              this.fileInfo.containerQId = val[0].id;
              this.errors.contentType = null;
            }
          }
        );

      this.contentTypes = this.contentTemplateService.getBizContentSelectOptions();
      this.contentTypes.sort((a, b) => (a.label > b.label ? 1 : -1));
      this.newContentService.clear();
    }

  }

  onTitleChange() {
    if (this.fileInfo.title.trim().length > 0) {
      this.errors.title = null;
    }
  }

  uploadFile() {
    
    this.loading = true;

    const formData = new FormData();
    formData.append("file", this.data.file);
    formData.append("fileSize", this.data.file.size);
    formData.append("temporary", "true");
    formData.append("generatePreview", "false");
    formData.append("generatePreviewAsync", "false");
    
    this.fileService.uploadFile(formData).subscribe(
      res => {
        this.fileInfo.docMetadata = res;
        
        let decoration: any = {};
        let record = {
          '__decoration': decoration
        }

        record.__decoration.__docMetadata = this.fileInfo.docMetadata;
        this.filename = this.fileInfo.docMetadata.name;

        /*let previewHandler = this.contentPreviewService.getPreviewHandler(record);
        if (previewHandler != null) {
          this.previewImageUrl = previewHandler.largeThumbnailUrl(record);
          this.fileInfo.thumbnailUrl = this.previewImageUrl;
        }*/
        this.previewImageUrl = "/assets/images/icons/file.svg";

        this.loading = false;

        if (!this.newContent) {
          this.updateFile();
        }
      },
      err => {
        this.alert.error("Error uploading file. Please try again later.", err.status);
        this.loading = false;
      });
  }

  focusOnTitle(id: string) {
    var elem = document.getElementById(id);
    if (elem) {
      setTimeout(() => {
        elem.focus();
      }, 0)
    }
  }

  captureFile() {
    
    let hasErrors = false;
    if (!this.fileInfo.title || this.fileInfo.title.trim().length == 0) {
      this.errors.title = { required: true };
      hasErrors = true;
    }

    if (!this.fileInfo.containerQId) {
      this.errors.contentType = { required: true };
      hasErrors = true;
    }

    if (hasErrors) {
      return;
    }

    this.newContentService.captureFileContent(this.fileInfo);
    this.navService.goToNewContentEdit(this.fileInfo.containerQId);
    this.dialogRef.close();
  }

  isFileDefaultImage() {
    if (this.previewImageUrl) {
      return this.previewImageUrl.endsWith(".svg");
    }
    return false;
  }

  updateFile() {
    this.dialogRef.close(this.fileInfo);
  }
}
