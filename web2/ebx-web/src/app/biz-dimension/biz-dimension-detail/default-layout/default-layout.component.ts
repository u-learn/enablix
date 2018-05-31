import { Component, OnInit, ViewEncapsulation, Input, EventEmitter, Output } from '@angular/core';

import { ContentRecordGroup } from '../../../model/content-record-group.model';

@Component({
  selector: 'ebx-default-layout',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DefaultLayoutComponent implements OnInit {

  @Input() contentGroups: ContentRecordGroup[];

  @Output() onNavToAllChildType = new EventEmitter<ContentRecordGroup>();

  constructor() { }

  ngOnInit() {
  }

  navToAllContent(cg: ContentRecordGroup) {
    this.onNavToAllChildType.emit(cg);
  }

}
