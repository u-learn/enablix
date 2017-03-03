enablix.studioApp.directive('ebxPlayEmailActionDef', function() {

	return {
		restrict: 'E',
		scope : {
			actionDef : "=",
			focusItems : "=",
			contentGroupsDef : "=",
			userGroupsDef: "="
		},

		link: function(scope, element, attrs) {
		},

		controller: 'PlayEmailActionDefCtrl',
		templateUrl: "widgets/directive/play/execution/detail/action/emailActionDef.html"
	}
});

// controller
enablix.studioApp.controller('PlayEmailActionDefCtrl', 
		['$scope', 'PlayDefinitionService', 'Notification', 
function ($scope,   PlayDefinitionService,   Notification) {	
	
	var TRIGGER_ENTITY_CONTENT_GRP_ID = "~triggerEntity~";		
	
	$scope.emailContentGroup = { items: [] };
	$scope.emailUserGroup = { items: [] }
	$scope.allContentGroups = [];
	
	if ($scope.actionDef.recipient && $scope.actionDef.recipient.userGroups 
			&& $scope.actionDef.recipient.userGroups.userGroup) {
		
		angular.forEach($scope.actionDef.recipient.userGroups.userGroup, function(userGrp) {
			
			for (var i = 0; i < $scope.userGroupsDef.userGroup.length; i++) {
			
				var ug = $scope.userGroupsDef.userGroup[i];			
				
				if (ug.id == userGrp.id) {
					$scope.emailUserGroup.items.push(ug);
					break;
				}
			}
		});
	}
	
	if ($scope.actionDef.emailContent && $scope.actionDef.emailContent.contentGroups 
			&& $scope.actionDef.emailContent.contentGroups.contentGroup) {
		
		angular.forEach($scope.actionDef.emailContent.contentGroups.contentGroup, function(contentGrp) {
			
			var cg = PlayDefinitionService.findContentGroupDef($scope.contentGroupsDef, contentGrp.id);
			
			if (cg != null) {
				$scope.emailContentGroup.items.push({
					id: cg.id,
					name: cg.name
				});
			}
			
		});
	}
	
	// add for focus entity
	var triggerEntityContentGrp = {
		id: TRIGGER_ENTITY_CONTENT_GRP_ID,
		name: "Play Focus"
	};
	
	$scope.allContentGroups.push(triggerEntityContentGrp);
	
	if ($scope.actionDef.emailContent && $scope.actionDef.emailContent.triggerEntity) {
		$scope.emailContentGroup.items.push(triggerEntityContentGrp);
	}
	
	if ($scope.contentGroupsDef) {
		angular.forEach($scope.contentGroupsDef.contentGroup, function(contentGrp) {
			$scope.allContentGroups.push({
				id: contentGrp.id,
				name: contentGrp.name
			});
		})
	}
	
	
	// callback functions
	$scope.updateContentGroups = function($item, $modal) {
		
		$scope.actionDef.emailContent = {};
		
		$scope.actionDef.emailContent.contentGroups = { contentGroup: [] };
		
		angular.forEach($scope.emailContentGroup.items, function(contentGrp) {
		
			if (contentGrp.id == TRIGGER_ENTITY_CONTENT_GRP_ID) {
				$scope.actionDef.emailContent.triggerEntity = {};
			} else {
				$scope.actionDef.emailContent.contentGroups.contentGroup.push({id: contentGrp.id})
			}
		});
		
	}
	
	
	$scope.updateUserGroups = function($item, $modal) {
		
		if (!$scope.actionDef.recipient) {
			$scope.actionDef.recipient = {};
		}
		
		$scope.actionDef.recipient.userGroups = { userGroup: [] };
		
		angular.forEach($scope.emailUserGroup.items, function(userGrp) {
			$scope.actionDef.recipient.userGroups.userGroup.push({id: userGrp.id})
		});
		
	}

	
}]);
