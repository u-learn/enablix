import { Component, OnInit, ViewEncapsulation, Input, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { ContentTemplateService } from '../../core/content-template.service';
import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ContentWorkflowService } from '../../services/content-workflow.service';

@Component({
  selector: 'ebx-edit-biz-dimension',
  templateUrl: './edit-biz-dimension.component.html',
  styleUrls: ['./edit-biz-dimension.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class EditBizDimensionComponent implements OnInit {

  container: Container;
  record: any;

  approvalWFRequired: boolean;

  constructor(
    private ctService: ContentTemplateService,
    private contentService: ContentService,
    private alert: AlertService,
    private cwService: ContentWorkflowService,
    private dialogRef: MatDialogRef<EditBizDimensionComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {

  }

  ngOnInit() {
    
    this.container = this.ctService.getContainerByQId(this.data.containerQId);

    this.approvalWFRequired = this.cwService.isApprovalWFRequired();

    if (this.data.newRecord) {
      
      this.record = {};

    } else if (this.data.recordIdentity) {
      
      this.contentService.getContentRecord(this.data.containerQId, this.data.recordIdentity).subscribe(res => {
        this.record = res;
      }, err => {
        this.alert.error("Error fetching record details.", err.status);
      });
    }
  }

  save() {

    if (this.approvalWFRequired) {

      this.cwService.submitContent(this.container.qualifiedId, this.record, false, null).subscribe(
        res => {
          var msg = this.record.identity ? "Object update request accepted." :
                      "Object add request accepted.";
          this.alert.success(msg, true);
          this.dialogRef.close(true);
        }, 
        err => {
          this.alert.error("Error saving data. Please try later.", err.status);
        }
      );

    } else {
      
      this.contentService.saveContainerData(this.container.qualifiedId, this.record).subscribe(
        res => {
          var msg = this.record.identity ? "Object updated successfully." :
                      "Object added successfully.";
          this.alert.success(msg, true);
          this.dialogRef.close(true);
        }, 
        err => {
          this.alert.error("Error saving data. Please try later.", err.status);
        }
      );
    }
  }

}
