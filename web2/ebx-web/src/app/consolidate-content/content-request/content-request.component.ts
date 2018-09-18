import { Component, OnInit, ViewEncapsulation, ViewChildren, QueryList } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Router, ActivatedRoute } from '@angular/router';

import { ConsolidateContentService } from '../consolidate-content.service';
import { ContentRequestListComponent, ContentRequestActions } from '../content-request-list.component';
import { AlertService } from '../../core/alert/alert.service';
import { DataPage } from '../../model/data-page.model';
import { TableColumn, TableActionConfig, TableAction } from '../../core/model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { DataFilter, DataFiltersOptions, DataFiltersConfig, DataFilterValueTx, AllOptionFilterValueTx, AtleastOneValueDataFilterValidator } from '../../core/data-filters/data-filters.model';
import { Constants } from '../../util/constants';
import { UserService } from '../../core/auth/user.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { ContentWorkflowService } from '../../services/content-workflow.service';
import { ContentRequestDetail, SimpleActionInput } from '../../model/content-workflow.model';
import { ContentTemplateService } from '../../../app/core/content-template.service';

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
        public ctService: ContentTemplateService,
        public alert: AlertService, public user: UserService,
        public navService: NavigationService, public router: Router) { 

    super(ccService, contentWFService, ctService, alert, user, navService, router);
    this.tableActions = 
        new ContentRequestActions(this, [
                ContentWorkflowService.ACTION_APPROVE, 
                ContentWorkflowService.ACTION_REJECT,
                ContentWorkflowService.ACTION_WITHDRAW
              ]);
  }

  ngOnInit() {

    super.initComponent()

    this.filters = { 
      requestStateNotIn: [ ContentWorkflowService.STATE_DRAFT, ContentWorkflowService.STATE_PUBLISHED]
    };
    
    let requestStateOptions = this.contentWFService.getContentRequestStateOptions();
    
    var defaultOption = {
      id: ContentWorkflowService.STATE_PENDING_APPROVAL, 
      label: this.contentWFService.stateDisplayText[ContentWorkflowService.STATE_PENDING_APPROVAL]
    };
    
    var allOption = { id: "~all~", label: "All" };
    requestStateOptions.unshift(allOption);

    this.pagination.sort.direction = Direction.DESC;

    let filterConfig: DataFilter[] = [
      {
        id: "requestStateIn",
        type: "multi-select",
        options: {
          singleSelect: true
        },
        name: "Request Status",
        masterList: function() {
          return Observable.of(requestStateOptions);
        },
        defaultValue: function() { return [defaultOption]; },
        valueTx: new AllOptionFilterValueTx(requestStateOptions),
        validator: new AtleastOneValueDataFilterValidator("Please select Request Status"),
      }
    ];

    this.dataFiltersConfig = {
      filters: filterConfig,
      options: {
        heading: "Filter",
        resetLabel: "Reset",
        searchLabel: "Go",
        searchOnLoad: true
      }
    }
    
  }

}