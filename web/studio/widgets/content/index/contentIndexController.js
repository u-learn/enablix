enablix.studioApp.controller('contentIndexCtrl', 
			['$scope', '$state', 'ContentIndexService', 'StateUpdateService', 'Notification', 
    function( $scope,   $state,   ContentIndexService,   StateUpdateService,   Notification) {
	
		ContentIndexService.getContentIndexData(enablix.templateId, function(data) {
	    	
			$scope.indexData = data; 
	    	
	    	var firstNode = data[0];
	    	$scope.contentIndex.selectNodeLabel(firstNode);
	    	
	    }, function(data) {
	    	//alert("Error fetching content index");
	    	Notification.error({message: "Error fetching content index", delay: enablix.errorMsgShowTime});
	    });
	    
		$scope.contentIndex = $scope.contentIndex || {};
		
		var moveToNodeView = function(selectedNode) {
			
			if (selectedNode.type == 'container') {
				StateUpdateService.goToStudioList(selectedNode.qualifiedId, selectedNode.elementIdentity);
				
			} else if (selectedNode.type == 'instance') {
				StateUpdateService.goToStudioDetail(selectedNode.qualifiedId, selectedNode.elementIdentity);
				
			} else if (selectedNode.type == 'container-instance') {
				
				if (isNullOrUndefined(selectedNode.elementIdentity)) {
					StateUpdateService.goToStudioAdd(selectedNode.qualifiedId, selectedNode.parentIdentity);
				} else {
					StateUpdateService.goToStudioDetail(selectedNode.qualifiedId, selectedNode.elementIdentity);
				}
			}
			
			expandParentIndexNode(selectedNode);
		};
		
		var expandParentIndexNode = function(_treeNode) {
			if (!isNullOrUndefined(_treeNode)) {
				expandIndexNode(_treeNode.parentNode);
			}
		}
		
		var expandIndexNode = function(_treeNode) {
			
			if (!isNullOrUndefined(_treeNode)) {
				_treeNode.collapsed = false;
				expandIndexNode(_treeNode.parentNode);
			}
		}
		
		$scope.contentIndex.selectNodeCallback = moveToNodeView;
		
		$scope.goToDetailEdit = function(containerQId, elementIdentity) {
			StateUpdateService.goToStudioEdit(containerQId, elementIdentity);
		}
		
		$scope.postDataSave = function(data) {
			addChildToCurrentNode(data, true);
		}
		
		$scope.addCancelled = function() {
			moveToNodeView($scope.contentIndex.currentNode);
		}
		
		$scope.goToAddContent = function(containerQId, parentIdentity) {
			StateUpdateService.goToStudioAdd(containerQId, parentIdentity);
		};
		
		var addChildToCurrentNode = function(childData, selectChildAsCurrent) {
			
			var currentNode = $scope.contentIndex.currentNode;
			var childNode = ContentIndexService.addInstanceDataChild(
					currentNode, currentNode.containerDef, childData);
			
			if (selectChildAsCurrent) {
				$scope.selectChildOfCurrent($scope.contentIndex.currentNode.containerDef.qualifiedId, childData.identity);
			}
			
			return childNode;
		};
		
		$scope.postDataUpdate = function(data) {
			$scope.updateCurrentNodeData(data);
			moveToNodeView($scope.contentIndex.currentNode);
		}

		$scope.updateCancelled = function(data) {
			moveToNodeView($scope.contentIndex.currentNode);
		}
		
		$scope.postDataDelete = function(parentNode, deletedChildIdentity) {
			ContentIndexService.deleteInstanceChildNode(parentNode, deletedChildIdentity);
			if (parentNode.type === "enclosure") {
				parentNode = parentNode.parentNode;
			}
			$scope.contentIndex.selectNodeLabel(parentNode);
		}
		
		$scope.updateCurrentNodeData = function(updatedData) {
			ContentIndexService.updateNodeData($scope.contentIndex.currentNode, updatedData);
		};
		
		$scope.getCurrentIndexNode = function() {
			return $scope.contentIndex.currentNode;
		}
		
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