import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../environments/environment';

import { Constants } from '../util/constants';
import { DataSearchService } from '../core/data-search/data-search.service';
import { DataSearchRequest } from '../core/data-search/data-search-request.model';
import { Pagination, Direction, SortCriteria } from '../core/model/pagination.model';
import { DataPage } from '../model/data-page.model';
import { DataType, ConditionOperator, ValueType } from '../core/data-search/filter-metadata.model';
import { ContentTemplateService } from '../core/content-template.service';

const RECENT_CONTENT_DOMAIN_TYPE = "com.enablix.core.domain.recent.RecentData";

@Injectable()
export class RecentContentService {

  constructor(private dataSearchService: DataSearchService, 
              private ctService: ContentTemplateService) { }

  fetchRecentContent(bizContentOnly: boolean = true, pageSize: number = 5, 
      pageNum: number = 0, filters?: any, pagination?: Pagination) : Observable<DataPage> {

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
      },
      lastXDays: {
        field: "createdAt",
        operator: ConditionOperator.GTE,
        dataType: DataType.DATE,
        dateFilter: {
          valueType: ValueType.LAST_X_DAYS
        }
      }
    };

    searchRequest.filters = { obsolete: false };

    if (bizContentOnly) {
      let bizContentQIds = this.ctService.templateCache.bizContentContainers.map(cont => cont.qualifiedId);
      searchRequest.filters.containerQIdIn = bizContentQIds
    }

    if (filters) {
      for(let fltr in filters) {
        searchRequest.filters[fltr] = filters[fltr];
      }
    }

    if (pagination) {
      searchRequest.pagination = pagination;
    } else {
      searchRequest.pagination = new Pagination();
      searchRequest.pagination.pageNum = pageNum;
      searchRequest.pagination.pageSize = pageSize;
      
      searchRequest.pagination.sort = new SortCriteria();
      searchRequest.pagination.sort.field = Constants.FLD_CREATED_AT;
      searchRequest.pagination.sort.direction = Direction.DESC;
    }

    return this.dataSearchService.getDataSearchResult(RECENT_CONTENT_DOMAIN_TYPE, searchRequest);
  }

  getIconUrl(act: any) : string {
    return environment.baseAPIUrl + "/doc/icon/r/" + act.data.containerQId + "/" + act.data.recordIdentity + "/";
  }

  getContentColor(act: any) : string {
    let color = null;
    let container = this.ctService.getContainerByQId(act.data.containerQId);
    if (container && this.ctService.isBusinessDimension(container)) {
      color = container.color;
    }
    return color;
  }

}
