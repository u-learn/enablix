import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ReportService } from '../report.service';
import { ReportConfig } from '../model/report-config.model';
import { AlertService } from '../../core/alert/alert.service';
import { NavigationService } from '../../app-routing/navigation.service';

@Component({
  selector: 'ebx-reports-home',
  templateUrl: './reports-home.component.html',
  styleUrls: ['./reports-home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ReportsHomeComponent implements OnInit {

  reports: ReportConfig[];
  selectedRpt: ReportConfig;

  constructor(private reportService: ReportService,
              private alert: AlertService,
              private route: ActivatedRoute,
              private navService: NavigationService) { }

  ngOnInit() {
    
    this.reportService.init();
    this.reports = this.reportService.getReports();

    this.route.params.subscribe(params => {
      
      let rptId = params['reportId'];

      if (rptId && this.reports) {      
        for(let i in this.reports) {
          let rpt = this.reports[i];
          if (rpt.id == rptId) {
            this.selectedRpt = rpt;
            break;
          }
        }
      }

    });
  }

  navToReport(rpt: ReportConfig) {
    this.navService.goToReport(rpt.id);
  }

}
