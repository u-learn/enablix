import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ContentTemplateService } from '../core/content-template.service';
import { NavigationService } from '../app-routing/navigation.service';

@Component({
  selector: 'ebx-container-detail-url-mapper',
  templateUrl: './container-detail-url-mapper.component.html',
  styleUrls: ['./container-detail-url-mapper.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ContainerDetailUrlMapperComponent implements OnInit {

  constructor(private router: Router, private route: ActivatedRoute,
          private ctService: ContentTemplateService, 
          private navService: NavigationService) { }

  ngOnInit() {
    
    this.route.params.subscribe(params => {
      
      var cQId = params['cQId'];
      var recIdentity = params['recordIdentity'];

      let container = this.ctService.getConcreteContainerByQId(cQId);
      
      if (this.ctService.isBusinessContent(container)) {
        this.navService.goToContentDetail(container.qualifiedId, recIdentity);
      } else {
        this.navService.goToDimDetail(container.qualifiedId, recIdentity);
      }

    });
  }

}
