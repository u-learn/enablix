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
			rowActions: "=?",
			selectAction: "=?"
		},
		link: function(scope, element, attrs) {
			
			scope.rowActions = scope.rowActions || [];
			
		    scope.setPage = function (pageNum) {
		    	if (scope.setPageFn && pageNum >=0 && pageNum < scope.pageData.totalPages) {
		    		scope.setPageFn(pageNum);
		    	}
		    };
		    
		    scope.callRowAction = function(action, dataRecord, $event) {
		    	if (action.actionCallbackFn) {
		    		action.actionCallbackFn(dataRecord, $event);
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
		    
		    scope.selectActionCallback = function(dataRecord, $event) {
		    	dataRecord._selected = !dataRecord._selected;
		    	if (scope.selectAction && scope.selectAction.actionCallbackFn) {
		    		scope.selectAction.actionCallbackFn(dataRecord, $event)
		    	}
		    }
		    
		},

		templateUrl: "widgets/directive/genericTable/genericTable.html"
	};
}]);