enablix.studioApp.controller('ContentDetailCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification',
	function( $scope,   $stateParams,   ContentDataService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.$stateParams = $stateParams;
		
		$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
		
		if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
			$scope.containerDef = ContentTemplateService.getContainerDefinition(
					enablix.template, $scope.containerDef.linkContainerQId);
		}
		
		$scope.pageHeading = $scope.containerDef.label + " Information";
		
		$scope.navToEdit = function() {
			$scope.goToDetailEdit(containerQId, elementIdentity);
		}
		
		$scope.navToContentList = function(parentIdentity) {
			$scope.goToContentList(containerQId, parentIdentity);
		};
		
		ContentDataService.getContentRecordData(enablix.templateId, containerQId, elementIdentity, 
				function(data) {
					$scope.containerData = data;
				}, 
				function(data) {
					//alert('Error retrieving record data');
					Notification.error({message: "Error retrieving record data", delay: enablix.errorMsgShowTime});
				});
		
		$scope.deleteRecord = function() {
			
			ContentDataService.deleteContentData(containerQId, elementIdentity, 
				function(data) {
					Notification.primary("Deleted successfully!");
					var parentNode = $scope.getCurrentIndexNode ? $scope.getCurrentIndexNode().parentNode : null;
					$scope.postDataDelete(parentNode, elementIdentity);
				}, 
				function(data) {
					Notification.error({message: "Error deleting record", delay: enablix.errorMsgShowTime});
				});
			
		}
		
	}
]);