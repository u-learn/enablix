import { Component, OnInit, ViewEncapsulation , Input, Output, EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';

@Component({
  selector: 'ebx-content-email-button',
  templateUrl: './content-email-button.component.html',
  styleUrls: ['./content-email-button.component.css'],
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
    console.log("Email action");
  }

}
