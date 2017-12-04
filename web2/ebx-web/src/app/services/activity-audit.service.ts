import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { FilterMetadata, DataType, ConditionOperator } from '../core/data-search/filter-metadata.model';
import { Pagination } from '../model/pagination.model';
import { DataPage } from '../model/data-page.model';
import { DataSearchService } from '../core/data-search/data-search.service';
import { DataSearchRequest } from '../core/data-search/data-search-request.model';
import { ContentTemplateService } from '../core/content-template.service';

const ACTIVITY_AUDIT_DOMAIN = "com.enablix.core.domain.activity.ActivityAudit";

@Injectable()
export class ActivityAuditService {

  filterMetadata: { [key: string] : FilterMetadata };

  activityDesc: any = {
    "CONTENT_UPDATE": "updated",
    "CONTENT_ADD": "added",
    "DOC_DOWNLOAD" : "downloaded",
    "CONTENT_SUGGEST_APPROVED": "approved",
    "CONTENT_SUGGEST_REJECT": "rejected" 
  }

  constructor(private dataSearchService: DataSearchService,
              private ctService: ContentTemplateService) { 
    
    this.filterMetadata = {
      activityTypeIn : {
        field: "activity.activityType",
        dataType: DataType.STRING,
        operator: ConditionOperator.IN
      }
    }

  }

  fetchActivities(filters: { [key: string]: any }, pagination: Pagination) : Observable<DataPage> {

    let searchRequest = new DataSearchRequest();
    
    searchRequest.projectedFields = [];
    searchRequest.pagination = pagination;

    searchRequest.filterMetadata = this.filterMetadata;
    searchRequest.filters = filters;

    return this.dataSearchService.getDataSearchResult(ACTIVITY_AUDIT_DOMAIN, searchRequest);
  }

  getTextDescription(act: any) : string {
    return act.actor.name + " " + this.activityDesc[act.activity.activityType] + " " 
          //+ this.ctService.getContainerSingularLabel(act.activity.containerQId) + " - " 
          + act.activity.itemTitle;
  }

}
