enablix.studioApp.controller('ContentEditCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.$stateParams = $stateParams;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		$scope.pageHeading = "Edit " + $scope.containerDef.label;
		
		ContentDataService.getContentRecordData(enablix.templateId, containerQId, elementIdentity, 
				function(data) {
					$scope.containerData = data;
				}, 
				function(data) {
					alert('Error retrieving record data');
				});
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			ContentDataService.saveContainerData(
					enablix.templateId, containerQId, 
					null, dataToSave, 
					function(data) {
						alert("Update successfully!");
						$scope.updateCurrentNodeData(data);
						StateUpdateService.goToStudioDetail(containerQId, elementIdentity);
					}, 
					function (data) {
						alert("Error updating data");
					});
		};
	}
]);