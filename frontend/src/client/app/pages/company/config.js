/*@ngInject*/
export default ($stateProvider) => {
    $stateProvider
      .state('company', {
        url: '/company',
        template: require('./company.template.html'),
        controller: 'CompanyController',
        controllerAs: 'ctrl',
        resolve: {
        }
      })
      .state('company.info', {
        url: '/info',
        template: require('./views/company-information/company-information.html'),
        controller: require('./views/company-information/company-information.js'),
        controllerAs: 'ctrl'
      })
      .state('company.sales', {
        url: '/sales',
        template: require('./views/company-sales/company-sales.html'),
        controller: require('./views/company-sales/company-sales.js'),
        controllerAs: 'ctrl'
      })
      .state('company.insights', {
        url: '/insights'
      })
      .state('company.properties', {
        url: '/properties',
        template: require('./views/company-properties/company-properties.html'),
        controller: require('./views/company-properties/company-properties.js'),
        controllerAs: 'ctrl'
      })
      .state('recent-activity', {
        url: '/recent-activity',
        template: require('./views/recent-activity/recent-activity.html'),
        controller: require('./views/recent-activity/recent-activity.js'),
        controllerAs: 'ctrl'
      })
      .state('company.integrations', {
        url: '/integrations'
      })
      .state('company.members', {
        url: '/members'
      })
      .state('consolidate-content', {
        url: '/consolidate-content',
        template: require('./views/consolidate-content/consolidate-content.html'),
        controller: require('./views/consolidate-content/consolidate-content.js'),
        controllerAs: 'ctrl'
      })
      .state('consolidate-content.drafts', {
        url: '/drafts'
      })
      .state('consolidate-content.quality-check', {
        url: '/quality-check'
      })
      .state('consolidate-content.pending', {
        url: '/pending',
        template: require('./views/consolidate-content/consolidate-content.html'),
        controller: require('./views/consolidate-content/consolidate-content.js'),
        controllerAs: 'ctrl'
      })
      .state('consolidate-content.edit', {
        url: '/edit',
        template: require('./views/consolidate-content/consolidate-content.html'),
        controller: require('./views/consolidate-content/consolidate-content.js'),
        controllerAs: 'ctrl'
      })
};