import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { ContentTemplateService } from '../../core/content-template.service';
import { SearchBarService } from '../../core/search-bar/search-bar.service';
import { GlobalSearchControllerService } from '../../core/search-bar/global-search-controller.service';
import { ContentService } from '../../core/content/content.service';
import { DataPage } from '../../model/data-page.model';
import { AlertService } from '../../core/alert/alert.service';
import { FilterMetadata } from '../../core/data-search/filter-metadata.model';
import { DataSearchRequest } from '../../core/data-search/data-search-request.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { Constants } from '../../util/constants';

@Component({
  selector: 'ebx-biz-content-list',
  templateUrl: './biz-content-list.component.html',
  styleUrls: ['./biz-content-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BizContentListComponent implements OnInit {

  container: Container;
  dataPage: DataPage;

  filterMetadata: { [key: string] : FilterMetadata } = {};
  filters: { [key: string] : any} = {};

  textFilter: string;

  constructor(private route: ActivatedRoute, 
    private contentTemplateService: ContentTemplateService,
    private contentService: ContentService,
    private alert: AlertService,
    private sbService: SearchBarService,
    private globalSearchCtrl: GlobalSearchControllerService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {

      var containerQId = params['cQId'];

      if (containerQId) {

        this.container = this.contentTemplateService.getConcreteContainerByQId(containerQId);
        
        this.filterMetadata = this.contentTemplateService.getBoundedFiltermetadata(this.container);

        this.route.queryParams.subscribe(queryParams => {
          
          this.filters = this.sbService.buildFiltersFromQueryParams(queryParams);
          let filterIds = this.sbService.getFilterIdsFromQueryParams(queryParams);
          this.textFilter = this.sbService.getTextQueryFromQueryParams(queryParams);
          this.globalSearchCtrl.setBizContentListSearchBar(this.container, filterIds, this.textFilter);

          this.fetchData();
        });

        //this.fetchData();
      }
    });


  }

  fetchData() {
    
    let searchRequest = new DataSearchRequest();
    
    searchRequest.projectedFields = [];
    searchRequest.filters = this.filters;
    searchRequest.filterMetadata = this.filterMetadata;

    searchRequest.pagination = new Pagination();
    searchRequest.pagination.pageNum = 0;
    searchRequest.pagination.pageSize = 40;
    
    searchRequest.pagination.sort = new SortCriteria();
    searchRequest.pagination.sort.field = Constants.FLD_MODIFIED_AT
    searchRequest.pagination.sort.direction = Direction.DESC;

    searchRequest.textQuery = this.textFilter;

    this.contentService.getFilteredRecords(this.container.qualifiedId, searchRequest).subscribe(
      result => {
         this.dataPage = result;
      },
      error => {
        this.alert.error("Error fetching content records. Please try again later.", error.status); 
      }
    );
  }

}
