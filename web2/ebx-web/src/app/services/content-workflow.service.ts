import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';

@Injectable()
export class ContentWorkflowService {

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { }

  submitContent(contentQId: string, rec: any, saveAsDraft: boolean, notes: any) {
    
    let uri = this.apiUrlService.postSaveContentDraft();
    let addRequest = !rec.identity;

    let contentDetail: any = {
      "contentQId": contentQId,
      "notes": notes,
      "data": rec,
      "addRequest": addRequest
    }

    return this.http.post(uri, contentDetail);
  }

}
