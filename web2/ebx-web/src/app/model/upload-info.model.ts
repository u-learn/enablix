export class UploadInfo {
  title: string;
  containerQId: string;
}

export class UrlCaptureInfo extends UploadInfo {
  url: string;
}

export class FileUploadInfo extends UploadInfo {
  docMetadata: any;
}

export class TextInfo extends UploadInfo {
  text: string;
}