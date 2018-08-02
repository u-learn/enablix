export class UploadInfo {
  title: string;
  containerQId: string;
}

export class UrlCaptureInfo extends UploadInfo {
  url: string;
  thumbnailUrl: string;
}

export class FileUploadInfo extends UploadInfo {
  docMetadata: any;
  thumbnailUrl: string;
}

export class TextInfo extends UploadInfo {
  text: string;
}

export class AssetInfo extends UploadInfo {
  
}