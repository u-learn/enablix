import { ContentPreviewHandler } from './content-preview-handler';

export class UrlPreviewHandler implements ContentPreviewHandler {
  
  smallThumbnailUrl(dataRecord: any) : string {
    
    if (this.canHandle(dataRecord)) {
      return dataRecord.__urls[0].previewImageUrl;
    }

    return "";
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__urls && dataRecord.__urls.length > 0;
  }

  type() : string {
    return 'url';
  }

}