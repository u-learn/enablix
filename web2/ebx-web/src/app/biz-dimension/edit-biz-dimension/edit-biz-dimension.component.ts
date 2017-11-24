import { Component, OnInit, ViewEncapsulation, Input, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { ContentTemplateService } from '../../core/content-template.service';
import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';

@Component({
  selector: 'ebx-edit-biz-dimension',
  templateUrl: './edit-biz-dimension.component.html',
  styleUrls: ['./edit-biz-dimension.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class EditBizDimensionComponent implements OnInit {

  container: Container;
  record: any;

  constructor(
    private ctService: ContentTemplateService,
    private contentService: ContentService,
    private alert: AlertService,
    private dialogRef: MatDialogRef<EditBizDimensionComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {

  }

  ngOnInit() {
    
    this.container = this.ctService.getContainerByQId(this.data.containerQId);

    if (this.data.newRecord) {
      
      this.record = {};

    } else if (this.data.recordIdentity) {
      
      this.contentService.getContentRecord(this.data.containerQId, this.data.recordIdentity).subscribe(res => {
        this.record = res;
      }, err => {
        this.alert.error("Error fetching record details.");
      });
    }
  }

  save() {
    this.contentService.saveContainerData(this.container.qualifiedId, this.record).subscribe(
        res => {
          this.dialogRef.close(true);
        }, 
        err => {
          this.alert.error("Error saving data. Please try later.");
        }
      );
    
  }

}
