import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { UploadFileComponent } from '../upload/upload-file/upload-file.component';
import { UploadUrlComponent } from '../upload/upload-url/upload-url.component';
import { UploadTextComponent } from '../upload/upload-text/upload-text.component';

@Component({
  selector: 'ebx-upload-button',
  templateUrl: './upload-button.component.html',
  styleUrls: ['./upload-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UploadButtonComponent implements OnInit {

  @ViewChild("fileInput") fileInput;
  @ViewChild("fileForm") fileForm;
  
  uploadOptions = false;

  constructor(private dialog: MatDialog) { }

  ngOnInit() {
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
}
