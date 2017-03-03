enablix.studioApp.directive('ebxPlayExecuteOn', function() {

	return {
		restrict: 'E',
		scope : {
			executeOnDef : "=",
			focusItems : "=",
			contentGroupsDef : "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'PlayExecuteOnDefCtrl',
		templateUrl: "widgets/directive/play/execution/detail/playExecuteOn.html"
	}
});

// controller
enablix.studioApp.controller('PlayExecuteOnDefCtrl', 
		['$scope', 'PlayDefinitionService', 'Notification', 
function ($scope,   PlayDefinitionService,   Notification) {	
	
	var FOCUS_ENTITY_CONTENT_GRP_ID = "~focusEntity~";		
	
	$scope.onUpdateContentGroup = { items: [] };
	$scope.allContentGroups = [];
	
	if ($scope.executeOnDef && $scope.executeOnDef.type == 'ON_CONTENT_UPDATE') {

		// add for focus entity
		var focusEntityContentGrp = {
			id: FOCUS_ENTITY_CONTENT_GRP_ID,
			name: "Play Focus"
		};
		
		$scope.allContentGroups.push(focusEntityContentGrp);
		
		if ($scope.contentGroupsDef) {
			angular.forEach($scope.contentGroupsDef.contentGroup, function(contentGrp) {
				$scope.allContentGroups.push({
					id: contentGrp.id,
					name: contentGrp.name
				});
			})
		}

		
		if ($scope.executeOnDef.updateOn && $scope.executeOnDef.updateOn.contentGroups 
				&& $scope.executeOnDef.updateOn.contentGroups.contentGroup) {
		
			angular.forEach($scope.executeOnDef.updateOn.contentGroups.contentGroup, function(contentGrp) {
				
				var cg = PlayDefinitionService.findContentGroupDef($scope.contentGroupsDef, contentGrp.id);
				
				if (cg != null) {
					$scope.onUpdateContentGroup.items.push({
						id: cg.id,
						name: cg.name
					});
				}
			});
			
		}
		
		if (!isNullOrUndefined($scope.executeOnDef.updateOn) 
				&& !isNullOrUndefined($scope.executeOnDef.updateOn.focusItem)) {
			$scope.onUpdateContentGroup.items.push(focusEntityContentGrp);
		}
		
	}
	
	// callback functions
	$scope.onExecTypeChange = function() {
		
		if ($scope.executeOnDef.type == 'RECURRING' && !$scope.executeOnDef.recurringSchedule) {
			$scope.executeOnDef.recurringSchedule = {
					interval: {
						timeUnit: "DAY",
						timeValue: ""
					}
			}
		}
		
		if ($scope.executeOnDef.type == "ON_CONTENT_UPDATE" && !$scope.executeOnDef.updateOn) {
			$scope.executeOnDef.updateOn = {};
		}
	};
	
	$scope.updateContentGroups = function($item, $modal) {
		
		$scope.executeOnDef.updateOn = {};
		
		$scope.executeOnDef.updateOn.contentGroups = { contentGroup: [] };
		
		$scope.executeOnDef.updateOn.focusItem = null;
		
		angular.forEach($scope.onUpdateContentGroup.items, function(contentGrp) {
			
			if (contentGrp.id == FOCUS_ENTITY_CONTENT_GRP_ID) {
				$scope.executeOnDef.updateOn.focusItem = [{}];
			} else {
				$scope.executeOnDef.updateOn.contentGroups.contentGroup.push({id: contentGrp.id})
			}
			
		});
		
	}
	
}]);
