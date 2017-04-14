enablix.studioApp.controller('PortalSubCntnrListCtrl', 
		   ['$scope', 'ContentTemplateService', '$stateParams', 'ContentDataService', 'StudioSetupService', 'QIdUtil', 
    function($scope,   ContentTemplateService,   $stateParams,   ContentDataService,   StudioSetupService,   QIdUtil) {

		$scope.subContainerList = [];
		$scope.$stateParams = $stateParams;
		
		var parentContDef = ContentTemplateService.getContainerDefinition(
				enablix.template, $stateParams.containerQId);
		
		if (ContentTemplateService.hasChildContainer(parentContDef, $stateParams.subContainerQId)) {

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
	
		$scope.hasContentStack = ContentTemplateService.hasContentStackConfigItem(parentContDef);
		
		// check and push content stack cards
		if ($stateParams.containerQId && $stateParams.elementIdentity && $scope.hasContentStack) {
			
			var pageData = {
				page: 0,
				size: enablix.subContainerItemLimit
			};
			
			ContentDataService.getContentStackItemForRecord($stateParams.containerQId, 
					$stateParams.elementIdentity, $stateParams.subContainerQId,
					function(data) {
				
						angular.forEach(data, function(contentGroup, $index) {
							
							var containerDef = ContentTemplateService.getContainerDefinition(
													enablix.template, contentGroup.contentQId);
							
							var subCntnrItem = {
									"id" : containerDef.id,
									"qualifiedId" : containerDef.qualifiedId,
									"label" : containerDef.label,
									"containerDef": containerDef,
									"type": "multi",
									"records": contentGroup.records,
									"category": "sub-container",
									"parentQId": $scope.containerQId
								};
							
							$scope.subContainerList.push(subCntnrItem);
						});
						
					}, 
					function(errorData) {
						Notification.error({message: "Error retrieving content records", delay: enablix.errorMsgShowTime});
					});
		}
					
	}                                          
]);