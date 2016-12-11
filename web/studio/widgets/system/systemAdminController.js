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
			         { label: "System" },
			         { label: "User Management" }
				];
				
			} else if ($state.includes("system.docstore")) {
				
				$scope.breadcrumbList = 
				[
			         { label: "Setup" },
			         { label: "System" },
			         { label: "Document Store" }
				];
				
			} else if ($state.includes("system.quicklinks")) {
				
				$scope.breadcrumbList = 
				[
			         { label: "Setup" },
			         { label: "System" },
			         { label: "Quick Links" }
				];
				
			} else if ($state.includes("system.emailConfig")) {
				
				$scope.breadcrumbList = 
				[
			         { label: "Setup" },
			         { label: "System" },
			         { label: "Email Configuration" }
				];
				
			} else if ($state.includes("system.recommendations")) {
				
				$scope.breadcrumbList = 
					[
				         { label: "Setup" },
				         { label: "System" },
				         { label: "Recommendation" }
					];
				
			} else if ($state.includes('system.contentrequestlist') 
					|| $state.includes('system.contentrequestedit') 
					|| $state.includes('system.contentrequestview')) {
				
				$scope.breadcrumbList = 
					[
				         { label: "Setup" },
				         { label: "System" },
				         { label: "Content Requests" }
					];
				
			} else if ($state.includes('system.contentconnlist') 
					|| $state.includes('system.contentconndetail') 
					|| $state.includes('system.contentconnadd') 
					|| $state.includes('system.contentconnedit')) {
				
				$scope.breadcrumbList = 
					[
				         { label: "Setup" },
				         { label: "System" },
				         { label: "Content Mappings" }
					];
			}

		};
		
		setSetupBreadcrumb();
	}
]);			
