enablix.studioApp.controller('ContentConnAddEditController', 
			['$scope', '$state', '$stateParams', 'ContentConnectionService', 'StateUpdateService', 'Notification', 'ContentTemplateService', 'ContentDataService',
	function( $scope,   $state,   $stateParams,   ContentConnectionService,   StateUpdateService,   Notification,   ContentTemplateService,   ContentDataService) {
	
		$scope.readOnly = $state.includes("system.contentconndetail");
		
		$scope.contentConnection = {};
		var contentConnIdentity = $stateParams.connectionIdentity;
		$scope.addOperation = isNullOrUndefined(contentConnIdentity);
		
		if (!$scope.addOperation) {
			
			if ($scope.readOnly) {
				$scope.pageHeading = "Content Type Mapping"
			} else {
				$scope.pageHeading = "Content Type Mapping"
			}
			
			ContentConnectionService.getContentConnection(contentConnIdentity,
				function(data) {
					$scope.contentConnection = data;
					$scope.contentConnectionContainerName = $scope.getContainerName(data.containerQId);
				}, function(data) {
					Notification.error({message: "Error loading content mapping data", delay: enablix.errorMsgShowTime});
				});
			
		} else {
			$scope.pageHeading = "Add Content Type Mapping"; 
		}
		
		$scope.containerBusinessCategory = "BUSINESS_CONTENT";
		
		$scope.refDataContainers = ContentTemplateService.getRefDataContainers();
		$scope.refDataContainers.sort(sortByLabel);
		
		$scope.connectContainers = ContentTemplateService.getContainersByBusinessCategory($scope.containerBusinessCategory);
		$scope.connectContainers.sort(sortByLabel);

		function sortByLabel(c1, c2) {
			return c1.label == c2.label ? 0 : (c1.label < c2.label ? -1 : 1);
		}
		
		$scope.getContainerName = function(_containerQId) {
			var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, _containerQId);
			return containerDef ? containerDef.label : ""; 
		};
		
		$scope.chunkedConnectContainers = chunkArray($scope.connectContainers, 3);
		
		$scope.refDataList = [];
		
		$scope.onContentTypeSelected = function() {
			
			var _containerQId = $scope.contentConnection.containerQId;
			
			var titleAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, _containerQId);
			if (isNullOrUndefined(titleAttrId)) {
				var containerDef = ContentTemplateService.getConcreteContainerDefinition(enablix.template, _containerQId);
				if (!isNullOrUndefined(containerDef) && containerDef.contentItem && containerDef.contentItem.length > 0) {
					titleAttrId = containerDef.contentItem[0].id;
				}
			}
			
			$scope.contentConnection.connections = [];
			$scope.contentItemIdentityMap = {};
			
			ContentDataService.getContentData(enablix.templateId, _containerQId, null, 
					function(data) {
				
						angular.forEach(data, function(dataItem) {
							$scope.contentConnection.connections.push(
								{
									contentValue: dataItem.identity,
									connectedContainers: []
								});
							
							$scope.contentItemIdentityMap[dataItem.identity] = {
								id: dataItem.identity,
								title: dataItem[titleAttrId]
							};
						});
						
					}, 
					function(data) {
						//alert('Error retrieving list data');
						Notification.error({message: "Error retrieving reference data", delay: enablix.errorMsgShowTime});
					});
		};
		
		$scope.toggleSelection = function(_contentItemConnection, _containerQId) {
			
			var idx = _contentItemConnection.connectedContainers.indexOf(_containerQId);

		    // is currently selected
		    if (idx > -1) {
		    	_contentItemConnection.connectedContainers.splice(idx, 1);
		    }

		    // is newly selected
		    else {
		    	_contentItemConnection.connectedContainers.push(_containerQId);
		    }
		};
		
		$scope.saveContentConnection = function() {
			
			console.log($scope.contentConnection.connections.length);
			
			ContentConnectionService.saveContentConnection($scope.contentConnection, 
				function() {
					Notification.primary("Saved successfully!");
					$scope.readOnly = true;
					
				}, function(errorData) {
					Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
				});
		};
		
		$scope.editContentConnection = function() {
			StateUpdateService.goToContentConnEdit(contentConnIdentity);
		}
		
		$scope.cancelOperation = function() {
			if ($scope.addOperation) {
				StateUpdateService.goToContentConnList();
			} else {
				StateUpdateService.goToContentConnDetail(contentConnIdentity);
			}
		}
		
	}
]);			
