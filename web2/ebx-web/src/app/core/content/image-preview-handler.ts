import { environment } from '../../../environments/environment';

import { ContentPreviewHandler } from './content-preview-handler';

export class ImagePreviewHandler implements ContentPreviewHandler {
  
  smallThumbnailUrl(dataRecord: any) : string {
    return environment.baseAPIUrl + "/doc/previewn/" + dataRecord.__decoration.__docMetadata.identity;
  }

  largeThumbnailUrl(dataRecord: any) : string {
    return this.smallThumbnailUrl(dataRecord);
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__docMetadata
            && dataRecord.__decoration.__docMetadata.previewStatus != 'AVAILABLE' 
            && dataRecord.__decoration.__docMetadata.contentType
            && dataRecord.__decoration.__docMetadata.contentType.indexOf("image") >= 0
            && dataRecord.__decoration.__docMetadata.contentType.indexOf("tiff") == -1;
  }

  type() : string {
    return 'image';
  }

  getImageUrl(dataRecord: any) {
    return environment.baseAPIUrl + "/doc/previewn/" + dataRecord.__decoration.__docMetadata.identity;
  }

}