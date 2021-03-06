enablix.studioApp.controller('contentIndexCtrl', 
			['$scope', '$state', '$stateParams', 'ContentTemplateService', 'ContentIndexService', 'StateUpdateService', 'Notification',
    function( $scope,   $state,   $stateParams,   ContentTemplateService,   ContentIndexService,   StateUpdateService,   Notification) {
	
		$scope.contentIndex = $scope.contentIndex || {};
		var studioConfig = ContentTemplateService.getStudioConfig($stateParams.studioName);
		
		ContentIndexService.getContentIndexData(enablix.templateId, $stateParams.studioName, 
			function(data) {
		    	
				$scope.indexData = data; 
				if (!$stateParams.containerQId && $scope.contentIndex.selectNodeLabel) {
					var firstNode = data[0];
					$scope.contentIndex.selectNodeLabel(firstNode);
				}
		    	
		    }, function(data) {
		    	//alert("Error fetching content index");
		    	Notification.error({message: "Error fetching content index", delay: enablix.errorMsgShowTime});
		    });
	    
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
		
		$scope.contentIndex.selectNodeHeadCallback = function(selectedNode, $event) {
			if (!selectedNode.dataLoaded && selectedNode.containerDef) {
				if (studioConfig.navigableIndex) {
					ContentIndexService.loadIndexChildren(selectedNode, false);
				} else {
					selectedNode.collapsed = true;
				}
			}
		}
		
		$scope.contentIndex.selectNodeCallback = moveToNodeView;
		
		$scope.goToDetailEdit = function(containerQId, elementIdentity) {
			StateUpdateService.goToStudioEdit(containerQId, elementIdentity);
		}
		
		$scope.postDataSave = function(data) {
			if (studioConfig.navigableIndex) {
				addChildToCurrentNode(data, true);
			} else {
				StateUpdateService.goToStudioDetail($scope.contentIndex.currentNode.containerDef.qualifiedId, data.identity);
			}
		}
		
		$scope.addCancelled = function() {
			moveToNodeView($scope.contentIndex.currentNode);
		}
		
		$scope.postContentDraftSave = function() {
			moveToNodeView($scope.contentIndex.currentNode);
		}
		
		$scope.goToAddContent = function(containerQId, parentIdentity) {
			StateUpdateService.goToStudioAdd(containerQId, parentIdentity);
		};
		
		var addChildToCurrentNode = function(childData, selectChildAsCurrent) {
			
			var currentNode = $scope.contentIndex.currentNode;
			var childNode = ContentIndexService.addInstanceDataChild(
					currentNode, currentNode.containerDef, childData, true);
			
			if (selectChildAsCurrent) {
				$scope.selectChildOfCurrent($scope.contentIndex.currentNode.containerDef.qualifiedId, childData.identity);
			}
			
			return childNode;
		};
		
		$scope.postDataUpdate = function(data) {
			
			$scope.updateCurrentNodeData(data);
			
			if (data.identity == $scope.contentIndex.elementIdentity) {
				moveToNodeView($scope.contentIndex.currentNode);
			} else {
				StateUpdateService.goToStudioDetail($scope.contentIndex.currentNode.containerDef.qualifiedId, data.identity);
			}
		}

		$scope.updateCancelled = function(data) {
			
			if (data.identity == $scope.contentIndex.elementIdentity) {
				moveToNodeView($scope.contentIndex.currentNode);
			} else {
				StateUpdateService.goToStudioDetail($scope.contentIndex.currentNode.containerDef.qualifiedId, data.identity);
			}
		}
		
		$scope.postDataDelete = function(parentNode, deletedChildIdentity) {
			
			if (!isNullOrUndefined(parentNode)) {
				
				ContentIndexService.deleteInstanceChildNode(parentNode, deletedChildIdentity);
				if (parentNode.type === "enclosure") {
					parentNode = parentNode.parentNode;
				}
				
				$scope.contentIndex.selectNodeLabel(parentNode);
				
			} else {
				// move to the root container list
				moveToNodeView($scope.contentIndex.currentNode);
			}
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