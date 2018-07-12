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
import { Utility } from '../../util/utility';

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

  initiated: boolean = false;
  searchBarData: SearchBarData;

  bizDimListDataset: LocalDataset;
  bizContentListDataset: LocalDataset;

  bizDimObjListDataset: LocalDataset;

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
        });

      Utility.sortArrayByLabel(bizContentSearchItems);

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

  buildBizTypeItem(container: Container, type: ObjectType, removable: boolean = true) : NavCtxItem {
    let item = new NavCtxItem();
    item.id = container.qualifiedId;
    item.label = container.label;
    item.type = type;
    item.removable = removable;
    item.color = container.color;
    item.routeParams = [container.qualifiedId];
    return item;
  }

  buildBizDimObjectItem(container: Container, record: any, removable: boolean = true) : NavCtxItem {
    let item = new NavCtxItem();
    item.id = record.identity;
    item.label = record.__title;
    item.type = ObjectType.BIZ_DIM_OBJ;
    item.removable = removable;
    item.color = container.color;
    item.routeParams = [container.qualifiedId, record.identity];
    return item;
  }

  buildBizContentListNavCtx(bizDimContainer: Container, removable: boolean = true) : NavContext {
    return {
      contextItems: [this.buildBizTypeItem(bizDimContainer, ObjectType.BIZ_CONTENT, removable)]
    }
  }


  buildBizDimListContext(bizDimContainer: Container, removable: boolean = true) {
    return {
      contextItems: [this.buildBizTypeItem(bizDimContainer, ObjectType.BIZ_DIM, removable)]
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

  getTextQueryFromQueryParams(queryParams: any) {
    return queryParams['tq'];
  }
  
  private getFilterId(queryParam: string) {
    if (queryParam.startsWith("sf_")) {
      return queryParam.substring(3);
    }
    return null;
  }

  getFilterQueryParams(sbData: SearchBarData, removeFilterPrefix: boolean = false) {
    
    let filters = sbData.filters;
    let queryParams = {};
    
    for (let j = 0; j < filters.length; j++) {
    
      let filter = filters[j];
    
      for (let prop in filter.queryParams) {
    
        var fltrId = removeFilterPrefix ? this.getFilterId(prop) : prop;
        if (!fltrId) {
          fltrId = prop;
        }

        if (!queryParams[fltrId]) {
          queryParams[fltrId] = []
        }
        queryParams[fltrId].push(filter.queryParams[prop]);
      }
    }
    return queryParams;
  }

  buildTextSearchInput(sbData: SearchBarData, text: string, pageNum: number = 0, pageSize: number = 10) {
    
    var contentQIds = [];
    var parent = null;
    
    sbData.context.contextItems.forEach((ci) => {
    
      if (ci.type == ObjectType.BIZ_CONTENT || ci.type == ObjectType.BIZ_DIM) {
    
        contentQIds.push(ci.id);
    
      } else if (ci.type == ObjectType.BIZ_CONTENT_OBJ || ci.type == ObjectType.BIZ_DIM_OBJ) {
    
        parent = {
          qualifiedId: ci.routeParams[0],
          identity: ci.routeParams[1]
        }
      }
    });

    var filters = this.getFilterQueryParams(sbData, true);
    if (filters['cqid']) {
      filters['cqid'].forEach((qid) => contentQIds.push(qid));
      delete filters['cqid'];
    }

    var filterMetadata = this.buildFilterMetadata(filters);

    return {
      contentQIds: contentQIds,
      parent: parent,
      request: {
        textQuery: text,
        filterMetadata: filterMetadata,
        filters: filters,
        pagination: {
          pageNum: pageNum,
          pageSize: pageSize
        }
      }
    };
  }

  buildFilterMetadata(filters: any) {
    var filterMetadata = {};
    if (filters) {
      for (let fltrId in filters) {
        filterMetadata[fltrId] = {
          field: fltrId + ".id",
          dataType: "STRING",
          operator: "IN"
        }
      }
    }
    return filterMetadata;
  }

  typeaheadSearchBizContent(text: string, sbData: SearchBarData, pageNum: number = 0, pageSize: number = 5) {
    let apiUrl = this.apiUrlService.postTypeaheadBizContentSearch();
    var input = this.buildTextSearchInput(sbData, text, pageNum, pageSize);
    return this.http.post(apiUrl, input); 
  }

}
