import { Component, OnInit, ViewEncapsulation, Input, EventEmitter, Output } from '@angular/core';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ConfirmDialogComponent } from '../../confirm-dialog/confirm-dialog.component'; 
import { ContentWorkflowService } from '../../services/content-workflow.service';


@Component({
  selector: 'ebx-content-req-approve-button',
  templateUrl: './content-req-approve-button.component.html',
  styleUrls: ['./content-req-approve-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentReqApproveButtonComponent implements OnInit {

  @Input() container: Container;
  @Input() crRecord?: any;

  @Output() onApprove = new EventEmitter<boolean>();

  constructor(private contentService: ContentService,
              private alert: AlertService, 
              private contentWFService: ContentWorkflowService) { }

  ngOnInit() {
  }

  approveRecord() {
    
    if (this.crRecord) {

      this.contentWFService.approveContentRequest(this.crRecord.identity, null).subscribe(res => {
        this.alert.success("Content approved.");
        this.onApprove.emit(true);  
      }, err => {
        this.alert.error("Unable to approve. Please try later.", err.status);
      });

    }
    
  }

}
