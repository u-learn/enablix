import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';
import { UserService } from '../core/auth/user.service';
import { Permissions } from '../model/permissions.model';

@Injectable()
export class ContentWorkflowService {

  static VIEW_STUDIO

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
        private user: UserService) { }

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

  isApprovalWFRequired() : boolean {
    return !this.user.userHasPermission(Permissions.PUBLISH_CONTENT);
  }

}
