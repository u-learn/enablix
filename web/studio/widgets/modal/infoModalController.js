enablix.studioApp.controller('InfoModalController', 
			['$scope', '$stateParams', 'StateUpdateService', 'Notification', 'RESTService', '$modalInstance', 'modalTitle', 'modalInfo',
	function( $scope,   $stateParams,   StateUpdateService,   Notification,   RESTService,   $modalInstance,   modalTitle,   modalInfo) {
		
		$scope.modalTitle = modalTitle;
		$scope.infoText = modalInfo;
				
		$scope.close = function() {
			$modalInstance.close();
		}
	}
]);