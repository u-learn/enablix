enablix.studioApp.controller('PortalPeersCtrl',
			['$scope', 'StateUpdateService', '$stateParams', 'PeerContentService', 'ContentTemplateService',
    function ($scope,   StateUpdateService,   $stateParams,   PeerContentService,   ContentTemplateService) {
		
		if (!isNullOrUndefined($scope.containerQId) && !isNullOrUndefined($scope.contentIdentity)) {
			PeerContentService.getPeers($scope.containerQId, $scope.contentIdentity, 
				function(peers) {
					console.log(peers);
					$scope.peers = peers;
				});
		}
		
	}]);
