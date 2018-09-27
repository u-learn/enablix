export interface ContentThumbnailProvider {

  smallThumbnailUrl(dataRecord: any) : string;

  largeThumbnailUrl(dataRecord: any) : string;

  canHandle(dataRecord: any) : boolean;

}

export interface ContentPreviewHandler extends ContentThumbnailProvider {
  type(): string;
}