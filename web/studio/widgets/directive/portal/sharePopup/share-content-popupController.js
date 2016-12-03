enablix.studioApp.controller('ShareController', [
             '$scope', '$stateParams', '$modalInstance', '$timeout', 'containerQId', 'contentIdentity', 'ContentShareService', 'Notification',
	function( $scope,   $stateParams,   $modalInstance,   $timeout,   containerQId,   contentIdentity,   ContentShareService,   Notification) {

		$scope.shareContent = function() {
			
			$scope.shareForm.emailid.$touched = true;
			if ($scope.shareForm.$invalid) {
				return;
			}
			
			ContentShareService.shareContent(containerQId, contentIdentity, $scope.emailid, function(sent) {
				if(sent) {
					Notification.primary({message: "Mail sent successfully", delay: enablix.errorMsgShowTime});
				} else {
					Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime});
				}
			}, function(fail) {
				Notification.error({message: "Error sending mail ", delay: enablix.errorMsgShowTime});
			});
			
			$modalInstance.close();
		}

		$scope.close = function() {
			$modalInstance.dismiss('cancel');
		}

}]);