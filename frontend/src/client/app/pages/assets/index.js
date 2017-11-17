import angular from 'angular';
import AssetsController from './assets.controller';
import RelevanceDropdown from './directives/relevance-dropdown/relevance-dropdown.js';
import routes from './config.js';

export default angular.module('Assets', [])
  .config(routes)
  .controller('AssetsController', AssetsController)
  .directive('relevanceDropdown', () => new RelevanceDropdown())
  .name;
