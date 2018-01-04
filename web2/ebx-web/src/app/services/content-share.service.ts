import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../core/api-url.service';

@Injectable()
export class ContentShareService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  shareByEmail(contentQId: string, contentIdentity: string, emailIds: string[], message: string) {
    
    let shareData: any = {
      "containerQId" : contentQId, 
      "contentIdentity" : contentIdentity,
      "emailIds" : emailIds,
      "emailCustomContent": message
    };

    let apiUrl = this.apiUrlService.postShareByEmailUrl();
    return this.http.post(apiUrl, shareData);
  }

}
