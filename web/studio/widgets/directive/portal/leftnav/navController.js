enablix.studioApp.controller('PortalLeftNavCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil) {
		
		$scope.navItemList = [];
		$scope.$stateParams = $stateParams;
		
		var enclosureId = $stateParams.enclosureId;
		
		$scope.navToItemDetail = function(navItem) {
			var _subCntnrType = 'multi';
			
			if ($stateParams.containerQId === navItem.qualifiedId
					|| (navItem.containerDef && navItem.containerDef.single)) {
				_subCntnrType = 'single';
			} 
			
			if (isNullOrUndefined(enclosureId)) {
				StateUpdateService.goToPortalContainerBody($stateParams.containerQId, 
					$stateParams.elementIdentity, _subCntnrType, navItem.qualifiedId);
				
			} else {
				StateUpdateService.goToPortalEnclosureBody(enclosureId, 
						_subCntnrType, navItem.qualifiedId);
			}
		};

		var cntnrList = [];
		
		if (isNullOrUndefined(enclosureId)) {
			
			var containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.containerQId);
		
			var abtIndexItem = {
					"id" : containerDef.id,
					"qualifiedId" : containerDef.qualifiedId,
					"label" : "About",
					"containerDef": containerDef,
					"single": true
				};
			
			$scope.navItemList.push(abtIndexItem);
			cntnrList = containerDef.container;
			
		} else {
			
			var enclDef = ContentTemplateService.getPortalEnclosureDefinition(enclosureId);
			
			angular.forEach(enclDef.childContainer, function(cntnrRef) {

				var containerDef = ContentTemplateService.getContainerDefinition(
						enablix.template, cntnrRef.id);

				if (!isNullOrUndefined(containerDef)) {
					cntnrList.push(containerDef);
				}
				
			});
			
		}
		
		angular.forEach(cntnrList, function(subCntnr) {
			
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
