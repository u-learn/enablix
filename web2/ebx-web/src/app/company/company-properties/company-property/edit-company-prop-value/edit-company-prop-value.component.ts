import { Component, OnInit, ViewEncapsulation, Input, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { ContentTemplateService } from '../../../../core/content-template.service';
import { Container } from '../../../../model/container.model';
import { ContentService } from '../../../../core/content/content.service';
import { AlertService } from '../../../../core/alert/alert.service';

@Component({
  selector: 'ebx-edit-company-prop-value',
  templateUrl: './edit-company-prop-value.component.html',
  styleUrls: ['./edit-company-prop-value.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class EditCompanyPropValueComponent implements OnInit {

  container: Container;
  record: any;

  constructor(public ctService: ContentTemplateService,
    private contentService: ContentService,
    private alert: AlertService,
    private dialogRef: MatDialogRef<EditCompanyPropValueComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {

    this.container = this.ctService.getContainerByQId(this.data.containerQId);

    if (this.data.newRecord) {
      
      this.record = {};

    } else if (this.data.recordIdentity) {
      
      this.contentService.getContentRecord(this.data.containerQId, this.data.recordIdentity).subscribe(res => {
        this.record = res;
      }, err => {
        this.alert.error("Error fetching property value details.", err.status);
      });
    }
  }

  save() {

    this.contentService.saveContainerData(this.container.qualifiedId, this.record).subscribe(
      res => {
        var msg = this.record.identity ? "Property updated successfully." :
                    "Property added successfully.";
        this.alert.success(msg, true);
        this.dialogRef.close(true);
      }, 
      err => {
        this.alert.error("Error saving data. Please try later.", err.status);
      }
    );
  }

  

}
