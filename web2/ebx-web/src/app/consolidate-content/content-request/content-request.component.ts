import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { ConsolidateContentService } from '../consolidate-content.service';
import { ContentRequestListComponent } from '../content-request-list.component';
import { AlertService } from '../../core/alert/alert.service';
import { DataPage } from '../../model/data-page.model';
import { TableColumn, TableActionConfig, TableAction } from '../../model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../model/pagination.model';
import { Constants } from '../../util/constants';
import { TableComponent } from '../../table/table.component';
import { UserService } from '../../core/auth/user.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { ContentWorkflowService } from '../../services/content-workflow.service';
import { ContentRequestDetail, SimpleActionInput } from '../../model/content-workflow.model';

@Component({
  selector: 'ebx-content-request',
  templateUrl: '../content-request-list.component.html',
  styleUrls: ['./content-request.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentRequestComponent extends ContentRequestListComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  tableActions: ContentRequestActions;

  constructor(public ccService: ConsolidateContentService,
        public contentWFService: ContentWorkflowService,
        public alert: AlertService, public user: UserService,
        public navService: NavigationService) { 

    super(ccService, contentWFService, alert, user, navService);
    this.tableActions = new ContentRequestActions(this);
  }

  ngOnInit() {

    super.initComponent()

    this.filters = { requestState: ConsolidateContentService.STATE_PENDING_APPROVAL };

    this.fetchData();
    
  }

}


class ContentRequestActions implements TableActionConfig<any> {

  component: ContentRequestComponent;

  actions: TableAction<any>[];

  constructor(comp: ContentRequestComponent) {
    
    this.component = comp;
    this.actions = [
      {
        label: "Approve",
        iconClass: "approve",
        successMessage: "Approved",
        execute: (selectedRecords: any[]) => {
          return this.component.approveRecords(selectedRecords);
        }
      },
      {
        label: "Reject",
        iconClass: "reject",
        successMessage: "Rejected",
        execute: (selectedRecords: any[]) => {
          return this.component.rejectRecords(selectedRecords);
        }
      }
    ];

  }

  getAvailableActions(selectedRecords: any[]) : TableAction<any>[] {
    console.log(selectedRecords);
    return selectedRecords && selectedRecords.length > 0 ? this.actions : [];
  }

}