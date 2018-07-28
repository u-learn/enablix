import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { Container } from '../../model/container.model';
import { EbxLinkifyPipe } from '../../core/pipes/ebx-linkify.pipe';

@Component({
  selector: 'ebx-text-content-preview',
  templateUrl: './text-content-preview.component.html',
  styleUrls: ['./text-content-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TextContentPreviewComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;
  @Input() editing: boolean = false;

  constructor(private linkify: EbxLinkifyPipe) { }

  ngOnInit() {

  }

  getValue() {
    let pt = this.record[this.container.textItemId];
    let rt = this.record[this.container.textItemId + '_rt'];
    
    let linkifyArgs = {
      target: '_blank',
      linkParams: {
        cQId: this.container.qualifiedId,
        cId: this.record.identity
      }
    }

    return this.linkify.transform(rt ? rt : pt, linkifyArgs);
  }

}
