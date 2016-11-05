enablix.studioApp.controller('ContentEditCtrl', 
			['$scope', '$stateParams', 'ContentDataService', 'ContentOperInitService', 'ContentTemplateService', 'StateUpdateService', 'StudioSetupService', 'Notification', 'ContentApprovalService', 'ConfirmationModalWindow', 
	function( $scope,   $stateParams,   ContentDataService,   ContentOperInitService,   ContentTemplateService,   StateUpdateService,   StudioSetupService,   Notification,   ContentApprovalService,   ConfirmationModalWindow) {
		
		var containerQId = $stateParams.containerQId;
		var elementIdentity = $stateParams.elementIdentity;
		
		$scope.$stateParams = $stateParams;
		
		ContentOperInitService.initEditContentOper($scope, containerQId, elementIdentity);
		
		$scope.pendingEditRequests = null;
		
		ContentApprovalService.getPendingRequestsForContent(elementIdentity, function(data) {
			$scope.pendingEditRequests = data.content;
		});
		
		$scope.saveContentData = function() {
			
			if ($scope.pendingEditRequests && $scope.pendingEditRequests.length > 0) {
				
				var confirmModal = ConfirmationModalWindow.showWindow("Confirm", 
						"There are existing edit requests for this content which will be rejected. Do you want to continue?", 
						"Proceed", "Cancel");
				
				confirmModal.result.then(function(confirmed) {
					
					if (confirmed) {
						
						angular.forEach($scope.pendingEditRequests, function(record) {
							ContentApprovalService.rejectContent(record.objectRef.identity, "", function(data) {
								// do nothing
							});
						});
						
						proceedWithSave();
					}
				});
				
			} else {
				proceedWithSave();
			}
			
		};
		
		var proceedWithSave = function() {
			
			var dataToSave = $scope.containerData;
			
			ContentDataService.saveContainerData(
					enablix.templateId, containerQId, 
					null, dataToSave, 
					function(data) {
						//alert("Update successfully!");
						Notification.primary("Updated successfully!");
						$scope.postDataUpdate(data);
					}, 
					function (data) {
						//alert("Error updating data");
						Notification.error({message: "Error updating data", delay: enablix.errorMsgShowTime});
					});
			
		}
		
		$scope.cancelOperation = function() {
			$scope.updateCancelled($scope.containerData);
		};
	}
]);