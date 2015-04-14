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
		
		$scope.addChildToCurrentNode = function(childData, selectChildAsCurrent) {
			
			var childNode = ContentIndexService.addInstanceDataChild(
					$scope.contentIndex.currentNode, 
					$scope.contentIndex.currentNode.containerDef, childData);
			
			if (selectChildAsCurrent) {
				$scope.contentIndex.selectNodeLabel(childNode);
			}
			
			return childNode;
		};
		
		$scope.updateCurrentNodeData = function(updatedData) {
			ContentIndexService.updateNodeData($scope.contentIndex.currentNode, updatedData);
		};
		
		$scope.selectChildOfCurrent = function(childIdentity) {
			
			if ($scope.contentIndex.currentNode.children) {
			
				var childrenNodes = $scope.contentIndex.currentNode.children;
				
				for (var i = 0; i < childrenNodes.length; i++) {
					var child = childrenNodes[i];
					if (child.elementIdentity == childIdentity) {
						$scope.contentIndex.selectNodeLabel(child);
						break;
					}
				}
			}
		}
		
	}]);