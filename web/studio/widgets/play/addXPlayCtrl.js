enablix.studioApp.controller('AddXPlayCtrl', 
			['$scope', '$stateParams', 'PlayDefinitionService', 'Notification', 'QIdUtil', 'StateUpdateService', '$filter', 'ContentTemplateService',
	function( $scope,   $stateParams,   PlayDefinitionService,   Notification,   QIdUtil,   StateUpdateService,   $filter,   ContentTemplateService) {
		
		$scope.prototypePlayTemplate = {};
		$scope.playScopeDef = {};
		
		PlayDefinitionService.getPlayDefinition($stateParams.playDefId, function(playDef) {
				initializeAddXPlay(playDef);
			}, function(errorData) {
				Notification.error({message: "Error retrieving base play definition data", delay: enablix.errorMsgShowTime});
			});
		
		var initializeAddXPlay = function(_playDef) {
			
			var playTemplate = _playDef.playTemplate;
			$scope.prototypePlayTemplate = playTemplate;
			$scope.playScopeDef = playTemplate.scope;
			
			var focusItemQId = playTemplate.focusItems.focusItem[0].qualifiedId;
			$scope.focusItemContainer = ContentTemplateService.getContainerDefinition(enablix.template, focusItemQId);
		}
		
	}
]);