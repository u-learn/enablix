enablix.studioApp.controller('MyAccountController', 
			['$scope', '$state', '$rootScope', 'RESTService', 'StateUpdateService',
	function( $scope,   $state,   $rootScope,   RESTService,   StateUpdateService) {
		
		$scope.$state = $state;
		
		$rootScope.$on('$stateChangeSuccess', 
			function(event, toState, toParams, fromState, fromParams) {
				if ($state.includes("myaccount")) {
					setSetupBreadcrumb();
				}
			});
		
		var setSetupBreadcrumb = function() {
			
			if ($state.includes('myaccount.contentrequestlist') 
					|| $state.includes('myaccount.contentrequestedit') 
					|| $state.includes('myaccount.contentrequestview')) {
				
				$scope.breadcrumbList = 
					[
				         { label: "My Account" },
				         { label: "Content Requests" }
					];
			}

		};
		
		setSetupBreadcrumb();
		
	}
]);			
