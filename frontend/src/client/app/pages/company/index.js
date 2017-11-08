import angular from 'angular';
import CompanyController from './company.controller';

import CompanyNavBar from './directives/company-navbar/company-navbar';
import RecentActivity from './directives/recent-activity/recent-activity';
import ContentTypes from './directives/content-types/content-types';
import CompanyProperties from './directives/company-properties/company-properties';

import routes from './config.js';

export default angular.module('Company', [])
  .config(routes)
  .controller('CompanyController', CompanyController)
  .directive('recentActivity', () => new RecentActivity())
  .directive('contentTypes', () => new ContentTypes())
  .directive('companyNavbar', () => new CompanyNavBar())
  .directive('companyProperties', () => new CompanyProperties())
  .name;
