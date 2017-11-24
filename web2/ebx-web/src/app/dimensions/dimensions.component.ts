import { Component, OnInit, ViewEncapsulation } from '@angular/core';

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

  dimContainers: Container[];

  constructor(private contentTemplateService: ContentTemplateService,
              private navService: NavigationService) { }

  ngOnInit() {
    this.dimContainers = this.contentTemplateService.getPortalDimensionContainers();
    console.log(this.dimContainers);
  }

  
}
