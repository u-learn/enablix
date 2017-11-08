import angular from 'angular';

import HomeController from './home.controller';
import NavBar from './directives/nav-bar/nav-bar';
import UploadAssetPopup from './directives/upload-asset-popup/upload-asset-popup';
import Cards from './directives/cards/cards';
import DimensionCards from './directives/dimension-cards/dimension-cards';
import SimpleSidebar from './directives/simple-sidebar/simple-sidebar';
import Footer from './directives/footer/footer';
import SearchBar from './directives/search-bar/search-bar';

import routes from './config.js';

export default angular.module('Home', [])
  .config(routes)
  .controller('HomeController', HomeController)
  .directive('navBar', () => new NavBar())
  .directive('uploadAssetPopup', ($state) => new UploadAssetPopup($state))
  .directive('cards', ($state) => new Cards($state))
  .directive('dimensionCards', ($state) => new DimensionCards($state))
  .directive('simpleSidebar', ($state) => new SimpleSidebar())
  .directive('footer', ($state) => new Footer())
  .directive('searchBar', ($state) => new SearchBar())
  .name;