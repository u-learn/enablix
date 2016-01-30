var enablix = enablix || {};
enablix.studioApp = angular.module("studio", ['ui.router', 'angularTreeview', 'listGroupTreeview', 
           'angularFileUpload', 'ui.bootstrap', 'isteven-multi-select', 'ui-notification', 'enablixFilters',
           'ngSanitize', 'ui.select','ngMessages']);

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
		.state('portal.container.body', { // state not used?
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
			url: '/enclosure/{enclosureId}/{childContainerQId}',
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
		.state('portal.enclosure.body', { // state not used?
			url: '{type}/{subContainerQId}/',
			templateUrl: function($stateParams) {
				return 'views/portal/container/body-' + $stateParams.type + '.html';
			},
			controller: 'PortalCntnrBodyCtrl',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system', {
			url: '/system',
			templateUrl: 'views/system/system-admin.html',
			controller: 'SystemAdminController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.users', {
			url: '/users',
			templateUrl: 'views/user/userdata.html',
			controller: 'UserController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.users.list', {
			url: '/list',
			templateUrl: 'views/user/userlist.html',
			controller: 'UserController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.users.add', {
			url: '/add',
			templateUrl: 'views/user/adduser.html',
			controller: 'SaveUserController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.users.edit', {
			url: '/edit/{identity}/',
			templateUrl: 'views/user/adduser.html',
			controller: 'SaveUserController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.docstore', {
			url: '/docstore',
			templateUrl: 'views/system/docstore/doc-store-config.html',
			controller: 'DocStoreConfigController'
		})
		.state('setpassword', {
			url: '/setpassword',
			templateUrl: 'views/set-password.html',
			controller: 'SetPasswordController'
			
		});
	
	// The custom “X-Requested-With” is a conventional header sent by browser clients, 
	// and it used to be the default in Angular but they took it out in 1.3.0. 
	// Spring Security responds to it by not sending a “WWW-Authenticate” header in a 
	// 401 response, and thus the browser will not pop up an authentication dialog 
	// (which is desirable in our app since we want to control the authentication).
	
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	$httpProvider.defaults.withCredentials = true;
	
});

