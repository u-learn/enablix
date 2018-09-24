import { Injectable } from '@angular/core';

import { DataSearchService } from '../core/data-search/data-search.service';
import { DataSearchRequest } from '../core/data-search/data-search-request.model';
import { FilterMetadata, DataType, ConditionOperator } from '../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../core/model/pagination.model';
import { ContentTemplateService } from '../core/content-template.service';
import { ContentService } from '../core/content/content.service';
import { ContentWorkflowService } from '../services/content-workflow.service';

@Injectable()
export class ConsolidateContentService {

  constructor(private dsService: DataSearchService,
      private ctService: ContentTemplateService,
      private contentService: ContentService,
      private cwService: ContentWorkflowService) { 
  }

  getContentRequests(filters: {[key: string] : any}, pagination: Pagination) {

    let searchRequest = new DataSearchRequest();

    searchRequest.filterMetadata = ContentWorkflowService.filterMetadata;
    searchRequest.filters = filters;


    searchRequest.pagination = pagination;

    searchRequest.projectedFields = [];

    return this.dsService.getDataSearchResult(ContentWorkflowService.contentRequestDomain, searchRequest);
  }

  getStateDisplayName(state: string) {
    let name = this.cwService.stateDisplayText[state];
    return name ? name : state;
  }

  getRequestTypeDisplayName(requestType: string) {
    let name = this.cwService.requestTypeDisplayText[requestType];
    return name ? name : requestType;
  }

  getRecordContainer(rec: any) {
    return this.ctService.getConcreteContainerByQId(rec.objectRef.contentQId);
  }

  getDecoratedContentRecord(rec: any) {
    let contentrec = rec.objectRef.data;
    if (contentrec && !contentrec.__decoration) {
      this.contentService.decorateRecord(this.getRecordContainer(rec), contentrec);
    }
    return contentrec;
  }

}
