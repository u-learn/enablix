enablix.studioApp.directive('ebxLayoutSwitch', [
				'$state', '$stateParams', 'LayoutService',
		function($state,   $stateParams,   LayoutService) {

			return {
				restrict : 'E',
				scope : {
					pageName : '=?',
					containerQId : '='
				},
				link : function(scope, element, attrs) {

					scope.pageName = scope.pageName || $state.current.pageName;
					scope.layoutOptions = [];
					
					var promise = LayoutService.getLayoutOptionsByPageAndContainer(scope.pageName, scope.containerQId);
					
					promise.then(function(layoutOptions) {
					
						if (!isNullOrUndefined(layoutOptions)) {
						
							scope.layoutOptions = layoutOptions;
							
							for (var i = 0; i < layoutOptions.length; i++) {
							
								var opt = layoutOptions[i];
								if ($state.current.name == opt.stateName) {
									scope.currentLayoutName = opt.name;
									break;
								}
							}
						}
					});
					
					scope.switchLayout = function(_layout) {
						LayoutService.switchLayout(_layout);
					}
					
				},
				templateUrl : "widgets/directive/layoutswitch/layoutSwitch.html"
			};
		} ]);