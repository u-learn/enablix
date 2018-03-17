import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';

@Injectable()
export class RecoContentService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  fetchRecoContent() : Observable<any> {
    let apiUrl = this.apiUrlService.getRecoContentUrl();
    return this.http.get(apiUrl);
  }

}
