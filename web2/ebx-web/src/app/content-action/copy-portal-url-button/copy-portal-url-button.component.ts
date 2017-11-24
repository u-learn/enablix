import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';

@Component({
  selector: 'ebx-copy-portal-url-button',
  templateUrl: './copy-portal-url-button.component.html',
  styleUrls: ['./copy-portal-url-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CopyPortalUrlButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  constructor(private alert: AlertService, private dialog: MatDialog) { }

  ngOnInit() {
  }

  copy() {
    console.log("Copy action");
  }

}
