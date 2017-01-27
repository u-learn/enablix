enablix.studioApp.directive('ebxGenericTable', [
        'StateUpdateService',
function(StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			tableData : "=",
			tableHeaders : "=",
			pageData : "=?",
			
			// Deprecated. Should use "fetchResultFn" and "paginationData" attributes
			setPageFn : "=?",
			
			rowActions: "=?",
			selectAction: "=?",
			
			/**
			 * fetchResultFn is a callback function of type
			 * 
			 * fetchResult(paginationData) {
			 * 	// TODO: fetch data list
			 * }
			 * 
			 */
			fetchResultFn: "=?",
			
			/**
			 * Pagination attribute is a JSON structure of type:
			 * 
			 * paginationData = {
			 *		pageSize: enablix.defaultPageSize,
			 *		pageNum: 0,
			 *		sort: {
			 *			field: "createdAt",
			 *			direction: "DESC"
			 *		}
			 *	};
			 */
			paginationData: "=?"
		},
		link: function(scope, element, attrs) {
			
			scope.rowActions = scope.rowActions || [];
			
		    scope.setPage = function (pageNum) {
		    	
				if (pageNum >=0 && pageNum < scope.pageData.totalPages) {
		    		
		    		if (scope.setPageFn) {
		    			scope.setPageFn(pageNum);
		    			
		    		} else {
		    			scope.fetchResult(pageNum);
		    		}
		    	}
		    };
		    
		    scope.sortData = function(sortProperty, sortDir) {
		    	scope.fetchResult(0, sortProperty, sortDir);
		    };
		    
		    scope.fetchResult = function(pageNum, sortProperty, sortDir) {
		    	
		    	if (scope.fetchResultFn && scope.paginationData) {

		    		scope.paginationData.pageNum = pageNum;
		    		
		    		if (!isNullOrUndefined(sortProperty)) {
		    			scope.paginationData.sort = {
		    					field: sortProperty,
		    					direction: isNullOrUndefined(sortDir) ? 'ASC' : sortDir
		    			}
		    		}
		    		
		    		scope.fetchResultFn(scope.paginationData);
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