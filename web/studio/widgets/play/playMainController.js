enablix.studioApp.controller('PlayMainController', 
			['$scope', '$state', '$stateParams', '$rootScope', 'PlayDefinitionService', 'Notification', 'StateUpdateService',
	function( $scope,   $state,   $stateParams,   $rootScope,   PlayDefinitionService,   Notification,   StateUpdateService) {
		
		$scope.breadcrumbList = [
			{ label: "Play" }
		];
		
		$scope.$state = $state;
		$scope.$stateParams = $state.params;
		
		$rootScope.$on('$stateChangeSuccess', 
			function(event, toState, toParams, fromState, fromParams) {
				if ($state.includes("play")) {
					$scope.$stateParams = toParams;
				}
			});
				
		$scope.playDefinitions = [];
		
		PlayDefinitionService.getAllPrototypePlaysSummary(function(playDefs) {
				$scope.playDefinitions = playDefs;
			}, function(errorData) {
				Notification.error({message: "Error retrieving play definitions.", delay: enablix.errorMsgShowTime});
			});
		
		$scope.navToPlayDefinition = function(playDef) {
			if (playDef.playTemplate.executable) {
				StateUpdateService.goToPlayRList(playDef.playTemplate.id);
			} else if (playDef.playTemplate.prototype) {
				StateUpdateService.goToPlayXList(playDef.playTemplate.id);
			}
		};
		
	}
]);