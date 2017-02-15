enablix.studioApp.directive('ebxGenericTable', [
        '$compile', 'orderByFilter',
function($compile,   orderByFilter) {

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
			paginationData: "=?",
			
			clientSort: "@" 
		},
		link: function(scope, element, attrs) {
			
			scope.rowActions = scope.rowActions || [];
			scope.clientSort = scope.clientSort || false;
			
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
		    	
		    	if (scope.clientSort) {
		    		
		    		var sortInfo = scope.pageData ? scope.pageData.sort : scope.sortInfo;
		    		
		    		sortInfo[0].property = sortProperty;
		    		
		    		var ascending = !(sortDir === 'DESC');
		    		sortInfo[0].ascending = ascending;
		    		
		    		var dirAndSortProp = (ascending ? "+" : "-") + sortProperty
		    		
		    		scope.tableData = orderByFilter(scope.tableData, dirAndSortProp);
		    		
		    	} else {
		    		scope.fetchResult(0, sortProperty, sortDir);
		    	}
		    	
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
		    
		    scope.callRowAction = function(action, dataRecord, $event, $index) {
		    	if (action.actionCallbackFn) {
		    		action.actionCallbackFn(dataRecord, $event, $index);
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
		    
		    scope.getTableCellClass = function(col, dataRecord) {
		    	if (isFunction(col.tableCellClass)) {
		    		return col.tableCellClass(dataRecord)
		    	}
		    	return isNullOrUndefined(col.tableCellClass) ? "" : col.tableCellClass; 
		    }
		    
		    scope.selectActionCallback = function(dataRecord, $event) {
		    	dataRecord._selected = !dataRecord._selected;
		    	if (scope.selectAction && scope.selectAction.actionCallbackFn) {
		    		scope.selectAction.actionCallbackFn(dataRecord, $event)
		    	}
		    }
		    
		    // set up sort info data
		    if (scope.clientSort && !scope.pageData) {
		    	scope.sortInfo = [{}]; // no sort info
		    }
		    
		},

		templateUrl: "widgets/directive/genericTable/genericTable.html"
	};
}]);