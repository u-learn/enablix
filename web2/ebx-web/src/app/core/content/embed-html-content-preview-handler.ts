import { ContentPreviewHandler } from './content-preview-handler';
import { ContentDocMetadataResolver, PreviewDataBasedHandler } from './preview-data-based-handler';
import { UserPreferenceService } from '../user-preference.service';
import { NoPreviewHandler } from './no-preview-handler';

export class EmbedHtmlContentPreviewHandler implements ContentPreviewHandler {
  
  previewDataBasedHandler: PreviewDataBasedHandler = 
    new PreviewDataBasedHandler(new ContentDocMetadataResolver());
  noPreviewHandler: NoPreviewHandler = new NoPreviewHandler();

  constructor(private userPref: UserPreferenceService) {
  }

  smallThumbnailUrl(dataRecord: any) : string {
    return this.previewDataBasedHandler.canHandle(dataRecord) ? 
            this.previewDataBasedHandler.smallThumbnailUrl(dataRecord) : 
            this.noPreviewHandler.smallThumbnailUrl(dataRecord);
  }

  public largeThumbnailUrl(dataRecord: any) : string {
    return this.previewDataBasedHandler.canHandle(dataRecord) ? 
            this.previewDataBasedHandler.largeThumbnailUrl(dataRecord) : 
            this.noPreviewHandler.largeThumbnailUrl(dataRecord);
  }

  canHandle(dataRecord: any) : boolean {
    
    var embedHtmlViewEnabled = false;
    
    var viewSetting = this.userPref.getPrefByKey('asset.view.settings');
    if (viewSetting && viewSetting.config) {
      embedHtmlViewEnabled = viewSetting.config.enablePortalEmbedHtmlView;
    } 
    
    return embedHtmlViewEnabled && dataRecord.__decoration 
            && dataRecord.__decoration.__docMetadata 
            && dataRecord.__decoration.__docMetadata.embedHtml;
  }

  type() : string {
    return 'embed-html';
  }

}