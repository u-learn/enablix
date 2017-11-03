import angular from 'angular';
import uirouter from 'angular-ui-router';
import * as _ from 'lodash';
import './styles/main.scss';

import Home from './pages/home';
import Company from './pages/company';
import Assets from './pages/assets';

angular
  .module('app', [uirouter, Company, Assets, Home])
  .config(($urlRouterProvider, $locationProvider, $stateProvider) => {
    $locationProvider.html5Mode(true);
    $urlRouterProvider.otherwise('/home');
  })
  .factory('_', ['$window', ($window) => {
    return $window._;
  }])
  .run(['$rootScope', ($root) => {
    $root.$on('$stateChangeStart', (e, newUrl, oldUrl) => {
      if (newUrl !== oldUrl) {
        $root.loadingView = true;
      }
    });
    $root.$on('$stateChangeSuccess', () => {
      $root.loadingView = false;
    });
    $root.$on('$stateChangeError', () => {
      $root.loadingView = false;
    });
  }
]
);