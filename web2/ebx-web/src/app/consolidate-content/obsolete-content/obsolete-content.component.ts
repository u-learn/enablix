import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { ContentQualityService } from '../../core/content/content-quality.service';
import { DataPage } from '../../model/data-page.model';
import { AlertService } from '../../core/alert/alert.service';
import { TableColumn, TableActionConfig, TableAction } from '../../core/model/table.model';
import { NavigationService } from '../../app-routing/navigation.service';
import { ContentTemplateService } from '../../core/content-template.service';
import { ContentService } from '../../core/content/content.service';
import { UserService } from '../../core/auth/user.service';

@Component({
  selector: 'ebx-obsolete-content',
  templateUrl: './obsolete-content.component.html',
  styleUrls: ['./obsolete-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ObsoleteContentComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  constructor(private cqService: ContentQualityService, 
    private alert: AlertService, public router: Router,
    private ctService: ContentTemplateService,
    private contentService: ContentService,
    private navService: NavigationService,
    private userService: UserService) { }

  ngOnInit() {

    this.tableColumns = [
      {
        heading: "Content Asset",
        key: "asset"
      },
      {
        heading: "Content Type",
        key: "contentType",
        sortProp: "objectRef.contentQId"
      },
      {
        heading: "Verification Due",
        key: "verifyDueDate",
        sortProp: "alert.info.obsoleteOn"
      },
      {
        heading: "Author",
        key: "author",
        sortProp: "authorName"
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.ASC;
    this.pagination.sort.field = 'alert.info.obsoleteOn';

    this.filters = { 
      alertRuleId: 'OBSOLETE_CONTENT_VERIFY_RULE'
    };

    if (this.userService.userHasPermission('VERIFY_ANY_CONTENT')) {
      this.filters['authorId'] = this.userService.getUserIdentity();
    }

    this.fetchData();
  }

  fetchData() {
    this.cqService.getQualityAlerts(this.filters, this.pagination).subscribe((res: any) => {
      this.dataPage = res;
    }, error => {
      this.alert.error("Error fetching verification record list", error.status);
    });
  }

  navToRecordDetail(rec: any) {
    this.navService.goToRecordDetail(rec.contentQualityAlert.contentQId, 
      rec.contentQualityAlert.recordIdentity, this.router.url);
  }

  getRecordContainer(rec: any) {
    return this.ctService.getContainerByQId(rec.contentQualityAlert.contentQId);
  }

  getDecoratedContentRecord(rec: any) {
    this.contentService.decorateRecord(this.getRecordContainer(rec), rec.record);
    return rec.record;
  }

}
