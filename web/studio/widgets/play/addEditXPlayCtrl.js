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
		
		$scope.selectedFocus = {items : []}; // need to have "items" because of https://github.com/angular-ui/ui-select/issues/1353
		$scope.focusOptions = [];
		$scope.focusEditable = false;
		
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
			
			$scope.focusEditable = playTemplate.focusItems.editable;
			$scope.prototypePlayTemplate = playTemplate;
			
			$scope.scopeLabel = (playTemplate.scope && playTemplate.scope.label) || $scope.scopeLabel;
			$scope.contentGroupsLabel = (playTemplate.contentGroups && playTemplate.contentGroups.label) || $scope.contentGroupsLabel;
			$scope.userGroupsLabel = (playTemplate.userGroups && playTemplate.userGroups.label) || $scope.userGroupsLabel;
			$scope.executionLabel = (playTemplate.execution && playTemplate.execution.label) || $scope.executionLabel;
			
			ContentTemplateService.walkContainers(function(_containerDef) {
				
				if (!_containerDef.refData && !ContentTemplateService.isLinkedContainer(_containerDef)) {
					
					var focusOpt = {
							id: _containerDef.qualifiedId,
							label: _containerDef.label
						};
					
					$scope.focusOptions.push(focusOpt);
				}
			});

			// populate selected focus
			if (playTemplate.focusItems) {
				angular.forEach(playTemplate.focusItems.focusItem, function(focusItem) {
					
					var selFocusOpt = {id: focusItem.qualifiedId};
					$scope.selectedFocus.items.push(selFocusOpt);
					
					// set the label for the selected focus
					for (var i = 0; i < $scope.focusOptions.length; i++) {
						var focusOpt = $scope.focusOptions[i];
						if (focusOpt.id == focusItem.qualifiedId) {
							selFocusOpt.label = focusOpt.label;
							break;
						}
					}
				});
			}
			
		}
		
		$scope.updateFocusInPlayTemplate = function($item, $model) {
			
			if ($scope.focusEditable) {
				
				$scope.prototypePlayTemplate.focusItems.focusItem = [];
				
				angular.forEach($scope.selectedFocus.items, function(selFocus) {
					$scope.prototypePlayTemplate.focusItems.focusItem.push({qualifiedId: selFocus.id})
				});
			}

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