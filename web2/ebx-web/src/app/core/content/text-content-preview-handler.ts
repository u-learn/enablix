import { ContentPreviewHandler } from './content-preview-handler';

export class TextContentPreviewHandler implements ContentPreviewHandler {
  
  smallThumbnailUrl(dataRecord: any) : string {
    return "";
  }

  largeThumbnailUrl(dataRecord: any) : string {
    return "";
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__textContent;
  }

  type() : string {
    return 'text';
  }

}