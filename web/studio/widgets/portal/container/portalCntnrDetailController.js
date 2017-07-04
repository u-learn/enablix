enablix.studioApp.controller('PortalCntnrDetailCtrl', 
		   ['$scope', 'ContentTemplateService', '$stateParams', 'ContentUtil', 'ContentDataService', 'StudioSetupService', 'ContentConnectionService', 'Notification',
    function($scope,   ContentTemplateService,   $stateParams,   ContentUtil,   ContentDataService,   StudioSetupService,   ContentConnectionService,   Notification) {

		$scope.subContainerList = [];
		$scope.$stateParams = $stateParams;
		
		$scope.containerQId = $stateParams.containerQId;
		$scope.instanceIdentity = $stateParams.elementIdentity;
		$scope.hasContentStack = false;
		
		$scope.groupByQId = $stateParams.gbQId;
		
		var enclosureId = $stateParams.enclosureId;
		
		var cntnrList = [];
		
		var fetchRecordDetails = function() {
			
			ContentDataService.getRecordAndChildData($stateParams.containerQId, $scope.instanceIdentity,
				function(data) {
				
					angular.forEach(data, function(contentGroup, $index) {
						
						var containerDef = ContentTemplateService.getContainerDefinition(
												enablix.template, contentGroup.contentQId);
						
						var concreteContQId = ContentTemplateService.isLinkedContainer(containerDef) ? 
												containerDef.linkContainerQId : containerDef.qualifiedId;
						
						var subCntnrItem = {
								"id" : containerDef.id,
								"qualifiedId" : containerDef.qualifiedId,
								"concreteQId" : concreteContQId,
								"label" : containerDef.label,
								"containerDef": containerDef,
								"type": containerDef.single ? "single" : "multi",
								"records": contentGroup.records,
								"category": "sub-container",
								"recordCount": contentGroup.records.totalElements
							};
						
						if ($stateParams.containerQId == containerDef.qualifiedId) {
							subCntnrItem.label = "About";
							subCntnrItem.type = 'single';
							subCntnrItem.category = 'about';
							$scope.aboutSubContainer = subCntnrItem;
						}
						
						addSubContainerItem(subCntnrItem);
						
					});
				},
				function(errorData) {
					Notification.error({message: "Error retrieving content records", delay: enablix.errorMsgShowTime});
				}, enablix.subContainerItemLimit, "PORTAL");
			
			$scope.subCntnrMultiListLimit = enablix.subContainerItemLimit;
		}
		
		var fetchGroupedBySubContainers = function(_gbQId) {
			
			ContentConnectionService.getFirstContentConnVOByContentQId(_gbQId, 
				function(data) {
					
					$scope.groupedSubContainers = data.valueLinks;
					
					angular.forEach($scope.groupedSubContainers, function(groupSubCntnr) {
						groupSubCntnr.subContainerList = [];
					});
					
					getRecordData();
				}, 
				function(errorData) {
					Notification.error({message: "Error retrieving content grouping data", delay: enablix.errorMsgShowTime});
				});
		}
		
		var addSubContainerItem = function(_subCntnrItem) {
			
			if ($scope.groupByQId) { 
				
				if (_subCntnrItem.category != 'about') {
					
					// do not show the about sub container in group by view, else show
					
					angular.forEach($scope.groupedSubContainers, function(groupSubCntnr) {
						
						if (groupSubCntnr.connectedContainers.contains(_subCntnrItem.concreteQId)) {
							
							// sub container might exist under multiple groups, hence make a copy and add
							var copy = angular.copy(_subCntnrItem);
							copy.id = copy.id + groupSubCntnr.recordIdentity;
							
							groupSubCntnr.subContainerList.push(copy);
						}
					});
				}
				
			} else {
				
				if (_subCntnrItem.category == 'about') {
					// about container should always be the first, so add it to front
					//$scope.subContainerList.unshift(_subCntnrItem);
				} else {
					$scope.subContainerList.push(_subCntnrItem);
				}
			}
		}
		
		var checkAndFetchContentStack = function() {
			
			// check and push content stack cards
			if ($scope.containerQId && $scope.instanceIdentity && $scope.hasContentStack) {
				
				var pageData = {
					page: 0,
					size: enablix.subContainerItemLimit
				};
				
				ContentDataService.getContentStackForRecord($scope.containerQId, $scope.instanceIdentity, pageData,
						function(data) {
					
							angular.forEach(data, function(contentGroup, $index) {
								
								var containerDef = ContentTemplateService.getContainerDefinition(
														enablix.template, contentGroup.contentQId);
								
								var concreteContQId = ContentTemplateService.isLinkedContainer(containerDef) ? 
														containerDef.linkContainerQId : containerDef.qualifiedId;
								
								var subCntnrItem = {
										"id" : containerDef.id,
										"qualifiedId" : containerDef.qualifiedId,
										"concreteQId" : concreteContQId,
										"label" : containerDef.label,
										"containerDef": containerDef,
										"type": "multi",
										"records": contentGroup.records,
										"category": "content-stack",
										"parentQId": $scope.containerQId,
										"recordCount": contentGroup.records.totalElements
									};
								
								addSubContainerItem(subCntnrItem);
							});
							
						}, 
						function(errorData) {
							Notification.error({message: "Error retrieving content records", delay: enablix.errorMsgShowTime});
						});
			}
			
		}
		
		var getRecordData = function() {
			fetchRecordDetails();
			checkAndFetchContentStack();
		}
		
		if (isNullOrUndefined(enclosureId)) {
			
			var containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.containerQId);
		
			$scope.hasContentStack = ContentTemplateService.hasContentStackConfigItem(containerDef);
			
			if ($scope.groupByQId) {
				fetchGroupedBySubContainers($scope.groupByQId);
			} else {
				getRecordData();
			}
			
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
						
						checkAndFetchContentStack();
						
					}
				}
			}
			
		}
		
		var selectedSubContainers = [];
		$scope.subContSelectChange = function(_selectedContainers) {
			selectedSubContainers = _selectedContainers;
		}
		
		$scope.filterSubContainers = function() {
			return function(item) {
				return selectedSubContainers.length == 0 || selectedSubContainers.contains(item, function(o1, o2) {
					return o1.id === o2.qualifiedId;
				});
			};
		}
		
		angular.forEach(cntnrList, function(subCntnr) {
			
			var concreteContQId = ContentTemplateService.isLinkedContainer(subCntnr) ? 
									subCntnr.linkContainerQId : subCntnr.qualifiedId;
			
			var subCntnrItem = {
					"id" : subCntnr.id,
					"qualifiedId" : subCntnr.qualifiedId,
					"concreteQId" : concreteContQId,
					"label" : subCntnr.label,
					"containerDef": subCntnr,
					"type": subCntnr.single ? "single" : "multi"
				};
			
			$scope.subContainerList.push(subCntnrItem);
		});
		
	}                                          
]);