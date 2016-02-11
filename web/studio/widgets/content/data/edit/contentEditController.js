enablix.studioApp.controller('ContentEditCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.$stateParams = $stateParams;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		var containerLabel = $scope.containerDef.label;
		
		if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
					enablix.template, $scope.containerDef.linkContainerQId);
			
			if (isNullOrUndefined(containerLabel)) {
				containerLabel = $scope.containerDef.label;
			}
		}
		
		$scope.pageHeading = "Edit " + containerLabel;
		
		$scope.containerData = {};
		
		ContentDataService.getContentRecordData(enablix.templateId, containerQId, elementIdentity, 
				function(data) {
					$scope.containerData = angular.copy(data);
				}, 
				function(data) {
					//alert('Error retrieving record data');
					Notification.error({message: "Error retrieving record data", delay: enablix.errorMsgShowTime});
				});
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			ContentDataService.saveContainerData(
					enablix.templateId, containerQId, 
					null, dataToSave, 
					function(data) {
						//alert("Update successfully!");
						Notification.primary("Updated successfully!");
						$scope.postDataUpdate(data);
					}, 
					function (data) {
						//alert("Error updating data");
						Notification.error({message: "Error updating data", delay: enablix.errorMsgShowTime});
					});
		};
		
		$scope.cancelOperation = function() {
			$scope.updateCancelled($scope.containerData);
		};
	}
]);