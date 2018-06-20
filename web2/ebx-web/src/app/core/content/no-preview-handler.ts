import { ContentPreviewHandler } from './content-preview-handler';

export class NoPreviewHandler implements ContentPreviewHandler {

  supportedExts: string[] = [
    "doc", "docx", "gdoc", "gsheet", "gslides", "pdf", "pps", 
    "ppt", "pptx", "rtf", "txt", "xls", "xlsx", "xml"
  ];

  getNoPreviewImgUrl(docMetadata: any) : string {
    let imgUrl = docMetadata.name ? "/assets/images/icons/file.svg" :
                  "/assets/images/icons/file_unknown.svg";
    let filename = docMetadata.name;
    let fileExt = this.getFileExtension(filename);
    if (fileExt && this.supportedExts.includes(fileExt)) {
      imgUrl = "/assets/images/icons/file_" + fileExt + ".svg";
    }
    return imgUrl;
  }

  getFileExtension(filename: string) : string {
    var a = filename.split(".");
    if (a.length === 1 || ( a[0] === "" && a.length === 2 )) {
        return null;
    }
    return a.pop().toLowerCase();
  }

  smallThumbnailUrl(dataRecord: any) : string {
    if (dataRecord.__decoration && dataRecord.__decoration.__docMetadata) {
      return this.getNoPreviewImgUrl(dataRecord.__decoration.__docMetadata);
    }
    return null;
  }

  largeThumbnailUrl(dataRecord: any) : string {
    return this.smallThumbnailUrl(dataRecord);
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration && dataRecord.__decoration.__docMetadata;
  }

  type() : string {
    return 'file-default';
  }
}
