enablix.studioApp.controller('ContentSuggestEditCtrl', 
			['$scope', '$stateParams', 'ContentApprovalService', 'ContentTemplateService', 'Notification', 'StateUpdateService', 'isDraftPage',
	function( $scope,   $stateParams,   ContentApprovalService,   ContentTemplateService,   Notification,   StateUpdateService,   isDraftPage) {
		
		$scope.temporaryFileUpload = true;
		
		$scope.approvalRecord = {};
		$scope.draftEdit = isDraftPage;
		
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
		
		$scope.saveContentData = function(saveAsDraft) {
			
			// if draft page and save as draft, then edit action
			// if draft page and publish, then if content approval required then submit action else publish action
			// if not draft page
			var dataToSave = $scope.containerData; 
			
			if ($scope.draftEdit && !saveAsDraft) {
				
				ContentApprovalService.publishContent($stateParams.refObjectIdentity, $scope.approvalRecord.objectRef.contentQId, 
						$scope.approvalRecord.objectRef.parentIdentity, $scope.notes, dataToSave, 
						function(data) {
							Notification.primary("Saved successfully!");
							StateUpdateService.goToPreviousState();
						}, 
						function (data) {
							Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
						});
				
			} else {
				
				ContentApprovalService.editContent($stateParams.refObjectIdentity, $scope.approvalRecord.objectRef.contentQId, 
						$scope.approvalRecord.objectRef.parentIdentity, $scope.notes, dataToSave, 
						function(data) {
							Notification.primary("Saved successfully!");
							StateUpdateService.goToPreviousState();
						}, 
						function (data) {
							Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
						});
			}
		};
		
		$scope.headingCancelLabel = "Back";
		$scope.saveBtnLabel = isDraftPage ? "Publish" : "Save";
		$scope.cancelOperation = function() {
			StateUpdateService.goBack();
		};
		
	}
]);