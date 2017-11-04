import angular from 'angular';

import HomeController from './home.controller';
import NavBar from './directives/nav-bar/nav-bar';
import UploadAssetPopup from './directives/upload-asset-popup/upload-asset-popup';

import routes from './config.js';

export default angular.module('Home', [])
  .config(routes)
  .controller('HomeController', HomeController)
  .directive('navBar', () => new NavBar())
  .directive('uploadAssetPopup', ($state) => new UploadAssetPopup($state))
  .name;