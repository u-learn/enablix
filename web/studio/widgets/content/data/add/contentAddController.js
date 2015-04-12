enablix.studioApp.controller('ContentAddCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $stateParams.parentIdentity;
		
		// Add state params to scope for easy access
		$scope.$stateParams = $stateParams;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		$scope.pageHeading = "Add " + $scope.containerDef.label;
		
		$scope.containerData = {};
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			ContentDataService.saveContainerData(
					enablix.templateId, containerQId, 
					parentIdentity, dataToSave, 
					function(data) {
						alert("Saved successfully!");
						$scope.addChildToCurrentNode(data);
						//StateUpdateService.goToStudioList(containerQId, parentIdentity);
					}, 
					function (data) {
						alert("Error saving data");
					});
		};
	}
]);