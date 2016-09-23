enablix.studioApp.controller('PortalBreadcrumbCtrl',
			['$scope', '$rootScope', '$state', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil', 'Notification', 'StateUpdateService', 
    function ($scope,   $rootScope,   $state,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil,   Notification,   StateUpdateService) {
		
		var PORTAL_HOME_QID = "_portal_home";
		var PORTAL_HOME_LABEL = "Home";
				
		
		$rootScope.$on('$stateChangeSuccess', 
			function(event, toState, toParams, fromState, fromParams) {
				if (toState.name.indexOf("portal") == 0
						&& fromState.name.indexOf("portal") == 0
						&& toState.name != "portal.home"
						&& toState.name != "portal.search") {
					createBreadCrumbList(toParams);
				}
			});
		
		$scope.navToItem = function(_containerQId, _contentIdentity, _enclosureId) {

			if (_containerQId === PORTAL_HOME_QID) {
				StateUpdateService.goToPortalHome();
				
			} else if (!isNullOrUndefined(_enclosureId)) {
				StateUpdateService.goToPortalEnclosureDetail(_enclosureId, _containerQId);
				
			} else if (!isNullOrUndefined(_containerQId) && isNullOrUndefined(_contentIdentity)) {
				StateUpdateService.goToPortalContainerList(_containerQId);
				
			} else if (!isNullOrUndefined(_containerQId) && !isNullOrUndefined(_contentIdentity)) {
				StateUpdateService.goToPortalContainerBody(
						_containerQId, _contentIdentity, 'single', _containerQId);
				
			} 
		}
		
		var createBreadCrumbList = function($stateParams) {

			$scope.breadcrumbList = $scope.breadcrumbList || [];
			
			var containerQId = $stateParams.containerQId;
			var elemIdentity = $stateParams.elementIdentity;
			var subContainerQId = $stateParams.subContainerQId;
			var enclosureId = $stateParams.enclosureId;
			
			var breadCrumbs = [];
			
			breadCrumbs.push({
				label: PORTAL_HOME_LABEL,
				qualifiedId: PORTAL_HOME_QID
			});
			
			if (isNullOrUndefined(enclosureId)) {
	
				if (!isNullOrUndefined(elemIdentity)) {
					
					ContentDataService.getNavigationPath(containerQId, elemIdentity, 
					function(navPath) {
						
						if (!isNullOrUndefined(navPath)) {
							
							var navContentPointer = navPath;
							
							var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, navContentPointer.qualifiedId);
							
							// check if it is part of enclosure. this happens when enclosure item is navigated
							// via link from other sections e.g. recent updates
							var enclDef = ContentTemplateService.getParentEnclosureDefinition(navContentPointer.qualifiedId);
							
							if (!isNullOrUndefined(enclDef)) {
								// add breadcrumb item for enclosure container name
								breadCrumbs.push({
									label: enclDef.label,
									enclosureId: enclDef.id
								});
								
								// add breadcrumb for container name
								breadCrumbs.push({
									label: containerDef.label,
									enclosureId: enclDef.id,
									qualifiedId: navContentPointer.qualifiedId
								})
								
							} else {
								// first breadcrumb item, container name
								breadCrumbs.push({
									label: containerDef.label,
									qualifiedId: navContentPointer.qualifiedId
								});
							}
							
							while (!isNullOrUndefined(navContentPointer)) {
								
								breadCrumbs.push({
									label: navContentPointer.label,
									qualifiedId: navContentPointer.qualifiedId,
									identity: navContentPointer.identity
								});
								
								// move pointer to next
								navContentPointer = navContentPointer.next;
							}
							
							// sub container list page
							if ($state.includes("portal.subContainerList")) {
								var subContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, $stateParams.subContainerQId);
								if (!isNullOrUndefined(subContainerDef)) {
									breadCrumbs.push({
										label: subContainerDef.label,
										qualifiedId: subContainerDef.qualifiedId
									});
								}
							}
							
						}
						
					}, 
					function(errResp) {
						Notification.error({message: "Error loading breadcrumbs", delay: enablix.errorMsgShowTime});
					});
					
				} else {
					
					// Bread crumb for container list page
					
					var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, $stateParams.containerQId);
					breadCrumbs.push({
						label: containerDef.label,
						qualifiedId: containerDef.qualifiedId
					});
				}
				
			} else {
				
				var enclDef = ContentTemplateService.getPortalEnclosureDefinition(enclosureId);
				breadCrumbs.push({
					label: enclDef.label,
					enclosureId: enclDef.id
				});
				
				var childContainerQId = $stateParams.childContainerQId;
				if (!isNullOrUndefined(childContainerQId)) {
					
					var subContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, childContainerQId);
				
					if (!isNullOrUndefined(subContainerDef)) {
						breadCrumbs.push({
							label: subContainerDef.label,
							enclosureId: enclDef.id,
							qualifiedId: subContainerDef.qualifiedId
						});
					}
				}
				
			}
			
			$scope.breadcrumbList = breadCrumbs;
			
		};
		
		createBreadCrumbList($stateParams);
		
	}]);
