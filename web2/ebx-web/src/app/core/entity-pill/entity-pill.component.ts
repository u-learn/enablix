import { Component, OnInit, ViewEncapsulation, Input, EventEmitter, Output } from '@angular/core';

import { ContentTemplateService } from '../content-template.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { Constants } from '../../util/constants';

@Component({
  selector: 'ebx-entity-pill',
  templateUrl: './entity-pill.component.html',
  styleUrls: ['./entity-pill.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class EntityPillComponent implements OnInit {

  @Input() containerQId: string;
  @Input() identity: string;
  @Input() label: string;
  @Input() navigable: boolean = false;
  @Input() removable: boolean = false;

  @Output() onRemove = new EventEmitter<void>();
  @Output() onClick = new EventEmitter<void>();

  color: string;
  isBizContent: boolean = false;
  isBizDim: boolean = false;

  constructor(private ctService: ContentTemplateService, private navService: NavigationService) { }

  ngOnInit() {
    
    if (this.containerQId) {
      
      let container = this.ctService.getConcreteContainerByQId(this.containerQId);
      
      if (container) {
        this.color = container.color;
        this.isBizContent = this.ctService.isBusinessContent(container);
        this.isBizDim = this.ctService.isBusinessDimension(container);
      } else {
        this.color = Constants.defaultTypeColor;
      }

    } else {
      this.color = Constants.defaultTypeColor;
    }
  }

  onPillClick() {
    this.onClick.emit();
    this.navToContentDetail();
  }

  navToContentDetail() {
    if (this.navigable) {
      if (this.isBizContent) {
        this.navService.goToContentDetail(this.containerQId, this.identity, {});
      } else if (this.isBizDim) {
        this.navService.goToDimDetail(this.containerQId, this.identity);
      }
    }
    
  }

  remove() {
    this.onRemove.emit();
  }

}
