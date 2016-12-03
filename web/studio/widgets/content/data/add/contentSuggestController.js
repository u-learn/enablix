enablix.studioApp.controller('ContentSuggestCtrl', 
			['$scope', 'containerQId', 'contentIdentity', 'parentIdentity', 'PubSub', 'ContentApprovalService', 'ContentOperInitService', 'ContentDataService', 'Notification', 'QIdUtil', '$modalInstance',
	function( $scope,   containerQId,   contentIdentity,   parentIdentity,   PubSub,   ContentApprovalService,   ContentOperInitService,   ContentDataService,   Notification,   QIdUtil,   $modalInstance) {
		
		var editOperation = !isNullOrUndefined(contentIdentity);
		if (editOperation) {
			// edit suggestion
			ContentOperInitService.initEditContentOper($scope, containerQId, contentIdentity);
		} else {
			// add suggestion
			ContentOperInitService.initAddContentOper($scope, containerQId, parentIdentity);
		}
		
		$scope.approvalWFRequired = ContentApprovalService.isApprovalWFRequired();
		$scope.temporaryFileUpload = $scope.approvalWFRequired;
		$scope.headingCancelLabel = "x";
		
		$scope.saveContentData = function() {
			
			var dataToSave = $scope.containerData; 
			
			if ($scope.approvalWFRequired) {
				
				ContentApprovalService.submitContent(
					containerQId, parentIdentity, $scope.notes, dataToSave, 
					function(data) {
						Notification.primary("Saved successfully!");
						$modalInstance.close();
					}, 
					function (data) {
						Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
					});
				
			} else {
				
				if (editOperation) {
					
					ContentDataService.saveContainerData(
						enablix.templateId, containerQId, 
						null, dataToSave, 
						function(data) {
							//alert("Update successfully!");
							Notification.primary("Updated successfully!");
							PubSub.publish(ContentDataService.contentChangeEventId(containerQId), dataToSave);
							$modalInstance.close();
						}, 
						function (data) {
							//alert("Error updating data");
							Notification.error({message: "Error updating data", delay: enablix.errorMsgShowTime});
						});
					
				} else {
					
					ContentDataService.saveContainerData(
						enablix.templateId, containerQId, 
						parentIdentity, dataToSave, 
						function(data) {
							Notification.primary("Saved successfully!");
							PubSub.publish(ContentDataService.contentChangeEventId(containerQId), dataToSave);
							$modalInstance.close();
						}, 
						function (data) {
							Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
						});
				}
			}
		};
		
		$scope.cancelOperation = function() {
			$modalInstance.close();
		};
		
	}
]);