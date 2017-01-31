enablix.studioApp.controller('AddEditXPlayCtrl', 
			['$scope', '$state', '$stateParams', 'PlayDefinitionService', 'Notification', 'QIdUtil', 'StateUpdateService', '$filter', 'ContentTemplateService',
	function( $scope,   $state,   $stateParams,   PlayDefinitionService,   Notification,   QIdUtil,   StateUpdateService,   $filter,   ContentTemplateService) {
		
		$scope.isScopeDefCollapsed = false;
		$scope.isUserGroupsCollapsed = false;
		$scope.isContentGroupsCollapsed = false;
		$scope.isPlayerCollapsed = false;
		$scope.playDefActive = false;
		
		$scope.editOperation = $state.includes('play.editExecutable');
		$scope.prototypePlayTemplate = {};
		$scope.pageHeading = $scope.editOperation ? "Edit Play" : "New Play";
		
		$scope.scopeLabel = "Scope";
		$scope.contentGroupsLabel = "Content";
		$scope.userGroupsLabel = "Member";
		$scope.executionLabel = "Player";
		
		PlayDefinitionService.getPlayDefinition($stateParams.playDefId, function(playDef) {
				initializeAddXPlay(playDef);
			}, function(errorData) {
				Notification.error({message: "Error retrieving base play definition data", delay: enablix.errorMsgShowTime});
			});
		
		var initializeAddXPlay = function(_playDef) {
			
			var playTemplate = _playDef.playTemplate;
			$scope.pageHeading = $scope.editOperation ? playTemplate.title : "New " + playTemplate.name + " Play";
			
			if (!$scope.editOperation) {
				playTemplate.prototypeId = playTemplate.id;
				playTemplate.prototype = false;
				playTemplate.executable = true;
				playTemplate.id = null;
			}
			
			$scope.prototypePlayTemplate = playTemplate;
			
			$scope.scopeLabel = playTemplate.scope.label || $scope.scopeLabel;
			$scope.contentGroupsLabel = playTemplate.contentGroups.label || $scope.contentGroupsLabel;
			$scope.userGroupsLabel = playTemplate.userGroups.label || $scope.userGroupsLabel;
			$scope.executionLabel = playTemplate.execution.label || $scope.executionLabel;
			
			var focusItemQId = playTemplate.focusItems.focusItem[0].qualifiedId;
			$scope.focusItemContainer = ContentTemplateService.getContainerDefinition(enablix.template, focusItemQId);
		}
		
		$scope.updatePlay = function() {
			
			PlayDefinitionService.saveOrUpdatePlayTemplate($scope.prototypePlayTemplate,
				function(data) {
					Notification.primary("Saved successfully!");
				}, 
				function (data) {
					Notification.error({message: "Error saving data", delay: enablix.errorMsgShowTime});
				});
		};
		
		$scope.cancelOperation = function() {
			StateUpdateService.goBack();
		}
		
	}
]);