import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/map';

import { ApiUrlService } from '../core/api-url.service';
import { ReportConfig, OffsetDaysFilterValueTx, ReportDataProvider } from './model/report-config.model';
import { IdPropDataFilterValueTx, ClientSideOnlyFilterValueTx, AtleastOneValueDataFilterValidator, PassThroughDataFilterValidator } from '../core/data-filters/data-filters.model';
import { FilterMetadata, ConditionOperator, DataType } from '../core/data-search/filter-metadata.model';
import { AlertService } from '../core/alert/alert.service';
import { ContentTemplateService } from '../core/content-template.service';
import { DataSearchService } from '../core/data-search/data-search.service';
import { DataSearchRequest } from '../core/data-search/data-search-request.model';
import { Utility } from '../util/utility';
import { EbxDateTimezonePipe } from '../core/pipes/ebx-date-timezone.pipe';

@Injectable()
export class ContentCoverageReportService implements ReportDataProvider {

  reportConfig: ReportConfig;

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
              private alert: AlertService, private ctService: ContentTemplateService,
              private dsService: DataSearchService, private datePipe: DatePipe,
              private dateTimezonePipe: EbxDateTimezonePipe) { }
  
  getReportConfig() : ReportConfig {
    
    if (this.reportConfig) {
      return this.reportConfig;
    }

    let _this = this;
    let config : ReportConfig = new ReportConfig();
    
    config.id = "content-coverage-report";
    config.category = "coverage";
    config.name = "Dimensions";
    config.heading = "Dimensions Coverage";
    config.type = "HEATMAP";
    config.init = function() {};

    config.options = {
      height: 600,
      margin: { top: 150, right: 0, bottom: 70, left: 170 },
      colors: [/*"#f7fbff", */"#deebf7", "#c6dbef", /*"#9ecae1", */"#6baed6", "#4292c6", /*"#2171b5", "#08519c", "#08306b"*/],
      customColors : { "0": "#f58080"},
      buckets: 4,
      valueText: true,
      categorize: true,
      onClick: (e) => {
        console.log(e);
      }
    };

    config.filterMetadata = {
      "latest" : {
         "field" : "latest",
         "operator" : ConditionOperator.EQ,
         "dataType" : DataType.BOOL
       },
       "contentQIdIn" : {
         "field" : "contentQId",
         "operator" : ConditionOperator.IN,
         "dataType" : DataType.STRING
       }
    };

    let bizDimOptions = [];
    this.ctService.templateCache.bizDimensionContainers.forEach((cntnr) => {
      bizDimOptions.push({
        id: cntnr.qualifiedId,
        label: cntnr.label
      });
    });
    Utility.sortArrayByLabel(bizDimOptions);

    let bizContentOptions = [];
    this.ctService.templateCache.bizContentContainers.forEach((cntnr) => {
      bizContentOptions.push({
        id: cntnr.qualifiedId,
        label: cntnr.label
      });
    });
    Utility.sortArrayByLabel(bizContentOptions);

    config.filters = [
      {
        id: "contentQIdIn",
        type: "multi-select",
        options: {},
        name: "Business Dimensions",
        masterList: function() {
          return Observable.of(bizDimOptions);
        },
        defaultValue: function() {
          return bizDimOptions;
        },
        validator: new AtleastOneValueDataFilterValidator("Please select one or more Business Dimensions"),
        valueTx: IdPropDataFilterValueTx.theInstance
      },
      {
        id: "contentTypes",
        type: "multi-select",
        options: {},
        name: "Content Type",
        masterList: function() {
          return Observable.of(bizContentOptions);
        },
        defaultValue: function() {
          return [];
        },
        validator: PassThroughDataFilterValidator.theInstance,
        valueTx: ClientSideOnlyFilterValueTx.theInstance
      }
    ];

    config.dataProvider = _this;
    this.reportConfig = config;
    return config;
  }

  fetchData(filters: { [key: string]: any }) : Observable<any> {
    
    let CONTENT_COVERAGE_DOMAIN = "com.enablix.core.domain.content.summary.ContentCoverage";
    let searchRequest = new DataSearchRequest();

    searchRequest.filterMetadata = this.reportConfig.filterMetadata;
    searchRequest.filters = filters || {};
    searchRequest.filters.latest = true;

    return this.dsService.getDataSearchResult(CONTENT_COVERAGE_DOMAIN, searchRequest);
  }

  prepareChartData(data: any, filters: any) {
    
    var reportData = [];
    var filterValues = filters.filterSelection;
    
    this.reportConfig.options.restrictedXLabels = [];
    if (filterValues.contentTypes && filterValues.contentTypes.length > 0) {
      var restrictedXLabels = [];
      filterValues.contentTypes.forEach((contType) => {
        restrictedXLabels.push(contType.label);
      });
      this.reportConfig.options.restrictedXLabels = restrictedXLabels;
    }
    
    this.reportConfig.options.yLabelCategories = [];
    if (filterValues.contentQIdIn && filterValues.contentQIdIn.length > 0) {
      var yLabelCategories = [];
      filterValues.contentQIdIn.forEach((contentCat) => {
        yLabelCategories.push(contentCat.label);
      });
      this.reportConfig.options.yLabelCategories = yLabelCategories;
    }
    
    // change heading
    if (data.length > 0) {
      this.reportConfig.subheading = "As of " + this.dateTimezonePipe.transform(data[0].asOfDate);
    }
    
    data.forEach((dataRecord) => {
      
      var recCategory = this.ctService.getContainerLabel(dataRecord.contentQId);
      
      dataRecord.stats.forEach((stat) => {
        
        var contDef = this.ctService.getConcreteContainerByQId(stat.itemId);
        
        if (contDef && // below check is to remove data items which do not belong to selected content type
            ((!this.reportConfig.options.restrictedXLabels || this.reportConfig.options.restrictedXLabels.length == 0
                  || this.reportConfig.options.restrictedXLabels.indexOf(contDef.label) > -1) 
              || this.reportConfig.options.fillEmptyAsZero)) {
          // x, y, value, category are required for the heatmap chart to display
          reportData.push({
            y: dataRecord.recordTitle,
            x: contDef.label,
            value: stat.count,
            category: recCategory,
            data: {
              contentQId: dataRecord.contentQId,
              recordIdentity: dataRecord.recordIdentity,
              subContainerQId: stat.itemId
            }
          });
        }
        
      })
      
    });
    
    return reportData;
  }
}
