import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ContentPreviewHandler } from './content-preview-handler';
import { ImagePreviewHandler } from './image-preview-handler';
import { UrlPreviewHandler } from './url-preview-handler';
import { PreviewDataBasedHandler } from './preview-data-based-handler';
import { TextContentPreviewHandler } from './text-content-preview-handler';
import { ApiUrlService } from '../api-url.service';

@Injectable()
export class ContentPreviewService {

  private previewHandlers: ContentPreviewHandler[];

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { 
    this.previewHandlers = [];
    this.previewHandlers.push(new ImagePreviewHandler());
    this.previewHandlers.push(new PreviewDataBasedHandler());
    this.previewHandlers.push(new UrlPreviewHandler());
    this.previewHandlers.push(new TextContentPreviewHandler());
  }

  getPreviewHandler(dataRecord: any) : ContentPreviewHandler {
    
    for (let i = 0; i < this.previewHandlers.length; i++) {
    
      let handler = this.previewHandlers[i];
      
      if (handler.canHandle(dataRecord)) {
        return handler;
      }
    }

    return null;
  }

  getFilePreviewData(docIdentity: string) : Observable<any> {
    let apiUrl = this.apiUrlService.getDocPreviewDataUrl(docIdentity);
    return this.http.get(apiUrl);
  }

}
