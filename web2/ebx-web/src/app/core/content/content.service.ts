import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Container } from '../../model/container.model';
import { ContentPreviewService } from './content-preview.service';
import { ContentTemplateService } from '../content-template.service';
import { ApiUrlService } from '../api-url.service';
import { ContentRecordGroup } from '../../model/content-record-group.model';


@Injectable()
export class ContentService {

  constructor(private contentTemplateService: ContentTemplateService, 
              private contentPreviewService: ContentPreviewService,
              private http: HttpClient, 
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

    if (container.textItemId) {
      decoration.__textContent = dataRecord[container.textItemId];
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

    return this.http.post(apiUrl, data, {headers: {atActivityOrigin: 'Portal'}});
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

     return this.http.post(apiUrl, {});
   }

}
