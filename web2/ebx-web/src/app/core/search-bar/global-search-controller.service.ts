import { Injectable, EventEmitter } from '@angular/core';

import { SearchBarController } from './search-bar-controller';
import { NavigationService } from '../../app-routing/navigation.service';
import { SearchBarData, SearchBarItem, SearchDataset, NavCtxItem, NavContext } from '../../model/search-bar-data.model';
import { SearchBarService } from './search-bar.service';
import { Container } from '../../model/container.model';
import { LocalDataset } from './local-dataset';
import { BoundedItemsDSBuilderService } from './bounded-items-dsbuilder.service';
import { LinkedContainerDsbuilderService } from './linked-container-dsbuilder.service';

@Injectable()
export class GlobalSearchControllerService implements SearchBarController {

  onDataUpdate = new EventEmitter<void>();

  sbData: SearchBarData;

  navStates: NavState[] = [
    {
      navPath: "",
      routePath: "/portal"
    },
    {
      navPath: ">BIZ_DIM",
      routePath: "/portal/dim/list"
    },
    {
      navPath: ">BIZ_DIM_OBJ",
      routePath: "/portal/dim/detail"
    },
    {
      navPath: ">BIZ_CONTENT",
      routePath: "/portal/content/list"
    }
  ]

  constructor(
        private navService: NavigationService,
        private sbService: SearchBarService,
        private biDSBuilder: BoundedItemsDSBuilderService,
        private lcDSBuilder: LinkedContainerDsbuilderService) { 
    this.sbData = new SearchBarData();
  }

  onSearchBarDataUpdate() : EventEmitter<void> {
    return this.onDataUpdate;
  }

  searchBarData(): SearchBarData {
    return this.sbData;
  }

  searchInputId() : string {
    return "globalSearchTB";
  }

  addSearchBarItem(sbItem: SearchBarItem) {
    sbItem.addToSearchBarData(this.sbData);
    this.onDataUpdate.emit();
    this.checkAndNavigate();
  }

  removeSearchBarItem(sbItem: SearchBarItem) {
    sbItem.removeFromSearchBarData(this.sbData);
    this.onDataUpdate.emit();
    this.checkAndNavigate();
  }

  checkAndNavigate() {
    
    let navPath = '';
    let routeParams = [];
    let queryParams = {};

    let ctxItems = this.sbData.context.contextItems;
    for (let i = 0; i < ctxItems.length; i++) {
      navPath += '>' + ctxItems[i].type;
      ctxItems[i].routeParams.forEach(param => routeParams.push(param));
    }

    let filters = this.sbData.filters;
    for (let j = 0; j < filters.length; j++) {
      let filter = filters[j];
      for (let prop in filter.queryParams) {
        if (!queryParams[prop]) {
          queryParams[prop] = []
        }
        queryParams[prop].push(filter.queryParams[prop]);
      }
    }

    for (let k = 0; k < this.navStates.length; k++) {
      if (this.navStates[k].navPath == navPath) {
        this.navService.goToRoute(this.navStates[k].routePath, routeParams, queryParams);
      }
    }

  }

  updateSearchBarData(sbData: SearchBarData) {
    this.sbData = sbData;
    this.onDataUpdate.emit();
  }

  setDashboardSearchBar() {
    
    let sbData = new SearchBarData();
    sbData.context = new NavContext();
    sbData.datasets = [
      this.sbService.bizDimListDataset, 
      this.sbService.bizDimObjListDataset, 
      this.sbService.bizContentListDataset
    ];

    this.updateSearchBarData(sbData);
  }

  setFreetextSearchBar(text: string) {
    
    let sbData = new SearchBarData();
    sbData.context = new NavContext();
    sbData.freetext = text;
    sbData.datasets = [
      this.sbService.bizDimListDataset, 
      this.sbService.bizDimObjListDataset, 
      this.sbService.bizContentListDataset
    ];

    this.updateSearchBarData(sbData);
  }

  setBizDimListSearchBar(container: Container, queryFilters: string[]) {
    let sbData = new SearchBarData();
    sbData.context = this.sbService.buildBizDimListContext(container);
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.biDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }

  setBizDimDetailSearchBar(container: Container, record: any, queryFilters: string[]) {
    let sbData = new SearchBarData();
    sbData.context = this.sbService.buildBizDimObjectContext(container, record);
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.lcDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }

  setBizContentListSearchBar(container: Container, queryFilters: string[]) {
    let sbData = new SearchBarData();
    sbData.context = this.sbService.buildBizContentListNavCtx(container);
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.biDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }

  typeaheadItemSelected(bizItem: any) {
    // nav to record details
    var contentQId = bizItem.contentQId;
    var recIdentity = bizItem.record.identity;
    if (contentQId && recIdentity) {
      this.navService.goToContentDetail(contentQId, recIdentity);
    }
  }

  doFreetextSearch(text: any) {
    if (text) {
      this.navService.goToFreetextSearch(text);  
    }
  }
  

}

class NavState {
  navPath: string;
  routePath: string;
}
