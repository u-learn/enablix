enablix.studioApp.controller('ContentDetailCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		ContentDataService.getContentRecordData(enablix.templateId, containerQId, elementIdentity, 
				function(data) {
					$scope.containerData = data;
				}, 
				function(data) {
					alert('Error retrieving record data');
				})
		
	}
]);