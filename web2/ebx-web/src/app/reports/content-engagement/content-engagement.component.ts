import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

import { ActivityAuditService } from '../../core/activity-audit.service';
import { ApiUrlService } from '../../core/api-url.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { DataPage } from '../../model/data-page.model';
import { AlertService } from '../../core/alert/alert.service';
import { TableColumn } from '../../core/model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { DataFilter, DataFiltersOptions, DataFiltersConfig, DataFilterValueTx, IdPropDataFilterValueTx, AtleastOneValueDataFilterValidator } from '../../core/data-filters/data-filters.model';
import { OffsetDaysFilterValueTx } from '../model/report-config.model';
import { Utility } from '../../util/utility';
import { Constants } from '../../util/constants';
import { ContentTemplateService } from '../../core/content-template.service';
import { DataSearchService } from '../../core/data-search/data-search.service';
import { DataSearchRequest } from '../../core/data-search/data-search-request.model';
import { ReportBaseConfig } from '../model/report-config.model';

@Component({
  selector: 'ebx-content-engagement',
  templateUrl: './content-engagement.component.html',
  styleUrls: ['./content-engagement.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentEngagementComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  dataFiltersConfig: DataFiltersConfig;
  lastUsedFilters: any;

  @Input() reportConfig : ReportBaseConfig;

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
    private alert: AlertService, private actvyAuditService: ActivityAuditService,
    private navService: NavigationService, private router: Router,
    private ctService: ContentTemplateService, private dsService: DataSearchService) { }

  ngOnInit() {
    
    let _this = this;

    var metricPeriods = [
        { label: 'Last 30 Day', id: 30 },
        { label: 'Last 90 Days', id: 90 },  
        { label: 'Last 6 Months', id: 182 },
        { label: 'Last 1 Year', id: 365 }
      ];

    let bizContentOptions = [];
    this.ctService.templateCache.bizContentContainers.forEach((cntnr) => {
      bizContentOptions.push({
        id: cntnr.qualifiedId,
        label: cntnr.label
      });
    });
    Utility.sortArrayByLabel(bizContentOptions);

    let filters: DataFilter[] = [
      {
        id: "contentQIdIn",
        type: "multi-select",
        options: {},
        name: "Content Type",
        masterList: function() {
          return Observable.of(bizContentOptions);
        },
        defaultValue: function() { return null; },
        valueTx: IdPropDataFilterValueTx.theInstance
      },
      {
        id: "timePeriod",
        type: "multi-select",
        options: {
          singleSelect: true
        },
        name: "Time Period",
        masterList: function() {
          return Observable.of(metricPeriods);
        },
        defaultValue: function() { return [metricPeriods[0]]; },
        validator: new AtleastOneValueDataFilterValidator("Please select Time Period"),
        valueTx: OffsetDaysFilterValueTx.theInstance
      }
    ];

    var prefKey = "report.content-engagement.defaultFilterValues";
    this.dataFiltersConfig = {
      filters: filters,
      options: {
        heading: "Filter",
        resetLabel: "Reset",
        searchLabel: "Go",
        prefValuesKey: prefKey,
        searchOnLoad: true
      }
    }

    this.tableColumns = [
      {
        heading: "Title",
        key: "contentTitle"
      },
      {
        heading: "Type",
        key: "contentType"
      },
      {
        heading: "Access Count",
        key: "accessCount",
        sortProp: "accessCount",
        dataType: DataType.NUMBER,
      },
      {
        heading: "Download Count",
        key: "downloadCount",
        sortProp: "downloadCount",
        dataType: DataType.NUMBER
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.DESC;
    this.pagination.sort.field = 'accessCount';
  }

  fetchFilteredData(filters: any) {
    this.pagination.pageNum = 0;
    this.fetchData(filters);
  }

  getContentTypeName(contentQId: string) {
    var container = this.ctService.getContainerByQId(contentQId);
    return container ? container.label : '';
  }

  fetchData(filters: any) {
    
    this.lastUsedFilters = filters;
    Utility.removeNullProperties(filters.dataFilters);
    console.log(filters.dataFilters);


    let request = {
      contentQIdIn: filters.dataFilters['contentQIdIn'],
      timePeriod: filters.dataFilters['timePeriod'],
      pagination: this.pagination
    }

    let apiUrl = this.apiUrlService.postFetchContentEngagementReportData();
    
    this.http.post<DataPage>(apiUrl, request).subscribe((data) => {
      this.dataPage = data;
    }, error => {
      this.alert.error("Error fetching content engagement data.", error.statusCode);
    });;
  }

}
