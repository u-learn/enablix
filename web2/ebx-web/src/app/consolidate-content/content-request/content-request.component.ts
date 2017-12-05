import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList, AfterViewInit } from '@angular/core';

import { ConsolidateContentService } from '../consolidate-content.service';
import { AlertService } from '../../core/alert/alert.service';
import { DataPage } from '../../model/data-page.model';
import { TableColumn } from '../../model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../model/pagination.model';
import { Constants } from '../../util/constants';
import { TableComponent } from '../../table/table.component';

@Component({
  selector: 'ebx-content-request',
  templateUrl: './content-request.component.html',
  styleUrls: ['./content-request.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentRequestComponent implements OnInit, AfterViewInit {

  @ViewChildren(TableComponent) dataTables: QueryList<TableComponent>;

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  constructor(private ccService: ConsolidateContentService,
    private alert: AlertService) { }

  ngOnInit() {

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
        heading: "Creator",
        key: "createdOn",
        sortProp: "createdAt"
      },
      {
        heading: "Created By",
        key: "createdBy",
        sortProp: "createdByName"
      }
    ];

    this.filters = { requestState: ConsolidateContentService.STATE_PENDING_APPROVAL };
    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.ASC;
    this.pagination.sort.field = Constants.FLD_CREATED_AT;

    this.fetchData();
    
  }

  ngAfterViewInit() {
    this.dataTables.changes.subscribe((tables : QueryList<TableComponent>) => {
      let table = tables.first;
      table.onRefreshData.subscribe(res => {
        this.fetchData();
      });
    })
  }

  fetchData() {    
    this.ccService.getContentRequests(this.filters, this.pagination).subscribe(res => {
      this.dataPage = res;
    }, error => {
      this.alert.error("Error fetching pending requests", error.status);
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

}
