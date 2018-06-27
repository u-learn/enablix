import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ContentTemplateService } from '../../core/content-template.service';
import { Utility } from '../../util/utility';

@Component({
  selector: 'ebx-content-stack-preview',
  templateUrl: './content-stack-preview.component.html',
  styleUrls: ['./content-stack-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentStackPreviewComponent implements OnInit {

  _record: any;
  @Input() container: Container;
  @Input() editing: boolean;

  @Output() onStackUpdate = new EventEmitter<void>();

  showCount: number = 5;

  stackArr: any;
  contentStack: any;
  heading: string;

  @Input() set record(rec: any) {
    this._record = rec;
    this.initStackInfo();
  }

  get record() {
    return this._record
  }

  constructor(private contentService: ContentService,
    private alert: AlertService,
    private ctService: ContentTemplateService) { }

  ngOnInit() {

    if (this.container.contentStackItemId) {
      var stackQId = Utility.getQId(this.container.qualifiedId, this.container.contentStackItemId);
      var cItem = this.ctService.templateCache.contentStackQIdMap[stackQId];  
      if (cItem) {
        this.heading = cItem.label;
      }
    }
    
  }

  initStackInfo() {

    this.stackArr = this.record[this.container.contentStackItemId];

    this.contentService.getContentStackDetails(this.stackArr).subscribe(
      (result: any) => {
        this.contentService.decorateContentGroup(result);
        this.contentStack = result;
      }, 
      (error: any) => {
        this.alert.error("Error fetch asset details", error.statusCode);
      }
    );
  }

  fetchStackDetails() {
    if (this.stackArr) {
      this.contentService.getContentStackDetails(this.stackArr).subscribe(
        (result: any) => {
          this.contentService.decorateContentGroup(result);
          this.contentStack = result;
        }, 
        (error: any) => {
          this.alert.error("Error fetch asset details", error.statusCode);
        }
      );
    }
  }

  selectionDone(stackSelection: any) {
    if (stackSelection) {
      this.stackArr = stackSelection;
      this.record[this.container.contentStackItemId] = this.stackArr;
      this.fetchStackDetails();
      this.onStackUpdate.emit();
    }
  }

}
