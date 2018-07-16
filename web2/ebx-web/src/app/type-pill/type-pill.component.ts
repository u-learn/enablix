import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentTemplateService } from '../core/content-template.service';
import { NavigationService } from '../app-routing/navigation.service';

@Component({
  selector: 'ebx-type-pill',
  templateUrl: './type-pill.component.html',
  styleUrls: ['./type-pill.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TypePillComponent implements OnInit {

  @Input() containerQId: string;
  @Input() navigable: boolean = false;

  label: string;
  color: string;
  isBizContent: boolean = false;
  isBizDim: boolean = false;

  constructor(private ctService: ContentTemplateService, private navService: NavigationService) { }

  ngOnInit() {
    let container = this.ctService.getConcreteContainerByQId(this.containerQId);
    if (container) {
      this.label = container.singularLabel;
      this.color = container.color;
      this.isBizContent = this.ctService.isBusinessContent(container);
      this.isBizDim = this.ctService.isBusinessDimension(container);
    }
  }

  navToContentList() {
    if (this.navigable) {
      if (this.isBizContent) {
        this.navService.goToContentList(this.containerQId);
      } else if (this.isBizDim) {
        this.navService.goToDimList(this.containerQId);
      }
    }
  }

}
