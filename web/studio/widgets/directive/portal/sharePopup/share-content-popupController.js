enablix.studioApp.controller('ShareController', [
             '$scope', '$stateParams', '$modalInstance', 'containerQId', 'contentIdentity', 'ContentShareService', 'Notification',
	function( $scope,   $stateParams,   $modalInstance,   containerQId,   contentIdentity,   ContentShareService,   Notification) {

		$scope.shareContent = function() {
			
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
			$modalInstance.close();
		}

}]);