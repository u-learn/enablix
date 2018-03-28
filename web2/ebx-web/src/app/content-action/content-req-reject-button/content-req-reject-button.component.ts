import { Component, OnInit, ViewEncapsulation, Input, EventEmitter, Output } from '@angular/core';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ConfirmDialogComponent } from '../../core/confirm-dialog/confirm-dialog.component'; 
import { ContentWorkflowService } from '../../services/content-workflow.service';


@Component({
  selector: 'ebx-content-req-reject-button',
  templateUrl: './content-req-reject-button.component.html',
  styleUrls: ['./content-req-reject-button.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentReqRejectButtonComponent implements OnInit {

  @Input() container: Container;
  @Input() crRecord?: any;

  @Output() onReject = new EventEmitter<any>();

  constructor(private contentService: ContentService,
              private alert: AlertService, 
              private contentWFService: ContentWorkflowService) { }

  ngOnInit() {
  }

  rejectRecord() {
    
    if (this.crRecord) {

      this.contentWFService.rejectContentRequest(this.crRecord.objectRef.identity, null).subscribe(res => {
        this.alert.success("Content rejected.", true);
        this.onReject.emit(res);  
      }, err => {
        this.alert.error("Unable to reject. Please try later.", err.status);
      });

    }
    
  }

}
