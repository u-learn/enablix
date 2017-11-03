import angular from 'angular';
import AssetsController from './assets.controller';

import routes from './config.js';

export default angular.module('Assets', [])
  .config(routes)
  .controller('AssetsController', AssetsController)
//   .directive('recentActivity', () => new RecentActivity())
  .name;
