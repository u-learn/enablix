enablix.studioApp.controller('ContentDetailCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification) {
		
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
					//alert('Error retrieving record data');
					Notification.error({message: "Error retrieving record data", delay: null});
				});
		
	}
]);