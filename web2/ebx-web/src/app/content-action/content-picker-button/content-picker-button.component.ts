import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { ContentBrowserComponent } from '../../content-browser/content-browser.component';

@Component({
  selector: 'ebx-content-picker-button',
  templateUrl: './content-picker-button.component.html',
  styleUrls: ['./content-picker-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentPickerButtonComponent implements OnInit {

  @Input() preSelected?: any;
  @Output() onSelectionDone = new EventEmitter<any>();

  constructor(private dialog: MatDialog) { }

  ngOnInit() {
  }

  openPicker() {
    
    let dialogRef = this.dialog.open(ContentBrowserComponent, {
        width: '80vw',
        height: '90vh',
        disableClose: true,
        data: { selectedItems: this.preSelected }
      });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.onSelectionDone.emit(result);
      }
    });
  }
}
