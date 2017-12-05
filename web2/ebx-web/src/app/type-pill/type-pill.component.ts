import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentTemplateService } from '../core/content-template.service';

@Component({
  selector: 'ebx-type-pill',
  templateUrl: './type-pill.component.html',
  styleUrls: ['./type-pill.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TypePillComponent implements OnInit {

  @Input() containerQId: string;

  label: string;
  color: string;
  isBizContent: boolean = false;

  constructor(private ctService: ContentTemplateService) { }

  ngOnInit() {
    let container = this.ctService.getConcreteContainerByQId(this.containerQId);
    if (container) {
      this.label = container.label;
      this.color = container.color;
      this.isBizContent = this.ctService.isBusinessContent(container);
    }
  }

}
