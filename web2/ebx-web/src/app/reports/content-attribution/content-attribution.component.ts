
import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material';
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
import { OppAttributionComponent } from './opp-attribution/opp-attribution.component';
import { DataSearchService } from '../../core/data-search/data-search.service';
import { DataSearchRequest } from '../../core/data-search/data-search-request.model';
import { ReportBaseConfig } from '../model/report-config.model';

@Component({
  selector: 'ebx-content-attribution',
  templateUrl: './content-attribution.component.html',
  styleUrls: ['./content-attribution.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentAttributionComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  dataFiltersConfig: DataFiltersConfig;
  lastUsedFilters: any;

  oppStatusList: any = {};

  @Input() reportConfig : ReportBaseConfig;

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
    private alert: AlertService, private actvyAuditService: ActivityAuditService,
    private navService: NavigationService, private router: Router,
    private ctService: ContentTemplateService, private dsService: DataSearchService,
    private dialog: MatDialog) { }

  ngOnInit() {
    
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

    this.oppStatusList = [
        { label: 'Closed Won', id: 'closed-won' },
        { label: 'Open', id: 'open' },  
        { label: 'Closed Lost', id: 'closed-lost' },
        { label: 'All', id: 'all' }
      ];

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
        validator: new AtleastOneValueDataFilterValidator("Please select one or more Content Type"),
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
        defaultValue: function() { return null; },
        validator: new AtleastOneValueDataFilterValidator("Please select Time Period"),
        valueTx: OffsetDaysFilterValueTx.theInstance
      },
      {
        id: "oppStatus",
        type: "multi-select",
        options: {
          singleSelect: true
        },
        name: "Opportunity Status",
        masterList: function() {
          return Observable.of(this.oppStatusList);
        },
        defaultValue: function() { return [{ id: 'closed-won', label: 'Closed Won'}]; },
        validator: new AtleastOneValueDataFilterValidator("Please select Opportunity Status"),
        valueTx: IdPropDataFilterValueTx.theInstance
      }
    ];

    var prefKey = "report.content-attribution-summary.defaultFilterValues";
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
        key: "contentTitle",
        sortProp: "contentTitle",
        headerCssClass: "small-font content-title-col"
      },
      {
        heading: "Type",
        key: "contentType",
        headerCssClass: "small-font content-type-col"
      },
      {
        heading: "Opportunity Count",
        key: "oppCount",
        sortProp: "oppAttribution.count",
        dataType: DataType.NUMBER,
        headerCssClass: "small-font opp-attr-cnt"
      },
      {
        heading: "Attributed Dollars",
        key: "oppDollars",
        sortProp: "oppAttribution.value",
        dataType: DataType.NUMBER,
        headerCssClass: "small-font opp-attr-value"
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.ASC;
    this.pagination.sort.field = 'contentTitle';
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

    let CONTENT_ATTRIBUTION_DOMAIN = "com.enablix.core.domain.report.attribution.ContentAttribution";
    let searchRequest = new DataSearchRequest();

    searchRequest.filterMetadata = {};
    searchRequest.filters = {};
    searchRequest.pagination = this.pagination;

    this.dsService.getDataSearchResult(CONTENT_ATTRIBUTION_DOMAIN, searchRequest).subscribe((data) => {
      this.dataPage = data;
    }, error => {
      this.alert.error("Error fetching content attribution data.", error.statusCode);
    });;
  }

  showAttribution(attribution: any) {

    let dialogRef = this.dialog.open(OppAttributionComponent, {
        width: '800px',
        height: '80vh',
        disableClose: false,
        autoFocus: false,
        data: { 
          metric: attribution,
          statusTx: this.oppStatusList
        }
      });
  }

}
