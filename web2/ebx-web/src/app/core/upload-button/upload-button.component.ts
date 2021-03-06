import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { UploadFileComponent } from '../upload/upload-file/upload-file.component';
import { UploadUrlComponent } from '../upload/upload-url/upload-url.component';
import { UploadTextComponent } from '../upload/upload-text/upload-text.component';
import { UploadAssetComponent } from '../upload/upload-asset/upload-asset.component';
import { BulkImportComponent } from '../bulk-import/bulk-import.component';
import { AppEventService } from '../app-event.service';

@Component({
  selector: 'ebx-upload-button',
  templateUrl: './upload-button.component.html',
  styleUrls: ['./upload-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UploadButtonComponent implements OnInit {

  @ViewChild("fileInput") fileInput;
  @ViewChild("fileForm") fileForm;

  @ViewChild("addBtn") addBtn;
  
  uploadOptions = false;

  constructor(private dialog: MatDialog,
    private appEventService: AppEventService) { }

  ngOnInit() {
    this.appEventService.onOpenAddAsset.subscribe(() => {
      setTimeout(() => this.showUploadOptions(), 100);
    });
  }

  showUploadOptions() {
    this.uploadOptions = true;
  }

  hideUploadOptions() {
    this.uploadOptions = false;
  }

  openFileBrowser() {
    this.fileInput.nativeElement.click();
  }

  onFileSelect(event) {
    
    let fileBrowser = this.fileInput.nativeElement;
    
    if (fileBrowser.files && fileBrowser.files[0]) {

      let dialogRef = this.dialog.open(UploadFileComponent, {
        width: '624px',
        disableClose: true,
        data: { file: fileBrowser.files[0] }
      });

      this.fileForm.nativeElement.reset();
    }
  }

  openUploadDialog(type: string) {
    
    let componentType = null;
    
    switch (type) {
      case "url":
        componentType = UploadUrlComponent;
        break;
      
      case "text":
        componentType = UploadTextComponent;
        break;
      
      case "asset":
        componentType = UploadAssetComponent;
        break;

      default:
        componentType = UploadFileComponent;
        break;
    }

    let dialogRef = this.dialog.open(componentType, {
      width: '624px',
      disableClose: true,
      data: { type: type }
    });

  }

  openBulkImportDialog() {
    let dialogRef = this.dialog.open(BulkImportComponent, {
      minWidth: '624px',
      maxWidth: '90vw',
      disableClose: true
    });
  }
}
