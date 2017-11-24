import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { ContentTemplateService } from '../../core/content-template.service';

@Component({
  selector: 'ebx-biz-dimension-list',
  templateUrl: './biz-dimension-list.component.html',
  styleUrls: ['./biz-dimension-list.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class BizDimensionListComponent implements OnInit {

  container: Container;

  constructor(private route: ActivatedRoute, private contentTemplateService: ContentTemplateService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      var containerQId = params['cQId'];
      if (containerQId) {
        this.container = this.contentTemplateService.getContainerByQId(containerQId);
      }
    });
  }

}
