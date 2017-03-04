var enablix = enablix || {};
enablix.studioApp = angular.module("studio", ['ui.router', 'angularTreeview', 'listGroupTreeview', 
           'angularFileUpload', 'ui.bootstrap', 'isteven-multi-select', 'ui-notification', 'enablixFilters',
           'ngSanitize', 'ui.select', 'ngMessages', 'pascalprecht.translate', 'noCAPTCHA', 'PubSub', 
           'ngMaterial', 'ngLoadingSpinner', 'ngQuill', 'digestHud']);

enablix.studioApp.config(['digestHudProvider', function(digestHudProvider) {
	  digestHudProvider.enable();

	  // Optional configuration settings:
	  digestHudProvider.setHudPosition('top right'); // setup hud position on the page: top right, bottom left, etc. corner
	  digestHudProvider.numTopWatches = 20;  // number of items to display in detailed table
	  digestHudProvider.numDigestStats = 25;  // number of most recent digests to use for min/med/max stats
	}]);

// default configuration for quill-editor [rich text editor]
enablix.studioApp.config(['ngQuillConfigProvider', function (ngQuillConfigProvider) {
    ngQuillConfigProvider.set({
    	modules: {
	        toolbar: 
	        	[
	                  ['bold'/*, 'italic', 'underline', 'strike'*/],        // toggled buttons
	                  //['blockquote', 'code-block'],

	                  //[{ 'header': 1 }, { 'header': 2 }],               // custom button values
	                  [{ 'list': 'ordered' }, { 'list': 'bullet' }],
	                  //[{ 'script': 'sub' }, { 'script': 'super' }],      // superscript/subscript
	                  [{ 'indent': '-1' }, { 'indent': '+1' }],          // outdent/indent
	                  //[{ 'direction': 'rtl' }],                         // text direction

	                  //[{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
	                  //[{ 'header': [1, 2, 3, 4, 5, 6, false] }],

	                  //[{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
	                  //[{ 'font': [] }],
	                  //[{ 'align': [] }],

	                  //['clean'],                                         // remove formattingbutton

	                  //['link', 'image', 'video']                         // link and image, video
	            ]
		}
    });
}]);

//material design default theme
enablix.studioApp.config(function($mdThemingProvider) {
	$mdThemingProvider.theme('default')
		.primaryPalette('blue-grey', {
			'default': '900'
		})
	    .accentPalette('blue', {
	    	'default': '800'
	    });	
});

enablix.dateFormat = 'MM/dd/yyyy';
enablix.errorMsgShowTime = 10000; // in milli-seconds 
enablix.defaultPageSize = 10;
enablix.subContainerItemLimit = 5;
enablix.uploadDocSizeLimit = 1 * 1024 * 1024; // size in bytes 

var appSetup = function(StudioSetupService) {
	return StudioSetupService.setupStudio();
};

var contentWFInit = function(ContentApprovalService) {
	return ContentApprovalService.init();
}

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
			templateUrl: 'views/loginForm.html',
			controller: 'Login2Controller'
		})
		.state('authorizationError', {
			url: '/authError',
			templateUrl: 'views/authError.html'
		})
		.state('studio', {
			url: '/studio/{studioName}',
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
		.state('portal.recentdatalist', {
			url: '/recentlist?sf_lastXDays',
			templateUrl: 'views/portal/recent-list.html',
			controller: 'RecentListCtrl',
			resolve: {
				setupData: appSetup 
			},
			noBreadcrumbs: true
		})
		.state('portal.home', {
			url: '/home',
			templateUrl: 'views/portal/portal-home.html',
			resolve: {
				setupData: appSetup 
			},
			noBreadcrumbs: true
		})
		.state('portal.search', {
			url: '/search/t/{searchText}/?page',
			params: {
				page: {
					value: '0',
					squash: true
				}
			},
			templateUrl: 'views/portal/portal-search.html',
			controller: 'PortalSearchCtrl',
			resolve: {
				setupData: appSetup 
			},
			noBreadcrumbs: true
		})
		.state('portal.containerlist', {
			url: '/containerlist/{containerQId}?page',
			params: {
				page: {
					value: '0',
					squash: true
				}
			},
			views: {
				// the main template
				'': {
					templateUrl: 'views/portal/portal-container-list.html',
					controller: 'PortalCntnrListCtrl',
					resolve: {
						setupData: appSetup 
					}
				},
				
				// right section template
				'rightSection@portal.containerlist' : {
					templateUrl: 'views/portal/container/recommended-section.html',
					controller: 'PortalCntnrRecommendedCtrl',
					resolve: {
						setupData: appSetup 
					}
				}
			}
		})
		.state('portal.container', {
			url: '/container/{containerQId}/{elementIdentity}',
			views: {
				// the main template
				'': {
					templateUrl: function($stateParams) {
						return 'views/portal/portal-container.html';
					},
					controllerProvider: function($stateParams) {
						var ctrlName = 'PortalCntnrDetailCtrl';
						return ctrlName;
					},
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
		.state('portal.subContainerList', {
			url: '/subcontainer/{containerQId}/{subContainerQId}/p/{elementIdentity}',
			views: {
				// the main template
				'': {
					templateUrl: 'views/portal/portal-container.html',
					controller: 'PortalSubCntnrListCtrl',
					resolve: {
						setupData: appSetup 
					}
				},
				
				// right section template
				'rightSection@portal.subContainerList' : {
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
		.state('system.users.view', {
			url: '/view/{identity}/',
			templateUrl: 'views/user/viewuser.html',
			controller: 'SaveUserController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.audit', {
			url: '/audit',
			templateUrl: 'views/system/audit/auditList.html',
			controller: 'AuditController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.docstore', {
			url: '/docstore',
			templateUrl: 'views/system/docstore/doc-store-config.html',
			controller: 'DocStoreConfigController'
		})
		.state('forgotpassword', {
			url: '/forgotpassword',
			templateUrl: 'views/forgot-password.html',
			controller: 'ForgotPasswordController'
			
		})
		.state('setpassword', {
			url: '/setpassword',
			templateUrl: 'views/set-password.html',
			controller: 'SetPasswordController'
			
		}).state('system.emailConfig', {
			url: '/emailConfig',
			templateUrl: 'views/setup/email-setup/emailConfigData.html',
			controller: 'EmailController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.emailConfig.list', {
			url: '/list',
			templateUrl: 'views/setup/email-setup/emailconfigList.html',
			controller: 'EmailController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.emailConfig.add', {
			url: '/add',
			templateUrl: 'views/setup/email-setup/addEmailConfig.html',
			controller: 'EmailController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.emailConfig.edit', {
			url: '/edit/{identity}',
			templateUrl: 'views/setup/email-setup/editEmailConfig.html',
			controller: 'EmailController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('system.quicklinks', {
			url: '/quicklinks',
			templateUrl: 'views/system/quicklinks/quickLinksConfig.html',
			controller: 'QuickLinksConfigController',
			resolve: {
				setupData: appSetup
			}
		})
		.state('system.recommendations', {
			url: '/reco',
			templateUrl: 'views/system/reco/recoConfig.html',
			controller: 'RecommendationConfigController',
			resolve: {
				setupData: appSetup
			}
		})
		.state('system.contentrequestlist', {
			url: '/contentrequest',
			templateUrl: 'views/content/content-suggest-list.html',
			controller: 'ContentRequestListCtrl',
			resolve: {
				setupData: appSetup,
				contentWFInit: contentWFInit,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('MANAGE_CONTENT_REQUEST')
				}]
			}
		})
		.state('system.contentrequestedit', {
			url: '/contentrequest/edit/{refObjectIdentity}/',
			templateUrl: 'views/content/content-suggest.html',
			controller: 'ContentSuggestEditCtrl',
			resolve: {
				setupData: appSetup,
				contentWFInit: contentWFInit,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('MANAGE_CONTENT_REQUEST')
				}]
			}
		})
		.state('system.contentrequestview', {
			url: '/contentrequest/a/{action}/{refObjectIdentity}/',
			templateUrl: 'views/content/content-suggest-detail.html',
			controller: 'ContentSuggestDetailCtrl',
			resolve: {
				setupData: appSetup,
				contentWFInit: contentWFInit,
				checkPageAccess: ['setupData', 'AuthorizationService', function(setupData, AuthorizationService) {
					return AuthorizationService.userHasPageAccess('MANAGE_CONTENT_REQUEST')
				}]
			}
		})
		.state('system.contentconnlist', {
			url: '/contentconn/{category}/list/',
			templateUrl: 'views/system/contentconn/content-conn-list.html',
			controller: 'ContentConnListController',
			resolve: {
				setupData: appSetup
			}
		})
		.state('system.contentconndetail', {
			url: '/contentconn/{category}/detail/{connectionIdentity}/',
			templateUrl: 'views/system/contentconn/content-conn-add-edit.html',
			controller: 'ContentConnAddEditController',
			resolve: {
				setupData: appSetup
			}
		})
		.state('system.contentconnadd', {
			url: '/contentconn/{category}/add/',
			templateUrl: 'views/system/contentconn/content-conn-add-edit.html',
			controller: 'ContentConnAddEditController',
			resolve: {
				setupData: appSetup
			}
		})
		.state('system.contentconnedit', {
			url: '/contentconn/{category}/edit/{connectionIdentity}/',
			templateUrl: 'views/system/contentconn/content-conn-add-edit.html',
			controller: 'ContentConnAddEditController',
			resolve: {
				setupData: appSetup
			}
		})
		.state('myaccount', {
			url: '/account',
			templateUrl: 'views/account/myaccount.html',
			controller: 'MyAccountController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('myaccount.contentrequestlist', {
			url: '/contentrequest',
			templateUrl: 'views/content/content-suggest-list.html',
			controller: 'ContentRequestListCtrl',
			resolve: {
				setupData: appSetup,
				contentWFInit: contentWFInit
			}
		})
		.state('myaccount.contentrequestedit', {
			url: '/contentrequest/edit/{refObjectIdentity}/',
			templateUrl: 'views/content/content-suggest.html',
			controller: 'ContentSuggestEditCtrl',
			resolve: {
				setupData: appSetup,
				contentWFInit: contentWFInit
			}
		})
		.state('myaccount.contentrequestview', {
			url: '/contentrequest/a/{action}/{refObjectIdentity}/',
			templateUrl: 'views/content/content-suggest-detail.html',
			controller: 'ContentSuggestDetailCtrl',
			resolve: {
				setupData: appSetup,
				contentWFInit: contentWFInit
			}
		})
		.state('myaccount.slackauthorization', {
			url: '/slackauthorization?source',
			templateUrl: 'views/account/slack/slackauthorization.html',
			controller: 'slackAuthController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('myaccount.slackdtls', {
			url: '/slackdtls?code&state&teamName&error&slackUserID',
			templateUrl: 'views/account/slack/slackdtls.html',
			controller: 'slackDtlsController',
			resolve: {
				setupData: appSetup 
			}
		})
		.state('play', {
			url: '/play',
			templateUrl: 'views/play/play.html',
			controller: 'PlayMainController',
			resolve: {
				setupData: appSetup
			}
		})
		.state('play.executablelist', { // list of executable play template with given playDefId as the prototypeId
			url: '/xlist/{playDefId}',
			templateUrl: 'views/play/def/play-xlist.html',
			controller: 'PlayXListCtrl',
			resolve: {
				setupData: appSetup
			}
		})
		.state('play.addExecutable', { 
			url: '/xadd/{playDefId}',
			templateUrl: 'views/play/def/add-edit-xplay.html',
			controller: 'AddEditXPlayCtrl',
			resolve: {
				setupData: appSetup
			}
		})
		.state('play.editExecutable', { 
			url: '/xedit/{playDefId}',
			templateUrl: 'views/play/def/add-edit-xplay.html',
			controller: 'AddEditXPlayCtrl',
			resolve: {
				setupData: appSetup
			}
		})
		.state('play.runlist', {
			url: '/rlist/{playDefId}', // list of play run instances created from an executable play for the given playDefId
			templateUrl: 'views/play/def/play-rlist.html',
			controller: 'PlayRListCtrl',
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

