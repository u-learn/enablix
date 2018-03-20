import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { Container } from '../../model/container.model';

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

  constructor() { }

  ngOnInit() {

  }

  getValue() {
    let pt = this.record[this.container.textItemId];
    let rt = this.record[this.container.textItemId + '_rt'];
    return rt ? rt : pt;
  }

}
