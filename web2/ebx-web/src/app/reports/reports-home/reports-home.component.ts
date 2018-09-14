import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { ReportService } from '../report.service';
import { ReportBaseConfig, ReportConfig } from '../model/report-config.model';
import { AlertService } from '../../core/alert/alert.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { UserService } from '../../core/auth/user.service';

@Component({
  selector: 'ebx-reports-home',
  templateUrl: './reports-home.component.html',
  styleUrls: ['./reports-home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ReportsHomeComponent implements OnInit {

  reports: ReportBaseConfig[];
  reportCategories: any[];

  selectedRpt: ReportBaseConfig;
  selectedRptCategory: string;

  rptsByCategory: Map<string, ReportBaseConfig[]>;

  subHeading: string;

  constructor(private reportService: ReportService,
              private alert: AlertService,
              private route: ActivatedRoute,
              private navService: NavigationService,
              private user: UserService) { }

  ngOnInit() {
    
    this.reportCategories = [
      {
        id: 'activity',
        label: 'Activity'
      },
      {
        id: 'coverage',
        label: 'Coverage'
      },
      {
        id: 'engagement',
        label: 'Engagement'
      },
      {
        id: 'attribution',
        label: 'Attribution'
      }
    ]

    this.reportService.init();
    this.reports = this.reportService.getReports();
    
    this.rptsByCategory = new Map();

    this.reports.forEach((rpt: ReportConfig) => {
      if (this.hasReportPermission(rpt)) {
        var rpts = this.rptsByCategory.get(rpt.category);
        if (!rpts) {
          rpts = [];
          this.rptsByCategory.set(rpt.category, rpts);
        }
        rpts.push(rpt);
      }
    });

    this.route.params.subscribe(params => {
      
      this.subHeading = null;
      let rptId = params['reportId'];
      this.selectReport(rptId);

    });

  }

  selectReport(rptId: string) {
    if (rptId && this.reports) {      
      for(let i in this.reports) {
        let rpt = this.reports[i];
        if (rpt.id == rptId) {
          this.selectedRpt = rpt;
          this.selectedRptCategory = rpt.category;
          break;
        }
      }
    }
  }

  getReportsByCategory(rptCategory: string) {
    let rpts = this.rptsByCategory.get(rptCategory);
    return !rpts ? [] : rpts; 
  }

  navToFirstRptOfCategory(rptCat: string) {
    let rpts = this.rptsByCategory.get(rptCat);
    if (rpts && rpts.length > 0) {
      for (let i in rpts) {
        let rpt = rpts[i];
        if (this.hasReportPermission(rpt)) {
          this.navToReport(rpt);
          break;
        }
      }
    }
  }

  navToReport(rpt: ReportBaseConfig) {
    this.navService.goToReport(rpt.id);
  }

  hasAccess(rptCat: string) {
    
    let rptPerms = [];
    let rpts = this.rptsByCategory.get(rptCat);
    
    if (rpts) {
      
      rpts.forEach((rpt) => {
        rptPerms.push(this.getReportPermName(rpt));
      });
    }

    return this.user.userHasAtleastOnePermission(rptPerms);;
  }

  getReportPermName(rpt: ReportBaseConfig) {
    return 'VIEW_REPORT-' + rpt.id;
  }

  hasReportPermission(rpt: ReportBaseConfig) {
    if (rpt) {
      return this.user.userHasPermission(this.getReportPermName(rpt));
    }
    return false;
  }

}
