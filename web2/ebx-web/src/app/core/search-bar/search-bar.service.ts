import { Injectable, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { ApiUrlService } from '../api-url.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { ContentTemplateService } from '../content-template.service';
import { ContentService, ContentDeleteEvent, ContentUpdateEvent } from '../content/content.service';
import { SearchBarData, NavContext, ObjectType, SearchBarItem, NavCtxItem } from '../../model/search-bar-data.model';
import { Container } from '../../model/container.model';
import { LocalDataset } from './local-dataset';
import { BoundedItemsDSBuilderService } from './bounded-items-dsbuilder.service';
import { LinkedContainerDsbuilderService } from './linked-container-dsbuilder.service';


/**
  
  Navigation paths:
    1. Dashboard 
    2. Biz Dim List
    3. Biz Dim List + Filters
    4. Biz Dim Detail 
    5. Biz Dim Detail + Biz Content Type(s)
    6. Biz Content List
    7. Biz Content List + Filters

 **/


@Injectable()
export class SearchBarService {

  @Output() onSearchBarDataUpdate = new EventEmitter<void>();
  searchBarData: SearchBarData;

  initiated: boolean = false;

  bizDimListDataset: LocalDataset;
  bizContentListDataset: LocalDataset;

  bizDimObjListDataset: LocalDataset;


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

  constructor(private navService: NavigationService, 
              private ctService: ContentTemplateService,
              private contentService: ContentService,
              private biDSBuilder: BoundedItemsDSBuilderService,
              private lcDSBuilder: LinkedContainerDsbuilderService,
              private route: ActivatedRoute,
              private apiUrlService: ApiUrlService,
              private http: HttpClient) { 
    
    this.contentService.onContentUpdate.subscribe(res => {
      this.checkAndUpdateDimObjList(res);
    });

    this.ctService.onTemplateLoad.subscribe(res => {
      this.initiated = false;
      this.init();
    });
  }

  init() {

    if (!this.initiated) {

      this.initiated = true;
      
      this.loadBizDimObjects();

      // search bar row of business dimensions
      let bizDimSearchItems = 
        this.ctService.templateCache.bizDimensionContainers.map(cont => {
          
          let item = new NavCtxItem();
          
          item.id = cont.qualifiedId;
          item.label = cont.label;
          item.type = ObjectType.BIZ_DIM;
          item.color = cont.color;
          item.data = cont;

          item.routeParams = [cont.qualifiedId];
          return item;
        })

      this.bizDimListDataset = new LocalDataset("Dimensions", bizDimSearchItems);

      // search bar row of business content
      let bizContentSearchItems = 
        this.ctService.templateCache.bizContentContainers.map(cont => {
          
          let item = new NavCtxItem();
          
          item.id = cont.qualifiedId;
          item.label = cont.label;
          item.type = ObjectType.BIZ_CONTENT;
          item.color = cont.color;
          item.data = cont;
          
          item.routeParams = [cont.qualifiedId];
          return item;
        })

      this.bizContentListDataset = new LocalDataset("Content Types", bizContentSearchItems);

      this.searchBarData = new SearchBarData();
    }

    return this.searchBarData;
  }

  loadBizDimObjects() {
    
    let bizDimObjects: NavCtxItem[] = [];

    this.ctService.templateCache.bizDimensionContainers.forEach(cont => {
    
      this.contentService.getAllRecords(cont.qualifiedId).subscribe(res => {
          res.content.forEach(ct => {
            let item = new NavCtxItem();
            item.id = ct.identity;
            item.label = ct.__title;
            item.color = cont.color;
            item.type = ObjectType.BIZ_DIM_OBJ;

            item.routeParams = [cont.qualifiedId, ct.identity];
            item.data = { container: cont, record: ct};

            bizDimObjects.push(item);
          });
        });
    });

    this.bizDimObjListDataset = new LocalDataset("Objects", bizDimObjects);
  }

  checkAndUpdateDimObjList(contentUpdate: ContentUpdateEvent) {
    
    if (contentUpdate && contentUpdate.new) {
      
      let container = this.ctService.getContainerByQId(contentUpdate.containerQId);
      
      if (container) {
        
        if (this.ctService.isBusinessDimension(container)) {
          
          let ct = contentUpdate.record;

          let item = new NavCtxItem();
          item.id = ct.identity;
          item.label = ct[container.titleItemId];
          item.color = container.color;
          item.type = ObjectType.BIZ_DIM_OBJ;

          item.routeParams = [container.qualifiedId, ct.identity];
          item.data = { container: container, record: ct};

          this.bizDimObjListDataset.data.push(item);
        }
      }
    }
  }

  updateSearchBarData(sbData: SearchBarData) {
    this.searchBarData = sbData;
    this.onSearchBarDataUpdate.emit();
  }

  setDashboardSearchBar() {
    
    let sbData = new SearchBarData();
    sbData.context = new NavContext();
    sbData.datasets = [this.bizDimListDataset, this.bizDimObjListDataset, this.bizContentListDataset];

    this.updateSearchBarData(sbData);
  }

  setFreetextSearchBar(text: string) {
    
    let sbData = new SearchBarData();
    sbData.context = new NavContext();
    sbData.freetext = text;
    sbData.datasets = [this.bizDimListDataset, this.bizDimObjListDataset, this.bizContentListDataset];

    this.updateSearchBarData(sbData);
  }

  setBizDimListSearchBar(container: Container, queryFilters: string[]) {
    let sbData = new SearchBarData();
    sbData.context = this.buildBizDimListContext(container);
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.biDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }

  setBizDimDetailSearchBar(container: Container, record: any, queryFilters: string[]) {
    let sbData = new SearchBarData();
    sbData.context = this.buildBizDimObjectContext(container, record);
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.lcDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }

  setBizContentListSearchBar(container: Container, queryFilters: string[]) {
    let sbData = new SearchBarData();
    sbData.context = this.buildBizContentListNavCtx(container);
    sbData.initialFilterIds = queryFilters;
    sbData.datasets = this.biDSBuilder.buildSearchDatasets(container, sbData);
    this.updateSearchBarData(sbData);
  }
  
  addSearchBarItem(sbItem: SearchBarItem) {
    sbItem.addToSearchBarData(this.searchBarData);
    this.onSearchBarDataUpdate.emit();
    this.checkAndNavigate();
  }

  removeSearchBarItem(sbItem: SearchBarItem) {
    sbItem.removeFromSearchBarData(this.searchBarData);
    this.onSearchBarDataUpdate.emit();
    this.checkAndNavigate();
  };

  checkAndNavigate() {
    
    let navPath = '';
    let routeParams = [];
    let queryParams = {};

    let ctxItems = this.searchBarData.context.contextItems;
    for (let i = 0; i < ctxItems.length; i++) {
      navPath += '>' + ctxItems[i].type;
      ctxItems[i].routeParams.forEach(param => routeParams.push(param));
    }

    let filters = this.searchBarData.filters;
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

  buildBizTypeItem(container: Container, type: ObjectType) : NavCtxItem {
    let item = new NavCtxItem();
    item.id = container.qualifiedId;
    item.label = container.label;
    item.type = type;
    item.color = container.color;
    item.routeParams = [container.qualifiedId];
    return item;
  }

  buildBizDimObjectItem(container: Container, record: any) : NavCtxItem {
    let item = new NavCtxItem();
    item.id = record.identity;
    item.label = record.__title;
    item.type = ObjectType.BIZ_DIM_OBJ;
    item.color = container.color;
    item.routeParams = [container.qualifiedId, record.identity];
    return item;
  }

  buildBizContentListNavCtx(bizDimContainer: Container) : NavContext {
    return {
      contextItems: [this.buildBizTypeItem(bizDimContainer, ObjectType.BIZ_CONTENT)]
    }
  }


  buildBizDimListContext(bizDimContainer: Container) {
    return {
      contextItems: [this.buildBizTypeItem(bizDimContainer, ObjectType.BIZ_DIM)]
    }
  }


  buildBizDimObjectContext(bizDimContainer: Container, record: any) {
    return {
      contextItems: [this.buildBizDimObjectItem(bizDimContainer, record)]
    }
  }

  buildFiltersFromQueryParams(queryParams: any) {

    let fs = {};
    
    for (let prop in queryParams) {
      
      let filterId = this.getFilterId(prop);

      if (filterId) {
      
        let propVal = queryParams[prop];
        if (typeof propVal === 'string') {
          fs[filterId] = [propVal];
        } else {
          fs[filterId] = propVal;  
        }
        
      }
    }

    return fs;
  }

  getFilterIdsFromQueryParams(queryParams: any) {

    let filterIds: string[] = [];

    for (let prop in queryParams) {
      
      let filterId = this.getFilterId(prop);

      if (filterId) {
      
        let propVal = queryParams[prop];
        if (typeof propVal === 'string') {
          
          filterIds.push(propVal)

        } else if (propVal instanceof Array) {
          
          propVal.forEach(val => {
            filterIds.push(val);
          });
        }
        
      }
    }

    return filterIds;
  }
  
  private getFilterId(queryParam: string) {
    if (queryParam.startsWith("sf_")) {
      return queryParam.substring(3);
    }
    return null;
  }

  typeaheadSearchBizContent(text: string, pageNum: number = 0, pageSize: number = 5) {
    let apiUrl = this.apiUrlService.postTypeaheadBizContentSearch();
    return this.http.post(apiUrl, {
              text: text,
              page: pageNum,
              size: pageSize
            }); 
  }

}

class NavState {
  navPath: string;
  routePath: string;
}
