enablix.studioApp.controller('ModalPageController', 
			['$scope', '$modalInstance',
	function( $scope,   $modalInstance) {
		
		$scope.close = function() {
			$modalInstance.close();
		}
			
	}
]);