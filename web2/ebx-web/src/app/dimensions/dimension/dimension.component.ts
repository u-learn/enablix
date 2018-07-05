import { Component, OnInit, ViewEncapsulation, Input, OnChanges, SimpleChanges } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Constants } from '../../util/constants';
import { Container } from '../../model/container.model';
import { DataSearchService } from '../../core/data-search/data-search.service';
import { DataSearchRequest } from '../../core/data-search/data-search-request.model';
import { Pagination, Direction, SortCriteria } from '../../core/model/pagination.model';
import { DataPage } from '../../model/data-page.model';
import { NavigationService } from '../../app-routing/navigation.service';
import { EditBizDimensionComponent } from '../../biz-dimension/edit-biz-dimension/edit-biz-dimension.component';
import { AlertService } from '../../core/alert/alert.service';
import { FilterMetadata } from '../../core/data-search/filter-metadata.model';

const DIM_PAGE_SIZE = 4;

@Component({
  selector: 'ebx-dimension',
  templateUrl: './dimension.component.html',
  styleUrls: ['./dimension.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DimensionComponent implements OnInit, OnChanges {

  @Input() container: Container;
  @Input() showCount?: number = DIM_PAGE_SIZE;
  @Input() showAddAction?: boolean = false;

  @Input() filterMetadata?: { [key: string] : FilterMetadata };
  @Input() filters?: { [key: string] : any};

  @Input() textFilter?: string;
  
  data: DataPage;
  remainingCount = 0;

  constructor(private dataSearchService: DataSearchService,
              private navService: NavigationService,
              private alert: AlertService,
              private dialog: MatDialog) { }

  ngOnInit() {
    this.fetchData();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.filters && !changes.filters.isFirstChange()) {
      this.fetchData();
    }
  }

  fetchData() {
    let searchRequest = new DataSearchRequest();
    
    searchRequest.projectedFields = [];

    searchRequest.filterMetadata = this.filterMetadata ? this.filterMetadata : {};

    searchRequest.filters = this.filters ? this.filters : {};
    searchRequest.textQuery = this.textFilter;

    searchRequest.pagination = new Pagination();
    searchRequest.pagination.pageNum = 0;
    searchRequest.pagination.pageSize = this.showCount;
    
    searchRequest.pagination.sort = new SortCriteria();
    searchRequest.pagination.sort.field = Constants.FLD_MODIFIED_AT;
    searchRequest.pagination.sort.direction = Direction.DESC;

    this.dataSearchService.getContainerDataSearchResult(this.container.qualifiedId, searchRequest)
        .subscribe(
            result => {
               this.data = result;
               if (this.data.content && this.data.totalElements > this.showCount) {
                 this.data.content.splice(-1, 1); // remove the last record
                 this.remainingCount = this.data.totalElements - this.showCount + 1;
               }
            },
            error => {
              this.alert.error("Error fetching dimensions. Please try again later.", error.status); 
            }
          );
  }


  navToDimList() {
    this.navService.goToDimList(this.container.qualifiedId);
  }

  navToDimDetail(recIdentity: string) {
    this.navService.goToDimDetail(this.container.qualifiedId, recIdentity);
  }

  addNewDim() {

    let dialogRef = this.dialog.open(EditBizDimensionComponent, {
        width: '624px',
        disableClose: true,
        data: { containerQId: this.container.qualifiedId, newRecord: true }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.fetchData();
      }
    });
  }

}
