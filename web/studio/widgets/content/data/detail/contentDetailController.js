enablix.studioApp.controller('ContentDetailCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.$stateParams = $stateParams;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		$scope.pageHeading = $scope.containerDef.label + " detail";
		
		$scope.navToEdit = function() {
			StateUpdateService.goToStudioEdit(containerQId, elementIdentity);
		}
		
		ContentDataService.getContentRecordData(enablix.templateId, containerQId, elementIdentity, 
				function(data) {
					$scope.containerData = data;
				}, 
				function(data) {
					alert('Error retrieving record data');
				});
		
	}
]);