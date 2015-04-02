enablix.studioApp.controller('contentIndexCtrl', 
			['$scope', '$state', 'ContentIndexService', 
    function( $scope,   $state,   ContentIndexService) {
	
		ContentIndexService.getContentIndexData(enablix.templateId, function(data) {
	    	 $scope.indexData = data; 
	    }, function(data) {
	    	alert("Error fetching content index");
	    });
	    
		$scope.contentIndex = $scope.contentIndex || {};
		
		$scope.contentIndex.selectNodeCallback = function(selectedNode) {
			
			if (selectedNode.type == 'container') {
			
				$state.go('studio.list', {'containerQId' : selectedNode.qualifiedId, 
					"parentIdentity" : selectedNode.elementIdentity});
				
			} else if (selectedNode.type == 'instance') {
				
				$state.go('studio.detail', {'containerQId' : selectedNode.qualifiedId, 
					"elementIdentity" : selectedNode.elementIdentity});
			}
			
		};
		
	}]);