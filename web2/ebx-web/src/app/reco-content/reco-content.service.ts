import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';

@Injectable()
export class RecoContentService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  fetchRecoContent(pageSize: number = 5, pageNo: number = 0) : Observable<any> {
    let apiUrl = this.apiUrlService.getRecoContentUrl(pageSize, pageNo);
    return this.http.get(apiUrl);
  }

}
