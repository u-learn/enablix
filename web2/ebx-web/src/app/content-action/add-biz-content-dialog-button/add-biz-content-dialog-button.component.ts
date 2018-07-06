import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material';

import { Container } from '../../model/container.model';
import { BizContentComponent } from '../../biz-content/biz-content.component';
import { ContentService } from '../../core/content/content.service';

@Component({
  selector: 'ebx-add-biz-content-dialog-button',
  templateUrl: './add-biz-content-dialog-button.component.html',
  styleUrls: ['./add-biz-content-dialog-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AddBizContentDialogButtonComponent implements OnInit {

  @Input() container: Container;
  @Output() onDialogClose = new EventEmitter<any>();

  constructor(private dialog: MatDialog, private contentService: ContentService) { }

  ngOnInit() {
  }

  openNewRecordDialog() {
    
    var record = {};
    this.contentService.decorateRecord(this.container, record);

    let dialogRef = this.dialog.open(BizContentComponent, {
        width: '80vw',
        height: '90vh',
        disableClose: true,
        autoFocus: false,
        data: { 
          record: record,
          container: this.container,
          editing: true
        }
      });

    dialogRef.afterClosed().subscribe(result => {
      this.onDialogClose.emit(result);
    });
  }

}
