import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

import { ApiUrlService } from '../../core/api-url.service';
import { DataPage } from '../../model/data-page.model';
import { AlertService } from '../../core/alert/alert.service';
import { TableColumn } from '../../core/model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { ReportBaseConfig } from '../model/report-config.model';
import { EbxDateTimezonePipe } from '../../core/pipes/ebx-date-timezone.pipe';

@Component({
  selector: 'ebx-content-type-coverage',
  templateUrl: './content-type-coverage.component.html',
  styleUrls: ['./content-type-coverage.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentTypeCoverageComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  pagination: Pagination;

  @Input() reportConfig : ReportBaseConfig;

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
    private alert: AlertService, private dateTimezonePipe: EbxDateTimezonePipe) { }

  ngOnInit() {
    
    let _this = this;


    this.tableColumns = [
      {
        heading: "Content Type",
        key: "containerLabel",
        sortProp: "containerLabel"
      },
      {
        heading: "Number of Assets",
        key: "count",
        sortProp: "count",
        dataType: DataType.NUMBER
      },
      {
        heading: "Last Added",
        key: "lastCreateDt",
        sortProp: "lastCreateDt"
      },
      {
        heading: "Last Updated",
        key: "lastUpdateDt",
        sortProp: "lastUpdateDt"
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.DESC;
    this.pagination.sort.field = 'count';

    this.fetchData();
  }

  fetchFilteredData() {
    this.pagination.pageNum = 0;
    this.fetchData();
  }

  fetchData() {
    
    let apiUrl = this.apiUrlService.postFetchContentTypeCoverageReportData();
    this.http.post<DataPage>(apiUrl, this.pagination).subscribe((data) => {
      
      this.dataPage = data;
      
      if (data.content && data.content.length > 0) {
        var asOfDate = data.content[0].asOfDate;
        this.reportConfig.subheading = "As of " 
          + this.dateTimezonePipe.transform(asOfDate);
      }

    }, error => {
      this.alert.error("Error fetching content coverage data.", error.statusCode);
    });;
  }

}
