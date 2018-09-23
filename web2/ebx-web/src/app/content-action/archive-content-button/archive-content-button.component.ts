import { Component, OnInit, ViewEncapsulation, Input, EventEmitter, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ConfirmDialogComponent } from '../../core/confirm-dialog/confirm-dialog.component'; 
import { ContentWorkflowService } from '../../services/content-workflow.service';

@Component({
  selector: 'ebx-archive-content-button',
  templateUrl: './archive-content-button.component.html',
  styleUrls: ['./archive-content-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArchiveContentButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  @Output() onArchive = new EventEmitter<boolean>();
  @Output() onUnarchive = new EventEmitter<boolean>();

  archiveAction: boolean = true;

  constructor(private contentService: ContentService,
      private alert: AlertService) { }

  ngOnInit() {
    this.archiveAction = !this.record.archived;
  }

  archiveContent() {
    this.contentService.archiveRecord(this.record.identity, this.container.qualifiedId).subscribe(
      (result: any) => {
        if (result && result.result == 'success') {
          this.alert.success("Asset archived successfully.", true);
          this.onArchive.emit(true);
        } else {
           this.alert.error("Unable to archive asset. Please try again later.", 404); 
        }
      },
      err => {
        this.alert.error("Unable to archive asset. Please try again later.", err.statusCode);
      }
    );
  }

  unarchiveContent() {
    this.contentService.unarchiveRecord(this.record.identity, this.container.qualifiedId).subscribe(
      (result: any) => {
        if (result && result.result == 'success') {
          this.alert.success("Asset un-archived successfully.", true);
          this.onUnarchive.emit(true);
        } else {
           this.alert.error("Unable to un-archive asset. Please try again later.", 404); 
        }
      },
      err => {
        this.alert.error("Unable to un-archive asset. Please try again later.", err.statusCode);
      }
    );
  }

}
