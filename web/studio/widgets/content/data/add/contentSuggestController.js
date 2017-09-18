enablix.studioApp.controller('ContentSuggestCtrl', 
			['$scope', 'containerQId', 'contentIdentity', 'parentIdentity', 'PubSub', 'ContentApprovalService', 'ContentOperInitService', 'ContentDataService', 'Notification', 'QIdUtil', '$modalInstance',
	function( $scope,   containerQId,   contentIdentity,   parentIdentity,   PubSub,   ContentApprovalService,   ContentOperInitService,   ContentDataService,   Notification,   QIdUtil,   $modalInstance) {
		
		$scope.editOperation = !isNullOrUndefined(contentIdentity);
		if ($scope.editOperation) {
			// edit suggestion
			ContentOperInitService.initEditContentOper($scope, containerQId, contentIdentity);
		} else {
			// add suggestion
			$scope.addOperation = true;
			ContentOperInitService.initAddContentOper($scope, containerQId, parentIdentity);
		}
		
		$scope.approvalWFRequired = ContentApprovalService.isApprovalWFRequired();
		$scope.temporaryFileUpload = $scope.approvalWFRequired;
		$scope.headingCancelLabel = "x";
		
		$scope.saveContentData = function(_saveAsDraft) {
			
			var dataToSave = $scope.containerData; 
			
			if ($scope.approvalWFRequired || (!$scope.editOperation && _saveAsDraft)) {
				
				ContentApprovalService.submitContent(
					containerQId, parentIdentity, $scope.notes, dataToSave, 
					function(data) {
						Notification.primary("Saved successfully!");
						$modalInstance.close();
					}, 
					function (data) {
						Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
					}, _saveAsDraft);
				
			} else {
				
				if ($scope.editOperation) {
					
					ContentDataService.saveContainerData(
						enablix.templateId, containerQId, 
						null, dataToSave, 
						function(data) {
							//alert("Update successfully!");
							Notification.primary("Updated successfully!");
							PubSub.publish(ContentDataService.contentChangeEventId(containerQId), data);
							$modalInstance.close();
						}, 
						function (data) {
							if (data.qualityAlerts) {
								$scope.qualityAlerts = data;
							} else {
								Notification.error({message: "Error updating data", delay: enablix.errorMsgShowTime});
							}
						});
					
				} else {
					
					ContentDataService.saveContainerData(
						enablix.templateId, containerQId, 
						parentIdentity, dataToSave, 
						function(data) {
							Notification.primary("Saved successfully!");
							PubSub.publish(ContentDataService.contentChangeEventId(containerQId), data);
							$modalInstance.close();
						}, 
						function (data) {
							if (data.qualityAlerts) {
								$scope.qualityAlerts = data;
							} else {
								Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
							}
						});
				}
			}
		};
		
		$scope.cancelOperation = function() {
			$modalInstance.close();
		};
		
	}
]);