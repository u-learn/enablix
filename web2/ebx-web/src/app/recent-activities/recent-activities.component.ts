import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { environment } from '../../environments/environment';

import { ActivityAuditService } from '../services/activity-audit.service';
import { FilterMetadata, DataType, ConditionOperator } from '../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../model/pagination.model';
import { DataPage } from '../model/data-page.model';
import { Constants } from '../util/constants';
import { AlertService } from '../core/alert/alert.service';
import { ContentTemplateService } from '../core/content-template.service';
import { NavigationService } from '../app-routing/navigation.service';


@Component({
  selector: 'ebx-recent-activities',
  templateUrl: './recent-activities.component.html',
  styleUrls: ['./recent-activities.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RecentActivitiesComponent implements OnInit {

  recentActivities: DataPage;

  constructor(private actAuditService: ActivityAuditService,
              private ctService: ContentTemplateService,
              private alert: AlertService,
              private navService: NavigationService) { }

  ngOnInit() {

    let filters = {
      activityTypeIn: [
        "CONTENT_ADD",
        "CONTENT_UPDATE",
        "DOC_DOWNLOAD",
        "CONTENT_SUGGEST_APPROVED",
        "CONTENT_SUGGEST_REJECT"
      ]
    };

    let pagination = new Pagination();
    pagination.pageNum = 0;
    pagination.pageSize = 10;
    
    pagination.sort = new SortCriteria();
    pagination.sort.field = Constants.FLD_MODIFIED_AT;
    pagination.sort.direction = Direction.DESC;

    this.actAuditService.fetchActivities(filters, pagination)
        .subscribe(
            res => {
              this.recentActivities = res;
            },
            error => {
              this.alert.error("Error in fetching recent activities");
            }
          );
  }

  getTextDescription(act: any) : string {
    return this.actAuditService.getTextDescription(act);
  }

  getIconUrl(act: any) : string {
    return environment.baseAPIUrl + "/doc/icon/r/" 
      + act.activity.containerQId + "/" + act.activity.itemIdentity + "/";
  }

  getContentColor(act: any) : string {
    let color = null;
    let container = this.ctService.getContainerByQId(act.activity.containerQId);
    if (container && this.ctService.isBusinessDimension(container)) {
      color = container.color;
    }
    return color;
  }

  navToItemDetail(act: any) {
    
    let container = this.ctService.getContainerByQId(act.activity.containerQId);
    
    if (container) {
      if (this.ctService.isBusinessDimension(container)) {
        this.navToDimDetail(container.qualifiedId, act.activity.itemIdentity);
      } else {
        this.navToContentDetail(container.qualifiedId, act.activity.itemIdentity);
      }
    }
  }

  navToDimDetail(containerQId: string, recIdentity: string) {
    if (containerQId && recIdentity) {
      this.navService.goToDimDetail(containerQId, recIdentity);
    }
  }

  navToContentDetail(containerQId: string, recIdentity: string) {
    if (containerQId && recIdentity) {
      this.navService.goToContentDetail(containerQId, recIdentity);
    }
  }
}
