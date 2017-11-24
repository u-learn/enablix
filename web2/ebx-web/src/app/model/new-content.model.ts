import { Container } from './container.model';
import { UrlCaptureInfo, FileUploadInfo, TextInfo } from './upload-info.model';

export class NewContent {
  data: any;
  container: Container;
  urlInfo?: UrlCaptureInfo;
  fileInfo?: FileUploadInfo;
  textInfo?: TextInfo;
}