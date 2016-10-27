enablix.studioApp.directive('ebxBack', 
				['NavigationTracker', 'StateUpdateService', 
		 function(NavigationTracker, StateUpdateService) {

			return {
				restrict: 'E',
				scope : {
					text: '@'
				},
		
				link: function(scope, element, attrs) {
					scope.text = scope.text || "Back";
					scope.goBack = function() {
						StateUpdateService.goBack();
					}
				},
				templateUrl: "widgets/directive/back/backButton.html"
			}
	
		}
]);