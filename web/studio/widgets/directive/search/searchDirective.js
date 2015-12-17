enablix.studioApp.directive('ebxSearchBox', [
		'StateUpdateService',
		function(StateUpdateService) {

			return {
				restrict : 'E',
				scope : {
				},
				link : function(scope, element, attrs) {
					
					element.bind("keydown keypress", function (event) {
						var keyCode = event.which || event.keyCode;
						// If enter key is pressed
			            if (keyCode === 13) {
			                scope.$apply(function() {
			                    scope.search();
			                });
			                event.preventDefault();
			            }
					});
					
					scope.search = function() {
						if (!isNullOrUndefined(scope.searchText) && scope.searchText.length > 0) {
							StateUpdateService.goToPortalSearch(scope.searchText);
						}
					}
					
				},
				templateUrl : "widgets/directive/search/searchBox.html"
			};
		} ]);