import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'ebx-message-dialog',
  templateUrl: './message-dialog.component.html',
  styleUrls: ['./message-dialog.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MessageDialogComponent implements OnInit {

  title: string;
  text: string;
  type?: string = "INFO";
  okLabel: string;

  constructor(private dialogRef: MatDialogRef<MessageDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    if (this.data) {
      this.title = this.data.title;
      this.text = this.data.text;
      this.okLabel = this.data.okLabel ? this.data.okLabel : "Ok";
      this.type = this.data.type ? this.data.type : "INFO";
    } else {
      this.ok();
    }
  }

  ok() {
    this.dialogRef.close(true);
  }

}
