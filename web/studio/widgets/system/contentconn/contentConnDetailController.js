enablix.studioApp.controller('ContentConnDetailController', 
			['$scope', '$state', '$stateParams', 'ContentConnectionService', 'StateUpdateService', 'Notification',
	function( $scope,   $state,   $stateParams,   ContentConnectionService,   StateUpdateService,   Notification) {
	
		$scope.contentConnection = {};
		var contentConnIdentity = $stateParams.connectionIdentity;
		
		ContentConnectionService.getContentConnection(contentConnIdentity,
			function(data) {
				$scope.contentConnection = data;
				
			}, function(data) {
				Notification.error({message: "Error loading content mapping data", delay: enablix.errorMsgShowTime});
			});
		
	}
]);			
