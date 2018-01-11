import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router, ActivatedRoute } from '@angular/router';

import { ContentRequestListComponent, ContentRequestActions } from '../content-request-list.component';
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
  selector: 'ebx-my-content-request',
  templateUrl: '../content-request-list.component.html',
  styleUrls: ['./my-content-request.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MyContentRequestComponent extends ContentRequestListComponent implements OnInit {

  tableActions: ContentRequestActions;

  constructor(public ccService: ConsolidateContentService,
        public contentWFService: ContentWorkflowService,
        public alert: AlertService, public user: UserService,
        public navService: NavigationService, public router: Router) { 
    
    super(ccService, contentWFService, alert, user, navService, router);
    
    this.tableActions = 
        new ContentRequestActions(this, [
                ContentWorkflowService.ACTION_WITHDRAW
              ]);
  }

  ngOnInit() {

    super.initComponent();

    this.filters = { 
      requestStateNotIn: [ ContentWorkflowService.STATE_DRAFT, ContentWorkflowService.STATE_PUBLISHED],
      createdBy: this.user.getUserIdentity()
    };

    this.pagination.sort.direction = Direction.DESC;

    this.fetchData();
    
  }
}
