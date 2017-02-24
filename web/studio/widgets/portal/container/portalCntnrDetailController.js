enablix.studioApp.controller('PortalCntnrDetailCtrl', 
		   ['$scope', 'ContentTemplateService', '$stateParams', 'ContentUtil', 'ContentDataService', 'StudioSetupService', 
    function($scope,   ContentTemplateService,   $stateParams,   ContentUtil,   ContentDataService,   StudioSetupService) {

		$scope.subContainerList = [];
		$scope.$stateParams = $stateParams;
		
		$scope.containerQId = $stateParams.containerQId;
		$scope.instanceIdentity = $stateParams.elementIdentity;
		$scope.hasContentStack = false;
		
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
			
			/*ContentDataService.getRecordAndChildData($stateParams.containerQId, $scope.instanceIdentity,
				function(data) {
				
					angular.forEach(data, function(contentGroup) {
						
						var containerDef = ContentTemplateService.getContainerDefinition(
												enablix.template, contentGroup.contentQId);
						
						var containerLabel = $stateParams.containerQId == containerDef.qualifiedId ? "About" : containerDef.label;
						var containerType = $stateParams.containerQId == containerDef.qualifiedId ? "single" : (subCntnr.single ? "single" : "multi");
						
						var subCntnrItem = {
								"id" : containerDef.id,
								"qualifiedId" : containerDef.qualifiedId,
								"label" : containerLabel,
								"containerDef": containerDef,
								"type": containerType,
								"records": contentGroup.records
							};
						
						$scope.subContainerList.push(subCntnrItem);
					});
				},
				function(errorData) {
					Notification.error({message: "Error retrieving content records", delay: enablix.errorMsgShowTime});
				}, enablix.subContainerItemLimit);
			*/
			
			$scope.subCntnrMultiListLimit = enablix.subContainerItemLimit;
			$scope.hasContentStack = ContentTemplateService.hasContentStackConfigItem(containerDef);
			
		} else {
			
			// get enclosure definition
			var enclDef = ContentTemplateService.getPortalEnclosureDefinition(enclosureId);
			
			var childContainerQId = $stateParams.childContainerQId;
			
			if (isNullOrUndefined(childContainerQId) || childContainerQId === "") {
			
				// enclosure main page where we will display cards for each container
				// within this enclosure
				angular.forEach(enclDef.childContainer, function(cntnrRef) {
	
					var containerDef = ContentTemplateService.getContainerDefinition(
							enablix.template, cntnrRef.id);
	
					if (!isNullOrUndefined(containerDef)) {
						cntnrList.push(containerDef);
					}
					
				});
				
				$scope.subCntnrMultiListLimit = enablix.subContainerItemLimit;
				
			} else {
				
				// child container of enclosure specified. We will display the detail
				// page for that child container
				for (var i = 0; i < enclDef.childContainer.length; i++) {
					
					var childCntnr = enclDef.childContainer[i];
					
					if (childCntnr.id == childContainerQId) {
						
						var containerDef = ContentTemplateService.getContainerDefinition(
								enablix.template, childCntnr.id);
						
						cntnrList.push(containerDef)
						
						$scope.hasContentStack = ContentTemplateService.hasContentStackConfigItem(containerDef);
					}
				}
			}
			
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
		
		// check and push content stack cards
		if ($scope.containerQId && $scope.instanceIdentity && $scope.hasContentStack) {
			
			ContentDataService.getContentStackForRecord($scope.containerQId, $scope.instanceIdentity, 
					function(data) {
				
						var contentGroupsMap = {};
						ContentUtil.groupContentRecordsByQId(data, contentGroupsMap);
						
						angular.forEach(contentGroupsMap, function(value, key) {
							
							if (value.records.length > 0) {
								
								var subCntnrItem = {
										"id" : value.qualifiedId,
										"qualifiedId" : value.qualifiedId,
										"label" : value.label,
										"containerDef": value.containerDef,
										"type": value.records.length == 1 ? "single" : "multi",
										"records": value.records
									};
								
								$scope.subContainerList.push(subCntnrItem);
							}
						});
					}, 
					function(errorData) {
						Notification.error({message: "Error retrieving content records", delay: enablix.errorMsgShowTime});
					});
		}
		
	}                                          
]);