enablix.studioApp.controller('PlayContentGroupDetailCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'ContentUtil', 'ContentTemplateService', 'CorrelationService', 'PlayDefinitionService', '$modalInstance', 'contentGroup', 'focusItems', 'Notification',
    function ($scope,   ConditionUtil,   QIdUtil,   ContentUtil,   ContentTemplateService,   CorrelationService,   PlayDefinitionService,   $modalInstance,   contentGroup,   focusItems,   Notification) {

		$scope.isContentSetCollapsed = false;
		$scope.isCorrContentCollapsed = false;
				
		$scope.contentGroup = angular.copy(contentGroup);
		$scope.contentSetRecords = [];
		
		$scope.contentGroupType = "";
		
		$scope.focusQId = focusItems.focusItem[0].qualifiedId;
		var focusContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, $scope.focusQId);
		$scope.focusName = focusContainerDef.label;
		
		$scope.masterCorrItemTypes = [];
		
		var decorateCorrItems = function(_corrItemList, _selectedCorrItems, _parent) {

			if (!isNullOrUndefined(_corrItemList)) {
			
				angular.forEach(_corrItemList, function(corrItem) {
					
					corrItem.label = ContentTemplateService.getContainerLabel(corrItem.qualifiedId);
					corrItem.parent = _parent;
					
					var childCorrelatedItems = null;
					
					if (!isNullOrUndefined(_selectedCorrItems)) {
						
						for (var i = 0; i < _selectedCorrItems.length; i++) {
						
							var selCorrItem = _selectedCorrItems[i];
							
							if (selCorrItem.qualifiedId === corrItem.qualifiedId) {
								childCorrelatedItems = selCorrItem.correlatedItem;
								corrItem._selected = true;
								break;
							}
						}
					}
					
					decorateCorrItems(corrItem.correlatedItem, childCorrelatedItems, corrItem);
				});
			}
		}
		
		CorrelationService.getCorrelatedItemTypeHierarchy($scope.focusQId, function(corrItemTypeHierarchy) {
				
				$scope.masterCorrItemTypes = corrItemTypeHierarchy;
				var selectedCorrItems = null;
				
				if ($scope.contentGroup.focusItemCorrelatedContent 
						&& $scope.contentGroup.focusItemCorrelatedContent.length > 0) {
					
					selectedCorrItems = $scope.contentGroup.focusItemCorrelatedContent[0].correlatedItem;
					
					if (selectedCorrItems.length > 0) {
						$scope.contentGroupType = 'corrContent';
					}
				} 
				
				decorateCorrItems($scope.masterCorrItemTypes, selectedCorrItems);
				
			}, function (errorData) {
				Notification.error({message: "Error retrieving correlation types", delay: enablix.errorMsgShowTime});
			});
		
		$scope.deleteContentSetRecord = function(record) {
			deleteRecordFromScopeContentSet(record);
			deleteRecordFromContentGroup(record);
		}
		
		if (!isNullOrUndefined($scope.contentGroup.contentSet)) {
			
			PlayDefinitionService.getContentSetRecords($scope.contentGroup.contentSet, function(data) {
				
					angular.forEach(data, function(contentDataRec) {
						
						var containerDef = ContentTemplateService.getContainerDefinition(enablix.template, contentDataRec.containerQId);
						var containerLabel = containerDef.label;
						var contentLabel = ContentUtil.getContentLabelValue(containerDef, contentDataRec.record);
						
						$scope.contentSetRecords.push({
							identity: contentDataRec.record.identity,
							qualifiedId: contentDataRec.containerQId,
							label: contentLabel,
							containerLabel: containerLabel
						});
						
					});
					
				}, function(errorData) {
					Notification.error({message: "Error retrieving content set records", delay: enablix.errorMsgShowTime});
				});
			
			if ($scope.contentGroup.contentSet.contentRecord 
					&& $scope.contentGroup.contentSet.contentRecord.length > 0) {
				$scope.contentGroupType = 'contentSet';
			}
			
		}
		
		var deleteRecordFromScopeContentSet = function(record) {
			
			var indx = -1;
			for (var i = 0; i < $scope.contentSetRecords.length; i++) {
				var contentSetRec = $scope.contentSetRecords[i];
				if (contentSetRec.identity == record.identity) {
					indx = i;
					break;
				}
			}
			
			if (indx != -1) {
				$scope.contentSetRecords.splice(indx, 1);
			}
		}
		
		var deleteRecordFromContentGroup = function(record) {
			
			var indx = -1;
			for (var i = 0; i < $scope.contentGroup.contentSet.contentRecord.length; i++) {
				var contentSetRec = $scope.contentGroup.contentSet.contentRecord[i];
				var identity = contentSetRec.value[0].value;
				if (identity == record.identity) {
					indx = i;
					break;
				}
			}
			
			if (indx != -1) {
				$scope.contentGroup.contentSet.contentRecord.splice(indx, 1);
			}
		}
		
		$scope.contentSetTableHeaders =
			 [{
				 desc: "Type",
				 valueKey: "containerLabel"
			 },
			 {
				 desc: "Title",
				 valueKey: "label"
			 }];
			 
		$scope.contentSetTableRecordActions = 
			[{
				actionName: "Remove",
				tooltip: "Delete",
				iconClass: "fa fa-times",
				tableCellClass: "remove",
				actionCallbackFn: $scope.deleteContentSetRecord
			}];
		
		var createCorrItemHierarchy = function(correlatedItemList) {
			
			var correlatedItems = [];
			
			if (!isNullOrUndefined(correlatedItemList)) {
				angular.forEach(correlatedItemList, function(corrItem) {
					if (corrItem._selected) {
						var selCorrItem = {
							qualifiedId: corrItem.qualifiedId
						}
						correlatedItems.push(selCorrItem);
						selCorrItem.correlatedItem = createCorrItemHierarchy(corrItem.correlatedItem);
					}
				});
			}
			
			return correlatedItems;
		}
		
		$scope.cancelOperation = function() {
			$modalInstance.dismiss('cancel');
		}
		
		var clearCorrItemHierarchySelection = function(_corrItemList) {
			
			if (!isNullOrUndefined(_corrItemList)) {
			
				angular.forEach(_corrItemList, function(corrItem) {
					corrItem._selected = false;
					clearCorrItemHierarchySelection(corrItem.correlatedItem);
				});
			}
		}
		
		var resetNotSelectedContentGroupType = function() {
			
			if ($scope.contentGroupType === 'corrContent') {
				
				$scope.contentSetRecords = [];
				$scope.contentGroup.contentSet = {};
				$scope.contentGroup.contentSet.contentRecord = [];
				
			} else if ($scope.contentGroupType === 'contentSet') {
				
				$scope.contentGroup.focusItemCorrelatedContent = [];
				clearCorrItemHierarchySelection($scope.masterCorrItemTypes);
			}
		}
		
		$scope.updateContentGroup = function() {
			
			resetNotSelectedContentGroupType();
			
			// populate focusItemCorrelatedcontent in content group
			$scope.contentGroup.focusItemCorrelatedContent = [];
			
			var focusCorrItem = {
					itemQId: $scope.focusQId
			};
			
			$scope.contentGroup.focusItemCorrelatedContent.push(focusCorrItem);
			focusCorrItem.correlatedItem = createCorrItemHierarchy($scope.masterCorrItemTypes);
			
			$modalInstance.close($scope.contentGroup);
		}
		
		$scope.contentSelected = function(selectedContentList) {
			
			$scope.contentGroup.contentSet = {};
			$scope.contentGroup.contentSet.contentRecord = [];
			
			$scope.contentSetRecords = selectedContentList;
			
			angular.forEach(selectedContentList, function(selectedContentItem) {
				
				$scope.contentGroup.contentSet.contentRecord.push({
					qualifiedId: selectedContentItem.qualifiedId,
					attribute: {
						value: "identity"
					},
					value: [{value: selectedContentItem.identity}]
				});
				
			});
			
			$scope.isContentSetCollapsed = false;
		}
		
		$scope.contentGroupTypeChanged = function() {
			// may be reset not selected content group type??
		}
		
}]);
