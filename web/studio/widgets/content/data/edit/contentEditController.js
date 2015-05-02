enablix.studioApp.controller('ContentEditCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.$stateParams = $stateParams;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		$scope.pageHeading = "Edit " + $scope.containerDef.label;
		
		$scope.containerData = {};
		
		ContentDataService.getContentRecordData(enablix.templateId, containerQId, elementIdentity, 
				function(data) {
					$scope.containerData = angular.copy(data);
				}, 
				function(data) {
					//alert('Error retrieving record data');
					Notification.error({message: "Error retrieving record data", delay: null});
				});
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			ContentDataService.saveContainerData(
					enablix.templateId, containerQId, 
					null, dataToSave, 
					function(data) {
						//alert("Update successfully!");
						Notification.primary("Updated successfully!");
						$scope.postDataSave(data);
					}, 
					function (data) {
						//alert("Error updating data");
						Notification.error({message: "Error updating data", delay: null});
					});
		};
	}
]);