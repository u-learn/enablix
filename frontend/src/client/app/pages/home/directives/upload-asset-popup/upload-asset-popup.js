import UploadAssetPopupTemplate from './upload-asset-popup.template.html';

  /* @ngInject*/
export default class UploadAssetPopup {
  constructor () {
    this.template = UploadAssetPopupTemplate;
    this.restrict = 'E';
    this.scope = {
        assetType: '@'
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) { 
 
  }
}
