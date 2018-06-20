import { Component, OnInit, ViewEncapsulation, Input, ViewChild } from '@angular/core';
import { environment } from '../../../environments/environment';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ApiUrlService } from '../../core/api-url.service';
import { ConfirmDialogComponent } from '../../core/confirm-dialog/confirm-dialog.component'; 
import { MessageDialogComponent } from '../../core/message-dialog/message-dialog.component'; 

@Component({
  selector: 'ebx-doc-download-button',
  templateUrl: './doc-download-button.component.html',
  styleUrls: ['./doc-download-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DocDownloadButtonComponent implements OnInit {

  _record: any;
  @Input() container: Container;

  downloadUrl: string;

  constructor(private alert: AlertService, private apiUrlService: ApiUrlService,
      private dialog: MatDialog) { }

  @Input()
  set record(rec: any) {
    this._record = rec;
    if (rec) {
      this.downloadUrl = this.apiUrlService.getDocDownloadUrl(this._record.__decoration.__docMetadata.identity) + "?atChannel=WEB";
    }
  }

  get record() {
    return this._record;
  }

  ngOnInit() {
  }

  docDownload() {
    
    var continueDownload = true;
      
    if (this._record && this._record.__decoration.indicators 
        && this._record.__decoration.indicators["DOWNLOAD_MSG"]) {
      
      var downloadMsgs = this._record.__decoration.indicators["DOWNLOAD_MSG"];
      
      if (downloadMsgs && downloadMsgs.length > 0) {
        
        for (var i = 0; i < downloadMsgs.length; i++) {
          
          var downloadMsg = downloadMsgs[i];
          
          if (downloadMsg.value) {
            
            continueDownload = false;
            
            var dialogRef = this.dialog.open(MessageDialogComponent, {
              width: '624px',
              disableClose: true,
              data: { 
                title: "Information",
                text: downloadMsg.config.message,
                okLabel: "OK",
                type: "INFO"
              }
            });

            dialogRef.afterClosed().subscribe(res => {
              this.goDownload();
            });

            break;
          }
        }
      }
      
    } 

    if (continueDownload && this._record && this._record.__decoration.indicators 
        && this._record.__decoration.indicators["DOWNLOAD_PROMPT"]) {
      
      var downloadPrompts = this._record.__decoration.indicators["DOWNLOAD_PROMPT"];
      
      if (downloadPrompts && downloadPrompts.length > 0) {
        
        for (var i = 0; i < downloadPrompts.length; i++) {
        
          var downloadPrompt = downloadPrompts[i];
          
          if (downloadPrompt.value) {
        
            continueDownload = false;  
            
            var confirmRef = this.dialog.open(ConfirmDialogComponent, {
              width: '624px',
              disableClose: true,
              data: { 
                title: "Confirm",
                text: downloadPrompt.config.message,
                confirmLabel: "Proceed",
                cancelLabel: "Cancel"
              }
            });

            confirmRef.afterClosed().subscribe(res => {
              if (res) {
                this.goDownload();  
              }
            });

            break;
          }
        }
      }
    }  

    if (continueDownload) {
      this.goDownload();
    }
  }

  goDownload() {
    //this trick will generate a temp <a /> tag
    var link = document.createElement("a");    
    link.href = this.downloadUrl;
    
    //set the visibility hidden so it will not effect on your web-layout
    link.hidden = true;
    
    //this part will append the anchor tag and remove it after automatic click
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
