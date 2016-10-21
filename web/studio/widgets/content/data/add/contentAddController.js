enablix.studioApp.controller('ContentAddCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 'QIdUtil',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification,   QIdUtil) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $scope.parentIdentity = $stateParams.parentIdentity;
		
		// Add state params to scope for easy access
		$scope.$stateParams = $stateParams;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		var containerLabel = $scope.containerDef.label;
		
		var linkContainerValue = null;
		var linkContentItemId = null;
		
		if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
			
			linkContentItemId = $scope.containerDef.linkContentItemId;
			linkContainerValue = [{id: parentIdentity}];
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
					enablix.template, $scope.containerDef.linkContainerQId);
			
			if (isNullOrUndefined(containerLabel)) {
				containerLabel = $scope.containerDef.label;
			}
		}
		
		$scope.parentRecord = {};
		
		if (!isNullOrUndefined($stateParams.parentIdentity)) {
		
			var parentQId = QIdUtil.getParentQId(containerQId);
			
			ContentDataService.getContentRecordData(enablix.templateId, parentQId, parentIdentity, null,
				function(data) {
			
					$scope.parentRecord = data;
					
					if (!isNullOrUndefined($scope.parentRecord)) {
						
						var inheritableItems = ContentTemplateService.getInheritableItems($scope.containerDef.qualifiedId, parentQId);
						
						angular.forEach(inheritableItems, function(inheritableItem) {
							$scope.containerData[inheritableItem.contentItemId] = $scope.parentRecord[inheritableItem.parentContentItemId];
						});
					}
				});
		}
		
		$scope.pageHeading = "Add " + containerLabel;
		
		$scope.containerData = {};
		
		if (!isNullOrUndefined(linkContainerValue)) {
			$scope.containerData[linkContentItemId] = linkContainerValue;
		}
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			ContentDataService.saveContainerData(
					enablix.templateId, containerQId, 
					parentIdentity, dataToSave, 
					function(data) {
						//alert("Saved successfully!");
						Notification.primary("Saved successfully!");
						$scope.postDataSave(data);
					}, 
					function (data) {
						//alert("Error saving data");
						Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
					});
		};
		
		$scope.cancelOperation = function() {
			$scope.addCancelled();
		};
		
	}
]);