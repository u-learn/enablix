enablix.studioApp.factory('StateUpdateService', 
	[
	 			'$state', '$stateParams', '$rootScope', '$location', '$window', 'NavigationTracker',
	 	function($state,   $stateParams,   $rootScope,   $location,   $window,   NavigationTracker) {
	 		
	 		var goToStudioList = function(_containerQId, _parentIdentity) {
				$state.go('studio.list', {'containerQId' : _containerQId, 
					"parentIdentity" : _parentIdentity});

	 		};
	 		
	 		var goToStudioDetail = function(_containerQId, _elementIdentity) {
				$state.go('studio.detail', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};
	 		
	 		var goToStudioAdd = function(_containerQId, _parentIdentity) {
	 			$state.go('studio.add', {
					'containerQId': _containerQId,
					'parentIdentity': _parentIdentity
				});
	 		};

	 		var goToStudioEdit = function(_containerQId, _elementIdentity) {
				$state.go('studio.edit', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};

	 		var goToRefdataList = function(_containerQId) {
				$state.go('refdata.list', {'containerQId' : _containerQId});
	 		};

	 		var goToRefdataAdd = function(_containerQId) {
				$state.go('refdata.add', {'containerQId' : _containerQId});
	 		};

	 		var goToRefdataDetail = function(_containerQId, _elementIdentity) {
				$state.go('refdata.detail', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};
	 		
	 		var goToRefdataEdit = function(_containerQId, _elementIdentity) {
				$state.go('refdata.edit', {'containerQId' : _containerQId, 
					"elementIdentity" : _elementIdentity});

	 		};
	 		
	 		var goToHome = function() {
				$state.go('home');
	 		};
	 		
	 		var goToLogin = function(previousUrl) {
				if(previousUrl.lastIndexOf("#")>0 && previousUrl.substring(previousUrl.lastIndexOf("#") + 1, previousUrl.length).length > 7)
					//if previous URL is other than logout
					$window.location.href = $location.protocol() + "://" + $location.host() 
 											+ ":" + $location.port() + "/login.html#/login#" +"redirect#" + encodeURIComponent(previousUrl);
				else
					$window.location.href = $location.protocol() + "://" + $location.host() 
 											+ ":" + $location.port() + "/login.html#/login";
											
	 		};
	 		
	 		var goToWebsite = function(previousUrl) {
	 			$window.location.href = $location.protocol() + "://" + $location.host() 
					+ ":" + $location.port() + "/index.html";
											
	 		};
	 		
	 		var goToApp = function() {
	 			$window.location.href = $location.protocol() + "://" + $location.host() 
 						+ ":" + $location.port() + "/app.html#/portal/home";
	 		};
	 		
			var goToForgotPasswordPage = function(){
	 			$state.go('forgotpassword');
	 		};

	 		var goToAppSetPassword = function() {
	 			$window.location.href = $location.protocol() + "://" + $location.host() 
					+ ":" + $location.port() + "/app.html#/setpassword";
	 		}
	 		
	 		var goToStudio = function(_containerQId) {
				$state.go('studio');
	 		};
	 		
	 		var goToPortalContainerDetail = function(_containerQId, _elemIdentity) {
	 			$state.go("portal.container", {
	 				"containerQId": _containerQId,
	 				"elementIdentity": _elemIdentity
	 			});
	 		};
	 		
	 		var goToPortalSubContainerList = function(_containerQId, _subContainerQId, _parentIdentity) {
	 			$state.go("portal.subContainerList", {
	 				"containerQId": _containerQId,
	 				"subContainerQId": _subContainerQId,
	 				"elementIdentity": _parentIdentity
	 			});
	 		}
	 		
	 		var goToPortalContainerList = function(_containerQId) {
	 			$state.go("portal.containerlist", {
	 				"containerQId": _containerQId
	 			});
	 		}
	 		
	 		var goToPortalContainerBody = function(_containerQId, _containerInstanceIdentity, 
	 												_subCntnrType, _subContainerQId) {
	 			$state.go("portal.container.body", {
	 				"containerQId": _containerQId,
	 				"elementIdentity": _containerInstanceIdentity,
	 				"type": _subCntnrType,
	 				"subContainerQId": _subContainerQId
	 			});
	 		};

	 		var goToPortalSubItem = function(_containerQId, _containerInstanceIdentity, 
						_subContainerQId, _subItemIdentity) {
				$state.go("portal.subItem", {
					"containerQId": _containerQId,
					"elementIdentity": _containerInstanceIdentity,
					"subItemIdentity": _subItemIdentity,
					"subContainerQId": _subContainerQId
				});
			};
	 		
	 		var goToPortalHome = function() {
	 			$state.go("portal.home");
	 		};
	 		
	 		var goToAuthError = function() {
	 			$state.go("authorizationError");
	 		};
	 		
	 		var goAddUser = function(){	 			
	 			$state.go("system.users.add");
	 		};
	 		
	 		var goToListUser = function(){	 			
	 			$state.go("system.users.list");
	 		};
	 		
	 		var goEditUser=function(_identity){
	 			$state.go("system.users.edit",{"identity" : _identity});
	 		};
	 		
	 		var goToSetPassword=function(){
	 			$state.go("setpassword");
	 		};
	 		
	 		var goToAddEmailConfig=function(){
	 			$state.go("system.emailConfig.add");
	 		};
	 		
	 		var goToEmailConfig = function() {
	 			$state.go("system.emailConfig.list");
	 		};
	 		
	 		var goToEditEmailConfig=function(){
	 			$state.go("system.emailConfig.edit")
	 		}

	 		var goToPortalEnclosureDetail = function(_enclosureId, _childContainerId) {
	 			$state.go("portal.enclosure", {
	 				"enclosureId": _enclosureId,
	 				"childContainerQId": _childContainerId
	 			});
	 		}
	 		
	 		var goToPortalEnclosureBody = function(_enclosureId, _subCntnrType, _subContainerQId) {
	 			$state.go("portal.enclosure.body", {
	 				"enclosureId": _enclosureId,
	 				"type": _subCntnrType,
	 				"subContainerQId": _subContainerQId
	 			});
	 		};
	 		
	 		var goToPortalSearch = function(_searchText) {
	 			$state.go("portal.search", {
	 				"searchText": _searchText
	 			});
	 		};
	
	 		var reload = function() {
	 			NavigationTracker.stopRecording();
	 			$state.transitionTo($state.current, $stateParams, {
	 			    reload: true,
	 			    inherit: false,
	 			    notify: true
	 			});
	 		};

	 		var goToContentRequestEdit = function(refObjectIdentity) {
	 			$state.go("system.contentrequestedit", {
	 				"refObjectIdentity": refObjectIdentity
	 			});
	 		}
	 		
	 		var goToContentRequestDetail = function(refObjectIdentity) {
	 			$state.go("system.contentrequestview", {
	 				"action": "view",
	 				"refObjectIdentity": refObjectIdentity
	 			});
	 		}
	 		
	 		var goToMyContentRequestEdit = function(refObjectIdentity) {
	 			$state.go("myaccount.contentrequestedit", {
	 				"refObjectIdentity": refObjectIdentity
	 			});
	 		}
	 		
	 		var goToMyContentRequestDetail = function(refObjectIdentity) {
	 			$state.go("myaccount.contentrequestview", {
	 				"action": "view",
	 				"refObjectIdentity": refObjectIdentity
	 			});
	 		}
	 		
	 		var goToPreviousState = function() {
	 			var prevState = NavigationTracker.getPreviousState();
	 			if (!isNullOrUndefined(prevState)) {
	 				$state.go(prevState.route, prevState.routeParams);
	 			}
	 		}
	 		
	 		var goBack = function() {
	 			NavigationTracker.stopRecording();
	 			var prevState = NavigationTracker.getPreviousState(true);
	 			if (!isNullOrUndefined(prevState)) {
	 				$state.go(prevState.route, prevState.routeParams);
	 			}
	 		}
	 		
	 		var goToContentConnList = function(_category) {
	 			$state.go("system.contentconnlist", {
	 				category: _category
	 			});
	 		};
	 		
	 		var goToContentConnDetail = function(_category, _connIdentity) {
	 			$state.go("system.contentconndetail", {
	 				"category": _category,
	 				"connectionIdentity": _connIdentity
	 			});
	 		};
	 		
	 		var goToContentConnEdit = function(_category, _connIdentity) {
	 			$state.go("system.contentconnedit", {
	 				"category": _category,
	 				"connectionIdentity": _connIdentity
	 			});
	 		};
	 		
	 		var goToContentConnAdd = function(_category) {
	 			$state.go("system.contentconnadd", {
	 				category: _category
	 			});
	 		};
	 		
	 		var goToPlayXList = function(_playDefId) {
	 			$state.go("play.executablelist", {
	 				playDefId: _playDefId
	 			});
	 		};
	 		
	 		var goToPlayRList = function(_playDefId) {
	 			$state.go("play.runlist", {
	 				playDefId: _playDefId
	 			});
	 		};
	 		
	 		var goToAddXPlay = function(_playDefId) {
	 			$state.go("play.addExecutable", {
	 				playDefId: _playDefId
	 			});
	 		};
	 		
	 		var goToEditXPlay = function(_playDefId) {
	 			$state.go("play.editExecutable", {
	 				playDefId: _playDefId
	 			});
	 		};
	 		
	 		var goToRecentUpdateList = function(_params) {
	 			$state.go("portal.recentdatalist", _params);
	 		};
	 		var goToSlackDtls = function(teamName) {
	 			$state.go("myaccount.slackdtls", {
	 				teamName: teamName
	 			});
	 		};
	 		var goToSlackAuth = function(source) {
	 			$state.go("myaccount.slackauthorization",{
	 				source :source
	 			});
	 		};
	 		return {
	 			goToApp: goToApp,
	 			goToAppSetPassword: goToAppSetPassword,
	 			goToStudioList: goToStudioList,
	 			goToStudioDetail: goToStudioDetail,
	 			goToStudioAdd: goToStudioAdd,
	 			goToStudioEdit: goToStudioEdit,
	 			goToRefdataList: goToRefdataList,
	 			goToRefdataAdd: goToRefdataAdd,
	 			goToRefdataDetail: goToRefdataDetail,
	 			goToRefdataEdit: goToRefdataEdit,
	 			goToHome: goToHome,
	 			goToLogin: goToLogin,
	 			goToWebsite: goToWebsite,
	 			goToStudio: goToStudio,
	 			goToPortalHome: goToPortalHome,
	 			goToPortalContainerDetail: goToPortalContainerDetail,
	 			goToPortalSubContainerList: goToPortalSubContainerList,
	 			goToPortalContainerList: goToPortalContainerList,
	 			goToPortalContainerBody: goToPortalContainerBody,
	 			goToPortalEnclosureDetail: goToPortalEnclosureDetail,
	 			goToPortalEnclosureBody: goToPortalEnclosureBody,
	 			goToPortalSubItem: goToPortalSubItem,
	 			goToPortalSearch: goToPortalSearch,
	 			goToAuthError: goToAuthError,
	 			goBack: goBack,
	 			reload: reload,
	 			goToPreviousState: goToPreviousState,
	 			goAddUser : goAddUser,
	 			goEditUser : goEditUser,
	 			goToListUser : goToListUser,
	 			goToSetPassword : goToSetPassword,
	 			goToAddEmailConfig : goToAddEmailConfig,
	 			goToEmailConfig : goToEmailConfig,
	 			goToEditEmailConfig : goToEditEmailConfig,
	 			goToForgotPasswordPage : goToForgotPasswordPage,
	 			goToContentRequestEdit : goToContentRequestEdit,
	 			goToContentRequestDetail : goToContentRequestDetail,
	 			goToMyContentRequestEdit : goToMyContentRequestEdit,
	 			goToMyContentRequestDetail : goToMyContentRequestDetail,
	 			goToContentConnList : goToContentConnList,
	 			goToContentConnDetail : goToContentConnDetail,
	 			goToContentConnEdit : goToContentConnEdit,
	 			goToContentConnAdd : goToContentConnAdd,
	 			goToPlayXList : goToPlayXList,
	 			goToPlayRList : goToPlayRList,
	 			goToAddXPlay : goToAddXPlay,
	 			goToEditXPlay : goToEditXPlay,
	 			goToRecentUpdateList : goToRecentUpdateList,
				goToSlackDtls:goToSlackDtls,
	 			goToSlackAuth:goToSlackAuth
	 		};
	 	}
	 ]);
