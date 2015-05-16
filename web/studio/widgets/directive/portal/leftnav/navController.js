enablix.studioApp.controller('PortalLeftNavCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil) {
		
		$scope.navItemList = [];
		$scope.$stateParams = $stateParams;
		
		var containerDef = ContentTemplateService.getContainerDefinition(
								enablix.template, $stateParams.containerQId);
		
		$scope.navToItemDetail = function(navItem) {
			var _subCntnrType = 'multi';
			
			if ($stateParams.containerQId === navItem.qualifiedId
					|| (navItem.containerDef && navItem.containerDef.single)) {
				_subCntnrType = 'single';
			} 
			
			StateUpdateService.goToPortalContainerBody($stateParams.containerQId, 
					$stateParams.elementIdentity, _subCntnrType, navItem.qualifiedId);
		};
		
		var abtIndexItem = {
				"id" : containerDef.id,
				"qualifiedId" : containerDef.qualifiedId,
				"label" : "About",
				"containerDef": containerDef,
				"single": true
			};
		
		$scope.navItemList.push(abtIndexItem);
		
		angular.forEach(containerDef.container, function(subCntnr) {
			
			var indxItem = {
					"id" : subCntnr.id,
					"qualifiedId" : subCntnr.qualifiedId,
					"label" : subCntnr.label,
					"containerDef": subCntnr,
					"single": subCntnr.single
				};
			
			$scope.navItemList.push(indxItem);
		});
				
	}]);
