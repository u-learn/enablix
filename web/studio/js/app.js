var enablix = enablix || {};
enablix.studioApp = angular.module("studio", ['ui.router', 'angularTreeview', 'listGroupTreeview', 
           'angularFileUpload', 'ui.bootstrap', 'isteven-multi-select', 'ui-notification', 'enablixFilters',
           'ngSanitize', 'ui.select']);

//enablix.templateId = "entSoftwareTemplate"; //"amlSalesTemplate";
enablix.dateFormat = 'MM/dd/yyyy';
enablix.errorMsgShowTime = 10000; // in milli-seconds 

var appSetup = function(StudioSetupService) {
	return StudioSetupService.setupStudio();
};

enablix.studioApp.config(function($stateProvider, $urlRouterProvider, $httpProvider) {
	
	//$urlRouterProvider.otherwise("/login");
	
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
		.state('authorizationError', {
			url: '/authError',
			templateUrl: 'views/authError.html'
		})
		.state('studio', {
			url: '/studio',
			templateUrl: 'views/studio/studio.html',
			controller: 'MainStudioCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_STUDIO')
				}]
			}
		})
		.state('studio.list', {
			url: '/list/{containerQId}/{parentIdentity}/',
			templateUrl: 'views/content/content-list.html',
			controller: 'ContentListCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_STUDIO')
				}]
			}
		})
		.state('studio.add', {
			url: '/add/{containerQId}/{parentIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentAddCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_STUDIO')
				}] 
			}
		})
		.state('studio.detail', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-detail.html',
			controller: 'ContentDetailCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_STUDIO')
				}] 
			}
		})
		.state('studio.edit', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentEditCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_STUDIO')
				}] 
			}
		})
		.state('refdata', {
			url: '/refdata',
			templateUrl: 'views/refdata/refdata.html',
			controller: 'MainStudioCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_REF_DATA')
				}] 
			}
		})
		.state('refdata.list', {
			url: '/list/{containerQId}/',
			templateUrl: 'views/content/content-list.html',
			controller: 'ContentListCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_REF_DATA')
				}]  
			}
		})
		.state('refdata.add', {
			url: '/add/{containerQId}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentAddCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_REF_DATA')
				}]  
			}
		})
		.state('refdata.detail', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-detail.html',
			controller: 'ContentDetailCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_REF_DATA')
				}]  
			}
		})
		.state('refdata.edit', {
			url: '/detail/{containerQId}/{elementIdentity}/',
			templateUrl: 'views/content/content-add.html',
			controller: 'ContentEditCtrl',
			resolve: {
				setupData: appSetup,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('VIEW_REF_DATA')
				}]  
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
		.state('portal.search', {
			url: '/search/t/{searchText}/',
			templateUrl: 'views/portal/portal-search.html',
			controller: 'PortalSearchCtrl',
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
			url: '/c/{type}/{subContainerQId}/',
			templateUrl: function($stateParams) {
				return 'views/portal/container/body-' + $stateParams.type + '.html';
			},
			controller: 'PortalCntnrBodyCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('portal.subItem', {
			url: '/subitem/{containerQId}/{elementIdentity}/i/{subContainerQId}/{subItemIdentity}/',
			views: {
				// the main template
				'': {
					templateUrl: 'views/portal/portal-subItem.html',
					controller: 'PortalCntnrSubItemCtrl',
					resolve: {
						setupData: appSetup 
					}
				},
				
				// right section template
				'rightSection@portal.subItem' : {
					templateUrl: 'views/portal/container/recommended-section.html',
					controller: 'PortalCntnrRecommendedCtrl',
					resolve: {
						setupData: appSetup 
					}
				}
			}
		})
		.state('portal.enclosure', {
			url: '/enclosure/{enclosureId}/',
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
				'rightSection@portal.enclosure' : {
					templateUrl: 'views/portal/container/recommended-section.html',
					controller: 'PortalEnclRecommendedCtrl',
					resolve: {
						setupData: appSetup 
					}
				}
			}
		})
		.state('portal.enclosure.body', {
			url: '{type}/{subContainerQId}/',
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

