enablix.studioApp.directive('ebPagination', [
        'StateUpdateService',
function(StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			totalPages : "=",
			currentPage : "=",
			setPageFn : "="
		},
		link: function(scope, element, attrs) {
			
			scope.getPageNumbers = function (totalPages) {
		        var ret = [];
		        for (var i = 0; i < totalPages; i++) {
		        	ret.push(i);
		        }
		        return ret;
		    };
		    
		    scope.setPage = function (pageNum) {
		    	if (scope.setPageFn && pageNum >=0 && pageNum < scope.totalPages) {
		    		scope.setPageFn(pageNum);
		    	}
		    };
		    
		},

		templateUrl: "widgets/directive/pagination/pagination.html"
	};
}]);