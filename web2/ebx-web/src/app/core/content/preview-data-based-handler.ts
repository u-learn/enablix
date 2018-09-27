import { environment } from '../../../environments/environment';

import { ContentPreviewHandler } from './content-preview-handler';

export class PreviewDataBasedHandler implements ContentPreviewHandler {


  constructor(private docResolver: DocMetadataResolver) { }

  public smallThumbnailUrl(dataRecord: any) : string {
    let docMd = this.docResolver.getDocMetadata(dataRecord);
    return environment.baseAPIUrl + "/doc/sthmbnl/" + docMd.identity + "/";
  }

  public largeThumbnailUrl(dataRecord: any) : string {
    let docMd = this.docResolver.getDocMetadata(dataRecord);
    return environment.baseAPIUrl + "/doc/lthmbnl/" + docMd.identity + "/";
  }

  public canHandle(dataRecord: any) : boolean {
    let docMd = this.docResolver.getDocMetadata(dataRecord);
    return docMd && docMd.previewStatus == 'AVAILABLE';
  }

  type() : string {
    return 'file-images';
  }
}

export interface DocMetadataResolver {

  getDocMetadata(dataRecord: any) : any;

}

export class ContentDocMetadataResolver implements DocMetadataResolver {
  getDocMetadata(dataRecord: any) : any {
    return dataRecord.__decoration ? dataRecord.__decoration.__docMetadata : null;
  }
}

export class ThumbnailDocMetadataResolver implements DocMetadataResolver {
  getDocMetadata(dataRecord: any) : any {
    return dataRecord.__thumbnailDoc;
  }
}