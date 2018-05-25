import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from './api-url.service';
import { AlertService } from './alert/alert.service';


@Injectable()
export class ContentConnService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
              private alert: AlertService) { }

  getFirstContentConnVOByContentQId(contentQId: string) : Observable<any> {
    let apiUrl = this.apiUrlService.getFirstContentConnByContentQId(contentQId);
    return this.http.get(apiUrl);     
  }

}
