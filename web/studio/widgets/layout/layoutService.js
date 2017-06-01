enablix.studioApp.factory('LayoutService', 
	[
	 			'$state', '$stateParams', '$q', '$rootScope', '$location', '$window', 'StateUpdateService', 
	 	function($state,   $stateParams,   $q,   $rootScope,   $location,   $window,   StateUpdateService) {

	 		var layoutProviders = {};

	 		var registerPageLayoutProvider = function(_pageLayoutProvider) {
	 			layoutProviders[_pageLayoutProvider.pageName()] = _pageLayoutProvider;
	 		}
	 		
	 		var getLayoutOptionsByPageAndContainer = function(_pageName, _containerQId) {
	 			
	 			var promise = null;
	 			
	 			var layoutProvider = layoutProviders[_pageName];
	 			if (!isNullOrUndefined(layoutProvider)) {
	 				promise = layoutProvider.getLayoutOptionsByContainerQId(_containerQId);
	 			}
	 			
	 			if (promise == null) {
	 				deferred = $q.defer();
		 			deferred.resolve(null);
		 			promise = deferred.promise;
	 			}
	 			
	 			return promise;
	 		};
	 		
	 		var switchLayout = function(_layout) {
				
				var params = {};
				
				angular.forEach($stateParams, function(value, key) {
					params[key] = value;
				});
				
				angular.forEach(_layout.stateParams, function(value, key) {
					params[key] = value;
				});
				
				StateUpdateService.goToState(_layout.stateName, params);
			}
	 		
	 		return {
	 			getLayoutOptionsByPageAndContainer : getLayoutOptionsByPageAndContainer,
	 			registerPageLayoutProvider : registerPageLayoutProvider,
	 			switchLayout: switchLayout
	 		};
	 	}
	 ]);


enablix.studioApp.factory('PortalContainerDetailPageLayoutProvider', [ 		
			'$state', '$stateParams', '$q',
	function($state,   $stateParams,   $q) {
		
		var getLayoutOptionsByContainerQId = function(_containerQId) {
			
			var layouts = 
				[{
					id: "default",
					name: "Default",
					type: "default",
					stateName: "portal.container"
				},
				{
					id: "groupByOppStage",
					name: "Group By Opp Stage",
					type: "gbv",
					stateName: "portal.container-gbv",
					stateParams: { gbQId: "oppStage"}
				}];
			
			var deferred = $q.defer();
			deferred.resolve(layouts);
			return deferred.promise;
		}
				
		return {
			pageName: function() { return "portal-container-detail"; },
			getLayoutOptionsByContainerQId: getLayoutOptionsByContainerQId
		}
	}
		 	
]);

angular.module("studio").run(['LayoutService', 'PortalContainerDetailPageLayoutProvider',
	function(LayoutService, PortalContainerDetailPageLayoutProvider) { 
		LayoutService.registerPageLayoutProvider(PortalContainerDetailPageLayoutProvider); 
	}
]);
