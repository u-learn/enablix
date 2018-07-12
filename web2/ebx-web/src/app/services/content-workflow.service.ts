import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ApiUrlService } from '../core/api-url.service';
import { UserService } from '../core/auth/user.service';
import { Permissions } from '../model/permissions.model';
import { ContentRequestDetail, SimpleActionInput } from '../model/content-workflow.model';
import { FilterMetadata, DataType, ConditionOperator } from '../core/data-search/filter-metadata.model';

@Injectable()
export class ContentWorkflowService {

  static contentRequestDomain = "com.enablix.content.approval.model.ContentApproval";
  static filterMetadata: { [key: string] : FilterMetadata} = {
     "contentIdentity" : {
       "field" : "objectRef.data.identity",
       "operator" : ConditionOperator.EQ,
       "dataType" : DataType.STRING
     },
     "contentQId" : {
       "field" : "objectRef.contentQId",
       "operator" : ConditionOperator.EQ,
       "dataType" : DataType.STRING
     },
     "requestState" : {
       "field" : "currentState.stateName",
       "operator" : ConditionOperator.EQ,
       "dataType" : DataType.STRING
     },
     "requestStateNot" : {
       "field" : "currentState.stateName",
       "operator" : ConditionOperator.NOT_EQ,
       "dataType" : DataType.STRING
     },
     "requestStateNotIn" : {
       "field" : "currentState.stateName",
       "operator" : ConditionOperator.NOT_IN,
       "dataType" : DataType.STRING
     },
     "refObjectNE" : {
       "field" : "objectRef.identity",
       "operator" : ConditionOperator.NOT_EQ,
       "dataType" : DataType.STRING
     },
     "createdBy" : {
       "field" : "createdBy",
       "operator" : ConditionOperator.EQ,
       "dataType" : DataType.STRING
     }
  };

  static ACTION_SAVE_DRAFT = "SAVE_DRAFT";
  static ACTION_REJECT = "REJECT";
  static ACTION_APPROVE = "APPROVE";
  static ACTION_EDIT = "EDIT";
  static ACTION_VIEW_DETAILS = "VIEW_DETAILS";
  static ACTION_WITHDRAW = "WITHDRAW";
  static ACTION_PUBLISH = "PUBLISH";
  static ACTION_DISCARD = "DISCARD";
  static ACTION_SUBMIT = "SUBMIT";
   
  static STATE_DRAFT = "DRAFT";
  static STATE_PUBLISHED = "PUBLISHED";
  static STATE_PENDING_APPROVAL = "PENDING_APPROVAL";
  static STATE_WITHDRAWN = "WITHDRAWN";
  static STATE_APPROVED = "APPROVED";
  static STATE_REJECTED = "REJECTED";

  stateDisplayText = {};
  actionDisplayText = {};
  stateActionMap: any;

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
        private user: UserService) { 

    this.stateDisplayText[ContentWorkflowService.STATE_DRAFT] = "Draft";
    this.stateDisplayText[ContentWorkflowService.STATE_PUBLISHED] = "Published";
    this.stateDisplayText[ContentWorkflowService.STATE_PENDING_APPROVAL] = "Pending Approval";
    this.stateDisplayText[ContentWorkflowService.STATE_WITHDRAWN] = "Withdrawn";
    this.stateDisplayText[ContentWorkflowService.STATE_APPROVED] = "Approved";
    this.stateDisplayText[ContentWorkflowService.STATE_REJECTED] = "Rejected";

    this.actionDisplayText[ContentWorkflowService.ACTION_SUBMIT] = "Approval Requested";
    this.actionDisplayText[ContentWorkflowService.ACTION_APPROVE] = "Approved";
    this.actionDisplayText[ContentWorkflowService.ACTION_REJECT] = "Rejected";
    this.actionDisplayText[ContentWorkflowService.ACTION_WITHDRAW] = "Withdrawn";
  }

  init() : Observable<any> {

    if (!this.stateActionMap) {
      
      let apiUrl = this.apiUrlService.getContentWFStateActionMapUrl();
      
      return this.http.get<any>(apiUrl).map(res => {
        this.stateActionMap = res;
        return res;
      });  
    }

    return Observable.of(this.stateActionMap);
    
  }

  submitContent(contentQId: string, rec: any, saveAsDraft: boolean, notes: any) {
    
    let uri = saveAsDraft ? this.apiUrlService.postSaveContentDraft() : 
                this.apiUrlService.postSubmitContentRequestUrl();
                
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
    
    if (rec.__decoration) {
      delete rec['__decoration'];
    }

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
    
    requestList.forEach((item) => {
      if (item.data.__decoration) {
        delete item.data['__decoration'];
      }
    })

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

    if (rec.__decoration) {
      delete rec['__decoration'];
    }
    
    let apiUrl = this.apiUrlService.postEditContentRequestUrl();
    return this.http.post(apiUrl, contentDetail); 
  }

  isApprovalWFRequired() : boolean {
    return !this.user.userHasPermission(Permissions.PUBLISH_CONTENT);
  }

  isActionAllowed(actionName: string, crRecord: any) : boolean {
         
    if (crRecord) {
      
      if ((actionName == ContentWorkflowService.ACTION_WITHDRAW 
             || actionName == ContentWorkflowService.ACTION_DISCARD)
          && this.user.getUserIdentity() != crRecord.createdBy) {
        return false;
      }

      if ((actionName == ContentWorkflowService.ACTION_PUBLISH)
            && crRecord.currentState.stateName == ContentWorkflowService.STATE_DRAFT
            && crRecord.createdBy == this.user.getUserIdentity()) {
        return true;
      }
    
      let nextActions = this.stateActionMap[crRecord.currentState.stateName];
       
      if (nextActions) {
        
        for (let i = 0; i < nextActions.length; i++) {
        
          let nextAction =  nextActions[i];
          if (nextAction.actionName == actionName) {
            
            if (actionName == ContentWorkflowService.ACTION_EDIT 
                && this.user.getUserIdentity() == crRecord.createdBy) {
              return true;
            }
            
            if (!this.user.userHasAllPermissions(nextAction.requiredPermissions)) {
              return false;
            }
              
            return true;
          }
        }
      }
    }
     
    return false;
  }

  isActionAllowedForAllRecords(actionName: string, crRecords: any[]) : boolean {

    for (let i = 0; i < crRecords.length; i++) {
      if (!this.isActionAllowed(actionName, crRecords[i])) {
        return false;
      }
    }

    return true;
  }

}
