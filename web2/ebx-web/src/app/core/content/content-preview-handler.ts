export interface ContentPreviewHandler {
  
  smallThumbnailUrl(dataRecord: any) : string;

  largeThumbnailUrl(dataRecord: any) : string;

  canHandle(dataRecord: any) : boolean;

  type(): string;
}