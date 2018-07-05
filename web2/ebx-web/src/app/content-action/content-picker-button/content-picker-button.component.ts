import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { ContentBrowserComponent } from '../../content-browser/content-browser.component';
import { Container } from '../../model/container.model';
@Component({
  selector: 'ebx-content-picker-button',
  templateUrl: './content-picker-button.component.html',
  styleUrls: ['./content-picker-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentPickerButtonComponent implements OnInit {

  @Input() preSelected?: any;
  @Input() scopeContainer?: Container;
  @Output() onSelectionDone = new EventEmitter<any>();

  constructor(private dialog: MatDialog) { }

  ngOnInit() {
  }

  openPicker() {
    
    let dialogRef = this.dialog.open(ContentBrowserComponent, {
        width: '80vw',
        height: '90vh',
        disableClose: true,
        autoFocus: false,
        data: { 
          selectedItems: this.preSelected,
          scopeContainer: this.scopeContainer
        }
      });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.onSelectionDone.emit(result);
      }
    });
  }
}
