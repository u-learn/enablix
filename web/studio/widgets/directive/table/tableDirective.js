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
			otherActions: "="
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
			
			scope.getPageNumbers = function (totalPages) {
		        var ret = [];
		        for (var i = 0; i < totalPages; i++) {
		        	ret.push(i);
		        }
		        return ret;
		    };
		    
		    scope.setPage = function (pageNum) {
		    	if (scope.setPageFn && pageNum >=0 && pageNum < scope.pageData.totalPages) {
		    		scope.setPageFn(pageNum);
		    	}
		    };
		    
		    scope.callOtherAction = function(action, elementIdentity) {
		    	if (action.actionCallbackFn) {
		    		action.actionCallbackFn(elementIdentity);
		    	}
		    }

		},

		templateUrl: "widgets/directive/table/defaultTable.html"
	};
}]);