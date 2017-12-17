import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../core/api-url.service';
import { UserService } from '../core/auth/user.service';
import { Permissions } from '../model/permissions.model';
import { ContentRequestDetail, SimpleActionInput } from '../model/content-workflow.model';

@Injectable()
export class ContentWorkflowService {

  static VIEW_STUDIO

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
        private user: UserService) { }

  submitContent(contentQId: string, rec: any, saveAsDraft: boolean, notes: any) {
    
    let uri = this.apiUrlService.postSaveContentDraft();
    let addRequest = !rec.identity;

    if (rec.__decoration) {
      delete rec['__decoration'];
    }

    let contentDetail: any = {
      "contentQId": contentQId,
      "notes": notes,
      "data": rec,
      "addRequest": addRequest
    }

    return this.http.post(uri, contentDetail);
  }

  getContentRequest(refObjIdentity: string) {
    let apiUrl = this.apiUrlService.getContentRequestUrl(refObjIdentity);
    return this.http.get(apiUrl);
  }

  deleteContentRequest(refObjIdentity: string) {
    let apiUrl = this.apiUrlService.postDiscardContentRequestUrl();
    return this.http.post(apiUrl, { identity: refObjIdentity });  
  }

  deleteContentRequestList(actionRequestList: SimpleActionInput[]) {
    let apiUrl = this.apiUrlService.postDiscardContentRequestListUrl();
    return this.http.post(apiUrl, actionRequestList);  
  }

  publishContentRequest(refObjIdentity: string, contentQId: string, rec: any, notes: string) {
    var contentDetail = {
      "identity": refObjIdentity,
      "contentQId": contentQId,
      "notes": notes,
      "data": rec
    };

    let apiUrl = this.apiUrlService.postPublishContentRequestUrl();
    return this.http.post(apiUrl, contentDetail);
  }

  publishContentRequestList(requestList: ContentRequestDetail[]) {
    let apiUrl = this.apiUrlService.postPublishContentRequestListUrl();
    return this.http.post(apiUrl, requestList);
  }

  approveContentRequest(refObjIdentity: string, notes: string) {
         
    var actionInput = {
        "identity": refObjIdentity,
        "notes": notes
    };
    
    let apiUrl = this.apiUrlService.postApproveContentRequestUrl();
    return this.http.post(apiUrl, actionInput); 
  }

  approveContentRequestList(actionRequestList: SimpleActionInput[]) {
    let apiUrl = this.apiUrlService.postApproveContentRequestListUrl();
    return this.http.post(apiUrl, actionRequestList); 
  }

  rejectContentRequest(refObjIdentity: string, notes: string) {
         
    var actionInput = {
        "identity": refObjIdentity,
        "notes": notes
    };
    
    let apiUrl = this.apiUrlService.postRejectContentRequestUrl();
    return this.http.post(apiUrl, actionInput); 
  }

  rejectContentRequestList(actionRequestList: SimpleActionInput[]) {
    let apiUrl = this.apiUrlService.postRejectContentRequestListUrl();
    return this.http.post(apiUrl, actionRequestList); 
  }

  withdrawContentRequest(refObjIdentity: string, notes: string) {
         
    var actionInput = {
        "identity": refObjIdentity,
        "notes": notes
    };
    
    let apiUrl = this.apiUrlService.postWithdrawContentRequestUrl();
    return this.http.post(apiUrl, actionInput); 
  }

  withdrawContentRequestList(actionRequestList: SimpleActionInput[]) {
    let apiUrl = this.apiUrlService.postWithdrawContentRequestListUrl();
    return this.http.post(apiUrl, actionRequestList); 
  }

  editContentRequest(refObjIdentity: string, contentQId: string, rec: any, notes: string) {
    
    var contentDetail = {
      "identity": refObjIdentity,
      "contentQId": contentQId,
      "notes": notes,
      "data": rec
    };
    
    let apiUrl = this.apiUrlService.postEditContentRequestUrl();
    return this.http.post(apiUrl, contentDetail); 
  }

  isApprovalWFRequired() : boolean {
    return !this.user.userHasPermission(Permissions.PUBLISH_CONTENT);
  }

}
