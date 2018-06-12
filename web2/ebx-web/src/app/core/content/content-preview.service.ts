import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ContentPreviewHandler } from './content-preview-handler';
import { ImagePreviewHandler } from './image-preview-handler';
import { EmbedHtmlContentPreviewHandler } from './embed-html-content-preview-handler';
import { UrlPreviewHandler } from './url-preview-handler';
import { PreviewDataBasedHandler } from './preview-data-based-handler';
import { TextContentPreviewHandler } from './text-content-preview-handler';
import { ContentStackPreviewHandler } from './content-stack-preview-handler';
import { ApiUrlService } from '../api-url.service';

@Injectable()
export class ContentPreviewService {

  private previewHandlers: ContentPreviewHandler[];

  constructor(private http: HttpClient, private apiUrlService: ApiUrlService) { 
    this.previewHandlers = [];
    this.previewHandlers.push(new ImagePreviewHandler());
    this.previewHandlers.push(new EmbedHtmlContentPreviewHandler());
    this.previewHandlers.push(new PreviewDataBasedHandler());
    this.previewHandlers.push(new UrlPreviewHandler());
    this.previewHandlers.push(new ContentStackPreviewHandler());
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

  getFilePreviewData(docIdentity: string) : Observable<any> {
    let apiUrl = this.apiUrlService.getDocPreviewDataUrl(docIdentity);
    return this.http.get(apiUrl);
  }

}

class NoPreviewHandler implements ContentPreviewHandler {

  supportedExts: string[] = [
    "doc", "docx", "gdoc", "gsheet", "gslides", "pdf", "pps", 
    "ppt", "pptx", "rtf", "txt", "xls", "xlsx", "xml"
  ];

  getNoPreviewImgUrl(docMetadata: any) : string {
    let imgUrl = docMetadata.name ? "/assets/images/icons/file.svg" :
                  "/assets/images/icons/file_unknown.svg";
    let filename = docMetadata.name;
    let fileExt = this.getFileExtension(filename);
    if (fileExt && this.supportedExts.includes(fileExt)) {
      imgUrl = "/assets/images/icons/file_" + fileExt + ".svg";
    }
    return imgUrl;
  }

  getFileExtension(filename: string) : string {
    var a = filename.split(".");
    if (a.length === 1 || ( a[0] === "" && a.length === 2 )) {
        return null;
    }
    return a.pop().toLowerCase();
  }

  smallThumbnailUrl(dataRecord: any) : string {
    if (dataRecord.__decoration && dataRecord.__decoration.__docMetadata) {
      return this.getNoPreviewImgUrl(dataRecord.__decoration.__docMetadata);
    }
    return null;
  }

  largeThumbnailUrl(dataRecord: any) : string {
    return this.smallThumbnailUrl(dataRecord);
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__docMetadata;
  }

  type() : string {
    return 'file-default';
  }
}
