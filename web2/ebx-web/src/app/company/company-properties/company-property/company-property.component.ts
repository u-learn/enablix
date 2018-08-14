import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { Container } from '../../../model/container.model';
import { ContentService } from '../../../core/content/content.service';
import { AlertService } from '../../../core/alert/alert.service';
import { EditCompanyPropValueComponent } from './edit-company-prop-value/edit-company-prop-value.component';
import { ConfirmDialogComponent } from '../../../core/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'ebx-company-property',
  templateUrl: './company-property.component.html',
  styleUrls: ['./company-property.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CompanyPropertyComponent implements OnInit {

  @Input() container: Container;

  propData: any;

  constructor(private contentService: ContentService,
    private alert: AlertService,
    private dialog: MatDialog) { }

  ngOnInit() {
    this.contentService.getAllRecords(this.container.qualifiedId, 100).subscribe(
        result => {
          if (result) {
            this.propData = result;
          }
        },
        err => {
          this.alert.error("Unable to fetch property list for " + this.container.label, err.statusCode);
        }
      );
  }

  fetchData() {
    this.contentService.getAllRecords(this.container.qualifiedId, 100).subscribe(
        result => {
          if (result) {
            this.propData = result;
          }
        },
        err => {
          this.alert.error("Unable to fetch property list for " + this.container.label, err.statusCode);
        }
      );
  }

  addNewPropValue() {
    this.editPropertyValue({}, true);
  }

  editPropertyValue(rec: any, newRecord: boolean = false) {

    let dialogRef = this.dialog.open(EditCompanyPropValueComponent, {
        width: '624px',
        disableClose: true,
        data: { 
          containerQId: this.container.qualifiedId, 
          newRecord: newRecord, 
          recordIdentity: rec.identity 
        }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.fetchData();
      }
    });
  }

  deletePrompt(rec: any) {

    var promptText = "You are about to delete the property value '" 
        + rec[this.container.titleItemId] + "'. Would you like to proceed?";
    
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '624px',
        disableClose: true,
        data: { 
          title: "Delete Property",
          text: promptText,
          confirmLabel: "Delete",
          cancelLabel: "No, keep it."
        }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.deleteRecord(rec);
      }
    })
  }

  deleteRecord(rec: any) {
    
    this.contentService.deleteContentRecord(this.container.qualifiedId, rec.identity).subscribe(res => {
      this.fetchData();
      this.alert.success("Property delete successfully.", true);
    }, err => {
      this.alert.error("Unable to delete. Please try later.", err.status);
    });  
    
  }

}
