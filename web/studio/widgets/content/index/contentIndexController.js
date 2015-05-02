enablix.studioApp.controller('contentIndexCtrl', 
			['$scope', '$state', 'ContentIndexService', 'StateUpdateService', 'Notification', 
    function( $scope,   $state,   ContentIndexService,   StateUpdateService,   Notification) {
	
		ContentIndexService.getContentIndexData(enablix.templateId, function(data) {
	    	$scope.indexData = data; 
	    }, function(data) {
	    	//alert("Error fetching content index");
	    	Notification.error({message: "Error fetching content index", delay: null});
	    });
	    
		$scope.contentIndex = $scope.contentIndex || {};
		
		$scope.contentIndex.selectNodeCallback = function(selectedNode) {
			
			if (selectedNode.type == 'container') {
				StateUpdateService.goToStudioList(selectedNode.qualifiedId, selectedNode.elementIdentity);
				
			} else if (selectedNode.type == 'instance') {
				StateUpdateService.goToStudioDetail(selectedNode.qualifiedId, selectedNode.elementIdentity);
				
			} else if (selectedNode.type == 'container-instance') {
				
				if (selectedNode.elementIdentity == undefined || selectedNode.elementIdentity == null) {
					StateUpdateService.goToStudioAdd(selectedNode.qualifiedId, selectedNode.parentIdentity);
				} else {
					StateUpdateService.goToStudioDetail(selectedNode.qualifiedId, selectedNode.elementIdentity);
				}
			}
			
		};
		
		$scope.postDataSave = function(data) {
			addChildToCurrentNode(data, true);
		}
		
		$scope.goToAddContent = function(containerQId, parentIdentity) {
			StateUpdateService.goToStudioAdd(containerQId, parentIdentity);
		};
		
		var addChildToCurrentNode = function(childData, selectChildAsCurrent) {
			
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
		};
		
		$scope.goToContentDetail = function(elementIdentity) {
			$scope.selectChildOfCurrent(elementIdentity);
		};
		
	}]);