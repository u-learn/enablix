enablix.studioApp.controller('contentIndexCtrl', 
			['$scope', '$state', 'ContentIndexService', 'StateUpdateService', 'Notification', 
    function( $scope,   $state,   ContentIndexService,   StateUpdateService,   Notification) {
	
		ContentIndexService.getContentIndexData(enablix.templateId, function(data) {
	    	$scope.indexData = data; 
	    }, function(data) {
	    	//alert("Error fetching content index");
	    	Notification.error({message: "Error fetching content index", delay: enablix.errorMsgShowTime});
	    });
	    
		$scope.contentIndex = $scope.contentIndex || {};
		
		var moveToNodeDetail = function(selectedNode) {
			
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
		
		$scope.contentIndex.selectNodeCallback = moveToNodeDetail;
		
		$scope.goToDetailEdit = function(containerQId, elementIdentity) {
			StateUpdateService.goToStudioEdit(containerQId, elementIdentity);
		}
		
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
		
		$scope.postDataUpdate = function(data) {
			$scope.updateCurrentNodeData(data);
			moveToNodeDetail($scope.contentIndex.currentNode);
		}
		
		$scope.updateCurrentNodeData = function(updatedData) {
			ContentIndexService.updateNodeData($scope.contentIndex.currentNode, updatedData);
		};
		
		$scope.selectChildOfCurrent = function(containerQId, childIdentity) {
			
			if ($scope.contentIndex.currentNode.children) {
			
				var childrenNodes = $scope.contentIndex.currentNode.children;
				
				if (childrenNodes.length > 0) {
					
					for (var i = 0; i < childrenNodes.length; i++) {
						var child = childrenNodes[i];
						if (child.elementIdentity == childIdentity) {
							$scope.contentIndex.selectNodeLabel(child);
							break;
						}
					}
					
				} else {
					StateUpdateService.goToStudioDetail(containerQId, childIdentity);
				}
			}
		};
		
		$scope.goToContentDetail = function(containerQId, elementIdentity) {
			$scope.selectChildOfCurrent(containerQId, elementIdentity);
		};
		
	}]);