enablix.studioApp.directive('ebxSearchResultItem', [
	function() {

		return {
			restrict : 'E',
			scope : {
				resultItem: "=",
				index: "="
			},
			controller : 'SearchResultItemCtrl',
			templateUrl : function(elem, attr) {
				return "widgets/directive/search/searchResultItem.html";
			}
		};
	} ]);