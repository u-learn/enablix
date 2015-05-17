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
		
		ContentDataService.getContentData(enablix.templateId, containerQId, parentIdentity, 
				function(data) {
					$scope.listData = data;
				}, 
				function(data) {
					//alert('Error retrieving list data');
					Notification.error({message: "Error retrieving list data", delay: enablix.errorMsgShowTime});
				});
		
		$scope.pageHeading = $scope.containerDef.label;
		
		$scope.navToAddContent = function() {
			$scope.goToAddContent(containerQId, parentIdentity);
		};
		
		$scope.navToContentDetail = function(elementIdentity) {
			$scope.goToContentDetail(containerQId, elementIdentity);
		};
		
	}
]);