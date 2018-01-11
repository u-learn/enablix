import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ContentTemplateService } from '../core/content-template.service';
import { NavigationService } from '../app-routing/navigation.service';

@Component({
  selector: 'ebx-container-list-url-mapper',
  templateUrl: './container-list-url-mapper.component.html',
  styleUrls: ['./container-list-url-mapper.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ContainerListUrlMapperComponent implements OnInit {

  constructor(private router: Router, private route: ActivatedRoute,
          private ctService: ContentTemplateService, 
          private navService: NavigationService) { }

  ngOnInit() {
    
    this.route.params.subscribe(params => {
      
      var cQId = params['cQId'];
      let container = this.ctService.getConcreteContainerByQId(cQId);
      
      if (this.ctService.isBusinessContent(container)) {
        this.navService.goToContentList(container.qualifiedId);
      } else {
        this.navService.goToDimList(container.qualifiedId);
      }

    });
  }

}
