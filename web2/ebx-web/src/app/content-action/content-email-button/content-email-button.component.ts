import { Component, OnInit, ViewEncapsulation , Input, Output, EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { EmailSharePopupComponent } from './email-share-popup/email-share-popup.component'

@Component({
  selector: 'ebx-content-email-button',
  templateUrl: './content-email-button.component.html',
  styleUrls: ['./content-email-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentEmailButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  @Output() onEmailSent = new EventEmitter<boolean>();



  constructor(private alert: AlertService, private dialog: MatDialog) { }

  ngOnInit() {
  }

  email() {

    let dialogRef = this.dialog.open(EmailSharePopupComponent, {
        width: '624px',
        disableClose: true,
        data: { record: this.record, container: this.container }
      });

  }

}
