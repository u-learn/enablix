import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { environment } from '../../environments/environment';

import { ActivityAuditService } from '../services/activity-audit.service';
import { FilterMetadata, DataType, ConditionOperator } from '../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../model/pagination.model';
import { DataPage } from '../model/data-page.model';
import { Constants } from '../util/constants';
import { AlertService } from '../core/alert/alert.service';


@Component({
  selector: 'ebx-recent-activities',
  templateUrl: './recent-activities.component.html',
  styleUrls: ['./recent-activities.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RecentActivitiesComponent implements OnInit {

  recentActivities: DataPage;

  constructor(private actAuditService: ActivityAuditService,
              private alert: AlertService) { }

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

}
