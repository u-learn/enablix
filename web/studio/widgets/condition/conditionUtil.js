enablix.studioApp.factory('ConditionUtil', 
	[
	 			'RESTService',
	 	function(RESTService) {
	 		
	 		var AND_TYPE = "AND";
	 		var OR_TYPE = "OR";
	 		var BASIC_TYPE = "BASIC";
	 		
	 		var andType = function() {
	 			return AND_TYPE;
	 		};
	 		
	 		var orType = function() {
	 			return OR_TYPE;
	 		};
	 		
	 		var basicType = function() {
	 			return BASIC_TYPE;
	 		};
	 		
	 		/**
	 		 * The callbackFn should be of type:
	 		 * 
	 		 * function(node, nodeType) {
	 		 *	  // logic
	 		 * }
	 		 * 
	 		 * where nodeType, is one of AND_TYPE, OR_TYPE or BASIC_TYPE
	 		 */
	 		var walkCondition = function(_conditionHolder, _callbackFn) {

	 			if (!isNullOrUndefined(_conditionHolder.andCondition)) {
	 				walkANDCondition(_conditionHolder.andCondition, _callbackFn);
	 			}
	 			
	 			if (!isNullOrUndefined(_conditionHolder.orCondition)) {
 					walkORCondition(_conditionHolder.orCondition, _callbackFn);
	 			}
	 			
	 			if (!isNullOrUndefined(_conditionHolder.basicCondition)) {
	 				_callbackFn(_conditionHolder.basicCondition, BASIC_TYPE);
	 			}
	 			
	 		};
	 		
	 		var walkGROUPCondition = function(_node, _callbackFn) {
	 			
	 			if (!isNullOrUndefined(_node.andCondition)) {
	 				angular.forEach(_node.andCondition, function(andCond) {
	 					walkANDCondition(andCond, _callbackFn);
	 				});
	 			}
	 			
	 			if (!isNullOrUndefined(_node.orCondition)) {
	 				angular.forEach(_node.orCondition, function(orCond) {
	 					walkANDCondition(orCond, _callbackFn);
	 				});
	 			}
	 			
	 			if (!isNullOrUndefined(_node.basicCondition)) {
	 				angular.forEach(_node.basicCondition, function(basicCond) {
	 					_callbackFn(basicCond, BASIC_TYPE);
	 				});
	 			}
	 		};
	 		
	 		var walkANDCondition = function(_andNode, _callbackFn) {
	 			_callbackFn(_andNode, AND_TYPE);
	 			walkGROUPCondition(_andNode, _callbackFn);
	 		};
	 		
	 		var walkORCondition = function(_orNode, _callbackFn) {
	 			_callbackFn(_orNode, OR_TYPE);
	 			walkGROUPCondition(_orNode, _callbackFn);
	 		};
				
	 		return {
	 			walkCondition : walkCondition,
	 			andType: andType,
	 			orType: orType,
	 			basicType: basicType
	 		};
	 	}
	 ]);