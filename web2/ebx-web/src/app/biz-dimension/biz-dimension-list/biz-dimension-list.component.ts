import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { ContentTemplateService } from '../../core/content-template.service';
import { SearchBarService } from '../../core/search-bar/search-bar.service';
import { GlobalSearchControllerService } from '../../core/search-bar/global-search-controller.service';
import { FilterMetadata } from '../../core/data-search/filter-metadata.model';

@Component({
  selector: 'ebx-biz-dimension-list',
  templateUrl: './biz-dimension-list.component.html',
  styleUrls: ['./biz-dimension-list.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class BizDimensionListComponent implements OnInit {

  container: Container;
  filterMetadata: { [key: string] : FilterMetadata };
  filters: { [key: string] : any};

  constructor(private route: ActivatedRoute, 
    private contentTemplateService: ContentTemplateService,
    private sbService: SearchBarService,
    private globalSearchCtrl: GlobalSearchControllerService) { 

  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      
      var containerQId = params['cQId'];
      
      if (containerQId) {
        
        this.container = this.contentTemplateService.getConcreteContainerByQId(containerQId);
        this.filterMetadata = this.contentTemplateService.getBoundedFiltermetadata(this.container);

        this.route.queryParams.subscribe(queryParams => {
          this.filters = this.sbService.buildFiltersFromQueryParams(queryParams);
          let filterIds = this.sbService.getFilterIdsFromQueryParams(queryParams);
          let textquery = this.sbService.getTextQueryFromQueryParams(queryParams);

          this.globalSearchCtrl.setBizDimListSearchBar(this.container, filterIds, textquery);
        });
      }
    });



  }

}
