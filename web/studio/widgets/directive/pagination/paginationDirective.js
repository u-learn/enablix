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
			
			scope.getPageNumbers = function (totalPages, currentPage) {
				
				var firstPageNum = 0;
				var lastPageNum = totalPages - 1;
				
				if (totalPages >= 10) {

					firstPageNum = currentPage - 5;
					if (firstPageNum < 1) {
						firstPageNum = 0;
					}
					
					lastPageNum = firstPageNum + 9;
					if (lastPageNum >= totalPages) {
						lastPageNum = totalPages - 1;
					}

				}
				
		        var ret = [];
		        for (var i = firstPageNum; i <= lastPageNum; i++) {
		        	ret.push(i);
		        }
		        
		        return ret;
		    };
		    
		    scope.setPage = function (pageNum) {
		    	if (scope.setPageFn && pageNum >=0 && pageNum < scope.totalPages && pageNum != scope.currentPage) {
		    		scope.setPageFn(pageNum);
		    	}
		    };
		    
		},

		templateUrl: "widgets/directive/pagination/pagination.html"
	};
}]);