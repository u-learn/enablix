enablix.studioApp.controller('SystemAdminController', 
			['$scope', '$state', '$rootScope', 'RESTService', 'StateUpdateService',
	function( $scope,   $state,   $rootScope,   RESTService,   StateUpdateService) {
		
		$scope.$state = $state;
		
		$rootScope.$on('$stateChangeSuccess', 
			function(event, toState, toParams, fromState, fromParams) {
				if ($state.includes("system")) {
					setSetupBreadcrumb();
				}
			});
		
		var setSetupBreadcrumb = function() {
			
			if ($state.includes("system.users")) {
				
				$scope.breadcrumbList = 
				[
			         { label: "Setup" },
			         { label: "User Management" }
				];
				
			} else if ($state.includes("system.docstore")) {
				
				$scope.breadcrumbList = 
				[
			         { label: "Setup" },
			         { label: "Document Store" }
				];
			}

		};
		
		setSetupBreadcrumb();
		//StateUpdateService.goToListUser();
	}
]);			
