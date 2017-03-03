enablix.studioApp.controller('PlayActionDetailCtrl', 
			['$scope', 'ConditionUtil', 'ContentUtil', 'ContentTemplateService', 'PlayDefinitionService', '$modalInstance', 'checkpointDef', 'contentGroupsDef', 'userGroupsDef', 'focusItems', 'Notification', 
    function ($scope,   ConditionUtil,   ContentUtil,   ContentTemplateService,   PlayDefinitionService,   $modalInstance,   checkpointDef,   contentGroupsDef,   userGroupsDef,   focusItems,   Notification) {

		$scope.isUserSetCollapsed = false;
		$scope.isRefSetCollapsed = false;
		$scope.isFilterSetCollapsed = false;
		
		$scope.checkpointDef = angular.copy(checkpointDef);
		$scope.contentGroupsDef = contentGroupsDef;
		$scope.userGroupsDef = userGroupsDef;
		$scope.focusItems = focusItems;
		
		$scope.actionType = PlayDefinitionService.getActionType($scope.checkpointDef.actions);
		$scope.actionName = PlayDefinitionService.getFirstActionName($scope.checkpointDef.actions);
		$scope.actionDef = PlayDefinitionService.getActionDef($scope.checkpointDef.actions)
		
		$scope.executeOnDef = checkpointDef.executionTime;
		
		// scope functions
		$scope.cancelOperation = function() {
			$modalInstance.dismiss('cancel');
		}
		
		$scope.updatePlayAction = function() {
			$modalInstance.close($scope.checkpointDef);
		}
		
	}]);
