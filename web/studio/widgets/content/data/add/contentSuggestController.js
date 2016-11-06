enablix.studioApp.controller('ContentSuggestCtrl', 
			['$scope', 'containerQId', 'contentIdentity', 'parentIdentity', 'ContentApprovalService', 'ContentOperInitService', 'ContentTemplateService', 'Notification', 'QIdUtil', '$modalInstance',
	function( $scope,   containerQId,   contentIdentity,   parentIdentity,   ContentApprovalService,   ContentOperInitService,   ContentTemplateService,   Notification,   QIdUtil,   $modalInstance) {
		
		if (!isNullOrUndefined(contentIdentity)) {
			// edit suggestion
			ContentOperInitService.initEditContentOper($scope, containerQId, contentIdentity);
		} else {
			// add suggestion
			ContentOperInitService.initAddContentOper($scope, containerQId, parentIdentity);
		}
		
		$scope.temporaryFileUpload = true;
		$scope.headingCancelLabel = "x";
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			ContentApprovalService.submitContent(
					containerQId, parentIdentity, $scope.notes, dataToSave, 
					function(data) {
						Notification.primary("Saved successfully!");
						$modalInstance.close();
					}, 
					function (data) {
						Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
					});
		};
		
		$scope.cancelOperation = function() {
			$modalInstance.close();
		};
		
	}
]);