import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentItem } from '../model/content-item.model';

@Component({
  selector: 'ebx-content-item',
  templateUrl: './content-item.component.html',
  styleUrls: ['./content-item.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentItemComponent implements OnInit {

  @Input() contentItem: ContentItem;
  @Input() record: any;
  @Input() editable?: boolean = false; 

  constructor() { }

  ngOnInit() {
  }

  getValue() : any {
    return this.record[this.contentItem.id];
  }

}