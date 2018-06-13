import { ContentPreviewHandler } from './content-preview-handler';
import { PreviewDataBasedHandler } from './preview-data-based-handler';
import { UserPreferenceService } from '../user-preference.service';

export class EmbedHtmlContentPreviewHandler extends PreviewDataBasedHandler {
  
  previewDataBasedHandler: PreviewDataBasedHandler = new PreviewDataBasedHandler();

  constructor(private userPref: UserPreferenceService) {
    super();
  }

  smallThumbnailUrl(dataRecord: any) : string {
    return super.canHandle(dataRecord) ? super.smallThumbnailUrl(dataRecord) : "";
  }

  public largeThumbnailUrl(dataRecord: any) : string {
    return super.canHandle(dataRecord) ? super.largeThumbnailUrl(dataRecord) : "";
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