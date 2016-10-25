enablix.studioApp.controller('ContentAddCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentOperInitService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 'QIdUtil',
	function( $scope,   $stateParams,   ContentDataService,   ContentOperInitService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification,   QIdUtil) {
		
		var containerQId = $stateParams.containerQId;
		var parentIdentity = $scope.parentIdentity = $stateParams.parentIdentity;
		
		// Add state params to scope for easy access
		$scope.$stateParams = $stateParams;
		
		ContentOperInitService.initAddContentOper($scope, containerQId, parentIdentity);
		
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