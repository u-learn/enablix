import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ReportConfig } from '../model/report-config.model';
import { DataFiltersOptions, DataFiltersConfig } from '../../core/data-filters/data-filters.model';
import { AlertService } from '../../core/alert/alert.service';
import { Utility } from '../../util/utility';

declare let d3: any;

@Component({
  selector: 'ebx-report-detail',
  templateUrl: './report-detail.component.html',
  styleUrls: ['./report-detail.component.scss', '../../../../node_modules/nvd3/build/nv.d3.css'],
  encapsulation: ViewEncapsulation.None
})
export class ReportDetailComponent implements OnInit {

  _reportConfig: ReportConfig;

  dataFiltersConfig: DataFiltersConfig;
  reportData: any;
  chartData: any;

  @Input() 
  set reportConfig(rptCfg: ReportConfig) {
    
    this._reportConfig = rptCfg;
    this.reportData = null;
    this.chartData = null;
    
    if (rptCfg) {

      var prefKey = "report." + rptCfg.id + ".defaultFilterValues";

      this.dataFiltersConfig = {
        filters: rptCfg.filters,
        options: {
          heading: "Filter",
          resetLabel: "Reset",
          searchLabel: "Go",
          prefValuesKey: prefKey,
          searchOnLoad: true
        }
      }
    }
  }

  get reportConfig() {
    return this._reportConfig;
  }

  constructor(private alert: AlertService) { }

  ngOnInit() {
    
  }

  fetchData(filters: any) {
    
    Utility.removeNullProperties(filters.dataFilters);

    this.reportConfig.dataProvider.fetchData(filters.dataFilters).subscribe((data) => {

      this.reportData = data;

      if (data.content) { // check if it is a response with pagination data
        this.reportData = data.content;
      }

      this.chartData = this.reportConfig.dataProvider.prepareChartData(this.reportData, filters);

    }, error => {
      this.alert.error("Error fetching report data.", error.statusCode);
    })
  }

}
