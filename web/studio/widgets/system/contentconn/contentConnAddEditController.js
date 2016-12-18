enablix.studioApp.controller('ContentConnAddEditController', 
			['$scope', '$state', '$stateParams', 'ContentConnectionService', 'StateUpdateService', 'Notification', 'ContentTemplateService', 'ContentDataService',
	function( $scope,   $state,   $stateParams,   ContentConnectionService,   StateUpdateService,   Notification,   ContentTemplateService,   ContentDataService) {
	
		$scope.isConnContextCollapse = true;
		$scope.readOnly = $state.includes("system.contentconndetail");
		$scope.categoryLabel = ContentConnectionService.getLabelForCategory($stateParams.category);
		
		$scope.contentConnection = {};
		var contentConnIdentity = $stateParams.connectionIdentity;
		$scope.addOperation = isNullOrUndefined(contentConnIdentity);
		
		$scope.contextContainers = ContentTemplateService.getContentConnectionContextContainers();
		
		if (!$scope.addOperation) {
			
			$scope.pageHeading = $scope.categoryLabel + " Mapping"
			
			ContentConnectionService.getContentConnection(contentConnIdentity,
				function(data) {
					
					$scope.contentConnection = data;
					$scope.contentConnectionContainerName = $scope.getContainerName(data.contentQId);
					
					initConnectionContextData();
					initContentItemData($scope.contentConnection.contentQId, false);
					
				}, function(data) {
					Notification.error({message: "Error loading content mapping data", delay: enablix.errorMsgShowTime});
				});
			
		} else {
			
			$scope.pageHeading = "Add " + $scope.categoryLabel + " Mapping";
			
			$scope.contentConnection.connectionContext = {
				contextAttributes: []
			};
			
			initConnectionContextData();
			
		}
		
		$scope.containerBusinessCategory = ContentConnectionService.getBusinessCategory($stateParams.category);
		
		// set up master data for content source
		$scope.refDataContainers = ContentTemplateService.getRefDataContainers();
		$scope.refDataContainers.sort(sortByLabel);
		
		// set up master data for content connection (container to be connected)
		$scope.connectContainers = ContentTemplateService.getContainersByBusinessCategory($scope.containerBusinessCategory);
		$scope.connectContainers.sort(sortByLabel);

		$scope.chunkedConnectContainers = chunkArray($scope.connectContainers, 3);

		// set up master data for content connection context
		$scope.contextContainerQIdToCntnrMap = {};
		
		var contextCntnrIndx = 0;
		angular.forEach($scope.contextContainers, function(contextCntnr) {
			
			var _containerQId = contextCntnr.qualifiedId;
			$scope.contextContainerQIdToCntnrMap[_containerQId] = contextCntnr;
			
			var titleAttrId = getTitleAttrId(_containerQId);
			
			ContentDataService.getContentData(enablix.templateId, _containerQId, null, 
					function(data) {
						contextCntnr.data = [];
						angular.forEach(data, function(dataItem) {
							contextCntnr.data.push({
								id: dataItem.identity,
								title: dataItem[titleAttrId]
							});
						});
						
					}, 
					function(data) {
						//alert('Error retrieving list data');
						Notification.error({message: "Error retrieving reference data", delay: enablix.errorMsgShowTime});
					});
			
			contextCntnrIndx++;
			
		});
		
		$scope.getContainerName = function(_containerQId) {
			var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, _containerQId);
			return containerDef ? containerDef.label : ""; 
		};
		
		function sortByLabel(c1, c2) {
			return c1.label == c2.label ? 0 : (c1.label < c2.label ? -1 : 1);
		}
		
		function getTitleAttrId(_containerQId) {
			var titleAttrId = ContentTemplateService.getContainerLabelAttrId(enablix.template, _containerQId);
			if (isNullOrUndefined(titleAttrId)) {
				var containerDef = ContentTemplateService.getConcreteContainerDefinition(enablix.template, _containerQId);
				if (!isNullOrUndefined(containerDef) && containerDef.contentItem && containerDef.contentItem.length > 0) {
					titleAttrId = containerDef.contentItem[0].id;
				}
			}
			return titleAttrId;
		}

		function initConnectionContextData() {
			
			if (isNullOrUndefined($scope.contentConnection.connectionContext.contextAttributes)) {
				$scope.contentConnection.connectionContext.contextAttributes = [];
			}
			
			var contextAttrs = $scope.contentConnection.connectionContext.contextAttributes;
			
			angular.forEach($scope.contextContainers, function(contextCntnr) {
				
				var contextAttr = null;
				
				for (var i = 0; i < contextAttrs.length; i++) {
					if (contextAttrs[i].attributeQId === contextCntnr.qualifiedId) {
						contextAttr = contextAttrs[i];
						break;
					}
				}
				
				if (contextAttr == null) {
					contextAttr = {
							attributeQId: contextCntnr.qualifiedId,
							attributeValue: []
					};
					contextAttrs.push(contextAttr);
				}
			});
			
		}
		
		$scope.contentItemIdentityMap = {};
		
		function initContentItemData(_contentQId, _initNewConnections) {
			
			var _containerQId = _contentQId;
			var titleAttrId = getTitleAttrId(_containerQId);
			
			if (_initNewConnections) {
				$scope.contentConnection.connections = [];
			}
			
			ContentDataService.getContentData(enablix.templateId, _containerQId, null, 
					function(data) {
				
						angular.forEach(data, function(dataItem) {
							
							if (_initNewConnections) { 
								$scope.contentConnection.connections.push(
									{
										contentValue: dataItem.identity,
										connectedContainers: []
									});
							}
							
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
		}
		
		$scope.onContentTypeSelected = function() {
			initContentItemData($scope.contentConnection.contentQId, true);
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
			
			$scope.contentConnection.connectedContainerCategory = $scope.containerBusinessCategory;
			
			if (isNullOrUndefined($scope.contentConnection.tags)) {
				$scope.contentConnection.tags = [];
			}
			
			var tags = ["usage:correlation", "category:" + $stateParams.category];

			angular.forEach(tags, function(tag) {
				if (!$scope.contentConnection.tags.contains(tag)) {
					$scope.contentConnection.tags.push(tag);
				}
			});
			
			ContentConnectionService.saveContentConnection($scope.contentConnection, 
				function(result) {
					Notification.primary("Saved successfully!");
					StateUpdateService.goToContentConnDetail($stateParams.category, result.payload.identity);
					
				}, function(errorData) {
					Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
				});
		};
		
		$scope.editContentConnection = function() {
			StateUpdateService.goToContentConnEdit($stateParams.category, contentConnIdentity);
		}
		
		$scope.cancelOperation = function() {
			if ($scope.addOperation) {
				StateUpdateService.goToContentConnList($stateParams.category);
			} else {
				StateUpdateService.goToContentConnDetail($stateParams.category, contentConnIdentity);
			}
		}
		
		$scope.deleteContentConn = function() {
			
			ContentConnectionService.deleteContentConnection(contentConnIdentity, function() {
				
				Notification.primary("Deleted successfully!");
				StateUpdateService.goToContentConnList($stateParams.category);
				
			}, function(errorData) {
				Notification.error({message: "Error deleting record. Please try later.", delay: enablix.errorMsgShowTime});
			});
		}
		
	}
]);			
