import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, RouterLink } from '@angular/router';
import { NvD3Component, NvD3Module } from 'ng2-nvd3';

import 'd3';
import 'nvd3';

import { ReportsHomeComponent } from './reports-home/reports-home.component';
import { CoreModule } from '../core/core.module';
import { ReportService } from './report.service';
import { ContentCoverageReportService } from './content-coverage-report.service';
import { ActivitySummaryReportService } from './activity-summary-report.service';
import { ActivityTrendReportService } from './activity-trend-report.service';
import { ReportDetailComponent } from './report-detail/report-detail.component';
import { HeatmapChartComponent } from './heatmap-chart/heatmap-chart.component';
import { ActivityAuditComponent } from './activity-audit/activity-audit.component';
import { ContentEngagementComponent } from './content-engagement/content-engagement.component';
import { ContentEngagementDistributionComponent } from './content-engagement/content-engagement-distribution/content-engagement-distribution.component';
import { ContentAttributionComponent } from './content-attribution/content-attribution.component';
import { OppAttributionComponent } from './content-attribution/opp-attribution/opp-attribution.component';

@NgModule({
  imports: [
    CoreModule,
    RouterModule,
    BrowserModule,
    NvD3Module
  ],
  providers: [
    ReportService,
    ContentCoverageReportService,
    ActivitySummaryReportService,
    ActivityTrendReportService
  ],
  declarations: [
    ReportsHomeComponent,
    ReportDetailComponent,
    HeatmapChartComponent,
    ActivityAuditComponent,
    ContentEngagementComponent,
    ContentEngagementDistributionComponent,
    ContentAttributionComponent,
    OppAttributionComponent
  ],
  exports: [
    ReportsHomeComponent
  ],
  entryComponents: [
    ContentEngagementDistributionComponent,
    OppAttributionComponent
  ],
})
export class ReportsModule { }
