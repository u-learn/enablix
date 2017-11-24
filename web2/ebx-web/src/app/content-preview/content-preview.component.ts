import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentPreviewService } from '../core/content/content-preview.service';
import { Container } from '../model/container.model';

@Component({
  selector: 'ebx-content-preview',
  templateUrl: './content-preview.component.html',
  styleUrls: ['./content-preview.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ContentPreviewComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;
  @Input() editing: boolean = false;

  previewType: string;

  constructor(private contentPreviewService: ContentPreviewService) { }

  ngOnInit() {
    let previewHandler = this.contentPreviewService.getPreviewHandler(this.record);
    if (previewHandler) {
      this.previewType = previewHandler.type();
    }
  }

}
