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
import { Pagination, Direction, SortCriteria } from '../../model/pagination.model';
import { DataPage } from '../../model/data-page.model';
import { DataType, ConditionOperator } from '../../core/data-search/filter-metadata.model';
import { Constants } from '../../util/constants';



@Injectable()
export class ContentService {

  @Output() onContentUpdate = new EventEmitter<ContentUpdateEvent>();
  @Output() onContentDelete = new EventEmitter<ContentDeleteEvent>();

  constructor(private contentTemplateService: ContentTemplateService, 
              private contentPreviewService: ContentPreviewService,
              private http: HttpClient, private dsService: DataSearchService,
              private apiUrlService: ApiUrlService) { }

  getContentRecord(containerQId: string, contentIdentity: string) : Observable<any> {
    let templateId = this.contentTemplateService.contentTemplate.id;
    let apiUrl = this.apiUrlService.getContentRecordUrl(templateId, containerQId, contentIdentity);
    return this.http.get(apiUrl);
  }

  decorateRecord(container: Container, dataRecord: any) {
        
    if (dataRecord && dataRecord.__associations && dataRecord.__associations.parent) {
      dataRecord.parentIdentity = dataRecord.__associations.parent.recordIdentity;
    }
    
    let decoration: any = dataRecord.__decoration = {};

    decoration.containerQId = container.qualifiedId;
    decoration.__linkedContentCount = 0;
    
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
        if (dataRecord[item.id + "_rt"]) {
          dataRecord[item.id + "_rt"] = dataRecord[item.id];
        }
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
        decoration.__thumbnailUrl = dataRecord.__urls[0].previewImageUrl;
      }
    }

    if (!decoration.__thumbnailUrl && decoration.__docMetadata) {
      let filename = decoration.__docMetadata.name;
      let fileExt = this.getFileExtension(filename);
      if (fileExt) {
        decoration.__noPreviewImgUrl = "/assets/images/icons/file_" + fileExt + ".svg";
      }
    }

    if (container.textItemId) {
      decoration.__textContent = dataRecord[container.textItemId];
      if (!decoration.__textContent) {
        decoration.__thumbnailUrl = "/assets/images/icons/text-icon.png";
      }
    }
  }

  getFileExtension(filename: string) : string {
    var a = filename.split(".");
    if (a.length === 1 || ( a[0] === "" && a.length === 2 )) {
        return null;
    }
    return a.pop().toLowerCase();
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

  getRecordAndChildData(contentQId: string, contentIdentity: string, childSizeLimit: string): Observable<any> {
         
    var options = {};
    if (childSizeLimit) {
      options = {
        params : {size: childSizeLimit}
      }
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