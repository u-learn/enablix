enablix.studioApp.directive('ebTable', [
        'StateUpdateService',
function(StateUpdateService) {

	return {
		restrict: 'E',
		scope : {
			tableData : "=",
			tableHeaders : "=",
			rowClickFn : "=",
			recordEditFn : "=",
			recordDeleteFn : "=",
			pageData : "=",
			setPageFn : "=",
			otherActions: "=",
			
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
			ebLayout: "@",
			containerQId: "@"
		},
		link: function(scope, element, attrs) {
			scope.navToRowDetail = function(elementIdentity) {
				if (scope.rowClickFn) {
					scope.rowClickFn(elementIdentity);
				}
			};
			
			scope.navToRecordEdit = function(elementIdentity) {
				if (scope.recordEditFn) {
					scope.recordEditFn(elementIdentity);
				}
			};
			
			scope.deleteTheRecord = function(elementIdentity) {
				if (scope.recordDeleteFn) {
					scope.recordDeleteFn(elementIdentity);
				}
			};
			
		    scope.setPage = function (pageNum) {
		    	if (scope.setPageFn && pageNum >=0 && pageNum < scope.pageData.totalPages) {
		    		scope.setPageFn(pageNum);
		    	} else {
		    		scope.fetchResult(pageNum);
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
		    
		    scope.callOtherAction = function(action, elementIdentity) {
		    	if (action.actionCallbackFn) {
		    		action.actionCallbackFn(elementIdentity);
		    	}
		    }

		    var layout = scope.ebLayout || "default";
		    scope.contentUrl = "widgets/directive/table/" + layout + "Table.html";
		    
		},

		template: '<div ng-include="contentUrl"></div>'
	};
}]);