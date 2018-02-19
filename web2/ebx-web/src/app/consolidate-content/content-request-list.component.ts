import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList, AfterViewInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router, ActivatedRoute } from '@angular/router';

import { ConsolidateContentService } from './consolidate-content.service';
import { DataPage } from '../model/data-page.model';
import { AlertService } from '../core/alert/alert.service';
import { TableColumn, TableActionConfig, TableAction } from '../model/table.model';
import { DataType } from '../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../model/pagination.model';
import { ContentRequestDetail, SimpleActionInput } from '../model/content-workflow.model';
import { Constants } from '../util/constants';
import { TableComponent } from '../table/table.component';
import { UserService } from '../core/auth/user.service';
import { NavigationService } from '../app-routing/navigation.service';
import { ContentWorkflowService } from '../services/content-workflow.service';
import { ContentTemplateService } from '../../app/core/content-template.service';

export class ContentRequestListComponent {
  
  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  constructor(public ccService: ConsolidateContentService,
        public contentWFService: ContentWorkflowService,
        public ctService: ContentTemplateService,
        public alert: AlertService, public user: UserService,
        public navService: NavigationService, public router: Router) {

  }

  initComponent() {
    
    this.tableColumns = [
      {
        heading: "Content Asset",
        key: "asset",
        sortProp: "objectRef.contentTitle"
      },
      {
        heading: "Content Type",
        key: "contentType",
        sortProp: "objectRef.contentQId"
      },
      {
        heading: "Status",
        key: "status",
        sortProp: "currentState.stateName"
      },
      {
        heading: "Created On",
        key: "createdOn",
        sortProp: "createdAt"
      },
      {
        heading: "Creator",
        key: "createdBy",
        sortProp: "createdByName"
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.ASC;
    this.pagination.sort.field = Constants.FLD_CREATED_AT;

  }

  fetchData() {    
    this.ccService.getContentRequests(this.filters, this.pagination).subscribe(res => {
      this.dataPage = res;
    }, error => {
      this.alert.error("Error fetching draft content", error.status);
    });
  }

  getStateDisplayName(state: string) {
    return this.ccService.getStateDisplayName(state);
  }

  getRecordContainer(rec: any) {
    return this.ccService.getRecordContainer(rec);
  }

  getDecoratedContentRecord(ccRec: any) {
    return this.ccService.getDecoratedContentRecord(ccRec);
  }

  publishRecords(selectedRecords: any[]) : Observable<any> {

    let requestList: ContentRequestDetail[] = this.txToContentRequestDetail(selectedRecords);

    let response = this.contentWFService.publishContentRequestList(requestList).share();
    response.subscribe(res => {
        this.alert.success("Content Asset published successfully.");
        this.fetchData();
      }, err => {
        this.alert.error("Unable to publish. Please try later.", err.status);
      });

    return response;
  }

  deleteRecords(selectedRecords: any[]) : Observable<any> {

    let requestList: SimpleActionInput[] = this.txToSimpleActionInput(selectedRecords);

    let response = this.contentWFService.deleteContentRequestList(requestList).share();
    response.subscribe(res => {
        this.alert.success("Content deleted.");
        this.fetchData();
      }, err => {
        this.alert.error("Unable to delete. Please try later.", err.status);
      });

    return response;
  }

  approveRecords(selectedRecords: any[]) : Observable<any> {

    let requestList: SimpleActionInput[] = this.txToSimpleActionInput(selectedRecords);

    let response = this.contentWFService.approveContentRequestList(requestList).share();
    response.subscribe(res => {
        this.alert.success("Content approved.");
        this.fetchData();
      }, err => {
        this.alert.error("Unable to approve. Please try later.", err.status);
      })

    return response;
  }

  rejectRecords(selectedRecords: any[]) : Observable<any> {

    let requestList: SimpleActionInput[] = this.txToSimpleActionInput(selectedRecords);

    let response = this.contentWFService.rejectContentRequestList(requestList).share();
    response.subscribe(res => {
        this.alert.success("Content rejected.");
        this.fetchData();
        return res;
      }, err => {
        this.alert.error("Unable to reject. Please try later.", err.status);
      });

    return response;
  }

  withdrawRecords(selectedRecords: any[]) : Observable<any> {
    
    let requestList: SimpleActionInput[] = this.txToSimpleActionInput(selectedRecords);

    let response = this.contentWFService.withdrawContentRequestList(requestList).share();
    response.subscribe(res => {
        this.alert.success("Content request withdrawn.");
        this.fetchData();
        return res;
      }, err => {
        this.alert.error("Unable to withdraw requests. Please try later.", err.status);
      });

    return response;
  }

  txToSimpleActionInput(selectedRecords: any[]) : SimpleActionInput[] {
    return selectedRecords.map((rec: any) => {
          return {
            identity: rec.objectRef.identity,
            notes: null
          }
        });
  }

  txToContentRequestDetail(selectedRecords: any[]) : ContentRequestDetail[] {
    return selectedRecords.map((rec: any, index: number) => {
          return {
            identity: rec.objectRef.identity,
            notes: null,
            data: rec.objectRef.data,
            contentQId: rec.objectRef.contentQId
          }
        });
  }

  navToContentRequest(rec: any) {
    let cntnr = this.ctService.getContainerByQId(rec.objectRef.contentQId);
    if (this.ctService.isBusinessContent(cntnr)) {
      this.navService.goToContentRequestDetail(rec.objectRef.identity, this.router.url);
    }
  }

}

export class ContentRequestActions implements TableActionConfig<any> {

  component: ContentRequestListComponent;
  allActions: { [key: string]: TableAction<any> } = {};

  actionOptions: string[] = [];

  constructor(comp: ContentRequestListComponent, actionOptions: string[]) {
    
    this.component = comp;
    this.actionOptions = actionOptions;

    this.allActions[ContentWorkflowService.ACTION_APPROVE] = {
      label: "Approve",
      iconClass: "approve",
      successMessage: "Approved",
      execute: this.component.approveRecords.bind(this.component)
    }

    this.allActions[ContentWorkflowService.ACTION_REJECT] = {
      label: "Reject",
      iconClass: "reject",
      successMessage: "Rejected",
      execute: this.component.rejectRecords.bind(this.component)
    }

    this.allActions[ContentWorkflowService.ACTION_WITHDRAW] = {
      label: "Withdraw",
      iconClass: "withdraw",
      successMessage: "Withdrawn",
      execute: this.component.withdrawRecords.bind(this.component)
    }

    this.allActions[ContentWorkflowService.ACTION_DISCARD] = {
      label: "Delete",
      iconClass: "trash",
      successMessage: "Deleted",
      execute: this.component.deleteRecords.bind(this.component),
      confirmConfig: {
        title: "Delete Asset",
        confirmMsg: this.getDeleteConfirmText,
        okLabel: "Delete Asset",
        cancelLabel: "No, keep it."
      }
    }

    this.allActions[ContentWorkflowService.ACTION_PUBLISH] = {
      label: "Publish",
      iconClass: "send-blue",
      successMessage: "Published",
      execute: this.component.publishRecords.bind(this.component)
    }
  }

  getDeleteConfirmText(selRecords: any[]) : string {
    if (selRecords.length == 1) {
      return "You are about to delete '" + selRecords[0].objectRef.contentTitle 
        + "' asset.<br>You won't be able to restore it. Would you like to proceed?";
    } else {
      return "You are about to delete multiple records."
        + "<br>You won't be able to restore it. Would you like to proceed?"; 
    }
  }

  getAvailableActions(selectedRecords: any[]) : TableAction<any>[] {
    
    let actions: TableAction<any>[] = [];
    
    this.actionOptions.forEach(actionName => {
      if (this.component.contentWFService.isActionAllowedForAllRecords(
            actionName, selectedRecords)) {
        let action = this.allActions[actionName];
        if (action) {
          actions.push(action);
        }
      }  
    });
    
    return selectedRecords && selectedRecords.length > 0 ? actions : [];
  }

}