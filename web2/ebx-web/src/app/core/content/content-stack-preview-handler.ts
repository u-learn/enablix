import { environment } from '../../../environments/environment';
import { ContentPreviewHandler } from './content-preview-handler';

export class ContentStackPreviewHandler implements ContentPreviewHandler {

  kitImage = "/assets/images/icons/kit.svg";

  constructor() { }

  smallThumbnailUrl(dataRecord: any) : string {
    return this.kitImage;
  }

  largeThumbnailUrl(dataRecord: any) : string {
    return this.smallThumbnailUrl(dataRecord);
  }

  canHandle(dataRecord: any) : boolean {
    return dataRecord.__decoration &&
            (dataRecord.__decoration.__hasContentStackItem ||
              dataRecord.__decoration.__hasLinkedBizContent);
  }

  type() : string {
    return 'content-stack';
  }

  getImageUrl(dataRecord: any) {
    return this.kitImage;
  }

}
