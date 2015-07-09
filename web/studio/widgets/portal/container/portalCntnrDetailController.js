enablix.studioApp.controller('PortalCntnrDetailCtrl', 
		   ['$scope', 'ContentTemplateService', '$stateParams', 'ContentDataService', 'StudioSetupService', 
    function($scope,   ContentTemplateService,   $stateParams,   ContentDataService,   StudioSetupService) {

		$scope.subContainerList = [];
		$scope.$stateParams = $stateParams;
		
		var enclosureId = $stateParams.enclosureId;
		
		var cntnrList = [];
		
		if (isNullOrUndefined(enclosureId)) {
			
			var containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.containerQId);
		
			var abtSubCntnrItem = {
					"id" : containerDef.id,
					"qualifiedId" : containerDef.qualifiedId,
					"label" : "About",
					"containerDef": containerDef,
					"type": "single"
				};
			
			$scope.subContainerList.push(abtSubCntnrItem);
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
			
			var subCntnrItem = {
					"id" : subCntnr.id,
					"qualifiedId" : subCntnr.qualifiedId,
					"label" : subCntnr.label,
					"containerDef": subCntnr,
					"type": subCntnr.single ? "single" : "multi"
				};
			
			$scope.subContainerList.push(subCntnrItem);
		});	   
		
	}                                          
]);