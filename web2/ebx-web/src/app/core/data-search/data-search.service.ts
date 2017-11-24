import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DataSearchRequest } from './data-search-request.model';
import { DataPage } from '../../model/data-page.model';
import { ApiUrlService } from '../api-url.service';

@Injectable()
export class DataSearchService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }
  
  getDataSearchResult(domainType: string, searchRequest: DataSearchRequest) : Observable<DataPage> {
    const apiUrl = this.apiUrlService.postDataSearchUrl(domainType);
    return this.http.post<DataPage>(apiUrl, searchRequest);
  }


  getContainerDataSearchResult(containerQId: string, searchRequest: DataSearchRequest) : Observable<DataPage> {
    const apiUrl = this.apiUrlService.postContainerDataSearchUrl(containerQId);
    return this.http.post<DataPage>(apiUrl, searchRequest);
  }

}
