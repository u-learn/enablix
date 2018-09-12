import { Injectable } from '@angular/core';
import { cloneDeep } from 'lodash';
import { Observable } from 'rxjs/Observable';

import { ReportConfig, ReportBaseConfig } from './model/report-config.model';
import { PassThroughDataFilterValidator, DataFilterValueTx, FixedDataFilterValueProxyTx } from '../core/data-filters/data-filters.model';
import { ActivitySummaryReportService } from './activity-summary-report.service';
import { ActivityTrendReportService } from './activity-trend-report.service';
import { ContentCoverageReportService } from './content-coverage-report.service';
import { UserPreferenceService } from '../core/user-preference.service';

@Injectable()
export class ReportService {

  reports: ReportBaseConfig[] = [];
  initialized: boolean = false;

  passThruValidator = new PassThroughDataFilterValidator();

  constructor(
      private userPrefService: UserPreferenceService,
      private activityTrendRptService: ActivityTrendReportService,
      private activitySummaryRptService: ActivitySummaryReportService,
      private contentCoverageRptService: ContentCoverageReportService) { }

  init() {
    
    if (!this.initialized) {

      this.reports.push({
          id: 'all-activity',
          category: 'activity',
          name : "All Activity",
          heading : "All Activity",
          type : "CUSTOM"
        });

      this.reports.push({
          id: 'content-engagement',
          category: 'engagement',
          name : "Content Engagement",
          heading : "Content Engagement",
          type : "CUSTOM"
        });

      this.reports.push({
          id: 'content-engagement-demo',
          category: 'engagement',
          name : "Content Engagement",
          heading : "Content Engagement",
          type : "CUSTOM"
        });

      this.reports.push({
          id: 'content-attribution-summary',
          category: 'attribution',
          name : "Content Attribution",
          heading : "Content Attribution",
          type : "CUSTOM"
        });

      this.reports.push({
          id: 'biz-content-type-coverage',
          category: 'coverage',
          name : "Content",
          heading : "Content",
          type : "CUSTOM"
        });

      this.reports.push(this.contentCoverageRptService.getReportConfig());

      this.reports.push(this.activityTrendRptService.getReportConfig());
      this.reports.push(this.activitySummaryRptService.getReportConfig());

      this.processCannedReports();

      this.initialized = true;  
    }
    
  }

  processCannedReports() {

    var cannedRptsConfig = this.userPrefService.getPrefByKey('reports.canned.list');
    
    if (cannedRptsConfig) {
      
      var cannedReports = cannedRptsConfig.config.reports;
      
      if (cannedReports) {
        
        cannedReports.forEach((cannedRep) => {
        
          for (var i = 0; i < this.reports.length; i++) {
          
            var baseRptDef = this.reports[i];
            
            if (cannedRep.baseReportId == baseRptDef.id) {
            
              var cannedRptDef: any = cloneDeep(baseRptDef);
              cannedRptDef.id = cannedRep.id;
              cannedRptDef.name = cannedRep.name;
              cannedRptDef.heading = cannedRep.heading;
              
              if (cannedRptDef.options && cannedRep.options) {
                cannedRptDef.options = Object.assign(cannedRptDef.options, cannedRep.options);
              }
              
              if (cannedRep.filters && cannedRptDef.filters) {

                cannedRep.filters.forEach((cannedFilter) => {
                
                  for (var k = 0; k < cannedRptDef.filters.length; k++) {
                  
                    var rptFilter = cannedRptDef.filters[k];
                    
                    if (rptFilter.id == cannedFilter.id) {
                      rptFilter.type = cannedFilter.type;
                      
                      rptFilter.masterList = function() { // This must return a promise
                        return Observable.of(cannedFilter.value);
                      };
                      
                      rptFilter.defaultValue = function() {
                        return cannedFilter.value;
                      };

                      rptFilter.validator = this.passThruValidator;
                      
                      var baseValueTx = rptFilter.valueTx;
                      rptFilter.valueTx = new FixedDataFilterValueProxyTx(baseValueTx, cannedFilter.value);
                      
                      break;
                    }
                  }
                });
              }
              
              // insert after the base report
              this.reports.splice(i + 1, 0, cannedRptDef);
              
              break;
            }
          }
        });
      }
    }
    
  }

  getReports() {
    return this.reports;
  }

}

