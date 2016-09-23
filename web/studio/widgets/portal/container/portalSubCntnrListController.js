enablix.studioApp.controller('PortalSubCntnrListCtrl', 
		   ['$scope', 'ContentTemplateService', '$stateParams', 'ContentDataService', 'StudioSetupService', 'QIdUtil', 
    function($scope,   ContentTemplateService,   $stateParams,   ContentDataService,   StudioSetupService,   QIdUtil) {

		$scope.subContainerList = [];
		$scope.$stateParams = $stateParams;
		
		var containerDef = ContentTemplateService.getContainerDefinition(
								enablix.template, $stateParams.subContainerQId);
	
		var subCntnrItem = {
				"id" : containerDef.id,
				"qualifiedId" : containerDef.qualifiedId,
				"label" : containerDef.label,
				"containerDef": containerDef,
				"type": containerDef.single ? "single" : "multi"
			};
		
		$scope.subContainerList.push(subCntnrItem);
					
	}                                          
]);