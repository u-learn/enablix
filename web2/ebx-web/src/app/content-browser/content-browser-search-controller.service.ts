import { Injectable, EventEmitter } from '@angular/core';

import { SearchBarController } from '../core/search-bar/search-bar-controller';
import { NavigationService } from '../app-routing/navigation.service';
import { SearchBarData, SearchBarItem, SearchDataset, NavCtxItem, NavContext, ObjectType } from '../model/search-bar-data.model';
import { SearchBarService } from '../core/search-bar/search-bar.service';
import { Container } from '../model/container.model';
import { LocalDataset } from '../core/search-bar/local-dataset';
import { BoundedItemsDSBuilderService } from '../core/search-bar/bounded-items-dsbuilder.service';
import { LinkedContainerDsbuilderService } from '../core/search-bar/linked-container-dsbuilder.service';
import { FilterMetadata } from '../core/data-search/filter-metadata.model';
import { DataSearchRequest } from '../core/data-search/data-search-request.model';
import { Pagination, SortCriteria, Direction } from '../core/model/pagination.model';
import { Constants } from '../util/constants';
import { ContentService } from '../core/content/content.service';
import { ContentTemplateService } from '../core/content-template.service';
import { AlertService } from '../core/alert/alert.service';
import { FreetextSearchService } from '../services/freetext-search.service';

@Injectable()
export class ContentBrowserSearchControllerService implements SearchBarController {

  onDataUpdate = new EventEmitter<void>();
  onBrowserDataUpdate = new EventEmitter<void>();

  sbData: SearchBarData;

  browserData: any;


  navStates: any[] = [
    {
      navPath: "",
      searchFn: null
    },
    {
      navPath: ">BIZ_DIM",
      searchFn: null
    },
    {
      navPath: ">BIZ_DIM_OBJ",
      searchFn: null
    },
    {
      navPath: ">BIZ_CONTENT",
      searchFn: this.fetchBizContentList.bind(this)
    },
    {
      navPath: ">FREE_TEXT",
      searchFn: this.performFreetextSearch.bind(this)
    },
    {
      navPath: ">BIZ_CONTENT>FREE_TEXT",
      searchFn: this.performFreetextSearch.bind(this)
    }
  ]

  pageNum: number = 0;
  pageSize: number = 10;
  hasNextPage: boolean = false;

  constructor(private sbService: SearchBarService,
    private contentService: ContentService,
    private ctService: ContentTemplateService,
    private textSearch: FreetextSearchService,
    private alert: AlertService) { }

  onSearchBarDataUpdate() : EventEmitter<void> {
    return this.onDataUpdate;
  }

  updateSearchBarData(sbData: SearchBarData) {
    this.sbData = sbData;
    this.onDataUpdate.emit();
  }

  searchBarData(): SearchBarData {
    return this.sbData;
  }

  searchInputId() : string {
    return "contentBrowserSearchTB";
  }

  initBrowserSearchBar(scopeContainer?: Container) {
    
    let sbData = new SearchBarData();
    sbData.context = new NavContext();

    var limitScope = false;
    if (scopeContainer) {
      if (this.ctService.isBusinessContent(scopeContainer)) {
        sbData.context = this.sbService.buildBizContentListNavCtx(scopeContainer, false);
        limitScope = true;
      } /*else if (this.ctService.isBusinessDimension(scopeContainer)) {
        sbData.context = this.sbService.buildBizDimListContext(scopeContainer, false);
      }*/
    }
    

    sbData.datasets = limitScope ? [] : [
      // this.sbService.bizDimListDataset, 
      // this.sbService.bizDimObjListDataset, 
      this.sbService.bizContentListDataset
    ];

    this.browserData = null;
    this.updateSearchBarData(sbData);
    this.checkAndFetchData();
  }

  addSearchBarItem(sbItem: SearchBarItem) {
    sbItem.addToSearchBarData(this.sbData);
    this.onDataUpdate.emit();
    this.checkAndFetchData();
  }

  removeSearchBarItem(sbItem: SearchBarItem) {
    sbItem.removeFromSearchBarData(this.sbData);
    this.onDataUpdate.emit();
    this.checkAndFetchData();
  }

  checkAndFetchData(nextPage: boolean = false) {
    
    if (nextPage && !this.hasNextPage) {
      return;
    }

    if (nextPage) {
      this.pageNum++;
    } else {
      this.pageNum = 0;
    }

    let navPath = '';

    let ctxItems = this.sbData.context.contextItems;
    for (let i = 0; i < ctxItems.length; i++) {
      navPath += '>' + ctxItems[i].type;
    }

    if (this.sbData.freetext) {
      navPath += '>' + 'FREE_TEXT'
    }

    var searchExec = false;
    for (let k = 0; k < this.navStates.length; k++) {
      if (this.navStates[k].navPath == navPath) {
        if (this.navStates[k].searchFn) {
          this.navStates[k].searchFn();
          searchExec = true;
          break;
        }
      }
    }

    if (!searchExec) {
      this.browserData = null;
      this.hasNextPage = false;
    }

  }

  typeaheadItemSelected(bizItem: any) {
    
    var contentQId = bizItem.contentQId;
    var recIdentity = bizItem.record.identity;

    if (contentQId && recIdentity) {
      this.contentService.getContentRecord(contentQId, recIdentity, null).subscribe(
        data => {

          this.browserData = [];
          this.hasNextPage = false;

          if (data) {
          
            var container = this.ctService.getConcreteContainerByQId(contentQId);
          
            if (container) {
              this.contentService.decorateRecord(container, data);
              this.browserData.push({
                containerQId: container.qualifiedId,
                record: data
              });

              this.onBrowserDataUpdate.emit();
            }
          }
        },
        err => {
          this.alert.error("Error fetching content record. Please try again later.", err.status);
        }
      )
    }
  }

  fetchBizContentList() {

    let containerQId = null;
    let ctxItems = this.sbData.context.contextItems;

    for (let i = 0; i < ctxItems.length; i++) {
      if (ctxItems[i].type == ObjectType.BIZ_CONTENT) {
        containerQId = ctxItems[i].id;
        break;
      }
    }

    // clear context. TODO: need to define new dataset
    this.sbData.datasets = [];

    if (containerQId) {

      let searchRequest = new DataSearchRequest();
      
      searchRequest.projectedFields = [];
      searchRequest.filters = {};
      searchRequest.filterMetadata = {};

      searchRequest.pagination = new Pagination();
      searchRequest.pagination.pageNum = this.pageNum;
      searchRequest.pagination.pageSize = this.pageSize;
      
      searchRequest.pagination.sort = new SortCriteria();
      searchRequest.pagination.sort.field = Constants.FLD_MODIFIED_AT
      searchRequest.pagination.sort.direction = Direction.DESC;

      this.contentService.getFilteredRecords(containerQId, searchRequest).subscribe(
        result => {
          
          if (this.pageNum == 0) {
            this.browserData = [];
          }

          this.hasNextPage = !result.last;

          var container = this.ctService.getConcreteContainerByQId(containerQId);
          if (container) {
            result.content.forEach((item) => {
              this.contentService.decorateRecord(container, item);
              this.browserData.push({
                containerQId: container.qualifiedId,
                record: item
              });
            });

            this.onBrowserDataUpdate.emit();
          }
        },
        error => {
          this.alert.error("Error fetching content records. Please try again later.", error.status); 
        }
      );
    }
  }


  performFreetextSearch() {
    this.doFreetextSearch(this.sbData.freetext, false);
  }

  doFreetextSearch(text: string, resetPage: boolean = true) {
    
    if (resetPage) {
      this.pageNum = 0;
    }

    if (text) {

      this.sbData.freetext = text;
      let searchScope = this.sbService.resolveSearchScope(this.sbData);
      this.textSearch.searchBizContent(text, this.pageNum, this.pageSize, searchScope).subscribe((res: any) => {
      
          if (res) {
      
            if (this.pageNum == 0) {
              this.browserData = [];
            }
            
            this.hasNextPage = res.currentPage < res.totalPages;

            res.content.forEach(item => {
      
              var container = this.ctService.getConcreteContainerByQId(item.containerQId);
      
              if (container) {
                this.contentService.decorateRecord(container, item.record);
                this.browserData.push({
                  containerQId: container.qualifiedId,
                  record: item.record
                });
              }
            });

            this.onBrowserDataUpdate.emit();
          }

        }, err => {
          this.alert.error("Unable to search. Please try later.", err.statusCode);
        }); 
    }
  }

  clearFreetext() {
    this.sbData.freetext = null;
    this.checkAndFetchData();
  }

}
