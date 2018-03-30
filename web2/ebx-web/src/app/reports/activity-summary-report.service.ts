import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/map';

import { ApiUrlService } from '../core/api-url.service';
import { ReportConfig, OffsetDaysFilterValueTx, ReportDataProvider } from './model/report-config.model';
import { IdPropDataFilterValueTx, AtleastOneValueDataFilterValidator } from '../core/data-filters/data-filters.model';
import { FilterMetadata, ConditionOperator, DataType } from '../core/data-search/filter-metadata.model';
import { AlertService } from '../core/alert/alert.service';

@Injectable()
export class ActivitySummaryReportService implements ReportDataProvider {

  reportConfig: ReportConfig;

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
              private alert: AlertService) { }

  getReportConfig() : ReportConfig {
    
    if (this.reportConfig) {
      return this.reportConfig
    }

    let _this = this;
    let config : ReportConfig = new ReportConfig();
    
    config.id = "activity-metric-calculator";
    config.name = "Activity Summary";
    config.heading = "Activity Summary";
    config.type = "HORIZONTAL_BAR";
    config.init = function() {};
    config.options = {
      columndetails : {        
        'Metric': { column: 'metricName' },
        'Value': { column: 'metricValue' }
      }
    };

    config.filterMetadata = {
      "activityMetricTime": {
          "field": "asOfDate",
          "operator": ConditionOperator.GTE,
          "dataType": DataType.DATE
      }
    };

    var trendTypes = [
        { label: 'Daily', id: 'daily' },
        { label: 'Weekly', id: 'weekly' },  
        { label: 'Monthly', id: 'monthly' }
      ];

    var metricPeriods = [
        { label: 'Last Day', id: 1 },
        { label: 'Last 7 Days', id: 6 },  
        { label: 'Last 30 Days', id: 29 },
        { label: 'Last 90 Days', id: 89 }
      ];

    config.filters = [
        {
          id: "activityMetricTime",
          type: "multi-select",
          options: {
            singleSelect: true
          },
          name: "Time",
          masterList: function() {
            return Observable.of(metricPeriods);
          },
          defaultValue: function() {
            return [metricPeriods[0]];
          },
          validator: new AtleastOneValueDataFilterValidator("Please select Time value"),
          valueTx: OffsetDaysFilterValueTx.theInstance
        }
      ];

    config.chartOptions = {
      chart: {
        type: 'multiBarHorizontalChart',
        height: 500,
        margin : {
            top: 20,
            right: 20,
            bottom: 80,
            left: 150
        },
        x: function(d) { return d.metricName; },
        y: function(d) { return d.metricValue; },
        showControls: false,
        showValues: true,
        showLegend: false,
        duration: 500,
        valueFormat: d3.format(".0f"),
        xAxis: {
          showMinMax: true
        },
        yAxis: {
          axisLabel: 'Values',
          tickFormat: function(d) {
            return d3.format(".0f")(d);
          }
        }
      }
    }

    config.dataProvider = _this;

    this.reportConfig = config;
    return config;
  }

  fetchData(filters: { [key: string]: any }) : Observable<any> {
    let searchFilters = filters || {};
    let apiUrl = this.apiUrlService.getActivityMetricSummaryUrl();
    return this.http.get(apiUrl, {params: searchFilters}).map((data: any) => {
      return data.metricData;
    });
  }

  prepareChartData(metricData: any, filters: any) {
    let previewActvyIndx = -1;
    for (var i = 0; i < metricData.length; i++) {
      if (metricData[i].metricCode === "ACTMETRIC7") {
        previewActvyIndx = i;
        break;
      }
    }

    if (previewActvyIndx >= 0) {
      metricData.splice(previewActvyIndx, 1);
    }

    return [{
      key: "Activity Count",
      color: "#1F8AEF",
      values: metricData
    }];
  }

}
