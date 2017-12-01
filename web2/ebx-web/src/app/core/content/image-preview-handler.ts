import { environment } from '../../../environments/environment';

import { ContentPreviewHandler } from './content-preview-handler';

export class ImagePreviewHandler implements ContentPreviewHandler {
  
  smallThumbnailUrl(dataRecord: any) : string {
    return environment.baseAPIUrl + "/doc/preview/" + dataRecord.__decoration.__docMetadata.identity;
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__docMetadata 
            && dataRecord.__decoration.__docMetadata.contentType
            && dataRecord.__decoration.__docMetadata.contentType.indexOf("image") >= 0;
  }

  type() : string {
    return 'image';
  }

  getImageUrl(dataRecord: any) {
    return environment.baseAPIUrl + "/doc/preview/" + dataRecord.__decoration.__docMetadata.identity + "?atChannel=WEB";
  }

}