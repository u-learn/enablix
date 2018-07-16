import { Injectable, EventEmitter, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';
 
import { ContentTemplate } from '../model/content-template.model';
import { ApiUrlService } from './api-url.service';
import { ContentTemplateCache } from './content-template-cache';
import { Container } from '../model/container.model';
import { SelectOption } from './select/select.component';
import { AlertService } from './alert/alert.service';
import { FilterMetadata, DataType, ConditionOperator } from './data-search/filter-metadata.model';
import { Utility } from '../util/utility';
import { ContentItem } from '../model/content-item.model';

@Injectable()
export class ContentTemplateService {

  contentTemplate: ContentTemplate;
  templateCache: ContentTemplateCache;

  @Output() onTemplateLoad: EventEmitter<any> = new EventEmitter();

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
              private alert: AlertService) { }

  init(reload: boolean = false): Observable<ContentTemplate> {
    
    console.log("Loading content template...");

    if (this.contentTemplate && !reload) {
      return Observable.of(this.contentTemplate);
    } 

    return this.http.get<ContentTemplate>(this.apiUrlService.getDefaultContentTemplateUrl()).map(
                   (contentTemplate: ContentTemplate) => { 
                     return this.loadTemplate(contentTemplate);
                   },
                   error => {
                     this.alert.error("Error loading content template", error.status);
                   }
                 );
  }

  loadTemplate(contentTemplate: ContentTemplate) {
    this.contentTemplate = contentTemplate; 
    this.templateCache = new ContentTemplateCache(this.contentTemplate);
    this.onTemplateLoad.emit(this.contentTemplate);
    return this.contentTemplate;
  }

  getContainerByQId(containerQId: string) : Container {
    
    let container = this.templateCache.getContainerByQId(containerQId);

    if (!container) {
      // check if parent is a content stack
      let stackItemQId = Utility.getParentQId(containerQId);
      if (stackItemQId) {
        let stackItem = this.templateCache.contentStackQIdMap[stackItemQId];
        if (stackItem) {
          let stackRecordQId = containerQId.substr(stackItemQId.length + 1);
          container = this.templateCache.getContainerByQId(stackRecordQId);
        }
      }
    }
    
    return container;
  }

  getConcreteContainerByQId(containerQId: string) : Container {
    let container = this.getContainerByQId(containerQId);
    if (container && container.linkContainerQId) {
      container = this.getContainerByQId(container.linkContainerQId);
    }
    return container;
  }

  getPortalTopNavItemContainers() : any[] {
    if (this.contentTemplate.portalUIDefinition) {
      return this.contentTemplate.portalUIDefinition.topNavigation.itemContainers.itemContainer;
    }
    return [];
  }

  getPortalDimensionContainers() : Container[] {
    
    let dimContainers: Container[] = [];
    let showCount = 4;
    let dimCount = this.templateCache.bizDimensionContainers.length;
    
    for (var i = 0; i < dimCount && i < showCount; i++) {
      dimContainers.push(this.templateCache.bizDimensionContainers[i]);
    }

    return dimContainers;
  }

  isLinkedContainer(container: Container) : boolean {
    return this.templateCache.isLinkedContainer(container);
  }

  isBusinessDimension(container: Container) : boolean {
    return this.templateCache.isBusinessDimension(container);
  }

  isBusinessContent(container: Container) : boolean {
    return this.templateCache.isBusinessContent(container);
  }

  getConcreteContainers() : Container[] {
    return this.templateCache.concreteContainers;
  }

  getConcreteContainersSelectOptions(): SelectOption[] {
    return this.containerListToSelectOption(this.templateCache.concreteContainers);
  }

  getBizContentSelectOptions(): SelectOption[] {
    return this.containerListToSelectOption(this.templateCache.bizContentContainers);
  }

  private containerListToSelectOption(containers: Container[]): SelectOption[] {
    let options: SelectOption[] = [];
    containers.forEach(container => {
          options.push({id: container.qualifiedId, label: container.singularLabel});
        });
    return options;
  }

  getBoundedValueList(contentItem) : Observable<any> {
        
    if (contentItem.bounded.fixedList) {
      
      let itemList = [];
      
      contentItem.bounded.fixedList.data.forEach(optItem => {
        itemList.push({id: optItem.id, label: optItem.label});
      });
      
      return Observable.of(itemList);
      
    } else if(contentItem.bounded.refList) {
      
      if (contentItem.qualifiedId) {
        
        let apiUrl = this.apiUrlService.getBoundedRefListUrl(
          this.contentTemplate.id, contentItem.qualifiedId);
        
        return this.http.get(apiUrl);

      } else {
        
        let apiUrl = this.apiUrlService.postBoundedDefRefListUrl();

        return this.http.post(apiUrl, contentItem.bounded);
      }
    }
  }

  getContainerLabel(containerQId: string) : string {
    let container = this.getContainerByQId(containerQId);
    return container ? container.label : "";
  }

  getContainerSingularLabel(containerQId: string) : string {
    let container = this.getContainerByQId(containerQId);
    return container ? container.singularLabel : "";
  }

  getBoundedFiltermetadata(container: Container) : { [key: string] : FilterMetadata } {

    let filterMd: { [key: string] : FilterMetadata } = {};
    container.contentItem.forEach(ci => {
          
      if (ci.type === 'BOUNDED') {
        let filterId = ci.id;
        filterMd[filterId] = {
          field: ci.id + ".id",
          dataType: DataType.STRING,
          operator: ConditionOperator.IN
        };
      }
    });

    return filterMd;
  }

  updateContainerDefinition(container: Container) {
    let apiUrl = this.apiUrlService.postUpdateContainerDefUrl(this.contentTemplate.id);
    return this.http.post(apiUrl, container);
  }

  addContainerDefinition(container: Container) {
    let apiUrl = this.apiUrlService.postAddContainerDefUrl(this.contentTemplate.id);
    return this.http.post(apiUrl, container);
  }

  deleteContainerDefinition(containerQId: string) {
    let apiUrl = this.apiUrlService.deleteContainerDefUrl(this.contentTemplate.id, containerQId);
    return this.http.delete(apiUrl);
  }

  updateContainerOrder(containerOrder: any) {
    let apiUrl = this.apiUrlService.postUpdateContainerOrderUrl(this.contentTemplate.id);
    return this.http.post(apiUrl, containerOrder);
  }

  getBizDimBoundedItems() : ContentItem[] {
    
    let boundedItems: ContentItem[] = []; 
    
    this.templateCache.bizDimensionContainers.forEach((dimCont) => {
      
      let lblAttr = this.templateCache.getContainerLabelAttrId(dimCont.qualifiedId);

      boundedItems.push({
        id: dimCont.qualifiedId,
        qualifiedId: null,
        type: "BOUNDED",
        label: dimCont.label,
        bounded: {
          multivalued: true,
          refList: {
            datastore: {
              storeId: dimCont.qualifiedId,
              dataId: "identity",
              dataLabel: lblAttr
            }
          }
        }
      })
    });

    return boundedItems;
  }

}
