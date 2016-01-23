enablix.studioApp.controller('PortalBreadcrumbCtrl',
			['$scope', '$rootScope', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil', 'Notification', 'StateUpdateService', 
    function ($scope,   $rootScope,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil,   Notification,   StateUpdateService) {
		
		
		$rootScope.$on('$stateChangeSuccess', 
			function(event, toState, toParams, fromState, fromParams) {
				if (toState.name.indexOf("portal") == 0
						&& fromState.name.indexOf("portal") == 0
						&& toState.name != "portal.home"
						&& toState.name != "portal.search") {
					createBreadCrumbList(toParams);
				}
			});
		
		$scope.navToItem = function(_containerQId, _contentIdentity) {

			if (!isNullOrUndefined(_containerQId) && !isNullOrUndefined(_contentIdentity)) {
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
			
			if (isNullOrUndefined(enclosureId)) {
	
				ContentDataService.getNavigationPath(containerQId, elemIdentity, 
					function(navPath) {
						
						var breadCrumbs = [];

						if (!isNullOrUndefined(navPath)) {
							
							var navContentPointer = navPath;
							
							var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, navContentPointer.qualifiedId);
							
							// first breadcrumb item, container name
							breadCrumbs.push({
								label: containerDef.label,
							})
							
							while (!isNullOrUndefined(navContentPointer)) {
								
								breadCrumbs.push({
									label: navContentPointer.label,
									qualifiedId: navContentPointer.qualifiedId,
									identity: navContentPointer.identity
								});
								
								// move pointer to next
								navContentPointer = navContentPointer.next;
							}
						}
						
						$scope.breadcrumbList = breadCrumbs;
					}, 
					function(errResp) {
						Notification.error({message: "Error loading breadcrumbs", delay: enablix.errorMsgShowTime});
					});
				
			} else {
				
				var breadCrumbs = [];
				
				var enclDef = ContentTemplateService.getPortalEnclosureDefinition(enclosureId);
				breadCrumbs.push({
					label: enclDef.label
				});
				
				if (!isNullOrUndefined(subContainerQId)) {
					
					var subContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, subContainerQId);
				
					if (!isNullOrUndefined(subContainerDef)) {
						breadCrumbs.push({
							label: subContainerDef.label
						});
					}
				}
				
				$scope.breadcrumbList = breadCrumbs;
			}
			
		};
		
		createBreadCrumbList($stateParams);
		
	}]);
