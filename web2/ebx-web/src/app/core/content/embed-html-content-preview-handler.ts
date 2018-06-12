import { ContentPreviewHandler } from './content-preview-handler';
import { PreviewDataBasedHandler } from './preview-data-based-handler';

export class EmbedHtmlContentPreviewHandler extends PreviewDataBasedHandler {
  
  previewDataBasedHandler: PreviewDataBasedHandler = new PreviewDataBasedHandler();

  smallThumbnailUrl(dataRecord: any) : string {
    return super.canHandle(dataRecord) ? super.smallThumbnailUrl(dataRecord) : "";
  }

  largeThumbnailUrl(dataRecord: any) : string {
    return super.canHandle(dataRecord) ? super.largeThumbnailUrl(dataRecord) : "";
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__docMetadata 
            && dataRecord.__decoration.__docMetadata.embedHtml;
  }

  type() : string {
    return 'embed-html';
  }

}