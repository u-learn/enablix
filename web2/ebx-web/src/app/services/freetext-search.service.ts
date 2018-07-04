import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';

@Injectable()
export class FreetextSearchService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  searchBizContent(text: string, pageNum: number, pageSize: number, searchScope?: any) {
    let apiUrl = this.apiUrlService.postBizContentSearch();
    return this.http.post(apiUrl, {
              text: text,
              page: pageNum,
              size: pageSize,
              contentQIds: searchScope
            });
  }

}
