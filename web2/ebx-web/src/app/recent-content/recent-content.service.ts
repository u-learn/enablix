import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
 
import { Constants } from '../util/constants';
import { DataSearchService } from '../core/data-search/data-search.service';
import { DataSearchRequest } from '../core/data-search/data-search-request.model';
import { Pagination, Direction, SortCriteria } from '../model/pagination.model';
import { DataPage } from '../model/data-page.model';
import { DataType, ConditionOperator } from '../core/data-search/filter-metadata.model';
import { ContentTemplateService } from '../core/content-template.service';

const RECENT_CONTENT_DOMAIN_TYPE = "com.enablix.core.domain.recent.RecentData";

@Injectable()
export class RecentContentService {

  pageSize: number = 5;

  constructor(private dataSearchService: DataSearchService, 
              private ctService: ContentTemplateService) { }

  fetchRecentContent() : Observable<DataPage> {

    let searchRequest = new DataSearchRequest();
    
    searchRequest.projectedFields = [];

    searchRequest.filterMetadata = {
      obsolete: {
        field: "obsolete",
        dataType: DataType.BOOL,
        operator: ConditionOperator.EQ
      },
      containerQIdIn: {
        field: "data.containerQId",
        dataType: DataType.STRING,
        operator: ConditionOperator.IN
      }
    };

    let bizContentQIds = this.ctService.templateCache.bizContentContainers.map(cont => cont.qualifiedId);

    searchRequest.filters = {
      obsolete: false,
      containerQIdIn: bizContentQIds
    };

    searchRequest.pagination = new Pagination();
    searchRequest.pagination.pageNum = 0;
    searchRequest.pagination.pageSize = this.pageSize;
    
    searchRequest.pagination.sort = new SortCriteria();
    searchRequest.pagination.sort.field = Constants.FLD_CREATED_AT;
    searchRequest.pagination.sort.direction = Direction.DESC;

    return this.dataSearchService.getDataSearchResult(RECENT_CONTENT_DOMAIN_TYPE, searchRequest);
  }

}
