/*@ngInject*/
export default ($stateProvider) => {
    $stateProvider
        .state('home', {
            url: '/home',
            template: require('./home.template.html'),
            controller: 'HomeController',
            controllerAs: 'ctrl'
        })
        .state('home.dashboard', {
            url: '/dashboard',
            template: require('./views/dashboard-1/dashboard-1.html'),
            controller: require('./views/dashboard-1/dashboard-1.js'),
            controllerAs: 'ctrl'
        })
        .state('home.dimension', {
            url: '/dimension',
            template: require('./views/dashboard-dimension/dashboard-dimension.html'),
            controller: require('./views/dashboard-dimension/dashboard-dimension.js'),
            controllerAs: 'ctrl'
        })
        .state('home.salesFunnel', {
            url: '/sales-funnel',
            template: require('./views/dashboard-sales-funnel/dashboard-sales-funnel.html'),
            controller: require('./views/dashboard-sales-funnel/dashboard-sales-funnel.js'),
            controllerAs: 'ctrl'
        })
        .state('home.salesFunnelList', {
            url: '/sales-funnel-list',
            template: require('./views/dashboard-sales-funnel/dashboard-sales-funnel-list-view.html'),
            controller: require('./views/dashboard-sales-funnel/dashboard-sales-funnel-list-view.js'),
            controllerAs: 'ctrl'
        })
        .state('home.freeResults', {
            url: '/free-results',
            template: require('./views/dashboard-search-results/dashboard-search-results.html'),
            controller: require('./views/dashboard-search-results/dashboard-search-results.js'),
            controllerAs: 'ctrl'
        })
};