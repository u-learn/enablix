import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';

import { AlertService } from '../../core/alert/alert.service';
import { DataPage } from '../../model/data-page.model';
import { RecentContentService } from '../recent-content.service';
import { Utility } from '../../util/utility';
import { GlobalSearchControllerService } from '../../core/search-bar/global-search-controller.service';
import { TableColumn, TableActionConfig, TableAction } from '../../core/model/table.model';
import { DataType } from '../../core/data-search/filter-metadata.model';
import { Pagination, SortCriteria, Direction } from '../../core/model/pagination.model';
import { Constants } from '../../util/constants';
import { NavigationService } from '../../app-routing/navigation.service';

@Component({
  selector: 'ebx-recent-content-list',
  templateUrl: './recent-content-list.component.html',
  styleUrls: ['./recent-content-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RecentContentListComponent implements OnInit {

  dataPage: DataPage;
  tableColumns: TableColumn[];

  filters: {[key: string] : any};
  pagination: Pagination;

  constructor(private route: ActivatedRoute, 
              private recentContentService: RecentContentService,
              private alert: AlertService, 
              private navService: NavigationService, 
              private router: Router,
              private globalSearch: GlobalSearchControllerService) { }

  ngOnInit() {

    this.globalSearch.setDashboardSearchBar();

    this.tableColumns = [
      {
        heading: "",
        key: "updateType"
      },
      {
        heading: "Content Title",
        key: "contentTitle",
        sortProp: "data.title"
      },
      {
        heading: "Content Type",
        key: "contentType",
        sortProp: "data.contentQId"
      },
      {
        heading: "Updated On",
        key: "updatedOn",
        sortProp: "createdAt"
      }
    ];

    this.pagination = new Pagination();
    this.pagination.pageNum = 0;
    this.pagination.pageSize = 10;
    this.pagination.sort = new SortCriteria();
    this.pagination.sort.direction = Direction.DESC;
    this.pagination.sort.field = Constants.FLD_CREATED_AT;

    this.route.queryParams.subscribe(queryParams => {
      this.filters = Utility.readSearchFiltersInQueryParams(queryParams);
      this.fetchData(); 
    });
    
  }

  fetchData() {
    this.recentContentService.fetchRecentContent(false, this.pagination.pageSize, this.pagination.pageNum, this.filters, this.pagination).subscribe(res => {
          this.dataPage = res;
        }, err => {
          this.alert.error("Unable to fetch recent updates. Please try later.", err.statusCode);
        });
  }

  getIconUrl(act: any) : string {
    return this.recentContentService.getIconUrl(act);
  }

  getContentColor(act: any) : string {
    return this.recentContentService.getContentColor(act);
  }

  navToItemDetail(act: any) {    
    this.navService.goToRecordDetail(act.data.contentQId, act.data.recordIdentity, this.router.url);
  }


}
