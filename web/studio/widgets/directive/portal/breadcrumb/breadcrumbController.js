enablix.studioApp.controller('PortalBreadcrumbCtrl',
			['$scope', '$rootScope', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil',
    function ($scope,   $rootScope,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil) {
		
		
		$rootScope.$on('$stateChangeSuccess', 
			function(event, toState, toParams, fromState, fromParams) {
				if (toState.name.indexOf("portal") == 0
						&& fromState.name.indexOf("portal") == 0) {
					createBreadCrumbList(toParams);
				}
			});
		
		var createBreadCrumbList = function($stateParams) {

			$scope.breadcrumbList = $scope.breadcrumbList || [];
			
			var containerQId = $stateParams.containerQId;
			var elemIdentity = $stateParams.elementIdentity;
			var subContainerQId = $stateParams.subContainerQId;
			var enclosureId = $stateParams.enclosureId;
			
			if (isNullOrUndefined(enclosureId)) {
				var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
	
				ContentDataService.getContentRecordData(enablix.templateId, containerQId, elemIdentity, 
					function(recordData) {
						
						var breadCrumbs = []
					
						// first breadcrumb item, container name
						breadCrumbs.push(containerDef.label);
						
						var containerLabel = ContentUtil.resolveContainerInstanceLabel(containerDef, recordData);
						breadCrumbs.push(containerLabel);
						
						if (containerQId != subContainerQId) {
							var subContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, subContainerQId);
							breadCrumbs.push(subContainerDef.label);
						}
						
						$scope.breadcrumbList = breadCrumbs;
					}, 
					function(errResp) {
						// ignore
					});
				
			} else {
				
				var breadCrumbs = [];
				
				var enclDef = ContentTemplateService.getPortalEnclosureDefinition(enclosureId);
				breadCrumbs.push(enclDef.label);
				
				if (!isNullOrUndefined(subContainerQId)) {
					
					var subContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, subContainerQId);
				
					if (!isNullOrUndefined(subContainerDef)) {
						breadCrumbs.push(subContainerDef.label);
					}
				}
				
				$scope.breadcrumbList = breadCrumbs;
			}
			
		};
		
		createBreadCrumbList($stateParams);
		
	}]);
