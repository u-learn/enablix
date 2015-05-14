var enablix = enablix || {};
enablix.studioApp = angular.module("studio", ['ui.router', 'angularTreeview', 'listGroupTreeview', 
           'angularFileUpload', 'ui.bootstrap', 'isteven-multi-select', 'ui-notification']);

//enablix.templateId = "entSoftwareTemplate"; //"amlSalesTemplate";
enablix.dateFormat = 'MM/dd/yyyy';
enablix.errorMsgShowTime = 10000; // in milli-seconds 

enablix.studioApp.filter('ebDate', function($filter) {
    return function(input) {
        if (input == null) { return ""; }
        return $filter('date')(input, enablix.dateFormat);
    };
});

var appSetup = function(StudioSetupService) {
	return StudioSetupService.setupStudio();
};

enablix.studioApp.config(function($stateProvider, $urlRouterProvider, $httpProvider) {
	
	$urlRouterProvider.otherwise("/login");
	
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
			templateUrl: 'views/login.html',
			controller: 'LoginController'
		})
		.state('studio', {
			url: '/studio',
			templateUrl: 'views/studio/studio.html',
			controller: 'MainStudioCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('studio.list', {
			url: '/list/{containerQId}/{parentIdentity}/',
			templateUrl: 'views/content/content-list.html',
			controller: 'ContentListCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('studio.add', {
			url: '/add/{containerQId}/{parentIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentAddCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('studio.detail', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-detail.html',
			controller: 'ContentDetailCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('studio.edit', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentEditCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('refdata', {
			url: '/refdata',
			templateUrl: 'views/refdata/refdata.html',
			controller: 'MainStudioCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('refdata.list', {
			url: '/list/{containerQId}/',
			templateUrl: 'views/content/content-list.html',
			controller: 'ContentListCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('refdata.add', {
			url: '/add/{containerQId}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentAddCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('refdata.detail', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-detail.html',
			controller: 'ContentDetailCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('refdata.edit', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentEditCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('portal', {
			url: '/portal',
			templateUrl: 'views/portal/portal.html',
			controller: 'MainPortalCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('portal.home', {
			url: '/home',
			templateUrl: 'views/portal/portal-home.html',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('portal.container', {
			url: '/container/{containerQId}/{elementIdentity}',
			views: {
				// the main template
				'': {
					templateUrl: 'views/portal/portal-container.html',
					controller: 'PortalCntnrDetailCtrl',
					resolve: {
						setupData: appSetup 
					}
				},
				
				// right section template
				'rightSection@portal.container' : {
					templateUrl: 'views/portal/container/recommended-section.html',
					controller: 'PortalCntnrRecommendedCtrl',
					resolve: {
						setupData: appSetup 
					}
				}
			}
		})
		.state('portal.container.body', {
			url: '/{type}/{subContainerQId}/',
			templateUrl: function($stateParams) {
				return 'views/portal/container/body-' + $stateParams.type + '.html';
			},
			controller: 'PortalCntnrBodyCtrl',
			resolve: {
				setupData: appSetup 
			}
		});
	
	// The custom “X-Requested-With” is a conventional header sent by browser clients, 
	// and it used to be the default in Angular but they took it out in 1.3.0. 
	// Spring Security responds to it by not sending a “WWW-Authenticate” header in a 
	// 401 response, and thus the browser will not pop up an authentication dialog 
	// (which is desirable in our app since we want to control the authentication).
	
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	$httpProvider.defaults.withCredentials = true;
	
});

