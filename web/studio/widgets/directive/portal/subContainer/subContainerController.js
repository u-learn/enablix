enablix.studioApp.controller('PortalSubContainerCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'ContentTemplateService', 'ContentDataService', 'ContentUtil', 'Notification',
    function ($scope,   StateUpdateService,   $stateParams,   ContentTemplateService,   ContentDataService,   ContentUtil,   Notification) {
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(
						enablix.template, $scope.subContainerQId);

		$scope.navigableHeader = !isNullOrUndefined($scope.navContentData);
		
		if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
					enablix.template, $scope.containerDef.linkContainerQId);
		}
		
		$scope.$stateParams = $stateParams;
		
		var decorateData = function(_containerDef, _dataRecord) {
			_dataRecord.headingLabel = ContentUtil.resolveContainerInstancePortalLabel(
									_containerDef, _dataRecord);
			_dataRecord.containerId = _containerDef.id;
			_dataRecord.containerQId = _containerDef.qualifiedId;
			_dataRecord.hasSubContainers = !isNullOrUndefined(_containerDef.container)
											&& _containerDef.container.length > 0;
		
			ContentUtil.decorateData(_containerDef, _dataRecord, true);
		}
		
		$scope.toggleContainer = function($event) {
			var elem = $event.currentTarget;
			if ($event.target.nodeName != 'A' ) {
				$(elem).toggleClass('active');
				$(elem).next().slideToggle('fast');
			}
		}
		
		$scope.toggleContainerItem = function($event, itemId) {
			var elem = $event.currentTarget;
			$(elem).toggleClass('active');
			$('#' + itemId).slideToggle('fast');
			return false;
		}
		
		$scope.navToItemDetail = function(_containerQId, _contentIdentity) {
			StateUpdateService.goToPortalContainerBody(
					_containerQId, _contentIdentity, 'single', _containerQId);
		}
		
		if ($stateParams.containerQId === $scope.subContainerQId 
				|| !isNullOrUndefined($scope.elementIdentity)) {
		
			var elemIdentity = isNullOrUndefined($scope.elementIdentity) ? $stateParams.elementIdentity
									: $scope.elementIdentity;
								
				var cntnrQId = isNullOrUndefined($stateParams.containerQId) ? $scope.subContainerQId 
									: $stateParams.containerQId;
				
			ContentDataService.getContentRecordData(enablix.templateId, cntnrQId, elemIdentity,  
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
