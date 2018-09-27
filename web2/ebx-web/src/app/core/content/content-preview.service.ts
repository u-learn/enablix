import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ContentThumbnailProvider, ContentPreviewHandler } from './content-preview-handler';
import { ImagePreviewHandler } from './image-preview-handler';
import { EmbedHtmlContentPreviewHandler } from './embed-html-content-preview-handler';
import { UrlPreviewHandler } from './url-preview-handler';
import { ContentDocMetadataResolver, ThumbnailDocMetadataResolver, PreviewDataBasedHandler } from './preview-data-based-handler';
import { TextContentPreviewHandler } from './text-content-preview-handler';
import { ContentStackPreviewHandler } from './content-stack-preview-handler';
import { ApiUrlService } from '../api-url.service';
import { UserPreferenceService } from '../user-preference.service';
import { NoPreviewHandler } from './no-preview-handler';

@Injectable()
export class ContentPreviewService {

  private previewHandlers: ContentPreviewHandler[];
  private thumbnailProviders: ContentThumbnailProvider[];

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService,
              private userPrefs: UserPreferenceService) { 

    this.thumbnailProviders = [];
    this.thumbnailProviders.push(new PreviewDataBasedHandler(new ThumbnailDocMetadataResolver()));

    this.previewHandlers = [];
    this.previewHandlers.push(new ContentStackPreviewHandler());
    this.previewHandlers.push(new ImagePreviewHandler());
    this.previewHandlers.push(new EmbedHtmlContentPreviewHandler(this.userPrefs));
    this.previewHandlers.push(new PreviewDataBasedHandler(new ContentDocMetadataResolver()));
    this.previewHandlers.push(new UrlPreviewHandler());
    this.previewHandlers.push(new TextContentPreviewHandler());
    this.previewHandlers.push(new NoPreviewHandler());
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

  getThumbnailProvider(dataRecord: any) : ContentThumbnailProvider {
    
    for (let i = 0; i < this.thumbnailProviders.length; i++) {
    
      let handler = this.thumbnailProviders[i];
      
      if (handler.canHandle(dataRecord)) {
        return handler;
      }
    }

    return this.getPreviewHandler(dataRecord);
  }

  getFilePreviewData(docIdentity: string) : Observable<any> {
    let apiUrl = this.apiUrlService.getDocPreviewDataUrl(docIdentity);
    return this.http.get(apiUrl);
  }

}

