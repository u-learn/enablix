import { Component, OnInit, ViewEncapsulation, Input, ChangeDetectorRef, EventEmitter, Output } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { MatDialog, MatDialogRef } from '@angular/material';

import { ImportRecord, ImportRequest } from '../bulk-import.model';
import { TableColumn, TableActionConfig, TableAction } from '../../model/table.model';
import { ContentTemplateService } from '../../content-template.service';
import { ContentService } from '../../content/content.service';
import { AlertService } from '../../alert/alert.service';
import { SelectOption } from '../../select/select.component';
import { ContentItem } from '../../../model/content-item.model';
import { BulkSelectTypeComponent } from '../bulk-select-type/bulk-select-type.component';
import { BulkAddTagsComponent } from '../bulk-add-tags/bulk-add-tags.component';

@Component({
  selector: 'ebx-import-edit',
  templateUrl: './import-edit.component.html',
  styleUrls: ['./import-edit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ImportEditComponent implements OnInit {

  @Output() onImportSubmit = new EventEmitter<any>();

  _importRequest: ImportRequest;
  tableData: any;

  contentTypes: SelectOption[];

  tableActions: ImportRecordActions;

  tagItems: ContentItem[];

  @Input() 
  set importRequest(request: ImportRequest) {
    this._importRequest = request;
    this.tableData = {
      content: request.records
    };
  }

  get importRequest() {
    return this._importRequest;
  }

  tableColumns: TableColumn[];

  constructor(private contentTemplateService: ContentTemplateService,
    private contentService: ContentService,
    private alert: AlertService,
    private changeDetector: ChangeDetectorRef,
    private dialog: MatDialog) { }

  ngOnInit() {
    this.tableColumns = [
      {
        heading: "Title",
        key: "asset"
      },
      {
        heading: "Content Type",
        key: "contentType"
      },
      {
        heading: "Tags",
        key: "tags"
      }
    ];

    this.contentTypes = this.contentTemplateService.getBizContentSelectOptions();
    this.contentTypes.sort((a, b) => (a.label > b.label ? 1 : -1));

    this.tableActions = new ImportRecordActions(this, this.changeDetector);

    this.tagItems = this.contentTemplateService.getBizDimBoundedItems();
  }

  bulkTypeSelect(selRecords: any[]) {
    
    let dialogRef = this.dialog.open(BulkSelectTypeComponent, {
      width: '624px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe((res: any) => {
      if (res) {
        var jsonObj = JSON.stringify(res);
        selRecords.forEach((rec) => {
          rec.contentType = JSON.parse(jsonObj);
        });
      }
    });
  }

  bulkDelete(selRecords: any[]) {
    
    selRecords.forEach((selRec) => {
      
      var indx = -1;
      
      for(var i = 0; i < this.tableData.content.length; i++) {
        let rec = this.tableData.content[i];
        if (rec.id == selRec.id) {
          indx = i;
          break;
        }
      }

      if (indx >= 0) {
        this.tableData.content.splice(indx, 1);
      }

    });

    return Observable.of(1);
  }



  bulkTagsUpdate(selRecords: any[]) {

    let dialogRef = this.dialog.open(BulkAddTagsComponent, {
      width: '624px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe((res: any) => {
      if (res) {
        var jsonObj = JSON.stringify(res);
        selRecords.forEach((rec) => {
          rec.tags = JSON.parse(jsonObj);
        });
      }
    });

  }

  submitImportRequest() {
    
    var hasErrors = false;

    this.tableData.content.forEach((rec: any) => {
      
      rec.errors = {};
      
      if (!rec.title || rec.title.trim().length == 0) {
        rec.errors.title = ["Please enter title"];
        hasErrors = true;
      }

      if (!rec.contentType || rec.contentType.length == 0) {
        rec.errors.contentType = ["Please select content type"];
        hasErrors = true;
      }

    });

    if (hasErrors) {
      return;
    }

    this.importRequest.records.forEach((rec: ImportRecord) => {
      rec.contentQId = rec.contentType[0].id;
    });

    this.contentService.submitImportRequest(this.importRequest).subscribe(
      (res: any) => {
        this.onImportSubmit.emit(res);
        this.changeDetector.detectChanges();
      },
      (err) => {
        this.alert.error("Error submitting request. Please try later.", err.statusCode);
        this.changeDetector.detectChanges();
      }
    );
  }

  getTagItems(rec: ImportRecord) {
    
    var boundedItems = null;
    
    if (rec.contentType) {
      var contentQId = rec.contentType[0].id;
      var container = this.contentTemplateService.getContainerByQId(contentQId);
      if (container) {
        boundedItems = container.contentItem.filter(item => item.bounded);
      }
    }

    return boundedItems;
  }

}

export class ImportRecordActions implements TableActionConfig<any> {

  component: ImportEditComponent;
  allActions: TableAction<any>[] = [];

  constructor(comp: ImportEditComponent, private changeDetector: ChangeDetectorRef) {
    
    this.component = comp;

    this.allActions.push({
      label: "Remove",
      iconClass: "trash",
      successMessage: "Removed",
      execute: this.component.bulkDelete.bind(this.component),
      confirmConfig: {
        title: "Remove Asset",
        confirmMsg: this.getDeleteConfirmText,
        okLabel: "Remove Asset",
        cancelLabel: "No, keep it."
      }
    });

    this.allActions.push({
      label: "Select Content Type",
      iconClass: "classify",
      successMessage: "",
      execute: this.component.bulkTypeSelect.bind(this.component)
    });

    this.allActions.push({
      label: "Add Tags",
      iconClass: "tags",
      successMessage: "Tags Added",
      execute: this.component.bulkTagsUpdate.bind(this.component)
    });
  }

  getDeleteConfirmText(selRecords: any[]) : string {
    if (selRecords.length == 1) {
      return "You are about to remove '" + selRecords[0].title 
        + "' asset.<br>Would you like to proceed?";
    } else {
      return "You are about to remove multiple records."
        + "<br>Would you like to proceed?"; 
    }
  }

  getAvailableActions(selectedRecords: any[]) : TableAction<any>[] {
    return selectedRecords && selectedRecords.length > 0 ? this.allActions : [];
  }

}
