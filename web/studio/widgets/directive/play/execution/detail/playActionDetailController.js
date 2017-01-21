enablix.studioApp.controller('PlayActionDetailCtrl', 
			['$scope', 'ConditionUtil', 'ContentUtil', 'ContentTemplateService', 'PlayDefinitionService', '$modalInstance', 'checkpointDef', 'contentGroupsDef', 'userGroupsDef', 'focusItems', 'Notification',
    function ($scope,   ConditionUtil,   ContentUtil,   ContentTemplateService,   PlayDefinitionService,   $modalInstance,   checkpointDef,   contentGroupsDef,   userGroupsDef,   focusItems,   Notification) {

		$scope.isUserSetCollapsed = false;
		$scope.isRefSetCollapsed = false;
		$scope.isFilterSetCollapsed = false;
		
		$scope.checkpointDef = angular.copy(checkpointDef);
		$scope.focusQId = focusItems.focusItem[0].qualifiedId;
		
		$scope.actionList = [{type: "email", name: "Email"}];
		
		$scope.actionType = "email";
		$scope.actionDef = {};
		$scope.executeOnDef = checkpointDef.executionTime;
		
		if ($scope.checkpointDef.actions.email && $scope.checkpointDef.actions.email.length > 0) {
			$scope.actionType = "email";
			$scope.actionDef = $scope.checkpointDef.actions.email[0];
		}
		
		// scope functions
		$scope.cancelOperation = function() {
			$modalInstance.dismiss('cancel');
		}
		
		$scope.updatePlayAction = function() {
			$modalInstance.close($scope.checkpointDef);
		}
		
		
	}]);
