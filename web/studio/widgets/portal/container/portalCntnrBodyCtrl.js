enablix.studioApp.controller('PortalCntnrBodyCtrl', 
		   ['$scope', '$state', 'ContentTemplateService', '$stateParams', 'ContentDataService', 'StudioSetupService', 'Notification', 'ContentUtil',
    function($scope,   $state,   ContentTemplateService,   $stateParams,   ContentDataService,   StudioSetupService,   Notification,   ContentUtil) {

		$scope.containerDef = ContentTemplateService.getContainerDefinition(
									enablix.template, $stateParams.subContainerQId);
		
		$scope.$stateParams = $stateParams;
		
		/*ContentDataService.getContentRecordData(enablix.templateId, $stateParams.containerQId, $stateParams.elementIdentity, 
				function(recordData) {
					var mainCntnrDef = ContentTemplateService.getContainerDefinition(
											enablix.template, $stateParams.containerQId);
					$scope.mainContainerLabel = ContentUtil.resolveContainerInstanceLabel(mainCntnrDef, recordData);
				}, 
				function(errResp) {
					// ignore
				});*/
		
		var decorateData = function(_containerDef, _dataRecord) {
			_dataRecord.headingLabel = ContentUtil.resolveContainerInstancePortalLabel(
										_containerDef, _dataRecord);
			
			for (var i = 0; i < _containerDef.contentItem.length; i++) {
				
				var item = _containerDef.contentItem[i];
				
				if (item.type == 'DOC') {
				
					var docInstance = _dataRecord[item.id];
					if (docInstance && docInstance.identity) {
						_dataRecord.downloadDocIdentity = docInstance.identity;
					}
					
					break;
				}
			}
		}
		
		if ($stateParams.containerQId === $stateParams.subContainerQId) {
			
			ContentDataService.getContentRecordData(enablix.templateId, $stateParams.containerQId, 
					$stateParams.elementIdentity, 
					function(recordData) {
						decorateData($scope.containerDef, recordData);
						$scope.bodyData = recordData;
					},
					function(errResp) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
					});
			
		} else {
			
			ContentDataService.getContentData(enablix.templateId, $stateParams.subContainerQId, 
					$stateParams.elementIdentity,
					function(data) {
						
						if ($stateParams.type == 'single' && data && data.length > 0) {
							$scope.bodyData = data[0];
							decorateData($scope.containerDef, $scope.bodyData);
							
						} else {
							
							if (data && data.length > 0) {
								angular.forEach(data, function(dataItem) {
									decorateData($scope.containerDef, dataItem);
								});
							}
							
							$scope.bodyData = data;
						}
						
					},
					function(errResp) {
						Notification.error({message: "Error retrieving data", delay: enablix.errorMsgShowTime});
					});
		}
		
		if ($stateParams.type == 'single') {
			
			// set up headers for the view
			$scope.singleHeaders = [];
			
			angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
				
				var header = {
					"key" : containerAttr.id,
					"label" : containerAttr.label,
					"dataType" : containerAttr.type 
				};
				
				$scope.singleHeaders.push(header);
				
			});
			
		} else {
			
			$scope.multiHeaders = [];
			
			angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
				
				var header = {
					"key" : containerAttr.id,
					"label" : containerAttr.label,
					"dataType" : containerAttr.type 
				};
				
				$scope.multiHeaders.push(header);
				
			});
			
			$scope.multiCondensedViewHeaders = [];
			
			var condensedViewItems = ContentTemplateService.getPortalCondensedViewItems($scope.containerDef.qualifiedId);
			
			angular.forEach(condensedViewItems, function(viewItem) {
				
				for (var i = 0; i < $scope.containerDef.contentItem.length; i++) {
					
					var contentItem = $scope.containerDef.contentItem[i];
					
					if (contentItem.id == viewItem.id) {
						
						var header = {
								"key" : contentItem.id,
								"label" : contentItem.label,
								"dataType" : contentItem.type 
							};		
						
						$scope.multiCondensedViewHeaders.push(header);
						break;
					}
					
				}
				
			});
		}
		
	}                                          
]);