enablix.studioApp.factory('CorrelationService', 
	[
	 			'RESTService',
	 	function(RESTService) {
	 		
	 		var getCorrelatedItemTypeHierarchy = function(_sourceItemQId, _onSuccess, _onError) {
	 			
	 			var params = {
	 					sourceItemQId: _sourceItemQId
	 			};
	 			
	 			return RESTService.getForData("getCorrelatedItemTypeHierarchy", params, null, _onSuccess, _onError);
	 		};
				
	 		return {
	 			getCorrelatedItemTypeHierarchy : getCorrelatedItemTypeHierarchy
	 		};
	 	}
	 ]);