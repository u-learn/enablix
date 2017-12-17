import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { ContentRequestListComponent } from '../content-request-list.component';
import { ConsolidateContentService } from '../consolidate-content.service';
import { AlertService } from '../../core/alert/alert.service';
import { DataPage } from '../../model/data-page.model';
import { TableColumn, TableActionConfig, TableAction } from '../../model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../model/pagination.model';
import { ContentRequestDetail, SimpleActionInput } from '../../model/content-workflow.model';
import { Constants } from '../../util/constants';
import { TableComponent } from '../../table/table.component';
import { UserService } from '../../core/auth/user.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { ContentWorkflowService } from '../../services/content-workflow.service';

@Component({
  selector: 'ebx-my-draft',
  templateUrl: '../content-request-list.component.html',
  styleUrls: ['./my-draft.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MyDraftComponent extends ContentRequestListComponent implements OnInit {

  tableActions: MyDraftActions;

  constructor(public ccService: ConsolidateContentService,
        public contentWFService: ContentWorkflowService,
        public alert: AlertService, public user: UserService,
        public navService: NavigationService) { 
    
    super(ccService, contentWFService, alert, user, navService);
    this.tableActions = new MyDraftActions(this);
  }

  ngOnInit() {

    super.initComponent();

    this.filters = { 
      requestState: ConsolidateContentService.STATE_DRAFT,
      createdBy: this.user.getUsername()
    };

    this.fetchData();
    
  }

}

class MyDraftActions implements TableActionConfig<any> {

  component: MyDraftComponent;

  actions: TableAction<any>[];

  constructor(comp: MyDraftComponent) {
    
    this.component = comp;
    this.actions = [
      {
        label: "Publish",
        iconClass: "send-blue",
        successMessage: "Published",
        execute: (selectedRecords: any[]) => {
          return this.component.publishRecords(selectedRecords);
        }
      },
      {
        label: "Trash",
        iconClass: "trash",
        successMessage: "Deleted",
        execute: (selectedRecords: any[]) => {
          return this.component.deleteRecords(selectedRecords);
        }
      }
    ];

  }

  getAvailableActions(selectedRecords: any[]) : TableAction<any>[] {
    return selectedRecords && selectedRecords.length > 0 ? this.actions : [];
  }

}
