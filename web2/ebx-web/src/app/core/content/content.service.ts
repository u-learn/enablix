import { Injectable, EventEmitter, Output } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Container } from '../../model/container.model';
import { ContentPreviewService } from './content-preview.service';
import { ContentTemplateService } from '../content-template.service';
import { ApiUrlService } from '../api-url.service';
import { ContentRecordGroup } from '../../model/content-record-group.model';
import { DataSearchService } from '../../core/data-search/data-search.service';
import { DataSearchRequest } from '../../core/data-search/data-search-request.model';
import { Pagination, Direction, SortCriteria } from '../../core/model/pagination.model';
import { DataPage } from '../../model/data-page.model';
import { DataType, ConditionOperator } from '../../core/data-search/filter-metadata.model';
import { Constants } from '../../util/constants';
import { ImportRequest } from '../bulk-import/bulk-import.model';
import { UserPreferenceService } from '../user-preference.service';


@Injectable()
export class ContentService {

  @Output() onContentUpdate = new EventEmitter<ContentUpdateEvent>();
  @Output() onContentDelete = new EventEmitter<ContentDeleteEvent>();

  constructor(private contentTemplateService: ContentTemplateService, 
              private contentPreviewService: ContentPreviewService,
              private http: HttpClient, private dsService: DataSearchService,
              private apiUrlService: ApiUrlService,
              private userPrefs: UserPreferenceService) { }

  getContentRecord(containerQId: string, contentIdentity: string, atChannel?: string, atCtxParams?: any) : Observable<any> {
    let templateId = this.contentTemplateService.contentTemplate.id;
    let apiUrl = this.apiUrlService.getContentRecordUrl(templateId, containerQId, contentIdentity);
    
    let options: any = {};
    options.headers = atCtxParams || {};
    if (atChannel && !options.headers.atChannel) {
      options.headers.atChannel = atChannel;
    }
    
    return this.http.get(apiUrl, options)
  }

  getRecordContentStack(containerQId: string, contentIdentity: string) : Observable<any> {
    let apiUrl = this.apiUrlService.getRecordContentStackUrl(containerQId, contentIdentity);
    return this.http.get(apiUrl);
  }

  getContentStackDetails(stackValue: any) : Observable<any> {
    
    let apiUrl = this.apiUrlService.postFetchContentStackDetails();
    
    let params = {
      page: '0',
      size: '50',
      sortProp: Constants.FLD_MODIFIED_AT,
      sortDir: Direction.DESC
    };

    let options = { params: params };
    return this.http.post(apiUrl, stackValue, options);
  }

  decorateContentGroup(cg: any) {
    
    for (var i = 0; i < cg.length; i++) {

      let contentGrp = cg[i];

      let container = this.contentTemplateService.getContainerByQId(contentGrp.contentQId);
      if (container.linkContainerQId) {
        contentGrp.linkContainer = container;
        container = this.contentTemplateService.getContainerByQId(container.linkContainerQId);
      }

      contentGrp.container = container;
      contentGrp.records.content.forEach(rec => {
        this.decorateRecord(container, rec);
      });
    }
  }

  decorateRecord(container: Container, dataRecord: any) {
        
    if (dataRecord && dataRecord.__associations && dataRecord.__associations.parent) {
      dataRecord.parentIdentity = dataRecord.__associations.parent.recordIdentity;
    }
    
    let decoration: any = dataRecord.__decoration = {};

    decoration.containerQId = container.qualifiedId;
    decoration.__linkedContentCount = 0;
    if (container.linkedBizContent) {
      decoration.__hasLinkedBizContent = true;
    }
    
    for (let i = 0; i < container.contentItem.length; i++) {
      
      let item = container.contentItem[i];
      
      if (item.type == 'DOC') {
        
        // set doc ref with qualified property
        let docInstance = dataRecord[item.id];
        if (docInstance && docInstance.identity) {
          decoration.__docMetadata = docInstance;
        }
        
      } else if (item.type == 'BOUNDED') {
        
        this.addLinkedContent(dataRecord, item.id);
        
      } else if (item.type == 'RICH_TEXT') {
        
        // if rich text item but rich text version of field does not exist, 
        // then set it equal to the plain text value
        if (!dataRecord[item.id + "_rt"]) {
          dataRecord[item.id + "_rt"] = dataRecord[item.id];
        }

      } else if (item.type == "CONTENT_STACK") {
        decoration.__contentStack = dataRecord[item.id];
        decoration.__hasContentStackItem = true;
      }
    }

    // set linked items for linked containers as well
    if (container.container) {
      for (let i = 0; i < container.container.length; i++) {
        let childContainer = container.container[i];
        if (childContainer.linkContainerQId) { // if it is linked container
          this.addLinkedContent(dataRecord, container.container[i].id);
        }
      }
    }
    
    // find the preview handler and set thumbnail url for the record
    let previewHandler = this.contentPreviewService.getPreviewHandler(dataRecord);
    if (previewHandler != null) {
      decoration.__thumbnailUrl = previewHandler.smallThumbnailUrl(dataRecord);
    }

    if (!decoration.__thumbnailUrl) {
      if (dataRecord.__urls && dataRecord.__urls.length > 0) {
        var embedUrl = dataRecord.__urls[0];
        decoration.__thumbnailUrl = embedUrl.type !== "unknown" ? embedUrl.previewImageUrl : 
                  "/assets/images/icons/url-icon.svg";
      }
    }

    if (!decoration.__thumbnailUrl && container.textItemId) {
      decoration.__textContent = dataRecord[container.textItemId];
      if (!decoration.__textContent) {
        decoration.__thumbnailUrl = "/assets/images/icons/text-icon.png";
      }
    }

    if (container.titleItemId) {
      dataRecord.__title = dataRecord[container.titleItemId];
    }

    // process indicators if any
    this.processIndicators(container, dataRecord);

  }

  processIndicators(container: Container, dataRecord: any) {
        
    var indicatorsConfig = this.userPrefs.getPrefByKey('ui.content.indicators');
    
    if (indicatorsConfig) {
    
      var allIndConfigs = {};
      var globalIndConfig = indicatorsConfig.config["GLOBAL"];
      
      if (globalIndConfig) {
      
        globalIndConfig.forEach((indConfig) => {
          allIndConfigs[indConfig.id] = indConfig;
        });
      }
      
      var contIndConfig = indicatorsConfig.config[container.qualifiedId];
      if (contIndConfig) {
        contIndConfig.forEach((indConfig) => {
          allIndConfigs[indConfig.id] = indConfig;
        });
      }
      
      dataRecord.__decoration.indicators = {};
      var rec = dataRecord; // for indicators expression evaluation
      
      for(let key in allIndConfigs) {
        let indctrConfig = allIndConfigs[key];
        let indctrId =key;
        
        var indctrVal = eval(indctrConfig.value);
        
        var indctrValueList = dataRecord.__decoration.indicators[indctrConfig.type];
        if (!indctrValueList) {
          indctrValueList = [];
          dataRecord.__decoration.indicators[indctrConfig.type] = indctrValueList;
        }
        
        indctrValueList.push({
          value: indctrVal,
          config: indctrConfig
        });
      }
    }
  }

  addLinkedContent(dataRecord: any, linkedContentProp: string) {
    
    let linkedItems = dataRecord[linkedContentProp];

    if (linkedItems && linkedItems.length > 0) {

      // set linked content text if not already set
      if (!dataRecord.__decoration.__linkedContentText) {
        dataRecord.__decoration.__linkedContentText = linkedItems[0].label;
      }

      // add linked item count
      dataRecord.__decoration.__linkedContentCount += linkedItems.length;  
    }

  }

  saveContainerData(containerQId: string, data: any) : Observable<any> {
    
    if (data.__decoration) {
      delete data['__decoration'];
    }

    let apiUrl = null;

    if (data.identity) {
      apiUrl = this.apiUrlService.postUpdateContainerDataUrl(
        this.contentTemplateService.contentTemplate.id, containerQId);
    } else {
      apiUrl = this.apiUrlService.postInsertRootContainerDataUrl(
        this.contentTemplateService.contentTemplate.id, containerQId);
    }

    return this.http.post(apiUrl, data, {headers: {atActivityOrigin: 'Portal'}}).map((res: any) => {
      
      this.onContentUpdate.emit({
        new: data.identity ? false : true,
        record: res.contentRecord,
        containerQId: containerQId
      });

      return res;
    });
  }

  getRecordAndChildData(contentQId: string, contentIdentity: string, 
    childPageNo: string = '0', childSizeLimit?: string, atChannel?: string, 
    childQIds?: any, textQuery?: string, atCtxParams?: any): Observable<any> {
         
    var options: any = {};
    options.params = {
      pageNum: childPageNo,
      sortProp: Constants.FLD_MODIFIED_AT,
      sortDir: Direction.DESC
    };

    if (childSizeLimit) {
      options.params.size = childSizeLimit;
    }

    options.headers = atCtxParams || {}
    if (atChannel && !options.headers.atChannel) {
      options.headers.atChannel = atChannel;
    }

    if (childQIds) {
      options.params['cqid'] = childQIds;
    }

    if (childQIds) {
      options.params['cqid'] = childQIds;
    }

    if (textQuery) {
      options.params['tq'] = textQuery;
    }

    const apiUrl = this.apiUrlService.getRecordAndChildren(contentQId, contentIdentity);
    return this.http.get(apiUrl, options);
  };

  deleteContentRecord(contentQId: string, contentIdentity: string) {
     
    let apiUrl = this.apiUrlService.getContentDeleteUrl(
      this.contentTemplateService.contentTemplate.id, contentQId, contentIdentity);

    return this.http.post(apiUrl, {}).map(res => { 
      
      this.onContentDelete.emit({
        containerQId: contentQId,
        recordIdentity: contentIdentity
      });

      return res; 
    });
  }

  getLinkdedAndMappedContent(linkedContentRequest: any) {
    let apiUrl = this.apiUrlService.postFetchLinkedMappedContent();
    return this.http.post(apiUrl, linkedContentRequest);
  }

  getAllRecords(containerQId: string, pageSize?: number) {

    let searchRequest = new DataSearchRequest();
    
    searchRequest.projectedFields = [];
    searchRequest.filters = {};

    searchRequest.pagination = new Pagination();
    searchRequest.pagination.pageNum = 0;
    searchRequest.pagination.pageSize = pageSize ? pageSize : 500;
    
    searchRequest.pagination.sort = new SortCriteria();
    searchRequest.pagination.sort.field = Constants.FLD_MODIFIED_AT
    searchRequest.pagination.sort.direction = Direction.DESC;

    return this.dsService.getContainerDataSearchResult(containerQId, searchRequest);
  }

  submitImportRequest(request: ImportRequest) {
    let apiUrl = this.apiUrlService.postImportRequestUrl();
    return this.http.post(apiUrl, request);
  }

  archiveRecord(recordIdentity: string, contentQId: string) {
    let apiUrl = this.apiUrlService.postArchiveContentRecord();
    return this.http.post(apiUrl, { 
        recordIdentity: recordIdentity,
        contentQId: contentQId
      });
  }

  unarchiveRecord(recordIdentity: string, contentQId: string) {
    let apiUrl = this.apiUrlService.postUnarchiveContentRecord();
    return this.http.post(apiUrl, {
        recordIdentity: recordIdentity,
        contentQId: contentQId
      });
  }

  getFilteredRecords(containerQId: string, searchRequest: DataSearchRequest) {
    return this.dsService.getContainerDataSearchResult(containerQId, searchRequest); 
  }

  attrHasValue(val: any) : boolean {
    return val && !this.isArrayAndEmpty(val) && !this.isStringAndEmpty(val);
  }

  isArrayAndEmpty(obj: any) {
    return this.isArray(obj) && obj.length == 0;
  }

  isString(obj: any) {
    return typeof obj === 'string';
  }

  isStringAndEmpty(obj: any) {
    return this.isString(obj) && obj.trim().length == 0;
  }

  isArray(obj: any) {
    return obj && obj.constructor === Array;
  }

}

export class ContentUpdateEvent {
  new: boolean;
  record: any;
  containerQId: string;
}

export class ContentDeleteEvent {
  containerQId: string;
  recordIdentity: string;
}
