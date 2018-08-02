import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ContentTemplateService } from '../core/content-template.service';
import { NavigationService } from '../app-routing/navigation.service';
import { Utility } from '../util/utility';

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

      var atCtxParams = Utility.readTrackingCtxInQueryParams(this.route.snapshot.queryParams);

      let container = this.ctService.getConcreteContainerByQId(cQId);
      
      if (this.ctService.isBusinessContent(container)) {
        this.navService.goToContentDetail(container.qualifiedId, recIdentity, atCtxParams);
      } else {
        this.navService.goToDimDetail(container.qualifiedId, recIdentity, this.route.snapshot.queryParams);
      }

    });
  }

}
