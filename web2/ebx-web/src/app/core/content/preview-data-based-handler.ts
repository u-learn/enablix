import { environment } from '../../../environments/environment';

import { ContentPreviewHandler } from './content-preview-handler';

export class PreviewDataBasedHandler implements ContentPreviewHandler {

  public smallThumbnailUrl(dataRecord: any) : string {
    return environment.baseAPIUrl + "/doc/sthmbnl/" + dataRecord.__decoration.__docMetadata.identity + "/";
  }

  public largeThumbnailUrl(dataRecord: any) : string {
    return environment.baseAPIUrl + "/doc/lthmbnl/" + dataRecord.__decoration.__docMetadata.identity + "/";
  }

  public canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__docMetadata 
            && dataRecord.__decoration.__docMetadata.previewStatus == 'AVAILABLE';
  }

  type() : string {
    return 'file-images';
  }
}