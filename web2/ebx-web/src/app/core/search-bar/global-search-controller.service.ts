import { Injectable, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { AppContext } from '../../app-context';
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
      routePath: AppContext.homePage || "/portal"
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

  constructor(private router: Router,
        private route: ActivatedRoute, 
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
    let queryParams = this.sbService.getFilterQueryParams(this.sbData);

    let ctxItems = this.sbData.context.contextItems;
    for (let i = 0; i < ctxItems.length; i++) {
      navPath += '>' + ctxItems[i].type;
      ctxItems[i].routeParams.forEach(param => routeParams.push(param));
    }

    var tq = this.sbData.freetext;
    
    if (navPath == '' && tq && tq != '') {
      
      this.navService.goToFreetextSearch(this.sbData.freetext); 

    } else {
      
      if (tq && tq != '') {
        queryParams['tq'] = tq;
      }
      
      for (let k = 0; k < this.navStates.length; k++) {
        var navState = this.navStates[k];
        if (navState.navPath == navPath) {
          this.navService.goToRoute(this.navStates[k].routePath, routeParams, queryParams);
        }
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

  setBizDimListSearchBar(container: Container, queryFilters: string[], textQuery: string = null) {
    let sbData = new SearchBarData();
    sbData.context = this.sbService.buildBizDimListContext(container);
    sbData.freetext = textQuery;
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.biDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }

  setBizDimDetailSearchBar(container: Container, childContainerWithData: any, record: any, queryFilters: string[], textQuery: string) {
    let sbData = new SearchBarData();
    sbData.context = this.sbService.buildBizDimObjectContext(container, record);
    sbData.freetext = textQuery;
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.lcDSBuilder.buildSearchDatasets(container, childContainerWithData, sbData);
    this.updateSearchBarData(sbData);
  }

  setBizContentListSearchBar(container: Container, queryFilters: string[], textQuery: string) {
    let sbData = new SearchBarData();
    sbData.context = this.sbService.buildBizContentListNavCtx(container);
    sbData.freetext = textQuery;
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.biDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }

  typeaheadItemSelected(bizItem: any) {
    // nav to record details
    var contentQId = bizItem.contentQId;
    var recIdentity = bizItem.record.identity;
    if (contentQId && recIdentity) {
      var atCtxParams = {
        atChannel: "WEB",
        atContext: "SEARCH",
        atActivityOrigin: "TYPEAHEAD",
        atContextTerm: this.sbData.freetext
      }
      this.navService.goToRecordDetail(contentQId, recIdentity, atCtxParams, this.router.url);
    }
  }

  doFreetextSearch(text: any) {
    if (text) { 
      this.sbData.freetext = text;
      this.checkAndNavigate();
    }
  }

  clearFreetext() {
    this.sbData.freetext = null;
    this.checkAndNavigate();
  }

}

class NavState {
  navPath: string;
  routePath: string;
}
