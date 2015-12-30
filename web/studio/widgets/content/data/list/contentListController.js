enablix.studioApp.controller('ContentListCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $stateParams.parentIdentity;
		
		$scope.containerDef = {};
		$scope.listHeaders = [];
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		var hiddenContentItemIds = [];
		
		angular.forEach(ContentTemplateService.getContainerListViewHiddenItems(containerQId), function(hiddenContentItem) {
			hiddenContentItemIds.push(hiddenContentItem.id);
		});
		
		$scope.navToEdit = function(elementIdentity) {
			$scope.goToDetailEdit(containerQId, elementIdentity);
		}
		
		$scope.deleteRecord = function(elementIdentity) {
			
			ContentDataService.deleteContentData(containerQId, elementIdentity, 
				function(data) {
					Notification.primary("Deleted successfully!");
					var parentNode = $scope.getCurrentIndexNode ? $scope.getCurrentIndexNode() : null;
					if (parentNode && parentNode.children && parentNode.children.length > 0) {
						$scope.postDataDelete(parentNode, elementIdentity);
					}
					fetchData();
				}, 
				function(data) {
					Notification.error({message: "Error deleting record", delay: enablix.errorMsgShowTime});
				});
			
		}
		
		angular.forEach($scope.containerDef.contentItem, function(containerAttr) {
			
			if (!hiddenContentItemIds.contains(containerAttr.id)) {
				
				var header = {
					"key" : containerAttr.id,
					"desc" : containerAttr.label,
					"dataType" : containerAttr.type 
				};
				
				$scope.listHeaders.push(header);
			}
		});
		
		var decorateData = function(_containerDef, _dataRecord) {
			
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
		
		var fetchData = function() {
			ContentDataService.getContentData(enablix.templateId, containerQId, parentIdentity, 
				function(data) {
					$scope.listData = data;
					
					angular.forEach($scope.listData, function(item) {
						decorateData($scope.containerDef, item);
					});
				}, 
				function(data) {
					//alert('Error retrieving list data');
					Notification.error({message: "Error retrieving list data", delay: enablix.errorMsgShowTime});
				});
		}
		
		fetchData();
		
		$scope.pageHeading = $scope.containerDef.label;
		
		$scope.navToAddContent = function() {
			$scope.goToAddContent(containerQId, parentIdentity);
		};
		
		$scope.navToContentDetail = function(elementIdentity) {
			$scope.goToContentDetail(containerQId, elementIdentity);
		};
		
	}
]);