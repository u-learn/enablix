import { Component, OnInit, ViewEncapsulation, Input, EventEmitter, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ConfirmDialogComponent } from '../../confirm-dialog/confirm-dialog.component'; 
import { ContentWorkflowService } from '../../services/content-workflow.service';

@Component({
  selector: 'ebx-content-delete-button',
  templateUrl: './content-delete-button.component.html',
  styleUrls: ['./content-delete-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentDeleteButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;
  @Input() contentType: string;
  @Input() crRecord?: any;

  @Output() onDelete = new EventEmitter<boolean>();

  constructor(private contentService: ContentService,
              private alert: AlertService, 
              private contentWFService: ContentWorkflowService,
              private dialog: MatDialog) { 
    this.onDelete = new EventEmitter<boolean>();
  }

  ngOnInit() {
  }

  getDeleteConfirmText() : string {
    return "You are about to delete '" + this.record.__title 
              + "' " + this.contentType.toLowerCase() + ".<br>"
              + "You won't be able to restore it. Would you like to proceed?";
  }

  deletePrompt() {
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '624px',
        disableClose: true,
        data: { 
          title: "Delete " + this.contentType,
          text: this.getDeleteConfirmText(),
          confirmLabel: "Delete " + this.contentType,
          cancelLabel: "No, keep it."
        }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.deleteRecord();
      }
    })
  }

  deleteRecord() {
    
    if (this.crRecord) {

      this.contentWFService.deleteContentRequest(this.crRecord.objectRef.identity).subscribe(res => {
        this.alert.success("Deleted successfully.");
        this.onDelete.emit(true);  
      }, err => {
        this.alert.error("Unable to delete. Please try later.", err.status);
      });

    } else {
      
      this.contentService.deleteContentRecord(this.container.qualifiedId, this.record.identity).subscribe(res => {
        this.alert.success("Deleted successfully.");
        this.onDelete.emit(true);
      }, err => {
        this.alert.error("Unable to delete. Please try later.", err.status);
      });  
    }
    
  }

}
