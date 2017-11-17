import UploadAssetPopupTemplate from './upload-asset-popup.template.html';
import Data from '../../../../helper/data/common.js';

  /* @ngInject*/
export default class UploadAssetPopup {
  constructor ($state) {
    this.template = UploadAssetPopupTemplate;
    this.restrict = 'E';
    this.scope = {
        assetType: '@',
        state: '='
    };
    this.transclude = true;
  }

  // optional compile function
  compile (tElement) {
    return this.link.bind(this);
  }

  // optional link function
  link (scope, element, attributes) {
    scope.asset = {};
    scope.tagOptions = tagOptions;
    scope.asset.selectedTag = tagOptions[0];

    scope.createAsset = (type) => {
      if (type === 'file') {
        scope.state.go('assets.file-edit');
      } else if (type === 'text') {
        scope.state.go('assets.text-edit');
      } else {
        scope.previewContent = false;
        scope.state.go('assets.url-edit');
      }
    };

    scope.url = {};

    scope.previewContent = false;
    scope.showPreview = () => {
      scope.previewContent = true;
    }    

    scope.$on('upload-prompt-text', () => { 
      scope.showTextPreview = true;
    });
    scope.$on('upload-prompt-file', () => {
      scope.showFilePreview = true;
    });
    scope.$on('upload-prompt-url', () => {
      scope.showUrlPreview = true;
    });

    scope.cancel = (type) => {
      scope[type] = false;
      scope.$emit('hide-overlay-prompt');
    };
  };
}

const tagOptions = Data.contentType;