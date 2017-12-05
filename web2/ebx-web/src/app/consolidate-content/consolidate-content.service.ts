import { Injectable } from '@angular/core';

import { DataSearchService } from '../core/data-search/data-search.service';
import { DataSearchRequest } from '../core/data-search/data-search-request.model';
import { FilterMetadata, DataType, ConditionOperator } from '../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../model/pagination.model';
import { ContentTemplateService } from '../core/content-template.service';
import { ContentService } from '../core/content/content.service';

@Injectable()
export class ConsolidateContentService {

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
   
  static STATE_DRAFT = "DRAFT";
  static STATE_PUBLISHED = "PUBLISHED";
  static STATE_PENDING_APPROVAL = "PENDING_APPROVAL";
  static STATE_WITHDRAWN = "WITHDRAWN";
  static STATE_APPROVED = "APPROVED";
  static STATE_REJECTED = "REJECTED";

  stateDisplayText = {};


  constructor(private dsService: DataSearchService,
      private ctService: ContentTemplateService,
      private contentService: ContentService) { 
    this.stateDisplayText[ConsolidateContentService.STATE_DRAFT] = "Draft";
    this.stateDisplayText[ConsolidateContentService.STATE_PUBLISHED] = "Published";
    this.stateDisplayText[ConsolidateContentService.STATE_PENDING_APPROVAL] = "Pending";
    this.stateDisplayText[ConsolidateContentService.STATE_WITHDRAWN] = "Withdrawn";
    this.stateDisplayText[ConsolidateContentService.STATE_APPROVED] = "Approved";
    this.stateDisplayText[ConsolidateContentService.STATE_REJECTED] = "Rejected";
  }

  getContentRequests(filters: {[key: string] : any}, pagination: Pagination) {

    let searchRequest = new DataSearchRequest();

    searchRequest.filterMetadata = ConsolidateContentService.filterMetadata;
    searchRequest.filters = filters;


    searchRequest.pagination = pagination;

    searchRequest.projectedFields = [];

    return this.dsService.getDataSearchResult(ConsolidateContentService.contentRequestDomain, searchRequest);
  }

  getStateDisplayName(state: string) {
    let name = this.stateDisplayText[state];
    return name ? name : state;
  }

  getRecordContainer(rec: any) {
    return this.ctService.getConcreteContainerByQId(rec.objectRef.contentQId);
  }

  getDecoratedContentRecord(rec: any) {
    let contentrec = rec.objectRef.data;
    if (contentrec && !contentrec.__decoration) {
      this.contentService.decorateRecord(this.getRecordContainer(rec), contentrec);
    }
    return contentrec;
  }

}
