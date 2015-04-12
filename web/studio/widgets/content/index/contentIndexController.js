enablix.studioApp.controller('contentIndexCtrl', 
			['$scope', '$state', 'ContentIndexService', 'StateUpdateService', 
    function( $scope,   $state,   ContentIndexService,   StateUpdateService) {
	
		ContentIndexService.getContentIndexData(enablix.templateId, function(data) {
	    	$scope.indexData = data; 
	    }, function(data) {
	    	alert("Error fetching content index");
	    });
	    
		$scope.contentIndex = $scope.contentIndex || {};
		
		$scope.contentIndex.selectNodeCallback = function(selectedNode) {
			
			if (selectedNode.type == 'container') {
				StateUpdateService.goToStudioList(selectedNode.qualifiedId, selectedNode.elementIdentity);
				
			} else if (selectedNode.type == 'instance') {
				StateUpdateService.goToStudioDetail(selectedNode.qualifiedId, selectedNode.elementIdentity);
			}
			
		};
		
		$scope.addChildToCurrentNode = function(childData) {
			var childNode = ContentIndexService.addInstanceDataChild(
					$scope.contentIndex.currentNode, 
					$scope.contentIndex.currentNode.containerDef, childData);
			$scope.contentIndex.selectNodeLabel(childNode);
		};
		
	}]);