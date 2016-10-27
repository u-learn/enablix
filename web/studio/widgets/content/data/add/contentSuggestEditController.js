enablix.studioApp.controller('ContentSuggestEditCtrl', 
			['$scope', '$stateParams', 'ContentApprovalService', 'ContentTemplateService', 'Notification', 'StateUpdateService',
	function( $scope,   $stateParams,   ContentApprovalService,   ContentTemplateService,   Notification,   StateUpdateService) {
		
		$scope.temporaryFileUpload = true;
		$scope.headingCancelLabel = "Back";
		
		$scope.approvalRecord = {};
		
		ContentApprovalService.getContent($stateParams.refObjectIdentity, function(record) {
			
			$scope.approvalRecord = record;
			$scope.containerData = record.objectRef.data;
			
			$scope.pageHeading = "Edit " + record.objectRef.contentTitle;
			$scope.$stateParams = $stateParams;
			
			var containerQId = record.objectRef.contentQId;
			
			$scope.containerDef = ContentTemplateService.getContainerDefinition(enablix.template, containerQId);
			if (!isNullOrUndefined($scope.containerDef.linkContainerQId)) {
				$scope.containerDef = ContentTemplateService.getContainerDefinition(
						enablix.template, $scope.containerDef.linkContainerQId);
			}
			
		});
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			ContentApprovalService.editContent($stateParams.refObjectIdentity, $scope.approvalRecord.objectRef.contentQId, 
					$scope.approvalRecord.objectRef.parentIdentity, $scope.notes, dataToSave, 
					function(data) {
						Notification.primary("Saved successfully!");
						StateUpdateService.goToPreviousState();
					}, 
					function (data) {
						Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
					});
		};
		
		$scope.cancelOperation = function() {
			StateUpdateService.goToPreviousState();
		};
		
	}
]);