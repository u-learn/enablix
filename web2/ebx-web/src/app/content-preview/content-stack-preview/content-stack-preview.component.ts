import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';

@Component({
  selector: 'ebx-content-stack-preview',
  templateUrl: './content-stack-preview.component.html',
  styleUrls: ['./content-stack-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentStackPreviewComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  showCount: number = 5;

  contentStack: any;

  constructor(private contentService: ContentService,
    private alert: AlertService) { }

  ngOnInit() {
    this.contentService.getRecordContentStack(this.container.qualifiedId, this.record.identity).subscribe(
      (result: any) => {
        console.log(result);
        this.contentService.decorateContentGroup(result);
        this.contentStack = result;
      }, 
      (error: any) => {
        this.alert.error("Error fetch asset details", error.statusCode);
      }
    );
  }

}
