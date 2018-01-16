import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router, ActivatedRoute } from '@angular/router';

import { ConsolidateContentService } from '../consolidate-content.service';
import { ContentRequestListComponent, ContentRequestActions } from '../content-request-list.component';
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

  tableActions: ContentRequestActions;

  constructor(public ccService: ConsolidateContentService,
        public contentWFService: ContentWorkflowService,
        public alert: AlertService, public user: UserService,
        public navService: NavigationService, public router: Router) { 

    super(ccService, contentWFService, alert, user, navService, router);
    this.tableActions = 
        new ContentRequestActions(this, [
                ContentWorkflowService.ACTION_APPROVE, 
                ContentWorkflowService.ACTION_REJECT,
                ContentWorkflowService.ACTION_WITHDRAW
              ]);
  }

  ngOnInit() {

    super.initComponent()

    this.filters = { };
    
    this.pagination.sort.direction = Direction.DESC;

    this.fetchData();
    
  }

}