import { Component, OnInit, ViewEncapsulation, Input, ViewChild, Output, EventEmitter  } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { UploadFileComponent } from '../../core/upload/upload-file/upload-file.component';
import { FileUploadInfo } from '../../model/upload-info.model';

@Component({
  selector: 'ebx-thumbnail-update-button',
  templateUrl: './thumbnail-update-button.component.html',
  styleUrls: ['./thumbnail-update-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ThumbnailUpdateButtonComponent implements OnInit {

  @ViewChild("tnFileForm") fileForm;
  @ViewChild("tnFileInput") fileInput;

  _record: any;
  @Input() container: Container;

  @Output() onDocUpdate = new EventEmitter<void>();

  constructor(private alert: AlertService, private dialog: MatDialog) { }

  @Input()
  set record(rec: any) {
    this._record = rec;
  }

  get record() {
    return this._record;
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
        data: { file: fileBrowser.files[0], updateOperation: true, thumbnailUpload: true }
      });

      dialogRef.afterClosed().subscribe((res: FileUploadInfo) => {
        if (res && res.docMetadata) {
          this._record.__thumbnailDoc = res.docMetadata;
          this.onDocUpdate.emit();
        }
      });

      this.fileForm.nativeElement.reset();
    }
  }

  ngOnInit() {
  }

}
