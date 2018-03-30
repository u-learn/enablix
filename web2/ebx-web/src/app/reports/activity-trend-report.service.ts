import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { DatePipe } from '@angular/common'
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/map';

import { ApiUrlService } from '../core/api-url.service';
import { ReportConfig, OffsetDaysFilterValueTx, ReportDataProvider } from './model/report-config.model';
import { IdPropDataFilterValueTx, AtleastOneValueDataFilterValidator } from '../core/data-filters/data-filters.model';
import { FilterMetadata } from '../core/data-search/filter-metadata.model';
import { AlertService } from '../core/alert/alert.service';


@Injectable()
export class ActivityTrendReportService implements ReportDataProvider {

  reportConfig: ReportConfig;
  metricIdLabelMap: any = {};

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
              private alert: AlertService, private datePipe: DatePipe) { }

  getReportConfig() : ReportConfig {

    if (this.reportConfig) {
      return this.reportConfig;
    }

    let _this = this;
    let config : ReportConfig = new ReportConfig();
    
    config.id = "activity-trend-calculator";
    config.name = "Activity Trend";
    config.heading = "Activity Trend";
    config.type = "MULTILINE";
    config.init = function() {};
    config.options = {
      columndetails :{        
        'Number of Logins': {column: 'ACTMETRIC2'},
        'Number of Distinct Logins': {column: 'ACTMETRIC3'},
        'Content Add': {column: 'ACTMETRIC4'},
        'Content Updates': {column: 'ACTMETRIC5'},
        'Content Access': {column: 'ACTMETRIC6'},
        'Content Preview': {column: 'ACTMETRIC7'},
        'Content Download': {column: 'ACTMETRIC8'},
        'Searches': {column: 'ACTMETRIC9'}
      }
    };

    config.filterMetadata = {};

    var trendTypes = [
        { label: 'Daily', id: 'daily' },
        { label: 'Weekly', id: 'weekly' },  
        { label: 'Monthly', id: 'monthly' }
      ];

    var trendPeriods = [
        { label: 'Last 7 Days', id: 6 },  
        { label: 'Last 30 Days', id: 29 },
        { label: 'Last 90 Days', id: 89 }
      ];

    config.filters = [
        {
          id: "activityMetricTrend",
          type: "multi-select",
          options: {
            singleSelect: true
          },
          name: "Trend",
          masterList: function() {
            return Observable.of(trendTypes);
          },
          defaultValue: function() {
            return [trendTypes[0]];
          },
          validator: new AtleastOneValueDataFilterValidator("Please select one or more Trend values"),
          valueTx: IdPropDataFilterValueTx.theInstance
        },
        {
          id: "activityMetricTime",
          type: "multi-select",
          options: {
            singleSelect: true
          },
          name: "Time",
          masterList: function() {
            return Observable.of(trendPeriods);
          },
          defaultValue: function() {
            return [trendPeriods[0]];
          },
          validator: new AtleastOneValueDataFilterValidator("Please select Time value"),
          valueTx: OffsetDaysFilterValueTx.theInstance
        },
        {
          id: "activityMetric",
          type: "multi-select",
          options: { },
          name: "Activity Metric",
          masterList: _this.getActivityMetrics.bind(_this),
          defaultValue: function() {
            return [{label:'Content Access', id:'ACTMETRIC6'}];
          },
          validator: new AtleastOneValueDataFilterValidator("Please select one or more Activity Metric values"),
          valueTx: IdPropDataFilterValueTx.theInstance
        }
      ];

    config.dataProvider = _this;

    config.chartOptions = {
      chart: {
        type: 'lineChart',
        height: 500,
        margin : {
            top: 20,
            right: 20,
            bottom: 80,
            left: 55
        },
        x: function(d){ return d.x; },
        y: function(d){ return d.y; },
        useInteractiveGuideline: true,
        dispatch: {
            stateChange: function(e) { console.log("stateChange"); },
            changeState: function(e) { console.log("changeState"); },
            tooltipShow: function(e) { console.log("tooltipShow"); },
            tooltipHide: function(e) { console.log("tooltipHide"); }
        },
        xScale: d3.time.scale.utc(),
        xAxis: {
            axisLabel: 'Time (ms)',
            tickFormat: function(d) {
              var monthlyTrend = false; // TODO:
              var dateStr = monthlyTrend ? _this.datePipe.transform(d, 'MMM-yyyy') : _this.datePipe.transform(d, 'MM/dd/yyyy');
              return dateStr;
            },
            rotateLabels: -45
        },
        yAxis: {
            axisLabel: 'Activity Count',
            tickFormat: function(d) {
                return d;
            },
            axisLabelDistance: -10
        },
        callback: function(chart){
            console.log("!!! lineChart callback !!!");
        }
      }
    }

    this.reportConfig = config;
    return config;
  }

  getActivityMetrics() : Observable<any> {
    
    let apiUrl = this.apiUrlService.getActivityMetricTypesUrl();
    
    return this.http.get(apiUrl).map((data: any) => {
      
      let metricTypes = [];
      this.metricIdLabelMap = {};
      
      if (data) {
        data.forEach((metric) => {
          if (metric.metricCode !== "ACTMETRIC7") {
            metricTypes.push({
              id: metric.metricCode,
              label: metric.metricName
            });
            this.metricIdLabelMap[metric.metricCode] = metric.metricName;
          }
        });
      }

      metricTypes.sort((a, b) => {
          return a.label === b.label ? 0 : (a.label < b.label ? -1 : 1);
        });

      return metricTypes;
    });
  }

  fetchData(filters: { [key: string]: any }) : Observable<any> {
    let searchFilters = filters || {};
    let apiUrl = this.apiUrlService.getActivityTrendDataUrl();
    return this.http.get(apiUrl, {params: searchFilters}).map((data: any) => {
      return data.trendData;
    });
  }

  prepareChartData(trendData: any, filters: any) {

    var newChartData: any = {};
              
    trendData.forEach((dataItem) => {
    
      var timeVal = new Date(dataItem.startDate);
           
      for (var key in dataItem) {
        
        if (key !== "Time" && key != "startDate"
            && key != "ACTMETRIC7") {
        
          let value = dataItem[key];
          var dataPoints = newChartData[key];
          
          if (!dataPoints) {
            dataPoints = [];
            newChartData[key] = dataPoints;
          }
          
          dataPoints.push({y: value, x: timeVal});
        }
        
      };
      
    });
    
    var nvd3ChartData = [];
    for(var key in newChartData) {
      nvd3ChartData.push({
        values: newChartData[key],
        key: this.metricIdLabelMap[key]
      });
    }
     
    var xAxisLabel = "";
    
    switch(filters.dataFilters.activityMetricTrend[0]) {
      case "daily" : xAxisLabel = "Date"; break;
      case "weekly" : xAxisLabel = "Week Start Date"; break;
      case "monthly" : xAxisLabel = "Month"; break;
    }
    
    this.reportConfig.chartOptions.chart.xAxis.axisLabel = xAxisLabel;

    return nvd3ChartData;
  }

}
