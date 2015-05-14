enablix.studioApp.controller('PortalCntnrDetailCtrl', 
		   ['$scope', 'StateUpdateService', '$stateParams', 'PortalContainerIndexService', 'StudioSetupService', 
    function($scope,   StateUpdateService,   $stateParams,   PortalContainerIndexService,   StudioSetupService) {
		
		$scope.portalIndexData = PortalContainerIndexService.getIndexList($stateParams.containerQId);
		
		$scope.portalIndex = $scope.portalIndex || {};
		
		$scope.portalIndex.selectNodeCallback = function(selectedNode) {
			
			var _subCntnrType = 'multi';
			
			if ($stateParams.containerQId === selectedNode.qualifiedId
					|| (selectedNode.containerDef && selectedNode.containerDef.single)) {
				_subCntnrType = 'single';
			} 
			
			StateUpdateService.goToPortalContainerBody($stateParams.containerQId, 
					$stateParams.elementIdentity, _subCntnrType, selectedNode.qualifiedId);
		}
		
	}                                          
]);