var enablix = enablix || {};
enablix.studioApp = angular.module("studio", ['ui.router', 'angularTreeview', 
           'angularFileUpload', 'ui.bootstrap', 'isteven-multi-select']);

enablix.templateId = "entSoftwareTemplate"; //"amlSalesTemplate";
enablix.dateFormat = 'MM/dd/yyyy';

enablix.studioApp.filter('ebDate', function($filter) {
    return function(input) {
        if (input == null) { return ""; }
        return $filter('date')(input, enablix.dateFormat);
    };
});

enablix.studioApp.config(function($stateProvider, $urlRouterProvider) {
	
	$urlRouterProvider.otherwise("/home");
	
	$stateProvider
		.state('home', {
			url: '/home',
			templateUrl: 'views/home.html'
		})
		.state('about', {
			url: '/about',
			templateUrl: 'views/about.html'
		})
		.state('login', {
			url: '/login',
			templateUrl: 'views/login.html'
		})
		.state('studio', {
			url: '/studio',
			templateUrl: 'views/studio/studio.html'
		})
		.state('studio.list', {
			url: '/list/{containerQId}/{parentIdentity}/',
			templateUrl: 'views/content/content-list.html',
			controller: 'ContentListCtrl'
		})
		.state('studio.add', {
			url: '/add/{containerQId}/{parentIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentAddCtrl'
		})
		.state('studio.detail', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-detail.html',
			controller: 'ContentDetailCtrl'
		})
		.state('studio.edit', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentEditCtrl'
		})
		.state('refdata', {
			url: '/refdata',
			templateUrl: 'views/refdata/refdata.html'
		})
		.state('refdata.list', {
			url: '/list/{containerQId}/{parentIdentity}/',
			templateUrl: 'views/content/content-list.html',
			controller: 'ContentListCtrl'
		})
		.state('refdata.add', {
			url: '/add/{containerQId}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentAddCtrl'
		});
});

