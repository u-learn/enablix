enablix.studioApp.directive('ebxGenericTable', [
        'StateUpdateService',
function(StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			tableData : "=",
			tableHeaders : "=",
			pageData : "=?",
			setPageFn : "=?",
			rowActions: "=?"
		},
		link: function(scope, element, attrs) {
			
			scope.rowActions = scope.rowActions || [];
			
		    scope.setPage = function (pageNum) {
		    	if (scope.setPageFn && pageNum >=0 && pageNum < scope.pageData.totalPages) {
		    		scope.setPageFn(pageNum);
		    	}
		    };
		    
		    scope.callRowAction = function(action, dataRecord) {
		    	if (action.actionCallbackFn) {
		    		action.actionCallbackFn(dataRecord);
		    	}
		    };
		    
		    scope.getColumnValue = function(col, dataRecord) {
		    	
		    	if (col.valueFn) {
		    		return col.valueFn(dataRecord);
		    	} else if (col.valueKey) {
		    		return dataRecord[col.valueKey];
		    	}
		    	
		    	return "--";
		    }

		},

		templateUrl: "widgets/directive/genericTable/genericTable.html"
	};
}]);