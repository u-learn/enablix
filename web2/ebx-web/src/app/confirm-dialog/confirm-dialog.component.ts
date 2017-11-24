import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'ebx-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ConfirmDialogComponent implements OnInit {

  title: string;
  text: string;
  confirmLabel: string;
  cancelLabel: string;

  constructor(private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    if (this.data) {
      this.title = this.data.title;
      this.text = this.data.text;
      this.confirmLabel = this.data.confirmLabel ? this.data.confirmLabel : "Ok";
      this.cancelLabel = this.data.cancelLabel ? this.data.cancelLabel : "Cancel";
    } else {
      this.cancel();
    }
  }

  confirm() {
    this.dialogRef.close(true);
  }

  cancel() {
    this.dialogRef.close(false);
  }

}
