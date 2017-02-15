enablix.studioApp.directive('ebxLinkedKits', [
	function() {

		return {
			restrict : 'E',
			scope : {
				headingText: "@",
				expanded: "@",
				records: "="
			},
			controller : 'LinkedKitsDisplayCtrl',
			templateUrl : function(elem, attr) {
				return "widgets/directive/contentkit/linkedKitsDisplay.html";
			}
		};
	} ]);

// controller
enablix.studioApp.controller('LinkedKitsDisplayCtrl',
		['$scope', '$state', 'StateUpdateService', 'ContentKitService', '$stateParams',
function ($scope,   $state,   StateUpdateService,   ContentKitService,   $stateParams) {
	
	var portalPage = $state.includes("portal");
			
	$scope.toggleContainer = function($event) {
		var elem = $event.currentTarget;
		if ($event.target.nodeName != 'A' ) {
			$(elem).toggleClass('active');
			$(elem).next().slideToggle('fast');
		}
	};
	
	$scope.navToLinkedKit = function(_linkedKitIdentity) {
		if (portalPage) {
			StateUpdateService.goToPortalContentKitDetail(_linkedKitIdentity);
		} else {
			StateUpdateService.goToContentKitDetail(_linkedKitIdentity);
		}
	};
	
}]);
