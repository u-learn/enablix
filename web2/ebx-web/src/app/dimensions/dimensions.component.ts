import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentTemplateService } from '../core/content-template.service';
import { Container } from '../model/container.model';
import { NavigationService } from '../app-routing/navigation.service';

@Component({
  selector: 'ebx-dimensions',
  templateUrl: './dimensions.component.html',
  styleUrls: ['./dimensions.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DimensionsComponent implements OnInit {

  @Input() dims?: string = "portal-home";

  dimContainers: Container[];

  constructor(private contentTemplateService: ContentTemplateService,
              private navService: NavigationService) { }

  ngOnInit() {
    if (this.dims === "all") {
      this.dimContainers = this.contentTemplateService.templateCache.bizDimensionContainers;
    } else {
      this.dimContainers = this.contentTemplateService.getPortalDimensionContainers();  
    }
    
  }

  
}
