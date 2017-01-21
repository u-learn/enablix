enablix.studioApp.controller('PlayUserGroupDetailCtrl', 
			['$scope', 'ConditionUtil', 'QIdUtil', 'ContentUtil', 'ContentTemplateService', 'CorrelationService', 'PlayDefinitionService', '$modalInstance', 'userGroup', 'focusItems', 'Notification',
    function ($scope,   ConditionUtil,   QIdUtil,   ContentUtil,   ContentTemplateService,   CorrelationService,   PlayDefinitionService,   $modalInstance,   userGroup,   focusItems,   Notification) {

		$scope.isUserSetCollapsed = false;
		$scope.isRefSetCollapsed = false;
		$scope.isFilterSetCollapsed = false;
				
		$scope.userGroup = angular.copy(userGroup);
		$scope.focusQId = focusItems.focusItem[0].qualifiedId;
		
		$scope.userContainerId = ContentTemplateService.getUserContainerQId();
		var userContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, $scope.userContainerId);
		
		// set master data for user set
		$scope.userBoundedRefItemDef = {
				"id" : "playUserSet",
				"bounded" : {
	                "refList" : {
	                    "datastore" : {
	                        "storeId" : $scope.userContainerId, 
	                        "dataId" : "email", 
	                        "dataLabel" : "userName"
	                    }
	                }, 
	                "multivalued" : true,
				},
                "type" : "BOUNDED", 
                "label" : "Users", 
            }; 
		
		$scope.userSet = [];
		if ($scope.userGroup.userSet) {
			angular.forEach($scope.userGroup.userSet.userId, function(usrId) {
				$scope.userSet.push({
					id: usrId
				});
			});
		}
		
		
		// set master data for user filters
		
		var createDefaultUserFilterSet = function() {
			
			var userFilterSet = {
					andCondition: {
						basicCondition: []
					}
			};
			
			angular.forEach(userContainerDef.contentItem, function(contentItemDef) {
				if (ContentTemplateService.isBoundedRefListItem(contentItemDef)) {
					userFilterSet.andCondition.basicCondition.push({
						attribute: {
							value: contentItemDef.id
						},
						value: []
					});
				}
			});
			
			return userFilterSet;
		}
		
		$scope.filteredUsers = $scope.userGroup.filteredUserSet ? 
				$scope.userGroup.filteredUserSet : createDefaultUserFilterSet();
		
		
		// set master data for user reference set
		$scope.masterRefSetList = [];
		var focusContainerDef = ContentTemplateService.getContainerDefinition(enablix.template, $scope.focusQId);
		$scope.focusName = focusContainerDef.label;
		
		angular.forEach(focusContainerDef.contentItem, function(contentItemDef) {
			
			var boundedRefListQId = ContentTemplateService.checkAndGetBoundedRefListContainerQId(contentItemDef);
			
			if (boundedRefListQId != null && boundedRefListQId == $scope.userContainerId) {
				
				var userRefSet = {
						focusQId: $scope.focusQId,
						attributeId: contentItemDef.id,
						attributeLabel: contentItemDef.label
					};
				
				$scope.masterRefSetList.push(userRefSet);
				
				// check if this is already selected
				if ($scope.userGroup.referenceUserSet && $scope.userGroup.referenceUserSet.focusItemAttr) {
					var groupUserRefSet = $scope.userGroup.referenceUserSet.focusItemAttr;
					for (var i = 0; i < groupUserRefSet.length; i++) {
						if (contentItemDef.id == groupUserRefSet[i].value) {
							userRefSet._selected = true;
						}
					}
				}
				
			}
		});
		
		$scope.toggleRefSetSelection = function(refUserSet) {
			refUserSet._selected = !refUserSet._selected; 
		}
		
		// scope functions
		$scope.cancelOperation = function() {
			$modalInstance.dismiss('cancel');
		}
		
		$scope.updateUserGroup = function() {
			
			// populate user set in user group
			$scope.userGroup.userSet = {
					userId: []	
			};
			
			angular.forEach($scope.userSet, function(userObj) {
				$scope.userGroup.userSet.userId.push(userObj.id);
			});
			
			// assign filter user set
			$scope.userGroup.filteredUserSet = $scope.filteredUsers;
			
			// populate reference user set
			$scope.userGroup.referenceUserSet = {
					focusItemAttr: []
			};
			
			angular.forEach($scope.masterRefSetList, function(refUserSet) {
				if (refUserSet._selected) {
					$scope.userGroup.referenceUserSet.focusItemAttr.push({
						itemQId: refUserSet.focusQId,
						value: refUserSet.attributeId
					});
				}
			});
			
			$modalInstance.close($scope.userGroup);
		}
		
		
	}]);
