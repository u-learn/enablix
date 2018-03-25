import { environment } from '../../../environments/environment';

import { ContentPreviewHandler } from './content-preview-handler';

export class PreviewDataBasedHandler implements ContentPreviewHandler {

  smallThumbnailUrl(dataRecord: any) : string {
    return environment.baseAPIUrl + "/doc/sthmbnl/" + dataRecord.__decoration.__docMetadata.identity + "/";
  }

  largeThumbnailUrl(dataRecord: any) : string {
    return environment.baseAPIUrl + "/doc/lthmbnl/" + dataRecord.__decoration.__docMetadata.identity + "/";
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__docMetadata 
            && dataRecord.__decoration.__docMetadata.previewStatus == 'AVAILABLE';
  }

  type() : string {
    return 'file-images';
  }
}