import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList, AfterViewInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';

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

export class ContentRequestListComponent {
  
  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  constructor(public ccService: ConsolidateContentService,
        public contentWFService: ContentWorkflowService,
        public alert: AlertService, public user: UserService,
        public navService: NavigationService) {

  }

  initComponent() {
    
    this.tableColumns = [
      {
        heading: "Asset",
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
        this.alert.success("Content published successfully.");
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
    this.navService.goToContentRequestDetail(rec.objectRef.identity);
  }

}