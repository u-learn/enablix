enablix.studioApp.controller('PortalSubContainerCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil) {
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(
						enablix.template, $scope.subContainerQId);

		$scope.$stateParams = $stateParams;
		
		var decorateData = function(_containerDef, _dataRecord) {
			_dataRecord.headingLabel = ContentUtil.resolveContainerInstancePortalLabel(
									_containerDef, _dataRecord);
			_dataRecord.containerId = _containerDef.id;
		
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
		
		$scope.toggleContainer = function($event) {
			var elem = $event.currentTarget;
			if ($event.target.nodeName != 'A' ) {
				$(elem).toggleClass('active');
				$(elem).next().slideToggle('fast');
			}
		}
		
		$scope.toggleContainerItem = function($event) {
			var elem = $event.currentTarget;
			$(elem).toggleClass('active');
			$($(elem).attr('href')).slideToggle('fast');
			return false;
		}
		
		if ($stateParams.containerQId === $scope.subContainerQId) {
		
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
			
			ContentDataService.getContentData(enablix.templateId, $scope.subContainerQId, 
				$stateParams.elementIdentity,
				function(data) {
					
					if ($scope.type == 'single' && data && data.length > 0) {
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
		
		if ($scope.type == 'single') {
		
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
		
		$scope.expanded = $scope.expanded || false;
		
	}]);
