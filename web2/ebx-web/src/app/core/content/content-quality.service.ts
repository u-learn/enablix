import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../api-url.service';
import { DataPage } from '../../model/data-page.model';
import { DataSearchRequest } from '../../core/data-search/data-search-request.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { FilterMetadata, DataType, ConditionOperator } from '../../core/data-search/filter-metadata.model';

@Injectable()
export class ContentQualityService {

  static filterMetadata : { [key: string] : FilterMetadata} = {
    "alertRuleId" : {
      "field" : "alert.ruleId",
      "operator" : ConditionOperator.EQ,
      "dataType" : DataType.STRING
    },
    "authorId" : {
      "field" : "authorUserId",
      "operator" : ConditionOperator.EQ,
      "dataType" : DataType.STRING
    }
  }

  constructor(private http: HttpClient,
              private apiUrlService: ApiUrlService) { }

  getQualityAlerts(filters: {[key: string] : any}, pagination: Pagination) : Observable<DataPage> {
        let searchRequest = new DataSearchRequest();

    searchRequest.filterMetadata = ContentQualityService.filterMetadata;
    searchRequest.filters = filters;
    searchRequest.pagination = pagination;
    searchRequest.projectedFields = [];

    let apiUrl = this.apiUrlService.postFetchQualityAlertsUrl();
    return this.http.post<DataPage>(apiUrl, searchRequest);
  }

}
